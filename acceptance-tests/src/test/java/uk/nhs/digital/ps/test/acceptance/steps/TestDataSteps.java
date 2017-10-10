package uk.nhs.digital.ps.test.acceptance.steps;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import org.openqa.selenium.By;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import uk.nhs.digital.ps.test.acceptance.models.Publication;
import uk.nhs.digital.ps.test.acceptance.data.TestDataRepo;
import uk.nhs.digital.ps.test.acceptance.models.TestDataFactory;
import uk.nhs.digital.ps.test.acceptance.pages.ContentPage;
import uk.nhs.digital.ps.test.acceptance.pages.PageHelper;

import static org.slf4j.LoggerFactory.getLogger;

public class TestDataSteps extends AbstractSpringSteps {

    private final static Logger log = getLogger(TestDataSteps.class);

    @Autowired
    private LoginSteps loginSteps;

    @Autowired
    private ContentPage contentPage;

    @Autowired
    private TestDataRepo testDataRepo;

    @Autowired
    private PageHelper pageHelper;

    @Given("^I have a publication opened for editing$")
    public void iHaveAPublicationOpenForEditing() throws Throwable {
        final Publication publication = TestDataFactory.createPublicationWithNoAttachments().build();
        testDataRepo.setCurrentPublication(publication);

        loginSteps.givenIAmLoggedInAsAdmin();
        contentPage.openContentTab();
        contentPage.newPublication(publication);
        contentPage.populatePublication(publication);
    }


    /**
     * Resets the test data repository before every scenario to prevent data leaking between scenarios, unless given
     * scenario is tagged with {@code @NeedsExistingTestData}.
     */
    @Before(value = "~@NeedsExistingTestData")
    public void clearTestData() {
        log.debug("Disposing of test data.");
        testDataRepo.clear();
    }

    /**
     * <p>
     * Clicks the 'Save & Close' button, thus closing the publication to ensure that the next time the same user logs
     * into the system, they don't get immediately taken into this publication's edit screen.
     * </p><p>
     * Only applicable to scenarios that leave the document in editable state; implemented as an {@linkplain After}
     * hook rather than a step because this is really a test cleanup/tear-down activity that doesn't warrant expressing
     * explicitly as a scenario step.
     * </p><p>
     * To ensure that this method gets called at the end of your scenario, tag the scenario with
     * {@code @EndWithSaveAndClose}.
     * </p>
     */
    @After(value = "@EndWithSaveAndClose", order = 500)
    public void saveAndCloseEditedPublication() throws Throwable {
        log.debug("Saving and closing current publication.");

        // The logout and login sequence is a workaround to a bug in Hippo CMS, where a rejection of a file upload
        // in the edit screen causes the 'Save & Close' button to stop working.
        contentPage.logout();
        loginSteps.givenIAmLoggedInAsAdmin();

        contentPage.saveAndClosePublication();

        waitUntilSaved();
    }

    private void waitUntilSaved() {
        // We're looking for 'Edit' button to appear as it shows when a document gets saved and is absent
        // when the document is being edited.
        pageHelper.findElement(By.cssSelector("span[class~='menu-action-text'] > span[title='Edit']"));
    }
}
