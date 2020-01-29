package edu.cs3500.spreadsheets.view;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Font;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;

import edu.cs3500.spreadsheets.model.AdapterSpreadsheet;
import edu.cs3500.spreadsheets.model.Coord;

/**
 * A JavaSwing container that holds the left-most column of row headers of a spreadsheet.
 */
public class RowHeader extends JPanel {
  private static final int CELL_HEIGHT = 20;
  private static final int CELL_WIDTH = 60;
  private Coord largest;

  /**
   * Constructs a RowHeader with the given spreadsheet.
   * @param sheet spreadsheet
   */
  public RowHeader(AdapterSpreadsheet sheet) {
    this.largest = sheet.getLargestCoord();
    this.setPreferredSize(new Dimension(CELL_WIDTH, 200 * largest.row));
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    Graphics2D g2d = (Graphics2D) g;

    int fontSize = 12;
    g.setFont(new Font("TimesRoman", Font.PLAIN, fontSize));

    g.setColor(Color.BLACK);

    // draw overlap cell between column & row headers
    g.drawRect(CELL_WIDTH, 0 * CELL_HEIGHT, CELL_WIDTH, CELL_HEIGHT);

    for (int j = 1; j <= this.largest.row; j++) {
      String label = Integer.toString(j);

      g.drawRect(0, (j - 1) * CELL_HEIGHT, CELL_WIDTH, CELL_HEIGHT);
      g.drawString(label, 0, (j - 1) * CELL_HEIGHT + (CELL_HEIGHT));
    }
  }

  /**
   * A method that extends this RowHeader as scrolling is done.
   */
  public void extend() {
    this.largest = new Coord(this.largest.col,
            this.largest.row + 1);
    this.setSize((this.largest.col * 60), (this.largest.row + 1 * 20));
    this.setPreferredSize(new Dimension(CELL_WIDTH, 700 * largest.row));
    this.repaint();
  }
}
