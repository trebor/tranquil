package org.trebor.tranquil.model.pattern;

import org.trebor.tranquil.model.term.AbstractTerm;

abstract class AbstractSlot extends AbstractTerm implements Slot
{
  private static int mNext = 0;
  
  public AbstractSlot(String name)
  {
    super(String.format("<%s-%d>", name, ++mNext));
  }
  
  public Slot copy()
  {
    return this;
  }
}
