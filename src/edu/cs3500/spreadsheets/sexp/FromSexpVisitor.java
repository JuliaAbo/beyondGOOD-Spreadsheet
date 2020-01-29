package edu.cs3500.spreadsheets.sexp;

import java.util.List;

/**
 * An abstracted function object for processing any {@link Sexp}ressions.
 *
 * @param <R> The return type of this function
 */
public interface FromSexpVisitor<R> {
  /**
   * Process a boolean value.
   *
   * @param b the value
   * @return the desired result
   */
  R visitBoolean(SBoolean b);

  /**
   * Process a numeric value.
   *
   * @param d the value
   * @return the desired result
   */
  R visitNumber(SNumber d);

  /**
   * Process a list value.
   *
   * @param l the contents of the list (not yet visited)
   * @return the desired result
   */
  R visitSList(List<Sexp> l);

  /**
   * Process a symbol.
   *
   * @param s the value
   * @return the desired result
   */
  R visitSymbol(SString s);

  /**
   * Process a string value.
   *
   * @param s the value
   * @return the desired result
   */
  R visitString(SString s);
}
