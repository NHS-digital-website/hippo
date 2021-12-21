package uk.nhs.digital.arc.factory;

import uk.nhs.digital.arc.transformer.abs.AbstractPageLevelTransformer;
import uk.nhs.digital.arc.transformer.impl.pagelevel.ArchiveTransformer;
import uk.nhs.digital.arc.transformer.impl.pagelevel.DatasetTransformer;
import uk.nhs.digital.arc.transformer.impl.pagelevel.PublicationPageTransformer;
import uk.nhs.digital.arc.transformer.impl.pagelevel.PublicationTransformer;

public class ArcTransformerFactory {
    public static AbstractPageLevelTransformer getArcTransformerFromDocumentType(final String documentType) {
        String workingType = documentType.toLowerCase();

        int index = workingType.indexOf("__");

        if (index >= 0) {
            workingType = workingType.substring(0, index);

        }

        switch (workingType) {
            case "publicationsystem:publication":
                return new PublicationTransformer();

            case "publicationsystem:publicationpage":
                return new PublicationPageTransformer();

            case "publicationsystem:archive":
                return new ArchiveTransformer();

            case "publicationsystem:dataset":
                return new DatasetTransformer();

            default:
                return null;
        }
    }
}
