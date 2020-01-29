import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.cs3500.spreadsheets.model.Cell;
import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.contents.BooleanVal;
import edu.cs3500.spreadsheets.model.contents.ConcatFunc;
import edu.cs3500.spreadsheets.model.contents.DoubleVal;
import edu.cs3500.spreadsheets.model.contents.Reference;
import edu.cs3500.spreadsheets.model.contents.StringVal;
import edu.cs3500.spreadsheets.model.contents.SumFunc;
import edu.cs3500.spreadsheets.model.visitor.SexpToIFormula;
import edu.cs3500.spreadsheets.sexp.SNumber;
import edu.cs3500.spreadsheets.sexp.SString;
import edu.cs3500.spreadsheets.sexp.SSymbol;
import edu.cs3500.spreadsheets.sexp.Sexp;
import edu.cs3500.spreadsheets.sexp.SexpVisitor;

import static junit.framework.TestCase.assertEquals;

/**
 * Tests the methods in SexpToIFormula.
 */
public class TestSexpToIFormula {
  private HashMap<Coord, Cell> spreadsheet = new HashMap<Coord, Cell>();
  private SexpVisitor v;

  /**
   * Initializes this spreadsheet.
   */
  private void initMap() {
    Cell a1 = new Cell(new DoubleVal(5));
    Cell b1 = new Cell(new DoubleVal(15));
    Cell c1 = new Cell(new StringVal("hello"));
    Cell a2 = new Cell(new BooleanVal(true));
    Cell b2 = new Cell(new BooleanVal(false));
    Cell c2 = new Cell(new DoubleVal(3));

    this.spreadsheet.put(new Coord(1,1), a1);
    this.spreadsheet.put(new Coord(2,1), b1);
    this.spreadsheet.put(new Coord(3,1), c1);

    this.spreadsheet.put(new Coord(1,1), a2);
    this.spreadsheet.put(new Coord(2,1), b2);
    this.spreadsheet.put(new Coord(3,1), c2);
    

    this.v = new SexpToIFormula(this.spreadsheet);
  }

  @Test
  public void testVisitBoolean() {
    initMap();
    assertEquals(this.v.visitBoolean(false), new BooleanVal(false));
    assertEquals(this.v.visitBoolean(true), new BooleanVal(true));
  }

  @Test
  public void testVisitNumber() {
    initMap();
    assertEquals(this.v.visitNumber(1), new DoubleVal(1));
    assertEquals(this.v.visitNumber(0), new DoubleVal(0));
    assertEquals(this.v.visitNumber(-2), new DoubleVal(-2));
    assertEquals(this.v.visitNumber(1000000), new DoubleVal(1000000));
    assertEquals(this.v.visitNumber(-500), new DoubleVal(-500));
    assertEquals(this.v.visitNumber(0.0), new DoubleVal(0.0));
    assertEquals(this.v.visitNumber(3.1415), new DoubleVal(3.1415));
  }

  @Test
  public void testVisitSList() {
    initMap();
    List<Sexp> list1 = new ArrayList<Sexp>();
    list1.add(new SSymbol("SUM"));
    list1.add(new SString("2"));

    assertEquals(this.v.visitSList(list1), new SumFunc(new StringVal("2")));

    list1.add(new SNumber(-1));
    assertEquals(this.v.visitSList(list1),
            new SumFunc(new StringVal("2"), new DoubleVal(-1)));

    List<Sexp> list2 = new ArrayList<Sexp>();
    list2.add(new SSymbol("CONCAT"));
    list2.add(new SString("hello "));
    list2.add(new SString("it's me "));

    assertEquals(this.v.visitSList(list2),
            new ConcatFunc(new StringVal("hello "), new StringVal("it's me ")));

    List<Sexp> list3 = new ArrayList<Sexp>();
    list3.add(new SSymbol("PRODUCT"));
    //list3.add(new SSymbol("( "));
    list3.add(new SSymbol("SUM"));
    list3.add(new SNumber(2));
    list3.add(new SNumber(3));
    //list3.add(new SSymbol(")"));
    list3.add(new SNumber(4));

    // TODO
    //assertEquals(this.v.visitSList(list3),
    //        new ProdFunc(new SumFunc(new DoubleVal(2), new DoubleVal(3)), new DoubleVal(4)));


  }

  @Test (expected = IllegalArgumentException.class)
  public void testVisitSListExcep1() { // tests for bad symbol
    initMap();
    List<Sexp> list1 = new ArrayList<Sexp>();
    list1.add(new SSymbol("HELLO"));
    list1.add(new SString("2"));

    assertEquals(this.v.visitSList(list1), new SumFunc(new StringVal("2")));
  }

  @Test (expected = IllegalArgumentException.class)
  public void testVisitSListExcep2() { // tests for bad symbol (SList must start with function)
    initMap();
    List<Sexp> list1 = new ArrayList<Sexp>();
    list1.add(new SString("2"));

    assertEquals(this.v.visitSList(list1), new SumFunc(new StringVal("2")));
  }

  @Test
  public void testVisitSymbol() {
    initMap();
    assertEquals(this.v.visitSymbol("A1"), new Reference(new Coord(1,1),
            new Coord(1,1), this.spreadsheet));
    assertEquals(this.v.visitSymbol("A1:A2"), new Reference(new Coord(1,1),
            new Coord(1,2), this.spreadsheet));
    assertEquals(this.v.visitSymbol("B3"), new Reference(new Coord(2,3),
            new Coord(2,3), this.spreadsheet));
  }

  @Test
  public void testVisitString() {
    initMap();
    assertEquals(this.v.visitString("1"), new StringVal("1"));
    assertEquals(this.v.visitString("hello"), new StringVal("hello"));
    assertEquals(this.v.visitString("False"), new StringVal("False"));
    assertEquals(this.v.visitString("true"), new StringVal("true"));
    assertEquals(this.v.visitString("lalala"), new StringVal("lalala"));
    assertEquals(this.v.visitString(""), new StringVal(""));
    assertEquals(this.v.visitString("\""), new StringVal("\""));
  }

}
