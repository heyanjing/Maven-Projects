package com.he.ocr.license.result;

import com.he.ocr.common.base.BaseBean;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class LicenseResult extends BaseBean {
	private String	config_str;
	private String	name;
	private String	num;
	private String	vehicle_type;
	private String	start_date;
	private String	end_date;
	private String	success;
}
