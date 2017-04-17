package com.he.ocr.shopsign.post;

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
public class ShopSignInputs extends BaseBean {
	private List<Image> inputs;

}
