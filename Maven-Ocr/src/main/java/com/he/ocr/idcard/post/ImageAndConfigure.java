package com.he.ocr.idcard.post;


import com.he.ocr.common.DataBean;
import com.he.ocr.common.base.BaseBean;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ImageAndConfigure extends BaseBean {
	private DataBean	image;
	private DataBean	configure;
}
