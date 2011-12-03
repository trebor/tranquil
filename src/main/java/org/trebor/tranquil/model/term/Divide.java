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
  
  public Divide copy()
  {
    Divide copy = new Divide();
    for (Term operand: getOperands())
      copy.addTerms(operand.copy());
    return copy; 
  }
  
  public Double compute(Double prior, Double next)
  {
    return prior == null ? next : prior / next;
  }
}
