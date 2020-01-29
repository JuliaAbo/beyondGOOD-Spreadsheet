package edu.cs3500.spreadsheets.view;

import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.Font;
import java.awt.Color;
import java.awt.BasicStroke;

import javax.swing.JPanel;

import edu.cs3500.spreadsheets.model.AdapterSpreadsheet;
import edu.cs3500.spreadsheets.model.Cell;
import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.ISpreadsheetModel;
import edu.cs3500.spreadsheets.model.contents.IFormula;
import edu.cs3500.spreadsheets.model.contents.IVal;

/**
 * A JavaSwing container that represents a spreadsheet.
 */
public class SpreadsheetPanel extends JPanel {
  private AdapterSpreadsheet sheet;
  private static final int CELL_HEIGHT = 20;
  private static final int CELL_WIDTH = 60;
  private Coord selected;
  private Coord largest;

  /**
   * Constructs a SpreadsheetPanel.
   *
   * @param sheet spreadsheet to display
   */
  public SpreadsheetPanel(AdapterSpreadsheet sheet) {
    this.sheet = sheet;
    this.selected = null;
    this.largest = this.sheet.getLargestCoord();
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    Graphics2D g2d = (Graphics2D) g;

    int fontSize = 12;
    g.setFont(new Font("TimesRoman", Font.PLAIN, fontSize));

    g.setColor(Color.BLACK);
    this.sheetBuilder(g2d, this.largest);
  }

  private void sheetBuilder(Graphics2D g, Coord toExpandTo) {

    Graphics2D g2d = (Graphics2D) g;

    int fontSize = 12;
    // Courier New
    g.setFont(new Font("TimesRoman", Font.PLAIN, fontSize));

    g.setColor(Color.BLACK);


    for (int i = 1; i <= toExpandTo.col; i++) {
      for (int j = 1; j <= toExpandTo.row; j++) {
        Coord c = new Coord(i, j);
        Cell temp = this.sheet.getCellAt(c);

        int x = i - 1;
        int y = j - 1;

        try {
          IVal cellContents = temp.evaluate(c);
          if (cellContents != null) {
            g.setColor(Color.WHITE);
            g.fillRect(x * CELL_WIDTH, y * CELL_HEIGHT, CELL_WIDTH, CELL_HEIGHT);

            g.setColor(Color.BLACK);
            g.drawRect(x * CELL_WIDTH, y * CELL_HEIGHT, CELL_WIDTH, CELL_HEIGHT);
            g.drawString(cellContents.toString(),
                    (x * CELL_WIDTH), (y * CELL_HEIGHT + CELL_HEIGHT));
          } else {
            g.setColor(Color.WHITE);
            g.fillRect(x * CELL_WIDTH, y * CELL_HEIGHT, CELL_WIDTH, CELL_HEIGHT);

            g.setColor(Color.BLACK);
            g.drawRect(x * CELL_WIDTH, y * CELL_HEIGHT, CELL_WIDTH, CELL_HEIGHT);
          }
        } catch (Cell.RefException e) {
          g.setColor(Color.WHITE);
          g.fillRect(x * CELL_WIDTH, y * CELL_HEIGHT, CELL_WIDTH, CELL_HEIGHT);

          g.setColor(Color.BLACK);
          g.drawRect(x * CELL_WIDTH, y * CELL_HEIGHT, CELL_WIDTH, CELL_HEIGHT);
          g.drawString("#REF!",
                  (x * CELL_WIDTH), (y * CELL_HEIGHT + CELL_HEIGHT));
        } catch (Cell.CyclicDataException e) {
          g.setColor(Color.WHITE);
          g.fillRect(x * CELL_WIDTH, y * CELL_HEIGHT, CELL_WIDTH, CELL_HEIGHT);

          g.setColor(Color.BLACK);
          g.drawRect(x * CELL_WIDTH, y * CELL_HEIGHT, CELL_WIDTH, CELL_HEIGHT);
          g.drawString("#VALUE!",
                  (x * CELL_WIDTH), (y * CELL_HEIGHT + CELL_HEIGHT));
        } catch (IllegalArgumentException e) {
          g.setColor(Color.WHITE);
          g.fillRect(x * CELL_WIDTH, y * CELL_HEIGHT, CELL_WIDTH, CELL_HEIGHT);

          g.setColor(Color.BLACK);
          g.drawRect(x * CELL_WIDTH, y * CELL_HEIGHT, CELL_WIDTH, CELL_HEIGHT);
          g.drawString("#VALUE!",
                  (x * CELL_WIDTH), (y * CELL_HEIGHT + CELL_HEIGHT));
        }
      }
    }
    if (this.selected != null) {
      g.setColor(Color.BLUE);
      g2d.setStroke(new BasicStroke(2));
      g.drawRect(((this.selected.col - 1) * CELL_WIDTH), (this.selected.row - 1) * CELL_HEIGHT,
              CELL_WIDTH, CELL_HEIGHT);
    }
  }


  /**
   * A method that returns the sheet being used.
   */
  public ISpreadsheetModel getSheet() {
    return this.sheet;
  }

  /**
   * A method that returns the largest Coord present.
   */
  public Coord getLargestCoord() {
    return this.largest;
  }

  /**
   * A method that selects a cell at a specified location.
   */
  public void selectCell(Coord loc) {
    this.selected = loc;
  }

  /**
   * A method that retrieves this the contents at this cell.
   */
  public IFormula retrieveCellContents(Coord c) {
    return this.sheet.getCellAt(c).getContents();
  }

  /**
   * A method that extends this spreadsheet vertically if it is scrolled that way.
   */
  public void extendVert() {
    this.largest = new Coord(this.largest.col,
            this.largest.row + 1);
    this.setSize((this.largest.col * 60), (this.largest.row + 1 * 20));
    this.paintComponent(this.getGraphics());
    this.repaint();
  }

  /**
   * A method that extends this spreadsheet horizontally if it is scrolled that way.
   */
  public void extendHor() {
    this.largest = new Coord(this.largest.col + 1,
            this.largest.row );
    this.setSize((this.largest.col + 1 * 60), (this.largest.row * 20));
    this.paintComponent(this.getGraphics());
    this.repaint();
  }
}
