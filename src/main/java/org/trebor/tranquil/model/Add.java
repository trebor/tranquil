package org.trebor.tranquil.model;

public class Add extends AbstractOperator
{
  public Add()
  {
    super("+", Arity.TWO);
  }
  
  public Add(Term a, Term b)
  {
    this();
    addTerms(a, b);
  }
}
