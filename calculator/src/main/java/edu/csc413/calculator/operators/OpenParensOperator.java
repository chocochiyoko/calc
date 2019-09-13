package edu.csc413.calculator.operators;

import edu.csc413.calculator.evaluator.Operand;

public class OpenParensOperator extends Operator  {
    public Operand execute (Operand first, Operand second){
        System.out.println("*****Parenthesis error******");
        throw new RuntimeException("*****Parenthesis error******");
    }

    public int priority() {
        return Integer.MAX_VALUE;
    }
    public String print() {return "(";}
}

