package edu.cs3500.spreadsheets.model.contents;


/**
 * A Function object that takes in one argument and has types A and R. A is the type of the argument
 * to be operated over and R is the return type.
 *
 * @param <A> the type of the argument to be operated over.
 * @param <R>  the return type.
 */
public interface IFunc<A, R> {

  /**
   * A method that calls the arg's accept method to appropriately manage operations over union data.
   * @param arg the argument to be operated on.
   * @return the result of operating on this argument.
   */
  R apply(A arg);
}
