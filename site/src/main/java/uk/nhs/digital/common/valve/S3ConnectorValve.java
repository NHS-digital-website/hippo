package uk.nhs.digital.common.valve;

import org.apache.commons.io.IOUtils;
import org.hippoecm.hst.container.valves.AbstractOrderableValve;
import org.hippoecm.hst.core.container.ContainerException;
import org.hippoecm.hst.core.container.ValveContext;
import uk.nhs.digital.externalstorage.s3.SchedulingS3Connector;

import java.io.InputStream;
import java.io.OutputStream;
import javax.servlet.http.HttpServletResponse;

public class S3ConnectorValve extends AbstractOrderableValve {

    private final SchedulingS3Connector s3connector;

    public S3ConnectorValve(final SchedulingS3Connector s3connector) {
        this.s3connector = s3connector;
    }

    public void invoke(final ValveContext context) throws ContainerException {

        final String s3Reference = context.getServletRequest().getParameter("s3Reference");

        s3connector.scheduleDownload(s3Reference, s3File -> {

            OutputStream responseOutputStream = null;
            InputStream s3InputStream = null;
            String fileName = null;

            try {
                final HttpServletResponse response = context.getServletResponse();

                if (context.getRequestContext().isCmsRequest()) {

                    fileName = context.getServletRequest().getParameter("fileName");

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
        });
    }
}
