package uk.nhs.digital.ps.chart.input;

import static org.apache.commons.lang3.StringUtils.isEmpty;
import static uk.nhs.digital.ps.chart.ChartType.AREA_MAP;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import uk.nhs.digital.ps.chart.AbstractHighchartsParameters;
import uk.nhs.digital.ps.chart.HighmapsParameters;
import uk.nhs.digital.ps.chart.model.HighmapsModel;
import uk.nhs.digital.ps.chart.model.Series;

import java.io.IOException;
import java.util.Iterator;
import javax.jcr.RepositoryException;

public class HighmapsXlsxInputParser extends AbstractHighchartsXlsxInputParser {

    private static final String KEY_COL_NAME = "key";
    private static final String NAME_COL_NAME = "name";

    private static final int KEY_COL_INDEX = 0;
    private static final int NAME_COL_INDEX = 1;
    private static final int SERIES_COL_INDEX = 2;

    public HighmapsXlsxInputParser() {
        super(AREA_MAP);
    }

    @Override
    protected HighmapsModel parseXlsxChart(final AbstractHighchartsParameters abstractParameters)
        throws IOException, RepositoryException {

        HighmapsParameters parameters = (HighmapsParameters) abstractParameters;

        final XSSFWorkbook workbook = readXssfWorkbook(parameters.getInputFileContent());

        final XSSFSheet sheet = workbook.getSheetAt(0);

        // Validate the headers, we should have Key, Name and then a single Series
        Iterator<Row> rowIterator = sheet.rowIterator();
        Row header = rowIterator.next();
        String seriesName = getStringValue(header.getCell(SERIES_COL_INDEX));

        if (!getStringValue(header.getCell(KEY_COL_INDEX)).equalsIgnoreCase(KEY_COL_NAME)
            || !getStringValue(header.getCell(NAME_COL_INDEX)).equalsIgnoreCase(NAME_COL_NAME)
            || isEmpty(seriesName)) {
            throw new RuntimeException("Missing column from map file input");
        }

        final Series<Object[]> series = new Series<>(seriesName);

        // Get the data
        rowIterator.forEachRemaining(row -> {
            // First column is the map key
            String key = getStringValue(row.getCell(KEY_COL_INDEX));
            Double data = getDoubleValue(row.getCell(SERIES_COL_INDEX));

            series.add(new Object[]{key, data});
        });

        String highmapsMapKey = parameters.getMapSource().getHighmapsMapKey();
        String title = parameters.getTitle();

        return new HighmapsModel(highmapsMapKey, title, series);
    }
}
