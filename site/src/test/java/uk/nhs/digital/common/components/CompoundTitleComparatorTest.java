package uk.nhs.digital.common.components;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.Test;
import org.junit.runner.RunWith;
import uk.nhs.digital.website.beans.GlossaryItem;

import java.util.List;

@RunWith(DataProviderRunner.class)
public class CompoundTitleComparatorTest {

    @Test
    @UseDataProvider("comparatorTests")
    public void test(List<String> input, List<String> expected) {

        List<String> output = input.stream()
            .map(title -> {
                GlossaryItem glossaryItem = mock(GlossaryItem.class);
                given(glossaryItem.getTitle()).willReturn(title);
                return glossaryItem;
            })
            .sorted(CompoundTitleComparator.COMPARATOR)
            .map(GlossaryItem::getTitle)
            .collect(toList());

        assertEquals("List orders match", expected, output);

    }

    @DataProvider
    public static Object[][] comparatorTests() {

        return new List[][] {
            new List[] {
                asList("dog", "pig", "cat", "giraffe", "anaconda"),
                asList("anaconda", "cat", "dog", "giraffe", "pig")
            },
            new List[] {
                // ensure captialised entries are correctly sorted
                asList("adware", "Authentication", "AES", "random", "OTHER", "Anti-virus", "APT", "ACE"),
                asList("ACE", "adware", "AES", "Anti-virus", "APT", "Authentication", "OTHER", "random")
            }
        };

    }

}
