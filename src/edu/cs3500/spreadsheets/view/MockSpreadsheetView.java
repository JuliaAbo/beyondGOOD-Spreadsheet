package edu.cs3500.spreadsheets.view;

import java.io.IOException;
import java.util.Objects;

import edu.cs3500.spreadsheets.controller.Features;
import edu.cs3500.spreadsheets.model.Coord;

/**
 * Mock version of the view of a spreadsheet (keeps a log of what methods were called).
 */
public class MockSpreadsheetView implements ISpreadsheetView {
  private StringBuilder log;

  /**
   * Constructs a MockSpreadsheetView.
   * @param log log of methods called
   */
  public MockSpreadsheetView(StringBuilder log) {
    this.log = Objects.requireNonNull(log);
  }

  @Override
  public void render() throws IOException {
    this.log.append("Render\n");
  }

  @Override
  public void addFeatures(Features features) {
    this.log.append("Add features\n");
  }

  @Override
  public void showErrorMessage(String error) {
    this.log.append("Show error: " + error + "\n");
  }

  @Override
  public void select(Coord c) {
    this.log.append("Select cell: " + c.toString() + "\n");
  }
}
