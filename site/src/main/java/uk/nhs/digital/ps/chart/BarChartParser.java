package uk.nhs.digital.ps.chart;

import org.apache.poi.xssf.usermodel.*;
import org.hippoecm.hst.content.beans.standard.HippoResource;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.util.Iterator;

public class BarChartParser {

    private HippoResource dataFile;

    public BarChartParser(HippoResource dataFile) {
        this.dataFile = dataFile;
    }

    public void parse() {
        dataFile.getFilename();

        XSSFWorkbook workbook = new XSSFWorkbook();
        workbook.toString();
    }

    public Iterator getRowIterator(Path path, final String sheetName) {
        XSSFWorkbook workbook;
        try {
            workbook = new XSSFWorkbook(path.toString());
        } catch (IOException ioe) {
            throw new UncheckedIOException(ioe);
        }

        XSSFSheet sheet = workbook.getSheet(sheetName);

        if (sheet == null) {
            throw new RuntimeException("Sheet \'" + sheetName + "\' not found in " + path);
        }

        return sheet.rowIterator();
    }
}
