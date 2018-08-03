package uk.nhs.digital.ps.test.acceptance.steps.site.ps;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.slf4j.LoggerFactory.getLogger;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import org.hamcrest.Matcher;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import uk.nhs.digital.ps.test.acceptance.config.AcceptanceTestProperties;
import uk.nhs.digital.ps.test.acceptance.data.ExpectedTestDataProvider;
import uk.nhs.digital.ps.test.acceptance.data.TestDataRepo;
import uk.nhs.digital.ps.test.acceptance.models.*;
import uk.nhs.digital.ps.test.acceptance.models.section.BodySection;
import uk.nhs.digital.ps.test.acceptance.models.section.ImageSection;
import uk.nhs.digital.ps.test.acceptance.pages.site.SitePage;
import uk.nhs.digital.ps.test.acceptance.pages.site.ps.PublicationPage;
import uk.nhs.digital.ps.test.acceptance.pages.widgets.AttachmentWidget;
import uk.nhs.digital.ps.test.acceptance.pages.widgets.SectionWidget;
import uk.nhs.digital.ps.test.acceptance.steps.AbstractSpringSteps;
import uk.nhs.digital.ps.test.acceptance.util.FileHelper;

import java.util.List;
import java.util.Optional;

public class PublicationSteps extends AbstractSpringSteps {

    private static final Logger log = getLogger(PublicationSteps.class);

    @Autowired
    private TestDataRepo testDataRepo;

    @Autowired
    private AcceptanceTestProperties acceptanceTestProperties;

    @Autowired
    private PublicationPage publicationPage;

    @Autowired
    private SitePage sitePage;

    @Then(("^it is visible to consumers"))
    public void thenItIsVisibleToConsumers() throws Throwable {

        final Publication publication = testDataRepo.getCurrentPublication();

        publicationPage.open(publication);
        sitePage.clickCookieAcceptButton();

        thenICanSeeThePublicationHeader();

        assertThat("Publication summary is as expected", publicationPage.getSummaryText(),
            is(publication.getSummary()));

        thenICanSeeThePublicationPages();

        assertAttachmentsUpload(publication.getAttachments());
    }

    private void assertAttachmentsUpload(final List<Attachment> attachments) {

        assertThat("Expected number of attachments is displayed.",
            publicationPage.getAttachments().size(),
            is(attachments.size()));

        attachments.forEach(attachment -> {
            final AttachmentWidget attachmentWidget =
                publicationPage.findAttachmentElementByName(attachment.getFullName());

            assertDisplayedAttachmentDetails(attachment, attachmentWidget);
        });
    }

    private void assertDisplayedAttachmentDetails(final Attachment attachment, final AttachmentWidget
        attachmentWidget) {
        assertThat("Attachment " + attachment.getFullName() + " is displayed",
            attachmentWidget, is(notNullValue()));

        assertThat("Correct size of attachment " + attachment.getFullName() + " is displayed",
            attachmentWidget.getSizeText(),
            is("size: " + FileHelper.toHumanFriendlyFileSize((long) attachment.getContent().length)));
    }

    @Given("^I have a sectioned publication$")
    public void givenIHaveASectionedPublication() throws Throwable {
        PublicationBuilder sectionedPublication = ExpectedTestDataProvider.getSectionedPublication();
        testDataRepo.setPublication(sectionedPublication.build());
    }

    @Then("^I can see the publication pages$")
    public void thenICanSeeThePublicationPages() throws Throwable {
        Publication publication = testDataRepo.getCurrentPublication();

        List<String> pageTitles = publication.getPages()
            .stream()
            .map(Page::getTitle)
            .collect(toList());

        if (pageTitles.isEmpty()) {
            assertNull("No pages displayed", publicationPage.getPageTitles());
        } else {
            pageTitles.add(0, "Overview");
            assertThat("Publication pages are as expected", publicationPage.getPageTitles(), contains(pageTitles.toArray()));
        }
    }

    @Then("^I can see the first page body sections$")
    public void thenICanSeeTheFirstPageBodySections() throws Throwable {
        Publication publication = testDataRepo.getCurrentPublication();

        List<Matcher<? super SectionWidget>> matchers = publication.getPages().get(0).getBodySections()
            .stream()
            .map(BodySection::getMatcher)
            .collect(toList());

        if (matchers.isEmpty()) {
            assertThat("No body sections displayed", publicationPage.getPageBodySections(), empty());
        } else {
            assertThat("Body sections are as expected", publicationPage.getPageBodySections(), contains(matchers));
        }
    }

    @Then("^I can see the publication header$")
    public void thenICanSeeThePublicationHeader() throws Throwable {
        final Publication publication = testDataRepo.getCurrentPublication();

        assertThat("Publication title is as expected", sitePage.getDocumentTitle(),
            is(publication.getTitle()));

        assertThat("Geographic coverage is as expected", publicationPage.getGeographicCoverage(),
            is(Optional.ofNullable(publication.getGeographicCoverage()).map(GeographicCoverage::getDisplayValue).orElse(null)));

        assertThat("Publication information type is as expected", publicationPage.getInformationType(),
            is(Optional.ofNullable(publication.getInformationType()).map(InformationType::getDisplayName).orElse(null)));

        assertThat("Granularity is as expected", publicationPage.getGranularity(),
            is(Optional.ofNullable(publication.getGranularity()).map(Granularity::getDisplayValue).orElse(null)));
    }

    @Then("^I should see the key fact infographics$")
    public void thenIShouldSeeTheKeyFactInfographics() throws Throwable {
        final Publication publication = testDataRepo.getCurrentPublication();

        List<Matcher<? super SectionWidget>> matchers = publication.getKeyFactImages()
            .stream()
            .map(BodySection::getMatcher)
            .collect(toList());

        assertThat("Key fact images are as expected", publicationPage.getKeyFactImages(), contains(matchers));
    }
}
