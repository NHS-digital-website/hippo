package uk.nhs.digital.ps.migrator.model.hippo;

import org.apache.tika.Tika;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.digital.ps.migrator.report.MigrationReport;
import uk.nhs.digital.ps.migrator.model.nesstar.PublishingPackage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Path;
import java.nio.file.Paths;

import static uk.nhs.digital.ps.migrator.report.IncidentType.ATTACHMENT_NOT_AVAILABLE;

public class Attachment {

    private static final Logger log = LoggerFactory.getLogger(Attachment.class);

    private static final int DOWNLOAD_CHUNK_SIZE = 1024 * 1024; // 1Mb
    private static final String INDICATORS_URL = "https://indicators.hscic.gov.uk";

    private final String title;
    private final String uri;
    private final Path attachmentDownloadDir;

    private final MigrationReport migrationReport;

    public Attachment(final Path attachmentDownloadDir, String title, String uri, final MigrationReport migrationReport) {
        this.attachmentDownloadDir = attachmentDownloadDir;
        this.title = title;
        this.uri = uri;
        this.migrationReport = migrationReport;
    }

    public String getTitle() {
        return title;
    }

    public String getUri() {
        return uri;
    }

    public String getFilePath() {
        return attachmentDownloadDir + getUri();
    }

    public String getFileName() {
        return Paths.get(getFilePath()).getFileName().toString();
    }

    public String getMimeType() {
        try {
            Tika tika = new Tika();
            return tika.detect(Paths.get(getFilePath()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean download(PublishingPackage publishingPackage) {

        File file = new File(getFilePath());
        // The URIs have spaces and we need to encode them or we get a 505 error response
        String encodedUri = INDICATORS_URL + getUri().trim().replace(" ", "%20");
        try {
            if (file.exists() && file.length() > 0) {
                log.debug("File {} already exists, not downloading.", getFilePath());
                return true;
            }

            file.getParentFile().mkdirs();
            file.createNewFile();

            try (FileOutputStream fos = new FileOutputStream(file)) {
                URL url = new URL(encodedUri);
                log.debug("Downloading attachment from: " + url);

                URLConnection connection = url.openConnection();
                ReadableByteChannel websiteChannel = Channels.newChannel(connection.getInputStream());
                long bytesExpected = connection.getContentLength();
                FileChannel fileChannel = fos.getChannel();

                long bytesRead = 0;
                while (bytesRead < bytesExpected) {
                    long read = fileChannel.transferFrom(websiteChannel, bytesRead, DOWNLOAD_CHUNK_SIZE);
                    bytesRead += read;
                    log.debug("Downloaded {} Mb / {} Mb", getMb(bytesRead), getMb(bytesExpected));
                }
            }
            return true;
        } catch (Exception e) {
            migrationReport.report(publishingPackage.getUniqueIdentifier(), ATTACHMENT_NOT_AVAILABLE, encodedUri);
            file.delete(); // Attempt to delete the file as it may be partially downloaded
            return false;
        }
    }

    private static float getMb(long bytes) {
        return bytes / (1024f * 1024);
    }
}
