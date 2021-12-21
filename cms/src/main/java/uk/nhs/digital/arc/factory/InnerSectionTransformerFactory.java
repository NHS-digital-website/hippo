package uk.nhs.digital.arc.factory;

import uk.nhs.digital.arc.json.PublicationBodyItem;
import uk.nhs.digital.arc.storage.ArcStorageManager;
import uk.nhs.digital.arc.transformer.abs.AbstractSectionTransformer;
import uk.nhs.digital.arc.transformer.impl.website.BodyItemSectionTransformer;
import uk.nhs.digital.arc.transformer.impl.website.EmphasisTransformer;
import uk.nhs.digital.arc.transformer.impl.website.InfographicTransformer;
import uk.nhs.digital.arc.transformer.impl.website.PublicationWebsiteSectionTransformer;
import uk.nhs.digital.arc.transformer.impl.website.WebsiteDynamicChartSectionTransformer;
import uk.nhs.digital.arc.transformer.impl.website.WebsiteIconListTransformer;
import uk.nhs.digital.arc.transformer.publicationsystem.PubSysChartsectionTransformer;
import uk.nhs.digital.arc.transformer.publicationsystem.PubSysImagesectionTransformer;
import uk.nhs.digital.arc.transformer.publicationsystem.PubSysTextsectionTransformer;

import javax.jcr.Session;

/**
 * This class is used to determine which of the various instances of the {@link uk.nhs.digital.arc.transformer.abs.AbstractTransformer} classes
 * we will use during the process of extracting data from a Json class and adding it to a content node
 *
 * @author Ian Pearce
 */
public class InnerSectionTransformerFactory {

    /**
     * Find the transformer that we need to use for a section. Sections are found in Publications and PublicationPage document types
     * @param section is an instance of the section, which is an ancestor of {@link PublicationBodyItem}
     * @param session the session is passed into the transformer and may be used within the transformer for a number of different operations
     * @return an instance of a transformer in the form of an {@link AbstractSectionTransformer}
     */
    public static AbstractSectionTransformer getTransformerFromSectionType(final PublicationBodyItem section,
                                                                           final Session session,
                                                                           final String docbase,
                                                                           final ArcStorageManager storageManager) {
        String sectionName = section.getClass().getSimpleName();
        if (null != sectionName) {
            if (sectionName.toLowerCase().startsWith(AbstractSectionTransformer.WEBSITE)) {
                return getWebsiteTransformerFromPublicationSectionType(section, session, docbase, storageManager);
            }

            if (sectionName.toLowerCase().startsWith(AbstractSectionTransformer.PUBLICATION_SYSTEM_NOCOLON)) {
                return getBodyItemTransformerFromPublicationPageSectionType(section, session, docbase, storageManager);
            }
        }
        return null;
    }

    /**
     * A {@link uk.nhs.digital.arc.json.Publication} contains {@link PublicationBodyItem} instances in an array. Each of those instances will require
     * a {@link uk.nhs.digital.arc.transformer.abs.AbstractSectionTransformer}  class to facilitate that extraction in order to add teh data to a ContentNode
     *
     * @param section is the section that has been found inside the PublicationPage. It is of type {@link PublicationBodyItem}
     * @param session is used as part of the initialisation of the transformer class
     * @return an instance of an {@link AbstractSectionTransformer} which wil extract the values and add to a content node
     */
    private static AbstractSectionTransformer getWebsiteTransformerFromPublicationSectionType(final PublicationBodyItem section,
                                                                                              final Session session,
                                                                                              final String docbase,
                                                                                              final ArcStorageManager storageManager) {
        switch (section.getClass().getSimpleName().toLowerCase()) {
            case "websitesection":
                return new PublicationWebsiteSectionTransformer(session, section, docbase, storageManager);

            case "websiteinfographic":
                return new InfographicTransformer(session, section, docbase, storageManager);

            case "websiteemphasis":
                return new EmphasisTransformer(session, section, docbase, storageManager);

            case "websitedynamicchartsection":
                return new WebsiteDynamicChartSectionTransformer(session, section, docbase, storageManager);

            case "websiteiconlist":
                return new WebsiteIconListTransformer(session, section, docbase, storageManager);

            default:
                return null;
        }
    }

    /**
     * A {@link uk.nhs.digital.arc.json.PublicationPage} has a number of {@link PublicationBodyItem} instances in an array. Each of those instances will require
     * a {@link uk.nhs.digital.arc.transformer.abs.AbstractSectionTransformer}  class to facilitate that extraction in order to add teh data to a ContentNode
     *
     * @param section is the section that has been found inside the PublicationPage. It is of type {@link PublicationBodyItem}
     * @param session is used as part of the initialisation of the transformer class
     * @return an instance of an {@link AbstractSectionTransformer} which wil extract the values and add to a content node
     */
    private static AbstractSectionTransformer getBodyItemTransformerFromPublicationPageSectionType(final PublicationBodyItem section,
                                                                                                   final Session session,
                                                                                                   final String docbase,
                                                                                                   final ArcStorageManager storageManager) {
        // Note: As more sections are required to be converted (and thus more Transformer classes), then more
        // clauses will be need in the switch statement below
        switch (section.getClass().getSimpleName().toLowerCase()) {
            case "publicationsystemchartsection":
                return new PubSysChartsectionTransformer(session, section, docbase, storageManager);

            case "websitesection":
                return new BodyItemSectionTransformer(session, section, docbase, storageManager);

            case "publicationsystemimagesection":
                return new PubSysImagesectionTransformer(session, section, docbase, storageManager);

            case "publicationsystemtextsection":
                return new PubSysTextsectionTransformer(session, section, docbase, storageManager);

            default:
                return null;
        }
    }
}
