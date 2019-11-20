package uk.nhs.digital.externalstorage.s3.valves;

import org.apache.commons.io.IOUtils;
import org.hippoecm.hst.container.valves.AbstractOrderableValve;
import org.hippoecm.hst.core.container.ContainerException;
import org.hippoecm.hst.core.container.ValveContext;
import uk.nhs.digital.common.ServiceProvider;
import uk.nhs.digital.externalstorage.s3.PooledS3Connector;

import java.io.InputStream;
import java.io.OutputStream;
import javax.servlet.http.HttpServletResponse;

public class S3ConnectorValve extends AbstractOrderableValve {

    private final ServiceProvider serviceProvider;

    public S3ConnectorValve(final ServiceProvider serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    public void invoke(final ValveContext context) throws ContainerException {

        // We're retrieving the service at the last possible moment to ensure that
        // we always use the latest instance of it as the service may ge re-registered
        // without restarting the application due to config changes in the Console.
        final PooledS3Connector s3Connector = serviceProvider.getService(PooledS3Connector.class);

        final String s3Reference = context.getServletRequest().getParameter("s3Reference");

        s3Connector.download(s3Reference, s3File -> {

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
