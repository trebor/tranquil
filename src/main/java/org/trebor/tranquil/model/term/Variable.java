package org.trebor.tranquil.model.term;

import java.util.Map;
import java.util.Map.Entry;

public class Variable extends AbstractTerm
{
  public Variable(String name)
  {
    super(name);
  }
  
  public Variable copy()
  {
    return new Variable(getName());
  }

  public Term evaluate(Map<Variable, Double> values)
  {
    for (Entry<Variable, Double> entry: values.entrySet())
      if (entry.getKey().equals(this))
        return new Constant(entry.getValue());

    return this;
  }
}
