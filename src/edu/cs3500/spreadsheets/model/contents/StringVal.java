package edu.cs3500.spreadsheets.model.contents;

import edu.cs3500.spreadsheets.model.visitor.IFormulaVisitor;
import edu.cs3500.spreadsheets.model.visitor.IValVisitor;
import edu.cs3500.spreadsheets.sexp.SString;

/**
 * Represents a String value.
 */
public class StringVal implements IVal {
  private final String contents;

  /**
   * Constructs a StringVal.
   *
   * @param s contents of StringVal
   */
  public StringVal(String s) {
    this.contents = s;
  }

  public String getStringVal() {
    return this.contents;
  }

  @Override
  public SString getValue() {
    return new SString(this.contents);
  }

  @Override
  public StringVal evaluate() {
    return this;
  }

  @Override
  public <R> R accept(IValVisitor<R> visitor) {
    return visitor.visitStringVal(this);
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

    if (!(obj instanceof StringVal)) {
      return false;
    }

    StringVal s = (StringVal) obj;

    return (s.contents).equals(this.contents);
  }

  @Override
  public int hashCode() {
    return this.contents.hashCode();
  }

  @Override
  public String toString() {
    return this.contents;
  }
}
