package com.mars.common.utils.poi;

import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.*;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * @author
 */
public class ExportExcelUtils {
    public static final String L = "L";
    public static final String R = "R";
    public static final String C = "C";

    public static XSSFWorkbook getXSSFWorkbook(Map<String, Object> dataMap, String sheetName, String titleName, int lastCol) {
        // 第一步，创建一个HSSFWorkbook，对应一个Excel文件
        XSSFWorkbook wb = new XSSFWorkbook();
        //第二步，创建确定位置的的合并单元格对象
        //起始行,结束行,起始列,结束列
        CellRangeAddress callRangeAddress1 = new CellRangeAddress(0, 2, 0, lastCol);

        // 第三步，创建单元格样式（字体大小，是否加粗，是否加边框,对齐方式）
        //标题头
        XSSFCellStyle styleTitle = createCellStyle(wb, (short) 14, true, false, "C");
        //表头
        XSSFCellStyle styleTableTitle = createCellStyle(wb, (short) 9, true, true, "C");
        //表内容
        XSSFCellStyle styleTableContent = createCellStyle(wb, (short) 9, false, true, "C");
        //申请信息表的字段组
        String[] applyTitle = (String[]) dataMap.get("applyTitle");
        //申请信息表的内容组
        String[][] applyContent = (String[][]) dataMap.get("applyContent");
        // 第五步，在workbook中添加一个sheet,对应Excel文件中的sheet
        XSSFSheet sheet = wb.createSheet(sheetName);
        //设置列宽
        for (int i = 0; i <= lastCol; i++) {
            if (i == 0) {
                sheet.setColumnWidth(i, 256 * 5);
            } else {
                sheet.setColumnWidth(i, 256 * 30);
            }
        }
        //第六步，加载合并单元格对象
        sheet.addMergedRegion(callRangeAddress1);

        // 第七步，根据模板往sheet中添加数据
        XSSFRow rowTitle = sheet.createRow(0);
        //createCell获取设置单元格   起始列数
        XSSFCell cellTitle = rowTitle.createCell(0);
        cellTitle.setCellStyle(styleTitle);
        setRegionBorder(callRangeAddress1, sheet);
        cellTitle.setCellValue(titleName);
        //设置前置内容数据
        XSSFRow rowApplyTitle = sheet.createRow(3);
        for (int i = 0; i < applyTitle.length; i++) {
            //获取列属性起始单元格
            XSSFCell cellApplyTitle = rowApplyTitle.createCell(i);
            //自适应表格长度
//            sheet.autoSizeColumn(i);
            //设计表头样式
            cellApplyTitle.setCellStyle(styleTableTitle);
            //传入一个包含表头属性的list集合,生成序号那一行的表头
            cellApplyTitle.setCellValue(applyTitle[i]);
        }
        //设置申请信息表的内容
        int three = 3;
        for (int row = 4; row <= three + applyContent.length; row++) {
            //获取当前行属性
            XSSFRow rowApplyContent = sheet.createRow(row);
            for (int cell = 0; cell < applyTitle.length; cell++) {
                //获取单元格
                XSSFCell cellApplyContent = rowApplyContent.createCell(cell);
                //获取样式
                cellApplyContent.setCellStyle(styleTableContent);
                if (cell == 0) {
                    //表的字段名
                    cellApplyContent.setCellValue(row - 3);
                } else {
                    String cellValue = applyContent[row - 4][cell - 1];
                    cellApplyContent.setCellValue(cellValue);
                }
            }
        }
        sheet.setFitToPage(true);
        PrintSetup ps = sheet.getPrintSetup();
        ps.setFitHeight((short) 0);
        //纸张类型
        ps.setPaperSize(HSSFPrintSetup.A4_PAPERSIZE);
        //打印方向,为true时为横向打印
        ps.setLandscape(true);
        return wb;
    }

    /**
     * 把list转换为String[][]
     *
     * @param titles
     * @param list
     * @return
     */
    public static String[][] convertListToArray(String[] titles, List<Map<String, Object>> list) {
        //excel的主体内容
        String[][] content = new String[list.size()][];

        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> map = list.get(i);
            content[i] = new String[titles.length];
            for (int j = 0; j < titles.length; j++) {
                Object obj = map.get(titles[j]);
                if (obj == null) {
                    obj = "";
                }
                content[i][j] = obj.toString();
            }
        }
        return content;
    }

    /**
     * 字体大小，是否加粗，是否水平居中，是否加边框,左对齐"L",右对齐"R"
     *
     * @param workbook
     * @param fontsize
     * @return 单元格样式
     */
    private static XSSFCellStyle createCellStyle(XSSFWorkbook workbook, short fontsize, boolean flag, boolean flag2, String alignType) {
        XSSFCellStyle style = workbook.createCellStyle();
        //左右对齐
        if (alignType != null) {
            if (L.equalsIgnoreCase(alignType)) {
                //左对齐
                style.setAlignment(HorizontalAlignment.LEFT);
            } else if (R.equalsIgnoreCase(alignType)) {
                //右对齐
                style.setAlignment(HorizontalAlignment.RIGHT);
            } else if (C.equalsIgnoreCase(alignType)) {
                //水平居中
                style.setAlignment(HorizontalAlignment.CENTER);
            }
        }
        //是否加边框
        if (flag2) {
            //下边框
            style.setBorderBottom(BorderStyle.THIN);
            //左边框
            style.setBorderLeft(BorderStyle.THIN);
            //上边框
            style.setBorderTop(BorderStyle.THIN);
            //右边框
            style.setBorderRight(BorderStyle.THIN);
        }
        //垂直居中
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setWrapText(true);
        //创建字体
        XSSFFont font = workbook.createFont();
        //是否加粗字体
        if (flag) {
            font.setBold(true);
        }
        font.setFontHeightInPoints(fontsize);
        //加载字体
        style.setFont(font);
        return style;
    }

    public static void setRegionBorder(CellRangeAddress region, Sheet sheet) {
        RegionUtil.setBorderBottom(BorderStyle.THIN, region, sheet);
        RegionUtil.setBorderLeft(BorderStyle.THIN, region, sheet);
        RegionUtil.setBorderRight(BorderStyle.THIN, region, sheet);
        RegionUtil.setBorderTop(BorderStyle.THIN, region, sheet);
    }

    /**
     * 发送响应流方法
     *
     * @param response
     * @param fileName
     */
    public static void setWebResponseHeader(HttpServletResponse response, String fileName) {
        try {
            try {
                fileName = new String(fileName.getBytes(StandardCharsets.UTF_8), "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            response.setContentType("application/octet-stream;charset=utf-8");
            //要保存的文件名
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");
            response.addHeader("Access-Control-Expose-Headers", "Content-Disposition");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 赋值Cell
     *
     * @param row
     * @param number
     * @param type
     * @param value
     * @param style
     * @return
     */
    private static XSSFCell putCell(XSSFRow row, int number, CellType type, String value, XSSFCellStyle style) {
        XSSFCell cell = row.createCell(number, type);
        cell.setCellValue(value);
        cell.setCellStyle(style);
        return cell;
    }

    /**
     * 单元格合并方法
     *
     * @param rowNum1 开始行
     * @param rowNum2 结束行
     * @param colNum1 开始列
     * @param colNum2 结束列
     * @param sheet
     */
    private static void sheetMergedRegion(int rowNum1, int rowNum2, int colNum1, int colNum2, XSSFSheet sheet) {
        CellRangeAddress cellAddresses = new CellRangeAddress(rowNum1, rowNum2, colNum1, colNum2);
        sheet.addMergedRegion(cellAddresses);
        ExcelUtil.setRegionBorder(cellAddresses, sheet);
    }

    /**
     * 创建Excel表头
     *
     * @param rowNum1   起始行
     * @param rowNum2   结束行
     * @param colNum1   起始列
     * @param colNum2   结束列
     * @param sheetName sheet名称
     * @param titleName 表头名称
     * @param wb        工作簿
     * @return
     */
    private static XSSFSheet createSheetTitle(int rowNum1, int rowNum2, int colNum1, int colNum2, String sheetName, String titleName, XSSFWorkbook wb) {
        //标题头
        XSSFCellStyle styleTitle = createCellStyle(wb, (short) 14, true, false, "C");
        //起始行,结束行,起始列,结束列
        CellRangeAddress callRangeAddress0 = new CellRangeAddress(rowNum1, rowNum2, colNum1, colNum2);
        XSSFSheet sheet = wb.createSheet(sheetName);
        //加载表头
        //合并单元格，参数依次为起始行，结束行，起始列，结束列 （索引0开始）
        sheet.addMergedRegion(callRangeAddress0);
        ExcelUtil.setRegionBorder(callRangeAddress0, sheet);
        //根据模板往sheet中添加数据
        XSSFRow rowTitle = sheet.createRow(0);
        //createCell获取设置单元格   起始列数
        XSSFCell cellTitle = rowTitle.createCell(0);
        cellTitle.setCellStyle(styleTitle);
        setRegionBorder(callRangeAddress0, sheet);
        cellTitle.setCellValue(titleName);
        return sheet;
    }
}
