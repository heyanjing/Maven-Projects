package com.he.poi.word;

import com.google.common.collect.Maps;
import com.he.module.util.Https;
import com.he.module.util.Jsons;

import java.util.List;
import java.util.Map;

public class WordTest {

	public static void main(String[] args) throws Exception {
		获取所有企业();
		
	}

	private static void 获取所有企业() throws Exception {
		String url="http://cqkfb.com/pcs/fdcqyContr/queryAllFdcqyxxContr.do";
		Map<String,Object> params=Maps.newHashMap();
//		params.put("pageIndex", "1");
		params.put("data", "1");
//		params.put("fdata", "重庆亿泊互联置业发展有限公司");
		for (int i = 101; i <=189; i++) {//189
			params.put("pageIndex", i);
			QiYePage page=Jsons.toBean(Https.post(url, params), QiYePage.class);
			List<QiYe> qiyeList=page.getQyxxlist();
			for (QiYe qiYe : qiyeList) {
				System.out.println(qiYe.getId()+","+qiYe.getQymc());
			}
		}
	}

}
