package edu.cs3500.spreadsheets.view;

import java.io.IOException;

import edu.cs3500.spreadsheets.controller.Features;
import edu.cs3500.spreadsheets.model.Cell;
import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.ISpreadsheetModel;

/**
 * Class that provides the behaviors of a textual view of a spreadsheet.
 */
public class TextualSpreadsheetView implements ISpreadsheetView {
  private final ISpreadsheetModel model;
  private final Appendable ap;

  public TextualSpreadsheetView(ISpreadsheetModel model, Appendable ap) {
    this.model = model;
    this.ap = ap;
  }

  @Override
  public void render() throws IOException {

    Coord largest = this.model.getLargestCoord();

    for (int i = 1; i <= largest.row; i++) {
      for (int j = 1; j <= largest.col; j++) {
        Coord c = new Coord(j, i);
        Cell temp = this.model.getCellAt(c);


        try {
          if (temp.evaluate(c) != null) {
            this.ap.append(c.toString() + " ");
            this.ap.append(temp.evaluate(c).toString() + "\n");
          }
        } catch (Cell.RefException e) {
          this.ap.append(c.toString() + " ");
          this.ap.append("#REF!\n");
        }
        catch (Cell.CyclicDataException e) {
          this.ap.append(c.toString() + " ");
          this.ap.append("#VALUE!\n");
        }
        catch (IllegalArgumentException e) {
          this.ap.append(c.toString() + " ");
          this.ap.append("#VALUE!\n");
        }

      }
    }
  }

  @Override
  public void showErrorMessage(String error) {
  // this does not need to do anything
  }

  @Override
  public void select(Coord c) {
    // do nothing
  }

  @Override
  public void addFeatures(Features features) {
    // do nothing
  }
}
