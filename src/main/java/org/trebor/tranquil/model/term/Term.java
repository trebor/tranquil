package org.trebor.tranquil.model.term;

public interface Term extends Cloneable
{
  String getName();
  Term copy();
  @Override
  boolean equals(Object term);
}
