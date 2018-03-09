package uk.nhs.digital.externalstorage.s3;

import com.amazonaws.services.s3.model.S3Object;

import java.io.InputStream;

public class S3FileProxy implements S3File {

    private S3Object s3Object;

    S3FileProxy(final S3Object s3Object) {
        this.s3Object = s3Object;
    }

    @Override
    public long getLength() {
        return s3Object.getObjectMetadata().getContentLength();
    }

    @Override
    public InputStream getContent() {
        return s3Object.getObjectContent();
    }
}
