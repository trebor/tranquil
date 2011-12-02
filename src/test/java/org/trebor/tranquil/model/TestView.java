package org.trebor.tranquil.model;

import static org.junit.Assert.assertEquals;
import static org.trebor.tranquil.view.TextRenderer.*;

import org.junit.Test;
import org.trebor.tranquil.model.term.Add;
import org.trebor.tranquil.model.term.Constant;
import org.trebor.tranquil.model.term.Divide;
import org.trebor.tranquil.model.term.Multiply;
import org.trebor.tranquil.model.term.Subtract;
import org.trebor.tranquil.model.term.Term;
import org.trebor.tranquil.model.term.Variable;

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
    
    assertEquals("A + 2", add.toString());
    assertEquals("A + 2", render(add));
    assertEquals("A - 2", sub.toString());
    assertEquals("A - 2", render(sub));
    assertEquals("A * 2", mlt.toString());
    assertEquals("A * 2", render(mlt));
    assertEquals("A / 2", div.toString());
    assertEquals("A / 2", render(div));
    assertEquals("(A + 2) * 3", cpx1.toString());
    assertEquals("(A + 2) * 3", render(cpx1));
    assertEquals("3 * (A + 2)", render(cpx2));
    assertEquals("A * 2 + 3", render(cpx3));
    assertEquals("3 + A * 2", render(cpx4));
    assertEquals("3 * ((A + 2) + 2)", render(cpx5));
    assertEquals("(A / 2) / (A * 2)", render(cpx6));
    assertEquals("(A / 2) / ((A / 2) / (A + 2))", cpx7.toString());
    assertEquals("(A / 2) / ((A / 2) / (A + 2))", render(cpx7));
  }
}
