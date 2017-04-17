package com.he.ocr.shopsign;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.he.ocr.common.DataBean;
import com.he.ocr.common.Outputs;
import com.he.ocr.common.ResultBean;
import com.he.ocr.common.Success;
import com.he.ocr.shopsign.post.Image;
import com.he.ocr.shopsign.post.ShopSignInputs;
import com.he.ocr.shopsign.result.ShopSignResult;
import com.he.ocr.util.Base64Utils;
import com.he.ocr.util.EntityUtils;
import com.he.ocr.util.HttpUtils;
import org.apache.http.HttpResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 字迹清晰的门店图片能识别出来
 *
 */
public class Mian {

	public static void main(String[] args) throws Exception {
//		ocr("/刘一手.jpg");
		ocr("/肯德基.jpg");
	}

	/**
	 * @param imgPath 门店图片地址
	 * @throws Exception
	 */
	private static ShopSignResult ocr(String imgPath) throws Exception {
		String host = "https://dm-54.data.aliyun.com";
		String path = "/rest/160601/ocr/ocr_shop_sign.json";
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
			ShopSignResult parseObject = JSON.parseObject(dataValue, ShopSignResult.class);
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
			ShopSignInputs shopSignInputs = new ShopSignInputs(inputs);
			return JSON.toJSONString(shopSignInputs);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
