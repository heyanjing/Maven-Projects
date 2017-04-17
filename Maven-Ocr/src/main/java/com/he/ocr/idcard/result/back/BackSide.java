package com.he.ocr.idcard.result.back;

import com.he.ocr.common.base.BaseBean;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class BackSide extends BaseBean {
	private String config_str;
	private String start_date;
	private String end_date;
	private String issue;
	private boolean success;

}
