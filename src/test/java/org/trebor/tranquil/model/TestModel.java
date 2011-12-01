package org.trebor.tranquil.model;

import static org.junit.Assert.*;
import static org.trebor.tranquil.model.Transformer.*;
import static org.trebor.tranquil.model.term.TermProperties.*;
import static org.trebor.tranquil.model.term.TermProperties.Presidence.*;
import static org.trebor.tranquil.view.TextRenderer.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.trebor.tranquil.model.pattern.ConstantSlot;
import org.trebor.tranquil.model.pattern.TermSlot;
import org.trebor.tranquil.model.pattern.Transform;
import org.trebor.tranquil.model.pattern.VariableSlot;
import org.trebor.tranquil.model.term.Add;
import org.trebor.tranquil.model.term.Constant;
import org.trebor.tranquil.model.term.Divide;
import org.trebor.tranquil.model.term.Multiply;
import org.trebor.tranquil.model.term.Operator;
import org.trebor.tranquil.model.term.Subtract;
import org.trebor.tranquil.model.term.Term;
import org.trebor.tranquil.model.term.Variable;
import org.trebor.tranquil.util.Util;

public class TestModel
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
  public void testMatcher()
  {
    VariableSlot v1 = new VariableSlot();
    ConstantSlot c1 = new ConstantSlot();
    TermSlot t1 = new TermSlot();
    TermSlot t2 = new TermSlot();
    Variable a = new Variable("A");
    Variable b = new Variable("B");
    Variable dog = new Variable("DOG");
    Variable cat = new Variable("CAT");
    Constant one = new Constant(1);
    Constant two = new Constant(2);

    assertEquals(two, new Transform(one, two).match(one));
    assertEquals(null, new Transform(one, two).match(two));
    assertEquals(null, new Transform(one, two).match(cat));
    assertEquals(two, new Transform(cat, two).match(cat));
    assertEquals(null, new Transform(cat, two).match(dog));
    assertEquals(null, new Transform(cat, two).match(one));
    
    
    assertEquals(one, new Transform(c1, c1).match(one));
    assertEquals(two, new Transform(c1, c1).match(two));
    assertEquals(null, new Transform(c1, c1).match(cat));
    assertEquals(null, new Transform(c1, c1).match(new Add(one, one)));
    
    assertEquals(cat, new Transform(v1, v1).match(cat));
    assertEquals(dog, new Transform(v1, v1).match(dog));
    assertEquals(null, new Transform(v1, v1).match(one));
    assertEquals(null, new Transform(v1, v1).match(new Add(cat, cat)));

    assertEquals(cat, new Transform(t1, t1).match(cat));
    assertEquals(dog, new Transform(t1, t1).match(dog));
    assertEquals(one, new Transform(t1, t1).match(one));
    assertEquals(new Add(cat, cat), new Transform(t1, t1).match(new Add(cat, cat)));
    
    Term template = new Multiply(t1, new Divide(t2, t1));
    Transform p1 = new Transform(template, t2);

    assertEquals(b, p1.match(new Multiply(a, new Divide(b, a))));
    assertEquals(two, p1.match(new Multiply(one, new Divide(two, one))));
    assertEquals(cat, p1.match(new Multiply(dog, new Divide(cat, dog))));
    assertEquals(b, p1.match(new Multiply(new Divide(b, a), a)));
    assertEquals(null, p1.match(new Multiply(new Divide(a, b), a)));
    assertEquals(new Add(cat, dog), p1.match(new Multiply(new Divide(new Add(cat, dog), a), a)));
    
    Transform p2 = new Transform(new Multiply(one, two), cat);
    assertEquals(cat, p2.match(new Multiply(one, two)));
    assertEquals(cat, p2.match(new Multiply(two, one)));
    assertEquals(null, p2.match(new Multiply(one, a)));
    
    Transform p3 = new Transform(new Multiply(one, t1), t1);
    assertEquals(two, p3.match(new Multiply(one, two)));
    assertEquals(two, p3.match(new Multiply(two, one)));
    assertEquals(cat, p3.match(new Multiply(one, cat)));
    assertEquals(cat, p3.match(new Multiply(cat, one)));
    assertEquals(null, p3.match(new Multiply(two, cat)));
    assertEquals(null, p3.match(new Multiply(cat, two)));

    Transform p4 = new Transform(new Multiply(t1, t2), t1);
    assertEquals(one, p4.match(new Multiply(one, two)));
    assertEquals(two, p4.match(new Multiply(two, one)));
    assertEquals(cat, p4.match(new Multiply(cat, dog)));
    assertEquals(dog, p4.match(new Multiply(dog, cat)));
  }

  @Test 
  @Ignore
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
      new Multiply(new Variable("A"), new Variable("B")),
    };

    boolean[] equalsTable =
    {
      true,  true,  true,  false, false, false, false, false, true ,
      true,  true,  true,  false, false, false, false, false, true ,
      true,  true,  true,  false, false, false, false, false, true ,
      false, false, false, true,  false, false, false, false, false,
      false, false, false, false, true , false, false, false, false,
      false, false, false, false, false, true , false, false, false,
      false, false, false, false, false, false, true , true , false,
      false, false, false, false, false, false, true , true , false,
      true , true , true , false, false, false, false, false, true ,
    };
    
    assertFalse(terms[0].equals(terms[3]));

    int i = 0;
    for (Term t1: terms)
      for (Term t2: terms)
      {
        String msg = String.format("%d: %s -> %s", i, t1, t2);
        boolean result = equalsTable[i++];
        if (result)
          assertEquals(msg, t1, t2);
        else
          assertFalse(msg, t1.equals(t2));
        
        assertEquals(msg, result, t1.equals(t2));
      }
  }
}
