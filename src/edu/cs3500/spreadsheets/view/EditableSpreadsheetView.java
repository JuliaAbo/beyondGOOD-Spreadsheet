package edu.cs3500.spreadsheets.view;

import java.awt.BorderLayout;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import edu.cs3500.spreadsheets.controller.Features;
import edu.cs3500.spreadsheets.model.AdapterSpreadsheet;
import edu.cs3500.spreadsheets.model.Coord;

/**
 * This is a class that represents a spreadsheet that can get real-time client updates. A client can
 * select a cell, which is represented by a blue box appearing around the cell, as well as use a
 * textField and button to edit that cell and update/ reject those contents.
 */
public class EditableSpreadsheetView extends JFrame implements ISpreadsheetView {
  private ContainerPanel visualSheet;
  private final TextInputPanel textInput;

  private SaveBar bar;

  /**
   * Constructs an EditableSpreadsheetView.
   *
   * @param a read-only spreadsheet
   */
  public EditableSpreadsheetView(AdapterSpreadsheet a) {
    super("My Spreadsheet");
    this.visualSheet = new ContainerPanel(a);
    this.visualSheet.scrollCorrect(new AddScrolledC(), new AddScrolledR());
    this.textInput = new TextInputPanel();
    this.setLayout(new BorderLayout());
    this.add(this.textInput, BorderLayout.PAGE_END);
    this.add(visualSheet, BorderLayout.CENTER);
    this.bar = new SaveBar();
    this.add(this.bar, BorderLayout.PAGE_START);

    this.pack();
    this.setVisible(true);
  }

  @Override
  public void render() throws IOException {
    this.repaint();
    this.visualSheet.requestFocus();
  }


  @Override
  public void showErrorMessage(String error) {
    JOptionPane myErrorMessage = new JOptionPane(error);
    myErrorMessage.showMessageDialog(myErrorMessage, error);
  }

  @Override
  public void select(Coord c) {

    this.visualSheet.setSelect(c);
    this.updateTextField(c);
    this.repaint();
  }

  public void updateTextField(Coord c) {
    this.textInput.updateTextField(this.visualSheet.retrieveCellContents(c));
  }

  @Override
  public void addFeatures(Features features) {
    this.bar.setListener(features);
    this.textInput.setListener(features);
    this.visualSheet.setListener(features);
  }

  private final class AddScrolledR implements AdjustmentListener {
    @Override
    public void adjustmentValueChanged(AdjustmentEvent e) {
      EditableSpreadsheetView.this.visualSheet.extendVert();
      EditableSpreadsheetView.this.repaint();
    }
  }

  private final class AddScrolledC implements AdjustmentListener {
    @Override
    public void adjustmentValueChanged(AdjustmentEvent e) {
      EditableSpreadsheetView.this.visualSheet.extendHor();
      EditableSpreadsheetView.this.repaint();
    }
  }
}
