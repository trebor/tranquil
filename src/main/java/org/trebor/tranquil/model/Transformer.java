package org.trebor.tranquil.model;

import java.util.HashMap;
import java.util.Map;

import org.trebor.tranquil.model.pattern.TermSlot;
import org.trebor.tranquil.model.pattern.Transform;
import org.trebor.tranquil.model.term.Add;
import org.trebor.tranquil.model.term.Constant;
import org.trebor.tranquil.model.term.Divide;
import org.trebor.tranquil.model.term.Multiply;
import org.trebor.tranquil.model.term.Operator;
import org.trebor.tranquil.model.term.Subtract;
import org.trebor.tranquil.model.term.Term;
import org.trebor.tranquil.model.term.TermProperties;
import org.trebor.tranquil.view.TextRenderer;

public class Transformer
{
  public static final Constant ZERO = new Constant(0);
  public static final Constant ONE = new Constant(1);
  public static final TermSlot A = new TermSlot();
  public static final TermSlot B = new TermSlot();
  public static final TermSlot C = new TermSlot();

  public static final Transform EXPAND = new Transform(new Multiply(A, new Add(B, C)), new Add(new Multiply(A, B), new Multiply(A, C)));
  
  public static Transform[] SIMPLIFIERS =
  {
    new Transform(new Divide(A, A), ONE),
    new Transform(new Add(ZERO, A), A),
    new Transform(new Subtract(A, ZERO), A),
    new Transform(new Multiply(ONE, A), A),
    new Transform(new Divide(A, ONE), A),
    new Transform(new Multiply(A, new Divide(B, A)), B),
    new Transform(new Add(new Multiply(A, B), new Multiply(A, C)), new Multiply(A, new Add(B, C))),
  };

  public static Term simplify(Term term)
  {
    return TermProperties.isAtomic(term)
      ? simplifyTerm(term)
      : simplifyOperator((Operator)term);
  }

  private static Term simplifyOperator(Operator operator)
  {
    // check all operands

    Map<Term, Term> operandMap = new HashMap<Term, Term>();
    for (Term oldOperand : operator.getOperands())
    {
      Term newOperand = simplify(oldOperand);
      if (newOperand != oldOperand)
        operandMap.put(oldOperand, newOperand);
    }

    // if operands not changed, return original operator

    if (!operandMap.isEmpty())
    {
      Operator newOperator = null;

      try
      {
        newOperator = operator.getClass().newInstance();
      }
      catch (InstantiationException e)
      {
        e.printStackTrace();
      }
      catch (IllegalAccessException e)
      {
        e.printStackTrace();
      }

      for (Term oldOperand : operator.getOperands())
      {
        Term newOperand = operandMap.get(oldOperand);
        newOperator.addTerms(newOperand == null
          ? oldOperand
          : newOperand);
      }
      operator = newOperator;
    }

    return simplifyTerm(operator);
  }
  
  private static Term simplifyTerm(Term term)
  {
    boolean change = false;

    do
    {
      change = false;
      for (Transform p : SIMPLIFIERS)
      {
        Term simple = p.match(term);
        if (null != simple)
        {
          term = simple;
          System.out.println("with: " + p);
          System.out.println("  -> " + TextRenderer.render(term));
          change = true;
        }
      }
      
      // if there were no changes, evaluate the sucker
      
//      if (!change)
//      {
//        Term simple = term.evaluate();
//        if (simple != term)
//          change = true;
//      }
      
    }
    while (change);
    
    return term;
  }
}
