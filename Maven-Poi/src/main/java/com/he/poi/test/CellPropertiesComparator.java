package com.he.poi.test;

import java.util.Comparator;

public class CellPropertiesComparator implements Comparator<CellProperties>{

    @Override
    public int compare(CellProperties cp1, CellProperties cp2) {
        return cp1.getFirstColumn()-cp2.getFirstColumn();
    }


}
