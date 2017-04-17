package com.he.ocr.english.result;

import com.he.ocr.common.base.BaseBean;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class EnglishResult extends BaseBean {
	private String		error_msg;
	private String		request_id;
	private List<Ret>	ret;
	private boolean		success;

}
