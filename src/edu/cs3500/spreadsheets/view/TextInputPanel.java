package edu.cs3500.spreadsheets.view;

import java.awt.FlowLayout;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;

import edu.cs3500.spreadsheets.controller.Features;
import edu.cs3500.spreadsheets.model.contents.IFormula;

/**
 * A JavaSwing container that represents a spreadsheet.
 */
class TextInputPanel extends JPanel {
  private JButton update;
  private JButton reject;
  private JTextField text;

  /**
   * Constructs a TextInputPanel.
   */
  TextInputPanel() {
    this.setLayout(new FlowLayout());

    JLabel contents = new JLabel("Contents");
    this.text = new JTextField();
    this.text.setPreferredSize(new Dimension(250, 20));

    this.update = new JButton("✓");
    this.update.setActionCommand("Update");

    this.reject = new JButton("✗");
    this.reject.setActionCommand("Reject");

    this.add(contents);
    this.add(this.text);
    this.add(this.update);
    this.add(this.reject);
  }

  /**
   * A method that sets the features on the desired actionable items (the buttons) of this panel.
   * @param features the features that need to be added.
   */
  public void setListener(Features features) {
    this.update.addActionListener(evt -> {
      features.update(this.grabText());
      this.clear();
    });
    this.reject.addActionListener(evt -> {
      features.reject();
    });
  }

  private String grabText() {
    return this.text.getText();
  }

  private void clear() {
    this.text.setText("");
  }

  /**
   * A method that updates the internal text panel with the correct IFormula.
   * @param f the IFormula to update with.
   */
  public void updateTextField(IFormula f) {
    if (f == null) {
      this.clear();
    } else {
      this.text.setText(f.toString());
    }
  }

}
