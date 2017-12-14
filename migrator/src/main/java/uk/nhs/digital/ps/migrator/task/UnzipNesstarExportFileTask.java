package uk.nhs.digital.ps.migrator.task;

import net.lingala.zip4j.core.ZipFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.digital.ps.migrator.config.ExecutionParameters;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.nio.file.Files.delete;
import static java.nio.file.Files.exists;
import static java.nio.file.Files.walkFileTree;
import static org.apache.commons.io.FileUtils.deleteDirectory;
import static org.apache.commons.io.FilenameUtils.getBaseName;
import static org.apache.commons.io.FilenameUtils.getExtension;

public class UnzipNesstarExportFileTask implements MigrationTask {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final ExecutionParameters executionParameters;

    public UnzipNesstarExportFileTask(final ExecutionParameters executionParameters) {
        this.executionParameters = executionParameters;
    }

    @Override
    public boolean isRequested() {
        return executionParameters.isNesstarUnzipForce()
            || executionParameters.getNesstarZippedExportFile() != null;
    }

    @Override
    public void execute() {
        final Path nesstarZipFilePath = executionParameters.getNesstarZippedExportFile();
        final Path nesstarTargetDir = executionParameters.getNesstarUnzippedExportDir();

        try {
            if (exists(nesstarTargetDir)) {
                if (executionParameters.isNesstarUnzipForce()) {
                    log.info(
                        "Forced decompression requested; deleting existing Nesstar export directory {}",
                        nesstarTargetDir
                    );
                    deleteDirectory(nesstarTargetDir.toFile());

                } else {
                    log.info("Existing Nesstar export directory was found so exiting with nothing to do.");
                    return;
                }
            }

            log.info("Unzipping: {}", nesstarZipFilePath);
            log.info("     into: {}", nesstarTargetDir);

            ZipFile zipFile = new ZipFile(nesstarZipFilePath.toFile());
            zipFile.extractAll(nesstarTargetDir.toString());


            // Extract all ZIP files recursively, including ZIPs within ZIPs until no ZIP is left to unzip
            for (final AtomicBoolean zipsFound = new AtomicBoolean(true); zipsFound.get(); ) {
                zipsFound.set(false);

                walkFileTree(nesstarTargetDir, new SimpleFileVisitor<Path>() {

                    @Override
                    public FileVisitResult visitFile(final Path candidateFileSystemItem,
                                                     final BasicFileAttributes attrs) throws IOException {

                        if ("zip".equalsIgnoreCase(getExtension(candidateFileSystemItem.getFileName().toString()))) {
                            try {

                                final ZipFile nestedZipFile = new ZipFile(candidateFileSystemItem.toFile());

                                final Path nestedDirToUnzipTo = Files.createDirectory(Paths.get(
                                    candidateFileSystemItem.getParent().toString(),
                                    getBaseName(candidateFileSystemItem.getFileName().toString())
                                ));

                                nestedZipFile.extractAll(nestedDirToUnzipTo.toString());

                                delete(candidateFileSystemItem);

                                zipsFound.set(true);

                            } catch (final Exception e) {
                                throw new RuntimeException(e);
                            }
                        }
                        return FileVisitResult.CONTINUE;
                    }
                });
            }

        } catch (final Exception e) {
            throw new RuntimeException(
                "Failed to unpack Nesstar archive " + (nesstarZipFilePath == null ? "" : nesstarZipFilePath.toString()),
                e
            );
        }
    }

}
