package org.trebor.tranquil.model.pattern;

import org.trebor.tranquil.model.term.Term;

public class TermSlot extends AbstractSlot
{
  public TermSlot()
  {
    super("term");
  }
  
  @Override
  public boolean equals(Object term)
  {
    return term instanceof Term;
  }
}
