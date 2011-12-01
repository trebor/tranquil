package org.trebor.tranquil.model;

import org.trebor.tranquil.view.TextRenderer;

public class Transformer
{
  public static final Constant ZERO = new Constant(0);
  public static final Constant ONE = new Constant(1);
  public static final Variable A = new Variable("A");
  public static final Variable B = new Variable("B");
  public static final Variable C = new Variable("C");

  public static final Pattern EXPAND = new Pattern(new Multiply(A, new Add(B, C)), new Add(new Multiply(A, B), new Multiply(A, C)));
  
  public static Pattern[] SIMPLIFIERS =
  {
    new Pattern(new Divide(A, A), ONE),
    new Pattern(new Add(ZERO, A), A),
    new Pattern(new Subtract(A, ZERO), A),
    new Pattern(new Multiply(ONE, A), A),
    new Pattern(new Divide(A, ONE), A),
    new Pattern(new Multiply(A, new Divide(B, A)), B),
    new Pattern(new Add(new Multiply(A, B), new Multiply(A, C)), new Multiply(A, new Add(B, C))),
  };

  public static Term simplify(Term term)
  {
    boolean change = false;

    System.out.println("input: " + TextRenderer.render(term));

    do
    {
      change = false;
      for (Pattern p : SIMPLIFIERS)
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
