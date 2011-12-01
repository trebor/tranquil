package org.trebor.tranquil.view;

import static org.trebor.tranquil.model.term.TermProperties.*;
import static org.trebor.tranquil.model.term.TermProperties.Presidence.*;

import java.util.List;

import org.trebor.tranquil.model.term.Operator;
import org.trebor.tranquil.model.term.Term;
import org.trebor.tranquil.model.term.TermProperties;

public class TextRenderer
{
  public static String render(Term term)
  {
    return render(term, null);
  }
  
  public static String render(Term term, Operator greatest)
  {
    return isAtomic(term)
      ? term.getName()
        : render((Operator)term, greatest);
  }

  public static String render(Operator operator, Operator greatest)
  {
    return TermProperties.isInfix(operator)
      ? renderInfix(operator, greatest)
      : renderPrefix(operator, greatest);
  }
  
  public static String renderPrefix(Operator o, Operator greatest)
  {
    StringBuffer result = new StringBuffer();
    
    result.append(o.getName() + "(");
    
    List<Term> operands = o.getOperands();
    int count = 0;
    for (Term operand: operands)
    {
      result.append(render(operand, greatest));
      if (++count < operands.size())
        result.append(", ");
    }
    result.append(")");
    
    return result.toString();
  }
  
  public static String renderInfix(Operator o, Operator greatest)
  {
    StringBuffer result = new StringBuffer();
    boolean requiresGrouping = presidence(o, greatest) != GREATER;
      
    if (requiresGrouping)
      result.append("(");
    
    List<Term> operands = o.getOperands();
    int count = 0;
    for (Term operand: operands)
    {
      result.append(render(operand, presidence(greatest, o) != GREATER
        ? o
          : greatest));
      
      if (++count < operands.size())
        result.append(" " + o.getName() + " ");
    }
    
    if (requiresGrouping)
      result.append(")");
    
    return result.toString();
  }
}
