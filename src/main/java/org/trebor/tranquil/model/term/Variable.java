package org.trebor.tranquil.model.term;

public class Variable extends AbstractTerm
{
  public Variable(String name)
  {
    super(name);
  }
  
  public Variable copy()
  {
    return new Variable(getName());
  }
}
