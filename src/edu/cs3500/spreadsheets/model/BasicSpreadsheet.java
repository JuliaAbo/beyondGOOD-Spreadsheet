package edu.cs3500.spreadsheets.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import edu.cs3500.spreadsheets.model.contents.IFormula;
import edu.cs3500.spreadsheets.model.contents.IVal;
import edu.cs3500.spreadsheets.model.visitor.RefNonDupVisitor;
import edu.cs3500.spreadsheets.sexp.Parser;
import edu.cs3500.spreadsheets.sexp.Sexp;

import edu.cs3500.spreadsheets.model.visitor.SexpToIFormula;


/**
 * An implementation of ISpreadSheetModel that represents a Basic Spreadsheet. Our representation of
 * cells is HashMap of Coord to Cells and Cells are a Cell class.
 */
public class BasicSpreadsheet implements ISpreadsheetModel {
  private final HashMap<Coord, Cell> mysheet;
  private final HashMap<Coord, IVal> computedValues;
  private final HashMap<Coord, List<Coord>> whatWePointTo;

  /**
   * A default constructor that initializes a BasicSpreadsheet.
   */
  private BasicSpreadsheet(BasicSpreadsheetBuilder builder) {
    this.mysheet = builder.mysheet;
    this.computedValues = builder.computedValues;
    this.whatWePointTo = builder.whatWePointTo;
  }

  /**
   * A class that represents a builder of a basic spreadsheet. Call individual methods to
   * customize.
   */
  public static class BasicSpreadsheetBuilder implements
          WorksheetReader.WorksheetBuilder<BasicSpreadsheet> {
    private final Parser p = new Parser();
    private int width;
    private int height;
    private HashMap<Coord, Cell> mysheet;
    private HashMap<Coord, IVal> computedValues;
    private HashMap<Coord, List<Coord>> whatWePointTo;

    /**
     * A default constructor.
     */
    public BasicSpreadsheetBuilder() {
      this.width = 25;
      this.height = 25;
      this.mysheet = new HashMap<Coord, Cell>();
      this.computedValues = new HashMap<Coord, IVal>();
      this.whatWePointTo = new HashMap<Coord, List<Coord>>();
    }

    /**
     * Builds a BasicSpreadsheetBuilder with it's current fields.
     *
     * @return BasicSpreadsheetBuilder
     */
    public BasicSpreadsheetBuilder build() {
      return this;
    }

    @Override
    public WorksheetReader.WorksheetBuilder<BasicSpreadsheet> createCell(int col, int row,
                                                                         String contents) {
      // parse contents to sexp
      Sexp s = this.p.parse(contents);

      // duplicate sheet
      //HashMap<Coord, Cell> duplicate = new HashMap<Coord, Cell>(mysheet);

      // convert Sexp to IFormula
      SexpToIFormula myVisitor = new SexpToIFormula(this.mysheet);
      IFormula c = s.accept(myVisitor);

      // desired coordinate
      Coord myCoord = new Coord(col, row);

      // retrieves set of coordinates this IFormula references
      RefNonDupVisitor myRefVisitor = new RefNonDupVisitor(myCoord);
      HashSet<Coord> referenced = myRefVisitor.apply(c);

      this.whatWePointTo.put(myCoord, new ArrayList<>(referenced));

      // creates the new cell
      Cell createdCell = new Cell(c, this.computedValues);

      // evaluates the current cell if possible. If not possible, null
      try {
        IVal evaluatedCell = createdCell.evaluate(myCoord);
        this.computedValues.put(myCoord, evaluatedCell);
      } catch (Cell.RefException | Cell.CyclicDataException e) {
        this.computedValues.put(myCoord, null);
      }

      // add new cell to spreadsheet
      this.mysheet.put(myCoord, createdCell);

      // update each cell that points to this cell
      for (Map.Entry<Coord, List<Coord>> entry : this.whatWePointTo.entrySet()) {
        if (entry.getValue().contains(myCoord)) {
          Coord toUpdate = entry.getKey();
          Cell updateCell = mysheet.get(toUpdate);

          try {
            this.computedValues.put(toUpdate, updateCell.evaluate(toUpdate));
          } catch (Cell.RefException | Cell.CyclicDataException e) {
            //Do nothing because no need to update.
          }
        }
      }
      return this;
    }

    @Override
    public BasicSpreadsheet createWorksheet() {
      return new BasicSpreadsheet(this);
    }
  }


  @Override
  public Cell getCellAt(Coord myCoord) throws IllegalArgumentException {
    if (myCoord == null) {
      throw new Cell.CyclicDataException();
    } else {
      return mysheet.getOrDefault(myCoord, new Cell());
    }
  }

  /**
   * Helper method to update a cell with the given.
   *
   * @param myCoord the coord to update
   * @param c       the IFormula to update with.
   */
  private void updateWithoutCycle(Coord myCoord, IFormula c) {

    RefNonDupVisitor myRefVisitor = new RefNonDupVisitor(myCoord);
    HashSet<Coord> referenced = myRefVisitor.apply(c);

    this.whatWePointTo.put(myCoord, new ArrayList<>(referenced));

    // creates the new cell
    Cell createdCell = this.getCellAt(myCoord);
    createdCell.updateCell(c);

    // evaluates the current cell if possible. If not possible, null
    try {
      IVal evaluatedCell = createdCell.evaluate(myCoord);
      this.computedValues.put(myCoord, evaluatedCell);
    } catch (Cell.RefException | Cell.CyclicDataException e) {
      // Do nothing because no need to update.
    }

    // add new cell to spreadsheet
    this.mysheet.put(myCoord, createdCell);

    // update each cell that points to this cell
    for (Map.Entry<Coord, List<Coord>> entry : this.whatWePointTo.entrySet()) {
      Coord toUpdate = entry.getKey();
      Cell needToUpdate = mysheet.get(toUpdate);
      try {
        needToUpdate.evaluate(toUpdate);
      } catch (Cell.RefException | Cell.CyclicDataException | NullPointerException d) {
       // We handle our exceptions elsewhere
      }
    }
  }

  @Override
  public void updateCell(Coord myCoord, IFormula myContents) throws IllegalArgumentException {

    if (myCoord == null) {
      throw new IllegalArgumentException("the coordinate can not be null!");
    } else {
      this.updateWithoutCycle(myCoord, myContents);
    }
  }

  @Override
  public void deleteCell(Coord myCellCoord) throws IllegalArgumentException {
    if (myCellCoord == null) {
      throw new IllegalArgumentException("the coordinate can not be null!");
    } else {
      mysheet.remove(myCellCoord);
    }
  }

  @Override
  public HashMap<Coord, Cell> getSheet() {
    return this.mysheet;
  }

  @Override
  public Coord getLargestCoord() {
    int largestRow = 25;
    int largestCol = 25;

    for (Map.Entry<Coord, Cell> entry : this.mysheet.entrySet()) {
      if (entry.getKey().row > largestRow) {
        largestRow = entry.getKey().row;
      }
      if (entry.getKey().col > largestCol) {
        largestCol = entry.getKey().col;
      }
    }
    return new Coord(largestCol, largestRow);
  }
}
