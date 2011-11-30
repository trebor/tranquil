package org.trebor.tranquil.model;

public class Constant implements Term
{
  private final double mValue;

  public Constant(double value)
  {
    mValue = value; 
  }
  
  double getValue()
  {
    return mValue;
  }

  public String getName()
  {
    return String.format("%1.0f", mValue);
  }
}
