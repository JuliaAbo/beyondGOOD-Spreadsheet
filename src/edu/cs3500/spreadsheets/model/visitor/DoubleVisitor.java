package edu.cs3500.spreadsheets.model.visitor;

import edu.cs3500.spreadsheets.model.contents.BooleanVal;
import edu.cs3500.spreadsheets.model.contents.DoubleVal;
import edu.cs3500.spreadsheets.model.contents.IVal;
import edu.cs3500.spreadsheets.model.contents.StringVal;

/**
 * A visitor that allows you to go to different types of IVals and perform dynamic dispatch over
 * them to return a Double.
 */
public class DoubleVisitor implements IValVisitor<Double> {
  private final Double baseCase;

  /**
   * Constructs a DoubleVisitor.
   * @param baseCase  the basecase of this visitor (depending on the operation).
   */
  public DoubleVisitor(Double baseCase) {
    this.baseCase = baseCase;
  }

  @Override
  public Double visitStringVal(StringVal c) {
    return baseCase;
  }

  @Override
  public Double visitBooleanVal(BooleanVal b) {
    return baseCase;
  }

  @Override
  public Double visitDoubleVal(DoubleVal i) {
    return i.getDoubleVal();
  }


  @Override
  public Double apply(IVal arg) {
    if (arg == null) {
      return this.baseCase;
    }
    return arg.accept(this);
  }
}
