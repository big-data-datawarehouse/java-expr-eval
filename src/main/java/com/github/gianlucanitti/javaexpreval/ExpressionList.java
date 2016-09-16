package com.github.gianlucanitti.javaexpreval;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents a list of expressions with the operators that concatenate them.
 * This class implements the logic to split an expression in multiple binary operations according to operator precedence.
 */
public final class ExpressionList{

  //operator at index i is between item i and item i+1
  private ArrayList<Expression> items;
  private ArrayList<Character> operators;

  private boolean expectOperator = false; //When true, it's expected that an operator is added; othervise, an expression is expected.

  public ExpressionList(){
    items = new ArrayList<Expression>();
    operators = new ArrayList<Character>();
  }

  public void addItem(Expression item) throws UnexpectedTokenException{
    if(expectOperator)
      throw new UnexpectedTokenException(expectOperator);
    items.add(item);
    expectOperator = true;
  }

  public void addOperator(char op) throws UnexpectedTokenException{
    if(!expectOperator)
      throw new UnexpectedTokenException(expectOperator);
    operators.add(op);
    expectOperator = false;
  }

  private void evalOperators(Character ... ops) throws InvalidOperatorException{
    List<Character> opsList = Arrays.asList(ops);
    int i = 0;
    while(i < operators.size()){
      Character op = operators.get(i);
      if(opsList.contains(op)){
        operators.remove(i);
        Expression left = items.remove(i);
        Expression right = items.remove(i);
        items.add(i, new BinaryOpExpression(left, op, right));
      }else{
        i++;
      }
    }
  }

  public Expression simplify() throws InvalidOperatorException{
    evalOperators('^');
    evalOperators('*', '/');
    evalOperators('+', '-');
    if(items.size() != 1 || operators.size() != 0) throw new InvalidOperatorException(operators.get(0)); //TODO: fix
    return items.get(0);
  }

}