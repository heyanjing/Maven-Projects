package com.he.poi.test;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author Chrise
 */
public class HeyjUtil {
    public static final String FORMAT_DATE     = "yyyy-MM-dd";
    public static final String FORMAT_DATETIME = "yyyy-MM-dd HH:mm:ss";

    /**
     * @param str 逗号分割的字符串
     * @return 逗号分隔的数字字符串转换为list集合
     */
    public static List<String> strToStrList(String str) {
        List<String> list = HeyjUtil.newArrayList();
        String[] strs = str.split(",");
        for (int i = 0; i < strs.length; i++) {
            list.add(strs[i]);
        }
        return list;
    }

    public static List<Integer> strToIntList(String str) {
        List<Integer> list = new ArrayList<Integer>();
        String[] strs = str.split(",");
        for (int i = 0; i < strs.length; i++) {

            list.add(Integer.parseInt(strs[i]));
        }
        return list;
    }

    public static <E> ArrayList<E> newArrayList() {
        return new ArrayList<E>();
    }

    public static <K, V> HashMap<K, V> newHashMap() {
        return new HashMap<K, V>();
    }

    public static <K, V> HashMap<K, V> LinkedHashMap() {
        return new LinkedHashMap<K, V>();
    }

    public static <E> HashSet<E> newHashSet() {
        return new HashSet<E>();

    }

    public static <E> HashSet<E> newHashSet(Collection<? extends E> c) {
        return new HashSet<E>(c);

    }

    public static <E> LinkedHashSet<E> newLinkedHashSet(Collection<? extends E> c) {
        return new LinkedHashSet<E>(c);

    }

    public static SimpleDateFormat newSdf(String pattern) {
        return new SimpleDateFormat(pattern);
    }

    public static SimpleDateFormat newDefaultSdf() {
        return newSdf(FORMAT_DATE);
    }

    public static Date parseDate(String date) {
        Date d = null;
        String format = null;
        try {
            if (date.length() == FORMAT_DATE.length()) {
                format = FORMAT_DATE;
            } else {
                format = FORMAT_DATETIME;
            }
            d = newSdf(format).parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return d;
    }

    // /**
    // * 下载
    // *
    // * @author Heyj 2015-10-27
    // * @param path 文件真实路径
    // * @param name 文件名，如果为null，则文件名默认为当前时间的毫秒值
    // * @param response
    // */
    // public static void download(String path, String name, HttpServletResponse response,HttpServletRequest request)throws Exception {
    // String downLoadName = null;
    // if (name == null) {
    // String suffix = path.substring(path.lastIndexOf("."), path.length());
    // name = System.currentTimeMillis() + suffix;
    // } else{
    // name=name.replaceAll(" ", "_");
    // }
    // String agent = request.getHeader("USER-AGENT");
    // if (null != agent && -1 != agent.indexOf("Firefox")) { // Firefox
    // downLoadName = new String(name.getBytes("UTF-8"), "iso-8859-1");
    // }else if (null != agent && -1 != agent.indexOf("Mozilla")){ //IE
    // downLoadName = URLEncoder.encode(name, "UTF-8");
    // } else {
    // downLoadName = URLEncoder.encode(name, "UTF-8");
    // }
    // response.setHeader("Content-Disposition", "attachment;filename=\"" +downLoadName);
    // FileInputStream fis = null;
    // OutputStream os = null;
    // byte[] buffer = new byte[1024];
    // int len = 0;
    // try {
    // fis = new FileInputStream(path);
    // os = response.getOutputStream();
    // while ((len = fis.read(buffer)) > 0) {
    // os.write(buffer, 0, len);
    // }
    // } catch (Exception e) {
    // throw e;
    // } finally {
    // try {
    // os.close();
    // fis.close();
    // os.flush();
    // } catch (IOException e) {
    // e.printStackTrace();
    // }
    // }
    // }

    // /**
    // * 获取表单中的所有上传文件
    // *
    // * @author Heyj 2015-11-7
    // * @param request
    // * @return
    // */
    // @SuppressWarnings("unchecked")
    // public static List<FileItem> getsUploadFileItem(HttpServletRequest request) {
    // List<FileItem> list = HeyjUtil.newArrayList();
    // try {
    // DiskFileItemFactory factory = new DiskFileItemFactory();
    // // 2、创建一个文件上传解析器
    // ServletFileUpload upload = new ServletFileUpload(factory);
    // // 解决上传文件名的中文乱码
    // upload.setHeaderEncoding("UTF-8");
    // // 3、判断提交上来的数据是否是上传表单的数据
    // if (!ServletFileUpload.isMultipartContent(request)) {
    // // 按照传统方式获取数据
    // return null;
    // }
    // // 4、使用ServletFileUpload解析器解析上传数据，解析结果返回的是一个List<FileItem>集合，每一个FileItem对应一个Form表单的输入项
    // List<FileItem> items = upload.parseRequest(request);
    // for (FileItem item : items) {
    // // 如果fileitem中封装的是普通输入项的数据
    // if (!item.isFormField()) {
    // list.add(item);
    // }
    // }
    // } catch (FileUploadException e) {
    // e.printStackTrace();
    // }
    // return list;
    // }

    public static Boolean isBlank(String str) {
        if (str == null || str.equals("null") || str.equals("") || str.equals("-1") || str.contains("请选择")) {
            return true;
        }
        return false;
    }

    public static Boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    public static String getLikeStr(String str) {
        return "%" + str + "%";
    }

    public static StringBuilder newSb() {
        return new StringBuilder();
    }

    /**
     * @param str 字符串
     * @param reg 正则
     * @return str是否与正则reg相匹配
     */
    public static Boolean isReg(String str, String reg) {
        if (!str.equals("")) {
            Pattern p = Pattern.compile(reg);
            return p.matcher(str).matches();
        }
        return true;
    }

    /**
     * @param strs 字符串的集合
     * @param reg 正则
     * @return strs 是否全部与正则reg相匹配
     */
    public static Boolean isRegAll(List<String> strs, String reg) {
        for (String str : strs) {
            if (!isReg(str, reg)) {
                return false;
            }
        }
        return true;
    }

    // public static void readDoc(HttpServletRequest request, HttpServletResponse response, Log log, CommonService commonService, UserSession userSession, String realPath) {
    // try {
    // String pathJson = commonService.generatePreviewByPoi(realPath);
    // request.setAttribute("pathJson", pathJson);// 路径
    // request.getRequestDispatcher("/WEB-PAGE/common/ReadTheDocumentHtml.jsp").forward(request, response);
    // commonService.saveSystemLog(userSession.getLoginname(), userSession.getLoginaddress(), new Date(), "预览文档");
    // } catch (Exception e) {
    // log.error("预览文档 erro -->" + e.getMessage(), e);
    // }
    // }

    // public static void readImg(HttpServletRequest request, HttpServletResponse response, Log log, CommonService commonService, UserSession userSession) {
    // try {
    // request.getRequestDispatcher("/WEB-PAGE/common/ReadTheImageHtml.jsp").forward(request, response);
    // commonService.saveSystemLog(userSession.getLoginname(), userSession.getLoginaddress(), new Date(), "预览图片");
    // } catch (Exception e) {
    // log.error("预览图片 erro -->" + e.getMessage(), e);
    // }
    // }

    /**
     * 获得与t集合size相同的全部是e的集合
     */
    public static <E> ArrayList<E> getList(int size, E e) {
        ArrayList<E> list = HeyjUtil.newArrayList();
        for (int i = 0; i < size; i++) {
            list.add(e);
        }
        return list;
    }

    /**
     * 获取sql查询时 id in( ids )时的ids
     */
    public static String getFindInIds(List<String> ids) {
        String result = "";
        if (ids.size() == 1) {
            return ids.get(0);
        } else if (ids.size() > 1) {
            for (int i = 0; i < ids.size(); i++) {
                String str = ids.get(i);
                if (isBlank(result)) {
                    result += str + "'";
                } else {
                    if (i == ids.size() - 1) {
                        result += ",'" + str;
                    } else {
                        result += ",'" + str + "'";
                    }
                }
            }
        }
        return result;
    }

    /**
     * 复制不为空的属性
     */
    public static void copyNotNullProperty(Object target, Object source) {
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(source.getClass());
            PropertyDescriptor[] pdList = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor pd : pdList) {
                Method writeMethod = pd.getWriteMethod();
                Method readMethod = pd.getReadMethod();
                if (readMethod == null || writeMethod == null) {
                    continue;
                }
                Object val = readMethod.invoke(source);
                if (val != null) {
                    writeMethod.invoke(target, val);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public static boolean deleteFile(File file) {
        return file.delete();
    }

    public static void deleteFileDeep(File file) {
        file.delete();
        File parentFile = file.getParentFile();
        if (parentFile.listFiles().length == 0) {
            deleteFileDeep(parentFile);
        }
    }

    public static File newFile(String path) {
        return new File(path);
    }

    public static String numFormat(String pattern, Object num) {
        DecimalFormat df = new DecimalFormat(pattern);
        return df.format(num);
    }
}
