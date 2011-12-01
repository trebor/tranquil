package org.trebor.tranquil.model.term;

public class Divide extends AbstractOperator
{
  public Divide()
  {
    super("/", Arity.TWO);
  }
  
  public Divide(Term a, Term b)
  {
    this();
    addTerms(a, b);
  }
}
