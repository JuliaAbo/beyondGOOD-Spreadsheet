package edu.cs3500.spreadsheets.model.contents;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.cs3500.spreadsheets.model.Cell;
import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.visitor.IFormulaVisitor;
import edu.cs3500.spreadsheets.model.visitor.RefVisitor;

/**
 * Represents a column of cells, or a range of columns of cells, in a spreadsheet.
 */
public class ColumnReference implements IFormula {
  private HashMap<Coord, Cell> columns;
  private HashMap<Coord, Cell> spreadsheet;
  private int leftCol;
  private int rightCol;
  //private static Integer cycleCheck;

  /**
   * Constructs a ColumnReference.
   *
   * @param leftCol  the left most column in the reference
   * @param rightCol the right most column in the reference
   * @param sheet    the spreadsheet
   */
  public ColumnReference(int leftCol, int rightCol, HashMap<Coord, Cell> sheet) {
    this.columns = new HashMap<Coord, Cell>();
    this.spreadsheet = sheet;

    this.leftCol = leftCol;
    this.rightCol = rightCol;

    //cycleCheck = 0;

    this.validate();
    this.updateColumnRef();
  }

  @Override
  public <R> R accept(IFormulaVisitor<R> visitor) {
    return visitor.vistColumnReference(this);
  }

  @Override
  public IFormula getFormula() {
    return this;
  }

  /**
   * Updates this column reference to hold every cell in the spreadsheet with non-empty contents.
   */
  public void updateColumnRef() {
    this.columns = new HashMap<Coord, Cell>();
    for (int i = leftCol; i <= rightCol; i++) {
      for (Map.Entry<Coord, Cell> entry : this.spreadsheet.entrySet()) {
        if (entry.getKey().col == i) {
          this.columns.put(entry.getKey(), entry.getValue());
        }
      }
    }
  }

  /**
   * Confirms that the provided columns are valid.
   */
  private void validate() {
    if (this.leftCol < 0) {
      throw new IllegalArgumentException("Bad upper row arguments");
    }
    if (this.rightCol < 0) {
      throw new IllegalArgumentException("Bad upper col arguments");
    }
    if (this.leftCol > this.rightCol) {
      throw new IllegalArgumentException("Not allowed");
    }
  }

  /**
   * A method to evaluate this ColumnReference and its entire region.
   *
   * @param alreadyComputed the HashMap of already computed values.
   * @return a List of the computed values.
   * @throws Cell.RefException if a cell evaluates to just a region.
   */
  public List<IVal> evaluate(HashMap<Coord, IVal> alreadyComputed) throws Cell.RefException {
    //if(cycleCheck > 50) {
    //  return new ArrayList<IVal>();
    //}
    this.updateColumnRef();

    ArrayList<IVal> myList = new ArrayList<>();
    for (Map.Entry<Coord, Cell> myentry : this.columns.entrySet()) {
      try {
        myList.add(myentry.getValue().evaluate(myentry.getKey()));
      } catch (Cell.RefException | Cell.CyclicDataException e) {
        // Does not need to update anything
      } catch (NullPointerException e) {
        return myList;
      }
    }
    return myList;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }

    if (!(obj instanceof ColumnReference)) {
      return false;
    }

    ColumnReference p = (ColumnReference) obj;

    return p.getColRef().equals(this.columns);
  }

  @Override
  public String toString() {
    return String.format("%s:%s", Coord.colIndexToName(this.leftCol),
            Coord.colIndexToName(this.rightCol));

  }

  @Override
  public int hashCode() {
    return this.columns.hashCode();
  }

  /**
   * Gets the coords referenced by this ColumnReference.
   *
   * @param checkAgainst coordinate to check against (if this coordinate is found in the accumulated
   *                     list, immediately return the list of Coords).
   * @return a list of the Coords used by all the cells referenced in this region.
   */
  public List<Coord> getCoords(Coord checkAgainst) {
    ArrayList<Coord> myCoords = new ArrayList<Coord>();

    this.updateColumnRef();

    for (int i = leftCol; i <= rightCol; i++) {
      for (Map.Entry<Coord, Cell> entry : this.columns.entrySet()) {
        if (entry.getKey().col == checkAgainst.col) {
          myCoords.add(checkAgainst);
          return myCoords;
        }
      }
    }

    myCoords.addAll(this.columns.keySet());

    if (myCoords.contains(checkAgainst)) {
      return myCoords;
    } else {
      myCoords.addAll(this.findCoords(checkAgainst));
    }
    return myCoords;
  }

  /**
   * Helper to get the Coords that this ColumnReference references.
   *
   * @param c coordinate to check against
   * @return list of Coords found.
   */
  private List<Coord> findCoords(Coord c) {
    ArrayList<Coord> myCoords = new ArrayList<Coord>();
    RefVisitor myRef = new RefVisitor(c);
    for (Map.Entry<Coord, Cell> myentry : this.columns.entrySet()) {
      IFormula myFormula = myentry.getValue().getContents();
      List<Coord> myFoundCoord = myRef.apply(myFormula);
      myCoords.addAll(myFoundCoord);
    }
    return myCoords;
  }

  /**
   * Gets a copy of the region of this ColumnReference.
   *
   * @return HashMap of the copy of this ColumnReference.
   */
  private HashMap<Coord, Cell> getColRef() {
    return new HashMap<Coord, Cell>(this.columns);
  }
}