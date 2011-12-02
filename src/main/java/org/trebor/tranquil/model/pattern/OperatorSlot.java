package org.trebor.tranquil.model.pattern;

import org.trebor.tranquil.model.term.Operator;

public class OperatorSlot extends AbstractSlot
{
  public OperatorSlot()
  {
    super("op");
  }
  
  @Override
  public boolean equals(Object term)
  {
    return term instanceof Operator;
  }
  
//  public OperatorSlot copy()
//  {
//    return new OperatorSlot();
//  }
}
