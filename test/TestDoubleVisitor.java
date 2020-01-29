import org.junit.Test;

import edu.cs3500.spreadsheets.model.contents.BooleanVal;
import edu.cs3500.spreadsheets.model.contents.DoubleVal;
import edu.cs3500.spreadsheets.model.contents.StringVal;
import edu.cs3500.spreadsheets.model.visitor.IValVisitor;
import edu.cs3500.spreadsheets.model.visitor.DoubleVisitor;

import static org.junit.Assert.assertEquals;

/**
 * Tests the methods in DoubleVisitor.
 */
public class TestDoubleVisitor {
  private IValVisitor<?> v1 = new DoubleVisitor(0.0);
  private IValVisitor<?> v2 = new DoubleVisitor(1.0);

  private StringVal a = new StringVal("a");
  private DoubleVal b = new DoubleVal(3.0);
  private BooleanVal f = new BooleanVal(false);

  @Test
  public void testVisitStringVal() {
    assertEquals(this.v1.visitStringVal(this.a), 0.0);
    assertEquals(this.v2.visitStringVal(this.a), 1.0);
  }

  @Test
  public void testVisitBooleanVal() {
    assertEquals(this.v1.visitBooleanVal(this.f), 0.0);
    assertEquals(this.v2.visitBooleanVal(this.f), 1.0);
  }

  @Test
  public void testVisitDoubleVal() {
    assertEquals(this.v1.visitDoubleVal(this.b), 3.0);
    assertEquals(this.v2.visitDoubleVal(this.b), 3.0);
  }

  @Test
  public void testApply() {
    assertEquals(this.v1.apply(this.a), 0.0);
    assertEquals(this.v2.apply(this.b), 3.0);
    assertEquals(this.v1.apply(this.f), 0.0);
  }
}
