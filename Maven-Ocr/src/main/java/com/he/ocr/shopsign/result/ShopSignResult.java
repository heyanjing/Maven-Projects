package com.he.ocr.shopsign.result;

import com.he.ocr.common.base.BaseBean;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ShopSignResult extends BaseBean {
	private String	shop_sign;
	private String	success;
}
