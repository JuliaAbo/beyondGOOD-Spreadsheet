package edu.cs3500.spreadsheets.model;

import java.util.HashMap;
import java.util.List;

import edu.cs3500.spreadsheets.model.contents.IFormula;
import edu.cs3500.spreadsheets.model.contents.IVal;
import edu.cs3500.spreadsheets.model.visitor.EvalVisitor;
import edu.cs3500.spreadsheets.model.visitor.RefVisitor;

/**
 * An interface that represents a cell in a spreadsheet and its available actions.
 */
public class Cell {
  private IFormula contents;
  private HashMap<Coord, IVal> computedValues;

  public Cell(IFormula contents) {
    this.contents = contents;
  }

  public Cell() {
    this.contents = null;
  }

  public Cell(IFormula contents, HashMap<Coord, IVal> computedValues) {
    this.contents = contents;
    this.computedValues = computedValues;
  }


  /**
   * A method that evaluates a Cell's contents to an IVal.
   * @return IVal the result of evaluating the Cell's contents. Return null if Cell is blank.
   * @throws RefException if the Cell is evaluated to a region.
   */
  public IVal evaluate(Coord myCoord) throws RefException, CyclicDataException {
    if (this.checkCycle(myCoord, this.contents)) {
      throw new CyclicDataException();
    }
    EvalVisitor myVisitor = new EvalVisitor(this.computedValues);
    List<IVal> myList = myVisitor.apply(this.contents);
    if (myList.size() > 1) {
      throw new RefException();
    } else if (myList.size() == 0) {
      return null;
    } else {
      return myList.get(0);
    }
  }

  private boolean checkCycle(Coord toCheck, IFormula contents) {
    RefVisitor myRefVisitor = new RefVisitor(toCheck);
    List<Coord> referenced = myRefVisitor.apply(contents);
    return (referenced.contains(toCheck));
  }

  public IFormula getContents() {
    return this.contents;
  }

  /**
   * A class to represent a reference exception that is thrown when a Cell is just a RefException.
   */
  public static class RefException extends Exception {
  }

  /**
   * A class to represent a reference exception that is thrown when a Cell is just a RefException.
   */
  public static class CyclicDataException extends IllegalArgumentException {
  }


  /**
   * A method that checks if this is equal to Object o.
   * @param obj the other object
   * @return a boolean if the object is equal or not.
   */
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }

    if (!(obj instanceof Cell)) {
      return false;
    }

    Cell c = (Cell) obj;

    if (this.contents == null && c.contents == null) {
      return true;
    }

    return this.contents.equals(c.contents);
  }

  @Override
  public int hashCode() {
    if (this.contents != null) {
      return this.contents.hashCode();
    }
    else {
      return -1;
    }
  }

  public void updateCell(IFormula f) {
    this.contents = f;
  }

}