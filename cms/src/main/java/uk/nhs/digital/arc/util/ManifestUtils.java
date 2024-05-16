package uk.nhs.digital.arc.util;

import uk.nhs.digital.arc.process.ProcessOutcome;
import uk.nhs.digital.arc.storage.ArcStorageManager;

import java.time.Instant;
import java.util.List;

public class ManifestUtils {
    /**
     * A docbase can be supplied as a fully qualified location or as a relative location.
     * If it's relative, we'll use the manifest location as teh docbase but if provided
     * fully qualified, we'll use the docbase directly
     *
     * @param documentBase         which may or may not be fully qualified URL
     * @param manifestFilePathData which will be a fully qualified location
     * @return a fully qualified location for the docbase
     */
    public static String getDocbaseUsingManifestLocationIfRequired(String documentBase, FilePathData manifestFilePathData) {
        FilePathData docbaseFilePathData = new FilePathData(documentBase);

        if (documentBase == null) {
            return null;
        }

        // If not a S3 reference (or HTTP or HTTPS) then assume a relative location
        if (!docbaseFilePathData.looksLikeAUrl()) {
            return "s3://" + manifestFilePathData.getS3Bucketname() + "/" + manifestFilePathData.getFolderPathNoBucket() + "/" + documentBase;
        }

        return documentBase;
    }

    public static void checkValidityOfUrls(ArcStorageManager storageManager, String docbase, List<String> allReferencedExternalUrls, ProcessOutcome processOutcome) {
        for (String referencedFile : allReferencedExternalUrls) {
            if (!storageManager.fileExists(new FilePathData(docbase, referencedFile))) {
                processOutcome.addIndentedErrorMessageLine("** Unable to find file '" + referencedFile + "' in the location you specified\n");
            } else {
                processOutcome.addIndentedMessageLine("Found the file '" + referencedFile + "' in the correct location\n");
            }
        }
    }

    public static String getOutputFileLocation(Instant now, String suffix) {
        String timestamp = now.toString().replaceAll("T", "_").replaceAll("Z", "");
        return String.format("%s_outcome_" + suffix + ".txt", timestamp);
    }
}
