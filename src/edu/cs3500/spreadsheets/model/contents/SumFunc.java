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
 * A class that returns a sum of the IFormulas passed in. If the formula evaluates to a nonnumeric
 * value, the value is skipped.
 */
public class SumFunc implements IFunction<IFormula[], IVal> {

  private IFormula[] myFormula;
  HashMap<Coord, IVal> computedValues;

  public SumFunc(IFormula... myFormula) {
    this.myFormula = myFormula;
    this.computedValues = new HashMap<Coord, IVal>();
  }

  public IVal evaluate(HashMap<Coord, IVal> computedValues) {
    this.computedValues = computedValues;
    return this.apply(myFormula);
  }

  @Override
  public IVal apply(IFormula... myFormula) {
    DoubleVisitor doubleVis = new DoubleVisitor(0.0);
    EvalVisitor evalVis = new EvalVisitor(this.computedValues);
    ArrayList<IVal> myValues = new ArrayList<>();
    double sum = 0.0;
    for (IFormula formula : myFormula) {
      myValues.addAll(evalVis.apply(formula));
    }
    for (IVal val : myValues) {
      sum = sum + doubleVis.apply(val);
    }
    return new DoubleVal(sum);
  }

  public List<IFormula> getFormulas() {
    IFormula[] duplicate = myFormula.clone();
    return Arrays.asList(duplicate);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }

    if (!(obj instanceof SumFunc)) {
      return false;
    }

    SumFunc p = (SumFunc) obj;

    return Arrays.equals(p.myFormula, this.myFormula);
  }

  @Override
  public int hashCode() {
    return this.myFormula.hashCode();
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
  public String toString() {
    String result = "(SUM";

    for (IFormula f : this.myFormula) {
      result += (" " + f.toString());
    }

    return result + ")";
  }

}
