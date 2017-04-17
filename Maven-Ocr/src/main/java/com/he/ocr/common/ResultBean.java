package com.he.ocr.common;

import com.he.ocr.common.base.BaseBean;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ResultBean extends BaseBean {
	private String		outputLabel;
	private Object		outputMulti;
	private DataBean	outputValue;

}
