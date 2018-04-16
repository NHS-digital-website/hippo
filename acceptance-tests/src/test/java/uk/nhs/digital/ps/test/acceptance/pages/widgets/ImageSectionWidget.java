package uk.nhs.digital.ps.test.acceptance.pages.widgets;

import static java.lang.String.format;

import org.openqa.selenium.WebElement;

public class ImageSectionWidget extends SectionWidget {
    public static final String UIPATH = "ps.publication.image-section";

    public ImageSectionWidget(final WebElement rootElement) {
        super(rootElement);
    }

    @Override
    protected String getUiPath() {
        return UIPATH;
    }

    public String getSource() {
        return findElement("image").getAttribute("src");
    }

    public String getAltText() {
        return findElement("image").getAttribute("alt");
    }

    public String getCaption() {
        WebElement caption = findElement("caption");
        return caption != null ? caption.getText() : null;
    }

    public String getLink() {
        WebElement link = findElement("link");
        return link != null ? link.getAttribute("href") : null;
    }

    @Override
    public String toString() {
        return format("src=%s,altText=%s,caption=%s,link=%s",
            getSource(),
            getAltText(),
            getCaption(),
            getLink()
        );
    }
}
