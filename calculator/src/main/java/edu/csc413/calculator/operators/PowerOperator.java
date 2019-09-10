package edu.csc413.calculator.operators;

import edu.csc413.calculator.evaluator.Operand;

public class PowOperator {
    public Operand power (Operand first, Operand second){
        Operand result = new Operand (first.getValue()^second.getValue());
        return result;
    }

    public int priority() {
        return 2;
    }
}
