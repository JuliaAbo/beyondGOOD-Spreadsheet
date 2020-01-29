package edu.cs3500.spreadsheets.model.contents;

import java.util.HashMap;
import java.util.List;

import edu.cs3500.spreadsheets.model.Coord;

/**
 * Represents an IFunction which is a type of IFormula. Is able to evaluate and perform operations
 * on other IFormula.
 * @param <A> the desired type of input argument.
 * @param <R> the desired type of resulting argument.
 */
public interface IFunction<A, R> extends IFormula {

  /**
   * A method that calls the arg's accept method to appropriately manage operations over union data.
   * @param arg the argument to be operated on.
   * @return the result of operating on this argument.
   */
  R apply(A arg);

  /**
   * A method that returns an IVal representing the simplified form of this IFunction.
   * @param computedValues the HashMap of already computed coordinates and their values.
   * @return IVal the result of evaluating this function.
   */
  public IVal evaluate(HashMap<Coord, IVal> computedValues);

  /**
   * Gets a copy of the IFormulas used in this IFunction.
   * @return a list of the IFormulas used in this IFunction.
   */
  public List<IFormula> getFormulas();

  /**
   * Returns this function in String format.
   * @return  formatted function
   */
  String toString();
}
