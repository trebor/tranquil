package org.trebor.tranquil.model;

public class Variable implements Term
{
  private final String mName;
  
  public Variable(String name)
  {
    mName = name;
  }
  
  public String getName()
  {
    return mName;
  }
}
