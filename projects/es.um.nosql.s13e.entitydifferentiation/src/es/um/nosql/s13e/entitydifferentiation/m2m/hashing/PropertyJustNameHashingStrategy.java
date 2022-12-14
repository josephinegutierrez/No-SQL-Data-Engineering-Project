package es.um.nosql.s13e.entitydifferentiation.m2m.hashing;

import org.eclipse.collections.api.block.HashingStrategy;
import es.um.nosql.s13e.NoSQLSchema.Property;

public class PropertyJustNameHashingStrategy implements HashingStrategy<Property>
{
  private static final long serialVersionUID = 2729984112225047153L;

  public static boolean checkEquality(Property p0, Property p1)
  {
    return p0.getName().equals(p1.getName());
  }

  @Override
  public int computeHashCode(Property arg0)
  {
    // Minimal condition to be true
    return arg0.getName().hashCode();
  }

  @Override
  public boolean equals(Property arg0, Property arg1)
  {
    return checkEquality(arg0, arg1);
  }
}