package edu.cs3500.spreadsheets.view;

import java.awt.FlowLayout;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;

import edu.cs3500.spreadsheets.controller.Features;

/**
 * A JavaSwing container that represents a spreadsheet.
 */
class SaveBar extends JPanel {
  private JButton save;
  private JTextField filepath;

  /**
   * Constructs a TextInputPanel.
   */
  public SaveBar() {
    this.setLayout(new FlowLayout());

    JLabel contents = new JLabel("Filepath:");
    this.filepath = new JTextField();
    this.filepath.setPreferredSize(new Dimension(250, 20));

    this.save = new JButton("save");
    this.save.setActionCommand("Save");

    this.add(contents);
    this.add(this.filepath);
    this.add(this.save);
  }

  /**
   * A method that sets the features on the desired actionable items (the buttons) of this panel.
   * @param features the features that need to be added.
   */
  public void setListener(Features features) {
    this.save.addActionListener(evt -> {
      features.save(this.grabText());
    });
  }

  private String grabText() {
    return this.filepath.getText();
  }

}
