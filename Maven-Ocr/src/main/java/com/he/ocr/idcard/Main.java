package com.he.ocr.idcard;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.he.ocr.common.DataBean;
import com.he.ocr.common.Outputs;
import com.he.ocr.common.ResultBean;
import com.he.ocr.common.Success;
import com.he.ocr.common.base.Result;
import com.he.ocr.idcard.base.Side;
import com.he.ocr.idcard.base.SideEnum;
import com.he.ocr.idcard.post.IdCardInputs;
import com.he.ocr.idcard.post.ImageAndConfigure;
import com.he.ocr.idcard.result.back.BackSide;
import com.he.ocr.idcard.result.face.FaceSide;
import com.he.ocr.util.Base64Utils;
import com.he.ocr.util.HttpUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 能正确识别身份证
 *
 */
public class Main {

	public static void main(String[] args) throws Exception {

		Result faceResult = ocr("/正面.jpg", SideEnum.Face);
		if (faceResult.isSuccess()) {
			FaceSide faceSide = (FaceSide) faceResult.getData();
			System.out.println(faceSide);
		} else {
			com.he.ocr.idcard.result.Error error = (com.he.ocr.idcard.result.Error) faceResult.getData();
			System.out.println(error.getError_msg());
		}
//		Result backResult = ocr("/背面.jpg", SideEnum.Back);
//		if (backResult.isSuccess()) {
//			BackSide backSide = (BackSide) backResult.getData();
//			System.out.println(backSide);
//		} else {
//			com.he.ocr.idcard.result.Error error = (com.he.ocr.idcard.result.Error) backResult.getData();
//			System.out.println(error.getError_msg());
//		}

	}

	/**
	 * @param idCardPath 身份证图片路径
	 * @param side SideEnum 下的枚举
	 * @return
	 * @throws Exception
	 */
	private static Result ocr(String idCardPath, SideEnum side) throws Exception {
		Result result = Result.error();
		String host = "https://dm-51.data.aliyun.com";
		String path = "/rest/160601/ocr/ocr_idcard.json";
		String method = "POST";
		String appcode = "20c23257e77d490cb28771ca2f7c18c4";
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Authorization", "APPCODE " + appcode);
		headers.put("Content-Type", "application/json; charset=UTF-8");
		Map<String, String> querys = new HashMap<String, String>();
		// 创建请求体
		String postBody = creatBody(idCardPath, SideEnum.Face.equals(side) ? SideEnum.Face.getSide() : SideEnum.Back.getSide());
		HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, postBody);
		// 获取响应体
		String responseBody = getResult(response);
		// 解析为对象
		Outputs outputs = JSON.parseObject(responseBody, Outputs.class);
		ResultBean resultBean = outputs.getOutputs().get(0);
		String outputLabel = resultBean.getOutputLabel();
		Object outputMulti = resultBean.getOutputMulti();
		DataBean outputValue = resultBean.getOutputValue();
		// 将dataValue解析为对应的对象
		String dataValue = outputValue.getDataValue();
		System.err.println(dataValue);
		Success success = JSON.parseObject(dataValue, Success.class);

		if (success.isSuccess()) {
			switch (side) {
			case Face:
				FaceSide faceSide = JSON.parseObject(dataValue, FaceSide.class);
				result.setSuccess(true);
				result.setData(faceSide);
				break;
			case Back:
				BackSide backSide = JSON.parseObject(dataValue, BackSide.class);
				result.setSuccess(true);
				result.setData(backSide);
				break;
			default:
				break;
			}
		} else {
			com.he.ocr.idcard.result.Error eror = JSON.parseObject(dataValue, com.he.ocr.idcard.result.Error.class);
			result.setData(eror);
		}
		return result;
	}

	public static <T> T parseObject(String dataValue, Class<T> clazz) {
		return JSON.parseObject(dataValue, clazz);
	}

	/**
	 * @param response
	 * @return 响应体里面的内容
	 * @throws Exception
	 */
	private static String getResult(HttpResponse response) throws Exception {
		HttpEntity entity = response.getEntity();
		InputStream in = entity.getContent();
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		byte[] tempbytes = new byte[1024];
		int len = 0;
		while ((len = in.read(tempbytes)) != -1) {
			bos.write(tempbytes, 0, len);
		}
		bos.close();
		in.close();
		String result = new String(bos.toByteArray());
		return result;
	}

	/**
	 * @param idCardPath 身份证图片路径
	 * @param witchSide face或back
	 * @return 返回请求体
	 */
	private static String creatBody(String idCardPath, String witchSide) {
		try {
			// image
			String base64Str = Base64Utils.encodeFile(idCardPath);
			DataBean image = new DataBean(50, base64Str);
			// configure
			Side side = new Side(witchSide);
			String sideBeanStr = JSON.toJSONString(side);
			DataBean configure = new DataBean(50, sideBeanStr);
			// image+configure
			ImageAndConfigure imageAndConfigure = new ImageAndConfigure(image, configure);
			// inputs
			List<ImageAndConfigure> list = Lists.newArrayList();
			list.add(imageAndConfigure);
			IdCardInputs inputs = new IdCardInputs(list);
			String json = JSON.toJSONString(inputs);
			return json;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
