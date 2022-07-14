package uk.nhs.digital.externalstorage.workflow.externalFileIsPublished;

import uk.nhs.digital.ps.PublicationSystemConstants;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

public class ExternalFileIsPublishedCheck {
    public static boolean isPublication(Node variantNode) throws RepositoryException {
        return variantNode.isNodeType(PublicationSystemConstants.NODE_TYPE_PUBLICATION)
            || variantNode.isNodeType(PublicationSystemConstants.NODE_TYPE_LEGACY_PUBLICATION);
    }
}
