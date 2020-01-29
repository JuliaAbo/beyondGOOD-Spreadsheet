package edu.cs3500.spreadsheets.model.contents;

import edu.cs3500.spreadsheets.model.visitor.IFormulaVisitor;
import edu.cs3500.spreadsheets.model.visitor.IValVisitor;
import edu.cs3500.spreadsheets.sexp.SNumber;

/**
 * Represents a Double value with a contents of a double.
 */
public class DoubleVal implements IVal {
  private Double contents;

  /**
   * Constructs a DoubleVal.
   *
   * @param d contents of DoubleVal
   */
  public DoubleVal(double d) {
    this.contents = d;
  }

  /**
   * Gets the contents of this DoubleVal.
   *
   * @return contents of this DoubleVal
   */
  public Double getDoubleVal() {
    return this.contents;
  }

  @Override
  public SNumber getValue() {
    return new SNumber(this.contents);
  }

  @Override
  public DoubleVal evaluate() {
    return this;
  }

  @Override
  public <R> R accept(IValVisitor<R> visitor) {
    return visitor.visitDoubleVal(this);
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

    if (!(obj instanceof DoubleVal)) {
      return false;
    }

    DoubleVal d = (DoubleVal) obj;

    return Math.abs(d.contents - this.contents) < 0.000001;
  }

  @Override
  public int hashCode() {
    return this.contents.hashCode();
  }

  @Override
  public String toString() {
    return String.format("%f", this.contents);
  }

}
