package org.trebor.tranquil.model.term;

public class Equals extends AbstractOperator
{

  public Equals()
  {
    super("=", Arity.TWO);
  }

  public Equals copy()
  {
    Equals copy = new Equals();
    for (Term operand: getOperands())
      copy.addTerms(operand.copy());
    return copy; 
  }

  public Double compute(Double prior, Double next)
  {
    if (prior == null)
      return next;
    
    return prior == next ? 1d : 0d;
  }
}
