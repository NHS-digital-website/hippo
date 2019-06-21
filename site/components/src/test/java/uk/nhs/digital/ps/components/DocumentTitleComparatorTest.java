package uk.nhs.digital.ps.components;

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
import uk.nhs.digital.ps.beans.Dataset;

import java.util.List;

@RunWith(DataProviderRunner.class)
public class DocumentTitleComparatorTest {

    @Test
    @UseDataProvider("comparatorTests")
    public void test(List<String> input, List<String> expected) {
        List<String> output = input.stream()
            .map(title -> {
                Dataset dataset = mock(Dataset.class);
                given(dataset.getTitle()).willReturn(title);
                return dataset;
            })
            .sorted(DocumentTitleComparator.COMPARATOR)
            .map(Dataset::getTitle)
            .collect(toList());

        assertEquals("List orders match", expected, output);
    }

    @DataProvider
    public static Object[][] comparatorTests() {
        return new List[][] {
            new List[] {
                asList("zebra", "antelope", "hippo", "anteater", "ant"),
                asList("ant", "anteater", "antelope", "hippo", "zebra")
            },
            new List[] {
                asList("9.5", "11.6", "8.9"),
                asList("8.9", "9.5", "11.6")
            },
            new List[] {
                asList("1.11", "1.9", "1.10"),
                asList("1.9", "1.10", "1.11")
            },
            new List[] {
                asList("2.02", "1.0", "2", "1.01", "1", "2.0"),
                asList("1", "1.0", "1.01", "2", "2.0", "2.02")
            },
            new List[] {
                asList("ant", "1", "2", "1", "ant", "2"),
                asList("1", "1", "2", "2", "ant", "ant")
            },
            new List[] {
                asList("1.1", "1.0a", "1.0", "1.1a", "1.0b"),
                asList("1.0", "1.0a", "1.0b", "1.1", "1.1a")
            },
            new List[] {
                asList(
                    "1.9 ant",
                    "11.0.1 panther",
                    "hippo",
                    "10.2.1 lion",
                    "1.10 zebra",
                    "2.0.0 antelope",
                    "Hippo",
                    "1 hippo",
                    "1 anteater",
                    "101 cheater",
                    "zebra",
                    "1.10.1 hippo",
                    "10.0.9 elephant",
                    "Zebra",
                    "1 ant",
                    "10 tiger"
                ),
                asList(
                    "1 ant",
                    "1 anteater",
                    "1 hippo",
                    "1.9 ant",
                    "1.10 zebra",
                    "1.10.1 hippo",
                    "2.0.0 antelope",
                    "10 tiger",
                    "10.0.9 elephant",
                    "10.2.1 lion",
                    "11.0.1 panther",
                    "101 cheater",
                    "Hippo",
                    "Zebra",
                    "hippo",
                    "zebra"
                )
            }
        };
    }
}
