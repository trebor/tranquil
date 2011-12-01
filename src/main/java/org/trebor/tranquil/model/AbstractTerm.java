package org.trebor.tranquil.model;

public abstract class AbstractTerm implements Term
{
  public boolean equals(Term other)
  {
    return getClass().equals(other.getClass()) && getName().equals(other.getName());
  }
}
