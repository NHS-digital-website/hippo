package uk.nhs.digital.externalstorage.s3.valves;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.MockitoAnnotations.initMocks;
import static uk.nhs.digital.test.util.RandomHelper.newRandomByteArray;
import static uk.nhs.digital.test.util.RandomHelper.newRandomInt;
import static uk.nhs.digital.test.util.RandomHelper.newRandomString;

import org.hippoecm.hst.core.container.ValveContext;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import uk.nhs.digital.common.ServiceProvider;
import uk.nhs.digital.externalstorage.s3.PooledS3Connector;
import uk.nhs.digital.externalstorage.s3.S3File;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.function.Consumer;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class S3ConnectorValveTest {

    @Mock private ServiceProvider serviceProvider;
    @Mock private PooledS3Connector s3Connector;
    @Mock private HttpServletRequest request;
    @Mock private HttpServletResponse response;
    @Mock private HstRequestContext hstRequestContext;
    @Mock private ValveContext valveContext;
    @Mock private S3File s3File;

    @Captor private ArgumentCaptor<Consumer<S3File>> downloadTaskArgumentCaptor;

    private String expectedFileName;
    private long expectedFileLength;
    private byte[] expectedFileContent;
    private String s3ObjectPath;

    private ServletOutputStreamDouble userResponseOutputStream;
    private InputStreamDouble s3InputStream;

    private S3ConnectorValve s3ConnectorValve;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        // set up prerequsites for a happy path; error cases will re-set appropriate mocks to suit their needs:

        given(valveContext.getServletRequest()).willReturn(request);
        given(valveContext.getServletResponse()).willReturn(response);
        given(valveContext.getRequestContext()).willReturn(hstRequestContext);

        expectedFileName = newRandomString();
        expectedFileLength = newRandomInt();
        expectedFileContent = newRandomByteArray();
        s3ObjectPath = newRandomString();

        given(hstRequestContext.isCmsRequest()).willReturn(true);

        given(request.getParameter("s3Reference")).willReturn(s3ObjectPath);
        given(request.getParameter("fileName")).willReturn(expectedFileName);

        userResponseOutputStream = new ServletOutputStreamDouble();
        given(response.getOutputStream()).willReturn(userResponseOutputStream);

        s3InputStream = new InputStreamDouble(expectedFileContent);
        given(s3File.getContent()).willReturn(s3InputStream);
        given(s3File.getLength()).willReturn(expectedFileLength);

        given(serviceProvider.getService(PooledS3Connector.class)).willReturn(s3Connector);

        s3ConnectorValve = new S3ConnectorValve(serviceProvider);
    }

    @Test
    public void schedulesDownload() throws Exception {

        // given
        // prerequisites for the 'happy path' scenario prepared in the setUp method

        // when
        s3ConnectorValve.invoke(valveContext);

        // then
        then(s3Connector).should().download(eq(s3ObjectPath), downloadTaskArgumentCaptor.capture());
        downloadTaskArgumentCaptor.getValue().accept(s3File); // ensures that the download task actually gets called

        // verify response headers
        then(response).should().setContentType("application/octet-stream");
        then(response).should().setHeader("Content-Disposition", "attachment; filename=" + expectedFileName);
        then(response).should().setHeader("Content-Length", String.valueOf(expectedFileLength));
        then(response).should().setStatus(HttpServletResponse.SC_ACCEPTED);

        // verify response content (stream)
        assertThat("Content received from S3 connector is written to the output.",
            userResponseOutputStream.getContent(),
            is(expectedFileContent)
        );

        assertThat("Response output stream was closed once.", userResponseOutputStream.getClosedCount(), is(1));
        assertThat("S3 input stream was closed once.", s3InputStream.getClosedCount(), is(1));
    }

    @Test
    public void reportsBadRequest_onNotCmsRequest() throws Exception {

        // given
        given(hstRequestContext.isCmsRequest()).willReturn(false);

        // when
        s3ConnectorValve.invoke(valveContext);

        // then
        then(s3Connector).should().download(eq(s3ObjectPath), downloadTaskArgumentCaptor.capture());
        downloadTaskArgumentCaptor.getValue().accept(s3File); // ensures that the download task actually gets called

        verifyZeroInteractions(s3Connector);
        then(response).should().setStatus(HttpServletResponse.SC_FORBIDDEN);
    }

    @Test
    public void handlesS3RequestFailure() {

        // given
        final RuntimeException expectedConnecorException = new RuntimeException();

        doThrow(expectedConnecorException).when(s3Connector).download(anyString(), any());

        try {
            // when
            s3ConnectorValve.invoke(valveContext);

            // then
            fail("Expected an exception but none was thrown");
        } catch (final RuntimeException runtimeException) {
            assertThat("Exception is as thrown by the connector.",
                runtimeException, is(sameInstance(expectedConnecorException))
            );
        } catch (final Exception unexpectedException) {
            fail("Exception of unexpected type was thrown: " + unexpectedException);
        }
    }

    @Test
    public void handlesS3FileTransferFailure() {

        // given
        final RuntimeException expectedCauseException = new RuntimeException();

        s3InputStream.willThrowException(expectedCauseException);

        try {
            // when
            s3ConnectorValve.invoke(valveContext);

            // then
            then(s3Connector).should().download(eq(s3ObjectPath), downloadTaskArgumentCaptor.capture());
            downloadTaskArgumentCaptor.getValue().accept(s3File); // ensures that the download task actually gets called

            fail("Expected an exception but none was thrown");
        } catch (final RuntimeException runtimeException) {
            assertThat("Exception carries expected error message.",
                runtimeException.getMessage(),
                is("Failed to download content from S3: " + expectedFileName + ": " + s3ObjectPath)
            );
            assertThat("Exception carries expected cause exception.",
                runtimeException.getCause(),
                is(sameInstance(expectedCauseException))
            );
        } catch (final Exception unexpectedException) {
            fail("Exception of unexpected type was thrown: " + unexpectedException);
        }

        assertThat("Response output stream was closed once.", userResponseOutputStream.getClosedCount(), is(1));
        assertThat("S3 input stream was closed once.", s3InputStream.getClosedCount(), is(1));
    }


    class ServletOutputStreamDouble extends ServletOutputStream {

        private final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        private int closedCount = 0;

        @Override
        public void write(final int bytes) throws IOException {
            throw new UnsupportedOperationException("Not applicable in this test.");
        }

        @Override
        public void write(final byte[] bytes, final int off, final int len) throws IOException {
            byteArrayOutputStream.write(bytes, off, len);
        }

        @Override
        public void close() throws IOException {
            byteArrayOutputStream.close();
            closedCount++;
        }

        byte[] getContent() {
            return byteArrayOutputStream.toByteArray();
        }

        int getClosedCount() {
            return closedCount;
        }
    }

    class InputStreamDouble extends InputStream {

        private ByteArrayInputStream byteArrayInputStream;

        private int closedCount = 0;

        private RuntimeException expectedException;

        InputStreamDouble(final byte[] buffer) {
            byteArrayInputStream = new ByteArrayInputStream(buffer);
        }

        @Override
        public int read(final byte[] buffer) throws IOException {
            throwExceptionIfProgrammedTo();

            return byteArrayInputStream.read(buffer);
        }

        @Override
        public int read() throws IOException {
            throw new UnsupportedOperationException("Not applicable in this test.");
        }

        @Override
        public void close() throws IOException {
            byteArrayInputStream.close();
            closedCount++;
        }

        int getClosedCount() {
            return closedCount;
        }

        void willThrowException(final RuntimeException expectedException) {
            this.expectedException = expectedException;
        }

        private void throwExceptionIfProgrammedTo() {
            if (expectedException != null) {
                throw expectedException;
            }
        }
    }
}
