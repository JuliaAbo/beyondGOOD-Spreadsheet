import org.junit.Test;

import java.util.HashMap;

import edu.cs3500.spreadsheets.model.BasicSpreadsheet;
import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.contents.BooleanVal;
import edu.cs3500.spreadsheets.model.contents.ColumnReference;
import edu.cs3500.spreadsheets.model.contents.ConcatFunc;
import edu.cs3500.spreadsheets.model.contents.DoubleVal;
import edu.cs3500.spreadsheets.model.contents.IVal;
import edu.cs3500.spreadsheets.model.contents.ProdFunc;
import edu.cs3500.spreadsheets.model.contents.StringVal;
import edu.cs3500.spreadsheets.model.contents.SumFunc;

import static junit.framework.TestCase.assertEquals;

/**
 * A class to test references of whole columns.
 */
public class TestColumnRef {
  private BasicSpreadsheet builder;

  private void initBuilder() {
    this.builder = new BasicSpreadsheet.BasicSpreadsheetBuilder().createWorksheet();
  }

  private void init() {
    BooleanVal firstCell = (new BooleanVal(false));
    StringVal secondCell = new StringVal("hi");
    DoubleVal thirdCell = new DoubleVal(5.0);
    this.builder.updateCell(new Coord(1, 1), firstCell);
    this.builder.updateCell(new Coord(1, 2), secondCell);
    this.builder.updateCell(new Coord(1, 3), thirdCell);
  }

  // Tests that this reference's coordinates get updated as they are added.
  @Test
  public void testSelectFilled() {
    Coord testCoord = new Coord(50, 50);
    this.initBuilder();
    ColumnReference firstCol = new ColumnReference(1, 1, builder.getSheet());
    assertEquals(0,firstCol.getCoords(testCoord).size());
    this.init();
    assertEquals(3,firstCol.getCoords(testCoord).size());
  }

  // Tests that this reference's coordinates get deleted when they are deleted.
  @Test
  public void testDeleteUpdates() {
    Coord testCoord = new Coord(50, 50);
    this.initBuilder();
    ColumnReference firstCol = new ColumnReference(1, 1, builder.getSheet());
    assertEquals(firstCol.getCoords(testCoord).size(),0);
    this.init();
    assertEquals(3,firstCol.getCoords(testCoord).size());
    builder.deleteCell(new Coord(1,1));
    firstCol.updateColumnRef();
    assertEquals(2,firstCol.getCoords(testCoord).size());
  }

  // Tests that you can sum the entirety of a column even with other non summing content

  @Test
  public void testWithSum() {
    this.initBuilder();
    ColumnReference firstCol = new ColumnReference(1, 1, builder.getSheet());
    assertEquals(firstCol.getCoords(new Coord(1,2)).size(),0);
    this.builder.updateCell(new Coord(1, 4), new DoubleVal(2.0));
    this.init();
    SumFunc firstSum = new SumFunc(firstCol);
    assertEquals(new DoubleVal(7.0),firstSum.evaluate(new HashMap<Coord, IVal>()));
  }

  // Tests that you can sum the entirety of two different columns, even with different types.
  @Test
  public void testWithSum2Col() {
    this.initBuilder();
    ColumnReference firstCol = new ColumnReference(1, 2, builder.getSheet());
    assertEquals(firstCol.getCoords(new Coord(1,2)).size(),0);
    this.builder.updateCell(new Coord(2, 4), new DoubleVal(2.0));
    this.init();
    SumFunc firstSum = new SumFunc(firstCol);
    assertEquals(new DoubleVal(7.0),firstSum.evaluate(new HashMap<Coord, IVal>()));
  }

  // Tests that you can multiply the entirety of two different columns together, even
  // when containing different types.
  @Test
  public void testWithProd() {
    this.initBuilder();
    ColumnReference firstCol = new ColumnReference(1, 2, builder.getSheet());
    assertEquals(firstCol.getCoords(new Coord(1,2)).size(),0);
    this.builder.updateCell(new Coord(2, 4), new DoubleVal(2.0));
    this.init();
    ProdFunc firstProd = new ProdFunc(firstCol);
    assertEquals(new DoubleVal(10.0),firstProd.evaluate(new HashMap<Coord, IVal>()));
  }

  // Tests that you can multiply the entirety of two different columns together, even
  // when containing different types.
  @Test
  public void testWithConcat() {
    this.initBuilder();
    ColumnReference firstCol = new ColumnReference(1, 2, builder.getSheet());
    assertEquals(firstCol.getCoords(new Coord(1,2)).size(),0);
    this.builder.updateCell(new Coord(2, 4), new StringVal("wow"));
    this.init();
    ConcatFunc firstProd = new ConcatFunc(firstCol);
    assertEquals(new StringVal("hiwow"),firstProd.evaluate(new HashMap<Coord, IVal>()));
  }

  // Tests that columns that are as of yet blank can still be accessed with proper content.
  @Test
  public void testUnAdded() {
    this.initBuilder();
    ColumnReference firstCol = new ColumnReference(1, 2, builder.getSheet());
    assertEquals(firstCol.getCoords(new Coord(1,2)).size(),0);
    ConcatFunc firstProd = new ConcatFunc(firstCol);
    assertEquals(new StringVal(""),firstProd.evaluate(new HashMap<Coord, IVal>()));
  }


}
