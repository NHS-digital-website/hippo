package uk.nhs.digital.arc.util;

public class FilePathData {

    private static final int S3_PROTO_LENGTH = 5;
    private static final int HTTP_PROTO_LENGTH = 7;
    private static final int HTTPS_PROTO_LENGTH = 8;

    private final String filePath;

    public FilePathData(String docbase, String filePath) {
        if (filePath.contains("://")) {
            this.filePath = filePath;
        } else {
            this.filePath = docbase + "/" + filePath;
        }
    }

    public FilePathData(String fullFilePath) {
        this.filePath = fullFilePath;
    }

    public boolean isS3Protocol() {
        return filePath != null && filePath.startsWith("s3://");
    }

    public boolean isHttpProtocol() {
        return filePath != null && filePath.startsWith("http://");
    }

    public boolean isHttpsProtocol() {
        return filePath != null && filePath.startsWith("https://");
    }

    public boolean looksLikeAUrl() {
        return isS3Protocol() || isHttpProtocol() || isHttpsProtocol();
    }

    public String getS3Bucketname() {
        if (isS3Protocol()) {
            String workPath = filePath.substring(S3_PROTO_LENGTH);
            return firstElement(workPath);
        }

        return null;
    }

    public String getFilename() {
        String[] elements = filePath.split("/");
        return elements != null && elements.length > 0 ? elements[elements.length - 1] : null;
    }

    private String firstElement(String workPath) {
        String[] elements = workPath.split("/");
        return elements != null && elements.length > 0 ? elements[0] : null;
    }

    public String getFilePathNoBucket() {
        return getFolderOrFilePathNoBucket(true);
    }

    public String getFolderPathNoBucket() {
        return getFolderOrFilePathNoBucket(false);
    }

    private String getFolderOrFilePathNoBucket(boolean wantFilename) {
        int offset = 0;
        String bucketName = "";

        if (isS3Protocol()) {
            bucketName = getS3Bucketname();
            offset = S3_PROTO_LENGTH + 1 + bucketName.length();
        }

        if (isHttpProtocol()) {
            offset = HTTP_PROTO_LENGTH;
        }

        if (isHttpsProtocol()) {
            offset = HTTPS_PROTO_LENGTH;
        }

        String workPath = filePath.substring(offset);

        if (wantFilename) {
            return workPath.substring(0, workPath.length());
        } else {
            String fileName = getFilename();
            return workPath.substring(0, workPath.length() - 1 - fileName.length());
        }
    }

    public String getFilePath() {
        return filePath;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof FilePathData) {
            FilePathData that = (FilePathData)o;

            if (that.filePath == null) {
                if (this.filePath == null) {
                    return true;
                }
            } else {
                return that.filePath.equals(this.filePath);
            }
        }

        return false;
    }

    @Override
    public int hashCode() {
        return (filePath == null) ? 0 : filePath.hashCode();
    }
}
