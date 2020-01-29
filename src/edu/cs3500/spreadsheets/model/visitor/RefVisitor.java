package edu.cs3500.spreadsheets.model.visitor;

import java.util.ArrayList;
import java.util.List;

import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.contents.ColumnReference;
import edu.cs3500.spreadsheets.model.contents.IFormula;
import edu.cs3500.spreadsheets.model.contents.IFunction;
import edu.cs3500.spreadsheets.model.contents.IVal;
import edu.cs3500.spreadsheets.model.contents.Reference;


/**
 * A visitor that assists with checking for cyclic data. It returns a list of coordinates referenced
 * in each IFormula visited.
 */
public class RefVisitor implements IFormulaVisitor<List<Coord>> {
  private Coord c;

  public RefVisitor(Coord c) {
    this.c = c;
  }

  @Override
  public List<Coord> apply(IFormula arg) {
    if (arg == null) {
      return new ArrayList<Coord>();
    } else {
      return arg.accept(this);
    }
  }

  @Override
  public List<Coord> visitIVal(IVal c) {
    return new ArrayList<Coord>();
  }

  @Override
  public List<Coord> visitReference(Reference b) {
    return b.getCoords(this.c);
  }

  @Override
  public List<Coord> vistColumnReference(ColumnReference c) {
    return c.getCoords(this.c);
  }

  @Override
  public List<Coord> visitFunction(IFunction i) {
    ArrayList<Coord> myList = new ArrayList<>();
    List<IFormula> myFormulas = i.getFormulas();
    for (IFormula formula : myFormulas) {
      myList.addAll(this.apply(formula));
    }
    return myList;
  }
}
