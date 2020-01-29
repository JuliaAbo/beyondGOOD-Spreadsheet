package edu.cs3500.spreadsheets.model.visitor;

import edu.cs3500.spreadsheets.model.contents.BooleanVal;
import edu.cs3500.spreadsheets.model.contents.DoubleVal;
import edu.cs3500.spreadsheets.model.contents.IVal;
import edu.cs3500.spreadsheets.model.contents.StringVal;

/**
 * A visitor that allows you to go to different types of IVals and perform dynamic dispatch over
 * them, to return a String.
 */
public class StringVisitor implements IValVisitor<String> {
  private final String basecase;

  /**
   * Construct a StringVisitor.
   * @param b   the basecase (depending on the operation where the visitor is used)
   */
  public StringVisitor(String b) {
    this.basecase = b;
  }

  @Override
  public String apply(IVal c) {
    if (c == null) {
      return this.basecase;
    }
    return c.accept(this);
  }

  @Override
  public String visitStringVal(StringVal c) {
    return c.getStringVal();
  }

  @Override
  public String visitBooleanVal(BooleanVal b) {
    return this.basecase;
  }

  @Override
  public String visitDoubleVal(DoubleVal i) {
    return this.basecase;
  }
}
