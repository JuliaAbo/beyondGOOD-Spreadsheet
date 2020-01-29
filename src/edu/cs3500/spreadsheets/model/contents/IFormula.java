package edu.cs3500.spreadsheets.model.contents;

import edu.cs3500.spreadsheets.model.visitor.IFormulaVisitor;

/**
 * An interface that represents a value, reference or function. It is the non-empty contents of the
 * cell.
 */
public interface IFormula {

  /**
   * Takes in a visitor and calls the appropriate method in that visitor based on this IFormula's
   * identity.
   * @param visitor the visitor.
   * @param <R> the desired return type
   * @return result of applying given visitor to this IFormula.
   */
  <R> R accept(IFormulaVisitor<R> visitor);

  /**
   * Gets this IFormula.
   * @return this IFormula
   */
  IFormula getFormula();

  /**
   * Equality check of this IFormula and the given Object.
   * @param o given object
   * @return boolean value of whether or not these values are equal.
   */
  boolean equals(Object o);

  /**
   * Returns the raw contents of this IFormula.
   * @return  unevaluated contents
   */
  String toString();

}
