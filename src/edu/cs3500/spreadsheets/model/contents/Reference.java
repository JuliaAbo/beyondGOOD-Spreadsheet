package edu.cs3500.spreadsheets.model.contents;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.cs3500.spreadsheets.model.Cell;
import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.visitor.IFormulaVisitor;
import edu.cs3500.spreadsheets.model.Cell.RefException;
import edu.cs3500.spreadsheets.model.visitor.RefVisitor;

/**
 * Represents a rectangular region of Cells in a Spreadsheet.
 */
public class Reference implements IFormula {
  private HashMap<Coord, Cell> regionRef;
  private Coord upperLeft;
  private Coord bottomRight;
  static Integer cycleCheck;

  /**
   * A constructor for References.
   *
   * @param upperLeft   the upper left coordinate of the region.
   * @param bottomRight the bottom left coordinate of the region
   * @param myMap       the already computed values
   */
  public Reference(Coord upperLeft, Coord bottomRight, HashMap<Coord, Cell> myMap) {
    this.regionRef = new HashMap<Coord, Cell>();
    this.upperLeft = upperLeft;
    this.bottomRight = bottomRight;
    cycleCheck = 0;
    if (upperLeft.equals(bottomRight)) {
      regionRef.put(upperLeft, (myMap.get(upperLeft)));
    }
    this.validate();
    this.buildRegion(myMap);
  }

  /**
   * Builds a rectangular region of Cells with the given coordinates of the rectangle and the
   * worksheet of Cells.
   *
   * @param myMap the worksheet of cells available
   */
  private void buildRegion(HashMap<Coord, Cell> myMap) {
    for (int i = upperLeft.row; i <= bottomRight.row; i++) {
      for (int j = upperLeft.col; j <= bottomRight.col; j++) {
        Coord desiredCoord = new Coord(j, i);
        this.regionRef.put(desiredCoord, (myMap.getOrDefault(desiredCoord, new Cell())));
      }
    }
  }

  /**
   * Confirms that the provided Coordinates are valid.
   *
   * @throws IllegalArgumentException if the arguments are null or invalid.
   */
  private void validate() throws IllegalArgumentException {
    if (this.upperLeft == null || this.bottomRight == null) {
      throw new IllegalArgumentException("Cannot be null");
    }
    if (upperLeft.row < 0) {
      throw new IllegalArgumentException("Bad upper row arguments");
    }
    if (upperLeft.col < 0) {
      throw new IllegalArgumentException("Bad upper col arguments");
    }
    if (bottomRight.row < 0) {
      throw new IllegalArgumentException("Bad bottom row args");
    }
    if (bottomRight.col < 0) {
      throw new IllegalArgumentException("Bad bottom col args");
    }
    if (upperLeft.row > bottomRight.row) {
      throw new IllegalArgumentException("Not allowed");
    }
    if (upperLeft.col > bottomRight.col) {
      throw new IllegalArgumentException("Not allowed");
    }
  }

  /**
   * A method to evaluate this Reference and its entire region.
   *
   * @param alreadyComputed the HashMap of already computed values.
   * @return a List of the computed values.
   * @throws RefException if a cell evaluates to just a region.
   */
  public List<IVal> evaluate(HashMap<Coord, IVal> alreadyComputed) throws RefException {
    if (cycleCheck > 50) {
      return new ArrayList<IVal>();
    }
    ArrayList<IVal> myList = new ArrayList<>();
    for (Map.Entry<Coord, Cell> myentry : this.regionRef.entrySet()) {
      try {
        myList.add(myentry.getValue().evaluate(myentry.getKey()));
      } catch (RefException | Cell.CyclicDataException e) {
        // Does not need to update anything
      } catch (NullPointerException e) {
        return myList;
      }
    }
    return myList;
  }

  /**
   * Gets the coords referenced by this reference.
   *
   * @return a list of the coords used by all the cells referenced in this region.
   */
  public List<Coord> getCoords(Coord checkAgainst) {
    ArrayList<Coord> myCoords = new ArrayList<Coord>();
    myCoords.addAll(this.regionRef.keySet());
    if (myCoords.contains(checkAgainst) || cycleCheck > 50) {
      return myCoords;
    }
    else {
      myCoords.addAll(this.findCoords(checkAgainst));
    }
    return myCoords;
  }

  private List<Coord> findCoords(Coord c) {
    cycleCheck = cycleCheck + 1;
    if (cycleCheck > 50) {
      return new ArrayList<Coord>();
    }
    ArrayList<Coord> myCoords = new ArrayList<Coord>();
    RefVisitor myRef = new RefVisitor(c);
    for (Map.Entry<Coord, Cell> myentry : this.regionRef.entrySet()) {
      IFormula myFormula = myentry.getValue().getContents();
      List<Coord> myFoundCoord = myRef.apply(myFormula);
      myCoords.addAll(myFoundCoord);
    }
    return myCoords;
  }

  @Override
  public <R> R accept(IFormulaVisitor<R> visitor) {
    return visitor.visitReference(this);
  }

  @Override
  public IFormula getFormula() {
    return this;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }

    if (!(obj instanceof Reference)) {
      return false;
    }

    Reference p = (Reference) obj;

    return p.getRegionRef().equals(this.regionRef);
  }

  @Override
  public String toString() {
    if (this.upperLeft.equals(this.bottomRight)) {
      return this.upperLeft.toString();
    } else {
      return this.upperLeft.toString() + ":" + this.bottomRight.toString();
    }
  }

  @Override
  public int hashCode() {
    return this.regionRef.hashCode();
  }

  /**
   * Gets a copy of the region of this reference.
   *
   * @return HashMap of the copy of this region.
   */
  private HashMap<Coord, Cell> getRegionRef() {
    return new HashMap<Coord, Cell>(this.regionRef);
  }
}
