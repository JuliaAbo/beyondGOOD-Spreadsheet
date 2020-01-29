package edu.cs3500.spreadsheets.model.visitor;


import edu.cs3500.spreadsheets.model.contents.ColumnReference;
import edu.cs3500.spreadsheets.model.contents.IFormula;
import edu.cs3500.spreadsheets.model.contents.IFunc;
import edu.cs3500.spreadsheets.model.contents.IFunction;
import edu.cs3500.spreadsheets.model.contents.IVal;
import edu.cs3500.spreadsheets.model.contents.Reference;

/**
 * A visitor that allows you to go to different types of IFormulas and perform dynamic dispatch over
 * them.
 * @param <R> the return type of the visitor
 */
public interface IFormulaVisitor<R> extends IFunc<IFormula, R> {

  @Override
  public R apply(IFormula arg);

  /**
   * A method that visits an IVal and calls the correct method on c.
   *
   * @param c the IVal to be visited.
   * @return the desired return type
   */
  public R visitIVal(IVal c);

  /**
   * A method that visits a Reference and calls the correct method on b.
   *
   * @param b the Reference to be visited.
   * @return the desired return type
   */
  public R visitReference(Reference b);

  public R vistColumnReference(ColumnReference c);

  /**
   * A method that visits a Function and calls the correct method on i.
   *
   * @param i the Function to be visited.
   * @return the desired return type
   */
  public R visitFunction(IFunction i);
}
