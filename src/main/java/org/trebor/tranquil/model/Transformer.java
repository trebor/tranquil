package org.trebor.tranquil.model;

import org.trebor.tranquil.model.pattern.Transform;
import org.trebor.tranquil.model.term.Add;
import org.trebor.tranquil.model.term.Constant;
import org.trebor.tranquil.model.term.Divide;
import org.trebor.tranquil.model.term.Multiply;
import org.trebor.tranquil.model.term.Subtract;
import org.trebor.tranquil.model.term.Term;
import org.trebor.tranquil.model.term.Variable;
import org.trebor.tranquil.view.TextRenderer;

public class Transformer
{
  public static final Constant ZERO = new Constant(0);
  public static final Constant ONE = new Constant(1);
  public static final Variable A = new Variable("A");
  public static final Variable B = new Variable("B");
  public static final Variable C = new Variable("C");

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
    boolean change = false;

    System.out.println("input: " + TextRenderer.render(term));

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
    }
    while (change);

    return term;
  }
}
