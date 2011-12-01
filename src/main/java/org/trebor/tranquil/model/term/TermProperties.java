package org.trebor.tranquil.model.term;

import java.util.HashMap;
import java.util.Map;

public class TermProperties
{
  @SuppressWarnings("rawtypes")
  private static final Class[] COMUTATIVE = 
  {
    Add.class,
    Multiply.class,
  };
  
  @SuppressWarnings({
    "serial", "rawtypes"
  })
  private static final Map<Class, Integer> PRESIDENCE_MAP = new HashMap<Class, Integer>()
  {
    {
      put(Multiply.class, 1);
      put(Divide.class, 1);
      put(Add.class, 2);
      put(Subtract.class, 2);
      put(null, Integer.MAX_VALUE);
    }
  };

  @SuppressWarnings("rawtypes")
  private static final Class[] INFIX = 
  {
    Add.class,
    Subtract.class,
    Multiply.class,
    Divide.class,
  };
  
  private static final boolean isMember(Term term, @SuppressWarnings("rawtypes") Class[] set)
  {
    for (@SuppressWarnings("rawtypes") Class member: set)
      if (member.equals(term.getClass()))
        return true;
    
    return false;
  }
  
  public static final boolean isComutative(Term term)
  {
    return isMember(term, COMUTATIVE);
  }
  
  public static final boolean isAtomic(Term term)
  {
    return !Operator.class.isAssignableFrom(term.getClass());
  }
  
  public static final boolean isInfix(Term term)
  {
    return isMember(term, INFIX);
  }

  public enum Presidence
  {
    GREATER, 
    EQUAL,
    LESS,
  }
  
  public static final Presidence presidence(Operator a, Operator b)
  {
    Integer pA = PRESIDENCE_MAP.get(a != null ? a.getClass() : null);
    Integer pB = PRESIDENCE_MAP.get(b != null ? b.getClass() : null);
    
    if (pA == null)
      throw new Error("unmapped operator: " + a);
    if (pB == null)
      throw new Error("unmapped operator: " + b);
    
    return pA < pB ? Presidence.GREATER : pA == pB ? Presidence.EQUAL : Presidence.LESS;
  }
}
