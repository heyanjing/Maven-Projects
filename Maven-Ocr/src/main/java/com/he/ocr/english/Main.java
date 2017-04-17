package com.he.ocr.english;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.he.ocr.common.DataBean;
import com.he.ocr.common.Outputs;
import com.he.ocr.common.ResultBean;
import com.he.ocr.common.Success;
import com.he.ocr.english.post.EnglishInputs;
import com.he.ocr.english.post.Image;
import com.he.ocr.english.result.EnglishResult;
import com.he.ocr.english.result.Ret;
import com.he.ocr.util.Base64Utils;
import com.he.ocr.util.EntityUtils;
import com.he.ocr.util.HttpUtils;
import org.apache.http.HttpResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 识别度还是比较低，英文或数字的图片要正常显示，字符不能带旋转
 *
 */
public class Main {

	public static void main(String[] args) throws Exception {

		EnglishResult englishResult = ocr("D:/google.png");

	}

	/**
	 * @param imgPath 英文图片路径
	 * @throws Exception
	 */
	private static EnglishResult ocr(String imgPath) throws Exception {

		String host = "https://dm-55.data.aliyun.com";
		String path = "/rest/160601/ocr/ocr_english.json";
		String method = "POST";
		String appcode = "20c23257e77d490cb28771ca2f7c18c4";
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Authorization", "APPCODE " + appcode);
		headers.put("Content-Type", "application/json; charset=UTF-8");
		Map<String, String> querys = new HashMap<String, String>();
		String postBody = creatBody(imgPath);
		HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, postBody);
		System.out.println(response.toString());
		String responseBody = EntityUtils.entity2String(response);
		Outputs outputs = JSON.parseObject(responseBody, Outputs.class);
		ResultBean resultBean = outputs.getOutputs().get(0);
		String outputLabel = resultBean.getOutputLabel();
		Object outputMulti = resultBean.getOutputMulti();
		DataBean outputValue = resultBean.getOutputValue();
		// 将dataValue解析为对应的对象
		String dataValue = outputValue.getDataValue();
		System.out.println(dataValue);
		Success success = JSON.parseObject(dataValue, Success.class);
		if (success.isSuccess()) {
			EnglishResult parseObject = JSON.parseObject(dataValue, EnglishResult.class);
			String error_msg = parseObject.getError_msg();
			String request_id = parseObject.getRequest_id();
			List<Ret> ret = parseObject.getRet();
			String result = ret.get(0).getResult();
			System.err.println(result);
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
			EnglishInputs englishInputs = new EnglishInputs(inputs);
			return JSON.toJSONString(englishInputs);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
