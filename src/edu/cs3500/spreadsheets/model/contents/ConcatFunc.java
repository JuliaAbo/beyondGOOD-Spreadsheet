package edu.cs3500.spreadsheets.model.contents;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.visitor.IFormulaVisitor;
import edu.cs3500.spreadsheets.model.visitor.StringVisitor;
import edu.cs3500.spreadsheets.model.visitor.EvalVisitor;

/**
 * Represents a function object that concatenates strings.
 */
public class ConcatFunc implements IFunction<IFormula[], IVal> {
  private final IFormula[] myFormula;
  private  HashMap<Coord, IVal> computedValues;

  /**
   * A constructor that builds an instance of this ConcatFunc.
   * @param myFormula the array of IFormulas to be evaluated
   */
  public ConcatFunc(IFormula... myFormula) {
    this.myFormula = myFormula;
    this.computedValues = new HashMap<Coord, IVal>();
  }

  @Override
  public IVal evaluate(HashMap<Coord, IVal> computedValues) {
    this.computedValues = computedValues;
    return this.apply(this.myFormula);
  }

  @Override
  public IVal apply(IFormula[] arg) {
    StringVisitor visitor = new StringVisitor("");
    EvalVisitor evalVis = new EvalVisitor(this.computedValues);
    ArrayList<IVal> myValues = new ArrayList<>();

    String result = "";
    for (IFormula formula : myFormula) {
      myValues.addAll(evalVis.apply(formula));
    }

    for (IVal val : myValues) {
      result = result + visitor.apply(val);
    }
    return new StringVal(result);
  }


  @Override
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
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }

    if (!(obj instanceof ConcatFunc)) {
      return false;
    }

    ConcatFunc c = (ConcatFunc) obj;

    return Arrays.equals(c.myFormula, this.myFormula);
  }

  @Override
  public int hashCode() {
    return this.myFormula.hashCode();
  }

  @Override
  public String toString() {
    String result = "(CONCAT";

    for (IFormula f : this.myFormula) {
      result += (" " + f.toString());
    }

    return result + ")";
  }
}
