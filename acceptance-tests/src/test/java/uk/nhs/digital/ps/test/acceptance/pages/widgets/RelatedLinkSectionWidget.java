package uk.nhs.digital.ps.test.acceptance.pages.widgets;

import static java.lang.String.format;

import org.openqa.selenium.WebElement;

public class RelatedLinkSectionWidget extends SectionWidget {
    public static final String UIPATH = "ps.publication.relatedlink-section";

    public RelatedLinkSectionWidget(final WebElement rootElement) {
        super(rootElement);
    }

    @Override
    protected String getUiPath() {
        return UIPATH;
    }

    public String getLinkText() {
        WebElement text = findElement("text");
        return text != null ? text.getText() : null;
    }

    public String getLinkUrl() {
        return findElement("text").getAttribute("href");
    }

    @Override
    public String toString() {
        return format("linkText=%s,linkUrl=%s",
            getLinkText(),
            getLinkUrl()
        );
    }
}
