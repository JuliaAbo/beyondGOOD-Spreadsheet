package edu.cs3500.spreadsheets.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

import edu.cs3500.spreadsheets.model.AdapterSpreadsheet;
import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.ISpreadsheetModel;
import edu.cs3500.spreadsheets.model.contents.IFormula;
import edu.cs3500.spreadsheets.model.visitor.SexpToIFormula;
import edu.cs3500.spreadsheets.sexp.Parser;
import edu.cs3500.spreadsheets.sexp.Sexp;
import edu.cs3500.spreadsheets.view.ISpreadsheetView;
import edu.cs3500.spreadsheets.view.TextualSpreadsheetView;

/**
 * Represents the Controller of a Spreadsheet, which enables and mediates interaction between User
 * and Model.
 */
public class Controller implements Features, ISpreadsheetController {
  private ISpreadsheetModel model;
  protected ISpreadsheetView view;
  private Coord selected;

  /**
   * Constructs a controller.
   *
   * @param model the model of the spreadsheet
   * @param view  the view of the spreadsheet
   */
  public Controller(ISpreadsheetModel model, ISpreadsheetView view) {
    this.selected = null;
    this.model = model;
    this.view = view;
    this.view.addFeatures(this);
  }

  @Override
  public void open() {
    try {
      this.view.render();
    } catch (IOException e) {
      throw new IllegalArgumentException("Couldn't render");
    }
  }

  @Override
  public void update(String contents) {
    // if no cell has been selected
    if (this.selected == null) {
      this.view.showErrorMessage("You have not selected a cell yet!");
    }
    // update selected cell
    else {
      try {
        if (contents.length() > 0 && contents.charAt(0) == '=') {
          contents = contents.substring(1);
        }
        Sexp mySexp = new Parser().parse(contents);
        IFormula myFormula = new SexpToIFormula(this.model.getSheet()).apply(mySexp);
        this.model.updateCell(this.selected, myFormula);
      } catch (IllegalArgumentException e) {
        this.view.showErrorMessage("You inputted a badly formatted formula. For a string, "
                + "please surround it in quotes. For a formula, please "
                + "surround in parentheses.");
      }
      try {
        this.view.render();
      } catch (IOException t) {
        this.view.showErrorMessage("Could not display changes");
      }
    }
  }

  @Override
  public void selectCell(Coord c) {
    this.selected = c;
    this.view.select(this.selected);
  }

  @Override
  public void reject() {
    this.view.select(this.selected);
  }

  @Override
  public void save(String filepath) {
    try {
      String filename = filepath;
      PrintWriter newPw = new PrintWriter(new File(filename));
      ISpreadsheetView view2 = new TextualSpreadsheetView(
              new AdapterSpreadsheet(this.model), newPw);
      try {
        view2.render();
        newPw.println();
        newPw.flush();
        newPw.close();
      } catch (IOException e) {
        this.view.showErrorMessage("Rendering that file did not work.");
      }
    } catch (FileNotFoundException e) {
      this.view.showErrorMessage("That file could not be located.");
    }
  }

  @Override
  public void delete() {
    this.model.deleteCell(this.selected);
    try {
      this.view.render();
    } catch (IOException t) {
      this.view.showErrorMessage("Could not display changes");
    }
  }

  @Override
  public void shiftSelect(String dir) {
    switch (dir) {
      case "right":
        this.selectCell(new Coord(this.selected.col + 1, this.selected.row));
        break;
      case "left":
        if (this.selected.col > 1) {
          this.selectCell(new Coord(this.selected.col - 1, this.selected.row));
        }
        break;
      case "up":
        if (this.selected.row > 1) {
          this.selectCell(new Coord(this.selected.col, this.selected.row - 1));
        }
        break;
      case "down":
        this.selectCell(new Coord(this.selected.col, this.selected.row + 1));
        break;
      default:
        break;
    }
  }
}
