package org.trebor.tranquil.model;

import static org.trebor.tranquil.model.TermProperties.isComutative;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.trebor.tranquil.util.Util;
import org.trebor.tranquil.view.TextRenderer;

public abstract class AbstractOperator extends AbstractTerm implements Operator
{
  private final List<Term> mTerms;
  private final Arity mArity;
  
  public AbstractOperator(String name, Arity arity)
  {
    super(name);
    mArity = arity;
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
  
  public List<List<Term>> getOperandPermutations()
  {
    return isComutative(this)
      ? Util.permute(getOperands())
      : Collections.singletonList(getOperands());
  }
  
  public int getOperandCount()
  {
    return mTerms.size();
  }
  
  public void addTerms(Term...terms)
  {
    for (Term term: terms)
    {
      if (!mArity.hasRoom(mTerms.size()))
        throw new Error(this.toString() + " is about to add too many parameters with " + term);
        mTerms.add(term);
    }
  }
  
  @Override
  public boolean equals(Term other)
  {
    if (!super.equals(other))
      return false;
    
    Operator otherOp = (Operator)other;
    
    if (getOperandCount() != otherOp.getOperandCount())
      return false;

    for (List<Term> operandPermutation: getOperandPermutations())
    {
      boolean isEqual = true;
      for (int i = 0; i < getOperandCount(); ++i)
        if (!otherOp.getOperand(i).equals(operandPermutation.get(i)))
        {
          isEqual = false;
          break;
        }

      if (isEqual)
        return true;
    }
    
    return false;
  }
  
  @Override
  public String toString()
  {
    return TextRenderer.render(this);
  }

  public Arity getArity()
  {
    return mArity;
  }
}
