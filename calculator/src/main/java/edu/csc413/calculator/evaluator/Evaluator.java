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
        System.out.println(expression);
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
                    if(operatorStack.isEmpty()){
                        operatorStack.push(newOperator);
                        continue;
                    }
                    if (newOperator.priority()==4){
                        operatorStack.push(newOperator);
                        continue;
                    }

                    else if (newOperator.priority()==5){

                        while (!operatorStack.isEmpty() && operatorStack.peek().priority()!=4){

                                    System.out.println(newOperator.priority());
                                    Operator oldOpr = operatorStack.pop();
                                    Operand op2 = operandStack.pop();
                                    Operand op1 = operandStack.pop();
                                   operandStack.push(oldOpr.execute(op1, op2));




                            }
                        //pop parens
                        if (!operatorStack.isEmpty()){
                            operatorStack.pop();

                        }
                        }


                   else try {
                        while (operatorStack.peek().priority() >= newOperator.priority() && operatorStack.peek().priority()!=4) {
                            Operator oldOpr = operatorStack.pop();
                            Operand op2 = operandStack.pop();
                            Operand op1 = operandStack.pop();
                            operandStack.push(oldOpr.execute(op1, op2));}
                        operatorStack.push(newOperator);
                    }
                    catch (EmptyStackException e) {
                        operatorStack.push(newOperator);
                        continue;
                    }
                }
            }
        }


        // Control gets here when we've picked up all of the tokens; you must add
        // code to complete the evaluation - consider how the code given here
        // will evaluate the expression 1+2*3
        // When we have no more tokens to scan, the operand stack will contain 1 2
        // and the operator stack will have + * with 2 and * on the top;
        // In order to complete the evaluation we must empty the stacks,
        // that is, we should keep evaluating the operator stack until it is empty;
        // Suggestion: create a method that processes the operator stack until empty.

        //Don't forget to change the return value!
        int result=0;
        Operand op1, op2;
        Operator thisOperator;
        while(!operatorStack.isEmpty())
        {

            op2=operandStack.pop();
            op1=operandStack.pop();
            thisOperator=operatorStack.pop();
            result = thisOperator.execute(op1,op2).getValue();
            System.out.println(op1.getValue()+ " "+ thisOperator.print() +" " + op2.getValue()+ "=" + result);
            operandStack.push(thisOperator.execute(op1,op2));



        }


        return result;
    }
}

