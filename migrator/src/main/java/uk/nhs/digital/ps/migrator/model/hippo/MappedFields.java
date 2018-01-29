package uk.nhs.digital.ps.migrator.model.hippo;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.partitioningBy;
import static java.util.stream.Collectors.toList;
import static org.apache.poi.ss.usermodel.Row.MissingCellPolicy.RETURN_BLANK_AS_NULL;
import static org.springframework.util.CollectionUtils.isEmpty;
import static uk.nhs.digital.ps.migrator.report.IncidentType.FIELD_MAPPING_INVALID;
import static uk.nhs.digital.ps.migrator.report.IncidentType.FIELD_MAPPING_MISSING_FIELD;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import uk.nhs.digital.ps.migrator.report.MigrationReport;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public final class MappedFields {
    public static final MappedFields EMPTY = empty();

    private static final List<String> GRANULARITY_OPTIONS = getAllowedGranularityOptions();
    private static final List<String> GEOGRAPHIC_COVERAGE_OPTIONS = getAllowedGeographicCoverageOptions();

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
    private static final String EMPTY_DATE = "0001-01-01T12:00:00.000Z";

    private String pCode;
    private String coverageStart;
    private String coverageEnd;
    private String geographicCoverage;
    private List<String> granularity;

    private MigrationReport migrationReport;
    private Map<String, List<Integer>> columnIndexes;

    private MappedFields() {}

    public MappedFields(MigrationReport migrationReport, Map<String, List<Integer>> columnIndexes, Row row) {
        this.migrationReport = migrationReport;
        this.columnIndexes = columnIndexes;

        this.pCode = getStringValue(row, ColumnType.P_CODE, null);
        this.coverageStart = getDateValue(row, ColumnType.COVERAGE_START);
        this.coverageEnd = getDateValue(row, ColumnType.COVERAGE_END);
        this.geographicCoverage = getStringValue(row, ColumnType.GEOGRAPHIC_COVERAGE, GEOGRAPHIC_COVERAGE_OPTIONS);
        this.granularity = getStringValues(row, ColumnType.GRANULARITY, GRANULARITY_OPTIONS);
    }

    private String getDateValue(Row row, ColumnType columnType) {
        Cell cell = row.getCell(getSingleColumnIndex(columnType), RETURN_BLANK_AS_NULL);
        if (cell != null) {
            return DATE_FORMAT.format(cell.getDateCellValue());
        }

        migrationReport.report(pCode, FIELD_MAPPING_MISSING_FIELD, columnType.columnName);
        return EMPTY_DATE;
    }

    private String getStringValue(Row row, ColumnType columnType, List<String> allowedValues) {
        // assert single column
        getSingleColumnIndex(columnType);

        List<String> values = getStringValues(row, columnType, allowedValues);
        return values.isEmpty() ? "" : values.get(0);
    }

    private List<String> getStringValues(Row row, ColumnType columnType, List<String> allowedValues) {
        List<Integer> indexes = columnIndexes.get(columnType.columnName);
        if (indexes.size() < 1) {
            throw new RuntimeException("Expected at least 1 column for: " + columnType.columnName);
        }

        List<String> values = streamRow(row)
            .filter((cell) -> columnIndexes.get(columnType.columnName).contains(cell.getColumnIndex()))
            .map(Cell::getStringCellValue)
            .map(String::trim)
            .filter(StringUtils::isNotBlank)
            .collect(toList());

        if (isEmpty(values)) {
            migrationReport.report(pCode, FIELD_MAPPING_MISSING_FIELD, columnType.columnName);
        }

        Map<Boolean, List<String>> valuesMap = values.stream()
            .collect(partitioningBy(value -> allowedValues == null || allowedValues.contains(value)));

        List<String> invalidValues = valuesMap.get(false);
        if (!isEmpty(invalidValues)) {
            migrationReport.report(pCode, FIELD_MAPPING_INVALID, columnType.columnName + " | " + String.join(", ", invalidValues));
        }

        return valuesMap.get(true);
    }

    private Integer getSingleColumnIndex(ColumnType columnType) {
        List<Integer> indexes = columnIndexes.get(columnType.columnName);
        if (indexes.size() != 1) {
            throw new RuntimeException("Expected single column for: " + columnType.columnName);
        }

        return indexes.get(0);
    }

    private enum ColumnType {
        P_CODE("P Code"),
        COVERAGE_START("Coverage Start"),
        COVERAGE_END("Coverage End"),
        GEOGRAPHIC_COVERAGE("Geographic Coverage"),
        GRANULARITY("Granularity");

        private String columnName;

        ColumnType(String columnName) {
            this.columnName = columnName;
        }

    }

    private Stream<Cell> streamRow(Row row) {
        return StreamSupport.stream(((Iterable<Cell>) row::cellIterator).spliterator(), false);
    }

    public String getPCode() {
        return pCode;
    }

    public String getCoverageStart() {
        return coverageStart;
    }

    public String getCoverageEnd() {
        return coverageEnd;
    }

    public String getGeographicCoverage() {
        return geographicCoverage;
    }

    public List<String> getGranularity() {
        return granularity;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    private static MappedFields empty() {
        MappedFields mappedFields = new MappedFields();
        mappedFields.coverageStart = EMPTY_DATE;
        mappedFields.coverageEnd = EMPTY_DATE;
        mappedFields.geographicCoverage = "";
        mappedFields.granularity = emptyList();
        return mappedFields;
    }

    private static List<String> getAllowedGranularityOptions() {
        return asList("Ambulance Trusts", "Cancer networks", "Care Trusts", "Census Area Statistics Wards", "Clinical Commissioning Area Teams",
            "Clinical Commissioning Groups", "Clinical Commissioning Regions", "Community health services", "Councils with Social Services Responsibilities",
            "Country", "County", "Crime & disorder reduction partnership", "Dental practices", "Deprivation", "Education Authority",
            "Government Office Regions", "GP practices", "Health Education England Region", "Hospital and Community Health Services",
            "Hospital Trusts", "Independent Sector Health Care Providers", "Local Authorities", "London Authorities", "Mental Health Trusts",
            "Middle Layer Super Output Areas", "NHS Health Boards", "NHS Trusts", "ONS Area Classification", "Parliamentary constituency",
            "Pharmacies and clinics", "Primary Care Organisations", "Primary Care Trusts", "Provider", "Provider clusters",
            "Regional health body", "Regions", "Strategic Health Authorities", "Sustainability and Transformation Partnerships", "Wards");
    }

    private static List<String> getAllowedGeographicCoverageOptions() {
        return asList("England", "England and Northern Ireland", "England and Scotland", "England and Wales", "England Scotland and Northern Ireland",
            "England Wales and Northern Ireland", "Great Britain", "International", "Northern Ireland", "Scotland", "UK", "Wales");
    }
}
