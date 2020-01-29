import org.junit.Test;


import edu.cs3500.spreadsheets.controller.Controller;
import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.ISpreadsheetModel;
import edu.cs3500.spreadsheets.model.MockSpreadsheetModel;
import edu.cs3500.spreadsheets.view.ISpreadsheetView;
import edu.cs3500.spreadsheets.view.MockSpreadsheetView;

import static junit.framework.TestCase.assertEquals;

/**
 * Tests the methods in Controller.
 */
public class TestController {
  //private ISpreadsheetController c;

  void initController(StringBuilder log1, StringBuilder log2) {
    ISpreadsheetModel model = new MockSpreadsheetModel(log1);
    ISpreadsheetView view = new MockSpreadsheetView(log2);

    //this.c = new Controller(model, view);
  }

  @Test
  public void testCTOM1() { // update cell without selecting

    StringBuilder log1 = new StringBuilder();
    StringBuilder log2 = new StringBuilder();

    ISpreadsheetModel model = new MockSpreadsheetModel(log1);
    ISpreadsheetView view = new MockSpreadsheetView(log2);

    Controller c = new Controller(model, view);

    c.update("Hello, World");

    // check view
    assertEquals(log2.toString(), "Add features\n" +
            "Show error: You have not selected a cell yet!\n");

    // check model
    assertEquals(log1.toString(), "");
  }

  @Test
  public void testCTOM2() { // select cell, update with badly inputted arguments

    StringBuilder log1 = new StringBuilder();
    StringBuilder log2 = new StringBuilder();

    ISpreadsheetModel model = new MockSpreadsheetModel(log1);
    ISpreadsheetView view = new MockSpreadsheetView(log2);

    Controller c = new Controller(model, view);

    c.selectCell(new Coord(1, 1));
    c.update("Hello, World");

    // view
    assertEquals(log2.toString(), "Add features\n" +
            "Select cell: A1\n" +
            "Show error: You inputted a badly formatted formula. For a string, please surround it" +
            " in quotes. For a formula, please do not include an = and surround in parentheses.\n" +
            "Render\n");

    // model
    assertEquals(log1.toString(), "");
  }

  @Test
  public void testCTOM3() { // select cell, update with correctly inputted arguments

    StringBuilder log1 = new StringBuilder();
    StringBuilder log2 = new StringBuilder();

    ISpreadsheetModel model = new MockSpreadsheetModel(log1);
    ISpreadsheetView view = new MockSpreadsheetView(log2);

    Controller c = new Controller(model, view);

    c.selectCell(new Coord(1, 1));
    c.update("\"Hello, World\"");

    // check view
    assertEquals(log2.toString(), "Add features\n" +
            "Select cell: A1\n" +
            "Render\n");

    // check model
    assertEquals(log1.toString(), "Get sheet\n" +
            "Update cell: A1. " +
            "Contents: Hello, World\n");
  }

  @Test
  public void testCTOM4() { // select cell, but reject edit

    StringBuilder log1 = new StringBuilder();
    StringBuilder log2 = new StringBuilder();

    ISpreadsheetModel model = new MockSpreadsheetModel(log1);
    ISpreadsheetView view = new MockSpreadsheetView(log2);

    Controller c = new Controller(model, view);

    c.selectCell(new Coord(1, 1));
    c.reject();

    // check view
    assertEquals(log2.toString(), "Add features\n" +
            "Select cell: A1\n" +
            "Select cell: A1\n");

    // check model
    assertEquals(log1.toString(), "");
  }

  @Test
  public void testCTOM5() { // select 5 different cells, but reject edit

    StringBuilder log1 = new StringBuilder();
    StringBuilder log2 = new StringBuilder();

    ISpreadsheetModel model = new MockSpreadsheetModel(log1);
    ISpreadsheetView view = new MockSpreadsheetView(log2);

    Controller c = new Controller(model, view);

    c.selectCell(new Coord(1, 1));
    c.selectCell(new Coord(1, 2));
    c.selectCell(new Coord(1, 3));
    c.selectCell(new Coord(1, 4));
    c.selectCell(new Coord(1, 5));
    c.reject();

    // check view
    assertEquals(log2.toString(), "Add features\n" +
            "Select cell: A1\n" +
            "Select cell: A2\n" +
            "Select cell: A3\n" +
            "Select cell: A4\n" +
            "Select cell: A5\n" +
            "Select cell: A5\n");

    // check model
    assertEquals(log1.toString(), "");
  }

  @Test
  public void testCTOM6() { // select 5 different cells, update final selected cell with contents

    StringBuilder log1 = new StringBuilder();
    StringBuilder log2 = new StringBuilder();

    ISpreadsheetModel model = new MockSpreadsheetModel(log1);
    ISpreadsheetView view = new MockSpreadsheetView(log2);

    Controller c = new Controller(model, view);

    c.selectCell(new Coord(1, 1));
    c.selectCell(new Coord(1, 2));
    c.selectCell(new Coord(1, 3));
    c.selectCell(new Coord(1, 4));
    c.selectCell(new Coord(1, 5));
    c.update("5");

    // check view
    assertEquals(log2.toString(), "Add features\n" +
            "Select cell: A1\n" +
            "Select cell: A2\n" +
            "Select cell: A3\n" +
            "Select cell: A4\n" +
            "Select cell: A5\n" +
            "Render\n");

    // check model
    assertEquals(log1.toString(), "Get sheet\n" +
            "Update cell: A5. Contents: 5.000000\n");
  }

  @Test
  public void testCTOM7() { // select 5 different cells, update each cell with contents

    StringBuilder log1 = new StringBuilder();
    StringBuilder log2 = new StringBuilder();

    ISpreadsheetModel model = new MockSpreadsheetModel(log1);
    ISpreadsheetView view = new MockSpreadsheetView(log2);

    Controller c = new Controller(model, view);

    c.selectCell(new Coord(1, 1));
    c.update("12910");
    c.selectCell(new Coord(1, 2));
    c.update("true");
    c.selectCell(new Coord(1, 3));
    c.update("false");
    c.selectCell(new Coord(1, 4));
    c.update("\"string\"");
    c.selectCell(new Coord(1, 5));
    c.update("(SUM 7 1)");

    // check view
    assertEquals(log2.toString(), "Add features\n" +
            "Select cell: A1\n" +
            "Render\n" +
            "Select cell: A2\n" +
            "Render\n" +
            "Select cell: A3\n" +
            "Render\n" +
            "Select cell: A4\n" +
            "Render\n" +
            "Select cell: A5\n" +
            "Render\n");

    // check model
    assertEquals(log1.toString(), "Get sheet\n" +
            "Update cell: A1. Contents: 12910.000000\n" +
            "Get sheet\n" +
            "Update cell: A2. Contents: true\n" +
            "Get sheet\n" +
            "Update cell: A3. Contents: false\n" +
            "Get sheet\n" +
            "Update cell: A4. Contents: string\n" +
            "Get sheet\n" +
            "Update cell: A5. Contents: (SUM 7.000000 1.000000)\n");
  }

  @Test
  public void testCTOM8() { // Successfully render/select cells with valid/invalid contents

    StringBuilder log1 = new StringBuilder();
    StringBuilder log2 = new StringBuilder();

    ISpreadsheetModel model = new MockSpreadsheetModel(log1);
    ISpreadsheetView view = new MockSpreadsheetView(log2);

    Controller c = new Controller(model, view);

    c.selectCell(new Coord(1, 1));
    c.update("12910");
    c.selectCell(new Coord(1, 2));
    c.update("true");
    c.selectCell(new Coord(1, 3));
    c.update("false");
    c.selectCell(new Coord(1, 4));
    c.update("\"string\"");
    c.selectCell(new Coord(1, 5));
    c.update("(PRODUCT 7 1)");
    c.selectCell(new Coord(1, 6));
    c.update("(PRODUCT true 5)");
    c.selectCell(new Coord(1, 7));
    c.update("(PRODUCT false \"hello\")");
    c.selectCell(new Coord(1, 8));
    c.update("(CONCAT \"hello\" 5)");
    c.selectCell(new Coord(1, 9));
    c.update("(< 5 6)");
    c.selectCell(new Coord(1, 9));
    c.update("(< 6 5)");
    c.selectCell(new Coord(1, 9));
    c.update("(< true false)");


    // check view
    assertEquals(log2.toString(), "Add features\n" +
            "Select cell: A1\n" +
            "Render\n" +
            "Select cell: A2\n" +
            "Render\n" +
            "Select cell: A3\n" +
            "Render\n" +
            "Select cell: A4\n" +
            "Render\n" +
            "Select cell: A5\n" +
            "Render\n" +
            "Select cell: A6\n" +
            "Render\n" +
            "Select cell: A7\n" +
            "Render\n" +
            "Select cell: A8\n" +
            "Render\n" +
            "Select cell: A9\n" +
            "Render\n" +
            "Select cell: A9\n" +
            "Render\n" +
            "Select cell: A9\n" +
            "Render\n");

    // check model
    assertEquals(log1.toString(), "Get sheet\n" +
            "Update cell: A1. Contents: 12910.000000\n" +
            "Get sheet\n" +
            "Update cell: A2. Contents: true\n" +
            "Get sheet\n" +
            "Update cell: A3. Contents: false\n" +
            "Get sheet\n" +
            "Update cell: A4. Contents: string\n" +
            "Get sheet\n" +
            "Update cell: A5. Contents: (PROD 7.000000 1.000000)\n" +
            "Get sheet\n" +
            "Update cell: A6. Contents: (PROD true 5.000000)\n" +
            "Get sheet\n" +
            "Update cell: A7. Contents: (PROD false hello)\n" +
            "Get sheet\n" +
            "Update cell: A8. Contents: (CONCAT hello 5.000000)\n" +
            "Get sheet\n" +
            "Update cell: A9. Contents: (< 5.000000 6.000000)\n" +
            "Get sheet\n" +
            "Update cell: A9. Contents: (< 6.000000 5.000000)\n" +
            "Get sheet\n" +
            "Update cell: A9. Contents: (< true false)\n");
  }

  @Test
  public void testCTOM9() { // update a cell already filled with contents

    StringBuilder log1 = new StringBuilder();
    StringBuilder log2 = new StringBuilder();

    ISpreadsheetModel model = new MockSpreadsheetModel(log1);
    ISpreadsheetView view = new MockSpreadsheetView(log2);

    Controller c = new Controller(model, view);

    c.selectCell(new Coord(1, 1));
    c.update("12910");
    c.selectCell(new Coord(1, 1));
    c.update("true");

    // check view
    assertEquals(log2.toString(), "Add features\n" +
            "Select cell: A1\n" +
            "Render\n" +
            "Select cell: A1\n" +
            "Render\n");

    // check model
    assertEquals(log1.toString(), "Get sheet\n" +
            "Update cell: A1. Contents: 12910.000000\n" +
            "Get sheet\n" +
            "Update cell: A1. Contents: true\n");
  }

  @Test
  public void testCTOM10() { // update a cell already filled with contents

    StringBuilder log1 = new StringBuilder();
    StringBuilder log2 = new StringBuilder();

    ISpreadsheetModel model = new MockSpreadsheetModel(log1);
    ISpreadsheetView view = new MockSpreadsheetView(log2);

    Controller c = new Controller(model, view);

    c.selectCell(new Coord(1, 1));
    c.update("12910");
    c.selectCell(new Coord(1, 1));
    c.update("true");

    // check view
    assertEquals(log2.toString(), "Add features\n" +
            "Select cell: A1\n" +
            "Render\n" +
            "Select cell: A1\n" +
            "Render\n");

    // check model
    assertEquals(log1.toString(), "Get sheet\n" +
            "Update cell: A1. Contents: 12910.000000\n" +
            "Get sheet\n" +
            "Update cell: A1. Contents: true\n");
  }

  @Test
  public void testCTOM11() { // rejecting without updating

    StringBuilder log1 = new StringBuilder();
    StringBuilder log2 = new StringBuilder();

    ISpreadsheetModel model = new MockSpreadsheetModel(log1);
    ISpreadsheetView view = new MockSpreadsheetView(log2);

    Controller c = new Controller(model, view);

    c.selectCell(new Coord(1, 1));
    c.reject();

    // check view
    assertEquals(log2.toString(), "Add features\n" +
            "Select cell: A1\n" +
            "Select cell: A1\n");

    // check model
    assertEquals(log1.toString(), "");
  }

  @Test
  public void testCTOM12() { // select cells at a very large location.

    StringBuilder log1 = new StringBuilder();
    StringBuilder log2 = new StringBuilder();

    ISpreadsheetModel model = new MockSpreadsheetModel(log1);
    ISpreadsheetView view = new MockSpreadsheetView(log2);

    Controller c = new Controller(model, view);

    c.selectCell(new Coord(1, 1));
    c.selectCell(new Coord(10, 11));
    c.selectCell(new Coord(1000000, 100000));

    // check view
    assertEquals(log2.toString(), "Add features\n" +
            "Select cell: A1\n" +
            "Select cell: J11\n" +
            "Select cell: BDWGN100000\n");

    // check model
    assertEquals(log1.toString(), "");
  }

  @Test
  public void testCTOM13() { // updating cells at a very large location.

    StringBuilder log1 = new StringBuilder();
    StringBuilder log2 = new StringBuilder();

    ISpreadsheetModel model = new MockSpreadsheetModel(log1);
    ISpreadsheetView view = new MockSpreadsheetView(log2);

    Controller c = new Controller(model, view);

    c.selectCell(new Coord(1, 1));
    c.update("55");
    c.selectCell(new Coord(10, 11));
    c.update("66");
    c.selectCell(new Coord(1000000, 100000));
    c.update("77");

    // check view
    assertEquals(log2.toString(), "Add features\n" +
            "Select cell: A1\n" +
            "Render\n" +
            "Select cell: J11\n" +
            "Render\n" +
            "Select cell: BDWGN100000\n" +
            "Render\n");

    // check model
    assertEquals(log1.toString(), "Get sheet\n" +
            "Update cell: A1. Contents: 55.000000\n" +
            "Get sheet\n" +
            "Update cell: J11. Contents: 66.000000\n" +
            "Get sheet\n" +
            "Update cell: BDWGN100000. Contents: 77.000000\n");
  }

  @Test
  public void testCTOM14() { // doing nothing with controller should only add features.

    StringBuilder log1 = new StringBuilder();
    StringBuilder log2 = new StringBuilder();

    ISpreadsheetModel model = new MockSpreadsheetModel(log1);
    ISpreadsheetView view = new MockSpreadsheetView(log2);

    Controller c = new Controller(model, view);

    // check view
    assertEquals(log2.toString(), "Add features\n");

    // check model
    assertEquals(log1.toString(), "");
  }

  @Test
  public void testCTOM15() { // test bad formula composition

    StringBuilder log1 = new StringBuilder();
    StringBuilder log2 = new StringBuilder();

    ISpreadsheetModel model = new MockSpreadsheetModel(log1);
    ISpreadsheetView view = new MockSpreadsheetView(log2);

    Controller c = new Controller(model, view);

    c.selectCell(new Coord(1, 1));
    c.update("SUM 5 4");

    // view
    assertEquals(log2.toString(), "Add features\n" +
            "Select cell: A1\n" +
            "Show error: You inputted a badly formatted formula. For a string, please " +
            "surround it in quotes. For a formula, please surround in parentheses.\n" +
            "Render\n");

    // model
    assertEquals(log1.toString(), "");
  }

  @Test
  public void testCTOM17() { // test half paren

    StringBuilder log1 = new StringBuilder();
    StringBuilder log2 = new StringBuilder();

    ISpreadsheetModel model = new MockSpreadsheetModel(log1);
    ISpreadsheetView view = new MockSpreadsheetView(log2);

    Controller c = new Controller(model, view);

    c.selectCell(new Coord(1, 1));
    c.update("(SUM 5 4");

    // view
    assertEquals(log2.toString(), "Add features\n" +
            "Select cell: A1\n" +
            "Show error: You inputted a badly formatted formula. For a string, please surround " +
            "it in quotes. For a formula, please surround in parentheses.\n" +
            "Render\n");

    // model
    assertEquals(log1.toString(), "");
  }

}