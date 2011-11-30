package org.trebor.tranquil.model;

public class Subtract extends AbstractOperator
{
  public Subtract(Term a, Term b)
  {
    addTerms(a, b);
  }
  
  public Arity getArity()
  {
    return Arity.TWO;
  }

  public String getName()
  {
    return "-";
  }
}
