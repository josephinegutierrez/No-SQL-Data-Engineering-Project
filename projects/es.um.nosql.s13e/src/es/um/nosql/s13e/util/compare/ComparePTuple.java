package es.um.nosql.s13e.util.compare;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import es.um.nosql.s13e.NoSQLSchema.PTuple;
import es.um.nosql.s13e.NoSQLSchema.Type;

public class ComparePTuple extends Comparator<PTuple>
{
  @Override
  public boolean compare(PTuple t1, PTuple t2)
  {
    if (super.checkNulls(t1, t2))
      return false;

    if (super.checkEquals(t1, t2))
      return true;

    if (t1.getElements() == null && t2.getElements() == null)
      return true;

    if (t1.getElements() == null ^ t2.getElements() == null)
      return false;

    if (t1.getElements().size() != t2.getElements().size())
      return false;

    List<Type> t2ElemCopy = new ArrayList<Type>(t2.getElements());

    for (Type type1 : t1.getElements())
    {
      Optional<Type> typeToErase = t2ElemCopy.stream().filter(type2 ->
      {
        return new CompareType().compare(type1, type2);
      }).findFirst();

      if (typeToErase.isPresent())
        t2ElemCopy.remove(typeToErase.get());
    }

    return t2ElemCopy.isEmpty();
  }
}
