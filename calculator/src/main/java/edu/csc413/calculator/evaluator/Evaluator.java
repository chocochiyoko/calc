package edu.csc413.calculator.evaluator;


import edu.csc413.calculator.operators.AddOperator;
import edu.csc413.calculator.operators.Operator;

import java.util.EmptyStackException;
import java.util.Stack;
import java.util.StringTokenizer;

public class Evaluator {
    private Stack<Operand> operandStack;
    private Stack<Operator> operatorStack;
    private Stack<String> tokenStack;
    private StringTokenizer tokenizer;
    private static final String DELIMITERS = "+-*^/() ";

    public Evaluator() {
        operandStack = new Stack<>();
        operatorStack = new Stack<>();
        //the token stack is only used for implicit multiplication and negative numbers.
        tokenStack = new Stack<>();
    }

    public int eval(String expression) {
        String token;
        System.out.println(expression);
        // The 3rd argument is true to indicate that the delimiters should be used
        // as tokens, too. But, we'll need to remember to filter out spaces.
        this.tokenizer = new StringTokenizer(expression, DELIMITERS, true);


        while (this.tokenizer.hasMoreTokens()) {

            // filter out spaces
            if (!(token = this.tokenizer.nextToken()).equals(" ")) {

                // check if token is an operand
                // if it is not the first token in the expression
                //check if the last token was ")"
                //if it was push a "*" onto Operator Stack

                if (Operand.check(token)) {
                    if (!tokenStack.isEmpty()){
                    if (tokenStack.peek().equals(")")){
                        operatorStack.push(Operator.getOperator("*"));
                    }
                    }

                    operandStack.push(new Operand(token));
                } else {
                    if (!Operator.check(token)) {
                        System.out.println("*****invalid token******");
                        throw new RuntimeException("*****invalid token******");
                    }


                    Operator newOperator = Operator.getOperator(token);

                    //if operator stack is empty push newOperator and go directly to checking next token
                    //unless last operator is ")" then push a "*" before the "("
                    if (newOperator.priority() == Integer.MAX_VALUE) {

                        if (!tokenStack.isEmpty() && !operandStack.isEmpty()){
                            if (Operand.check(tokenStack.peek()) || tokenStack.peek().equals(")")) {
                                operatorStack.push(Operator.getOperator("*"));
                                System.out.println("pushed * hello");
                            }
                        }

                        operatorStack.push(newOperator);
                        tokenStack.push(token);
                        continue;
                    }
                    //if operator stack is empty push new Operator onto stack
                    //unless the token is "-", then push a -1 and a "*"
                    else if(operatorStack.isEmpty()){
                        if (token.equals("-") && tokenStack.isEmpty()){
                            System.out.println("pushing -1");
                            operatorStack.push(Operator.getOperator("*"));
                            operandStack.push(new Operand(-1));
                            tokenStack.push(token);
                            continue;
                        }
                        operatorStack.push(newOperator);

                    }


                    //if operator is ")" execute everything on stacks until "(" then pop the "("
                    else if (newOperator.priority() == Integer.MIN_VALUE){


                            while (!operatorStack.isEmpty() && operatorStack.peek().priority() != Integer.MAX_VALUE) {

                                helper (operandStack.pop(), operandStack.pop(), operatorStack.pop());

                            }

                            operatorStack.pop();

                        }
                    else if ((token.equals("-") && tokenStack.peek().equals("("))) {
                            System.out.println("pushing -1");
                            operatorStack.push(Operator.getOperator("*"));
                            operandStack.push(new Operand(-1));

                    }
                        //otherwise execute operations according to priority
                        //push newOperator onto stack if:
                        //stack is empty
                        //top of stack is "("
                        //it is higher priority than top of stack
                   else try {

                        while (operatorStack.peek().priority() >= newOperator.priority() && operatorStack.peek().priority() != Integer.MAX_VALUE) {

                            helper (operandStack.pop(), operandStack.pop(), operatorStack.pop());
                        }
                            operatorStack.push(newOperator);
                    }
                    catch (EmptyStackException e) {
                        operatorStack.push(newOperator);
                    }
                }

            }
            tokenStack.push(token);
        }


        int result=0;
        Operand op1, op2;
        Operator thisOperator;

        //if operator stack is empty the whole expression must have been in parens.
        //the result is just whatever is on the operand stack.

        //execute remaining stack
        while(!operatorStack.isEmpty())
        {
           helper (operandStack.pop(), operandStack.pop(), operatorStack.pop());

        }
        result=operandStack.pop().getValue();
        return result;
    }
    public void helper (Operand op2, Operand op1, Operator operator){
        operandStack.push(operator.execute(op1, op2));
    }

}

