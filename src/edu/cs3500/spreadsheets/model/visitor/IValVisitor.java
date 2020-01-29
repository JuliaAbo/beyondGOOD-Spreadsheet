package edu.cs3500.spreadsheets.model.visitor;

import edu.cs3500.spreadsheets.model.contents.BooleanVal;
import edu.cs3500.spreadsheets.model.contents.DoubleVal;
import edu.cs3500.spreadsheets.model.contents.IFunc;
import edu.cs3500.spreadsheets.model.contents.IVal;
import edu.cs3500.spreadsheets.model.contents.StringVal;

/**
 * A visitor that allows you to go to different types of IVals and perform dynamic dispatch over
 * them.
 * @param <S> the return type of the visitor
 */
public interface IValVisitor<S> extends IFunc<IVal, S> {

  @Override
  S apply(IVal c);

  /**
   * A method that visits a stringVal and operates over StringVal c.
   *
   * @param c the StringVal to be operated over.
   * @return the desired return type as specified.
   */
  S visitStringVal(StringVal c);

  /**
   * A method that visits a stringVal and operates over BooleanVal b.
   *
   * @param b the BooleanVal to be operated over.
   * @return the desired return type as specified.
   */
  S visitBooleanVal(BooleanVal b);

  /**
   * A method that visits a stringVal and operates over DoubleVal i.
   *
   * @param i the DoubleVal to be operated over.
   * @return the desired return type as specified.
   */
  S visitDoubleVal(DoubleVal i);
}
