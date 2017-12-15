package uk.nhs.digital.ps.migrator.model.hippo;

import java.io.*;
import java.nio.file.Path;
import java.util.*;
import org.yaml.snakeyaml.Yaml;
import uk.nhs.digital.ps.migrator.PublicationSystemMigrator;

public class TaxonomyLookup {

    private final ArrayList<String> taxonomyKeys = new ArrayList<>();
    private final static String HIPPO_TAXONOMY_KEY = "hippotaxonomy:key";

    private final Path taxonomyMappingImportPath;

    public TaxonomyLookup(final Path taxonomyMappingImportPath) {
        this.taxonomyMappingImportPath = taxonomyMappingImportPath;
        setup();
    }

    private void setup() {

        // Load taxonomy yaml from file
        // Currently uses development taxonomy file, but upon migration will use
        // the full taxonomy structure from Initial Import of Taxonomy
        Yaml yaml = new Yaml();
        File taxonomyYamlFile = new File(taxonomyMappingImportPath.toString());

        HashMap<String, String> map = null;

        try {
            map = yaml.load(new FileInputStream(taxonomyYamlFile));

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        // Extract all hippotaxonomy:key from yaml file
        extractKeys(map);
    }

    private HashMap extractKeys(HashMap map) {

        // Apply to all the sub-maps as well
        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();

            if (pair.getValue() instanceof HashMap) {
                extractKeys((HashMap)pair.getValue());
            } else {
                String key = pair.getKey().toString();
                String value = pair.getValue().toString();

                // build up a list of all keys first in taxonomyLookup.
                if (HIPPO_TAXONOMY_KEY.equals(key)) {
                    if (!taxonomyKeys.contains(value)) { taxonomyKeys.add(value);}
                }
            }
        }
        return map;
    }

    public boolean isKeyExist(String key) {
        return taxonomyKeys.contains(key);
    }
}
