package com.he.poi.test;

import com.he.poi.funs.CalculateMortgage;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.formula.functions.FreeRefFunction;
import org.apache.poi.ss.formula.udf.DefaultUDFFinder;
import org.apache.poi.ss.formula.udf.UDFFinder;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class T {
    public static void main(String[] args) {
        T.ttttt();
    }

    public static Map<String, Object> ttttt() {
        Map<String, Object> result = HeyjUtil.newHashMap();
        Boolean oneTime = true;
        // System.out.println("支持--->" + FunctionEval.getSupportedFunctionNames());
        // System.out.println("不支持->" + FunctionEval.getNotSupportedFunctionNames());
        try {
            String path = "D:/temp/静观农村公路审核明细.xlsx";
            path = "D:/temp/（改备注）重庆金融后援服务中心大楼及环境工程.xls";
            path = "D:/temp/t/03.xls";
            path = "D:/test/projects/poi/src/main/java/com/he/poi/funs/mortgage-calculation.xls";

            File file = new File(path);
            FileInputStream in = new FileInputStream(file);
            Workbook wb = null;
            if (file.getName().endsWith(".xls")) {
                wb = new HSSFWorkbook(in);
            } else if (file.getName().endsWith(".xlsx")) {
                wb = new XSSFWorkbook(in);
            }

            if (wb != null) {// 想wb中添加自定义函数
                String[] functionNames = { "calculatePayment", "he" };
                FreeRefFunction[] functionImpls = { new CalculateMortgage(), new IMSUB() };

                UDFFinder udfToolpack = new DefaultUDFFinder(functionNames, functionImpls);

                // register the user-defined function in the workbook
                wb.addToolPack(udfToolpack);
            }
            System.out.println(wb.getNumberOfSheets());

            FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

            if (wb != null) {
                Sheet sheet = wb.getSheetAt(0);
                System.err.println(sheet.getSheetName());
                int realRow = ReadExcel.getRealRowNum(sheet);// 真实行数
                List<CellRangeAddress> list = HeyjUtil.newArrayList();
                T.getCombineCell(sheet, list);
                Map<Integer, List<CellProperties>> maps = HeyjUtil.newHashMap();// 所有显示单元格
                List<CellProperties> cellPros = HeyjUtil.newArrayList();// 合并单元格集合
                List<CellProperties> heads = HeyjUtil.newArrayList();// 表头
                for (int i = 0; i < realRow; i++) {
                    System.out.println("第" + (i + 1) + "行========================================================");
                    List<CellProperties> rowCells = HeyjUtil.newArrayList();// 一行中需要显示的单元格
                    Row row = sheet.getRow(i);
                    float rowHeight = row.getHeightInPoints();// 行高
                    rowHeight *= 1.5;
                    short colNum = row.getLastCellNum();// 列数
                    if (oneTime) {
                        oneTime = false;
                        result.put("colNum", colNum);
                    }
                    for (int j = 0; j < colNum; j++) {
                        System.out.print("*第" + (j + 1) + "列*-->");
                        Cell cell = row.getCell(j);
                        float columnWidth = sheet.getColumnWidthInPixels(j);// 列宽
                        columnWidth *= 1.2;
                        if (cell != null) {
                            CellProperties cellPro = T.isCombineCell(list, cell);
                            cellPro.setColumnWidth(columnWidth);
                            cellPro.setRowHeight(rowHeight);
                            switch (evaluator.evaluateInCell(cell).getCellType()) {
                                case Cell.CELL_TYPE_BOOLEAN:
                                    cellPro.setContent(String.valueOf(cell.getBooleanCellValue()));
                                    break;
                                case Cell.CELL_TYPE_NUMERIC:
                                    cellPro.setContent(String.valueOf(HeyjUtil.numFormat("0.00", cell.getNumericCellValue())));
                                    break;
                                case Cell.CELL_TYPE_STRING:
                                    cellPro.setContent(String.valueOf(cell.getStringCellValue().trim()));
                                    break;
                                case Cell.CELL_TYPE_BLANK:
                                    cellPro.setContent("");
                                    break;
                                case Cell.CELL_TYPE_ERROR:
                                    cellPro.setContent("格式错误");
                                    break;
                                case Cell.CELL_TYPE_FORMULA:
                                    break;

                            }
                            // System.out.println("***第" + (j + 1) + "列** " + cellPro);
                            Boolean flag = false;// 默认不是合并单元格或者该合并单元格不在cellPros集合中
                            if (cellPro.getIsMerge()) {
                                j = cellPro.getLastColumn() - 1;
                                flag = T.isInMerge(cellPros, cellPro);
                            } else {
                                cellPro.setFirstColumn(j + 1);
                                cellPro.setLastColumn(j + 1);
                                cellPro.setFirstRow(i + 1);
                                cellPro.setLastRow(i + 1);
                            }
                            if (!flag) {
                                rowCells.add(cellPro);
                            }
                            if (cellPro.getIsMerge() && i == 1 && cellPro.getOccupyRow() > 1) {
                                heads.add(cellPro);
                            }
                            if (!cellPro.getIsMerge() && i == 2) {
                                heads.add(cellPro);
                            }
                            System.out.println(cellPro.getContent());
                        } else {
                            System.out.println("空");
                        }
                    }
                    maps.put(i + 1, rowCells);
                }
                // for (Entry<Integer, List<CellProperties>> en : maps.entrySet()) {
                // System.out.println("**********************************************************************************************************第" + en.getKey() + "行**********************************************************************************************************");
                // for (CellProperties cp : en.getValue()) {
                // System.out.println(cp);
                // }
                // }
                Collections.sort(heads, new CellPropertiesComparator());
                result.put("heads", heads);
                result.put("maps", maps);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }

    /**
     * cellPro 是否在cellPros 集合中
     * 
     * @param cellPros
     * @param cellPro
     */
    public static Boolean isInMerge(List<CellProperties> cellPros, CellProperties cellPro) {
        Boolean flag = false;
        int index = 0;
        CellProperties temp = null;
        for (int i = 0; i < cellPros.size(); i++) {
            CellProperties cp = cellPros.get(i);
            if (cp.getFirstRow() <= cellPro.getFirstRow() && cp.getLastRow() >= cellPro.getLastRow() && cp.getFirstColumn() <= cellPro.getFirstColumn() && cellPro.getLastColumn() <= cp.getLastColumn()) {
                flag = true;
                index = i;
                temp = cp;
                break;
            }
        }
        if (flag) {
            if (HeyjUtil.isNotBlank(cellPro.getContent()) && HeyjUtil.isBlank(temp.getContent())) {
                cellPros.remove(index);
                cellPros.add(index, cellPro);
            }
        }
        if (!flag) {// 不在合并单元格集合中，则添加到集合中
            cellPros.add(cellPro);
        }
        return flag;
    }

    /**
     * 合并单元格处理--加入list
     * 
     * @param sheet
     * @return
     */
    public static void getCombineCell(Sheet sheet, List<CellRangeAddress> list) {
        // 获得一个 sheet 中合并单元格的数量
        int sheetmergerCount = sheet.getNumMergedRegions();
        // 遍历合并单元格
        for (int i = 0; i < sheetmergerCount; i++) {
            // 获得合并单元格加入list中
            CellRangeAddress ca = sheet.getMergedRegion(i);
            list.add(ca);
        }
    }

    /**
     * 判断单元格是否为合并单元格
     * 
     * @param listCombineCell
     *            存放合并单元格的list
     * @param cell
     *            需要判断的单元格
     * @return
     */
    public static CellProperties isCombineCell(List<CellRangeAddress> listCombineCell, Cell cell) {
        CellProperties cellPro = new CellProperties(false, 1, 1);
        int firstC = 0;
        int lastC = 0;
        int firstR = 0;
        int lastR = 0;
        for (CellRangeAddress ca : listCombineCell) {
            // 获得合并单元格的起始行, 结束行, 起始列, 结束列
            firstC = ca.getFirstColumn();
            lastC = ca.getLastColumn();
            firstR = ca.getFirstRow();
            lastR = ca.getLastRow();
            if (cell.getColumnIndex() <= lastC && cell.getColumnIndex() >= firstC && cell.getRowIndex() <= lastR && cell.getRowIndex() >= firstR) {
                cellPro.setIsMerge(true);
                cellPro.setOccupyColumn(lastC - firstC + 1);
                cellPro.setOccupyRow(lastR - firstR + 1);
                cellPro.setFirstColumn(firstC + 1);
                cellPro.setLastColumn(lastC + 1);
                cellPro.setFirstRow(firstR + 1);
                cellPro.setLastRow(lastR + 1);
            }
        }
        return cellPro;
    }

}
