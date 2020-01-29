package edu.cs3500.spreadsheets.model.contents;

import edu.cs3500.spreadsheets.model.visitor.IFormulaVisitor;
import edu.cs3500.spreadsheets.model.visitor.IValVisitor;
import edu.cs3500.spreadsheets.sexp.SBoolean;

/**
 * Represents a Boolean value whose contents are a boolean.
 */
public class BooleanVal implements IVal {
  private final Boolean contents;

  /**
   * Constructs a BooleanVal.
   * @param b contents of BooleanVal
   */
  public BooleanVal(boolean b) {
    this.contents = b;
  }

  @Override
  public SBoolean getValue() {
    return new SBoolean(contents);
  }

  @Override
  public BooleanVal evaluate() {
    return this;
  }

  @Override
  public <R> R accept(IValVisitor<R> visitor) {
    return visitor.visitBooleanVal(this);
  }

  @Override
  public <R> R accept(IFormulaVisitor<R> visitor) {
    return visitor.visitIVal(this);
  }

  @Override
  public IFormula getFormula() {
    return this;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }

    if (!(obj instanceof BooleanVal)) {
      return false;
    }

    BooleanVal b = (BooleanVal) obj;

    return b.contents == this.contents;
  }

  @Override
  public int hashCode() {
    return this.contents.hashCode();
  }

  @Override
  public String toString() {
    return this.contents.toString();
  }
}

