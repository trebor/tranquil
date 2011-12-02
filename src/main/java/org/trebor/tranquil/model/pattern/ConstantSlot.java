package org.trebor.tranquil.model.pattern;

import org.trebor.tranquil.model.term.Constant;

public class ConstantSlot extends AbstractSlot
{
  public ConstantSlot()
  {
    super("const");
  }
  
  @Override
  public boolean equals(Object term)
  {
    return term instanceof Constant;
  }
  
//  public ConstantSlot copy()
//  {
//    return new ConstantSlot();
//  }
}
