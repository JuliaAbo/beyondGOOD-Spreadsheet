package edu.cs3500.spreadsheets.view;

import java.io.IOException;

import edu.cs3500.spreadsheets.controller.Features;
import edu.cs3500.spreadsheets.model.Coord;

/**
 * An interface describing the view of a spreadsheet.
 */
public interface ISpreadsheetView {

  /**
   * Renders a model in some manner (e.g. as text, graphics, etc.).
   *
   * @throws IOException if the rendering fails for some reason
   */
  void render() throws IOException;

  /**
   * Adds a listener for a spreadsheet action.
   *
   * @param features action listener
   */
  void addFeatures(Features features);

  /**
   * Shows an error message if the desired command is not valid.
   *
   * @param error the error to display
   */
  void showErrorMessage(String error);

  /**
   * Select the given cell in the spreadsheet.
   * @param c the coordinate of
   */
  void select(Coord c);

}
