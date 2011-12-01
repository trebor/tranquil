package org.trebor.tranquil.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.trebor.tranquil.view.TextRenderer;

public class Pattern
{
  private final Term mTemplate;
  private final Term mResult;
  
  public Pattern(Term template, Term result)
  {
    mTemplate = template;
    mResult = result;
  }
  
  public Term match(Term target)
  {
    Map<Variable, Term> variableMap = new HashMap<Variable, Term>();
    
    // if no match, return null
    
    if (!isMatch(target, mTemplate, variableMap))
      return null;
    
    // populate the result from the variable map

    Term result = null;
    
    try
    {
      result = populateResult(mResult, variableMap);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    
    return result;
  }
  
  private Term populateResult(Term result, Map<Variable, Term> variableMap) throws InstantiationException, IllegalAccessException
  {
    // if this result is a varible, return value in the map
    
    if (result instanceof Variable)
      return variableMap.get(result);
      

    // if this result is a constant, return the constant
    
    if (result instanceof Constant)
      return result;

    // the result is assumed now to be an operator, recurese
    
    return populateResult((Operator)result, variableMap);
  }

  private Term populateResult(Operator result, Map<Variable, Term> variableMap) throws InstantiationException, IllegalAccessException
  {
    Operator newResult = result.getClass().newInstance();
    
    for (Term operand: result.getOperands())
      newResult.addTerms(populateResult(operand, variableMap));
    
    return newResult;
  }
  
  private boolean isMatch(Term target, Term template, Map<Variable, Term> variableMap)
  {
    // if this is a variable slot in the template...
    
    if (template instanceof Variable)
    {
      if (!(target instanceof Variable || target instanceof Constant))
        return false;
      
      Term value = variableMap.get(template);
      
      // if undefined, assign it a value, this is a match
      
      if (null == value)
      {
        variableMap.put((Variable)template, target);
        return true;
      }
      
      // otherwise test it
      
      return value == target;
    }
    
    // if this is constant in the template...
    
    if (template instanceof Constant)
    {
      // if the target has no constant here, there is no match
      
      if (!(target instanceof Constant))
        return false;

      // otherwise test the value
      
      return ((Constant)target).getValue() == ((Constant)template).getValue();
    }
    
    // the the template must now be an operator, check target

    if (!(target instanceof Operator) || !target.getClass().equals(template.getClass()))
      return false;
    
    // match operators
    
    return isMatch((Operator)target, (Operator)template, variableMap);
  }

  private boolean isMatch(Operator target, Operator template,
    Map<Variable, Term> variableMap)
  {
    // if operand count is wrong, no match

    if (target.getOperandCount() != template.getOperandCount())
      return false;

    // if any operand fails to match, no match

    for (List<Term> operands: target.getOperandPermutations())
    {
      Map<Variable, Term> tempMap = new HashMap<Variable, Term>();
      tempMap.putAll(variableMap);
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
        variableMap.putAll(tempMap);
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
