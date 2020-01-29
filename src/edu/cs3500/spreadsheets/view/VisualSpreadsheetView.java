package edu.cs3500.spreadsheets.view;

import java.awt.Dimension;
import java.awt.BorderLayout;

import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.JOptionPane;

import edu.cs3500.spreadsheets.controller.Features;
import edu.cs3500.spreadsheets.model.AdapterSpreadsheet;
import edu.cs3500.spreadsheets.model.Coord;

/**
 * Class that represents a visual view of a spreadsheet.
 */
public class VisualSpreadsheetView extends JFrame implements ISpreadsheetView {
  private static final int CELL_HEIGHT = 20;
  private static final int CELL_WIDTH = 60;

  /**
   * Constructs a visual view of a spreadsheet.
   */
  public VisualSpreadsheetView(AdapterSpreadsheet sheet) {
    super("My Spreadsheet");

    this.setSize(500, 300);
    this.setLocation(200, 200);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setPreferredSize(new Dimension(740, 500));
    this.setLayout(new BorderLayout());

    // largest coordinate in sheet
    Coord size = sheet.getLargestCoord();

    // custom sheet panel
    SpreadsheetPanel spreadsheet = new SpreadsheetPanel(sheet);
    spreadsheet.setSize(size.col * CELL_WIDTH, size.row * CELL_HEIGHT);
    spreadsheet.setPreferredSize(
            new Dimension(size.col * CELL_WIDTH, size.row * CELL_HEIGHT));

    // display
    JPanel c = new JPanel();
    c.setLayout(new BorderLayout());

    JScrollPane scroll = new JScrollPane(c);

    ColumnHeader myColumns = new ColumnHeader(sheet);
    RowHeader myRows = new RowHeader(sheet);

    c.add(spreadsheet, BorderLayout.CENTER);

    // set scroll bars
    JViewport myColumnView = new JViewport();
    myColumnView.setView(myColumns);
    myColumnView.setScrollMode(JViewport.SIMPLE_SCROLL_MODE);
    scroll.setColumnHeaderView(myColumnView);
    scroll.setRowHeaderView(myRows);
    scroll.setPreferredSize(new Dimension((size.col * CELL_WIDTH) + CELL_WIDTH,
            (size.row * CELL_HEIGHT) + CELL_HEIGHT));

    // add scroll (with display)
    this.add(scroll);
    c.setPreferredSize(new Dimension((size.col * CELL_WIDTH),
            (size.row * CELL_HEIGHT)));
    this.pack();
    this.setVisible(true);
  }

  @Override
  public void render() throws IOException {
    this.repaint();
  }

  @Override
  public void showErrorMessage(String error) {
    JOptionPane myErrorMessage = new JOptionPane(error);
    myErrorMessage.showMessageDialog(myErrorMessage, error);
    this.setVisible(true);
  }

  @Override
  public void select(Coord c) {
    // no coordinate to select
  }

  @Override
  public void addFeatures(Features features) {
    // no features to add
  }
}
