package edu.cs3500.spreadsheets.model.contents;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.visitor.IFormulaVisitor;
import edu.cs3500.spreadsheets.model.visitor.DoubleVisitor;
import edu.cs3500.spreadsheets.model.visitor.EvalVisitor;

/**
 * A function object that represents a function in the spreadsheet which can determine if one
 * formula is less in numerical value than the other.  Returns a BooleanVal with the answer.
 */
public class LessThan implements IFunction<IFormula[], IVal> {
  private final IFormula[] myFormula;
  private HashMap<Coord, IVal> computedValues;

  /**
   * A constructor that ensures that this function compares exactly two formulae. Throws an
   * IllegalArgumentException if the correct number of arguments are not given.
   *
   * @param myFormula the array of formulas to be compared
   * @throws IllegalArgumentException if there are more or less than 2 arguments.
   */
  public LessThan(IFormula[] myFormula) throws IllegalArgumentException {
    if (myFormula.length != 2) {
      throw new IllegalArgumentException("Tried to pass too many to compare.");
    } else {
      this.myFormula = myFormula;
      this.computedValues = new HashMap<Coord, IVal>();
    }
  }

  /**
   * A method that evaluates a function. It calls the apply method of the function if this function
   * has not been evaluated already.
   *
   * @return an IVal of the simplified value - in this case a BooleanVal.
   */
  public IVal evaluate(HashMap<Coord, IVal> computedValues) {
    this.computedValues = new HashMap<Coord, IVal>();
    return this.apply(myFormula);
  }

  /**
   * A method that computes the simplified version of this less than formula, by returning a
   * BooleanVal that indicates whether or not the first argument is less than the second. Throws a
   * new IllegalArgumentException if either of the arguments to be checked are null.
   *
   * @param myFormula the array of arguments to be checked.
   * @return BooleanVal that indicates if the first argument is less than the second
   * @throws IllegalArgumentException if either argument is null.
   */
  @Override
  public IVal apply(IFormula[] myFormula) throws IllegalArgumentException {
    DoubleVisitor doubleVis = new DoubleVisitor(null);
    EvalVisitor evalVis = new EvalVisitor(this.computedValues);
    ArrayList<IVal> myValues = new ArrayList<>();
    for (IFormula formula : myFormula) {
      myValues.addAll(evalVis.apply(formula));
    }
    Double myVal1 = doubleVis.apply(myValues.get(0));
    Double myVal2 = doubleVis.apply(myValues.get(1));
    if (myVal2 == null || myVal1 == null) {
      throw new IllegalArgumentException("you can't compare these kinds of values!");
    } else {
      return new BooleanVal(myVal1 < myVal2);
    }
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }

    if (!(obj instanceof LessThan)) {
      return false;
    }

    LessThan p = (LessThan) obj;

    return Arrays.equals(p.myFormula, this.myFormula);
  }

  /**
   * A method that returns the formulas.
   *
   * @return a List of IFormula of the contents of the cell
   */
  public List<IFormula> getFormulas() {
    IFormula[] duplicate = myFormula.clone();
    return Arrays.asList(duplicate);
  }

  @Override
  public <R> R accept(IFormulaVisitor<R> visitor) {
    return visitor.visitFunction(this);
  }

  @Override
  public IFormula getFormula() {
    return this;
  }

  @Override
  public int hashCode() {
    return this.myFormula.hashCode();
  }

  @Override
  public String toString() {
    return "(< " + this.myFormula[0].toString() + " " + this.myFormula[1].toString() + ")";
  }
}
