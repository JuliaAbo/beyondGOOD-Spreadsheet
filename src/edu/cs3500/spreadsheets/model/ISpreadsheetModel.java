package edu.cs3500.spreadsheets.model;

import java.util.HashMap;

import edu.cs3500.spreadsheets.model.contents.IFormula;

/**
 * An interface describing the actions that a SpreadsheetModel must implement.
 */

public interface ISpreadsheetModel {
  /**
   * Gets a cell at a coordinate in the spreadsheet.
   *
   * @param myCoord the coordinate of the cell we would like to get n
   * @return ICell that is the cell at that position
   * @throws IllegalArgumentException if the given coord is null.
   */
  Cell getCellAt(Coord myCoord) throws IllegalArgumentException;

  /**
   * Updates the given cell with the provided contents.
   *
   * @param myCellCoord the coordinates cell to update
   * @param myContents  the contents to update with
   * @throws IllegalArgumentException if the contents being passed in are null.
   */
  void updateCell(Coord myCellCoord, IFormula myContents) throws IllegalArgumentException;

  /**
   * Removes the Cell from the spreadsheet.
   *
   * @param myCellCoord the coordinate desired
   * @throws IllegalArgumentException if given coord is null.
   */
  void deleteCell(Coord myCellCoord) throws IllegalArgumentException;

  /**
   * Get the data in this spreadsheet.
   *
   * @return the actual spreadsheet
   */
  HashMap<Coord, Cell> getSheet();

  /**
   * Gets the largest coordinate that contains data in this spreadsheet.
   *
   * @return largest coordinate with data
   */
  Coord getLargestCoord();

}
