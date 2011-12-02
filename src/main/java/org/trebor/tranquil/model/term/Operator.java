package org.trebor.tranquil.model.term;

import java.util.List;

public interface Operator extends Term
{
  enum Arity
  {
    ONE(1), TWO(2), MANY(Integer.MAX_VALUE);
    
    private final int mSize;
    
    Arity(int size)
    {
      mSize = size;
    }
    
    public boolean hasRoom(int size)
    {
      return size < mSize;
    }
  }
  
  Arity getArity();
  List<Term> getOperands();
  List<List<Term>> getOperandPermutations();
  Term getOperand(int index);
  int getOperandCount();
  void addTerms(Term... terms);
//  void replaceOperand(int i, Term term);
}