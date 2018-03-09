package uk.nhs.digital.common.valve;

import org.apache.commons.io.IOUtils;
import org.hippoecm.hst.container.valves.AbstractOrderableValve;
import org.hippoecm.hst.core.container.ContainerException;
import org.hippoecm.hst.core.container.ValveContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.digital.externalstorage.s3.S3Connector;
import uk.nhs.digital.externalstorage.s3.S3File;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import javax.servlet.http.HttpServletResponse;

public class S3ConnectorValve extends AbstractOrderableValve {

    private static final Logger log = LoggerFactory.getLogger(uk.nhs.digital.common.valve.S3ConnectorValve.class);

    private static final int DOWNLOAD_SHUTDOWN_TIMEOUT = 30;

    private final ExecutorService executorService;
    private final S3Connector s3Connector;

    public S3ConnectorValve(final ExecutorService executorService,
                            final S3Connector s3Connector
    ) {
        this.executorService = executorService;
        this.s3Connector = s3Connector;
    }

    public void invoke(final ValveContext context) throws ContainerException {

        try {
            schedule(() -> download(context));
        } catch (ContainerException ex) {
            log.error("Download failure.", ex);
            throw ex;
        }
    }

    private void schedule(final Runnable downloadTask) throws ContainerException {

        try {
            executorService.submit(downloadTask).get();

        } catch (final Exception ex) {
            throw new ContainerException("Failed to download from S3.", ex);
        }
    }

    private void download(final ValveContext context) {

        InputStream s3InputStream = null;
        OutputStream responseOutputStream = null;

        String s3Reference = null;
        String fileName = null;

        try {
            final HttpServletResponse response = context.getServletResponse();

            if (context.getRequestContext().isCmsRequest()) {

                s3Reference = context.getServletRequest().getParameter("s3Reference");
                fileName = context.getServletRequest().getParameter("fileName");

                final S3File s3File = s3Connector.getFile(s3Reference);

                response.setContentType("application/octet-stream");
                response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
                response.setHeader("Content-Length", String.valueOf(s3File.getLength()));
                response.setStatus(HttpServletResponse.SC_ACCEPTED);

                s3InputStream = s3File.getContent();

                responseOutputStream = response.getOutputStream();
                IOUtils.copyLarge(s3InputStream, responseOutputStream);
            } else {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            }
        } catch (final Exception ex) {
            throw new RuntimeException("Failed to download content from S3: " + fileName + ": " + s3Reference, ex);

        } finally {
            IOUtils.closeQuietly(responseOutputStream);
            IOUtils.closeQuietly(s3InputStream);
        }
    }

    @Override
    public void destroy() {
        try {
            executorService.shutdown();

            if (!executorService.isTerminated()) {
                log.info("At least one S3 download is still in progress; waiting for it to finish.");
            }

            // We're blocking the application shutdown until all files finish downloading or the timeout occur,
            // whichever comes first.
            //
            // Most files are small enough that it's likely they'll finish downloading within 30 seconds.
            // Terminating a longer running download is not a problem, the user will simply re-download the content
            // after logging back in but at least we try to gracefully handle most common cases.
            //
            final boolean abortedDownloadsInProgress = !executorService.awaitTermination(DOWNLOAD_SHUTDOWN_TIMEOUT, TimeUnit.SECONDS);
            if (abortedDownloadsInProgress) {
                log.warn("At least one in-progress S3 download has been forcefully terminated due to application shutdown.");
            }

        } catch (final InterruptedException ie) {
            throw new RuntimeException("Interrupted when shutting down S3 download executor service.", ie);
        }
    }
}
