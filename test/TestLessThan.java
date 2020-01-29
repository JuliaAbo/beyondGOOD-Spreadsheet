import org.junit.Test;

import java.util.HashMap;

import edu.cs3500.spreadsheets.model.Cell;
import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.contents.BooleanVal;
import edu.cs3500.spreadsheets.model.contents.DoubleVal;
import edu.cs3500.spreadsheets.model.contents.IFormula;
import edu.cs3500.spreadsheets.model.contents.IVal;
import edu.cs3500.spreadsheets.model.contents.LessThan;
import edu.cs3500.spreadsheets.model.contents.Reference;
import edu.cs3500.spreadsheets.model.contents.StringVal;
import edu.cs3500.spreadsheets.model.contents.SumFunc;


import static junit.framework.TestCase.assertEquals;

/**
 * Tests the methods in LessThan.
 */
public class TestLessThan {

  private LessThan sf;

  private IFormula d1 = new DoubleVal(2.0);
  private IFormula d2 = new DoubleVal(3.0);
  private IFormula d3 = new DoubleVal(6.0);

  DoubleVal doubleFive = new DoubleVal(5.0);

  private IFormula[] formulas = new IFormula[2];

  HashMap<Coord, IVal> myMap = new HashMap<Coord, IVal>();
  HashMap<Coord, Cell> myMapofCell = new HashMap<Coord, Cell>();

  private void initFormulas() {
    this.formulas[0] = this.d1;
    this.formulas[1] = this.d2;

    this.sf = new LessThan(this.formulas);
  }

  //Functions with references
  SumFunc a1anda2;

  //References
  Reference first;
  Reference second;
  Reference third;


  /**
   * An initializing method.
   */
  private void initRef() {
    HashMap<Coord, Cell> myMapofCell = new HashMap<Coord, Cell>();
    Coord a1 =  new Coord(1, 1);
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
    this.a1anda2 = new SumFunc(new IFormula[] {first, second});
    this.myMap.put(a1, new DoubleVal(5.0));
    this.myMap.put(a2, new DoubleVal(5.0));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testLessThanRefTrue() {
    this.initRef();
    LessThan mySf = new LessThan(new IFormula[] {this.first, this.d3});
    mySf.evaluate(this.myMap);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testLessThanRefFalse() {
    this.initRef();
    LessThan mySf = new LessThan(new IFormula[] {this.second, this.doubleFive});
    assertEquals( new BooleanVal(false), mySf.evaluate(this.myMap));
  }


  @Test(expected = IllegalArgumentException.class)
  public void testLessOneArg() {
    this.initRef();
    LessThan mySf = new LessThan(new IFormula[] {this.doubleFive});
  }



  @Test(expected = IllegalArgumentException.class)
  public void testLessTooManyArg() {
    this.initRef();
    LessThan mySf = new LessThan(new IFormula[] {
        this.doubleFive, this.doubleFive, this.doubleFive});
  }

  @Test
  public void testEvaluateSimpleTrue() {
    initFormulas();
    assertEquals(new BooleanVal(true), ( sf.evaluate(myMap)));
  }

  @Test
  public void testEvaluateSimpleFalse() {
    initFormulas();
    IFormula dA = new DoubleVal(55.0);
    this.formulas[0] = dA;
    this.sf = new LessThan(this.formulas);
    assertEquals(new BooleanVal(false), sf.evaluate(myMap));
  }

  @Test
  public void testEvaluateNegatives() {
    initFormulas();
    IFormula dA = new DoubleVal(-1.0);
    this.d3 = new BooleanVal(true);
    this.formulas[0] = dA;
    this.sf = new LessThan(this.formulas);
    assertEquals(sf.evaluate(myMap), (this.d3));
  }


  @Test(expected = IllegalArgumentException.class)
  public void testEvaluateNoNumeric() {
    initFormulas();
    IFormula dA = new StringVal("hi");
    IFormula dB = new BooleanVal(true);
    this.d3 = new DoubleVal(3.0);
    this.formulas[0] = dA;
    this.formulas[1] = dB;
    this.sf = new LessThan(this.formulas);
    this.sf.evaluate(myMap);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testEvaluateString() {
    initFormulas();
    IFormula dA = new StringVal("hi");
    this.d3 = new DoubleVal(3.0);
    this.formulas[0] = dA;
    this.sf = new LessThan(this.formulas);
    this.sf.evaluate(myMap);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testEvaluateWithBoolean() {
    initFormulas();
    IFormula dA = new BooleanVal(false);
    this.d3 = new DoubleVal(3.0);
    this.formulas[0] = dA;
    this.sf = new LessThan(this.formulas);
    this.sf.evaluate(myMap);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testEvaluateWithBlank() {
    initFormulas();
    IFormula dA = new BooleanVal(false);
    this.d3 = new DoubleVal(3.0);
    this.formulas[0] = dA;
    this.sf = new LessThan(this.formulas);
    this.sf.evaluate(myMap);
  }


}
