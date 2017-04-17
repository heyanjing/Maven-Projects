package com.he.ocr.idcard.result.face;

import com.he.ocr.common.base.BaseBean;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class FaceSide  extends BaseBean{
	private String address;
	private String birth;
	private String config_str;
	private FaceRect face_rect;
	private String name;
	private String nationality;
	private String num;
	private String request_id;
	private String sex;
	private boolean success;
}
