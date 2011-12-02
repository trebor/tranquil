package org.trebor.tranquil.model.term;

public class Subtract extends AbstractOperator
{
  public Subtract()
  {
    super("-", Arity.TWO);
  }
  
  public Subtract(Term a, Term b)
  {
    this();
    addTerms(a, b);
  }
  
  public Subtract copy()
  {
    Subtract copy = new Subtract();
    for (Term operand: getOperands())
      copy.addTerms(operand.copy());
    return copy;
  }
}
