package com.github.gianlucanitti.javaexpreval;

import junit.framework.TestCase;

import java.math.BigDecimal;
import java.math.MathContext;

public class BinaryOpExpressionTest extends TestCase{

  public void testEval(){
    try{
      BinaryOpExpression exp = new BinaryOpExpression(new ConstExpression(20), '+', new ConstExpression(15));
      assertEquals(new BigDecimal(20.0+15.0), exp.eval());
      exp = new BinaryOpExpression(new ConstExpression(1), '-', new ConstExpression(6));
      assertEquals(new BigDecimal(1.0-6.0), exp.eval());
      exp = new BinaryOpExpression(new ConstExpression(3), '*', new ConstExpression(8));
      assertEquals(new BigDecimal(3.0*8.0), exp.eval());
      exp = new BinaryOpExpression(new ConstExpression(14), '/', new ConstExpression(6));
      assertEquals(new BigDecimal(14.0/6.0, MathContext.DECIMAL64), exp.eval());
      //exp = new BinaryOpExpression(new ConstExpression(2), '^', new ConstExpression(10));
      //assertEquals(Math.pow(2.0, 10.0), exp.eval()); //TODO
    }catch(ExpressionException ex){
      fail(ex.getMessage());
    }
  }

  public void testToString(){
    try{
      BigDecimal a = new BigDecimal(14.5);
      BigDecimal b = new BigDecimal(20.7);
      for(char op: new char[]{'+', '-', '*', '/', '^'}){
        String s = "(" + a + op + b + ")";
        BinaryOpExpression expr = new BinaryOpExpression(new ConstExpression(a), op, new ConstExpression(b));
        assertTrue(s.equals(expr.toString()));
      }
    }catch(InvalidOperatorException ex){
      fail(ex.getMessage());
    }
  }

}
