/*
 * ====================================================================
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ====================================================================
 */
package com.he.poi.funs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.formula.functions.FreeRefFunction;
import org.apache.poi.ss.formula.udf.DefaultUDFFinder;
import org.apache.poi.ss.formula.udf.UDFFinder;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellReference;

/**
 * An example class of how to invoke a User Defined Function for a given
 * XLS instance using POI's UDFFinder implementation.
 * 
 * @author Jon Svede ( jon [at] loquatic [dot] com )
 * @author Brian Bush ( brian [dot] bush [at] nrel [dot] gov )
 */
public class UserDefinedFunctionExample {

    public static void main(String[] args) {
        String[] argss = { "D:/test/projects/poi/src/main/java/com/he/poi/funs/mortgage-calculation.xls", "Sheet1!B4" };
        if (argss.length != 2) {
            System.out.println("usage: UserDefinedFunctionExample fileName cellId");
            return;
        }

        System.out.println("fileName: " + argss[0]);
        System.out.println("cell: " + argss[1]);

        File workbookFile = new File(argss[0]);

        try {
            FileInputStream fis = new FileInputStream(workbookFile);
            Workbook workbook = WorkbookFactory.create(fis);
            fis.close();

            String[] functionNames = { "calculatePayment" };
            FreeRefFunction[] functionImpls = { new CalculateMortgage() };

            UDFFinder udfToolpack = new DefaultUDFFinder(functionNames, functionImpls);

            // register the user-defined function in the workbook
            workbook.addToolPack(udfToolpack);

            FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();

            CellReference cr = new CellReference(argss[1]);
            String sheetName = cr.getSheetName();
            Sheet sheet = workbook.getSheet(sheetName);
            int rowIdx = cr.getRow();
            int colIdx = cr.getCol();
            Row row = sheet.getRow(rowIdx);
            Cell cell = row.getCell(colIdx);

            CellValue value = evaluator.evaluate(cell);

            System.out.println("returns value: " + value);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
