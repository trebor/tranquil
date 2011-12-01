package org.trebor.tranquil.model;

import java.util.List;

public interface Operator extends Term
{
  enum Arity {ONE, TWO, MANY}
  
  Arity getArity();
  List<Term> getOperands();
  List<List<Term>> getOperandPermutations();
  Term getOperand(int index);
  int getOperandCount();
  void addTerms(Term... terms);
}