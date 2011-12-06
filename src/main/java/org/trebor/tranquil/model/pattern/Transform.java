package org.trebor.tranquil.model.pattern;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.trebor.tranquil.model.term.Operator;
import org.trebor.tranquil.model.term.Term;
import org.trebor.tranquil.model.term.Properties;
import org.trebor.tranquil.view.TextRenderer;

public class Transform
{
  private final Term mTemplate;
  private final Term mResult;
  
  public Transform(Term template, Term result)
  {
    mTemplate = template;
    mResult = result;
  }
  
  public Term match(Term target)
  {
    Map<Slot, Term> slotMap = new HashMap<Slot, Term>();
    
    // if no match, return null
    
    if (!isMatch(target, mTemplate, slotMap))
      return null;
    
    // populate the result from the variable map

    Term result = null;
    
    try
    {
      result = populateResult(mResult, slotMap);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    
    return result;
  }
  
  private Term populateResult(Term result, Map<Slot, Term> slotMap) throws InstantiationException, IllegalAccessException
  {
    // if this result is a varible, return value in the map
    
    if (result instanceof Slot)
      return slotMap.get(result).copy();
      
    // if this result is atmoc,return copy of atomic term
    
    if (Properties.isAtomic(result))
      return result.copy();

    // the result is assumed to be an operator, recurese
    
    return populateResult((Operator)result, slotMap);
  }

  private Term populateResult(Operator result, Map<Slot, Term> slotMap) throws InstantiationException, IllegalAccessException
  {
    Operator newResult = result.getClass().newInstance();
    
    for (Term operand: result.getOperands())
      newResult.addTerms(populateResult(operand, slotMap));
    
    return newResult;
  }
  
  private boolean isMatch(Term target, Term template, Map<Slot, Term> slotMap)
  {
    if (template instanceof Slot)
      return matchSlot(target, (Slot)template, slotMap);
    
    // if template and target are operators, match operator

    if (template instanceof Operator && target instanceof Operator)
      return matchOperator((Operator)target, (Operator)template, slotMap);
    
    // otherwise match atoms
    
    return template.equals(target);
  }

  private boolean matchSlot(Term target, Slot slot,
    Map<Slot, Term> slotMap)
  {
    // if target does not match slot, no match
    
    if (!slot.equals(target))
      return false;

    // establish value for this slot

    Term value = slotMap.get(slot);

    // if undefined, assign it, yes match

    if (null == value)
    {
      slotMap.put(slot, target);
      return true;
    }

    // match depends on assigned value equaling target
    
    return value.equals(target);
  }

  private boolean matchOperator(Operator target, Operator template,
    Map<Slot, Term> slotMap)
  {
    // if type wrong, no match

    if (target.getClass() != template.getClass())
      return false;

    // if operand count is wrong, no match

    if (target.getOperandCount() != template.getOperandCount())
      return false;

    // if any operand fails to match, no match

    for (List<Term> operands : target.getOperandPermutations())
    {
      Map<Slot, Term> tempMap = new HashMap<Slot, Term>();
      tempMap.putAll(slotMap);
      boolean match = true;
      for (int i = 0; i < target.getOperandCount(); ++i)
      {
        if (!isMatch(operands.get(i), template.getOperand(i), tempMap))
        {
          match = false;
          break;
        }
      }

      // if match found, be sure to update the valid variable bindings

      if (match)
      {
        slotMap.putAll(tempMap);
        return true;
      }
    }

    // no match

    return false;
  }
  
  public String toString()
  {
    return TextRenderer.render(mTemplate) + " -> " + TextRenderer.render(mResult);
  }
}
