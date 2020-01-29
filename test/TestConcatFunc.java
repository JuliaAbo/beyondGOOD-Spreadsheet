import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import edu.cs3500.spreadsheets.model.Cell;
import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.contents.BooleanVal;
import edu.cs3500.spreadsheets.model.contents.ConcatFunc;
import edu.cs3500.spreadsheets.model.contents.DoubleVal;
import edu.cs3500.spreadsheets.model.contents.IFormula;
import edu.cs3500.spreadsheets.model.contents.IVal;
import edu.cs3500.spreadsheets.model.contents.Reference;
import edu.cs3500.spreadsheets.model.contents.StringVal;
import edu.cs3500.spreadsheets.model.contents.SumFunc;

import static junit.framework.TestCase.assertEquals;

/**
 * Tests the methods in ConcatFunc.
 */
public class TestConcatFunc {
  private ConcatFunc cf;
  private IFormula d1 = new DoubleVal(2.0);
  private IFormula d2 = new DoubleVal(3.0);
  private IFormula d3 = new DoubleVal(6.0);

  //IVals
  private DoubleVal doubleFive = new DoubleVal(5.0);
  private DoubleVal doubleZero = new DoubleVal(0.0);
  private StringVal stringHi = new StringVal("Hi");
  private StringVal stringSpace = new StringVal(" ");
  private StringVal stringHeyo = new StringVal("heyO");
  private BooleanVal boolTrue = new BooleanVal(true);


  HashMap<Coord, IVal> myMap = new HashMap<Coord, IVal>();
  HashMap<Coord, Cell> myMapofCell = new HashMap<Coord, Cell>();



  //Functions with references
  SumFunc a1anda2;

  //References
  Reference first;
  Reference second;
  Reference third;
  Reference fourth;


  private void initRef() {
    this.myMapofCell = new HashMap<Coord, Cell>();
    Coord a1 =  new Coord(1, 1);
    Coord a2 = new Coord(1, 2);
    Coord a3 = new Coord(1, 3);
    Coord b1 = new Coord(2, 1);
    Coord b2 = new Coord(2, 2);

    Cell myA1 = new Cell(stringHi);
    Cell myA2 = new Cell(stringSpace);
    Cell myA3 = new Cell(stringHeyo);
    Cell myB1 = new Cell(doubleFive);

    //Cell B2 = new Cell(myB1);
    this.myMapofCell.put(a1, myA1);
    this.myMapofCell.put(a2, myA2);
    this.myMapofCell.put(a3, myA3);
    this.myMapofCell.put(b1, myB1);

    first = new Reference(a1, a1, myMapofCell);
    second = new Reference(a2, a2, myMapofCell);
    third = new Reference(a1, a2, myMapofCell);
    fourth = new Reference(a1, b1, myMapofCell);

    this.a1anda2 = new SumFunc(new IFormula[] {first, second});
  }

  @Test
  public void testEvaluate() {
    this.initRef();
    this.cf = new ConcatFunc(new IFormula[] {this.first});
    assertEquals(new StringVal("Hi"), this.cf.evaluate(this.myMap));

    this.cf = new ConcatFunc(new IFormula[] {this.third});
    assertEquals(new StringVal(" Hi"), this.cf.evaluate(this.myMap));

    this.cf = new ConcatFunc(new IFormula[] {this.doubleZero, this.stringHeyo, this.boolTrue});
    assertEquals(new StringVal("heyO"), this.cf.evaluate(this.myMap));

    this.cf = new ConcatFunc(new IFormula[] {});
    assertEquals(new StringVal(""), this.cf.evaluate(this.myMap));
  }

  @Test
  public void testApply() {
    this.initRef();

    this.cf = new ConcatFunc(new IFormula[] {this.first});
    assertEquals(new StringVal("Hi"), this.cf.apply(new IFormula[] {this.first}));

    this.cf = new ConcatFunc(new IFormula[] {this.third});
    assertEquals(new StringVal(" Hi"), this.cf.apply(new IFormula[] {this.third}));

    this.cf = new ConcatFunc(new IFormula[] {this.doubleZero, this.stringHeyo, this.boolTrue});
    assertEquals(new StringVal("heyO"), this.cf.apply(new IFormula[] {this.doubleZero,
        this.stringHeyo, this.boolTrue}));

    this.cf = new ConcatFunc(new IFormula[] {});
    assertEquals(new StringVal(""), this.cf.apply(new IFormula[] {}));
  }

  @Test
  public void testGetFormulas() {
    this.initRef();

    this.cf = new ConcatFunc(new IFormula[] {this.first});
    assertEquals(Arrays.asList(new IFormula[] {this.first}), this.cf.getFormulas());

    this.cf = new ConcatFunc(new IFormula[] {this.third});
    assertEquals(Arrays.asList(new IFormula[] {this.third}), this.cf.getFormulas());

    this.cf = new ConcatFunc(new IFormula[] {this.doubleZero, this.stringHeyo, this.boolTrue});
    assertEquals(Arrays.asList(new IFormula[] {this.doubleZero, this.stringHeyo, this.boolTrue}),
            this.cf.getFormulas());

    this.cf = new ConcatFunc(new IFormula[] {});
    assertEquals(new ArrayList<IFormula>(), this.cf.getFormulas());
  }

  @Test
  public void testEquals() {
    this.cf = new ConcatFunc(new IFormula[] {this.first});
    ConcatFunc c2 = new ConcatFunc(new IFormula[] {this.first});
    ConcatFunc c3 = new ConcatFunc(new IFormula[] {
        this.doubleZero, this.stringHeyo, this.boolTrue });

    assertEquals(this.cf.equals(this.cf), true);
    assertEquals(this.cf.equals(c2), true);
    assertEquals(this.cf.equals(c3), false);
    assertEquals(c2.equals(c3), false);
    assertEquals(c2.equals(this.cf), true);
    assertEquals(c3.equals(this.cf), false);
  }

  @Test
  public void testEvaluate2() {
    this.initRef();
    this.cf = new ConcatFunc(new IFormula[] {this.first});
    assertEquals(new StringVal("Hi"), this.cf.evaluate(this.myMap));

    this.cf = new ConcatFunc(new IFormula[] {this.third});
    assertEquals(new StringVal(" Hi"), this.cf.evaluate(this.myMap));

    this.cf = new ConcatFunc(new IFormula[] {this.doubleZero, this.stringHeyo, this.boolTrue});
    assertEquals(new StringVal("heyO"), this.cf.evaluate(this.myMap));

    this.cf = new ConcatFunc(new IFormula[] {});
    assertEquals(new StringVal(""), this.cf.evaluate(this.myMap));
  }

  @Test
  public void testApply2() {
    this.initRef();

    this.cf = new ConcatFunc(new IFormula[] {this.first});
    assertEquals(new StringVal("Hi"), this.cf.apply(new IFormula[] {this.first}));

    this.cf = new ConcatFunc(new IFormula[] {this.third});
    assertEquals(new StringVal(" Hi"), this.cf.apply(new IFormula[] {this.third}));

    this.cf = new ConcatFunc(new IFormula[] {this.doubleZero, this.stringHeyo, this.boolTrue});
    assertEquals(new StringVal("heyO"), this.cf.apply(new IFormula[]
        {this.doubleZero, this.stringHeyo, this.boolTrue}));

    this.cf = new ConcatFunc(new IFormula[] {});
    assertEquals(new StringVal(""), this.cf.apply(new IFormula[] {}));
  }

  @Test
  public void testGetFormulas2() {
    this.initRef();

    this.cf = new ConcatFunc(new IFormula[] {this.first});
    assertEquals(Arrays.asList(new IFormula[] {this.first}), this.cf.getFormulas());

    this.cf = new ConcatFunc(new IFormula[] {this.third});
    assertEquals(Arrays.asList(new IFormula[] {this.third}), this.cf.getFormulas());

    this.cf = new ConcatFunc(new IFormula[] {this.doubleZero, this.stringHeyo, this.boolTrue});
    assertEquals(Arrays.asList(new IFormula[] {this.doubleZero, this.stringHeyo, this.boolTrue}),
            this.cf.getFormulas());

    this.cf = new ConcatFunc(new IFormula[] {});
    assertEquals(new ArrayList<IFormula>(), this.cf.getFormulas());
  }

  @Test
  public void testEquals2() {
    this.cf = new ConcatFunc(new IFormula[] {this.first});
    ConcatFunc c2 = new ConcatFunc(new IFormula[] {this.first});
    ConcatFunc c3 = new ConcatFunc(new IFormula[]
        {this.doubleZero, this.stringHeyo, this.boolTrue});

    assertEquals(this.cf.equals(this.cf), true);
    assertEquals(this.cf.equals(c2), true);
    assertEquals(this.cf.equals(c3), false);
    assertEquals(c2.equals(c3), false);
    assertEquals(c2.equals(this.cf), true);
    assertEquals(c3.equals(this.cf), false);
  }
}

