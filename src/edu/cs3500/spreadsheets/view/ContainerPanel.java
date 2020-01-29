package edu.cs3500.spreadsheets.view;

import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JScrollBar;
import javax.swing.JViewport;

import edu.cs3500.spreadsheets.controller.Features;
import edu.cs3500.spreadsheets.model.AdapterSpreadsheet;
import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.contents.IFormula;

/**
 * A JPanel that contains elements of a visual spreadsheet view.
 */
public class ContainerPanel extends JPanel {
  private static final int CELL_HEIGHT = 20;
  private static final int CELL_WIDTH = 60;
  private SpreadsheetPanel spreadsheet;
  private JScrollPane scroll;
  private JPanel internal;
  private Coord size;
  private RowHeader rowHeader;
  private ColumnHeader columnHeader;

  /**
   * Constructs a ContainerPanel.
   * @param sheet  a read-only spreadsheet, necessary to construct the spreadsheetPanel
   */
  public ContainerPanel(AdapterSpreadsheet sheet) {
    this.setLocation(200, 200);
    this.setPreferredSize(new Dimension(740, 500));
    this.setLayout(new BorderLayout());
    this.setFocusable(true);
    this.requestFocusInWindow();

    // largest coordinate in sheet
    this.size = sheet.getLargestCoord();
    this.setSize(size.col * CELL_WIDTH, size.row * CELL_HEIGHT);

    // custom sheet panel
    this.spreadsheet = new SpreadsheetPanel(sheet);
    spreadsheet.setSize(size.col * CELL_WIDTH, size.row * CELL_HEIGHT);
    spreadsheet.setPreferredSize(
            new Dimension(size.col * CELL_WIDTH, size.row * CELL_HEIGHT));

    // display
    this.internal = new JPanel();
    this.internal.setLayout(new BorderLayout());

    //This is what I changed to do more efficient scrolling
    this.scroll = new JScrollPane();
    JViewport myViewport = new JViewport();
    myViewport.add(this.internal);
    myViewport.setScrollMode(JViewport.BLIT_SCROLL_MODE);
    myViewport.setViewSize(new Dimension(740, 500));
    this.scroll.setViewportView(myViewport);

    this.columnHeader = new ColumnHeader(sheet);
    this.rowHeader = new RowHeader(sheet);

    this.internal.add(spreadsheet, BorderLayout.CENTER);
    this.scroll.setColumnHeaderView(this.columnHeader);
    this.scroll.setRowHeaderView(this.rowHeader);
    this.scroll.setPreferredSize(new Dimension((size.col * CELL_WIDTH) + CELL_WIDTH,
            (size.row * CELL_HEIGHT) + (CELL_HEIGHT  + 500
            )));
    this.scroll.getVerticalScrollBar().setUnitIncrement(CELL_HEIGHT);
    this.scroll.getHorizontalScrollBar().setUnitIncrement(CELL_HEIGHT);

    // add scroll (with display) and text panel¬
    this.add(this.scroll);
    this.internal.setPreferredSize(new Dimension((size.col * CELL_WIDTH),
            (size.row * CELL_HEIGHT)));

    this.setFocusable(true);
  }

  /**
   * A method that sets a listener on to this Container panel's spreadsheet.
   * @param features the Features we would like this spreadsheet to support.
   */
  public void setListener(Features features) {
    this.spreadsheet.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();

        int col = x / CELL_WIDTH;
        int row = y / CELL_HEIGHT;

        Coord selected = new Coord(col + 1, row + 1);
        ContainerPanel.this.setSelect(selected);
        features.selectCell(selected);
      }
    });
    this.addKeyListener(new KeyAdapter() {
      @Override
      public void keyTyped(KeyEvent e) {
        if (e.getExtendedKeyCode() == KeyEvent.VK_BACK_SPACE) {
          features.delete();
        }
      }

      @Override
      public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
          features.shiftSelect("right");
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
          features.shiftSelect("left");
        }
        if (e.getKeyCode() == KeyEvent.VK_UP) {
          features.shiftSelect("up");
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
          features.shiftSelect("down");
        }
      }
    });
  }

  /**
   * A method that selects the desired Coord.
   * @param loc the location to select at.
   */
  public void setSelect(Coord loc) {
    this.spreadsheet.selectCell(loc);
    this.repaint();
  }

  /**
   * A method that returns the raw contents of a given cell.
   * @param c the desired coord of the cell
   * @return
   */
  public IFormula retrieveCellContents(Coord c) {
    return this.spreadsheet.retrieveCellContents(c);
  }

  /**
   * A method that sets adjustment listeners on our scroll bars.
   * @param rows the listener for the vertical scroll bar
   * @param columns the listener for the horizontal scroll bar
   */
  public void scrollCorrect(AdjustmentListener rows, AdjustmentListener columns) {
    this.scroll.getVerticalScrollBar().addAdjustmentListener(new AddScrolledR());
    this.scroll.getHorizontalScrollBar().addAdjustmentListener(new AddScrolledC());
  }

  /**
   * A method that extends this container panel vertically to assist with scrolling.
   */
  public void extendVert() {
    this.spreadsheet.extendVert();
  }

  /**
   * A method that extends this container panel horizontally to assist with scrolling.
   */
  public void extendHor() {
    this.spreadsheet.extendVert();
  }

  /**
   * A class that serves as an AdjustmentListener on our Vertical scroll bar.
   */
  private final class AddScrolledR implements AdjustmentListener {

    @Override
    public void adjustmentValueChanged(AdjustmentEvent e) {
      ContainerPanel.this.size = ContainerPanel.this.spreadsheet.getLargestCoord();
      JScrollBar x = ContainerPanel.this.scroll.getVerticalScrollBar();
      if (x.getValue() > x.getMaximum() - x.getVisibleAmount() - 20) {
        ContainerPanel.this.size = ContainerPanel.this.spreadsheet.getLargestCoord();
        ContainerPanel.this.internal.setPreferredSize(new Dimension((size.col * CELL_WIDTH),
                (size.row * CELL_HEIGHT)));
        ContainerPanel.this.spreadsheet.extendVert();
        ContainerPanel.this.rowHeader.extend();
        ContainerPanel.this.spreadsheet.repaint();
      }
    }
  }

  /**
   * A class that serves as an AdjustmentListener on our Horizontal scroll bar.
   */
  private final class AddScrolledC implements AdjustmentListener {
    @Override
    public void adjustmentValueChanged(AdjustmentEvent e) {
      ContainerPanel.this.size = ContainerPanel.this.spreadsheet.getLargestCoord();
      JScrollBar x = ContainerPanel.this.scroll.getHorizontalScrollBar();
      if ( x.getValue() > x.getMaximum() - x.getVisibleAmount() - 20) {
        ContainerPanel.this.size = ContainerPanel.this.spreadsheet.getLargestCoord();
        ContainerPanel.this.internal.setPreferredSize(new Dimension((size.col * CELL_WIDTH),
                (size.row * CELL_HEIGHT)));
        ContainerPanel.this.spreadsheet.extendHor();
        ContainerPanel.this.columnHeader.extend();
        ContainerPanel.this.spreadsheet.repaint();
      }
    }
  }
}
