import org.junit.Test;

import java.util.HashMap;

import edu.cs3500.spreadsheets.model.Cell;
import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.contents.BooleanVal;
import edu.cs3500.spreadsheets.model.contents.DoubleVal;
import edu.cs3500.spreadsheets.model.contents.IFormula;
import edu.cs3500.spreadsheets.model.contents.IVal;
import edu.cs3500.spreadsheets.model.contents.Reference;
import edu.cs3500.spreadsheets.model.contents.StringVal;
import edu.cs3500.spreadsheets.model.contents.SumFunc;

import static junit.framework.TestCase.assertEquals;


/**
 * Tests the methods in SumFunc.
 */
public class TestSumFunc {
  private SumFunc sf;

  private IFormula d1 = new DoubleVal(10.0);
  private IFormula d2 = new DoubleVal(5.0);
  private IFormula d3 = new DoubleVal(15.0);

  //IVals
  DoubleVal doubleFive = new DoubleVal(5.0);
  StringVal stringHi = new StringVal("Hi");


  private IFormula[] formulas = new IFormula[2];
  HashMap<Coord, IVal> myMap = new HashMap<Coord, IVal>();
  HashMap<Coord, Cell> myMapofCell = new HashMap<Coord, Cell>();

  private void initFormulas() {
    this.formulas[0] = this.d1;
    this.formulas[1] = this.d2;

    this.sf = new SumFunc(this.formulas);
    HashMap<Coord, IVal> myMap = new HashMap<Coord, IVal>();
  }

  //Functions with references
  SumFunc a1anda2;

  //References
  Reference first;
  Reference second;
  Reference third;


  /**
   * A initializing method.
   */
  private void initRef() {
    HashMap<Coord, Cell> myMapofCell = new HashMap<Coord, Cell>();
    Coord a1 = new Coord(1, 1);
    Coord a2 = new Coord(1, 2);
    Coord a3 = new Coord(1, 3);
    Coord b1 = new Coord(2, 1);
    Coord b2 = new Coord(2, 2);

    Cell myA1 = new Cell(doubleFive);
    Cell myA2 = new Cell(doubleFive);
    Cell myA3 = new Cell(a1anda2);

    //Cell B2 = new Cell(B1);
    this.myMapofCell.put(a1, myA1);
    this.myMapofCell.put(a2, myA2);
    this.myMapofCell.put(a3, myA3);

    first = new Reference(a1, a1, myMapofCell);
    second = new Reference(a2, a2, myMapofCell);
    third = new Reference(a1, a2, myMapofCell);
    this.a1anda2 = new SumFunc(new IFormula[]{first, second});
    this.myMap.put(a1, new DoubleVal(5.0));
    this.myMap.put(a2, new DoubleVal(5.0));
  }

  @Test
  public void testSumReference() {
    this.initRef();
    SumFunc mySf = new SumFunc(new IFormula[]{this.third});
    assertEquals(new DoubleVal(10.0), mySf.evaluate(this.myMap));
  }

  @Test
  public void testSumOneRef() {
    this.initRef();
    SumFunc mySf = new SumFunc(new IFormula[]{this.second, this.first});
    assertEquals(new DoubleVal(10.0), mySf.evaluate(this.myMap));
  }

  @Test
  public void testSumOneTwice() {
    this.initRef();
    SumFunc mySf = new SumFunc(new IFormula[]{this.second, this.second});
    assertEquals(new DoubleVal(10.0), mySf.evaluate(this.myMap));
  }

  @Test
  public void testSumOneRefSwitched() {
    this.initRef();
    SumFunc mySf = new SumFunc(new IFormula[]{this.first, this.second});
    assertEquals(new DoubleVal(10.0), mySf.evaluate(this.myMap));
  }

  @Test
  public void testSumRefWithString() {
    this.initRef();
    SumFunc mySf = new SumFunc(new IFormula[]{this.second, this.stringHi});
    assertEquals(new DoubleVal(5.0), mySf.evaluate(this.myMap));
  }

  @Test
  public void testEvaluateSimpleAdd() {
    initFormulas();

    assertEquals(sf.evaluate(this.myMap), (this.d3));
  }

  @Test
  public void testEvaluateAddNegatives() {
    initFormulas();
    IFormula dA = new DoubleVal(-2.5);
    this.d3 = new DoubleVal(2.5);
    this.formulas[0] = dA;
    this.sf = new SumFunc(this.formulas);
    assertEquals(sf.evaluate(myMap), this.d3);
  }

  @Test
  public void testEvaluateReturnNegative() {
    initFormulas();
    IFormula dA = new DoubleVal(-5.5);
    this.d3 = new DoubleVal(-.5);
    this.formulas[0] = dA;
    this.sf = new SumFunc(this.formulas);
    assertEquals(sf.evaluate(myMap), (this.d3));
  }

  @Test
  public void testEvaluateAddString() {
    initFormulas();
    IFormula dA = new StringVal("hi");
    this.d3 = new DoubleVal(5.0);
    this.formulas[0] = dA;
    this.sf = new SumFunc(this.formulas);
    assertEquals((sf.evaluate(myMap)), (this.d3));
  }

  @Test
  public void testEvaluateAddBoolean() {
    initFormulas();
    IFormula dA = new BooleanVal(false);
    this.d3 = new DoubleVal(5.0);
    this.formulas[0] = dA;
    this.sf = new SumFunc(this.formulas);
    assertEquals(sf.evaluate(myMap), this.d3);
  }

  @Test
  public void testEvaluateAddBlank() {
    initFormulas();
    IFormula dA = new BooleanVal(false);
    this.d3 = new DoubleVal(5.0);
    this.formulas[0] = dA;
    this.sf = new SumFunc(this.formulas);
    assertEquals(sf.evaluate(myMap), this.d3);
  }

}
