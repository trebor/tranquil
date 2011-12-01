package org.trebor.tranquil.model.pattern;

import org.trebor.tranquil.model.term.Variable;

public class VariableSlot extends AbstractSlot
{
  public VariableSlot()
  {
    super("var");
  }
  
  @Override
  public boolean equals(Object term)
  {
    return term instanceof Variable;
  }
}
