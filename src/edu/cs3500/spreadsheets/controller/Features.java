package edu.cs3500.spreadsheets.controller;

import edu.cs3500.spreadsheets.model.Coord;

/**
 * Represents the actionable things a user can do with a spreadsheet (user interactions).
 */
public interface Features {
  /**
   * Update the cell at the given Coord with the give IFormula.
   */
  void update(String contents);

  /**
   * Select the cell on the spreadsheet at the given Coord.
   * @param c coordinate on spreadsheet
   */
  void selectCell(Coord c);

  /**
   * Rejects the user's edits.
   */
  void reject();

  /**
   * Saves this spreadsheet.
   * @param filepath the filepath to the desired document.
   */
  void save(String filepath);

  /**
   * Deletes the selected Cell's contents.
   */
  void delete();

  /**
   * Shifts the selected cell on the spreadsheet by 1 in the given direction. If the selected cell
   * is at a left or upper edge, the selected cell will not change.
   * @param dir direction of shift (right, left, up, down)
   */
  void shiftSelect(String dir);

}
