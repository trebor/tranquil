package org.trebor.tranquil.model;

public abstract class AbstractTerm implements Term
{
  private final String mName;
  
  public AbstractTerm(String name)
  {
    mName = name;
  }

  public boolean equals(Term other)
  {
    return getClass().equals(other.getClass()) && getName().equals(other.getName());
  }
  
  public String getName()
  {
    return mName;
  }
}
