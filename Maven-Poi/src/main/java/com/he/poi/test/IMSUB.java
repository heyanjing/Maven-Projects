package com.he.poi.test;

import org.apache.poi.ss.formula.OperationEvaluationContext;
import org.apache.poi.ss.formula.eval.ErrorEval;
import org.apache.poi.ss.formula.eval.EvaluationException;
import org.apache.poi.ss.formula.eval.NumberEval;
import org.apache.poi.ss.formula.eval.OperandResolver;
import org.apache.poi.ss.formula.eval.ValueEval;
import org.apache.poi.ss.formula.functions.FreeRefFunction;

public class IMSUB implements FreeRefFunction {

    @Override
    public ValueEval evaluate(ValueEval[] args, OperationEvaluationContext ec) {
        if (args.length != 2) {
            return ErrorEval.VALUE_INVALID;
        }
        double no1, no2, result;
        try {
            ValueEval v1 = OperandResolver.getSingleValue(args[0], ec.getRowIndex(), ec.getColumnIndex());
            ValueEval v2 = OperandResolver.getSingleValue(args[1], ec.getRowIndex(), ec.getColumnIndex());

            no1 = OperandResolver.coerceValueToDouble(v1);
            no2 = OperandResolver.coerceValueToDouble(v2);

            result = subtraction(no1, no2);
            checkValue(result);
        } catch (EvaluationException e) {
            e.printStackTrace();
            return e.getErrorEval();
        }

        return new NumberEval(result);
    }

    public double subtraction(double no1, double no2) {
        return no1 - no2;
    }

    static final void checkValue(double result) throws EvaluationException {
        if (Double.isNaN(result) || Double.isInfinite(result)) {
            throw new EvaluationException(ErrorEval.NUM_ERROR);
        }
    }

}
