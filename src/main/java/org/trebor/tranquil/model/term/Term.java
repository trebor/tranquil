package org.trebor.tranquil.model.term;

import java.util.Map;

public interface Term extends Cloneable
{
  String getName();
  Term evaluate();
  Term evaluate(Map<Variable, Double> values);
  Term copy();
  @Override
  boolean equals(Object term);
}
