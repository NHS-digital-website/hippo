package uk.nhs.digital.ps.test.acceptance.data;

import static java.util.Arrays.asList;
import static uk.nhs.digital.ps.test.acceptance.data.TestDataRepo.PublicationClassifier.SINGLE;

import uk.nhs.digital.ps.test.acceptance.models.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestDataRepo {

    private Map<PublicationClassifier, List<Publication>> publications = new HashMap<>();

    private List<Attachment> attachments;
    private PublicationArchive publicationArchive;
    private PublicationSeries publicationSeries;
    private Dataset dataset;
    private Folder folder;

    public void setPublication(final Publication publication) {
        publications.clear();
        addPublications(SINGLE, publication);
    }

    public Publication getCurrentPublication() {
        List<Publication> publications = this.publications.get(SINGLE);
        return publications == null ? null : publications.get(0);
    }

    public void setAttachments(final List<Attachment> attachments) {
        this.attachments = attachments;
    }

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public void setPublicationSeries(final PublicationSeries publicationSeries) {
        this.publicationSeries = publicationSeries;
    }

    public PublicationSeries getPublicationSeries() {
        return publicationSeries;
    }

    public void setPublicationArchive(final PublicationArchive publicationArchive) {
        this.publicationArchive = publicationArchive;
    }

    public PublicationArchive getPublicationArchive() {
        return publicationArchive;
    }

    public void addPublications(final PublicationClassifier publicationClassifier, final Publication... publications) {
        addPublications(publicationClassifier, asList(publications));
    }

    public void addPublications(final PublicationClassifier publicationClassifier, final List<Publication> publications) {
        if (!this.publications.containsKey(publicationClassifier)) {
            this.publications.put(publicationClassifier, new ArrayList<>());
        }
        this.publications.get(publicationClassifier).addAll(publications);
    }

    public List<Publication> getPublications(final PublicationClassifier publicationClassifier) {
        return publications.get(publicationClassifier);
    }

    public void clear() {
        setPublication(null);
        publications.clear();
        dataset = null;
        publicationSeries = null;
        publicationArchive = null;
    }

    public Dataset getDataset() {
        return dataset;
    }

    public void setDataset(Dataset dataset) {
        this.dataset = dataset;
    }

    public Folder getFolder() {
        return folder;
    }

    public void setFolder(Folder folder) {
        this.folder = folder;
    }

    public enum PublicationClassifier {
        SINGLE,
        UPCOMING,
        LIVE;
    }
}
