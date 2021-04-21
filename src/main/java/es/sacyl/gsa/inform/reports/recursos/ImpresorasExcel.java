package es.sacyl.gsa.inform.reports.recursos;

import java.io.FileOutputStream;
import java.time.LocalDate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author 06551256M
 */
public class ImpresorasExcel {

    private static final Logger logger = LogManager.getLogger(ImpresorasExcel.class);

    private static final String[] colCabecera = {"Orden", "Fecha", "Texto", "Valor"};
    ;
    private final String filenameString = "/tmp/hoja.xlsx";

    public ImpresorasExcel() {
        try {

            Workbook workbook = new XSSFWorkbook();

            CreationHelper createHelper = workbook.getCreationHelper();

            Sheet hoja1 = workbook.createSheet("Hoja");

            Font headerFont = workbook.createFont();

            headerFont.setBold(true);

            headerFont.setFontHeightInPoints((short) 14);

            headerFont.setColor(IndexedColors.RED.getIndex());

            CellStyle headerCellStyle = workbook.createCellStyle();

            headerCellStyle.setFont(headerFont);

            // cabecera hijos
            Row headerRowHijos = hoja1.createRow(0);

            for (int i = 0; i < colCabecera.length; i++) {
                Cell cell = headerRowHijos.createCell(i);
                cell.setCellValue(colCabecera[i]);
                cell.setCellStyle(headerCellStyle);
            }
            //
            CellStyle dateCellStyle = workbook.createCellStyle();

            dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd-MM-yyyy"));

            int rowNum = 1;  // en la fila 0 ha escrito la cabecera
            int colNum = 0;
            Row row = hoja1.createRow(rowNum++);

            Cell dateOfBirthCell = row.createCell(colNum);
            dateOfBirthCell.setCellValue(LocalDate.now());
            dateOfBirthCell.setCellStyle(dateCellStyle);
            colNum++;
            row.createCell(colNum).setCellValue("Mes");

            FileOutputStream fileOut;

            fileOut = new FileOutputStream(filenameString);

            workbook.write(fileOut);

            fileOut.close();

            workbook.close();
        } catch (Exception e) {
            logger.error(e);
        }
    }
}
