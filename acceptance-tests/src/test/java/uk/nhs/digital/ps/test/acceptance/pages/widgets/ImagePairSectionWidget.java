package uk.nhs.digital.ps.test.acceptance.pages.widgets;

import static java.lang.String.format;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.stream.Collectors;

public class ImagePairSectionWidget extends SectionWidget {
    public static final String UIPATH = "ps.publication.image-pair-section";

    public ImagePairSectionWidget(final WebElement rootElement) {
        super(rootElement);
    }

    @Override
    protected String getUiPath() {
        return UIPATH;
    }

    public List<ImageSectionWidget> getImageSectionWidgets() {
        return getRootElement()
            .findElements(By.xpath(".//*[@data-uipath='" + ImageSectionWidget.UIPATH + "']"))
            .stream()
            .map(ImageSectionWidget::new)
            .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return format("imageSections=%s",
            getImageSectionWidgets()
        );
    }
}
