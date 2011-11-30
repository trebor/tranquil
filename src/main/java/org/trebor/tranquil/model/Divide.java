package org.trebor.tranquil.model;

public class Divide extends AbstractOperator
{
  public Divide(Term a, Term b)
  {
    addTerms(a, b);
  }
  
  public Arity getArity()
  {
    return Arity.TWO;
  }

  public String getName()
  {
    return "/";
  }
}
