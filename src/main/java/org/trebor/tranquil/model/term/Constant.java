package org.trebor.tranquil.model.term;

import java.util.Map;

public class Constant extends AbstractTerm
{
  public static final String INT_FORM = "%1.0f";
  public static final String DEC_FORM = "%s";
  
  private final double mValue;
  
  public Constant(double value)
  {        
    super(format(value));
    mValue = value; 
  }

  @Override
  public String toString()
  {
    return format(mValue);
  }

  private static String format(double value)
  {
    return String.format(Math.floor(value) == value ? INT_FORM : DEC_FORM, value);
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
  
  public Term evaluate(Map<Variable, Double> values)
  {
    return this;
  }
}
