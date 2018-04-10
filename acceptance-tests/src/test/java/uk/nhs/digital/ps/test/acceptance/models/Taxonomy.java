package uk.nhs.digital.ps.test.acceptance.models;

import org.apache.commons.lang3.StringUtils;

public class Taxonomy {

    private static final String DELIMITER = " => ";

    private String level1;
    private String level2;
    private String level3;

    private Taxonomy(final String level1, final String level2, final String level3) {
        this.level1 = level1;
        this.level2 = level2;
        this.level3 = level3;
    }

    /**
     * @return New instance
     */
    public static Taxonomy createNew(final String level1, final String level2, final String level3) {
        return new Taxonomy(level1, level2, level3);
    }

    public String getLevel1() {
        return level1;
    }

    public String getLevel2() {
        return level2;
    }

    public String getLevel3() {
        return level3;
    }

    public String getTaxonomyContext() {

        String taxonomyContext = "";

        if (!StringUtils.isBlank(level1)) {
            taxonomyContext += level1;
        }

        if (!StringUtils.isBlank(level2)) {
            taxonomyContext += DELIMITER + level2;
        }

        if (!StringUtils.isBlank(level3)) {
            taxonomyContext += DELIMITER + level3;
        }

        return taxonomyContext;
    }
}
