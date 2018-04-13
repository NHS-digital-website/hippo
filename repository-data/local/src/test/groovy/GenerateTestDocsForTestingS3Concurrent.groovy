import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.stream.Collectors

final int DEFAULT_DOC_COUNT = 100

final int documentCount = (System.properties['documentCount'] ?: DEFAULT_DOC_COUNT) as Integer

final String moduleResourcesPath = "${project.basedir}/src/main/resources"
final String templatesDirPath = "${moduleResourcesPath}/test-util/test-docs-templates/s3-performance"
final String publicationsTargetDirPath = "${moduleResourcesPath}/hcm-content/content/documents/corporate-website/publication-system/acceptance-tests/concurrent-s3-access-test"

final Path sourceDirYamlFile = Paths.get(templatesDirPath, 'concurrent-s3-access-test.yaml.template')
final Path targetDirYamlFile = Paths.get(publicationsTargetDirPath, 'concurrent-s3-access-test.yaml')

final Path targetDir = Paths.get(publicationsTargetDirPath)
targetDir.toFile().deleteDir()
Files.createDirectories(targetDir)

Files.copy(sourceDirYamlFile, targetDirYamlFile)


// CREATE LEGACY PUBLICATIONS - FOR DOWNLOAD
final String legacyPublicationYamlTemplate = new File("${templatesDirPath}/legacy-publication-download.yaml.template").text
(1..documentCount).forEach({ i ->
    final String targetFileContent = legacyPublicationYamlTemplate.replaceAll("\\{DOCUMENT_INDEX\\}", "${i}")
    final String targetFileName = "legacy-publication-${i}.yaml"

    createLegacyDownloadPublication(targetDir, targetFileName, targetFileContent, i)

    println "Created ${targetFileName}; ${documentCount - i}/${documentCount} to go."
})

private void createLegacyDownloadPublication(Path targetDir, String targetFileName, String targetFileContent, int documentIndex) {
    final Path legacyPublicationFile = Paths.get(targetDir.toString(), targetFileName)
    Files.createFile(legacyPublicationFile)
    legacyPublicationFile.toFile().text = targetFileContent

    createDataBinFile(targetDir, documentIndex, 1)
    createDataBinFile(targetDir, documentIndex, 2)
}

private void createDataBinFile(Path targetDir, int documentIndex, int documentVariantIndex) {
    final Path resourcePathOne = Files.createDirectories(Paths.get(
        targetDir.toString(),
        "legacy-publication-${documentIndex}",
        "legacy-publication-${documentIndex}[${documentVariantIndex}]",
        "Attachments-v3",
        "attachmentResource"
    ))
    Files.createFile(Paths.get(resourcePathOne.toString(), "data.bin"))
}

// CREATE LEGACY PUBLICATIONS - FOR UPLOAD
final String legacyUploadPublicationYamlTemplate = new File("${templatesDirPath}/legacy-publication-upload.yaml.template").text
(1..documentCount).forEach({ i ->
    final String targetFileContent = legacyUploadPublicationYamlTemplate.replaceAll("\\{DOCUMENT_INDEX\\}", "${i}")
    final String targetFileName = "legacy-publication-upload-${i}.yaml"

    createLegacyUploadPublication(targetDir, targetFileName, targetFileContent)

    println "Created ${targetFileName}; ${documentCount - i}/${documentCount} to go."
})

private void createLegacyUploadPublication(Path targetDir, String targetFileName, String targetFileContent) {
    final Path legacyPublicationFile = Paths.get(targetDir.toString(), targetFileName)
    Files.createFile(legacyPublicationFile)
    legacyPublicationFile.toFile().text = targetFileContent
}

// CREATE USERS
final Path usersYamlFile = Paths.get("${moduleResourcesPath}/hcm-config/configuration/users", "s3-perf-test-users.yaml")
final String userYamlTemplate = Paths.get(templatesDirPath, "s3-user.yaml.template").toFile().text

Files.deleteIfExists(usersYamlFile)

String usersYaml = '''
---
definitions:
  config:
    /hippo:configuration/hippo:users:
'''

final List<String> users = []
// CREATE USERS FOR DOWNLOAD
(1..documentCount).forEach({ i ->
    final String userYaml = userYamlTemplate.replaceAll('\\{USER-NUMBER\\}', "${i}")

    users << "user-${i}"

    usersYaml = usersYaml + userYaml
})

// CREATE USERS FOR UPLOAD
(1..documentCount).forEach({ i ->
    final String userYaml = userYamlTemplate.replaceAll('\\{USER-NUMBER\\}', "upload-${i}")

    users << "user-upload-${i}"

    usersYaml = usersYaml + userYaml
})
Files.createFile(usersYamlFile)
usersYamlFile.text = usersYaml


// ADD USERS TO GROUP 'author'
final Path groupYamlFile = Paths.get("${moduleResourcesPath}/hcm-config/configuration/groups", "s3-perf-test-group-author.yaml")
Files.deleteIfExists(groupYamlFile)
final String groupYamlTemplate = Paths.get(templatesDirPath, "s3-group-author.yaml.template").toFile().text

final String groupYaml = groupYamlTemplate.replaceAll(
    "\\{GROUP-MEMBERS\\}",
    users.stream().map({ i -> "\"${i}\""}).collect(Collectors.joining(","))
)
Files.createFile(groupYamlFile)
groupYamlFile.text = groupYaml
