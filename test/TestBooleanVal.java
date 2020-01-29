import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.contents.BooleanVal;
import edu.cs3500.spreadsheets.model.contents.IVal;
import edu.cs3500.spreadsheets.model.visitor.IFormulaVisitor;
import edu.cs3500.spreadsheets.model.visitor.IValVisitor;
import edu.cs3500.spreadsheets.model.visitor.DoubleVisitor;
import edu.cs3500.spreadsheets.model.visitor.EvalVisitor;
import edu.cs3500.spreadsheets.sexp.SBoolean;

import static junit.framework.TestCase.assertEquals;


/**
 * Tests the methods in BooleanVal.
 */

public class TestBooleanVal {
  private BooleanVal f = new BooleanVal(false);
  private BooleanVal t = new BooleanVal(true);
  private BooleanVal t2 = new BooleanVal(true);
  HashMap<Coord, IVal> myMap = new HashMap<Coord, IVal>();

  private IValVisitor<?> v1 = new DoubleVisitor(1.0);
  private IFormulaVisitor<?> v2 = new EvalVisitor(myMap);
  private IValVisitor<?> v3 = new DoubleVisitor(0.0);

  @Test
  public void testGetValue() {
    assertEquals(this.f.getValue(), new SBoolean(false));
    assertEquals(this.t.getValue(), new SBoolean(true));
  }

  @Test
  public void testEvaluate() {
    assertEquals(this.f.evaluate(), new BooleanVal(false));
    assertEquals(this.t.evaluate(), new BooleanVal(true));
  }

  @Test
  public void testAccept() {
    assertEquals(this.t.accept(this.v1), 1.0);
    assertEquals(this.f.accept(this.v1), 1.0);

    assertEquals(this.t.accept(this.v3), 0.0);
    assertEquals(this.f.accept(this.v3), 0.0);

    ArrayList<IVal> result = new ArrayList<>();
    result.add(this.f);

    assertEquals(this.f.accept(this.v2), result);

    result.clear();
    result.add(this.t);

    assertEquals(this.t.accept(this.v2), result);
  }

  @Test
  public void testEquals() {
    assertEquals(this.f.equals(this.t), false);
    assertEquals(this.t.equals(this.f), false);

    assertEquals(this.f.equals(this.f), true);
    assertEquals(this.t.equals(this.t), true);

    assertEquals(this.t.equals(this.t2), true);
  }

  @Test
  public void testHashCode() {
    assertEquals(this.f.hashCode(), 1237);
    assertEquals(this.t.hashCode(), 1231);
    assertEquals(this.t2.hashCode(), 1231);
  }

  @Test
  public void testToString() {
    assertEquals(this.f.toString(), "false");
    assertEquals(this.t.toString(), "true");
    assertEquals(this.t2.toString(), "true");
  }
}
