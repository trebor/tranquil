package org.trebor.tranquil.model;

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
  public boolean equals(Term other)
  {
    return getClass().equals(other.getClass()) &&
      getValue() == ((Constant)other).getValue();
  }
}
