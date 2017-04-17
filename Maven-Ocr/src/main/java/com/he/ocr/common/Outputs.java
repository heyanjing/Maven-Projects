package com.he.ocr.common;

import com.he.ocr.common.base.BaseBean;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor @EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Outputs extends BaseBean {
	private List<ResultBean> outputs;

}
