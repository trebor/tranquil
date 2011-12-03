package org.trebor.tranquil.model.pattern;

import java.util.Map;

import org.trebor.tranquil.model.term.AbstractTerm;
import org.trebor.tranquil.model.term.Term;
import org.trebor.tranquil.model.term.Variable;

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

  public Term evaluate(Map<Variable, Double> values)
  {
    return this;
  }
}
