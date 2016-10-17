package com.github.gianlucanitti.javaexpreval;

import junit.framework.TestCase;

import java.io.PrintWriter;

public class ExpressionContextTest extends TestCase{

    public void testGetSetVariable(){
        ExpressionContext c = new ExpressionContext();
        try {
            c.setVariable("someVar", 5);
            c.setVariable("some_other_variable123", new ConstExpression(1));
            assertEquals(5.0, c.getVariable("someVar"));
        }catch(ExpressionException ex){
            fail(ex.getMessage());
        }
        try{
            c.setVariable("123some_invalid_variable123", new ConstExpression(1));
            fail("A variable name with invalid characters is being accepted.");
        }catch(ExpressionException ex){
            assertEquals(ex.getMessage(), "Expression error: \"123some_invalid_variable123\" isn't a valid symbol name because it contains the '1' character.");
        }
        try {
            double x = c.getVariable("someUndefinedVar");
            fail("Undefined variable is bound to value " + x);
        }catch(UndefinedException ex){
            assertEquals("Expression error: The variable \"someUndefinedVar\" is not defined.", ex.getMessage());
        }
    }

    public void testSetVariableExpression(){
        try {
            ExpressionContext c = new ExpressionContext();
            c.setVariable("a", new BinaryOpExpression(new ConstExpression(3), '+', new ConstExpression(5)));
            assertEquals(8.0, c.getVariable("a"));
            c.setVariable("b", new NegatedExpression(new VariableExpression("a")));
            assertEquals(-8.0, c.getVariable("b"));
        }catch(ExpressionException ex){
            fail(ex.getMessage());
        }
    }

    public void testDelVariable(){
        ExpressionContext c = new ExpressionContext();
        try {
            c.setVariable("someVar", 1);
            c.setVariable("someOtherVar", 2);
        }catch(ExpressionException ex){
            fail(ex.getMessage());
        }
        c.delVariable("someVar");
        try {
            assertEquals(2.0, c.getVariable("someOtherVar"));
        }catch(UndefinedException ex){
            fail("The wrong variable was deleted.");
        }
        try{
            c.getVariable("someVar");
            fail("A variable is still accessible after being deleted.");
        }catch(UndefinedException ex){
            //ok (right variable was deleted)
        }
    }

    public void testToString(){
        ExpressionContext c = new ExpressionContext();
        try {
            c.setVariable("a", 4);
            c.setVariable("b", 8);
        }catch(InvalidSymbolNameException ex){
            fail(ex.getMessage());
        }
        assertTrue(c.toString().contains("a=4.0"));
        assertTrue(c.toString().contains("b=8.0"));
    }

    public void testClear(){
        ExpressionContext c = new ExpressionContext();
        try {
            c.setVariable("someVar", 1);
        }catch(InvalidSymbolNameException ex){
            fail(ex.getMessage());
        }
        c.clear();
        try{
            c.getVariable("someVar");
            fail("A variable is still accessible after clear.");
        }catch(UndefinedException ex){
            //ok (context has been cleared)
        }
    }

    public void testGetSetFunction(){
        ExpressionContext c = new ExpressionContext();
        try {
            //c.setFunction("someFunction", new ConstExpression(1));
            c.setFunction("someFunction", new ConstExpression(2)); //must replace the previous (same name and 0 arguments)
            assertEquals(2.0, c.getFunction("someFunction", 0).eval(new double[0], c, new PrintWriter(NullOutputStream.getWriter())));
        }catch(ExpressionException ex){
            fail(ex.getMessage());
        }
    }

}
