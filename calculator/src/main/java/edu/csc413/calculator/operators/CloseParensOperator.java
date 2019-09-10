package edu.csc413.calculator.operators;

import edu.csc413.calculator.evaluator.Operand;

public class CloseParensOperator extends Operator{
    public Operand execute (Operand first, Operand second){
        System.out.println("*****Parenthesis error******");
        throw new RuntimeException("*****Parenthesis error******");
    }

    public int priority() {
        return 5;
    }
    public String print() {return ")";}
}

