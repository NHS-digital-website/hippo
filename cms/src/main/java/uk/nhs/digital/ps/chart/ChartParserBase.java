package uk.nhs.digital.ps.chart;

import org.apache.poi.ss.usermodel.Cell;

import javax.jcr.Binary;

public abstract class ChartParserBase {

    protected abstract ChartData parseData(Binary data) throws Exception;

    protected boolean isScatterOrFunnel(ChartType chartType) {
        return chartType.equals(ChartType.SCATTER_PLOT)
            || chartType.equals(ChartType.FUNNEL_PLOT);
    }

    protected Double getDoubleValue(Cell cell) {
        return cell.getCellType() == Cell.CELL_TYPE_STRING ? Double.valueOf(cell.getStringCellValue()) : cell.getNumericCellValue();
    }

    protected String getStringValue(Cell cell) {
        return cell.getCellType() == Cell.CELL_TYPE_STRING ? cell.getStringCellValue() : String.valueOf(cell.getNumericCellValue());
    }
}
