package com.he.ocr.shopsign.post;

import com.he.ocr.common.DataBean;
import com.he.ocr.common.base.BaseBean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class Image extends BaseBean {
	private DataBean image;
}
