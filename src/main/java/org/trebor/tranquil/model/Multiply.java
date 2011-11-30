package org.trebor.tranquil.model;

public class Multiply extends AbstractOperator
{
  public Multiply(Term a, Term b)
  {
    addTerms(a, b);
  }
  
  public Arity getArity()
  {
    return Arity.TWO;
  }

  public String getName()
  {
    return "*";
  }
}
