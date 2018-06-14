package uk.nhs.digital.ps;

public interface PublicationSystemConstants {

    String NODE_TYPE_PUBLICATION = "publicationsystem:publication";
    String NODE_TYPE_LEGACY_PUBLICATION = "publicationsystem:legacypublication";
    String NODE_TYPE_DATASET = "publicationsystem:dataset";
    String NODE_TYPE_DATA_FILE = "publicationsystem:dataFile";
    String NODE_TYPE_CHART = "publicationsystem:chartSection";
    String NODE_TYPE_MAP = "publicationsystem:mapSection";

    String PROPERTY_PUBLICLY_ACCESSIBLE = "publicationsystem:PubliclyAccessible";
    String PROPERTY_CHART_CONFIG = "publicationsystem:chartConfig";
    String PROPERTY_CHART_TYPE = "publicationsystem:type";
    String PROPERTY_CHART_TITLE = "publicationsystem:title";
    String PROPERTY_CHART_YTITLE = "publicationsystem:yTitle";

    String PROPERTY_MAP_SOURCE = "publicationsystem:mapSource";

    String INDEX_FILE_NAME = "content";
}
