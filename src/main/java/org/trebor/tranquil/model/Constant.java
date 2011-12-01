package org.trebor.tranquil.model;

public class Constant extends AbstractTerm
{
  private final double mValue;

  public Constant(double value)
  {
    mValue = value; 
  }
  
  public double getValue()
  {
    return mValue;
  }

  public String getName()
  {
    return String.format("%1.0f", mValue);
  }
  
  @Override
  public boolean equals(Term other)
  {
    return getClass().equals(other.getClass()) &&
      getValue() == ((Constant)other).getValue();
  }
}
