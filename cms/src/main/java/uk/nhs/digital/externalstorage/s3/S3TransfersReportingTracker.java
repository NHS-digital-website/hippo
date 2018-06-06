package uk.nhs.digital.externalstorage.s3;

import static java.text.MessageFormat.format;
import static org.slf4j.LoggerFactory.getLogger;
import static uk.nhs.digital.externalstorage.s3.S3TransfersReportingTracker.TransferEvent.*;
import static uk.nhs.digital.externalstorage.s3.S3TransfersReportingTracker.TransferType.*;

import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;

import java.util.concurrent.TimeUnit;

/**
 * <p>
 * Tracks number of S3 transfers and logs those and other stats. Tracking is done
 * in a thread-safe manner as the transfers can be performed concurrently.
 * </p><p>
 * Introduced so that the tracking and logging plumbing doesn't pollute the class actually
 * performing the transfers.
 * </p>
 */
public class S3TransfersReportingTracker {

    private final Logger log = getLogger(S3TransfersReportingTracker.class);

    private final TransfersCounter transfersCounter;

    public S3TransfersReportingTracker() {
        transfersCounter = new TransfersCounter(log);
    }

    //<editor-fold desc="Uploads">

    StopWatch reportUploadScheduling(final String fileName) {
        return reportTransferInitialising(SCHEDULING, UPLOAD, fileName,
            transfersCounter.incrementUploadsScheduledAndGet()
        );
    }

    void reportUploadStarting(final String fileName) {
        reportTransferInitialising(STARTING, UPLOAD, fileName,
            transfersCounter.incrementUploadsActiveAndGet()
        );
    }

    void reportUploadStopped(final StopWatch stopWatch,
                             final String s3FileReference,
                             final long s3FileSizeInBytes
    ) {
        reportTransferCompleted(
            UPLOAD,
            s3FileSizeInBytes,
            stopWatch.getTime(TimeUnit.SECONDS),
            s3FileReference,
            transfersCounter.decrementUploadsAndGet()
        );
    }

    void reportUploadFailed(final String fileName, final Throwable throwable) {
        reportTransferFailed(UPLOAD, fileName, transfersCounter.decrementUploadsAndGet(), throwable);
    }

    //</editor-fold>

    //<editor-fold desc="Downloads">

    StopWatch reportDownloadScheduling(final String s3FileReference) {
        return reportTransferInitialising(SCHEDULING, DOWNLOAD, s3FileReference,
            transfersCounter.incrementDownloadsScheduledAndGet()
        );
    }

    void reportDownloadStarting(final String s3FileReference) {
        reportTransferInitialising(STARTING, DOWNLOAD, s3FileReference,
            transfersCounter.incrementDownloadsActiveAndGet()
        );
    }

    void reportDownloadStopped(final StopWatch stopWatch,
                               final String s3FileReference,
                               final long s3FileSizeInBytes
    ) {
        reportTransferCompleted(
            DOWNLOAD,
            s3FileSizeInBytes,
            stopWatch.getTime(TimeUnit.SECONDS),
            s3FileReference,
            transfersCounter.decrementDownloadsAndGet()
        );
    }

    void reportDownloadFailed(final String s3FileReference, final Throwable throwable) {
        reportTransferFailed(DOWNLOAD, s3FileReference, transfersCounter.decrementDownloadsAndGet(), throwable);
    }

    //</editor-fold>

    void reportAction(final String messageTemplate, final Object... args) {
        log.info(messageTemplate, args);
    }

    void reportError(final String message, final Throwable throwable) {
        log.error(message, throwable);
    }

    private StopWatch reportTransferInitialising(final TransferEvent transferEvent,
                                                 final TransferType transferType,
                                                 final String s3FileReference,
                                                 final TransferCounts transferCounts
    ) {
        // Example:
        // Scheduling S3 download of A4/903E79/file-1MB.zip; scheduled/waiting/active: up: 0/0/0, down: 1/1/0,  total: 1/1/0
        reportAction(format("{0} S3 {1} of {2}; {3}",
            transferEvent,
            transferType,
            s3FileReference,
            getTotalStatsMessage(transferCounts)
        ));

        return StopWatch.createStarted();
    }

    private void reportTransferCompleted(final TransferType transferType,
                                         final long sizeInBytes,
                                         final long durationInSecs,
                                         final String s3FileReference,
                                         final TransferCounts transferCounts
    ) {
        // Example:
        // Completed S3 download of A4/903E79/file-1MB.zip; 1.0 MB in 1s at 1.0 MB/s; scheduled/waiting/active: up: 0/0/0, down: 0/0/0,  total: 0/0/0
        reportAction(format("Completed S3 {0} of {1}; {2} in {3}s at {4}/s; {5}",
            transferType,
            s3FileReference,
            toHumanFriendlyFileSize(sizeInBytes),
            durationInSecs,
            toHumanFriendlyFileSize(durationInSecs > 0 ? sizeInBytes / durationInSecs : 0),
            getTotalStatsMessage(transferCounts)
        ));
    }

    private void reportTransferFailed(final TransferType transferType,
                                      final String s3FileReference,
                                      final TransferCounts transferCounts,
                                      final Throwable throwable) {
        // Example:
        // S3 download has failed for A4/903E79/file-1MB.zip; scheduled/waiting/active: up: 0/0/0, down: 0/0/0,  total: 0/0/0
        reportError(
            format(
                "S3 {0} has failed for {1}; {2}",
                transferType,
                s3FileReference,
                getTotalStatsMessage(transferCounts)
            ),
            throwable
        );
    }

    private String getTotalStatsMessage(final TransferCounts transferCounts) {
        return format(
            "scheduled/waiting/active: up: {0}/{1}/{2}, down: {3}/{4}/{5},  total: {6}/{7}/{8}",
            transferCounts.getUploadsScheduled(),
            transferCounts.getWaitingUploads(),
            transferCounts.getUploadsActive(),
            transferCounts.getDownloadsScheduled(),
            transferCounts.getWaitingDownloads(),
            transferCounts.getDownloadsActive(),
            transferCounts.getTotalScheduled(),
            transferCounts.getTotalWaiting(),
            transferCounts.getTotalActive()
        );
    }

    private String toHumanFriendlyFileSize(final long bytesCount) {

        // Modified version of http://programming.guide/java/formatting-byte-size-to-human-readable-format.html
        // to only support decimal units.

        final int unit = 1000;
        if (bytesCount < unit) {
            return bytesCount + " B";
        }
        final int exp = (int) (Math.log(bytesCount) / Math.log(unit));
        final String pre = String.valueOf("kMGTPE".charAt(exp - 1));
        return String.format("%.1f %sB", bytesCount / Math.pow(unit, exp), pre);
    }

    /**
     * Ensures thread-safe access to both counts in a multi-threaded use;
     * be mindful of correct synchronisation of the public methods if changing.
     */
    private static class TransfersCounter {

        private final Logger log;

        private volatile int uploadsActiveCount = 0;
        private volatile int uploadsScheduledCount = 0;

        private volatile int downloadsActiveCount = 0;
        private volatile int downloadsScheduledCount = 0;

        private TransfersCounter(Logger log) {
            this.log = log;
        }

        //<editor-fold desc="Uploads">

        synchronized TransferCounts incrementUploadsScheduledAndGet() {
            uploadsScheduledCount++;

            return newTransfersCountsSnapshot();
        }

        synchronized TransferCounts incrementUploadsActiveAndGet() {
            uploadsActiveCount++;

            return newTransfersCountsSnapshot();
        }

        synchronized TransferCounts decrementUploadsAndGet() {
            // Reaching any of the else/log.error statements below means, we have a
            // bug in the caller code, most likely failing to catch some exception
            // which prevented corresponding increment to take place.
            // Rare and unlikely event that only affects accuracy of logs so no need
            // to fail the request through exception.

            if (uploadsScheduledCount > 0) {
                uploadsScheduledCount--;
            } else {
                log.error("Count of scheduled uploads is already 0; won't decrement.");
            }

            if (uploadsActiveCount > 0) {
                uploadsActiveCount--;
            } else {
                log.error("Count of active uploads is already 0; won't decrement.");
            }

            return newTransfersCountsSnapshot();
        }

        //</editor-fold>

        //<editor-fold desc="Downloads">

        synchronized TransferCounts incrementDownloadsScheduledAndGet() {
            downloadsScheduledCount++;

            return newTransfersCountsSnapshot();
        }

        synchronized TransferCounts incrementDownloadsActiveAndGet() {
            downloadsActiveCount++;

            return newTransfersCountsSnapshot();
        }

        synchronized TransferCounts decrementDownloadsAndGet() {
            // Reaching any of the else/log.error statements below means, we have a
            // bug in the caller code, most likely failing to catch some exception
            // which prevented corresponding increment to take place.
            // Rare and unlikely event that only affects accuracy of logs so no need
            // to fail the request through exception.

            if (downloadsScheduledCount > 0) {
                downloadsScheduledCount--;
            } else {
                log.error("Count of scheduled downloads is already 0; won't decrement.");
            }

            if (downloadsActiveCount > 0) {
                downloadsActiveCount--;
            } else {
                log.error("Count of active downloads is already 0; won't decrement.");
            }

            return newTransfersCountsSnapshot();
        }

        //</editor-fold>

        private TransferCounts newTransfersCountsSnapshot() {
            return new TransferCounts(
                uploadsScheduledCount, uploadsActiveCount,
                downloadsScheduledCount, downloadsActiveCount
            );
        }
    }

    private static class TransferCounts {

        private final int uploadsScheduled;
        private final int uploadsActive;

        private final int downloadsScheduled;
        private final int downloadsActive;

        TransferCounts(final int uploadsScheduled, final int uploadsActive,
                       final int downloadsScheduled, final int downloadsActive
        ) {
            this.uploadsScheduled = uploadsScheduled;
            this.uploadsActive = uploadsActive;
            this.downloadsScheduled = downloadsScheduled;
            this.downloadsActive = downloadsActive;
        }

        //<editor-fold desc="Uploads">

        int getUploadsScheduled() {
            return uploadsScheduled;
        }

        int getUploadsActive() {
            return uploadsActive;
        }

        int getWaitingUploads() {
            return uploadsScheduled - uploadsActive;
        }

        //</editor-fold>

        //<editor-fold desc="Downloads">

        int getDownloadsScheduled() {
            return downloadsScheduled;
        }

        int getDownloadsActive() {
            return downloadsActive;
        }

        int getWaitingDownloads() {
            return downloadsScheduled - downloadsActive;
        }

        //</editor-fold>

        //<editor-fold desc="Totals">

        int getTotalScheduled() {
            return uploadsScheduled + downloadsScheduled;
        }

        int getTotalActive() {
            return uploadsActive + downloadsActive;
        }

        int getTotalWaiting() {
            return getTotalScheduled() - getTotalActive();
        }

        //</editor-fold>
    }

    enum TransferEvent {
        SCHEDULING("Scheduling"), STARTING("Starting");

        private String eventName;

        TransferEvent(final String eventName) {
            this.eventName = eventName;
        }

        @Override
        public String toString() {
            return eventName;
        }
    }

    enum TransferType {
        UPLOAD, DOWNLOAD;

        @Override
        public String toString() {
            return name().toLowerCase();
        }
    }
}
