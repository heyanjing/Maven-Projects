package com.he.ocr.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataBean {
	private String	dataValue;
	private int		dataType;

	public DataBean(int dataType, String dataValue) {
		this.dataType = dataType;
		this.dataValue = dataValue;
	}

}
