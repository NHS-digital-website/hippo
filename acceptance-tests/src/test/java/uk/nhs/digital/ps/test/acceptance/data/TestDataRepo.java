package uk.nhs.digital.ps.test.acceptance.data;

import uk.nhs.digital.ps.test.acceptance.models.Attachment;
import uk.nhs.digital.ps.test.acceptance.models.Publication;

import java.util.List;

public class TestDataRepo {

    private Publication currentPublication;
    private List<Attachment> currentAttachments;

    public void setCurrentPublication(final Publication publication) {
        this.currentPublication = publication;
    }

    public Publication getCurrentPublication() {
        return currentPublication;
    }

    public void clear() {
        setCurrentPublication(null);
    }

    public void setCurrentAttachments(final List<Attachment> currentAttachments) {
        this.currentAttachments = currentAttachments;
    }

    public List<Attachment> getCurrentAttachments() {
        return currentAttachments;
    }
}
