package org.trebor.tranquil.model.term;

import static org.trebor.tranquil.model.term.TermProperties.isComutative;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.trebor.tranquil.util.Util;

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
  
  public void replaceOperand(int i, Term term)
  {
    mTerms.set(i, term.copy());
  }

  public Term evaluate(Map<Variable, Double> values)
  {
    List<Term> evaluted = getEvaluatedOperands(values);
    List<Term> computed = compute(evaluted);
    
    // if the computed is a single constant, then return that aconstant
    
    if (computed.size() == 1 && computed.get(0) instanceof Constant)
      return computed.get(0);
      
    // if the operands have not changed then just return this operator unmodified
    
    if (computed == mTerms)
      return this;
   
    // create a new instant of this operator
    
    Operator operator = null;
    try
    {
      operator = getClass().newInstance();
    }
    catch (InstantiationException e)
    {
      e.printStackTrace();
    }
    catch (IllegalAccessException e)
    {
      e.printStackTrace();
    }
    
    for (Term operand: computed)
      operator.addTerms(operand);
    
    return operator;
  }
  
  protected List<Term> compute(List<Term> terms)
  {
    List<Term> keeps = new ArrayList<Term>(); 
    Double tally = null;

    // if this is a comutative operator, it can handle partial computation
    
    if (isComutative(this))
    {
      for (Term term: terms)
      {
        if (term instanceof Constant)
          tally = compute(tally, ((Constant)term).getValue());
        else
          keeps.add(term);
      }
    }

    // this is a non-comutative operator, complete computation or nothin
    
    else
    {
      for (Term term: terms)
      {
        if (term instanceof Constant)
          tally = compute(tally, ((Constant)term).getValue());
        else
        {
          keeps = terms;
          tally = null;
          break;
        }
      }
    }
    
    // if there is a tally, append it to the keepers

    if (tally != null)
      keeps.add(new Constant(tally));

    // return the keeper operands
    
    return keeps;
  }
  
  public abstract Double compute(Double prior, Double next);
  
  protected List<Term> getEvaluatedOperands(Map<Variable, Double> values)
  {
    List<Term> evaluated = new ArrayList<Term>();
    boolean changed = false;
    
    for (Term oldOperand : mTerms)
    {
      Term newOperand = oldOperand.evaluate(values);
      evaluated.add(newOperand);
      if (oldOperand != newOperand)
        changed = true;
    }

    return changed ? evaluated : mTerms;
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
        mTerms.add(term.copy());
    }
  }
  
  @Override
  public boolean equals(Object other)
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
  
  public Arity getArity()
  {
    return mArity;
  }
}
