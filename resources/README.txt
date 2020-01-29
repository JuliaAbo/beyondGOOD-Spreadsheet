Assignment 9 changes:
- Updated visitors to work with our ColumnReference

Assignment 9 additions:
- ColumnReference
    - Represents a reference that spans an entire column.

Features:
- We were able to get all of the features working in our assignment including:
    - selecting a cell
    - updating a cell's contents
    - rejecting an edit to a cell
    - displaying a cell's formula
    - handling bad input through a pop up message
    - infinite scrolling
- The only feature we did not implement was our providers' extra credit feature which was the
menu bar.

Design Overview:

Assignment 8 additions:
- IAdapterController
    - Represents a controller that adapts our controller and our providers controller. Required
    so we can adjust the view of the AdapterController (cyclic issue mentioned in the CodeReview).
- AdapterController
    - An implementation of AdapterController which allows our controller to use their functionality.
- ValueAdapter
    - Represents an adapter that bridges our value and our providers value.
- CellAdapter
    - Represents an adapter that bridges our Cell and our providers ICell.
- ModelAdapter
    - This is a class that allows for our ISpreadsheetModel to be used as a
    ReadOnlySpreadsheetModel.
- SpreadsheetModelAdapter
    - A dummy implementation that we needed to implement a ModelAdapter (necessary for the
    providers' evaluateCell() method.

- Providers Code
    - SpreadsheetModel
    - ReadOnlySpreadsheetModel
    - IValue
    - IFormulaCell
    - ICell
    - Features
    - SpreadsheetController
    - CellPanel
    - ColPanel
    - EditableView
    - EditPanel
    - InputPanel
    - RowPanel
    - ScrollPanel
    - SpreadsheetView
    - VisualView

Assignment 7:
- ISpreadsheetController
    - An interface that represents the controller of a spreadsheet (enables and mediates
    interaction between the User and Model).
- Features
    - Represents the actionable things a user can do with a spreadsheet (user interactions). We
    included this interface with the goal of decoupling our controller implementation from the
    Java Swing library.
- Controller
    - An implementation of both ISpreadsheetController and Features. The product is a controller
    that renders the spreadsheet and supports user interaction with it.
- MockSpreadsheetModel
    - Represents a mock version of the model of a spreadsheet that keeps a log of what methods in the
    class are called. This class is solely implemented for testing purposes.
- MockSpreadsheetView
    - Represents a mock version of the view of a spreadsheet that keeps a log of what methods in the
    class are called. This class is solely implemented for testing purposes.
- ContainerPanel
    - A JPanel that acts as a wrapper/container of our collection of components that make up
    the view of a spreadsheet (excluding the TextInputPanel).


Assignment 6:
- AdapterSpreadsheet
    - an Adapter that is a protected ISpreadsheet. It does not allow mutator methods on the
    spreadsheet and is used by the view
-ISpreadsheetView
    - An interface that represents the view.
-ColumnHeader
    - A JPanel that represents the top display of columns.
-RowHeader
    - A JPanel that represents the side display of rows.
-SpreadsheetPanel
    - A JPanel that represents the cells of data from our spreadsheet.
-TextInputPanel
    - A JPanel that represents the user interface for updating, deleting and adding cells.
-TextualSpreadsheetView
    - A view that renders the spreadsheet textually. Can output into the console, or into a file,
    or into another form, depending on the output specified.
-VisualSpreadsheetView
    - A graphical representation of our spreadsheet.
-Features
    - A class that represents the different actionable items of our spreadsheet. We created this
    for future functionality with the controller, but it is not implemented as of now.


Assignment 5:
- ISpreadsheetModel
    - An interface to represent the model of the spreadsheet.
- BasicSpreadsheet
   - Our implementation of the spreadsheet model. Values are represented by IVals. Errors are thrown
    and later displayed (in the view) if a value is invalid.
- Cell
    - How we represent a cell of a spreadsheet. It contains its raw contents, an IFormula,
     which can be evaluated to an IVal.
- IFormula
    - A Formula, which is one of an IVal, an IFunction or a Reference.
- IFunc
    - A function object that allows for abstraction that takes input A and outputs R.
- IVal
    - Our representation of values, the final form of evaluated contents.
- BooleanVal
    - An IVal. Represents a boolean value for our spreadsheet.
- DoubleVal
    - An IVal. Represents a double value for our spreadsheet.
- StringVal
    - An IVal. Represents a string value for our spreadsheet.
- Reference
    - A rectangular region of cells. Can be one cell (A1) or multiple (A1:B3).
- IFunction
    - An interface that represents functions (like SUM, PRODUCT, etc). Represents an operation on
    cells.
- ConcatFunc
   - A function that concatenates strings together.
- LessThan
   - A function that checks if the first value is less than the second.
- SumFunc
   - A function that adds two numbers together.
- ProdFunc
   - A function that multiplies two numbers together.
-DoubleVisitor
    - A visitor that returns a Double whose value is determined by the type of value it is visiting.
-EvalVisitor
    - A visitor that returns a list of evaluated contents (IVals).
-IFormulaVisitor
    - An interface that describes the behavior of a visitor over IFormulas.
-IValVisitor
    - An interface that describes the behavior of a visitor over IVals.
-RefNonDupVisitor
    - A visitor that collects the references that a cell makes into a non-duplicated HashSet.
-RefVisitor
   - A visitor that finds all the references that an IFormula references.
- SexpToIFormula
    - A visitor that converts from an Sexp to an IFormula.
- StringVisitor
    - A visitor that returns a String depending on the IVal it is visiting.
-Main
    - A main class that runs our program.

