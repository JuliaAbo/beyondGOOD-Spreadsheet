package edu.cs3500.spreadsheets;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.cs3500.spreadsheets.model.AdapterSpreadsheet;
import edu.cs3500.spreadsheets.model.BasicSpreadsheet;
import edu.cs3500.spreadsheets.model.Cell;
import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.WorksheetReader;
import edu.cs3500.spreadsheets.model.contents.IVal;
import edu.cs3500.spreadsheets.controller.Controller;
import edu.cs3500.spreadsheets.view.EditableSpreadsheetView;
import edu.cs3500.spreadsheets.view.ISpreadsheetView;
import edu.cs3500.spreadsheets.view.TextualSpreadsheetView;
import edu.cs3500.spreadsheets.view.VisualSpreadsheetView;

/**
 * The main class for our program.
 */
public class BeyondGood {
  /**
   * The main entry point.
   * @param args any command-line arguments
   */
  public static void main(String[] args) {
    if (args == null) {
      throw new IllegalArgumentException("Must provide a filename and cell");
    } else if (args.length > 4 || args.length < 1) {
      throw new IllegalArgumentException("Must provide legal arguments");
    } else {

      // — opens your graphical view with a blank new spreadsheet
      if (args[0].equals("-gui")) {
        BasicSpreadsheet.BasicSpreadsheetBuilder myBuilder =
                new BasicSpreadsheet.BasicSpreadsheetBuilder();
        ISpreadsheetView view =
                new VisualSpreadsheetView(new AdapterSpreadsheet(myBuilder.createWorksheet()));
        try {
          view.render();
        } catch (IOException e) {
          System.out.print("failed to render");
        }
      }
      else if (args[0].equals("-edit")) {
        BasicSpreadsheet.BasicSpreadsheetBuilder myBuilder =
                new BasicSpreadsheet.BasicSpreadsheetBuilder();
        BasicSpreadsheet spreadsheet = myBuilder.createWorksheet();

        AdapterSpreadsheet visualView = new AdapterSpreadsheet(spreadsheet);

        ISpreadsheetView editView = new EditableSpreadsheetView(visualView);
        Controller myController = new Controller(spreadsheet, editView);
        myController.open();
        try {
          editView.render();
        } catch (IOException e) {
          System.out.print("failed to render");
        }
      }
      else {
        try {
          String myFilename = args[1];
          FileReader fr = new FileReader(myFilename);
          WorksheetReader myReader = new WorksheetReader();
          BasicSpreadsheet.BasicSpreadsheetBuilder myBuilder =
                  new BasicSpreadsheet.BasicSpreadsheetBuilder();
          BasicSpreadsheet bs = myReader.read(myBuilder, fr);

          // This is to save the evaluation to a given file using our Textual View.
          // -in some-filename -save some-new-filename
          if (args[2].equals("-save")) {
            String filename = args[3];
            PrintWriter newPw = new PrintWriter(new File((filename)));
            ISpreadsheetView view = new TextualSpreadsheetView(new AdapterSpreadsheet(bs), newPw);
            try {
              view.render();
              newPw.println();
              newPw.flush();
              newPw.close();
            } catch (IOException e) {
              System.out.print("failed to render");
            }
          }

          // This is to do the evaluation of a single cell that prints to a console. Passed in with
          // -in filename -eval coordinate
          else if (args[2].equals("-eval")) {
            String stringCoord = args[3];
            Coord toEvaluate = getCoord(stringCoord);
            try {
              IVal evaluated = bs.getCellAt(toEvaluate).evaluate(toEvaluate);
              System.out.print(evaluated.toString());
            } catch (Cell.RefException e) {
              System.out.print("Error in cell " + toEvaluate);
            } catch (Cell.CyclicDataException d) {
              System.out.print("Error in cell " + toEvaluate);
            } catch (IllegalArgumentException d) {
              System.out.print("Error in cell " + toEvaluate);
            }
          }

          // Opens the graphical view, loads the requested file, evaluates it
          //-in some-filename -gui
          else if (args[2].equals("-gui")) {
            String filename = args[1];
            ISpreadsheetView view = new VisualSpreadsheetView(new AdapterSpreadsheet(bs));
            try {
              view.render();
            } catch (IOException e) {
              System.out.println("Failed to render");
            }
          }

          // Opens the graphical view, loads the requested file, evaluates it
          //-in some-filename -gui
          else if (args[2].equals("-edit")) {
            String filename = args[1];
            EditableSpreadsheetView view = new EditableSpreadsheetView(new AdapterSpreadsheet(bs));
            Controller myController = new Controller(bs, view);
            myController.open();
            try {
              view.render();
            } catch (IOException e) {
              System.out.println("Unable to render");
            }
          }


        } catch (FileNotFoundException e) {
          e.printStackTrace();
        }
      }
    }
  }

  /**
   * Gets the coordinate from a string.
   * @param readable  the string to parse
   * @return  a new Coord of the column and row found in the given string.
   *          Throws IllegalStateException if there is no coordinate given.
   */
  public static Coord getCoord(String readable) {
    Scanner scan = new Scanner(readable);
    final Pattern cellRef = Pattern.compile("([A-Za-z]+)([1-9][0-9]*)");
    scan.useDelimiter("\\s+");
    int col = 1;
    int row = 1;
    while (scan.hasNext()) {
      while (scan.hasNext("#.*")) {
        scan.nextLine();
        scan.skip("\\s*");
      }
      String cell = scan.next();
      Matcher m = cellRef.matcher(cell);
      if (m.matches()) {
        col = Coord.colNameToIndex(m.group(1));
        row = Integer.parseInt(m.group(2));
      } else {
        throw new IllegalStateException("Expected cell ref");
      }
    }
    return new Coord(col, row);
  }
}

