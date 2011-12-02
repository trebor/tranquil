package org.trebor.tranquil.model.term;

import org.trebor.tranquil.view.TextRenderer;

public abstract class AbstractTerm implements Term
{
  private final String mName;
  
  public AbstractTerm(String name)
  {
    mName = name;
  }

  @Override
  public boolean equals(Object other)
  {
    return getClass().equals(other.getClass()) && getName().equals(((Term)other).getName());
  }
  
  public String getName()
  {
    return mName;
  }
  
  @Override
  public String toString()
  {
    return TextRenderer.render(this);
  }
}
