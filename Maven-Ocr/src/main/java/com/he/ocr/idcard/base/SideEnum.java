package com.he.ocr.idcard.base;

public enum SideEnum {
	Face("face", 1), Back("back", 2);
	private String	side;
	private int		value;

	private SideEnum(String side, int value) {
		this.side = side;
		this.value = value;
	}

	public String getSide() {
		return side;
	}

	public void setSide(String side) {
		this.side = side;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

}
