package edu.csc413.calculator.operators;

import edu.csc413.calculator.evaluator.Operand;

public class DivideOperator extends Operator{
    public Operand execute (Operand first, Operand second){
        Operand result = new Operand (first.getValue()/second.getValue());
        return result;
    }

    public int priority() {
        return 2;
    }
    public String print() {return "/";}
}
