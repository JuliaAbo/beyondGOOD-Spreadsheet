package edu.cs3500.spreadsheets.model.visitor;

import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.cs3500.spreadsheets.model.Cell;
import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.contents.BooleanVal;
import edu.cs3500.spreadsheets.model.contents.ColumnReference;
import edu.cs3500.spreadsheets.model.contents.ConcatFunc;
import edu.cs3500.spreadsheets.model.contents.DoubleVal;
import edu.cs3500.spreadsheets.model.contents.IFormula;

import edu.cs3500.spreadsheets.model.contents.LessThan;
import edu.cs3500.spreadsheets.model.contents.ProdFunc;
import edu.cs3500.spreadsheets.model.contents.Reference;
import edu.cs3500.spreadsheets.model.contents.StringVal;
import edu.cs3500.spreadsheets.model.contents.SumFunc;
import edu.cs3500.spreadsheets.sexp.SSymbol;
import edu.cs3500.spreadsheets.sexp.Sexp;
import edu.cs3500.spreadsheets.sexp.SexpVisitor;

/**
 * A function object that coverts an S-expression to a IFormula.
 */
public class SexpToIFormula implements SexpVisitor<IFormula> {
  private final HashMap<Coord, Cell> duplicate;

  /**
   * Constructs a SexpToIFormula visitor.
   * @param duplicate   a copy of the spreadsheet
   */
  public SexpToIFormula(HashMap<Coord, Cell> duplicate) {
    this.duplicate = duplicate;
  }

  @Override
  public IFormula visitBoolean(boolean b) {
    return new BooleanVal(b);
  }

  @Override
  public IFormula visitNumber(double d) {
    return new DoubleVal(d);
  }


  @Override
  public IFormula visitSList(List<Sexp> l) {

    IFormula[] myArgs = new IFormula[l.size() - 1];
    for (int i = 1; i < l.size(); i++) {
      myArgs[i - 1] = l.get(i).accept(this);
    }
    if (l.get(0).equals(new SSymbol("SUM"))) {

      return new SumFunc(myArgs);
    } else if (l.get(0).equals(new SSymbol("PRODUCT"))) {
      return new ProdFunc(myArgs);
    } else if (l.get(0).equals(new SSymbol("<"))) {
      return new LessThan(myArgs);
    } else if (l.get(0).equals(new SSymbol("CONCAT"))) {
      return new ConcatFunc(myArgs);
    } else {
      throw new IllegalArgumentException("You tried to call a function that does not exist");
    }
  }

  @Override
  public IFormula visitSymbol(String s) {
    Coord firstCoord = null;
    Coord secondCoord = null;

    int startingInd = s.indexOf(':');

    // if string is a reference
    if (startingInd != -1) {
      //if (isCol(s.substring(0, startingInd)) != -1 && isCol(s.substring(startingInd + 1)) != -1) {
      //  return new ColumnReference()
      //}
      String p1 = s.substring(0, startingInd);
      String p2 = s.substring(startingInd + 1);

      if (isCol(p1) != -1) {
        return new ColumnReference(isCol(p1), isCol(p2), this.duplicate);
      }
      firstCoord = this.isCoord(s.substring(0, startingInd));
      secondCoord = this.isCoord(s.substring(startingInd + 1));
    }
    // otherwise, single coord
    else {
      firstCoord = isCoord(s);

      if (firstCoord == null) {
        throw new IllegalArgumentException("Illegal Symbol");
      }

      return new Reference(firstCoord, firstCoord, this.duplicate);
    }

    if (firstCoord == null && secondCoord == null) {
      throw new IllegalArgumentException("Illegal Symbol");
    } else if (secondCoord == null) {
      return new Reference(firstCoord, firstCoord, this.duplicate);
    } else {
      return new Reference(firstCoord, secondCoord, this.duplicate);
    }
  }

  @Override
  public IFormula visitString(String s) {
    return new StringVal(s);
  }

  public IFormula apply(Sexp arg) {
    return arg.accept(this);
  }

  private Coord isCoord(String s) {
    Scanner scan = new Scanner(s);
    final Pattern cellRef = Pattern.compile("([A-Za-z]+)([1-9][0-9]*)");
    scan.useDelimiter("\\s+");
    int col = -1;
    int row = -1;
    while (scan.hasNext()) {
      while (scan.hasNext("#.*")) {
        scan.nextLine();
        scan.skip("\\s*");
      }
      String cell = scan.next();
      Matcher m = cellRef.matcher(cell);
      if (m.matches()) {
        col = Coord.colNameToIndex(m.group(1));
        row = Integer.parseInt(m.group(2));
      }
    }
    if (col == -1 || row == -1) {
      return null;
    } else {
      return new Coord(col, row);
    }
  }

  private int isCol(String s) {
    Scanner scan = new Scanner(s);
    final Pattern cellRef = Pattern.compile("([A-Za-z]+)");
    scan.useDelimiter("\\s+");
    int col = -1;
    while (scan.hasNext()) {
      while (scan.hasNext("#.*")) {
        scan.nextLine();
        scan.skip("\\s*");
      }
      String cell = scan.next();
      Matcher m = cellRef.matcher(cell);
      if (m.matches()) {
        col = Coord.colNameToIndex(m.group(1));
      }
    }
    return col;
  }
}
