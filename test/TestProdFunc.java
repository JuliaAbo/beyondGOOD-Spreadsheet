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

import static junit.framework.TestCase.assertEquals;


/**
 * Tests the product function.
 */
public class TestProdFunc {

  @Test
  public void testProdReferenceZero() {
    HashMap<Coord, Cell> myMapofCell = new HashMap<Coord, Cell>();
    HashMap<Coord, IVal> myMap = new HashMap<Coord, IVal>();

    DoubleVal doubleFive = new DoubleVal(5.0);

    Coord a1 =  new Coord(1, 1);
    Coord a2 = new Coord(1, 2);

    Cell myA1 = new Cell(doubleFive);
    Cell myA2 = new Cell(doubleFive);

    myMapofCell.put(a1, myA1);
    myMapofCell.put(a2, myA2);

    Reference third = new Reference(a1, a2, myMapofCell);

    myMap.put(a1, new DoubleVal(5.0));
    myMap.put(a2, new DoubleVal(5.0));

    ProdFunc mySf = new ProdFunc(third);
    assertEquals( new DoubleVal(25.0), mySf.evaluate(myMap));
  }

  @Test
  public void testProdOneIValRef() {
    HashMap<Coord, Cell> myMapofCell = new HashMap<Coord, Cell>();
    HashMap<Coord, IVal> myMap = new HashMap<Coord, IVal>();

    DoubleVal doubleFive = new DoubleVal(5.0);

    Coord a1 =  new Coord(1, 1);
    Coord a2 = new Coord(1, 2);

    Cell myA1 = new Cell(doubleFive);
    Cell myA2 = new Cell(doubleFive);

    myMapofCell.put(a1, myA1);
    myMapofCell.put(a2, myA2);

    Reference second = new Reference(a2, a2, myMapofCell);

    myMap.put(a1, new DoubleVal(5.0));
    myMap.put(a2, new DoubleVal(5.0));

    ProdFunc mySf = new ProdFunc(second, doubleFive);
    assertEquals( new DoubleVal(25.0), mySf.evaluate(myMap));
  }

  @Test
  public void testProdOneIValRefSwitched() {
    HashMap<Coord, Cell> myMapofCell = new HashMap<Coord, Cell>();
    HashMap<Coord, IVal> myMap = new HashMap<Coord, IVal>();

    DoubleVal doubleFive = new DoubleVal(5.0);

    Coord a1 =  new Coord(1, 1);
    Coord a2 = new Coord(1, 2);

    Cell myA1 = new Cell(doubleFive);
    Cell myA2 = new Cell(doubleFive);

    myMapofCell.put(a1, myA1);
    myMapofCell.put(a2, myA2);

    Reference second = new Reference(a2, a2, myMapofCell);

    myMap.put(a1, new DoubleVal(5.0));
    myMap.put(a2, new DoubleVal(5.0));

    ProdFunc mySf = new ProdFunc(doubleFive, second);
    assertEquals( new DoubleVal(25.0), mySf.evaluate(myMap));
  }

  @Test
  public void testProdOneIValRefTwice() {
    HashMap<Coord, Cell> myMapofCell = new HashMap<Coord, Cell>();
    HashMap<Coord, IVal> myMap = new HashMap<Coord, IVal>();

    DoubleVal doubleFive = new DoubleVal(5.0);

    Coord a1 =  new Coord(1, 1);
    Coord a2 = new Coord(1, 2);

    Cell myA1 = new Cell(doubleFive);
    Cell myA2 = new Cell(doubleFive);

    myMapofCell.put(a1, myA1);
    myMapofCell.put(a2, myA2);

    Reference second = new Reference(a2, a2, myMapofCell);

    myMap.put(a1, new DoubleVal(5.0));
    myMap.put(a2, new DoubleVal(5.0));

    ProdFunc mySf = new ProdFunc(second, second);
    assertEquals( new DoubleVal(25.0), mySf.evaluate(myMap));
  }

  @Test
  public void testEvaluateSimpleProd() {
    HashMap<Coord, IVal> myMap = new HashMap<Coord, IVal>();

    Coord a1 =  new Coord(1, 1);
    Coord a2 = new Coord(1, 2);

    myMap.put(a1, new DoubleVal(5.0));
    myMap.put(a2, new DoubleVal(5.0));

    IFormula d1 = new DoubleVal(2.0);
    IFormula d2 = new DoubleVal(3.0);
    IFormula d3 = new DoubleVal(6.0);

    IFormula[] formulas = new IFormula[2];

    formulas[0] = d1;
    formulas[1] = d2;

    ProdFunc sf = new ProdFunc(formulas);

    assertEquals(d3, sf.evaluate(myMap));
  }

  @Test
  public void testEvaluateMultNeg() {
    HashMap<Coord, IVal> myMap = new HashMap<Coord, IVal>();

    Coord a1 =  new Coord(1, 1);
    Coord a2 = new Coord(1, 2);

    myMap.put(a1, new DoubleVal(5.0));
    myMap.put(a2, new DoubleVal(5.0));

    IFormula d1 = new DoubleVal(-1.0);
    IFormula d2 = new DoubleVal(3.0);
    IFormula d3 = new DoubleVal(-3.0);

    IFormula[] formulas = new IFormula[2];

    formulas[0] = d1;
    formulas[1] = d2;

    ProdFunc sf = new ProdFunc(formulas);

    assertEquals(sf.evaluate(myMap), d3);
  }

  @Test
  public void testEvaluateNoNumeric() {
    HashMap<Coord, IVal> myMap = new HashMap<Coord, IVal>();

    Coord a1 =  new Coord(1, 1);
    Coord a2 = new Coord(1, 2);

    myMap.put(a1, new DoubleVal(5.0));
    myMap.put(a2, new DoubleVal(5.0));

    IFormula d1 = new StringVal("hi");
    IFormula d2 = new BooleanVal(true);

    IFormula[] formulas = new IFormula[2];

    formulas[0] = d1;
    formulas[1] = d2;

    ProdFunc sf = new ProdFunc(formulas);

    assertEquals(sf.evaluate(myMap), new DoubleVal(0.0));
  }

  @Test
  public void testEvaluateProdString() {
    HashMap<Coord, IVal> myMap = new HashMap<Coord, IVal>();

    Coord a1 =  new Coord(1, 1);
    Coord a2 = new Coord(1, 2);

    myMap.put(a1, new DoubleVal(5.0));
    myMap.put(a2, new DoubleVal(5.0));

    IFormula d1 = new StringVal("hi");
    IFormula d3 = new DoubleVal(3.0);

    IFormula[] formulas = new IFormula[2];

    formulas[0] = d1;
    formulas[1] = d3;

    ProdFunc sf = new ProdFunc(formulas);

    assertEquals(sf.evaluate(myMap), d3);
  }

  @Test
  public void testEvaluateAddBoolean() {
    HashMap<Coord, IVal> myMap = new HashMap<Coord, IVal>();

    Coord a1 =  new Coord(1, 1);
    Coord a2 = new Coord(1, 2);

    myMap.put(a1, new DoubleVal(5.0));
    myMap.put(a2, new DoubleVal(5.0));

    IFormula d1 = new BooleanVal(false);
    IFormula d3 = new DoubleVal(3.0);

    IFormula[] formulas = new IFormula[2];

    formulas[0] = d1;
    formulas[1] = d3;

    ProdFunc sf = new ProdFunc(formulas);

    assertEquals(sf.evaluate(myMap), d3);
  }

  @Test
  public void testEvaluateAddBlank() {
    HashMap<Coord, IVal> myMap = new HashMap<Coord, IVal>();

    Coord a1 =  new Coord(1, 1);
    Coord a2 = new Coord(1, 2);

    myMap.put(a1, new DoubleVal(5.0));
    myMap.put(a2, new DoubleVal(5.0));

    IFormula d1 = new BooleanVal(false);
    IFormula d3 = new DoubleVal(3.0);

    IFormula[] formulas = new IFormula[2];

    formulas[0] = d1;
    formulas[1] = d3;

    ProdFunc sf = new ProdFunc(formulas);

    assertEquals(sf.evaluate(myMap), d3);
  }

}
