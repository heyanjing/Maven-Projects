package com.he.ocr.english.result;

import com.he.ocr.common.base.BaseBean;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Rect extends BaseBean {
	private float	height;
	private float	left;
	private float	top;
	private float	width;
}
