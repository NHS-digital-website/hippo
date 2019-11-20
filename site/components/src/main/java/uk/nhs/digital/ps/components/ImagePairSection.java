package uk.nhs.digital.ps.components;

import uk.nhs.digital.ps.beans.ImageSection;

public class ImagePairSection {
    private ImageSection first;
    private ImageSection second;

    ImagePairSection(ImageSection first) {
        this.first = first;
    }

    public String getSectionType() {
        return "imagePair";
    }

    public ImageSection getFirst() {
        return first;
    }

    public ImageSection getSecond() {
        return second;
    }

    public void setSecond(ImageSection second) {
        this.second = second;
    }
}
