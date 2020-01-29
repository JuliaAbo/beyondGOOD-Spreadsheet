import org.junit.Test;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import edu.cs3500.spreadsheets.model.BasicSpreadsheet;
import edu.cs3500.spreadsheets.model.ISpreadsheetModel;
import edu.cs3500.spreadsheets.model.WorksheetReader;
import edu.cs3500.spreadsheets.view.ISpreadsheetView;
import edu.cs3500.spreadsheets.view.TextualSpreadsheetView;

import static junit.framework.TestCase.assertEquals;

/**
 * Tests the methods in TextualSpreadsheetView.
 */
public class TestTextualSpreadsheetView {
  private ISpreadsheetView v;
  private WorksheetReader reader;
  private BasicSpreadsheet.BasicSpreadsheetBuilder builder;

  /**
   * Initializes the textual view with the given model and appendable.
   *
   * @param m  model
   * @param ap appendable
   */
  private void initView(ISpreadsheetModel m, Appendable ap) {
    this.v = new TextualSpreadsheetView(m, ap);
  }

  /**
   * Initializes the spreadsheet reader and builder.
   */
  private void initBuilder() {
    this.reader = new WorksheetReader();
    this.builder = new BasicSpreadsheet.BasicSpreadsheetBuilder();
  }

  // round trip test: read in model, render through view, read view's output into second model
  // check equivalency of non-empty sheet
  // sheet contains:
  // -
  @Test
  public void testTV1() {
    initBuilder();

    try {
      ISpreadsheetModel model1 = reader.read(builder,
              new FileReader("./resources/Input1.txt"));

      PrintWriter pw1 = new PrintWriter(new File("./input1test.txt"));
      initView(model1, pw1);

      this.v.render();

      // write file
      pw1.println();

      ISpreadsheetModel model2 = reader.read(builder,
              new FileReader("./input1test.txt"));

      assertEquals(model1.getSheet(), model2.getSheet());
    } catch (IOException e) {
      // Do nothing if IOException.
    }
  }

  // round trip test: read in model, render through view, read view's output into second model
  // check equivalency of non-empty sheet
  // sheet contains:
  // -
  @Test
  public void testInput2() {
    initBuilder();

    try {
      ISpreadsheetModel model1 = reader.read(builder,
              new FileReader("./resources/Input2.txt"));

      PrintWriter pw1 = new PrintWriter(new File("./input2test.txt"));
      initView(model1, pw1);

      this.v.render();

      // write file
      pw1.println();

      ISpreadsheetModel model2 = reader.read(builder,
              new FileReader("./input2test.txt"));

      assertEquals(model1.getSheet(), model2.getSheet());
    } catch (IOException e) {
      // Do nothing if IOException.
    }
  }

  // round trip test: read in model, render through view, read view's output into second model
  // check equivalency of non-empty sheet
  // sheet contains:
  // -
  @Test
  public void testInput3() {
    initBuilder();

    try {
      ISpreadsheetModel model1 = reader.read(builder,
              new FileReader("./resources/Input3.txt"));

      PrintWriter pw1 = new PrintWriter(new File("./input3test.txt"));
      initView(model1, pw1);

      this.v.render();

      // write file
      pw1.println();

      ISpreadsheetModel model2 = reader.read(builder,
              new FileReader("./input3test.txt"));

      assertEquals(model1.getSheet(), model2.getSheet());
    } catch (IOException e) {
      // Do nothing if IOException.
    }
  }

  // round trip test: read in model, render through view, read view's output into second model
  // check equivalency of empty sheet
  @Test
  public void testTV2() {
    initBuilder();

    try {
      ISpreadsheetModel model1 = builder.createWorksheet(); // empty sheet

      PrintWriter pw1 = new PrintWriter(new File("./blanktest.txt"));
      initView(model1, pw1);

      this.v.render();

      // write file
      pw1.println();

      ISpreadsheetModel model2 = reader.read(builder,
              new FileReader("./blanktest.txt"));

      assertEquals(model1.getSheet(), model2.getSheet());
    } catch (IOException e) {
      // Do nothing if IOException.
    }
  }


  // pass string buffer into view
  // check validity of mutated string buffer
  @Test
  public void testInput2Buff() {
    initBuilder();

    try {
      ISpreadsheetModel model1 = reader.read(builder,
              new FileReader("./resources/Input2.txt"));

      StringBuffer s = new StringBuffer();
      initView(model1, s);

      this.v.render();

      assertEquals(s.toString(), "A1 50000000.000000\n" +
              "B1 50000000.000000\n" +
              "C1 false\n" +
              "D1 Hi\n" +
              "A2 2.500000\n" +
              "B2 #REF!\n" +
              "C2 false\n" +
              "A3 0.000050\n" +
              "C3 true\n" +
              "D3 n %d ! && ! #4\n" +
              "C4 5.000000\n" +
              "D4 CAPS\n" +
              "A5 -1.000000\n" +
              "C5 0.000000\n" +
              "D5 lowercase\n" +
              "A6 -7400.000000\n" +
              "C6 46250000.000000\n" +
              "A7 String\n" +
              "B7 -7395.999950\n" +
              "C7 0.000000\n" +
              "D7 0.150500\n" +
              "A8 \n" +
              "B8 7.500000\n" +
              "C8 -7.500000\n" +
              "A9 Stringstringstringstring\n" +
              "B9 1.500000\n" +
              "C9 6.500000\n" +
              "A10 true\n" +
              "B10 -2.500000\n" +
              "C10 #VALUE!\n" +
              "A11 false\n" +
              "B11 \n" +
              "C11 \n" +
              "A12 What's Up\n" +
              "B12 \n" +
              "C12 #VALUE!\n" +
              "B13 \n" +
              "B14 What's Up\n" +
              "B15 What's Up\n" +
              "B16 What's UpString\n" +
              "B17 StringWhat's Up\n");
    } catch (IOException e) {
      // Do nothing if IOException.
    }
  }

  // pass string buffer into view
  // check validity of mutated string buffer
  @Test
  public void testInput3Buff() {
    initBuilder();

    try {
      ISpreadsheetModel model1 = reader.read(builder,
              new FileReader("./resources/Input3.txt"));

      StringBuffer s = new StringBuffer();
      initView(model1, s);

      this.v.render();

      assertEquals(s.toString(), "B1 1.000000\n" +
              "C1 false\n" +
              "D1 1.000000\n" +
              "E1 Hello ,World!\n" +
              "F1 0.000000\n" +
              "G1 false\n" +
              "A2 #VALUE!\n" +
              "B2 2.000000\n" +
              "C2 true\n" +
              "D2 true\n" +
              "E2 World!, Hello\n" +
              "F2 1.000000\n" +
              "G2 false\n" +
              "A3 Hello\n" +
              "B3 3.000000\n" +
              "D3 #REF!\n" +
              "E3 Hello \n" +
              "F3 1.000000\n" +
              "G3 false\n" +
              "A4  \n" +
              "B4 4.000000\n" +
              "D4 #REF!\n" +
              "E4 ,World!\n" +
              "F4 0.000000\n" +
              "A5 ,\n" +
              "B5 5.000000\n" +
              "E5 \n" +
              "F5 0.000000\n" +
              "A6 World!\n" +
              "B6 100.000000\n" +
              "D6 Hello\n" +
              "E6 Hello\n" +
              "F6 3.000000\n" +
              "E7 Hello\n" +
              "F7 15.000000\n" +
              "E8 \n" +
              "F8 15.000000\n" +
              "E9 0.000000\n" +
              "F9 105.000000\n" +
              "E10 0.000000\n" +
              "F10 #VALUE!\n" +
              "E11 1.000000\n" +
              "F11 1.000000\n" +
              "E12 6.000000\n" +
              "E13 60.000000\n" +
              "E14 360.000000\n");
    } catch (IOException e) {
      // Do nothing if IOException.
    }
  }

  // pass string buffer into view
  // check validity of mutated string buffer
  @Test
  public void testInput1Buff() {
    initBuilder();

    try {
      ISpreadsheetModel model1 = reader.read(builder,
              new FileReader("./resources/Input1.txt"));

      StringBuffer s = new StringBuffer();
      initView(model1, s);

      this.v.render();

      assertEquals(s.toString(), "A1 5.000000\n" +
              "B1 #REF!\n" +
              "C1 false\n" +
              "D1 Hello!\n" +
              "E1 #VALUE!\n" +
              "A2 5.000000\n" +
              "B2 10.000000\n" +
              "C2 false\n" +
              "D2 Hello!\n" +
              "A3 -5.000000\n" +
              "B3 10.000000\n" +
              "C3 true\n" +
              "D3 30.000000\n" +
              "A4 true\n" +
              "B4 10.000000\n" +
              "C4 false\n" +
              "A5 false\n" +
              "B5 25.000000\n" +
              "A6 Hello!\n" +
              "B6 25.000000\n" +
              "A7 \n" +
              "B7 25.000000\n" +
              "A8 0.000000\n" +
              "B8 0.000000\n" +
              "C8 Hello!\n" +
              "A9 -5.000000\n" +
              "B9 0.000000\n" +
              "C9 Hello!Goodbye\n" +
              "A10 Goodbye\n" +
              "B10 150.000000\n" +
              "ZZ100 10.000000\n");
    } catch (IOException e) {
      // Do nothing if IOException.
    }
  }

  // pass string buffer into view
  // check validity of mutated string buffer for EMPTY SHEET
  @Test
  public void testTV4() {
    initBuilder();

    try {
      ISpreadsheetModel model1 = builder.createWorksheet(); // empty sheet

      StringBuffer s = new StringBuffer();
      initView(model1, s);

      this.v.render();

      assertEquals(s.toString(), "");
    } catch (IOException e) {
      // Do nothing if IOException.
    }
  }
}
