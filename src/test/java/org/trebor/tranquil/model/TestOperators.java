package org.trebor.tranquil.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.trebor.tranquil.model.TermProperties.*;
import static org.trebor.tranquil.model.TermProperties.Presidence.*;
import static org.trebor.tranquil.model.Transformer.*;
import static org.trebor.tranquil.view.TextRenderer.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.trebor.tranquil.util.Util;
import org.trebor.tranquil.view.TextRenderer;

public class TestOperators
{
  @Test
  public void testProperties()
  {
    Variable a = new Variable("A");
    Constant b = new Constant(5);
    Operator add = new Add(a, b);
    Operator sub = new Subtract(a, b);
    Operator mlt = new Multiply(a, b);
    Operator div = new Divide(a, b);
    
    Term[] terms = 
    {
      a,
      b,
      add,
      sub,
      mlt,
      div,
    };

    boolean[] comumtative = 
    {
      false,
      false,
      true,
      false,
      true,
      false,
    };

    boolean[] atomic = 
    {
      true,
      true,
      false,
      false,
      false,
      false,
    };

    for (int i = 0; i < terms.length; ++i)
    {
      assertEquals(terms[i].getName(), isComutative(terms[i]), comumtative[i]);
      assertEquals(terms[i].getName(), isAtomic(terms[i]), atomic[i]);
    }
    
    assertEquals(EQUAL, presidence(mlt, div));
    assertEquals(EQUAL, presidence(add, sub));
    
    assertEquals(GREATER, presidence(mlt, add));
    assertEquals(GREATER, presidence(mlt, sub));
    
    assertEquals(LESS, presidence(add, mlt));
    assertEquals(LESS, presidence(sub, mlt));
    
    assertEquals(GREATER, presidence(div, add));
    assertEquals(GREATER, presidence(div, sub));
    
    assertEquals(LESS, presidence(add, div));
    assertEquals(LESS, presidence(sub, div));
  }
  
  
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
  
  @Test
  public void testMatcher()
  {
    Variable a = new Variable("A");
    Variable b = new Variable("B");
    Variable dog = new Variable("DOG");
    Variable cat = new Variable("CAT");
    Constant one = new Constant(1);
    Constant two = new Constant(2);
    Constant three = new Constant(3);

    assertEquals("2", render((new Pattern(one, two)).match(one)));
    assertEquals("1", render((new Pattern(cat, cat)).match(one)));
    assertEquals("DOG", render((new Pattern(cat, cat)).match(dog)));
    
    //Variable C = new Variable("C");

    Term template = new Multiply(a, new Divide(b, a));
    Pattern p1 = new Pattern(template, b);

    assertEquals("B", render(p1.match(new Multiply(a, new Divide(b, a)))));
    assertEquals("2", render(p1.match(new Multiply(one, new Divide(two, one)))));
    assertEquals("CAT", render(p1.match(new Multiply(dog, new Divide(cat, dog)))));
    assertEquals("B", render(p1.match(new Multiply(new Divide(b, a), a))));
    
    Pattern p2 = new Pattern(new Multiply(one, two), three);
    assertEquals("3", render(p2.match(new Multiply(one, two))));
    assertEquals("3", render(p2.match(new Multiply(two, one))));
    assertEquals(null, p2.match(new Multiply(one, a)));
    
    Pattern p3 = new Pattern(new Multiply(one, a), a);
    assertEquals("2", render(p3.match(new Multiply(one, two))));
    assertEquals("2", render(p3.match(new Multiply(two, one))));
    assertEquals("CAT", render(p3.match(new Multiply(one, cat))));
    assertEquals("CAT", render(p3.match(new Multiply(cat, one))));
    assertEquals(null, p3.match(new Multiply(two, cat)));
    assertEquals(null, p3.match(new Multiply(cat, two)));
    
    Pattern p4 = new Pattern(new Multiply(a, b), a);
    assertEquals("1", render(p4.match(new Multiply(one, two))));
    assertEquals("2", render(p4.match(new Multiply(two, one))));
    assertEquals("CAT", render(p4.match(new Multiply(cat, dog))));
    assertEquals("DOG", render(p4.match(new Multiply(dog, cat))));
  }

  @Test 
  public void testSimplify()
  {
    Variable a = new Variable("A");
    Variable dog = new Variable("DOG");
    Variable cat = new Variable("CAT");
    Constant zero = new Constant(0);
    Constant one = new Constant(1);
    Constant two = new Constant(2);
    
    assertEquals("A", render(simplify(new Multiply(one, a))));
    assertEquals("A", render(simplify(new Multiply(a, one))));
    assertEquals("2", render(simplify(new Multiply(two, one))));
    
    assertEquals("1 / A", render(simplify(new Divide(one, a))));
    assertEquals("A", render(simplify(new Divide(a, one))));
    assertEquals("1 / 2", render(simplify(new Divide(one, two))));
    assertEquals("2", render(simplify(new Divide(two, one))));
    
    assertEquals("1", render(simplify(new Divide(cat, cat))));
    assertEquals("CAT / DOG", render(simplify(new Divide(cat, dog))));
    
    assertEquals("DOG", render(simplify(new Add(zero, dog))));
    assertEquals("DOG", render(simplify(new Add(dog, zero))));
    
    assertEquals("DOG", render(simplify(new Subtract(dog, zero))));
    assertEquals("0 - DOG", render(simplify(new Subtract(zero, dog))));
    
    assertEquals("DOG", render(simplify(new Multiply(cat, new Divide(dog, cat)))));
    assertEquals("DOG", render(simplify(new Multiply(new Divide(dog, cat), cat))));
    assertEquals("1", render(simplify(new Multiply(new Divide(one, cat), cat))));
    assertEquals("CAT * (CAT / DOG)", render(simplify(new Multiply(cat, new Divide(cat, dog)))));
    
    assertEquals("A * (DOG + CAT)", render(simplify(new Add(new Multiply(cat, a), new Multiply(a, dog)))));
  }
  
  @Test
  public void testPermutation()
  {
    Variable a = new Variable("A");
    Variable b = new Variable("B");
    Constant one = new Constant(1);
    Constant two = new Constant(2);

    List<Term> terms0 = new ArrayList<Term>();
    terms0.add(one);
    terms0.add(two);
    
    List<List<Term>> permuted0 = Util.permute(terms0);
    
    assertEquals(2, permuted0.size());
    assertEquals(one, permuted0.get(0).get(0));
    assertEquals(two, permuted0.get(0).get(1));
    assertEquals(two, permuted0.get(1).get(0));
    assertEquals(one, permuted0.get(1).get(1));
    
    List<Term> terms1 = new ArrayList<Term>();
    terms1.add(a);
    terms1.add(b);
    terms1.add(one);
    terms1.add(two);
    
    assertEquals(24, Util.permute(terms1).size());
  }
  
  @Test
  public void testEquals()
  {
    Term[] terms = 
    {
      new Multiply(new Variable("A"), new Variable("B")),
      new Multiply(new Variable("B"), new Variable("A")),
      new Multiply(new Variable("B"), new Variable("A")),
      new Multiply(new Variable("C"), new Variable("A")),
      new Divide(new Variable("A"), new Variable("B")),
      new Divide(new Variable("B"), new Variable("A")),
      new Multiply(new Multiply(new Variable("A"), new Variable("B")), new Multiply(new Variable("C"), new Variable("D"))),
      new Multiply(new Multiply(new Variable("D"), new Variable("C")), new Multiply(new Variable("B"), new Variable("A"))),
    };

    boolean[] equalsTable =
    {
      true,  true,  true,  false, false, false, false, false,
      true,  true,  true,  false, false, false, false, false,
      true,  true,  true,  false, false, false, false, false,
      false, false, false, true,  false, false, false, false,
      false, false, false, false, true , false, false, false,
      false, false, false, false, false, true , false, false,
      false, false, false, false, false, false, true , true ,
      false, false, false, false, false, false, true , true , 
    };
    
    assertFalse(terms[0].equals(terms[3]));

    int i = 0;
    for (Term t1: terms)
      for (Term t2: terms)
        assertEquals(String.format("%d: %s -> %s", i, t1, t2), 
          equalsTable[i++], t1.equals(t2));
  }
}
