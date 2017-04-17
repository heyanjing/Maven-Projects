package com.he.poi.test;

import lombok.Data;

@Data
public class Table {
    private String name;
    private String code;
    public Table(String name, String code) {
        super();
        this.name = name;
        this.code = code;
    }
    
}
