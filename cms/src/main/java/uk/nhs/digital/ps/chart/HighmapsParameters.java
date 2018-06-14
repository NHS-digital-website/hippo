package uk.nhs.digital.ps.chart;

import javax.jcr.Binary;

public class HighmapsParameters extends AbstractHighchartsParameters {

    private MapSource mapSource;

    public HighmapsParameters(final String type,
                              final String mapSource,
                              final String title,
                              final Binary inputFileContent) {
        super(type, inputFileContent, title);

        this.mapSource = MapSource.toMapType(mapSource);
    }

    public MapSource getMapSource() {
        return mapSource;
    }
}
