package edu.cs3500.spreadsheets.model.contents;

import edu.cs3500.spreadsheets.model.visitor.IFormulaVisitor;
import edu.cs3500.spreadsheets.model.visitor.IValVisitor;
import edu.cs3500.spreadsheets.sexp.Sexp;

/**
 * A class that represents a Value. A Value is a Boolean, Double, or String.
 */
public interface IVal extends IFormula {

  /**
   * Get the value of this IVal in the form of an S-Expression.
   *
   * @return S-exp of this IVal.
   */
  Sexp getValue();

  /**
   * Retrieves the simplified IVal version of this formula.
   *
   * @return the simplified value of this formula.
   * @throws IllegalStateException if the contents is a reference
   */
  IVal evaluate() throws IllegalStateException;

  /**
   * Returns the result of applying the given IVal visitor to this IVal.
   *
   * @param visitor IValVisitor
   * @param <R>     the return type of the given visitor
   * @return result of applying given visitor to this IVal
   */
  <R> R accept(IValVisitor<R> visitor);

  /**
   * Returns the result of applying the given IFormula visitor to this IVal.
   *
   * @param visitor IFormulaVisitor
   * @param <R>     the return type of the given visitor
   * @return result of applying given visitor to this IVal
   */
  <R> R accept(IFormulaVisitor<R> visitor);

  /**
   * Equality check of this IVal and the given Object.
   *
   * @param obj given Object
   * @return boolean of whether this IVal is equal to the given Object.
   */
  boolean equals(Object obj);

  /**
   * Provides of unique identifier for this IVal.
   *
   * @return unique integer of this IVal
   */
  int hashCode();

  /**
   * Provides this IVal in String format.
   *
   * @return String of this IVal
   */
  String toString();

}
