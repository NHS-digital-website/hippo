package uk.nhs.digital.common.enums;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public enum Region {

    GREAT_BRITAIN("Great Britain", "England", "Scotland", "Wales"),
    UNITED_KINGDOM("United Kingdom", "England", "Scotland", "Wales", "Northern Ireland"),
    BRITISH_ISLES("British Isles", "England", "Scotland", "Wales", "Northern Ireland", "Republic of Ireland");

    private final String regionName;
    private final Set<String> geographicCoverageValues;

    Region(String regionName, String... geographicCoverageValues) {
        this.regionName = regionName;
        this.geographicCoverageValues = new HashSet<>(Arrays.asList(geographicCoverageValues));
    }

    /**
     * Converts selected geographic coverage values:
     * If England, Wales, Scotland are checked and no others - Great Britain
     * If England, Wales, Scotland and Northern Ireland are checked and no others - United Kingdom
     * If England, Wales, Scotland and Northern Ireland, and Republic of Ireland are ticked - British Isles
     * If any other combination is checked, serve up a list of the checked values
     */
    public static String[] convertGeographicCoverageValues(final String[] cmsValues) {

        for (Region region: Region.values()) {
            if (region.matches(cmsValues)) {
                return new String[] {region.getDisplayName()};
            }
        }
        return cmsValues;
    }

    private boolean matches(String[] cmsValues) {
        if (cmsValues == null) {
            return false;
        }
        return geographicCoverageValues.equals(new HashSet<>(Arrays.asList(cmsValues)));
    }

    private String getDisplayName() {
        return regionName;
    }
}
