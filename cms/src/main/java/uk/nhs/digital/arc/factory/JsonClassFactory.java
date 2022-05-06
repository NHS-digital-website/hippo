package uk.nhs.digital.arc.factory;

import uk.nhs.digital.arc.json.ArcDoctype;
import uk.nhs.digital.arc.json.Archive;
import uk.nhs.digital.arc.json.Dataset;
import uk.nhs.digital.arc.json.Publication;
import uk.nhs.digital.arc.json.PublicationPage;
import uk.nhs.digital.arc.json.Series;

public class JsonClassFactory {
    public static Class<? extends ArcDoctype> getJsonDataClassFromDocumentType(final String documentType) throws Exception {
        if (documentType == null) {
            throw new Exception();
        }

        String workingType = documentType.toLowerCase();

        int index = workingType.indexOf("__");

        if (index >= 0) {
            workingType = workingType.substring(0, index);

        }

        switch (workingType) {
            case "publicationsystem:publicationpage":
                return PublicationPage.class;

            case "publicationsystem:publication":
                return Publication.class;

            case "publicationsystem:series":
                return Series.class;

            case "publicationsystem:dataset":
                return Dataset.class;

            case "publicationsystem:archive":
                return Archive.class;

            default:
                return null;
        }
    }


}
