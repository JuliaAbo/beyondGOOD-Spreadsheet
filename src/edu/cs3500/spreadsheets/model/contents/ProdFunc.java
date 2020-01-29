package edu.cs3500.spreadsheets.model.contents;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;


import edu.cs3500.spreadsheets.model.Coord;

import edu.cs3500.spreadsheets.model.visitor.IFormulaVisitor;
import edu.cs3500.spreadsheets.model.visitor.DoubleVisitor;
import edu.cs3500.spreadsheets.model.visitor.EvalVisitor;

/**
 * A function that represents an IFunction that is a product.
 */
public class ProdFunc implements IFunction<IFormula[], IVal> {
  private final IFormula[] myFormula;
  private HashMap<Coord, IVal> computedValues;

  /**
   * A constructor for this ProdFunc.
   * @param myFormula All the IFormula to be evaluated.
   */
  public ProdFunc(IFormula... myFormula) {
    this.myFormula = myFormula;
  }

  public IVal evaluate(HashMap<Coord, IVal> computedValues) {
    this.computedValues = new HashMap<Coord, IVal>();
    return this.apply(myFormula);
  }

  @Override
  public IVal apply(IFormula... myFormula) {
    DoubleVisitor doubleVis = new DoubleVisitor(null);
    EvalVisitor evalVis = new EvalVisitor(computedValues);
    ArrayList<IVal> myValues = new ArrayList<>();
    Double prod = 1.0;
    for (IFormula formula : myFormula) {
      myValues.addAll(evalVis.apply(formula));
    }
    ArrayList<Double> myDoubles = new ArrayList<>();
    for (IVal val : myValues) {
      myDoubles.add(doubleVis.apply(val));
    }
    //myDoubles.removeIf(Objects::isNull);
    myDoubles.removeAll(Collections.singleton(null));
    if (myDoubles.size() == 0) {
      return new DoubleVal(0.0);
    }
    for (IVal val : myValues) {
      double baseCase = 1.0;
      if (doubleVis.apply(val) == null) {
        prod = prod * baseCase;
      } else {
        prod = prod * doubleVis.apply(val);
      }
    }
    return new DoubleVal(prod);
  }

  /**
   * A method to return a copy of the formulas used in this ProdFunc.
   * @return a List of the IFormulas used in this ProdFunc
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
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }

    if (!(obj instanceof ProdFunc)) {
      return false;
    }

    ProdFunc p = (ProdFunc) obj;

    return Arrays.equals(p.myFormula, this.myFormula);
  }

  @Override
  public int hashCode() {
    return this.myFormula.hashCode();
  }

  @Override
  public String toString() {
    String result = "(PROD";

    for (IFormula f : this.myFormula) {
      result += (" " + f.toString());
    }

    return result + ")";
  }

}
