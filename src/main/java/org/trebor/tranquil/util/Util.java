package org.trebor.tranquil.util;

import java.util.ArrayList;
import java.util.List;

public class Util
{

  public static <T> List<List<T>> permute(List<T> input)
  {
    List<List<T>> output = new ArrayList<List<T>>();
  
    if (input.isEmpty())
      output.add(new ArrayList<T>());
    else
    {
      for (int i = 0; i < input.size(); ++i)
      {
        List<T> missing1 = new ArrayList<T>(input);
        T t = missing1.remove(i);
        List<List<T>> permutations = permute(missing1);
        for (List<T> permutation : permutations)
        {
          permutation.add(0, t);
          output.add(permutation);
        }
      }
    }
    return output;
  }

}
