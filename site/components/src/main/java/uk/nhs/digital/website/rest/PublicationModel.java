package uk.nhs.digital.website.rest;

/**
 * Represents an incoming data object for the publication API
 * Final form of this object will be much complex, containing information to create
 * multiple document types within the CMS.
 */
public class PublicationModel {
    private String path;
    private String title;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
