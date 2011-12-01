package org.trebor.tranquil.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.trebor.tranquil.view.TextRenderer;

public class TestView
{

  @Test
  public void testRender()
  {
    Variable a = new Variable("A");
    Constant b = new Constant(2);
    Constant c = new Constant(3);
    Term add = new Add(a, b);
    Term sub = new Subtract(a, b);
    Term mlt = new Multiply(a, b);
    Term div = new Divide(a, b);
    Term cpx1 = new Multiply(add, c);
    Term cpx2 = new Multiply(c, add);
    Term cpx3 = new Add(mlt, c);
    Term cpx4 = new Add(c, mlt);
    Term cpx5 = new Multiply(c, new Add(add, b));
    Term cpx6 = new Divide(div, mlt);
    Term cpx7 = new Divide(div, new Divide(div, add));
    
    assertEquals("A + 2", TextRenderer.render(add));
    assertEquals("A - 2", TextRenderer.render(sub));
    assertEquals("A * 2", TextRenderer.render(mlt));
    assertEquals("A / 2", TextRenderer.render(div));
    assertEquals("(A + 2) * 3", TextRenderer.render(cpx1));
    assertEquals("3 * (A + 2)", TextRenderer.render(cpx2));
    assertEquals("A * 2 + 3", TextRenderer.render(cpx3));
    assertEquals("3 + A * 2", TextRenderer.render(cpx4));
    assertEquals("3 * ((A + 2) + 2)", TextRenderer.render(cpx5));
    assertEquals("(A / 2) / (A * 2)", TextRenderer.render(cpx6));
    assertEquals("(A / 2) / ((A / 2) / (A + 2))", TextRenderer.render(cpx7));
  }
}
