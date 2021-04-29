package es.sacyl.gsa.inform.reports.recursos;

import java.io.FileOutputStream;
import java.io.IOException;
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

    // datos que vamos a escribir en la cabecera de las celdas
    private static final String[] colCabecera = {"Orden", "Fecha", "Texto", "Valor"};

    // nombre del fichoro donde se va a grabar
    private final String filenameString = "C:\\temp\\hoja.xlsx";

    public ImpresorasExcel() {
        try {

            // crea un nuevo libre excel
            Workbook workbook = new XSSFWorkbook();

            CreationHelper createHelper = workbook.getCreationHelper();

            // crea una hoja dentro del libro  llamado hoja
            Sheet hoja1 = workbook.createSheet("Hoja1");

            // estilo de la cabecera
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setFontHeightInPoints((short) 14);
            headerFont.setColor(IndexedColors.RED.getIndex());

            // estilo de las celdas
            CellStyle headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setFont(headerFont);

            int rowNum = 0;  //contador de filas en la fila 0 ha escrito la cabecera
            int colNum = 0; // contador de columnas
            // crea la fila 0 en la hoja 1
            Row headerRow = hoja1.createRow(rowNum++);
// escribe la cabecera
            for (colNum = 0; colNum < colCabecera.length; colNum++) {
                Cell cell = headerRow.createCell(colNum);
                cell.setCellValue(colCabecera[colNum]);
                cell.setCellStyle(headerCellStyle);
            }
            // estilo para celta de fecha
            CellStyle dateCellStyle = workbook.createCellStyle();
            dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd-MM-yyyy"));

            // Añado fila
            Row row = hoja1.createRow(rowNum++);
            colNum = 0;
            row.createCell(colNum++).setCellValue(rowNum);

            Cell dateOfBirthCell = row.createCell(colNum++);
            dateOfBirthCell.setCellValue(LocalDate.now());
            dateOfBirthCell.setCellStyle(dateCellStyle);

            row.createCell(colNum++).setCellValue("Mes");
            row.createCell(colNum++).setCellValue(1500);

            // segunda fila
            colNum = 0;
            row = hoja1.createRow(rowNum++);
            row.createCell(colNum++).setCellValue(rowNum);
            dateOfBirthCell = row.createCell(colNum++);
            dateOfBirthCell.setCellValue(LocalDate.now());
            dateOfBirthCell.setCellStyle(dateCellStyle);

            row.createCell(colNum++).setCellValue("dia");
            row.createCell(colNum).setCellValue(150);

            // segunda hoja
            Sheet hoja2 = workbook.createSheet("Hoja2");
            // crea la fila 0 en la hoja 1
            headerRow = hoja2.createRow(1);
            headerRow.createCell(1).setCellValue("cabecera hoja dos");
            row = hoja2.createRow(2);
            row.createCell(2).setCellValue("valor hoja 2");

            FileOutputStream fileOut;

            fileOut = new FileOutputStream(filenameString);

            workbook.write(fileOut);

            fileOut.close();

            workbook.close();
        } catch (IOException e) {
            logger.error(e);
        }
    }
}
