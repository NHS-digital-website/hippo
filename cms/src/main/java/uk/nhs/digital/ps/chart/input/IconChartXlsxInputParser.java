package uk.nhs.digital.ps.chart.input;

import static uk.nhs.digital.ps.chart.enums.ChartType.ICON;

import com.google.common.collect.Streams;
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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;
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
        DataFormatter formatter = new DataFormatter();

        final List<String> allowList = IconXlsxAllowList.GetAllowedValuesList();

        Iterable<Row> iterable = () -> rowIterator;

        Predicate<Row> containsAllowed = row -> allowList.contains(formatter.formatCellValue(row.getCell(0)).toUpperCase());

        // if row(n).cell(0) value exists in allowlist
        // then save Map<String, String> where
        // key=row(n).cell(0) and value= key=row(n).cell(1)
        Map<String, String> xlsxValueMap = Streams.stream(iterable)
            .filter(row -> row.getCell(0) != null)
            .filter(containsAllowed)
            .collect(Collectors.toMap(
                row -> formatter.formatCellValue(row.getCell(0)),
                row -> formatter.formatCellValue(row.getCell(1))
            ));

        String title = parameters.getTitle();
        ChartType chartType = parameters.getChartType();
        IconType iconType = parameters.getIconType();
        String yTitle = parameters.getYTitle();

        return new IconVisualisationModel(chartType, iconType, title, yTitle, xlsxValueMap);
    }
}
