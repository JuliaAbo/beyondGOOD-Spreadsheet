package edu.cs3500.spreadsheets.model;

import java.util.HashMap;
import java.util.Objects;

import edu.cs3500.spreadsheets.model.contents.IFormula;

/**
 * A class that represents a mock ISpreadsheetModel. Its methods execute information for observation
 * so validity of interactions with the controller can be determined.
 */
public class MockSpreadsheetModel implements ISpreadsheetModel {
  private StringBuilder log;

  public MockSpreadsheetModel(StringBuilder log) {
    this.log = Objects.requireNonNull(log);
  }

  @Override
  public Cell getCellAt(Coord myCoord) throws IllegalArgumentException {
    log.append("Get cell at: " + myCoord.toString() + ".\n");
    return null;
  }

  @Override
  public void updateCell(Coord myCellCoord, IFormula myContents) throws IllegalArgumentException {
    log.append("Update cell: " + myCellCoord.toString() + ". Contents: " + myContents.toString()
            + "\n");
  }

  @Override
  public void deleteCell(Coord myCellCoord) throws IllegalArgumentException {
    log.append("Delete cell at: " + myCellCoord.toString() + ".\n");
  }

  @Override
  public HashMap<Coord, Cell> getSheet() {
    log.append("Get sheet\n");
    return null;
  }

  @Override
  public Coord getLargestCoord() {
    log.append("Get largest coord\n");
    return null;
  }
}
