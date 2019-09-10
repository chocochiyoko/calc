package edu.csc413.calculator.evaluator;


import edu.csc413.calculator.operators.AddOperator;
import edu.csc413.calculator.operators.Operator;

import java.util.EmptyStackException;
import java.util.Stack;
import java.util.StringTokenizer;

public class Evaluator {
    private Stack<Operand> operandStack;
    private Stack<Operator> operatorStack;
    private StringTokenizer tokenizer;
    private static final String DELIMITERS = "+-*^/() ";

    public Evaluator() {
        operandStack = new Stack<>();
        operatorStack = new Stack<>();

    }

    public int eval(String expression) {
        String token;
        int parens=0;
        // The 3rd argument is true to indicate that the delimiters should be used
        // as tokens, too. But, we'll need to remember to filter out spaces.
        this.tokenizer = new StringTokenizer(expression, DELIMITERS, true);


        while (this.tokenizer.hasMoreTokens()) {

            // filter out spaces
            if (!(token = this.tokenizer.nextToken()).equals(" ")) {
                // check if token is an operand
                if (Operand.check(token)) {
                    operandStack.push(new Operand(token));
                } else {
                    if (!Operator.check(token)) {
                        System.out.println("*****invalid token******");
                        throw new RuntimeException("*****invalid token******");
                    }

                    // TODO Operator is abstract - these two lines will need to be fixed:
                    // The Operator class should contain an instance of a HashMap,
                    // and values will be instances of the Operators.  See Operator class
                    // skeleton for an example.
                    Operator newOperator = Operator.getOperator(token);
                    //if operator stack is empty push newOperator and go directly to checking next token
                    if(operatorStack.isEmpty()){
                        operatorStack.push(newOperator);
                        continue;
                    }
                    //if operator is "(" push it to stack and go directly to checking next token
                    else if (newOperator.priority() == 4){
                        operatorStack.push(newOperator);
                        continue;
                    }

                    //if operator is ")" execute everything on stacks until "("
                    else if (newOperator.priority() == 5){

                        while (!operatorStack.isEmpty() && operatorStack.peek().priority() !=4 ){

                                    Operator oldOpr = operatorStack.pop();
                                    Operand op2 = operandStack.pop();
                                    Operand op1 = operandStack.pop();
                                   operandStack.push(oldOpr.execute(op1, op2));

                            }

                        //pop "("
                            operatorStack.pop();

                        }

                        //otherwise execute operations according to priority
                        //push newOperator onto stack once:
                        //stack is empty
                        //top of stack is "("
                        //it is higher priority than top of stack
                   else try {
                        while (operatorStack.peek().priority() >= newOperator.priority() && operatorStack.peek().priority() != 4) {

                            Operator oldOpr = operatorStack.pop();
                            Operand op2 = operandStack.pop();
                            Operand op1 = operandStack.pop();
                            operandStack.push(oldOpr.execute(op1, op2));
                        }
                            operatorStack.push(newOperator);
                    }
                    catch (EmptyStackException e) {
                        operatorStack.push(newOperator);
                    }
                }
            }
        }


        int result=0;
        Operand op1, op2;
        Operator thisOperator;
        //if operator stack is empty the whole expression must have been in parens.
        //the result is just whatever is on the operand stack.
        if (operatorStack.isEmpty()){
            result=operandStack.pop().getValue();
        }
        //execute remaining stack
        while(!operatorStack.isEmpty())
        {

            op2=operandStack.pop();
            op1=operandStack.pop();
            thisOperator=operatorStack.pop();
            result = thisOperator.execute(op1,op2).getValue();
            operandStack.push(thisOperator.execute(op1,op2));

        }



        return result;
    }
}

