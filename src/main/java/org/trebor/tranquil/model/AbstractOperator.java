package org.trebor.tranquil.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class AbstractOperator implements Operator
{
  private List<Term> mTerms;
  
  public AbstractOperator()
  {
    mTerms = new ArrayList<Term>();
  }
  
  public Term getOperand(int index)
  {
    return mTerms.get(index);
  }

  public List<Term> getOperands()
  {
    return Collections.unmodifiableList(mTerms);
  }

  public int getOperandCount()
  {
    return mTerms.size();
  }
  
  protected void addTerms(Term...terms)
  {
    for (Term term: terms)
      mTerms.add(term);
  }
}
