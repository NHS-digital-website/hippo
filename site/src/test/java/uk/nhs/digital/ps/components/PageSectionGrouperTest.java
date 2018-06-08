package uk.nhs.digital.ps.components;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static uk.nhs.digital.ps.beans.ImageSection.Size.FULL;
import static uk.nhs.digital.ps.beans.ImageSection.Size.HALF;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.hamcrest.Matcher;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import uk.nhs.digital.ps.beans.ImageSection;
import uk.nhs.digital.ps.beans.TextSection;

import java.util.List;

@RunWith(DataProviderRunner.class)
public class PageSectionGrouperTest {

    private PageSectionGrouper pageSectionGrouper = new PageSectionGrouper();

    @Test
    @UseDataProvider("pageSectionData")
    public void getPageSectionsTest(String testDesc, List<HippoBean> input, List<Object> expected) {
        // when
        List<Object> pageSections = pageSectionGrouper.groupSections(input);

        // then
        assertThat(testDesc, pageSections, matchesSections(expected));
    }

    @DataProvider
    public static Object[][] pageSectionData() {
        // list of sections so each can be reused in expected and input lists
        ImageSection[] halfImage = new ImageSection[] {mockImage(HALF), mockImage(HALF), mockImage(HALF), mockImage(HALF), mockImage(HALF)};
        ImageSection[] fullImage = new ImageSection[] {mockImage(FULL), mockImage(FULL), mockImage(FULL), mockImage(FULL)};
        ImageSection[] nullSizeImage = new ImageSection[] {mockImage(null), mockImage(null)};
        TextSection[] text = new TextSection[] {new TextSection(), new TextSection(), new TextSection(), new TextSection()};

        return new Object[][] {
            new Object[] {
                "Half image sections are correctly grouped",
                asList(halfImage[0], halfImage[1]),
                singletonList(newImagePair(halfImage[0], halfImage[1]))
            },
            new Object[] {
                "Non image sections are not grouped",
                asList(text[0], text[1]),
                asList(text[0], text[1])
            },
            new Object[] {
                "Single half image sections are grouped without a second image",
                asList(halfImage[0], text[1], halfImage[2]),
                asList(newImagePair(halfImage[0], null), text[1], newImagePair(halfImage[2], null))
            },
            new Object[] {
                "Full image sections are not grouped",
                asList(fullImage[0], fullImage[1]),
                asList(fullImage[0], fullImage[1])
            },
            new Object[] {
                "Full image sections are not grouped with half image sections",
                asList(halfImage[0], fullImage[1], halfImage[2]),
                asList(newImagePair(halfImage[0], null), fullImage[1], newImagePair(halfImage[2], null))
            },
            new Object[] {
                "Multiple half image sections are grouped into different pairs",
                asList(halfImage[0], halfImage[1], halfImage[2], halfImage[3], halfImage[4]),
                asList(newImagePair(halfImage[0], halfImage[1]), newImagePair(halfImage[2], halfImage[3]), newImagePair(halfImage[4], null))
            },
            new Object[] {
                "Null size doesn't get grouped",
                asList(nullSizeImage[0], nullSizeImage[1]),
                asList(nullSizeImage[0], nullSizeImage[1])
            },
            new Object[] {
                "List of sections are grouped as expected",
                asList(halfImage[0], fullImage[0], fullImage[1], halfImage[1], halfImage[2], halfImage[3], text[0], fullImage[2]),
                asList(newImagePair(halfImage[0], null), fullImage[0], fullImage[1], newImagePair(halfImage[1], halfImage[2]),
                    newImagePair(halfImage[3], null), text[0], fullImage[2])
            }
        };
    }

    private static ImageSection mockImage(ImageSection.Size size) {
        ImageSection imageSection = mock(ImageSection.class);
        given(imageSection.getSize()).willReturn(size);
        return imageSection;
    }

    private static ImagePairSection newImagePair(ImageSection imageSection1, ImageSection imageSection2) {
        ImagePairSection imagePairSection = new ImagePairSection(imageSection1);
        imagePairSection.setSecond(imageSection2);
        return imagePairSection;
    }

    private Matcher<List<Object>> matchesSections(List<Object> expected) {
        Matcher[] matchers = expected.stream()
            .map(section -> section instanceof ImagePairSection ? samePropertyValuesAs(section) : is(section))
            .toArray(Matcher[]::new);

        return contains(matchers);
    }
}
