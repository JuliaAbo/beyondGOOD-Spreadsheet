package edu.cs3500.spreadsheets.controller;

/**
 * Represents the Controller of a Spreadsheet, which enables and mediates interaction between User
 * and Model.
 */
public interface ISpreadsheetController {

  /**
   * Renders the spreadsheet so that a client may be able to edit it.
   */
  void open();
}
