package com.he.ocr.license;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.he.ocr.common.DataBean;
import com.he.ocr.common.Outputs;
import com.he.ocr.common.ResultBean;
import com.he.ocr.common.Success;
import com.he.ocr.license.post.Image;
import com.he.ocr.license.post.LicenseInputs;
import com.he.ocr.license.result.LicenseResult;
import com.he.ocr.util.Base64Utils;
import com.he.ocr.util.EntityUtils;
import com.he.ocr.util.HttpUtils;

/**
 * 能正确识别驾驶证
 *
 */
public class Main {

	public static void main(String[] args) throws Exception {
		LicenseResult ocr = ocr("/驾照.jpg");
	}

	/**
	 * @param imgPath 驾驶证图片地址
	 * @throws Exception
	 */
	private static LicenseResult ocr(String imgPath) throws Exception {
		String host = "https://dm-52.data.aliyun.com";
		String path = "/rest/160601/ocr/ocr_driver_license.json";
		String method = "POST";
		String appcode = "20c23257e77d490cb28771ca2f7c18c4";
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Authorization", "APPCODE " + appcode);
		headers.put("Content-Type", "application/json; charset=UTF-8");
		Map<String, String> querys = new HashMap<String, String>();
		String postBody = creatBody(imgPath);
		HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, postBody);
		String responseBody = EntityUtils.entity2String(response);
		System.err.println(responseBody);
		Outputs outputs = JSON.parseObject(responseBody, Outputs.class);
		ResultBean resultBean = outputs.getOutputs().get(0);
		String outputLabel = resultBean.getOutputLabel();
		Object outputMulti = resultBean.getOutputMulti();
		DataBean outputValue = resultBean.getOutputValue();
		// 将dataValue解析为对应的对象
		String dataValue = outputValue.getDataValue();
		Success success = JSON.parseObject(dataValue, Success.class);
		if (success.isSuccess()) {
			LicenseResult parseObject = JSON.parseObject(dataValue, LicenseResult.class);
			System.out.println(parseObject);
			return parseObject;
		} else {
			return null;
		}
	}

	private static String creatBody(String imgPath) {
		try {
			// dataType+dataValue
			String base64Str = Base64Utils.encodeFile(imgPath);
			DataBean dataBean = new DataBean(50, base64Str);
			// image
			Image image = new Image(dataBean);
			// inputs
			List<Image> inputs = Lists.newArrayList();
			inputs.add(image);
			LicenseInputs licenseInputs = new LicenseInputs(inputs);
			return JSON.toJSONString(licenseInputs);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
