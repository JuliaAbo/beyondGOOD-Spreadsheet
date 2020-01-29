import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.contents.DoubleVal;
import edu.cs3500.spreadsheets.model.contents.IVal;
import edu.cs3500.spreadsheets.model.visitor.IFormulaVisitor;
import edu.cs3500.spreadsheets.model.visitor.IValVisitor;
import edu.cs3500.spreadsheets.model.visitor.DoubleVisitor;
import edu.cs3500.spreadsheets.model.visitor.EvalVisitor;
import edu.cs3500.spreadsheets.sexp.SNumber;

import static junit.framework.TestCase.assertEquals;

/**
 * Tests the methods in DoubleVal.
 */
public class TestDoubleVal {
  private DoubleVal a = new DoubleVal(5.0);
  private DoubleVal b = new DoubleVal(10.0);
  private DoubleVal c = new DoubleVal(15.0);
  private DoubleVal d = new DoubleVal(5.0);
  HashMap<Coord, IVal> myMap = new HashMap<Coord, IVal>();


  private IValVisitor<?> v1 = new DoubleVisitor(1.0);
  private IFormulaVisitor<?> v2 = new EvalVisitor(myMap);
  private IValVisitor<?> v3 = new DoubleVisitor(0.0);

  @Test
  public void testGetValue() {
    assertEquals(this.a.getValue(), new SNumber(5.0));
    assertEquals(this.b.getValue(), new SNumber(10.0));
    assertEquals(this.c.getValue(), new SNumber(15.0));
  }

  @Test
  public void testEvaluate() {
    assertEquals(this.a.evaluate(), new DoubleVal(5.0));
    assertEquals(this.b.evaluate(), new DoubleVal(10.0));
    assertEquals(this.c.evaluate(), new DoubleVal(15.0));
  }

  @Test
  public void testAccept() {
    assertEquals(this.a.accept(this.v1), 5.0);
    assertEquals(this.b.accept(this.v1), 10.0);

    assertEquals(this.a.accept(this.v3), 5.0);
    assertEquals(this.b.accept(this.v3), 10.0);

    ArrayList<IVal> result = new ArrayList<>();
    result.add(this.a);

    assertEquals(this.a.accept(this.v2), result);

    result.clear();
    result.add(this.b);

    assertEquals(this.b.accept(this.v2), result);
  }

  @Test
  public void testEquals() {
    assertEquals(this.a.equals(this.b), false);
    assertEquals(this.b.equals(this.a), false);

    assertEquals(this.a.equals(this.a), true);
    assertEquals(this.b.equals(this.b), true);

    assertEquals(this.a.equals(this.d), true);
  }

  @Test
  public void testHashCode() {
    assertEquals(this.a.hashCode(), 1075052544);
    assertEquals(this.b.hashCode(), 1076101120);
    assertEquals(this.c.hashCode(), 1076756480);
    assertEquals(this.a.hashCode(), this.d.hashCode());
  }

  @Test
  public void testToString() {
    assertEquals(this.a.toString(), "5.000000");
    assertEquals(this.b.toString(), "10.000000");
    assertEquals(this.c.toString(), "15.000000");
  }
}
