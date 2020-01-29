
import org.junit.Test;

import java.util.HashMap;

import edu.cs3500.spreadsheets.model.BasicSpreadsheet;
import edu.cs3500.spreadsheets.model.Cell;
import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.contents.BooleanVal;
import edu.cs3500.spreadsheets.model.contents.DoubleVal;
import edu.cs3500.spreadsheets.model.contents.IFormula;
import edu.cs3500.spreadsheets.model.contents.LessThan;
import edu.cs3500.spreadsheets.model.contents.ProdFunc;
import edu.cs3500.spreadsheets.model.contents.Reference;
import edu.cs3500.spreadsheets.model.contents.StringVal;
import edu.cs3500.spreadsheets.model.contents.SumFunc;

import static junit.framework.TestCase.assertEquals;

/**
 * Tests the methods in BasicSpreadsheet.
 */
public class TestBasicSpreadsheet {
  private BasicSpreadsheet.BasicSpreadsheetBuilder builder;

  private void initBuilder() {
    this.builder = new BasicSpreadsheet.BasicSpreadsheetBuilder();
  }

  BasicSpreadsheet myBasic;
  Cell firstCell;

  private void init() {
    firstCell = new Cell(new BooleanVal(false));
    myBasic = new BasicSpreadsheet.BasicSpreadsheetBuilder()
            .createCell(1, 1, "false").createCell(1, 3, "true")
            .createWorksheet();
  }

  @Test
  public void testGetLargestCoordDefault() {
    initBuilder();
    init();
    assertEquals(this.myBasic.getLargestCoord(), new Coord(25, 25));
  }

  @Test
  public void testGetLargestCoordSmallerThanCorner() {
    initBuilder();
    init();
    this.builder.createCell(1, 1, "2.0");
    this.builder.createCell(27, 10, "2.0");
    this.myBasic = this.builder.createWorksheet();
    assertEquals(this.myBasic.getLargestCoord(), new Coord(27, 25));
  }

  @Test
  public void testGetLargestCoordYours() throws Cell.CyclicDataException {
    initBuilder();
    init();
    this.myBasic.updateCell(new Coord(100, 100), new DoubleVal(2));
    assertEquals(this.myBasic.getLargestCoord(), new Coord(100, 100));
  }

  @Test
  public void testGetSheet() {
    initBuilder();
    init();
    BasicSpreadsheet myDuplicate = new BasicSpreadsheet.BasicSpreadsheetBuilder()
            .createCell(1, 1, "false").createCell(1, 3, "true")
            .createWorksheet();
    assertEquals(this.myBasic.getSheet(), myDuplicate.getSheet());
  }

  @Test
  public void testGetSheetHashMap() {
    initBuilder();
    init();
    HashMap<Coord, Cell> duplicateSheet = new HashMap<Coord, Cell>();
    duplicateSheet.put(new Coord(1, 1), new Cell(new BooleanVal(false)));
    duplicateSheet.put(new Coord(1, 3), new Cell(new BooleanVal(true)));
    assertEquals(this.myBasic.getSheet(), duplicateSheet);
  }

  @Test
  public void testGetSheetHashMapBlank() {
    initBuilder();
    init();
    BasicSpreadsheet myDuplicate = new BasicSpreadsheet.BasicSpreadsheetBuilder().createWorksheet();
    HashMap<Coord, Cell> duplicateSheet = new HashMap<Coord, Cell>();
    assertEquals(myDuplicate.getSheet(), duplicateSheet);
  }


  @Test
  public void testBuilder1() {
    initBuilder();
    this.builder.createCell(1, 1, "2");
    this.builder.createCell(1, 2, "3");
    this.builder.createCell(1, 3, "false");

    BasicSpreadsheet sheet = this.builder.createWorksheet();

    assertEquals(sheet.getCellAt(new Coord(1, 1)), new Cell(new DoubleVal(2)));
    assertEquals(sheet.getCellAt(new Coord(1, 2)), new Cell(new DoubleVal(3)));
    assertEquals(sheet.getCellAt(new Coord(1, 3)), new Cell(new BooleanVal(false)));
  }

  @Test
  public void testBuilder2() {
    initBuilder();
    this.builder.createCell(100, 100, "\"heyo\"");
    this.builder.createCell(1, 4, "\"\"");

    BasicSpreadsheet sheet = this.builder.createWorksheet();

    assertEquals(sheet.getCellAt(new Coord(100, 100)), new Cell(new StringVal("heyo")));
    assertEquals(sheet.getCellAt(new Coord(1, 4)), new Cell(new StringVal("")));
  }

  @Test
  public void testBuilder3() {
    initBuilder();
    this.builder.createCell(3, 10, "(PRODUCT 2 3)");
    this.builder.createCell(5, 5, "(SUM 5 5)");
    this.builder.createCell(10, 10, "(PRODUCT (SUM E5 2) 3)");

    BasicSpreadsheet sheet = this.builder.createWorksheet();

    assertEquals(sheet.getCellAt(new Coord(3, 10)),
            new Cell(new ProdFunc(new DoubleVal(2), new DoubleVal(3))));
    assertEquals(sheet.getCellAt(new Coord(5, 5)),
            new Cell(new SumFunc(new DoubleVal(5), new DoubleVal(5))));

    HashMap<Coord, Cell> mysheet = new HashMap<Coord, Cell>();
    mysheet.put(new Coord(3, 10), new Cell(new ProdFunc(new DoubleVal(2), new DoubleVal(3))));
    mysheet.put(new Coord(5, 5), new Cell(new SumFunc(new DoubleVal(5), new DoubleVal(5))));

    assertEquals(sheet.getCellAt(new Coord(10, 10)),
            new Cell(new ProdFunc(
                    new SumFunc(
                            new Reference(
                                    new Coord(5, 5),
                                    new Coord(5, 5),
                                    mysheet),
                            new DoubleVal(2)),
                    new DoubleVal(3))));
  }

  @Test
  public void testBuilder4() { // test create empty spreadsheet
    initBuilder();

    BasicSpreadsheet sheet = this.builder.createWorksheet();

    assertEquals(sheet.getCellAt(new Coord(1, 1)), new Cell());
    assertEquals(sheet.getCellAt(new Coord(1, 2)), new Cell());
    assertEquals(sheet.getCellAt(new Coord(1, 3)), new Cell());
    assertEquals(sheet.getCellAt(new Coord(1, 4)), new Cell());
    assertEquals(sheet.getCellAt(new Coord(2, 1)), new Cell());
    assertEquals(sheet.getCellAt(new Coord(2, 2)), new Cell());
    assertEquals(sheet.getCellAt(new Coord(2, 3)), new Cell());
    assertEquals(sheet.getCellAt(new Coord(2, 4)), new Cell());
    assertEquals(sheet.getCellAt(new Coord(3, 1)), new Cell());
    assertEquals(sheet.getCellAt(new Coord(3, 2)), new Cell());
    assertEquals(sheet.getCellAt(new Coord(3, 3)), new Cell());
    assertEquals(sheet.getCellAt(new Coord(3, 4)), new Cell());
  }

  @Test
  public void testBuilder5() { // check cyclic INDIRECT
    // a spreadsheet can be build with cyclic data, but cells with cycles will be displayed as
    // an error
    initBuilder();
    this.builder.createCell(1, 1, "2");
    this.builder.createCell(1, 2, "A1");
    this.builder.createCell(1, 1, "A2");

    BasicSpreadsheet sheet = this.builder.createWorksheet();

    Coord a2 = new Coord(1, 2);

    assertEquals(sheet.getCellAt(new Coord(1, 1)),
            new Cell(new Reference(a2, a2, sheet.getSheet())));
  }

  @Test
  public void testBuilder6() { // check cyclic DIRECT
    // a spreadsheet can be build with cyclic data, but cells with cycles will be displayed as
    // an error
    initBuilder();
    this.builder.createCell(1, 1, "A1");

    BasicSpreadsheet sheet = this.builder.createWorksheet();

    Coord a1 = new Coord(1, 1);

    System.out.print(sheet.getCellAt(a1).getContents().toString());

    assertEquals(sheet.getCellAt(a1), new Cell(new Reference(a1, a1, sheet.getSheet())));
  }

  @Test
  public void testBuilder7() { // create BLANK, NUMBER, BOOLEAN, STRING, FORMULA
    initBuilder();
    this.builder.createCell(1, 1, "4");
    this.builder.createCell(1, 2, "3");
    this.builder.createCell(1, 3, "true");
    this.builder.createCell(1, 4, "\"heyo\"");
    this.builder.createCell(1, 5, "(SUM 2 100)");

    BasicSpreadsheet sheet = this.builder.createWorksheet();

    sheet.deleteCell(new Coord(1,1));

    assertEquals(sheet.getCellAt(new Coord(1, 1)), new Cell());
    assertEquals(sheet.getCellAt(new Coord(1, 2)), new Cell(new DoubleVal(3)));
    assertEquals(sheet.getCellAt(new Coord(1, 3)), new Cell(new BooleanVal(true)));
    assertEquals(sheet.getCellAt(new Coord(1, 4)), new Cell(new StringVal("heyo")));
    assertEquals(sheet.getCellAt(new Coord(1, 5)),
            new Cell(new SumFunc(new DoubleVal(2), new DoubleVal(100))));
  }

  @Test
  public void testBuilder8() { // test less than
    initBuilder();
    this.builder.createCell(1, 1, "(< 4 3)");
    this.builder.createCell(1, 2, "(< -100 3)");

    BasicSpreadsheet sheet = this.builder.createWorksheet();

    assertEquals(sheet.getCellAt(new Coord(1, 1)),
            new Cell(new LessThan(new IFormula[] {new DoubleVal(4), new DoubleVal(3)})));
    assertEquals(sheet.getCellAt(new Coord(1, 2)),
            new Cell(new LessThan(new IFormula[] {new DoubleVal(-100), new DoubleVal(3)})));
  }

  @Test
  public void testBuilder9() { // test less than
    initBuilder();
    this.builder.createCell(1, 1, "8");
    this.builder.createCell(1, 2, "(SUM A1 A1)");

    BasicSpreadsheet sheet = this.builder.createWorksheet();

    HashMap<Coord, Cell> mysheet = new HashMap<Coord, Cell>();
    mysheet.put(new Coord(1, 1), new Cell(new DoubleVal(8)));

    assertEquals(sheet.getCellAt(new Coord(1, 2)),
            new Cell(new SumFunc(
                    new Reference(
                            new Coord(1, 1),
                            new Coord(1, 1),
                            mysheet),
                    new Reference(
                            new Coord(1, 1),
                            new Coord(1, 1),
                            mysheet))));
  }

  @Test
  public void testBuilder10() { // incorrect type
    initBuilder();
    this.builder.createCell(1, 1, "8");
    this.builder.createCell(1, 2, "(SUM true A1)");

    BasicSpreadsheet sheet = this.builder.createWorksheet();

    HashMap<Coord, Cell> mysheet = new HashMap<Coord, Cell>();
    mysheet.put(new Coord(1, 1), new Cell(new DoubleVal(8)));

    assertEquals(sheet.getCellAt(new Coord(1, 2)),
            new Cell(new SumFunc(
                    new BooleanVal(true),
                    new Reference(
                            new Coord(1, 1),
                            new Coord(1, 1),
                            mysheet))));
  }


  @Test
  public void testGetCellAtNotExist() {
    this.init();
    assertEquals(myBasic.getCellAt(new Coord(1000001, 1000001)), new Cell());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetCellAtNullArg() {
    this.init();
    myBasic.getCellAt(null);
  }

  @Test
  public void testGetCellAtExists() {
    this.init();
    Cell result = myBasic.getCellAt(new Coord(1, 1));
    assertEquals(result, this.firstCell);
  }

  @Test
  public void testGetCellAtBlank() {
    this.init();
    Cell result = myBasic.getCellAt(new Coord(1, 2));
    assertEquals(result, new Cell());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDeleteCellNullArg() {
    this.init();
    myBasic.deleteCell(null);
  }


  @Test
  public void testDeleteCellValid() {
    this.init();
    Cell result = myBasic.getCellAt(new Coord(1, 1));
    assertEquals(result, this.firstCell);
    myBasic.deleteCell(new Coord(1, 1));
    assertEquals(myBasic.getCellAt(new Coord(1, 1)), new Cell());
  }

  @Test
  public void updateCellValidDouble() throws Cell.CyclicDataException {
    this.init();
    DoubleVal newCont = new DoubleVal(60);
    Cell result = myBasic.getCellAt(new Coord(1, 1));
    assertEquals(result, this.firstCell);
    myBasic.updateCell(new Coord(1, 1), newCont);
    assertEquals(new Cell(newCont), myBasic.getCellAt(new Coord(1, 1)));
  }

  @Test
  public void updateCellValidString() throws Cell.CyclicDataException {
    this.init();
    StringVal newCont = new StringVal("hi");
    Cell result = myBasic.getCellAt(new Coord(1, 1));
    assertEquals(result, this.firstCell);
    myBasic.updateCell(new Coord(1, 1), newCont);
    assertEquals(new Cell(newCont), myBasic.getCellAt(new Coord(1, 1)));
  }

  @Test
  public void updateCellValidBoolean() {
    this.init();
    BooleanVal newCont = new BooleanVal(true);
    Cell result = myBasic.getCellAt(new Coord(1, 1));
    assertEquals(result, this.firstCell);
    myBasic.updateCell(new Coord(1, 1), newCont);
    assertEquals(new Cell(newCont), myBasic.getCellAt(new Coord(1, 1)));
  }

  @Test
  public void updateCellValidRef() {
    this.init();
    HashMap<Coord, Cell> myMap = new HashMap<Coord, Cell>();
    Reference newCont = new Reference(new Coord(2, 2),new Coord(2, 2), myMap);
    Cell result = myBasic.getCellAt(new Coord(1, 1));
    assertEquals(result, this.firstCell);
    myBasic.updateCell(new Coord(1, 1), newCont);
    assertEquals(new Cell(newCont), myBasic.getCellAt(new Coord(1, 1)));
  }

  @Test
  public void updateCellValidFunc() {
    this.init();
    HashMap<Coord, Cell> myMap = new HashMap<Coord, Cell>();
    SumFunc newCont = new SumFunc(new IFormula[] {});
    Cell result = myBasic.getCellAt(new Coord(1, 1));
    assertEquals(result, this.firstCell);
    myBasic.updateCell(new Coord(1, 1), newCont);
    assertEquals(new Cell(newCont), myBasic.getCellAt(new Coord(1, 1)));
  }

  @Test(expected = IllegalArgumentException.class)
  public void updateCellNull() {
    this.init();
    DoubleVal newCont = new DoubleVal(60);
    myBasic.updateCell(null, newCont);
  }
}
