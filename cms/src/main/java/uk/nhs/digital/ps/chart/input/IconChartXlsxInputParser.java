package uk.nhs.digital.ps.chart.input;

import static uk.nhs.digital.ps.chart.enums.ChartType.ICON;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import uk.nhs.digital.ps.chart.enums.ChartType;
import uk.nhs.digital.ps.chart.enums.IconType;
import uk.nhs.digital.ps.chart.enums.IconXlsxAllowList;
import uk.nhs.digital.ps.chart.model.IconVisualisationModel;
import uk.nhs.digital.ps.chart.parameters.AbstractVisualisationParameters;
import uk.nhs.digital.ps.chart.parameters.VisualisationParameters;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.jcr.RepositoryException;

public class IconChartXlsxInputParser extends AbstractHighchartsXlsxInputParser {


    public IconChartXlsxInputParser() {
        super(ICON);
    }

    @Override
    protected IconVisualisationModel parseXlsxChart(final AbstractVisualisationParameters abstractParameters
    ) throws IOException, RepositoryException {

        VisualisationParameters parameters = (VisualisationParameters) abstractParameters;

        final XSSFWorkbook workbook = readXssfWorkbook(parameters.getInputFileContent());

        final XSSFSheet sheet = workbook.getSheetAt(0);

        Iterator<Row> rowIterator = sheet.rowIterator();

        final List<String> allowList = IconXlsxAllowList.GetAllowedValuesList();

        Map<String, String> xlsxValueMap = new HashMap<>();

        while (rowIterator.hasNext()) {
            Row currentRow = rowIterator.next();
            if (currentRow == null) {
                continue;
            }
            if (allowList.contains(getFormatCellValue(currentRow, 0).toUpperCase())) {
                xlsxValueMap.put(getFormatCellValue(currentRow, 0), getFormatCellValue(currentRow, 1));
            }
        }

        if (xlsxValueMap.size() != allowList.size()) {
            throw new RuntimeException("Missing element or wrong name in file input.");
        }

        String title = parameters.getTitle();
        ChartType chartType = parameters.getChartType();
        IconType iconType = parameters.getIconType();
        String yTitle = parameters.getYTitle();

        return new IconVisualisationModel(chartType, iconType, title, yTitle, xlsxValueMap);
    }

    private String getFormatCellValue(Row currentRow, int cellnum) {
        DataFormatter formatter = new DataFormatter();
        return formatter.formatCellValue(currentRow.getCell(cellnum));
    }
}
