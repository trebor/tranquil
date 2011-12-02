package org.trebor.tranquil.model.term;

public class Multiply extends AbstractOperator
{
  public Multiply()
  {
    super("*", Arity.TWO);
  }
  
  public Multiply(Term a, Term b)
  {
    this();
    addTerms(a, b);
  }
  
  public Multiply copy()
  {
    Multiply copy = new Multiply();
    for (Term operand: getOperands())
      copy.addTerms(operand.copy());
    return copy; 
  }
}
