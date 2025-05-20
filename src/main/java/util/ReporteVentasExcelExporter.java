package util;

import clases.ReporteVentas;
import clases.DetallesTicketReporte;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.File;
import java.io.IOException;

public class ReporteVentasExcelExporter {

    /**
     * Exporta el reporte de ventas a un archivo Excel y retorna la ruta absoluta del archivo generado.
     * @param reporte El objeto ReporteVentas a exportar.
     * @return Ruta absoluta del archivo Excel generado, o null si hubo error.
     */
    public static String exportarAExcel(ReporteVentas reporte) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Reporte de Ventas");

        int rowIdx = 0;
        Row header = sheet.createRow(rowIdx++);
        header.createCell(0).setCellValue("ID Ticket");
        header.createCell(1).setCellValue("Fecha");
        header.createCell(2).setCellValue("Num. Ticket");
        header.createCell(3).setCellValue("Total");
        header.createCell(4).setCellValue("IVA");
        header.createCell(5).setCellValue("IEPS");

        for (DetallesTicketReporte d : reporte.getDetallesTickets()) {
            Row row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue(d.getIdTicket());
            row.createCell(1).setCellValue(d.getFechaCreacion().toString());
            row.createCell(2).setCellValue(d.getNumeroTicket());
            row.createCell(3).setCellValue(d.getTotal());
            row.createCell(4).setCellValue(d.getIva());
            row.createCell(5).setCellValue(d.getIeps());
        }

        // Resumen
        rowIdx++;
        Row resumen = sheet.createRow(rowIdx++);
        resumen.createCell(0).setCellValue("Subtotal:");
        resumen.createCell(1).setCellValue(reporte.getSubtotal());
        resumen = sheet.createRow(rowIdx++);
        resumen.createCell(0).setCellValue("IVA:");
        resumen.createCell(1).setCellValue(reporte.getIvaTotal());
        resumen = sheet.createRow(rowIdx++);
        resumen.createCell(0).setCellValue("IEPS:");
        resumen.createCell(1).setCellValue(reporte.getIepsTotal());
        resumen = sheet.createRow(rowIdx++);
        resumen.createCell(0).setCellValue("Total:");
        resumen.createCell(1).setCellValue(reporte.getTotal());

        String fileName = "ReporteVentas_" + System.currentTimeMillis() + ".xlsx";
        try (FileOutputStream fileOut = new FileOutputStream(fileName)) {
            workbook.write(fileOut);
            workbook.close();
            return new File(fileName).getAbsolutePath();
        } catch (IOException e) {
            System.err.println("Error al exportar reporte a Excel: " + e.getMessage());
            return null;
        }
    }
}
