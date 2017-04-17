package com.he.poi.test;

import lombok.Data;

@Data
public class State {
    private String name;
    private String code;
    public State(String name, String code) {
        super();
        this.name = name;
        this.code = code;
    }
}
