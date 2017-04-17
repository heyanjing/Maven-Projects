package com.he.poi.test;

import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * @author Heyj
 */
public class ReadExcel {
    /**
     * 材料模板表头
     */
    public static String[]   METERIAL = { "材料类别(必填)", "材料名称(必填)", "规格型号(必填)", "材料单位(必填)", "材料品牌", "材料价格(必填)", "采价日期(2015/11/11)(必填)", "经 销 商", "材料备注" };
    /**
     * 材料核价模板表头
     */
    public static String[]   VERIFY   = { "材料类别", "材料名称(必填)", "规格型号(必填)", "材料单位(必填)", "材料品牌", "拟购数量", "施工报价", "监理核价", "建设核价", "跟审核价", "核定价(必填)", "采管费计取(是/否)(必填)", "采价日期(2015/11/11)(必填)", "材料备注" };
    /**
     * 选择表中对应的字段
     */
    public static String[][] TABLE    = { { "未匹配", "-1" }, { "项目编码", "WorkCode" }, { "项目名称", "WorkName" }, { "项目特征及主要工作内容", "FeatureContent" }, { "计量单位", "MeterialUnit" }, { "工程量", "WorkAmount" }, { "综合单价", "WorkPrice" }, { "合价", "WorkTotal" } };
    /**
     * 选择表中对应的字段
     */
    public static String[][] STATE    = { { "无效", "0" }, { "有效", "1" } };

    /**
     * 获取表的字段name与对应的code
     */
    public static List<Table> getTables() {
        List<Table> tables = HeyjUtil.newArrayList();
        for (int i = 0; i < ReadExcel.TABLE.length; i++) {
            Table table = new Table(ReadExcel.TABLE[i][0], ReadExcel.TABLE[i][1]);
            tables.add(table);
        }
        return tables;
    }

    /**
     * 获取状态
     */
    public static List<State> getState() {
        List<State> states = HeyjUtil.newArrayList();
        for (int i = 0; i < ReadExcel.STATE.length; i++) {
            State state = new State(ReadExcel.STATE[i][0], ReadExcel.STATE[i][1]);
            states.add(state);
        }
        return states;
    }

    /**
     * 获取必填信息的角标
     */
    public static List<Integer> findMustIndex(String[] defaultTitle) {
        List<Integer> list = HeyjUtil.newArrayList();
        for (int i = 0; i < defaultTitle.length; i++) {
            String str = defaultTitle[i];
            if (str.contains("必填")) {
                list.add(i);
            }
        }
        return list;
    }

    /**
     * 判断excel表中一行数据中必填信息是否填写
     */
    public static Boolean isMustOk(String[] defaultTitle, String[] rowContent) {
        List<String> strs = HeyjUtil.newArrayList();
        List<Integer> indexs = findMustIndex(defaultTitle);
        for (Integer index : indexs) {
            strs.add(rowContent[index]);
        }
        if (strs.size() > 0) {
            for (String str : strs) {
                if (str.equals("")) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 读取Excel表格表头的内容
     */
    public static String[] readExcelTitle(Workbook wb) {
        Sheet sheet = wb.getSheetAt(0);
        Row row = sheet.getRow(1);// 表头从第二行开始，第一行为提示信息
        // 标题总列数
        int colNum = row.getPhysicalNumberOfCells();
        String[] title = new String[colNum];
        for (int i = 0; i < colNum; i++) {
            title[i] = getStringCellValue(row.getCell((Integer) i));
        }
        return title;
    }

    /**
     * 读取Excel数据内容
     */
    public static Map<Integer, List<String>> readExcelContent(Workbook wb) {
        Map<Integer, List<String>> content = HeyjUtil.newHashMap();
        Sheet sheet = wb.getSheetAt(0);
        // 得到总行数
        // int rowNum = sheet.getLastRowNum();//包含了空行数据在内
        int rowNum = getRealRowNum(sheet); // 去除空行数据后的真实行数
        Row row = sheet.getRow(1);// 表头从第二行开始，第一行为提示信息
        int colNum = row.getPhysicalNumberOfCells();
        // 正文内容应该从第三行开始,，第一行为提示信息，第二行为表头的标题
        for (int i = 2; i < rowNum; i++) {
            List<String> contents = HeyjUtil.newArrayList();
            row = sheet.getRow(i);
            if (row != null) {
                for (int j = 0; j < colNum; j++) {
                    // 每个单元格的数据内容用"-"分割开，以后需要时用String类的replace()方法还原数据
                    // 也可以将每个单元格的数据设置到一个javabean的属性中，此时需要新建一个javabean
                    contents.add(getStringCellValue(row.getCell(j)).trim());
                }
                content.put(i, contents);// 将每一行的数据加入集合
            }
        }
        return content;
    }

    /**
     * 将单元格数据转换为字符串
     */
    private static String getStringCellValue(Cell cell) {
        String strCell = "";
        if (cell != null) {
            switch (cell.getCellType()) {
                case HSSFCell.CELL_TYPE_STRING:
                    strCell = cell.getStringCellValue().replaceAll(" ", "");
                    break;
                case HSSFCell.CELL_TYPE_NUMERIC:
                    if (HSSFDateUtil.isCellDateFormatted(cell)) {
                        Date date = cell.getDateCellValue();
                        if (date != null) {
                            SimpleDateFormat sdf = HeyjUtil.newSdf("yyyy/MM/dd");
                            strCell = sdf.format(date);
                            // strCell = String.valueOf(date.getTime());
                        }
                    } else {
                        DecimalFormat df = new DecimalFormat("0.00");
                        strCell = String.valueOf(df.format(cell.getNumericCellValue())).trim();
                        // if (strCell.indexOf(".") != -1) {
                        // strCell = strCell.substring(0, strCell.indexOf(".")).trim();
                        // }
                    }
                    break;
                case HSSFCell.CELL_TYPE_BOOLEAN:
                    strCell = String.valueOf(cell.getBooleanCellValue()).trim();
                    break;
                case HSSFCell.CELL_TYPE_BLANK:
                    strCell = "";
                    break;
                default:
                    strCell = "";
                    break;
            }
        }
        if (strCell.equals("") || strCell == null) {
            return "";
        }
        return strCell;
    }

    /**
     * 获取excel真实行数
     */
    public static Integer getRealRowNum(Sheet sheet) {
        Integer realNum = 0;
        CellReference cellReference = new CellReference("A1");
        boolean flag = false;
        for (int i = cellReference.getRow(); i <= sheet.getLastRowNum();) {
            Row r = sheet.getRow(i);
            if (r == null) {
                // 如果是空行（即没有任何数据、格式），直接把它以下的数据往上移动
                sheet.shiftRows(i + 1, sheet.getLastRowNum(), -1);
                continue;
            }
            flag = false;
            for (Cell c : r) {
                if (c.getCellType() != Cell.CELL_TYPE_BLANK) {
                    flag = true;
                    break;
                }
            }
            if (flag) {
                i++;
                continue;
            } else {// 如果是空白行（即可能没有数据，但是有一定格式）
                if (i == sheet.getLastRowNum()) {
                    // 如果到了最后一行，直接将那一行remove掉
                    sheet.removeRow(r);
                } else {
                    // 如果还没到最后一行，则数据往上移一行
                    sheet.shiftRows(i + 1, sheet.getLastRowNum(), -1);
                }
            }
        }
        realNum = sheet.getLastRowNum() + 1;
        return realNum;
    }

    /**
     * 获取excel表
     * @throws Exception 
     */
    public static Workbook getWorkBook(InputStream is, String fileName) throws Exception {
        Workbook wb = null;
        if (fileName.endsWith(".xls")) {
            wb = new HSSFWorkbook(new POIFSFileSystem(is));
        } else if (fileName.endsWith(".xlsx")) {
            wb = new XSSFWorkbook(OPCPackage.open(is));
        }
        // HSSFWorkbook wb = null;
        // try {
        // POIFSFileSystem fs = new POIFSFileSystem(is);
        // wb = new HSSFWorkbook(fs);
        // } catch (IOException e) {
        // e.printStackTrace();
        // }
        return wb;
    }

    /**
     * 判断表头信息是否与defaultTitle相同
     */
    public static Boolean isTemplate(Workbook wb, String[] defaultTitle) {
        Boolean flag = false;
        String[] titles = ReadExcel.readExcelTitle(wb);
        if (Arrays.equals(defaultTitle, titles)) {
            flag = true;
        } else {
            flag = false;
        }
        return flag;
    }
}
