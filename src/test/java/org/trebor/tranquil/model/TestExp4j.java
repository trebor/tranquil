package org.trebor.tranquil.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.congrace.exp4j.Calculable;
import de.congrace.exp4j.ExpressionBuilder;
import de.congrace.exp4j.UnknownFunctionException;
import de.congrace.exp4j.UnparsableExpressionException;

public class TestExp4j
{
  @Test
  public void test() throws UnknownFunctionException, UnparsableExpressionException
  {
    ExpressionBuilder exp = new ExpressionBuilder("4 + X * 3 + 1");
    exp.withVariable("X", 5);
    Calculable calc = exp.build();
    assertEquals(20, calc.calculate(), 0);
    assertEquals("4 X 3 * + 1 +", calc.getExpression());
  }
}
