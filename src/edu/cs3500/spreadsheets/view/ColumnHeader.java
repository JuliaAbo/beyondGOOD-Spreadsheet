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
 * This class represents the heading of the columns on our spreadsheet. It begins its representation
 * with A, then scales up according to the column specifications of the Coord class.
 */
public class ColumnHeader extends JPanel {
  //private AdapterSpreadsheet sheet;
  private static final int CELL_HEIGHT = 20;
  private static final int CELL_WIDTH = 60;
  private Coord largest;

  /**
   * Constructs a SpreadsheetPanel.
   *
   * @param sheet spreadsheet to display
   */
  public ColumnHeader(AdapterSpreadsheet sheet) {
    this.largest = sheet.getLargestCoord();
    this.setPreferredSize(new Dimension(200 * (this.largest.col), CELL_HEIGHT));
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    Graphics2D g2d = (Graphics2D) g;

    int fontSize = 10;

    g.setFont(new Font("TimesRoman", Font.PLAIN, fontSize));

    g.setColor(Color.BLACK);


    g.drawRect(0 * CELL_WIDTH, CELL_HEIGHT, CELL_WIDTH, CELL_HEIGHT);

    for (int j = 1; j <= this.largest.col; j++) {
      String label = Coord.colIndexToName(j);

      g.drawRect((j - 1) * CELL_WIDTH, 0, CELL_WIDTH, CELL_HEIGHT);
      g.drawString(label, ((j - 1) * CELL_WIDTH), (CELL_HEIGHT));
    }
  }

  /**
   * A method that extends the ColumnHeader so it can grow when infinite scrolling occurs.
   */
  public void extend() {
    this.largest = new Coord(this.largest.col + 1,
            this.largest.row );
    this.setSize((this.largest.col * 60), (this.largest.row  * 20));
    this.setPreferredSize((new Dimension(200 * (this.largest.col), CELL_HEIGHT)));
    this.repaint();
  }
}

