
import org.junit.Test;

import java.util.HashMap;

import edu.cs3500.spreadsheets.model.Cell;
import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.contents.BooleanVal;
import edu.cs3500.spreadsheets.model.contents.DoubleVal;
import edu.cs3500.spreadsheets.model.contents.IFormula;
import edu.cs3500.spreadsheets.model.contents.IVal;
import edu.cs3500.spreadsheets.model.contents.ProdFunc;
import edu.cs3500.spreadsheets.model.contents.Reference;
import edu.cs3500.spreadsheets.model.contents.StringVal;
import edu.cs3500.spreadsheets.model.contents.SumFunc;

import static junit.framework.TestCase.assertEquals;


/**
 * Tests the methods in Cell.
 */
public class TestCell {

  @Test
  public void testEqualsIVals() {

    Cell a1 = new Cell(new DoubleVal(5));
    Cell b1 = new Cell(new DoubleVal(15));
    Cell c1 = new Cell(new StringVal("hello"));
    Cell a2 = new Cell(new BooleanVal(true));
    Cell b2 = new Cell(new BooleanVal(false));
    Cell c2 = new Cell(new DoubleVal(15));

    assertEquals(b1.equals(c2), true);
    assertEquals(a1.equals(a2), false);
    assertEquals(a2.equals(b2), false);
    assertEquals(c1.equals(b2), false);
  }

  @Test
  public void equalsRegion() {
    Coord topA1 = new Coord(1, 1);
    Coord topA2 = new Coord(1, 2);
    Coord topA3 = new Coord(1, 3);

    HashMap<Coord, Cell> myMapofCell = new HashMap<Coord, Cell>();

    Reference first = new Reference(topA1, topA1, myMapofCell);
    Reference sameRegion = new Reference(topA1, topA1, myMapofCell);
    Reference second = new Reference(topA2, topA2, myMapofCell);
    Reference third = new Reference(topA1, topA2, myMapofCell);
    IFormula a1anda2 = new SumFunc(first, second);

    Cell myA1 = new Cell(first);
    Cell myA1SameReg = new Cell(sameRegion);
    Cell myA2 = new Cell(a1anda2);
    Cell myA3 = new Cell(third);
    Cell myA4 = new Cell(second);

    myMapofCell.put(topA1, myA1);
    myMapofCell.put(topA2, myA2);
    myMapofCell.put(topA3, myA3);

    assertEquals(myA1.equals(myA4), false);
    assertEquals(myA1.equals(myA1SameReg), true);
  }

  @Test
  public void equalsFunction() {
    HashMap<Coord, Cell> myMapofCell = new HashMap<Coord, Cell>();

    Coord topA1 = new Coord(1, 1);
    Coord topA2 = new Coord(1, 2);

    Reference first = new Reference(topA1, topA1, myMapofCell);
    Reference second = new Reference(topA2, topA2, myMapofCell);

    IFormula a1anda2 = new SumFunc(first, second);
    DoubleVal doubleFive = new DoubleVal(5.0);

    IFormula sameArgsSum = new SumFunc(first, second);
    IFormula difFunc = new ProdFunc(doubleFive);

    Cell myA2 = new Cell(a1anda2);
    Cell myA5 = new Cell(sameArgsSum);
    Cell myA6 = new Cell(difFunc);

    assertEquals(myA5.equals(myA2), true);
    assertEquals(myA6.equals(myA2), false);
  }

  // Cannot throw a checked exception, so the tests throws an IllegalArg so we are sure we get it.
  @Test(expected = IllegalArgumentException.class)
  public void testEvaluateRefIllegal() {
    HashMap<Coord, IVal> myMap = new HashMap<Coord, IVal>();
    HashMap<Coord, Cell> myMapofCell = new HashMap<Coord, Cell>();

    Coord topA1 = new Coord(1, 1);
    Coord topA2 = new Coord(1, 2);

    Reference third = new Reference(topA1, topA2, myMapofCell);

    myMap.put(topA1, new DoubleVal(5.0));
    myMap.put(topA2, new DoubleVal(5.0));

    Cell myA3WithMap = new Cell(third, myMap);

    try {
      myA3WithMap.evaluate(topA1);
    } catch (Cell.RefException | Cell.CyclicDataException e) {
      throw new IllegalArgumentException();
    }
  }

  @Test
  public void testEvaluateBlankCell() {
    Cell myBlank = new Cell();

    try {
      assertEquals(myBlank.evaluate(new Coord(1, 1)), null);
    } catch (Cell.RefException | Cell.CyclicDataException e) {
      // should not throw exception, but we must catch a checked exception
    }
  }

  @Test
  public void testEvaluateValidRef() {
    HashMap<Coord, IVal> myMap = new HashMap<Coord, IVal>();
    HashMap<Coord, Cell> myMapofCell = new HashMap<Coord, Cell>();

    Coord topA1 = new Coord(1, 1);
    Coord topA2 = new Coord(1, 2);

    Reference third = new Reference(topA1, topA2, myMapofCell);

    myMap.put(topA1, new DoubleVal(5.0));
    myMap.put(topA2, new DoubleVal(5.0));

    Cell myA1WithMap = new Cell(third, myMap);

    try {
      assertEquals(myA1WithMap.evaluate(topA1), new DoubleVal(5.0));
    } catch (Cell.RefException | Cell.CyclicDataException e) {
      // should not throw exception, but we must catch a checked exception
    }
  }

  @Test
  public void testEvaluateIVal() {
    HashMap<Coord, IVal> myMap = new HashMap<Coord, IVal>();

    Coord topA1 = new Coord(1, 1);
    Coord topA2 = new Coord(1, 2);

    myMap.put(topA1, new DoubleVal(5.0));
    myMap.put(topA2, new DoubleVal(5.0));

    Cell a1WithMap = new Cell(new DoubleVal(5), myMap);

    try {
      assertEquals(a1WithMap.evaluate(topA1), new DoubleVal(5.0));
    } catch (Cell.RefException | Cell.CyclicDataException e) {
      // should not throw exception, but we must catch a checked exception
    }
  }

  @Test
  public void testEvaluateFunction() {
    HashMap<Coord, Cell> myMapofCell = new HashMap<Coord, Cell>();
    HashMap<Coord, IVal> myMap = new HashMap<Coord, IVal>();
    Coord topA1 = new Coord(1, 1);
    Coord topA2 = new Coord(1, 2);

    Reference first = new Reference(topA1, topA1, myMapofCell);
    Reference second = new Reference(topA2, topA2, myMapofCell);
    IFormula a1anda2 = new SumFunc(new IFormula[]{first, second});

    Cell myA1 = new Cell(first);
    Cell myA2 = new Cell(a1anda2);

    myMapofCell.put(topA1, myA1);
    myMapofCell.put(topA2, myA2);

    myMap.put(topA1, new DoubleVal(5.0));
    myMap.put(topA2, new DoubleVal(5.0));

    Cell myA2WithMap = new Cell(a1anda2, myMap);
    try {
      assertEquals(myA2WithMap.evaluate(topA2), new DoubleVal(10.0));
    } catch (Cell.RefException | Cell.CyclicDataException e) {
      // should not throw exception, but we must catch a checked exception
    }
  }

  @Test
  public void testGetContents() {
    HashMap<Coord, Cell> myMapofCell = new HashMap<Coord, Cell>();

    Coord topA1 = new Coord(1, 1);
    Coord topA2 = new Coord(1, 2);

    Reference first = new Reference(topA1, topA1, myMapofCell);
    Reference sameRegion = new Reference(topA1, topA1, myMapofCell);
    Reference second = new Reference(topA2, topA2, myMapofCell);
    IFormula a1anda2 = new SumFunc(new IFormula[]{first, second});

    DoubleVal doubleFive = new DoubleVal(5.0);
    IFormula difFunc = new ProdFunc(doubleFive);

    Cell a1 = new Cell(new DoubleVal(5));
    Cell myA1 = new Cell(first);
    Cell myA2 = new Cell(a1anda2);
    Cell myA6 = new Cell(difFunc);

    myMapofCell.put(topA1, myA1);
    myMapofCell.put(topA2, myA2);

    assertEquals(a1.getContents(), new DoubleVal(5));
    assertEquals(myA1.getContents(), sameRegion);
    assertEquals(myA6.getContents(), difFunc);
  }
}


