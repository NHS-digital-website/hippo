package uk.nhs.digital.website.rest;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Represents an incoming data object for the publication API
 * Final form of this object will be much complex, containing information to create
 * multiple document types within the CMS.
 */
public class PublicationModel {

    @NotNull
    private String series;

    @NotNull
    @Size(min = 5, max = 200, message = "Title must be between 5 and 200 characters")
    private String title;

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
