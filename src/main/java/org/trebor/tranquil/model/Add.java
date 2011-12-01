package org.trebor.tranquil.model;

public class Add extends AbstractOperator
{
  public Add()
  {
  }
  
  public Add(Term a, Term b)
  {
    addTerms(a, b);
  }

  public Arity getArity()
  {
    return Arity.TWO;
  }

  public String getName()
  {
    return "+";
  }
}
