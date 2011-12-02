package org.trebor.tranquil.model.term;

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
  
  public Add copy()
  {
    Add copy = new Add();
    for (Term operand: getOperands())
      copy.addTerms(operand.copy());
    return copy; 
  }
}
