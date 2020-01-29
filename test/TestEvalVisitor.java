import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.cs3500.spreadsheets.model.Cell;
import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.contents.BooleanVal;
import edu.cs3500.spreadsheets.model.contents.DoubleVal;
import edu.cs3500.spreadsheets.model.contents.IFormula;
import edu.cs3500.spreadsheets.model.contents.IVal;
import edu.cs3500.spreadsheets.model.contents.LessThan;
import edu.cs3500.spreadsheets.model.contents.ProdFunc;
import edu.cs3500.spreadsheets.model.contents.Reference;
import edu.cs3500.spreadsheets.model.contents.StringVal;
import edu.cs3500.spreadsheets.model.contents.SumFunc;
import edu.cs3500.spreadsheets.model.visitor.EvalVisitor;

import static junit.framework.TestCase.assertEquals;


/**
 * A test class to verify the behavior of EvalVisitor.
 */
public class TestEvalVisitor {
  HashMap<Coord, IVal> myMap = new HashMap<Coord, IVal>();
  HashMap<Coord, Cell> myMapofCell = new HashMap<Coord, Cell>();
  EvalVisitor myEvalVisitor = new EvalVisitor(myMap);

  //IVals
  DoubleVal doubleFive = new DoubleVal(5.0);
  DoubleVal doubleZero = new DoubleVal(0.0);
  DoubleVal doubleNegative = new DoubleVal(-5.0);
  DoubleVal doubleFour = new DoubleVal(4.0);
  StringVal stringHi = new StringVal("Hi");
  StringVal stringEmpty = new StringVal("");
  BooleanVal boolTrue = new BooleanVal(true);
  BooleanVal boolFalse = new BooleanVal(false);

  //Functions
  SumFunc mySumZero;
  SumFunc mySumNeg;
  SumFunc mySumNonDouble;
  SumFunc mySumAllOther;
  LessThan myLessThanTrue;
  LessThan myLessThanFalse;
  LessThan myLessThanFalseEqual;
  LessThan myLessThanNonNum;
  ProdFunc myProduct20;
  ProdFunc myProductNeg;
  ProdFunc myProductZero;
  ProdFunc myProductNonNum;
  ProdFunc myProductBaseCase;
  ProdFunc nestedFunctions;

  //Functions with references
  SumFunc a1anda2;

  //References
  Reference first;
  Reference second;
  Reference third;


  /**
   * A method that initializes variables for the purpose of referencing.
   */
  private void refInit() {
    Coord a1 =  new Coord(1, 1);
    Coord a2 = new Coord(1, 2);
    Coord a3 = new Coord(1, 3);

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
  }


  /**
   * A method to initialize data.
   */
  private void init() {
    // Functions
    this.mySumZero = new SumFunc(new IFormula[] {doubleFive, doubleNegative});
    this.mySumNeg = new SumFunc(new IFormula[] {doubleNegative, doubleFour});
    this.mySumNonDouble = new SumFunc(new IFormula[] {stringHi, doubleFour});
    this.mySumAllOther = new SumFunc(new IFormula[] {stringHi, boolTrue});
    this.myLessThanTrue = new LessThan(new IFormula[] {doubleNegative, doubleFive});
    this.myLessThanFalse = new LessThan(new IFormula[] {doubleFive, doubleNegative});
    this.myLessThanFalseEqual = new LessThan(new IFormula[] {doubleFive, doubleFive});
    this.myLessThanNonNum = new LessThan(new IFormula[] {doubleFive, stringHi});
    this.myProduct20 = new ProdFunc(new IFormula[] {doubleFive, doubleFour});
    this.myProductNeg = new ProdFunc(new IFormula[] {doubleNegative, doubleFour});
    this.myProductZero = new ProdFunc(new IFormula[] {doubleZero, doubleFive});
    this.myProductNonNum = new ProdFunc(new IFormula[] {stringHi, doubleFive});
    this.myProductBaseCase = new ProdFunc(new IFormula[] {stringHi, boolTrue});
    this.nestedFunctions = new ProdFunc(new IFormula[] {this.myProduct20, this.doubleFive});

  }

  @Test
  public void testSumWithRef() {
    this.refInit();
    List<IVal> myList = myEvalVisitor.apply(this.a1anda2);
    ArrayList<IVal> myExpected = new ArrayList<IVal>();
    myExpected.add(new DoubleVal(10.0));
    assertEquals(myExpected, myList);
  }

  @Test
  public void testRefValues() {
    this.refInit();
    List<IVal> myList = myEvalVisitor.apply(this.first);
    ArrayList<IVal> myExpected = new ArrayList<IVal>();
    myExpected.add(new DoubleVal(5.0));
    assertEquals(myExpected, myList);
  }

  @Test
  public void testRefValuesMult() {
    this.refInit();
    List<IVal> myList = myEvalVisitor.apply(this.third);
    ArrayList<IVal> myExpected = new ArrayList<IVal>();
    myExpected.add(new DoubleVal(5.0));
    myExpected.add(new DoubleVal(5.0));
    assertEquals(myExpected, myList);
  }


  @Test
  public void testSumZero() {
    this.init();
    List<IVal> myList = myEvalVisitor.apply(this.mySumZero);
    ArrayList<IVal> myExpected = new ArrayList<IVal>();
    myExpected.add(new DoubleVal(0.0));
    assertEquals(myExpected, myList);
  }

  @Test
  public void testSumNeg() {
    this.init();
    List<IVal> myList = myEvalVisitor.apply(this.mySumNeg);
    ArrayList<IVal> myExpected = new ArrayList<IVal>();
    myExpected.add(new DoubleVal(-1.0));
    assertEquals(myExpected, myList);
  }

  @Test
  public void testSumNonDouble() {
    this.init();
    List<IVal> myList = myEvalVisitor.apply(this.mySumNonDouble);
    ArrayList<IVal> myExpected = new ArrayList<IVal>();
    myExpected.add(new DoubleVal(4.0));
    assertEquals(myExpected, myList);
  }

  @Test
  public void testSumAllOther() {
    this.init();
    List<IVal> myList = myEvalVisitor.apply(this.mySumAllOther);
    ArrayList<IVal> myExpected = new ArrayList<IVal>();
    myExpected.add(new DoubleVal(0.0));
    assertEquals(myExpected, myList);
  }

  @Test
  public void testVisitLessThanTrue() {
    this.init();
    List<IVal> myList = myEvalVisitor.apply(this.myLessThanTrue);
    ArrayList<IVal> myExpected = new ArrayList<IVal>();
    myExpected.add(new BooleanVal(true));
    assertEquals(myExpected, myList);
  }

  @Test
  public void testVisitLessThanFalse() {
    this.init();
    List<IVal> myList = myEvalVisitor.apply(myLessThanFalse);
    ArrayList<IVal> myExpected = new ArrayList<IVal>();
    myExpected.add(new BooleanVal(false));
    assertEquals(myExpected, myList);
  }

  @Test
  public void testVisitNestedFunctions() {
    this.init();
    List<IVal> myList = myEvalVisitor.apply(this.nestedFunctions);
    ArrayList<IVal> myExpected = new ArrayList<IVal>();
    myExpected.add(new DoubleVal(100.0));
    assertEquals(myExpected, myList);
  }


  @Test
  public void testVisitLessThanFalseEqual() {
    this.init();
    List<IVal> myList = myEvalVisitor.apply(myLessThanFalseEqual);
    ArrayList<IVal> myExpected = new ArrayList<IVal>();
    myExpected.add(new BooleanVal(false));
    assertEquals(myExpected, myList);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMyLessThanNonNum() {
    this.init();
    List<IVal> myList = myEvalVisitor.apply(this.myLessThanNonNum);
    ArrayList<IVal> myExpected = new ArrayList<IVal>();
    myExpected.add(new BooleanVal(false));
  }

  @Test
  public void testMyProdNeg() {
    this.init();
    List<IVal> myList = myEvalVisitor.apply(this.myProductNeg);
    ArrayList<IVal> myExpected = new ArrayList<IVal>();
    myExpected.add(new DoubleVal(-20.0));
    assertEquals(myExpected, myList);
  }


  @Test
  public void testMyProdNonNum() {
    this.init();
    List<IVal> myList = myEvalVisitor.apply(this.myProductNonNum);
    ArrayList<IVal> myExpected = new ArrayList<IVal>();
    myExpected.add(new DoubleVal(5.0));
    assertEquals(myExpected, myList);
  }

  @Test
  public void testMyProdZero() {
    this.init();
    List<IVal> myList = myEvalVisitor.apply(this.myProductZero);
    ArrayList<IVal> myExpected = new ArrayList<IVal>();
    myExpected.add(new DoubleVal(0.0));
    assertEquals(myExpected, myList);
  }

  @Test
  public void testMyProd20() {
    this.init();
    List<IVal> myList = myEvalVisitor.apply(this.myProduct20);
    ArrayList<IVal> myExpected = new ArrayList<IVal>();
    myExpected.add(new DoubleVal(20.0));
    assertEquals(myExpected, myList);
  }

  @Test
  public void testMyProdBaseCase() {
    this.init();
    List<IVal> myList = myEvalVisitor.apply(this.myProductBaseCase);
    ArrayList<IVal> myExpected = new ArrayList<IVal>();
    myExpected.add(new DoubleVal(0.0));
    assertEquals(myExpected, myList);
  }

  @Test
  public void testVisitBoolTrue() {
    List<IVal> myList = myEvalVisitor.apply(boolTrue);
    ArrayList<IVal> myExpected = new ArrayList<IVal>();
    myExpected.add(new BooleanVal(true));
    assertEquals(myExpected, myList);
  }

  @Test
  public void testVisitBoolFalse() {
    List<IVal> myList = myEvalVisitor.apply(boolFalse);
    ArrayList<IVal> myExpected = new ArrayList<IVal>();
    myExpected.add(new BooleanVal(false));
    assertEquals(myExpected, myList);
  }

  @Test
  public void testVisitString() {
    List<IVal> myList = myEvalVisitor.apply(stringHi);
    ArrayList<IVal> myExpected = new ArrayList<IVal>();
    myExpected.add(new StringVal("Hi"));
    assertEquals(myExpected, myList);
  }

  @Test
  public void testVisitStringEmpty() {
    List<IVal> myList = myEvalVisitor.apply(stringEmpty);
    ArrayList<IVal> myExpected = new ArrayList<IVal>();
    myExpected.add(new StringVal(""));
    assertEquals(myExpected, myList);
  }

  @Test
  public void testVisitNum() {
    List<IVal> myList = myEvalVisitor.apply(doubleFive);
    ArrayList<IVal> myExpected = new ArrayList<IVal>();
    myExpected.add(new DoubleVal(5.0));
    assertEquals(myExpected, myList);
  }

  @Test
  public void testVisitNumZero() {
    List<IVal> myList = myEvalVisitor.apply(doubleZero);
    ArrayList<IVal> myExpected = new ArrayList<IVal>();
    myExpected.add(new DoubleVal(0.0));
    assertEquals(myExpected, myList);
  }

  @Test
  public void testVisitNumNegative() {
    List<IVal> myList = myEvalVisitor.apply(doubleNegative);
    ArrayList<IVal> myExpected = new ArrayList<IVal>();
    myExpected.add(new DoubleVal(-5.0));
    assertEquals(myExpected, myList);
  }


}
