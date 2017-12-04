import groovy.io.FileType
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.yaml.snakeyaml.DumperOptions
import org.yaml.snakeyaml.Yaml

// Options for how the file is written out
DumperOptions options = new DumperOptions()
options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK)
options.setExplicitStart(true) // This puts '---' at the start of the document
Yaml parser = new Yaml(options)

Logger log = LoggerFactory.getLogger(YamlFormatter.class)
log.info("Starting to format yaml files")

// Go through all yaml files in all the repository-data subfolders and format them
File rootDir = new File("repository-data")
rootDir.eachFileRecurse(FileType.DIRECTORIES) { dir ->
    dir.eachFileMatch(FileType.FILES, ~/.*.yaml/) { file ->
        String text = file.getText()
        LinkedHashMap map = parser.load(text)

        // Don't remove the UUID for this file, it's referenced in the facet configuration
        boolean removeUuid = !file.getName().equals("corporate-website.yaml")
        map = format(map, removeUuid)

        // Write out the formatted yaml
        parser.dump(map, new FileWriter(file))
    }
}

log.info("Finished formatting yaml files")

LinkedHashMap format(LinkedHashMap map, boolean removeUuid) {
    // Sort alphabetically so the order is deterministic
    map = map.sort()

    // Remove the uuid so hippo can generate one on startup
    if (removeUuid) {
        map.remove("jcr:uuid")
    }

    // Apply to all the sub-maps as well
    for (String key:map.keySet()) {
        def value = map.get(key)
        if (value instanceof LinkedHashMap) {
            map.put(key, format(value, removeUuid))
        }
    }

    return map
}
