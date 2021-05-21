package com.dragonchang.util;

import com.dragonchang.domain.dto.ExcelData;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.extensions.XSSFCellBorder.BorderSide;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

/**
 * <p>
 * 导出Excel工具类
 * </p>
 *
 * @author gaox
 * @since 2020/3/23
 */
public class ExcelUtil {

    private static final String suffix = ".xlsx";

    /**
     * 使用浏览器选择路径下载
     *
     * @param data
     * @throws Exception
     */
    @Deprecated
    public static ResponseEntity<byte[]> exportExcel(ExcelData data) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        try {
            headers.setContentDispositionFormData("attachment", URLEncoder.encode(data.getFileName() + ".xlsx", "utf-8"));
            generateExcel(data).write(bout);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                bout.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new ResponseEntity<byte[]>(bout.toByteArray(), headers, HttpStatus.CREATED);
    }

    /**
     * 直接下载到本地
     *
     * @param excelData
     * @return
     * @throws Exception
     */
    public static void write2Local(ExcelData excelData) throws Exception {
        File f = new File(excelData.getSavePath() + excelData.getFileName() + suffix);
        FileOutputStream out = new FileOutputStream(f);
        generateExcel(excelData).write(out);
    }

    public static byte[] readDataAsByteArray(ExcelData data) {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        byte[] bytes = null;
        try {
            if (data != null) {
                SXSSFWorkbook wb = generateExcel(data);
                data.clear();
                wb.write(bout);
                wb=null;
            }
            bytes = bout.toByteArray();
            bout.close();
//            Runtime.getRuntime().gc();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bytes;
    }

    private static SXSSFWorkbook generateExcel(ExcelData data) throws Exception {
        //keep 50 rows in memory, exceeding rows will be flushed to disk
        SXSSFWorkbook wb = new SXSSFWorkbook(50);
        // temp files will be gzipped
        wb.setCompressTempFiles(true);
        int rowIndex = 0;
        String sheetName = data.getSheetName();
        if (null == sheetName) {
            sheetName = "";
        }
        SXSSFSheet sheet = wb.createSheet(sheetName);
        sheet.trackAllColumnsForAutoSizing();
        rowIndex = writeExcel(wb, sheet, data);
        return wb;
    }

    /**
     * 表显示字段
     *
     * @param wb
     * @param sheet
     * @param data
     * @return
     */
    private static int writeExcel(SXSSFWorkbook wb, Sheet sheet, ExcelData data) throws Exception{
        int rowIndex = 0;
        rowIndex = writeTitlesToExcel(wb, sheet, data.getTitles());
        rowIndex = writeRowsToExcel(wb, sheet, data.getRows(), rowIndex);
        autoSizeColumns(sheet, data.getTitles().size() + 1);
        return rowIndex;
    }

    /**
     * 设置表头
     *
     * @param wb
     * @param sheet
     * @param titles
     * @return
     */
    private static int writeTitlesToExcel(SXSSFWorkbook wb, Sheet sheet, List<String> titles) {
        int rowIndex = 0;
        int colIndex = 0;
        Font titleFont = wb.createFont();
        //设置字体
        titleFont.setFontName("simsun");
        //设置粗体
        titleFont.setBold(true);
        //设置字号
        titleFont.setFontHeightInPoints((short) 14);
        //设置颜色
        titleFont.setColor(IndexedColors.BLACK.index);
        CellStyle titleStyle = wb.createCellStyle();
        //水平居中
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        //垂直居中
        titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        //设置图案颜色
        titleStyle.setFillForegroundColor(IndexedColors.GREEN.getIndex());
        //设置图案样式
        titleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        titleStyle.setFont(titleFont);
//        setBorder(titleStyle, BorderStyle.THIN, new XSSFColor(new Color(0, 0, 0)));
        Row titleRow = sheet.createRow(rowIndex);
        titleRow.setHeightInPoints(25);
        colIndex = 0;
        Cell cell;
        for (String field : titles) {
            cell = titleRow.createCell(colIndex);
            cell.setCellValue(field);
            cell.setCellStyle(titleStyle);
            colIndex++;
        }
        rowIndex++;
        return rowIndex;
    }

    /**
     * 设置内容
     *
     * @param wb
     * @param sheet
     * @param rows
     * @param rowIndex
     * @return
     */
    private static int writeRowsToExcel(SXSSFWorkbook wb, Sheet sheet, List<List<Object>> rows, int rowIndex) throws Exception {
        int colIndex;
//        Font dataFont = wb.createFont();
//        dataFont.setFontName("simsun");
//        dataFont.setFontHeightInPoints((short) 14);
//        dataFont.setColor(IndexedColors.BLACK.index);
//
//        CellStyle dataStyle = wb.createCellStyle();
//        dataStyle.setAlignment(HorizontalAlignment.CENTER);
//        dataStyle.setVerticalAlignment(VerticalAlignment.CENTER);
//        dataStyle.setFont(dataFont);
//        setBorder(dataStyle, BorderStyle.THIN, new XSSFColor(new Color(0, 0, 0)));
        //声明一个画图的顶级管理器
//        Drawing drawing=(SXSSFDrawing) sheet.createDrawingPatriarch();
        Row dataRow ;
        Cell cell ;
        URL url ;
        for (List<Object> rowData : rows) {
            dataRow = sheet.createRow(rowIndex);
            dataRow.setHeightInPoints(25);
            colIndex = 0;
            for (Object cellData : rowData) {
                cell = dataRow.createCell(colIndex);
                if (cellData != null) {
//                    String data =cellData.toString();
//                    if(data.endsWith(".png")||data.endsWith(".jpg")){
////                        dataRow.setHeightInPoints(80);
////                        sheet.setColumnWidth(i, 35*100);
//                        url = new URL(data);
//                        byte[] picBytes=IOUtils.readStreamAsByteArray(url.openStream());
//                        XSSFClientAnchor anchor=new XSSFClientAnchor(0,0,1023*10000,255*10000,colIndex,rowIndex,colIndex,rowIndex);
//                        anchor.setAnchorType(ClientAnchor.AnchorType.MOVE_DONT_RESIZE);
//                        drawing.createPicture(anchor, wb.addPicture(picBytes, XSSFWorkbook.PICTURE_TYPE_JPEG));
//                    }else{
                        if(isNumeric(cellData.toString())) {
                            cell.setCellValue(Double.valueOf(cellData.toString()).doubleValue());
                        } else {
                            cell.setCellValue(cellData.toString());
                        }
//                    }
                } else {
                    cell.setCellValue("");
                }
//                cell.setCellStyle(dataStyle);
                colIndex++;
                cell =null;
            }
            rowIndex++;
        }
        return rowIndex;
    }

    public final static boolean isNumeric(String s) {
        if (s != null && !"".equals(s.trim())) {
            return s.matches("^[0.0-9.0]+$");
        } else {
            return false;
        }
    }

    private static void insertPic(XSSFWorkbook wb, Sheet sheet,short col1, int row1,short col2,int row2)throws Exception{
        XSSFDrawing patriarch = (XSSFDrawing) sheet.createDrawingPatriarch();
        /**
        * 创建一个新的客户端锚点，附加到excel工作表，并设置左上角和右下角
        *
        * @param realX1  图片的左上角在开始单元格（col1,row1）中的横坐标
        * @param realY1  图片的左上角在开始单元格（col1,row1）中的纵坐标
        * @param realX2  图片的右下角在结束单元格（col2,row2）中的横坐标
        * @param realY2  图片的右下角在结束单元格（col2,row2）中的纵坐标
        * @param col1  开始单元格所处的列号, base 0, 图片左上角在开始单元格内
        * @param row1  开始单元格所处的行号, base 0, 图片左上角在开始单元格内
        * @param col2  结束单元格所处的列号, base 0, 图片右下角在结束单元格内
        * @param row2  结束单元格所处的行号, base 0, 图片右下角在结束单元格内
        * */
        HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 100, 100, col1, row1, col2, row2);
        // 图片字节流
        ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
        BufferedImage bufferImg = ImageIO.read(new File("ok.jpg"));
        ImageIO.write(bufferImg, "jpg", byteArrayOut);
        byte[] imgtypes = byteArrayOut.toByteArray();
        /*
         * 在工作簿中添加一张图片，返回图片的索引，base 1
         * @param pictureType 图片类型 PICTURE_TYPE_JPEG|PICTURE_TYPE_PNG
         **/
        int puctureIndex = wb.addPicture(imgtypes, 5);
        // 创建图片
        patriarch.createPicture(anchor, puctureIndex);
    }
    /**
     * 自动调整列宽
     *
     * @param sheet
     * @param columnNumber
     */
    private static void autoSizeColumns(Sheet sheet, int columnNumber) {
        for (int i = 0; i < columnNumber; i++) {
            int orgWidth = sheet.getColumnWidth(i);
            sheet.autoSizeColumn(i, true);
            int newWidth = (int) (sheet.getColumnWidth(i) + 100);
            if (newWidth > orgWidth) {
//                if(newWidth>255*256){//Max width
                if (newWidth > 12000) {//为了美观
                    sheet.setColumnWidth(i, 12000);
                } else {
                    sheet.setColumnWidth(i, newWidth);
                }

            } else {
                sheet.setColumnWidth(i, orgWidth);
            }
        }
    }

    /**
     * 设置边框
     *
     * @param style
     * @param border
     * @param color
     */
    private static void setBorder(XSSFCellStyle style, BorderStyle border, XSSFColor color) {
        style.setBorderTop(border);
        style.setBorderLeft(border);
        style.setBorderRight(border);
        style.setBorderBottom(border);
        style.setBorderColor(BorderSide.TOP, color);
        style.setBorderColor(BorderSide.LEFT, color);
        style.setBorderColor(BorderSide.RIGHT, color);
        style.setBorderColor(BorderSide.BOTTOM, color);
    }

    public static BigDecimal convertToBillion(BigDecimal amount) {
        if(amount == null) {
            return null;
        }
        return amount.divide(new BigDecimal(100000000), 3, BigDecimal.ROUND_HALF_UP);
    }
}
