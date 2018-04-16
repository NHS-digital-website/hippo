package uk.nhs.digital.ps.test.acceptance.pages.widgets;

import static java.lang.String.format;

import org.openqa.selenium.WebElement;

public class TextSectionWidget extends SectionWidget {
    public static final String UIPATH = "ps.publication.text-section";

    public TextSectionWidget(final WebElement rootElement) {
        super(rootElement);
    }

    @Override
    protected String getUiPath() {
        return UIPATH;
    }

    public String getHeading() {
        WebElement heading = findElement("heading");
        return heading != null ? heading.getText() : null;
    }

    public String getText() {
        WebElement text = findElement("text");
        return text != null ? text.getText() : null;
    }

    @Override
    public String toString() {
        return format("heading=%s,text=%s",
            getHeading(),
            getText()
        );
    }
}
