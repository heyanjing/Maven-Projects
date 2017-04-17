package com.he.ocr.util;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

public class EntityUtils {
	public static String entity2String(HttpResponse response) throws Exception {
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
}
