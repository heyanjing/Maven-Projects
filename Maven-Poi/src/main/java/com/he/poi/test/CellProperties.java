package com.he.poi.test;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CellProperties {
    private Boolean isMerge;//是否合并单元格
    private Integer occupyRow;//合并所占行数
    private Integer occupyColumn;//合并所占列数
    private String content;//内容
    private Integer firstColumn;//起始列
    private Integer lastColumn;//结束列
    private Integer firstRow;//起始行
    private Integer lastRow;//结束行
    private float columnWidth;
    private float   rowHeight;
    public CellProperties(Boolean isMerge, Integer occupyRow, Integer occupyColumn) {
        super();
        this.isMerge = isMerge;
        this.occupyRow = occupyRow;
        this.occupyColumn = occupyColumn;
    }
    
    
}
