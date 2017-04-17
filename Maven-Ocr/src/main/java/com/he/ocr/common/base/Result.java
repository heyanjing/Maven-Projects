package com.he.ocr.common.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Result extends BaseBean {
	private boolean	success;
	private Object	data;

	public static Result error() {
		return new Result(false, null);
	}

	public static Result success() {
		return new Result(true, null);
	}
}
