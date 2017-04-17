package com.he.poi.test;

import org.apache.poi.hssf.converter.ExcelToHtmlConverter;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.converter.PicturesManager;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.hwpf.usermodel.Picture;
import org.apache.poi.hwpf.usermodel.PictureType;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.List;

public class TestPoi {
    /**
     * 将EXCEL文档转换成HTML文档。
     * 
     * @author Chrise 2015-10-12
     * @param filePath
     *            EXCEL文档文件路径。
     * @return 返回HTML文档对象。
     * @throws Exception
     *             抛出转换异常。
     */
    private Document excelToHtmlByPoi(final String filePath) throws Exception {
        HSSFWorkbook excel = new HSSFWorkbook(new FileInputStream(filePath));

        // 转换EXCEL文档
        ExcelToHtmlConverter converter = new ExcelToHtmlConverter(DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument());
        converter.setOutputColumnHeaders(false);
        converter.setOutputRowNumbers(false);
        converter.processWorkbook(excel);

        return converter.getDocument();
    }

    /**
     * 将WORD文档转换成HTML文档。
     * 
     * @author Chrise 2015-10-12
     * @param filePath
     *            WORD文档文件路径。
     * @param pictureDir
     *            图片目录名。
     * @return 返回HTML文档对象。
     * @throws Exception
     *             抛出转换异常。
     */
    private Document wordToHtmlByPoi(final String filePath, final String pictureDir) throws Exception {
        HWPFDocument word = new HWPFDocument(new FileInputStream(filePath));

        // 转换WORD文档
        WordToHtmlConverter converter = new WordToHtmlConverter(DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument());
        converter.setPicturesManager(new PicturesManager() {
            @Override
            public String savePicture(byte[] content, PictureType pictureType, String suggestedName, float widthInches, float heightInches) {
                return "图片路径";// rootVirtual + previewRoot + pictureDir + "/" + suggestedName;
            }
        });
        converter.processDocument(word);

        List<Picture> pictures = word.getPicturesTable().getAllPictures();
        if (pictures != null && !pictures.isEmpty()) {
            // 检查图片保存目录
            String dir = "";// this.rootReal + this.previewRoot + pictureDir;
            new File(dir).mkdirs();

            // 保存WORD文档中的图片
            for (Picture picture : pictures) {
                String file = dir + "/" + picture.suggestFullFileName();
                if (new File(file).exists())
                    continue;

                picture.writeImageContent(new FileOutputStream(file));
            }
        }

        return converter.getDocument();
    }

    /**
     * 保存HTML文档。
     * 
     * @author Chrise 2015-10-12
     * @param document
     *            HTML文档对象。
     * @param html
     *            HTML文档路径。
     * @throws Exception
     *             抛出保存文件异常。
     */
    private void saveHtmlDocument(Document document, String html) throws Exception {
        if (document == null)
            return;

        ByteArrayOutputStream out = null;
        FileOutputStream fos = null;
        BufferedWriter bw = null;

        try {
            out = new ByteArrayOutputStream();
            StreamResult result = new StreamResult(out);

            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.METHOD, "html");
            transformer.transform(new DOMSource(document), result);

            String content = new String(out.toByteArray(), "UTF-8");

            fos = new FileOutputStream(html);
            bw = new BufferedWriter(new OutputStreamWriter(fos, "UTF-8"));
            bw.write(content);
        } finally {
            if (out != null)
                out.close();
            if (bw != null)
                bw.close();
            if (fos != null)
                fos.close();
        }
    }

}
