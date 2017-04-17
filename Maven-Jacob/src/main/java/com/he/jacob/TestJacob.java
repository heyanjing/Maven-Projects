package com.he.jacob;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

public class TestJacob {

    /**
     * 将word或excel文档转换成HTML文档。
     * 
     * @author Chrise 2015-12-20
     * @param filePath
     *            待转换文档文件路径。
     * @param fileSuffix
     *            待转换文档文件后缀。
     * @param savePath
     *            HTML文档保存路径。
     * @throws Exception
     *             抛出转换异常。
     */
    private void docToHtmlByJacob(String filePath, String fileSuffix, String savePath) throws Exception {
        int type = 0;
        String property = null;
        ActiveXComponent com = null;

        try {
            // 创建COM对象
            if ("doc".equals(fileSuffix) || "docx".equals(fileSuffix)) {
                type = 8;
                property = "Documents";
                com = new ActiveXComponent("Word.Application");
            } else if ("xls".equals(fileSuffix) || "xlsx".equals(fileSuffix)) {
                type = 44;
                property = "Workbooks";
                com = new ActiveXComponent("Excel.Application");
            }

            // 调用COM转换文档
            if (com != null) {
                com.setProperty("Visible", new Variant(false));
                Dispatch docs = com.getProperty(property).toDispatch();
                Dispatch doc = Dispatch.invoke(docs, "Open", Dispatch.Method, new Object[] { filePath, new Variant(false), new Variant(true) }, new int[1]).toDispatch();
                Dispatch.invoke(doc, "SaveAs", Dispatch.Method, new Object[] { savePath, new Variant(type) }, new int[1]);
                Dispatch.call(doc, "Close", new Variant(false));
            }
        } finally {
            // 释放COM对象
            if (com != null) {
                com.invoke("Quit", new Variant[] {});
                ComThread.Release();
            }
        }
    }

}
