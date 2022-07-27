package uk.nhs.digital.common.components.apispecification.commonmark;

import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.MockitoAnnotations.openMocks;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.commonmark.node.Heading;
import org.commonmark.node.Text;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import java.util.Map;

@RunWith(DataProviderRunner.class)
public class HeadingAttributeProviderTest {

    // Bit of an overkill to be using mock for a simple map
    // but it enables nice, fluent syntax for verifications.
    @Mock private Map<String, String> attributes;

    @Before
    public void setUp() {
        openMocks(this);
    }

    @Test
    @UseDataProvider("headingLevelsAndClasses")
    public void doesNotSetHeadingId_whenNoHeadingIdPrefixProvided(
        final int headingLevel,
        final String cssClass
    ) {

        // given
        final String headingText = " Heading Text ";
        final Heading headingNode = new Heading();
        headingNode.appendChild(new Text(headingText));
        headingNode.setLevel(headingLevel);

        final String noHeadingIdPrefix = null;

        final HeadingAttributeProvider headingAttributeProvider = new HeadingAttributeProvider(noHeadingIdPrefix);

        final String expectedId = "heading-text";

        // when
        headingAttributeProvider.setAttributes(headingNode, "tagName is ignored", attributes);

        // then
        then(attributes).should(never()).put("id", expectedId);
        then(attributes).should().put("class", cssClass);
        then(attributes).shouldHaveNoMoreInteractions();
    }

    @Test
    @UseDataProvider("headingLevelsAndClasses")
    public void setsHeadingId_withCustomHeadingIdPrefix_whenProvided(
        final int headingLevel,
        final String cssClass
    ) {

        // given
        final String headingText = " Heading Text ";
        final Heading headingNode = new Heading();
        headingNode.appendChild(new Text(headingText));
        headingNode.setLevel(headingLevel);

        final String customPrefix = "customPrefix__";

        final HeadingAttributeProvider headingAttributeProvider = new HeadingAttributeProvider(customPrefix);

        final String expectedHeadingId = "customPrefix__heading-text";

        // when
        headingAttributeProvider.setAttributes(headingNode, "tagName is ignored", attributes);

        // then
        then(attributes).should().put("id", expectedHeadingId);
        then(attributes).should().put("class", cssClass);
        then(attributes).shouldHaveNoMoreInteractions();
    }

    @DataProvider
    public static Object[][] headingLevelsAndClasses() {
        // @formatter:off
        return new Object[][]{
            // headingLevel                 cssClass
            {1,                             "nhsd-t-heading-xxl"},
            {2,                             "nhsd-t-heading-xl"},
            {3,                             "nhsd-t-heading-l"},
            {4,                             "nhsd-t-heading-m"},
            {5,                             "nhsd-t-heading-s"},
            {6,                             "nhsd-t-heading-xs"},
            {7,                             "nhsd-t-heading-xs"}

        };
        // @formatter:on
    }
}
