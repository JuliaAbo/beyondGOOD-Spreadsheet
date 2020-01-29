package edu.cs3500.spreadsheets.model.visitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.cs3500.spreadsheets.model.Cell;
import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.contents.ColumnReference;
import edu.cs3500.spreadsheets.model.contents.IFormula;
import edu.cs3500.spreadsheets.model.contents.IFunction;
import edu.cs3500.spreadsheets.model.contents.IVal;
import edu.cs3500.spreadsheets.model.contents.Reference;

/**
 * A visitor that allows you to evaluate different types of IFormulas through dynamic dispatch to
 * return a resulting List-of IVal.
 */
public class EvalVisitor implements IFormulaVisitor<List<IVal>> {
  private HashMap<Coord, IVal> myComputedValues;

  /**
   * Constructs an EvalVisitor.
   * @param computedValues  Hashmap of coordinates in the spreadsheet that have already
   *                        been computed (to improve performance)
   */
  public EvalVisitor(HashMap<Coord, IVal> computedValues) {
    this.myComputedValues = computedValues;
  }

  @Override
  public List<IVal> apply(IFormula arg) {
    if (arg == null) {
      return new ArrayList<IVal>();
    } else {
      return arg.accept(this);
    }
  }

  @Override
  public List<IVal> visitIVal(IVal c) {
    ArrayList<IVal> myList = new ArrayList<>();
    myList.add(c.evaluate());
    return myList;
  }

  @Override
  public List<IVal> visitReference(Reference b) {
    try {
      return b.evaluate(myComputedValues);
    } catch (Cell.RefException e) {
      return new ArrayList<IVal>();
    }
  }

  @Override
  public List<IVal> vistColumnReference(ColumnReference c) {
    try {
      return c.evaluate(myComputedValues);
    } catch (Cell.RefException e) {
      return new ArrayList<IVal>();
    }
  }

  @Override
  public List<IVal> visitFunction(IFunction i) {
    ArrayList<IVal> myList = new ArrayList<>();
    myList.add(i.evaluate(myComputedValues));
    return myList;
  }
}



