package edu.cs3500.spreadsheets.model;

import java.util.HashMap;

import edu.cs3500.spreadsheets.model.contents.IFormula;

/**
 * A protective spreadsheet model that doesn't allow mutation.
 */
public class AdapterSpreadsheet implements ISpreadsheetModel {
  private final ISpreadsheetModel model;

  public AdapterSpreadsheet(ISpreadsheetModel m) {
    this.model = m;
  }

  @Override
  public Cell getCellAt(Coord myCoord) throws IllegalArgumentException {
    return this.model.getCellAt(myCoord);
  }

  @Override
  public void updateCell(Coord myCellCoord, IFormula myContents) throws IllegalArgumentException {
    throw new IllegalStateException("mutation should not happen");
  }

  @Override
  public void deleteCell(Coord myCellCoord) throws IllegalArgumentException {
    throw new IllegalStateException("mutation should not happen");
  }

  @Override
  public HashMap<Coord, Cell> getSheet() {
    return this.model.getSheet();
  }

  @Override
  public Coord getLargestCoord() {
    return this.model.getLargestCoord();
  }
}
