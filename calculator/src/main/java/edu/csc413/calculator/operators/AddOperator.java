package edu.csc413.calculator.operators;


import edu.csc413.calculator.evaluator.Operand;

public class AddOperator extends Operator{
    public Operand execute (Operand first, Operand second){
    Operand result = new Operand (first.getValue()+second.getValue());
    return result;
    }

    public int priority() {
        return 1;
    }

}
