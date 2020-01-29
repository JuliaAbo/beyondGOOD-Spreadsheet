import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.contents.IVal;
import edu.cs3500.spreadsheets.model.contents.StringVal;
import edu.cs3500.spreadsheets.model.visitor.IFormulaVisitor;
import edu.cs3500.spreadsheets.model.visitor.IValVisitor;
import edu.cs3500.spreadsheets.model.visitor.DoubleVisitor;
import edu.cs3500.spreadsheets.model.visitor.EvalVisitor;
import edu.cs3500.spreadsheets.sexp.SString;

import static junit.framework.TestCase.assertEquals;

/**
 * Tests the methods in StringVal.
 */

public class TestStringVal {
  private StringVal a = new StringVal("apple");
  private StringVal b = new StringVal("banana");
  private StringVal c = new StringVal("carrot");
  private StringVal a2 = new StringVal("apple");
  HashMap<Coord, IVal> myMap = new   HashMap<Coord, IVal>();

  private IValVisitor<?> v1 = new DoubleVisitor(1.0);
  private IFormulaVisitor<?> v2 = new EvalVisitor(myMap);
  private IValVisitor<?> v3 = new DoubleVisitor(0.0);

  @Test
  public void testGetValue() {
    assertEquals(this.a.getValue(), new SString("apple"));
    assertEquals(this.b.getValue(), new SString("banana"));
    assertEquals(this.c.getValue(), new SString("carrot"));
  }

  @Test
  public void testEvaluate() {
    assertEquals(this.a.evaluate(), new StringVal("apple"));
    assertEquals(this.b.evaluate(), new StringVal("banana"));
    assertEquals(this.c.evaluate(), new StringVal("carrot"));
  }

  @Test
  public void testAccept() {
    assertEquals(this.a.accept(this.v1), 1.0);
    assertEquals(this.b.accept(this.v1), 1.0);

    assertEquals(this.a.accept(this.v3), 0.0);
    assertEquals(this.b.accept(this.v3), 0.0);

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

    assertEquals(this.a.equals(this.a2), true);
  }

  @Test
  public void testHashCode() {
    assertEquals(this.a.hashCode(), 93029210);
    assertEquals(this.b.hashCode(), -1396355227);
    assertEquals(this.c.hashCode(), -1367590525);
    assertEquals(this.a.hashCode(), this.a2.hashCode());
  }

  @Test
  public void testToString() {
    assertEquals(this.a.toString(), "apple");
    assertEquals(this.b.toString(), "banana");
    assertEquals(this.c.toString(), "carrot");
    assertEquals(this.a2.toString(), "apple");
  }
}
