package uk.nhs.digital.common.components;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.hippoecm.hst.core.container.ComponentManager;
import org.hippoecm.hst.site.HstServices;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import static org.mockito.MockitoAnnotations.initMocks;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;


@RunWith(DataProviderRunner.class)
public class SearchComponentTest {

    private SearchComponent searchComponent;

    @Mock private ComponentManager compManager;

    @DataProvider
    public static Object[][] validInputsAndExpectedOutputs() {

        return new Object[][] {

            // queryInput                   expectedQueryOutput
            {"lorem",                       "lorem*"},
            {"lorem*",                      "lorem*"},
            {"lorem?",                      "lorem*"},
            {"lorem? ipsum",                "lorem* ipsum*"},
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
            {"\"dolor sit\" -\"lorem ipsum\"","\"dolor sit\" -\"lorem ipsum\""},

            {"lorem OR ipsum",              "lorem* OR ipsum*"},
            {"lorem OR ipsum sit",          "lorem* OR ipsum* sit*"},
            {"OR lorem",                    "OR lorem*"},
            {"lorem OR ipsum \"dolor sit\"","lorem* OR ipsum* \"dolor sit\""},
            {"\"dolor sit\" lorem OR ipsum","\"dolor sit\" lorem* OR ipsum*"},

            {"elit.",                       "elit."},
            {"ante,",                       "ante,"},
            {"elit. lorem",                 "elit. lorem*"},
            {"elit. -lorem",                "elit. -lorem*"},
            {"-elit. lorem",                "-elit. lorem*"},
            {"elit. \"dolor sit\"",         "elit. \"dolor sit\""},

        };
    }


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
        final String actualQueryOutput = searchComponent.applyWildcardsToQuery(queryInput);

        // then
        assertThat("Wildcard query is correct", actualQueryOutput, is(expectedQueryOutput));

    }

}
