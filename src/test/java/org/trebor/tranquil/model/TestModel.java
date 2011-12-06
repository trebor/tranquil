package org.trebor.tranquil.model;

import static org.junit.Assert.*;
import static org.trebor.tranquil.controller.Transformer.*;
import static org.trebor.tranquil.model.term.Properties.*;
import static org.trebor.tranquil.model.term.Properties.Presidence.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
  public void testSimplify()
  {
    Variable a = new Variable("A");
    Variable dog = new Variable("DOG");
    Variable cat = new Variable("CAT");
    Variable rat = new Variable("RAT");
    Constant zero = new Constant(0);
    Constant one = new Constant(1);
    Constant two = new Constant(2);
    
    assertEquals(a, simplify(a));
    assertEquals(dog, simplify(dog));
    assertEquals(one, simplify(one));
    
    assertEquals(a, simplify(new Multiply(one, a)));
    assertEquals(a, simplify(new Multiply(a, one)));
    assertEquals(two, simplify(new Multiply(two, one)));
    
    assertEquals(new Divide(one, a), simplify(new Divide(one, a)));
    assertEquals(a, simplify(new Divide(a, one)));
    assertEquals(new Constant(0.5d), simplify(new Divide(one, two)));
    assertEquals(two, simplify(new Divide(two, one)));
    
    assertEquals(one, simplify(new Divide(cat, cat)));
    assertEquals(new Divide(cat, dog), simplify(new Divide(cat, dog)));
    
    assertEquals(dog, simplify(new Add(zero, dog)));
    assertEquals(dog, simplify(new Add(dog, zero)));
    
    assertEquals(dog, simplify(new Subtract(dog, zero)));
    assertEquals(new Subtract(zero, dog), simplify(new Subtract(zero, dog)));
    
    assertEquals(dog, simplify(new Multiply(cat, new Divide(dog, cat))));
    assertEquals(dog, simplify(new Multiply(new Divide(dog, cat), cat)));
    assertEquals(one, simplify(new Multiply(new Divide(one, cat), cat)));
    assertEquals(new Multiply(cat, new Divide(cat, dog)), simplify(new Multiply(cat, new Divide(cat, dog))));
    
    assertEquals(new Multiply(a, new Add(dog, cat)), simplify(new Add(new Multiply(cat, a), new Multiply(a, dog))));
    
    Term base1 = new Multiply(cat, new Divide(dog, cat)); // dog
    assertEquals(dog, simplify(base1));
    Term base2 = new Multiply(one, new Divide(two, one)); // two
    assertEquals(two, simplify(base2));
    Term base3 = new Multiply(cat, new Divide(rat, dog)); // cat * rad / dog
    assertEquals(base3, simplify(base3));
    Term cplx1 = new Multiply(base1, new Add(base2, base3)); // dog * (two + b3)
    assertEquals(new Multiply(dog, new Add(two, base3)), simplify(cplx1));

    Term cplx2 = new Add(zero, new Multiply(base1, one)); // dog
    assertEquals(dog, simplify(cplx2));
    Term cplx3 = new Divide(new Multiply(rat, base1), one); // rat * dog
    assertEquals(new Multiply(rat, dog), simplify(cplx3));
    Term moma =  new Add(new Multiply(cplx2, cplx1), new Multiply(cplx2, cplx3));
    assertEquals(new Multiply(dog, new Add(new Multiply(dog, new Add(two, base3)), new Multiply(rat, dog))), simplify(moma));
    
    Term reduce1 = new Add(one, one);
    assertEquals(two, simplify(reduce1));
    Term reduce2 = new Multiply(cat, new Divide(cat, dog));
    assertEquals(reduce2, simplify(reduce2));
    Term reduce3 = new Multiply(reduce1, reduce2);
    assertEquals(new Multiply(two, reduce2), simplify(reduce3));
  }

  @Test
  public void testCopy()
  {
    Term a = new Variable("A");
    Term b = new Variable("B");
    Term one = new Constant(1);
    Term two = new Constant(2);
    Term mlt = new Multiply(a, b);
    Term div = new Divide(a, b);
    Term add = new Add(one, two);
    Term sub  = new Subtract(one, two);
    
    Term terms[] = 
    {
      one,
      two,
      a,
      b,
      mlt,
      div,
      add,
      sub,
      new Add(new Divide(mlt, add), new Multiply(div, sub)),
    };
    
    for (Term term: terms)
    {
      Term copy = term.copy();
      assertEquals(term, copy);
      assertTrue(term != copy);
    }
  }
  
  
  @Test
  public void testEvaluate()
  {
    Variable a = new Variable("A");
    Variable b = new Variable("B");
    Variable x = new Variable("X");
    Constant nTwo = new Constant(-2);
    Constant nOne = new Constant(-1);
    Constant zero = new Constant(0);
    Constant half = new Constant(1d/2d);
    Constant one = new Constant(1);
    Constant two = new Constant(2);
    Constant four = new Constant(4);
    
    Map<Variable, Double> map = new HashMap<Variable, Double>();
    map.put(a, 1d);
    map.put(b, 2d);
    
    Term[][] terms = 
    {
      {null, one, one},
      {null, x, x},
      {null, two, new Add(one, one)},
      {null, zero, new Add(one, nOne)},

      {null, zero, new Subtract(one, one)}, 
      {null, one, new Subtract(two, one)},
      {null, nOne, new Subtract(one, two)},

      {null, one, new Multiply(one, one)},
      {null, four, new Multiply(two, two)},
      {null, nTwo, new Multiply(nOne, two)},
      
      {null, one, new Divide(one, one)},
      {null, one, new Divide(two, two)},
      {null, two, new Divide(two, one)},
      {null, half, new Divide(one, two)},
      
      {null, one, new Divide(new Multiply(two, two), four)},
      {null, new Constant(1/4D), new Divide(new Multiply(new Divide(one, two), two), four)},
      {null, new Constant(5), new Add(new Subtract(new Add(one, two), two), four)},
      {null, new Constant(6), new Add(new Subtract(new Add(new Divide(new Multiply(new Divide(one, two), two), four), two), new Divide(new Multiply(new Divide(one, two), two), four)), four)},
      
      {half, new Divide(a, two), new Divide(a, two)},
      {two, new Divide(two, a), new Divide(two, a)},
      {two, new Divide(two, a), new Divide(new Add(one, one), a)},
      {new Constant(6), new Add(new Subtract(new Add(new Divide(new Multiply(new Divide(a, two), two), four), b), new Constant(1d/4)), four), new Add(new Subtract(new Add(new Divide(new Multiply(new Divide(a, two), two), four), b), new Divide(new Multiply(new Divide(one, two), two), four)), four)},
    };

    terms[18][1].evaluate(map);
    
    for (int i = 0; i <  terms.length; ++i)
    {
      Term t1 = terms[i][0];
      Term t2 = terms[i][1];
      Term t3 = terms[i][2];
      if (t1 == null)
        t1 = t2;
        
      assertEquals(i + ": " + t3.toString(), t2, t3.evaluate());
      assertEquals(i + ": " + t3.toString(), t1, t3.evaluate(map));
      assertEquals(i + ": " + t3.toString(), t1, t3.evaluate(map));
    }
    
    Term same1 = new Subtract(one, b);
    assertTrue(same1 == same1.evaluate());
    assertTrue(same1 != same1.evaluate(map));
    assertEquals(nOne, same1.evaluate(map));
    
    Term same2 = new Multiply(new Subtract(a, b), a);
    assertTrue(same2 == same2.evaluate());
    assertTrue(same2 != same2.evaluate(map));
    assertEquals(nOne, same2.evaluate(map));
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
