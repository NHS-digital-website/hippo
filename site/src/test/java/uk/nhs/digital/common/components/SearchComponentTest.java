package uk.nhs.digital.common.components;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.MockitoAnnotations.initMocks;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.hippoecm.hst.core.container.ComponentManager;
import org.hippoecm.hst.site.HstServices;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.onehippo.cms7.essentials.components.paging.Pageable;

import java.util.List;


@RunWith(DataProviderRunner.class)
public class SearchComponentTest {

    private SearchComponent searchComponent;

    @Mock private ComponentManager compManager;


    @Before
    public void setUp() throws Exception {
        initMocks(this);

        HstServices.setComponentManager(compManager);

        searchComponent = new SearchComponent();

    }

    @Test
    @UseDataProvider("validInputsAndExpectedOutputs")
    public void formatValidSearchInputsToIncludeWildcardCharacters(final String queryInput, final String expectedQueryOutput)
        throws Exception {

        // given
        // setUp

        // when
        final String actualQueryOutput = searchComponent.parseAndApplyWildcards(queryInput);

        // then
        assertThat("Wildcard query is correct", actualQueryOutput, is(expectedQueryOutput));

    }

    @Test
    @UseDataProvider("pageableTests")
    public void pageableNumbersTest(long maxPages, int currentPage, List<Integer> expected) throws Exception {

        Pageable pageable = mock(Pageable.class);
        given(pageable.getTotalPages()).willReturn(maxPages);
        given(pageable.getCurrentPage()).willReturn(currentPage);

        // then
        assertEquals("Pageable numbers are as expected", expected, searchComponent.getPageNumbers(pageable));
    }

    @DataProvider
    public static Object[][] validInputsAndExpectedOutputs() {

        return new Object[][] {

            // queryInput                   expectedQueryOutput
            {"lorem",                       "lorem*"},
            {"lorem*",                      "lorem*"},
            {"lorem*?",                     "lorem*"},
            {"lorem*? ipsum",               "lorem* ipsum*"},
            {"lorem ipsum",                 "lorem* ipsum*"},
            {"lorem ipsum*",                "lorem* ipsum*"},
            {"lor* ipsum",                  "lor* ipsum*"},
            {"lorem ips*",                  "lorem* ips*"},
            {"lo ip",                       "lo ip"},
            {"lor ip",                      "lor* ip"},

            {"\"dolor sit\"",               "\"dolor sit\""},
            {"\"dolor sit\" ipsum",         "\"dolor sit\" ipsum*"},
            {"\"dolor sit\" ipsum*",        "\"dolor sit\" ipsum*"},
            {"lorem \"dolor sit\" ipsum",   "lorem* \"dolor sit\" ipsum*"},
            {"lorem ipsum \"dolor sit\"",   "lorem* ipsum* \"dolor sit\""},

            {"-lorem",                      "-lorem*"},
            {"lorem -ipsum",                "lorem* -ipsum*"},
            {"lor* -ipsum",                 "lor* -ipsum*"},
            {"\"dolor sit\" -\"lorem ipsum\"","\"dolor sit\" \"lorem ipsum\""},

            {"lorem OR ipsum",              "lorem* OR ipsum*"},
            {"lorem OR ipsum sit",          "lorem* OR ipsum* sit*"},
            {"OR lorem",                    "lorem*"},
            {"lorem OR ipsum \"dolor sit\"","lorem* OR ipsum* \"dolor sit\""},
            {"\"dolor sit\" lorem OR ipsum","\"dolor sit\" lorem* OR ipsum*"},

            {"elit.",                       "elit."},
            {"ante,",                       "ante,"},
            {"elit. lorem",                 "elit. lorem*"},
            {"elit. -lorem",                "elit. -lorem*"},
            {"-elit. lorem",                "-elit. lorem*"},
            {"elit. \"dolor sit\"",         "elit. \"dolor sit\""},

            // Check that odd numbers of quotes are sanitized
            {"elit \"dolor sit",         "elit* dolor* sit*"},
            {"elit \"dolor\" sit\"",     "elit* \"dolor\" sit*"}

        };
    }

    @DataProvider
    public static Object[][] pageableTests() {
        // max pages
        // current page
        // expected range

        return new Object[][] {
            new Object[]{
                2,
                1,
                asList(1, 2)
            },
            new Object[]{
                2,
                2,
                asList(1, 2)
            },
            new Object[]{
                5,
                1,
                asList(1, 2, 3, 4, 5)
            },
            new Object[]{
                5,
                5,
                asList(1, 2, 3, 4, 5)
            },
            new Object[]{
                6,
                1,
                asList(1, 2, 3, 4, 5)
            },
            new Object[]{
                6,
                3,
                asList(1, 2, 3, 4, 5)
            },
            new Object[]{
                6,
                4,
                asList(2, 3, 4, 5, 6)
            },
            new Object[]{
                6,
                6,
                asList(2, 3, 4, 5, 6)
            },
            new Object[]{
                57,
                23,
                asList(21, 22, 23, 24, 25)
            },
        };
    }
}
