package org.trebor.tranquil.model.term;

public class Constant extends AbstractTerm
{
  private final double mValue;
  
  public Constant(double value)
  {
    super(String.format("%1.0f", value));
    mValue = value; 
  }
  
  public double getValue()
  {
    return mValue;
  }
  
  @Override
  public boolean equals(Object other)
  {
    return getClass().equals(other.getClass()) &&
      getValue() == ((Constant)other).getValue();
  }
  
  public Constant copy()
  {
    return new Constant(getValue());
  }
}
