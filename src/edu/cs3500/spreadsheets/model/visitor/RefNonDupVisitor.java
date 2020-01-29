package edu.cs3500.spreadsheets.model.visitor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.contents.ColumnReference;
import edu.cs3500.spreadsheets.model.contents.IFormula;
import edu.cs3500.spreadsheets.model.contents.IFunction;
import edu.cs3500.spreadsheets.model.contents.IVal;
import edu.cs3500.spreadsheets.model.contents.Reference;

/**
 * A visitor that allows you to go to different types of IFormulas through dynamic dispatch to
 * return a resulting HashSet of Coordinates that the IFormula references.
 */
public class RefNonDupVisitor implements IFormulaVisitor<HashSet<Coord>> {
  private Coord c;

  public RefNonDupVisitor(Coord c) {
    this.c = c;
  }

  @Override
  public HashSet<Coord> apply(IFormula arg) {
    if (arg == null) {
      return new HashSet<Coord>();
    } else {
      return arg.accept(this);
    }
  }

  @Override
  public HashSet<Coord> visitIVal(IVal c) {
    return new HashSet<Coord>();
  }

  @Override
  public HashSet<Coord> visitReference(Reference b) {
    List<Coord> myCoords = b.getCoords(this.c);
    return new HashSet<Coord>(myCoords);
  }

  @Override
  public HashSet<Coord> vistColumnReference(ColumnReference c) {
    List<Coord> myCoords = c.getCoords(this.c);
    return new HashSet<Coord>(myCoords);
  }

  @Override
  public HashSet<Coord> visitFunction(IFunction i) {
    ArrayList<Coord> myList = new ArrayList<>();

    List<IFormula> myFormulas = i.getFormulas();

    for (IFormula formula : myFormulas) {
      myList.addAll(this.apply(formula));
    }

    return new HashSet<>(myList);
  }
}
