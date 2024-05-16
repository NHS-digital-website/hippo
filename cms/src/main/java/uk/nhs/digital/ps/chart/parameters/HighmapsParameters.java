package uk.nhs.digital.ps.chart.parameters;

import uk.nhs.digital.ps.chart.enums.MapSource;

import javax.jcr.Binary;

public class HighmapsParameters extends AbstractVisualisationParameters {

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
