package uk.nhs.digital.publicationautomation;

import java.util.List;

public class PublicationFilesResponse {
    private List<String> fileNames;

    private List<String> s3Urls;

    private List<String> errors;

    public PublicationFilesResponse() {
    }

    public List<String> getFileNames() {
        return fileNames;
    }

    public void setFileNames(List<String> fileNames) {
        this.fileNames = fileNames;
    }

    public List<String> getS3Urls() {
        return s3Urls;
    }

    public void setS3Urls(List<String> s3Urls) {
        this.s3Urls = s3Urls;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
}
