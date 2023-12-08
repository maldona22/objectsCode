import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.HashSet;
import java.awt.Color;

import javalib.impworld.*;
import javalib.worldimages.*;
import tester.Tester;


// The data structure behind each individual tile on the gameboard
class Cell {
  /* Template
     *
     * Fields:
     * this.neighbors -- ArrayList<Cell>
     * this.numMinesAdjacent -- int
     * this.hasMine -- boolean
     * this.revealed -- boolean
     * this.flagged -- boolean
     * 
     * Methods:
     * this.getTopMiddleNeighbor() -- Cell
     * this.setTopMiddleNeighbor(Cell cell) -- void
     * this.getBottomMiddleNeighbor() -- Cell
     * this.setBottomMiddleNeighbor(Cell cell) -- void
     * this.getMiddleLeftNeighbor() -- Cell
     * this.setMiddleLeftNeighbor(Cell cell) -- void
     * this.getTopLeftNeighbor() -- Cell
     * this.setTopLeftNeighbor(Cell cell) -- void
     * this.getTopRightNeighbor() -- Cell
     * this.setTopRightNeighbor(Cell cell) -- void
     * this.getBottomLeftNeighbor() -- Cell
     * this.setBottomLeftNeighbor(Cell cell) -- void
     * this.getBottomRightNeighbor() -- Cell
     * this.setBottomRightNeighbor(Cell cell) -- void
     * this.getCell(int x, int y) -- Cell
     * this.numOfMinesAdjacent() -- int
     * 
     * Methods on fields:
     * this.neighbors.add(int index, Cell element) -- void
     * this.neighbors.add(Cell element) -- boolean
     * this.neighbors.addAll(int index, Collection<> c) -- boolean
     * this.neighbors.addAll(Collection<> c) -- boolean
     * this.neighbors.addFirst(Cell element) -- void
     * this.neighbors.addLast(Cell element) -- void
     * this.neighbors.clear() -- void
     * this.neighbors.clone() -- Object
     * this.neighbors.contains(Object o) -- boolean
     * this.neighbors.ensureCapacity(int minCapacity) -- void
     * this.neighbors.equals(Object o) -- boolean
     * this.neighbors.forEach(Consumer<> action) -- void
     * this.neighbors.get(int index) -- Cell
     * this.neighbors.getFirst() -- Cell
     * this.neighbors.getLast() -- Cell
     * this.neighbors.hashCode() -- int
     * this.neighbors.indexOf(Object o) -- int
     * this.neighbors.isEmpty() -- boolean
     * this.neighbors.iterator() -- Iterator<Cell>
     * this.neighbors.lastIndexOf(Object o) -- int
     * this.neighbors.listIterator() -- ListIterator<Cell>
     * this.neighbors.listIterator(int index) -- ListIterator<Cell>
     * this.neighbors.remove(int index) -- Cell
     * this.neighbors.remove(Object o) -- boolean
     * this.neighbors.removeAll(Collection<> c) -- boolean
     * this.neighbors.removeFirst() -- Cell
     * this.neighbors.removeIf(Predicate<> filter) -- boolean
     * this.neighbors.removeLast() -- Cell
     * this.neighbors.removeRange(int fromIndex, int toIndex) -- List<Cell>
     * this.neighbors.toArray() -- Object[]
     * this.neighbors.toArray(T[] a) -- <T> T[]
     * this.neighbors.trimToSize() -- void
     * 
     */

  // Neighboring cells are indexed depending on location around this cell, labeled X here
  // 0 1 2
  // 3 X 4
  // 5 6 7
  // This is an implementation detail that doesn't need to be remembered by its users
  // Users just use the designated setters and getters, or the neighbors iterator
  // when the location around the cell is irrelevant
  ArrayList<Cell> neighbors;

  // Used to display number of adjacent mines during gameplay
  int numOfMinesAdjacent;

  // Holds whether cell contains a mine or not
  boolean hasMine;

  // Holds whether the cell has been revealed by the player or not
  boolean revealed;

  // Holds whether the player as flagged this cell as a potential location of a mine
  boolean flagged;

  Cell() {
    // Most amount of neighbhors a cell can have is 8, so just prealloc it to begin
    // with
    this.neighbors = new ArrayList<Cell>(8);
    // I greatly dislike doing this
    // TODO figure out a way to set the *size* and not the *capacity* without this
    // nonsense
    for (int i = 0; i < 8; i++) {
      neighbors.add(i, null);
    }

    this.hasMine = false;
    this.flagged = false;
    this.revealed = false;
  }

  // Returns the cell directly above this cell
  Cell getTopMiddleNeighbor() {
    return neighbors.get(1);
  }

  // EFFECT: Mutates the neighbors arraylist to contain a reference to cell
  // where the cell directly above this cell is expected to be
  void setTopMiddleNeighbor(Cell cell) {
    neighbors.set(1, cell);
  }

  // Returns the cell directly below this cell
  Cell getBottomMiddleNeighbor() {
    return neighbors.get(6);
  }

  // EFFECT: Mutates the neighbors arraylist to contain a reference to cell
  // where the cell directly below this cell is expected to be
  void setBottomMiddleNeighbor(Cell cell) {
    neighbors.set(6, cell);
  }

  // Returns the cell to the left of this cell
  Cell getMiddleLeftNeighbor() {
    return neighbors.get(3);
  }

  // EFFECT: Mutates the neighbors arraylist to contain a reference to cell
  // where the cell directly to the left of this cell is expected to be
  void setMiddleLeftNeighbor(Cell cell) {
    neighbors.set(3, cell);
  }

  // Returns the cell to the right of this cell
  Cell getMiddleRightNeighbor() {
    return neighbors.get(4);
  }

  // EFFECT: Mutates the neighbors arraylist to contain a reference to cell
  // where the cell directly to the right of this cell is expected to be
  void setMiddleRightNeighbor(Cell cell) {
    neighbors.set(4, cell);
  }

  // Returns the cell above and to the left this cell
  Cell getTopLeftNeighbor() {
    return neighbors.get(0);
  }

  // EFFECT: Mutates the neighbors arraylist to contain a reference to cell
  // where the cell above and to the left of this cell is expected to be
  void setTopLeftNeighbor(Cell cell) {
    neighbors.set(0, cell);
  }

  // Returns the cell above and to the right this cell
  Cell getTopRightNeighbor() {
    return neighbors.get(2);
  }

  // EFFECT: Mutates the neighbors arraylist to contain a reference to cell
  // where the cell above and to the right of this cell is expected to be
  void setTopRightNeighbor(Cell cell) {
    neighbors.set(2, cell);
  }

  // Returns the cell below and to the left this cell
  Cell getBottomLeftNeighbor() {
    return neighbors.get(5);
  }

  // EFFECT: Mutates the neighbors arraylist to contain a reference to cell
  // where the cell below and to the left of this cell is expected to be
  void setBottomLeftNeighbor(Cell cell) {
    neighbors.set(5, cell);
  }

  // Returns the cell below and to the right this cell
  Cell getBottomRightNeighbor() {
    return neighbors.get(7);
  }

  // EFFECT: Mutates the neighbors arraylist to contain a reference to cell
  // where the cell below and to the right of this cell is expected to be
  void setBottomRightNeighbor(Cell cell) {
    neighbors.set(7, cell);
  }

  // Traverses the graph of cells and returns the one specified by the (x,y) pair
  Cell getCell(int x, int y) {
    if (y > 0) {
      return this.getBottomMiddleNeighbor().getCell(x, y - 1);
    } else if (x > 0) {
      return this.getMiddleRightNeighbor().getCell(x - 1, y);
    } else {
      return this;
    }
  }

  // Returns the number of mines adjacent to this cell
  int numOfMinesAdjacent() {
    int numOfMines = 0;
    for (Cell cell : neighbors) {
      if (cell == null) {
        continue;
      } else if (cell.hasMine) {
        numOfMines++;
      }
    }
    return numOfMines;
  }
}

// Acts as a sentinel of sorts for the first cell in the game board
// Also contains all the methods for initializing the graph of cells
class Board {
  /* Template
     *
     * Fields:
     * this.widthInTiles -- int
     * this.heightInTiles -- int
     * this.initialCell -- Cell
     * 
     * Methods:
     * this.stitchRows(Cell topRow, Cell bottomRow)  -- void
     * this.initializeBoard(Cell cell) -- void
     * <T> this.swap(ArrayList<T> list, final int indexA, final int indexB) -- void
     * this.fisherYatesShuffle(ArrayList<Integer> list, final int numItems) -- void
     * this.initializeMines(final int numMines) -- void
     * 
     * Methods on fields:
     * this.initialCell.getTopMiddleNeighbor() -- Cell
     * this.initialCell.setTopMiddleNeighbor(Cell cell) -- void
     * this.initialCell.getBottomMiddleNeighbor() -- Cell
     * this.initialCell.setBottomMiddleNeighbor(Cell cell) -- void
     * this.initialCell.getMiddleLeftNeighbor() -- Cell
     * this.initialCell.setMiddleLeftNeighbor(Cell cell) -- void
     * this.initialCell.getTopLeftNeighbor() -- Cell
     * this.initialCell.setTopLeftNeighbor(Cell cell) -- void
     * this.initialCell.getTopRightNeighbor() -- Cell
     * this.initialCell.setTopRightNeighbor(Cell cell) -- void
     * this.initialCell.getBottomLeftNeighbor() -- Cell
     * this.initialCell.setBottomLeftNeighbor(Cell cell) -- void
     * this.initialCell.getBottomRightNeighbor() -- Cell
     * this.initialCell.setBottomRightNeighbor(Cell cell) -- void
     * this.initialCell.getCell(int x, int y) -- Cell
     * this.initialCell.numOfMinesAdjacent() -- int
     * 
     */

  final int widthInTiles;
  final int heightInTiles;
  Cell initialCell;

  Board(int width, int height, int numMines) {
    this.widthInTiles = width;
    this.heightInTiles = height;
    this.initialCell = new Cell();
    initializeBoard(initialCell);
    initializeMines(numMines);
  }

  // EFFECT: Mutates every cell in both topRow and bottomRow so that their
  // neighbors arraylist is initialized with their correct values
  // Takes two rows of cells and connects them, functionally combining the graphs
  // together
  void stitchRows(Cell topRow, Cell bottomRow) {
    Cell currentTopCell = topRow;
    Cell currentBottomCell = bottomRow;
    for (int i = 0; i < widthInTiles; i++) {
      currentTopCell.setBottomMiddleNeighbor(currentBottomCell);
      currentBottomCell.setTopMiddleNeighbor(currentTopCell);

      if (i == 0) {
        currentTopCell.setBottomRightNeighbor(currentBottomCell.getMiddleRightNeighbor());
        currentBottomCell.setTopRightNeighbor(currentTopCell.getMiddleRightNeighbor());
      } else if (i == widthInTiles - 1) {
        currentTopCell.setBottomLeftNeighbor(currentBottomCell.getMiddleLeftNeighbor());
        currentBottomCell.setTopLeftNeighbor(currentTopCell.getMiddleLeftNeighbor());
      } else {
        currentTopCell.setBottomLeftNeighbor(currentBottomCell.getMiddleLeftNeighbor());
        currentTopCell.setBottomRightNeighbor(currentBottomCell.getMiddleRightNeighbor());
        currentBottomCell.setTopLeftNeighbor(currentTopCell.getMiddleLeftNeighbor());
        currentBottomCell.setTopRightNeighbor(currentTopCell.getMiddleRightNeighbor());
      }

      currentTopCell = currentTopCell.getMiddleRightNeighbor();
      currentBottomCell = currentBottomCell.getMiddleRightNeighbor();
    }
  }

  // EFFECT: Mutates cell so that the gameboard graph of cells is properly
  // initialized with cell being in the proverbial (0,0) position
  // Initializes the gameboard of cells
  void initializeBoard(Cell cell) {
    Cell topRow;
    Cell bottomRow;

    // Preloading the for loop by doing the one extraneous case "manually"
    // AKA when the topRow doesn't exist yet
    Cell firstCell = cell;
    Cell previousCell = firstCell;
    // Starting at one since previousCell is already taking the 0th indexed position
    // in the row
    for (int j = 1; j < widthInTiles; j++) {
      Cell newCell = new Cell();
      newCell.setMiddleLeftNeighbor(previousCell);
      previousCell.setMiddleRightNeighbor(newCell);
      previousCell = newCell;
    }
    topRow = firstCell;

    // Starting at one since the first row was already computed above
    for (int i = 1; i < heightInTiles; i++) {
      firstCell = new Cell();
      previousCell = firstCell;
      // Starting at one since previousCell is already taking the 0th indexed position
      // in the row
      for (int j = 1; j < widthInTiles; j++) {
        Cell newCell = new Cell();
        newCell.setMiddleLeftNeighbor(previousCell);
        previousCell.setMiddleRightNeighbor(newCell);
        previousCell = newCell;
      }
      bottomRow = firstCell;
      stitchRows(topRow, bottomRow);
      topRow = bottomRow;
    }
  }

  // EFFECT: Mutates the arraylist so that the element at indexA is now at indexB,
  // and that the element at indexB is now at indexA
  // Swaps the given elements positions in the list
  <T> void swap(ArrayList<T> list, final int indexA, final int indexB) {
    T temp = list.get(indexA);
    list.set(indexA, list.get(indexB));
    list.set(indexB, temp);
  }

  // EFFECT: Mutates the list so that the positions of the elements within it are changed
  // Shuffles the given list
  void fisherYatesShuffle(ArrayList<Integer> list, final int numItems) {
    Random rand = new Random();
    for (int i = 0; i <= numItems - 2; i++) {
      int j = i + rand.nextInt(numItems - i);
      swap(list, i, j);
    }
  }

  // Initializes the mines in a consistent O(N)
  // Confirmed with profiler (VisualVM)
  // EFFECT: mutates numMines random cells within the graph of cells so that hasMine is set to true
  // Randomly places mines throughout the gameboard
  void initializeMines(final int numMines) {
    int size = widthInTiles * heightInTiles;
    ArrayList<Integer> boardAsInts = new ArrayList<Integer>(size);
    for (int i = 0; i < size; i++) {
      boardAsInts.add(i);
    }
    fisherYatesShuffle(boardAsInts, size);
    for (int i = 0; i < numMines; i++) {
      int y = boardAsInts.get(i) / widthInTiles;
      int x = boardAsInts.get(i) % widthInTiles;
      initialCell.getCell(x, y).hasMine = true;
    }

    for (int i = 0; i < heightInTiles; i++) {
      for (int j = 0; j < widthInTiles; j++) {
        Cell currentCell = initialCell.getCell(j, i);
        currentCell.numOfMinesAdjacent = currentCell.numOfMinesAdjacent();
      }
    }
  }
}

// Holds different resources used through the program
final class ConstProps {
  /* Template
     *
     * Fields:
     * this.scoreboardLeftBorderSegmentWidth -- int
     * this.scoreboardRightBorderSegmentWidth -- int
     * this.scoreboardTopBorderSegmentHeight -- Cell
     * this.numberedTileWidthInPixels -- int
     * this.numberedTileHeightInPixels -- int
     * this.tileLeftBorderSegmentWidth -- int
     * this.tileRightBorderSegmentWidth -- int
     * this.heightScoreboardBackground -- int
     * this.tileBottomBorderSegmentHeight -- int
     * this.scoreboardBottomBorderSegmentHeight -- int
     * this.mineCounterWidth -- int
     * this.mineCounterHeight -- int
     * this.scoreboardTimerWidth -- int
     * this.scoreBoardTimerHeight -- int
     * this.scoreboardBackgroundColor -- Color
     * this.scoreboardBottomBorderSegment -- FromFileImage
     * this.scoreboardTopBorderSegment -- FromFileImage
     * this.scoreboardLeftBorderSegment -- FromFileImage
     * this.scoreboardRightBorderSegment -- FromFileImage
     * this.scoreboardCounterZero -- FromFileImage
     * this.scoreboardCounterOne -- FromFileImage
     * this.scoreboardCounterTwo -- FromFileImage
     * this.scoreboardCounterThree -- FromFileImage
     * this.scoreboardCounterFour -- FromFileImage
     * this.scoreboardCounterFive -- FromFileImage
     * this.scoreboardCounterSix -- FromFileImage
     * this.scoreboardCounterSeven -- FromFileImage
     * this.scoreboardCounterEight -- FromFileImage
     * this.scoreboardCounterNine -- FromFileImage
     * this.tileLeftBorderSegment -- FromFileImage
     * this.tileRightBorderSegment -- FromFileImage
     * this.tileBottomBorderLeftCorner -- FromFileImage
     * this.tileBottomBorderRightCorner -- FromFileImage
     * this.tileBottomBorderSegment -- FromFileImage
     * this.smileUnpressed -- FromFileImage
     * this.smilePressed -- FromFileImage
     * this.smileClick -- FromFileImage
     * this.smileLost -- FromFileImage
     * this.smileWon -- FromFileImage
     * this.zeroAdjMines -- FromFileImage
     * this.oneAdjMines -- FromFileImage
     * this.twoAdjMines -- FromFileImage
     * this.threeAdjMines -- FromFileImage
     * this.fourAdjMines -- FromFileImage
     * this.fiveAdjMines -- FromFileImage
     * this.sixAdjMines -- FromFileImage
     * this.sevenAdjMines -- FromFileImage
     * this.eightAdjMines -- FromFileImage
     * this.unknownAdjMines -- FromFileImage
     * this.flag -- FromFileImage
     * this.unflaggedMine -- FromFileImage
     * this.flaggedMine -- FromFileImage
     * this.detonatedMine -- FromFileImage
     * 
     * Methods:
     * this.determineImageFromCell(Cell cell) -- WorldImage
     * this.gameLostDetermineImageFromCell(Cell cell) -- WorldImage
     * 
     * Methods:
     * this.counterImages.add(int index, Cell element) -- void
     * this.counterImages.add(Cell element) -- boolean
     * this.counterImages.addAll(int index, Collection<> c) -- boolean
     * this.counterImages.addAll(Collection<> c) -- boolean
     * this.counterImages.addFirst(Cell element) -- void
     * this.counterImages.addLast(Cell element) -- void
     * this.counterImages.clear() -- void
     * this.counterImages.clone() -- Object
     * this.counterImages.contains(Object o) -- boolean
     * this.counterImages.ensureCapacity(int minCapacity) -- void
     * this.counterImages.equals(Object o) -- boolean
     * this.counterImages.forEach(Consumer<> action) -- void
     * this.counterImages.get(int index) -- Cell
     * this.counterImages.getFirst() -- Cell
     * this.counterImages.getLast() -- Cell
     * this.counterImages.hashCode() -- int
     * this.counterImages.indexOf(Object o) -- int
     * this.counterImages.isEmpty() -- boolean
     * this.counterImages.iterator() -- Iterator<Cell>
     * this.counterImages.lastIndexOf(Object o) -- int
     * this.counterImages.listIterator() -- ListIterator<Cell>
     * this.counterImages.listIterator(int index) -- ListIterator<Cell>
     * this.counterImages.remove(int index) -- Cell
     * this.counterImages.remove(Object o) -- boolean
     * this.counterImages.removeAll(Collection<> c) -- boolean
     * this.counterImages.removeFirst() -- Cell
     * this.counterImages.removeIf(Predicate<> filter) -- boolean
     * this.counterImages.removeLast() -- Cell
     * this.counterImages.removeRange(int fromIndex, int toIndex) -- List<Cell>
     * this.counterImages.toArray() -- Object[]
     * this.counterImages.toArray(T[] a) -- <T> T[]
     * this.counterImages.trimToSize() -- vcounterImages
     * 
     * this.numberedTileImages.add(int index, Cell element) -- void
     * this.numberedTileImages.add(Cell element) -- boolean
     * this.numberedTileImages.addAll(int index, Collection<> c) -- boolean
     * this.numberedTileImages.addAll(Collection<> c) -- boolean
     * this.numberedTileImages.addFirst(Cell element) -- void
     * this.numberedTileImages.addLast(Cell element) -- void
     * this.numberedTileImages.clear() -- void
     * this.numberedTileImages.clone() -- Object
     * this.numberedTileImages.contains(Object o) -- boolean
     * this.numberedTileImages.ensureCapacity(int minCapacity) -- void
     * this.numberedTileImages.equals(Object o) -- boolean
     * this.numberedTileImages.forEach(Consumer<> action) -- void
     * this.numberedTileImages.get(int index) -- Cell
     * this.numberedTileImages.getFirst() -- Cell
     * this.numberedTileImages.getLast() -- Cell
     * this.numberedTileImages.hashCode() -- int
     * this.numberedTileImages.indexOf(Object o) -- int
     * this.numberedTileImages.isEmpty() -- boolean
     * this.numberedTileImages.iterator() -- Iterator<Cell>
     * this.numberedTileImages.lastIndexOf(Object o) -- int
     * this.numberedTileImages.listIterator() -- ListIterator<Cell>
     * this.numberedTileImages.listIterator(int index) -- ListIterator<Cell>
     * this.numberedTileImages.remove(int index) -- Cell
     * this.numberedTileImages.remove(Object o) -- boolean
     * this.numberedTileImages.removeAll(Collection<> c) -- boolean
     * this.numberedTileImages.removeFirst() -- Cell
     * this.numberedTileImages.removeIf(Predicate<> filter) -- boolean
     * this.numberedTileImages.removeLast() -- Cell
     * this.numberedTileImages.removeRange(int fromIndex, int toIndex) -- List<Cell>
     * this.numberedTileImages.toArray() -- Object[]
     * this.numberedTileImages.toArray(T[] a) -- <T> T[]
     * this.numberedTileImages.trimToSize() -- void
     */

  // As determined via the profiler used (VisualVM), calling getWidth() and
  // getHeight()
  // on images was actually suprisingly expensive as an aggregate
  // Additionally, by hard coding them with their already known values
  // we no longer have to cast the result of getWidth() and getHeight() back to an
  // integer
  // Hence the reason for all of these width and height variables below
  final static int scoreboardLeftBorderSegmentWidth = 15;
  final static int scoreboardRightBorderSegmentWidth = 15;
  final static int scoreboardTopBorderSegmentHeight = 15;
  final static int numberedTileWidthInPixels = 24;
  final static int numberedTileHeightInPixels = 24;
  final static int tileLeftBorderSegmentWidth = 15;
  final static int tileRightBorderSegmentWidth = 15;
  final static int heightScoreboardBackground = 48;
  final static int tileBottomBorderSegmentHeight = 15;
  final static int scoreboardBottomBorderSegmentHeight = 15;
  final static int mineCounterWidth = 60;
  final static int mineCounterHeight = 34;
  final static int scoreboardTimerWidth = 60;
  final static int scoreBoardTimerHeight = 34;

  // The color used for the background of the scoreboard
  final static Color scoreboardBackgroundColor = new Color(192, 192, 192);

  // The images used to display the border around the scoreboard
  final static FromFileImage scoreboardBottomBorderSegment = new FromFileImage("ScoreboardBottomBorderSegment.png");
  final static FromFileImage scoreboardTopBorderSegment = new FromFileImage("ScoreboardTopBorderSegment.png");
  final static FromFileImage scoreboardLeftBorderSegment = new FromFileImage("ScoreboardLeftBorder.png");
  final static FromFileImage scoreboardRightBorderSegment = new FromFileImage("ScoreboardRightBorder.png");

  // The images used to display the numbers on the scoreboard
  // Used both for the timer and for the unflagged mine counter
  final static FromFileImage scoreboardCounterZero = new FromFileImage("Scoreboard0.png");
  final static FromFileImage scoreboardCounterOne = new FromFileImage("Scoreboard1.png");
  final static FromFileImage scoreboardCounterTwo = new FromFileImage("Scoreboard2.png");
  final static FromFileImage scoreboardCounterThree = new FromFileImage("Scoreboard3.png");
  final static FromFileImage scoreboardCounterFour = new FromFileImage("Scoreboard4.png");
  final static FromFileImage scoreboardCounterFive = new FromFileImage("Scoreboard5.png");
  final static FromFileImage scoreboardCounterSix = new FromFileImage("Scoreboard6.png");
  final static FromFileImage scoreboardCounterSeven = new FromFileImage("Scoreboard7.png");
  final static FromFileImage scoreboardCounterEight = new FromFileImage("Scoreboard8.png");
  final static FromFileImage scoreboardCounterNine = new FromFileImage("Scoreboard9.png");

  // Used to map an int to its respective image respresentation
  final static ArrayList<FromFileImage> counterImages = new ArrayList<FromFileImage>(
      Arrays.asList(scoreboardCounterZero, scoreboardCounterOne, scoreboardCounterTwo,
          scoreboardCounterThree, scoreboardCounterFour, scoreboardCounterFive, scoreboardCounterSix,
          scoreboardCounterSeven, scoreboardCounterEight, scoreboardCounterNine));

  // The images used to make up the border around the rows of tiles the player
  // interacts with
  // during gameplay
  final static FromFileImage tileLeftBorderSegment = new FromFileImage("TileLeftBorderSegment.png");
  final static FromFileImage tileRightBorderSegment = new FromFileImage("TileRightBorderSegment.png");
  final static FromFileImage tileBottomBorderLeftCorner = new FromFileImage("TileLeftBorderBottomCorner.png");
  final static FromFileImage tileBottomBorderRightCorner = new FromFileImage("TileRightBorderBottomCorner.png");
  final static FromFileImage tileBottomBorderSegment = new FromFileImage("TileBottomBorderSegment.png");

  // The images used to display the smiley face in the middle of the scoreboard
  // Depicts when the user is left clicking, has won, or has lost
  final static FromFileImage smileUnpressed = new FromFileImage("SmileUnpressed.png");
  final static FromFileImage smilePressed = new FromFileImage("SmilePressed.png");
  final static FromFileImage smileClick = new FromFileImage("SmileTilePressed.png");
  final static FromFileImage smileLost = new FromFileImage("SmileLost.png");
  final static FromFileImage smileWon = new FromFileImage("SmileWon.png");

  // The images used to reveal the number of adjacent mines for any given cell
  final static FromFileImage zeroAdjMines = new FromFileImage("0.png");
  final static FromFileImage oneAdjMines = new FromFileImage("1.png");
  final static FromFileImage twoAdjMines = new FromFileImage("2.png");
  final static FromFileImage threeAdjMines = new FromFileImage("3.png");
  final static FromFileImage fourAdjMines = new FromFileImage("4.png");
  final static FromFileImage fiveAdjMines = new FromFileImage("5.png");
  final static FromFileImage sixAdjMines = new FromFileImage("6.png");
  final static FromFileImage sevenAdjMines = new FromFileImage("7.png");
  final static FromFileImage eightAdjMines = new FromFileImage("8.png");

  // Used to map an int to its respective image respresentation
  final static ArrayList<FromFileImage> numberedTileImages = new ArrayList<FromFileImage>(
      Arrays.asList(zeroAdjMines, oneAdjMines, twoAdjMines, threeAdjMines, fourAdjMines, fiveAdjMines,
          sixAdjMines, sevenAdjMines, eightAdjMines));

  // The default image displayed across the game board
  final static FromFileImage unknownAdjMines = new FromFileImage("Unknown.png");

  // The image used to display a flag
  final static FromFileImage flag = new FromFileImage("Flag.png");

  // The image used to display unflagged mines once the game has been lost
  final static FromFileImage unflaggedMine = new FromFileImage("MineUnflagged.png");

  // The image used to display flagged mines once the game has been lost
  final static FromFileImage flaggedMine = new FromFileImage("MineFlagged.png");

  // The image used to display the mine the player hit when they lost the game
  final static FromFileImage detonatedMine = new FromFileImage("Detonated.png");

  // Returns the appropriate image to display based upon the properties of the given cell
  static WorldImage determineImageFromCell(Cell cell) {
    if (cell.revealed) {
      if (cell.hasMine == false) {
        return numberedTileImages.get(cell.numOfMinesAdjacent);
      } else {
        return detonatedMine;
      }
    } else {
      if (cell.flagged) {
        return flag;
      } else {
        return unknownAdjMines;
      }
    }
  }

  // Returns the appropriate image to display based upon the properties of the given cell
  // Specifically when displaying the final screen once the game has been lost
  static WorldImage gameLostDetermineImageFromCell(Cell cell) {
    if (cell.hasMine) {
      if (cell.revealed == false) {
        if (cell.flagged) {
          return flaggedMine;
        } else {
          return unflaggedMine;
        }
      } else {
        return detonatedMine;
      }
    } else if (cell.revealed == false) {
      if (cell.flagged) {
        return flag;
      } else {
        return unknownAdjMines;
      }
    } else {
      return numberedTileImages.get(cell.numOfMinesAdjacent);
    }
  }
}

// TODO Maybe remove this?
class MouseClickException extends Exception {
  public MouseClickException() {
    super();
  }

  public MouseClickException(String errorMessage) {
    super(errorMessage);
  }
}

// The data structure holding all the state for the game
class MineSweeperWorld extends World {
  /* Template
     *
     * Fields:
     * this.board -- Board
     * this.widthInTiles -- int
     * this.widthInPixels -- int
     * this.heightInTiles -- int
     * this.heightInPixels -- int
     * this.numMines -- int
     * this.tickRate -- double
     * this.timer -- double
     * this.displayedTime -- int
     * this.timeStart -- boolean
     * this.numberFlagged -- boolean
     * this.mouseClicked -- boolean
     * this.worldScene -- WorldScene
     * this.hitMine -- boolean
     * this.pow -- int
     * 
     * Methods:
     * this.makeScene() -- WorldScene
     * this.initializeWorldScene() -- WorldScene
     * this.createScoreboardTopBorder() -- BesideImage
     * this.createScoreboardBottomBorder() -- BesideImage
     * this.getNthDigit() -- int
     * this.createScoreboardMineCounter() -- BesideImage
     * this.createScoreboardTimer() -- BesideImage
     * this.createScoreboard() -- BesideImage
     * this.createLostScoreboard() -- BesideImage
     * this.createTileRows() -- AboveImage
     * this.createLostTileRows() -- AboveImage
     * this.createBottomBorder() -- BesideImage
     * this.createGameBoardImage() -- AboveImage
     * this.createLostGameBoardImage() -- AboveImage
     * this.onTick() -- void
     * this.floodFill(Cell cell, int tileX, int tileY) -- void
     * this.onMousePressed(Posn pos, String buttonPressed) -- void
     * this.onMouseReleased(Posn pos, String buttonPressed) -- void
     * this.onMouseClicked(Posn pos, String buttonPressed) -- void
     * this.gameWon(Cell cell, HashSet<Cell> visited) -- boolean
     * this.gameLost() -- boolean
     * this.worldEnds() -- WorldEnd
     * 
     * Methods on fields:
     * 
     */

  // The graph of cells that make up the game board
  Board board;

  // The number of tiles wide the gameboard is
  int widthInTiles;

  // The number of pixels wide the worldScene is
  int widthInPixels;

  // The number of tiles high the gameboard is
  int heightInTiles;

  // The number of pixels high the worldScene is
  int heightInPixels;

  // The number of mines in this current game of minesweeper
  int numMines;

  // The tickrate of the currently running game
  double tickRate;

  // A timer that determines when a second in real time has passed
  double timer;

  // The time displayed within the game
  int displayedTime;

  // Determines when timer starts counting, and ergo when the displayed time within the game
  // displayedTime starts counting
  boolean timeStart;

  // Holds the number of flags placed by the player
  int numberFlagged;

  // Holds whether the player holding down the left mouse button or not
  boolean mouseClicked;

  // The scene displayed on screen while the game is running
  WorldScene worldScene;

  // Holds whether a player has hit a mine or not
  boolean hitMine;

  MineSweeperWorld(int widthInTiles, int widthInPixels, int heightInTiles, int heightInPixels, int numMines,
      double tickRate) {
    this.widthInTiles = widthInTiles;
    this.widthInPixels = widthInPixels;
    this.heightInTiles = heightInTiles;
    this.heightInPixels = heightInPixels;
    this.hitMine = false;
    this.numMines = numMines;
    this.mouseClicked = false;
    this.tickRate = tickRate;
    this.timer = 0.0;
    this.displayedTime = 0;
    this.numberFlagged = 0;

    this.board = new Board(widthInTiles, heightInTiles, numMines);
    this.worldScene = initializeWorldScene();
  }

  // Originally the images drawn to the screen were created in here and everything was drawn
  // every tick using loops where the (x,y) position of any given cell was never directly dealt with
  // This was incredibly slow.
  // For reference, the timer on screen took on average 2.637 times longer to tick up
  // compared to the implementation currently being used (10 trials, Std = 0.225588)
  // Additionally, according to the profiler I used (VisualVM) >99% of cpu time
  // was spent creating the images to draw to the screen
  // Currently, the WorldScene is initialized and ended using the original method
  // (since it only needs to be done once), and every change after the initial screen is using
  // the new method where the WorldScene is updated via mutation changing only what is actually
  // changing rather than drawing every image on the screen over and over again
  @Override
  public WorldScene makeScene() {
    return worldScene;
  }

  // EFFECT: Mutates newScene to contain the initial game starting screen
  // Initializes worldScene
  public WorldScene initializeWorldScene() {
    WorldScene newScene = new WorldScene(widthInPixels, heightInPixels);
    newScene.placeImageXY(
        new RectangleImage(widthInPixels, heightInPixels, OutlineMode.SOLID, new Color(255, 255, 255)),
        widthInPixels / 2, heightInPixels / 2);
    newScene.placeImageXY(createGameBoardImage(),
        widthInPixels / 2,
        heightInPixels / 2);
    return newScene;
  }

  // Creates the top border of the scoreboard
  BesideImage createScoreboardTopBorder() {
    BesideImage scoreBoardTopBorder;
    scoreBoardTopBorder = new BesideImage(ConstProps.scoreboardTopBorderSegment,
        ConstProps.scoreboardTopBorderSegment);
    for (int j = 2; j < widthInTiles; j++) {
      scoreBoardTopBorder = new BesideImage(scoreBoardTopBorder, ConstProps.scoreboardTopBorderSegment);
    }
    return scoreBoardTopBorder;
  }

  // Creates the bottom border of the scoreboard
  BesideImage createScoreboardBottomBorder() {
    BesideImage scoreboardBottomBorder;
    scoreboardBottomBorder = new BesideImage(ConstProps.scoreboardBottomBorderSegment,
        ConstProps.scoreboardBottomBorderSegment);
    for (int j = 2; j < widthInTiles; j++) {
      scoreboardBottomBorder = new BesideImage(scoreboardBottomBorder, ConstProps.scoreboardBottomBorderSegment);
    }
    return scoreboardBottomBorder;
  }

  // Using this so I don't have to cast the result of Math.pow back to an int
  // Calculates the result of num to the power of exp
  int pow(int num, int exp) {
    if (exp > 0) {
      return num * pow(num, exp - 1);
    } else {
      return 1;
    }
  }

  // Returns the number within the nthPos of num
  // where the nthPos starts from the back and 1 rather than 0
  // Ex. the result of getNthDigit(123, 3) is 3
  // Ex. the result of getNthDigit(123, 1) is 1
  int getNthDigit(int num, int nthPos) {
    return (num / pow(10, nthPos - 1)) % 10;
  }

  // Creates the counter on the left side of the scoreboard that keeps track of the number of
  // unflagged mines that are left on the board
  BesideImage createScoreboardMineCounter() {
    // In reverse order
    WorldImage firstDigit = ConstProps.counterImages.get(getNthDigit(numMines - numberFlagged, 3));
    WorldImage secondDigit = ConstProps.counterImages.get(getNthDigit(numMines - numberFlagged, 2));
    WorldImage thirdDigit = ConstProps.counterImages.get(getNthDigit(numMines - numberFlagged, 1));
    return new BesideImage(firstDigit, secondDigit, thirdDigit);
  }

  // Creates the counter on the right side of the scoreboard that keeps track of
  // the amount of time the player has spent playing
  BesideImage createScoreboardTimer() {
    // In reverse order
    WorldImage firstDigit = ConstProps.counterImages.get(getNthDigit(displayedTime, 3));
    WorldImage secondDigit = ConstProps.counterImages.get(getNthDigit(displayedTime, 2));
    WorldImage thirdDigit = ConstProps.counterImages.get(getNthDigit(displayedTime, 1));
    return new BesideImage(firstDigit, secondDigit, thirdDigit);
  }

  // Creates the entirety of the scoreboard
  BesideImage createScoreboard() {
    RectangleImage scoreboardBackground = new RectangleImage((widthInTiles * ConstProps.numberedTileWidthInPixels),
        ConstProps.heightScoreboardBackground, OutlineMode.SOLID,
        ConstProps.scoreboardBackgroundColor);
    // TODO can probably remove this ternary now
    OverlayImage smile = mouseClicked ? new OverlayImage(ConstProps.smileClick, scoreboardBackground)
        : new OverlayImage(ConstProps.smileUnpressed, scoreboardBackground);
    BesideImage mineCounter = createScoreboardMineCounter();
    OverlayOffsetImage mineCounterWithSmile = new OverlayOffsetImage(mineCounter,
        ((widthInTiles * ConstProps.numberedTileWidthInPixels) / 2.0) - (ConstProps.mineCounterWidth / 2.0) - 9, 0,
        smile);
    BesideImage timer = createScoreboardTimer();
    OverlayOffsetImage mineCounterWithSmileAndTimer = new OverlayOffsetImage(timer,
        -(((widthInTiles * ConstProps.numberedTileWidthInPixels) / 2.0) - (ConstProps.mineCounterWidth / 2.0) - 9), 0,
        mineCounterWithSmile);
    BesideImage scoreBoard = new BesideImage(ConstProps.scoreboardLeftBorderSegment,
        new AboveImage(createScoreboardTopBorder(), mineCounterWithSmileAndTimer, createScoreboardBottomBorder()),
        ConstProps.scoreboardRightBorderSegment);
    return scoreBoard;
  }

  // Creates the scoreboard shown when the game is lost
  BesideImage createLostScoreboard() {
    RectangleImage scoreboardBackground = new RectangleImage((widthInTiles * ConstProps.numberedTileWidthInPixels),
        ConstProps.heightScoreboardBackground, OutlineMode.SOLID,
        ConstProps.scoreboardBackgroundColor);
    OverlayImage smile = new OverlayImage(ConstProps.smileLost, scoreboardBackground);
    BesideImage mineCounter = createScoreboardMineCounter();
    OverlayOffsetImage mineCounterWithSmile = new OverlayOffsetImage(mineCounter,
        ((widthInTiles * ConstProps.numberedTileWidthInPixels) / 2.0) - (ConstProps.mineCounterWidth / 2.0) - 9, 0,
        smile);
    BesideImage timer = createScoreboardTimer();
    OverlayOffsetImage mineCounterWithSmileAndTimer = new OverlayOffsetImage(timer,
        -(((widthInTiles * ConstProps.numberedTileWidthInPixels) / 2.0) - (ConstProps.mineCounterWidth / 2.0) - 9), 0,
        mineCounterWithSmile);
    BesideImage scoreBoard = new BesideImage(ConstProps.scoreboardLeftBorderSegment,
        new AboveImage(createScoreboardTopBorder(), mineCounterWithSmileAndTimer, createScoreboardBottomBorder()),
        ConstProps.scoreboardRightBorderSegment);
    return scoreBoard;
  }

  // Creates the initial gameboard of tiles without the bottom border
  AboveImage createTileRows() {
    Cell currentRowCell = board.initialCell;
    Cell currentColumnCell = board.initialCell;
    AboveImage tileRows = new AboveImage(createScoreboard());
    for (int i = 0; i < heightInTiles; i++) {
      BesideImage tileRow = new BesideImage(ConstProps.determineImageFromCell(currentColumnCell));
      for (int j = 1; j < widthInTiles; j++) {
        currentColumnCell = currentColumnCell.getMiddleRightNeighbor();
        tileRow = new BesideImage(tileRow, ConstProps.determineImageFromCell(currentColumnCell));
      }
      tileRow = new BesideImage(ConstProps.tileLeftBorderSegment, tileRow, ConstProps.tileRightBorderSegment);
      tileRows = new AboveImage(tileRows, tileRow);
      currentRowCell = currentRowCell.getBottomMiddleNeighbor();
      currentColumnCell = currentRowCell;
    }
    return tileRows;
  }

  // Creates the gameboard of tiles shown when the player has lost the game
  // Without the bottom border
  AboveImage createLostTileRows() {
    Cell currentRowCell = board.initialCell;
    Cell currentColumnCell = board.initialCell;
    AboveImage tileRows = new AboveImage(createLostScoreboard());
    for (int i = 0; i < heightInTiles; i++) {
      BesideImage tileRow = new BesideImage(ConstProps.gameLostDetermineImageFromCell(currentColumnCell));
      for (int j = 1; j < widthInTiles; j++) {
        currentColumnCell = currentColumnCell.getMiddleRightNeighbor();
        tileRow = new BesideImage(tileRow, ConstProps.gameLostDetermineImageFromCell(currentColumnCell));
      }
      tileRow = new BesideImage(ConstProps.tileLeftBorderSegment, tileRow, ConstProps.tileRightBorderSegment);
      tileRows = new AboveImage(tileRows, tileRow);
      currentRowCell = currentRowCell.getBottomMiddleNeighbor();
      currentColumnCell = currentRowCell;
    }
    return tileRows;
  }

  // Creates the bottom border of the gameboard of tiles
  BesideImage createBottomBorder() {
    BesideImage lastRow = new BesideImage(ConstProps.tileBottomBorderSegment);
    for (int i = 1; i < widthInTiles; i++) {
      lastRow = new BesideImage(lastRow, ConstProps.tileBottomBorderSegment);
    }
    lastRow = new BesideImage(ConstProps.tileBottomBorderLeftCorner, lastRow, ConstProps.tileBottomBorderRightCorner);
    return lastRow;
  }

  // Creates the entirety of the gameboard, border and all
  AboveImage createGameBoardImage() {
    return new AboveImage(createTileRows(), createBottomBorder());
  }

  // Creates the entirety of the gameboard shown when the game is lost, border and all
  AboveImage createLostGameBoardImage() {
    return new AboveImage(createLostTileRows(), createBottomBorder());
  }

  // Performs an action every tick
  // Specifically updates the timer and the timer displayed in game
  // EFFECT: Mutates worldScene so that the timer displayed matches the amount of real life seconds passed since the game started
  // EFFECT: Mutates displayedTime to match the amount of real life seconds passed since the game started
  // EFFECT: Mutates timer so that the amount of real time passed since the last tick is added to it
  // EFFECT: Mutates timer back to 0 when more than a second of real time has passed
  @Override
  public void onTick() {
    if (timeStart) {
      timer = timer + tickRate;
      if (timer / (1 / tickRate) >= 1) {
        displayedTime = displayedTime + 1;
        worldScene.placeImageXY(createScoreboardTimer(),
            widthInPixels - ConstProps.scoreboardRightBorderSegmentWidth - 9 - (ConstProps.scoreboardTimerWidth / 2),
            (7 + ConstProps.scoreboardTopBorderSegmentHeight) + (ConstProps.scoreBoardTimerHeight / 2));
        timer = 0.0;
      }
    }
  }

  // Reveals all the tiles without adjacent mines and a outer ring of cells that do
  // whenever the player clicks a cell
  // EFFECT: Mutates topCell.revealed to true if topCell does not have a mine and has not been revealed yet
  // EFFECT: Mutates bottomCell.revealed to true if bottomCell does not have a mine and has not been revealed yet
  // EFFECT: Mutates leftCell.revealed to true if leftCell does not have a mine and has not been revealed yet
  // EFFECT: Mutates rightCell.revealed to true if rightCell does not have a mine and has not been revealed yet
  // EFFECT: Mutates topCell.flagged to false topCell was flagged, does not have a mine, and has not been revealed yet
  // EFFECT: Mutates bottomCell.flagged to false bottomCell was flagged, does not have a mine, and has not been revealed yet
  // EFFECT: Mutates leftCell.flagged to false leftCell was flagged, does not have a mine, and has not been revealed yet
  // EFFECT: Mutates rightCell.flagged to false rightCell was flagged, does not have a mine, and has not been revealed yet
  // EFFECT: Mutates numberFlagged to numberFlagged - 1 if any of the following list were flagged, did not have a mine, and had not been revealed: topCell, bottomCell, leftCell, rightCell
  // EFFECT: Mutates worldScene to have the appropriate image displayed depending on whether topCell, bottomCell, leftCell, rightCell, or numberFlagged was mutated
  public void floodFill(Cell cell, int tileX, int tileY) {
    Cell topCell = cell.getTopMiddleNeighbor();
    Cell bottomCell = cell.getBottomMiddleNeighbor();
    Cell leftCell = cell.getMiddleLeftNeighbor();
    Cell rightCell = cell.getMiddleRightNeighbor();
    if (topCell != null) {
      if (topCell.hasMine == false && !topCell.revealed) {
        topCell.revealed = true;
        if (topCell.flagged) {
          topCell.flagged = false;
          numberFlagged--;
          worldScene.placeImageXY(createScoreboardMineCounter(),
              (9 + ConstProps.scoreboardLeftBorderSegmentWidth) + (ConstProps.scoreboardTimerWidth / 2),
              (7 + ConstProps.scoreboardTopBorderSegmentHeight) + (ConstProps.scoreBoardTimerHeight / 2));
        }
        worldScene.placeImageXY(ConstProps.determineImageFromCell(topCell),
            ConstProps.tileLeftBorderSegmentWidth + (tileX * ConstProps.numberedTileWidthInPixels)
                + (ConstProps.numberedTileWidthInPixels / 2),
            ConstProps.scoreboardTopBorderSegmentHeight + ConstProps.heightScoreboardBackground
                + ConstProps.scoreboardBottomBorderSegmentHeight
                + ((tileY - 1) * ConstProps.numberedTileHeightInPixels)
                + (ConstProps.numberedTileHeightInPixels / 2));
        if (topCell.numOfMinesAdjacent == 0) {
          floodFill(topCell, tileX, tileY - 1);
        }
      }
    }
    if (bottomCell != null) {
      if (bottomCell.hasMine == false && !bottomCell.revealed) {
        bottomCell.revealed = true;
        if (bottomCell.flagged) {
          bottomCell.flagged = false;
          numberFlagged--;
          worldScene.placeImageXY(createScoreboardMineCounter(),
              (9 + ConstProps.scoreboardLeftBorderSegmentWidth) + (ConstProps.scoreboardTimerWidth / 2),
              (7 + ConstProps.scoreboardTopBorderSegmentHeight) + (ConstProps.scoreBoardTimerHeight / 2));
        }
        worldScene.placeImageXY(ConstProps.determineImageFromCell(bottomCell),
            ConstProps.tileLeftBorderSegmentWidth + (tileX * ConstProps.numberedTileWidthInPixels)
                + (ConstProps.numberedTileWidthInPixels / 2),
            ConstProps.scoreboardTopBorderSegmentHeight + ConstProps.heightScoreboardBackground
                + ConstProps.scoreboardBottomBorderSegmentHeight
                + ((tileY + 1) * ConstProps.numberedTileHeightInPixels)
                + (ConstProps.numberedTileHeightInPixels / 2));
        if (bottomCell.numOfMinesAdjacent == 0) {
          floodFill(bottomCell, tileX, tileY + 1);
        }
      }
    }
    if (leftCell != null) {
      if (leftCell.hasMine == false && !leftCell.revealed) {
        leftCell.revealed = true;
        if (leftCell.flagged) {
          leftCell.flagged = false;
          numberFlagged--;
          worldScene.placeImageXY(createScoreboardMineCounter(),
              (9 + ConstProps.scoreboardLeftBorderSegmentWidth) + (ConstProps.scoreboardTimerWidth / 2),
              (7 + ConstProps.scoreboardTopBorderSegmentHeight) + (ConstProps.scoreBoardTimerHeight / 2));
        }
        worldScene.placeImageXY(ConstProps.determineImageFromCell(leftCell),
            ConstProps.tileLeftBorderSegmentWidth + ((tileX - 1) * ConstProps.numberedTileWidthInPixels)
                + (ConstProps.numberedTileWidthInPixels / 2),
            ConstProps.scoreboardTopBorderSegmentHeight + ConstProps.heightScoreboardBackground
                + ConstProps.scoreboardBottomBorderSegmentHeight + (tileY * ConstProps.numberedTileHeightInPixels)
                + (ConstProps.numberedTileHeightInPixels / 2));
        if (leftCell.numOfMinesAdjacent == 0) {
          floodFill(leftCell, tileX - 1, tileY);
        }
      }
    }
    if (rightCell != null) {
      if (rightCell.hasMine == false && !rightCell.revealed) {
        rightCell.revealed = true;
        if (rightCell.flagged) {
          rightCell.flagged = false;
          numberFlagged--;
          worldScene.placeImageXY(createScoreboardMineCounter(),
              (9 + ConstProps.scoreboardLeftBorderSegmentWidth) + (ConstProps.scoreboardTimerWidth / 2),
              (7 + ConstProps.scoreboardTopBorderSegmentHeight) + (ConstProps.scoreBoardTimerHeight / 2));
        }
        worldScene.placeImageXY(ConstProps.determineImageFromCell(rightCell),
            ConstProps.tileLeftBorderSegmentWidth + ((tileX + 1) * ConstProps.numberedTileWidthInPixels)
                + (ConstProps.numberedTileWidthInPixels / 2),
            ConstProps.scoreboardTopBorderSegmentHeight + ConstProps.heightScoreboardBackground
                + ConstProps.scoreboardBottomBorderSegmentHeight + (tileY * ConstProps.numberedTileHeightInPixels)
                + (ConstProps.numberedTileHeightInPixels / 2));
        if (rightCell.numOfMinesAdjacent == 0) {
          floodFill(rightCell, tileX + 1, tileY);
        }
      }
    }
  }

  // Displays the worried smile face when the player is left clicking
  // EFFECT: Mutates worldScene so that smileClick is being displayed in the game
  @Override
  public void onMousePressed(Posn pos, String buttonPressed) {
    if (buttonPressed.equals("LeftButton")) {
      worldScene.placeImageXY(ConstProps.smileClick,
          ConstProps.tileLeftBorderSegmentWidth + ((widthInTiles * ConstProps.numberedTileWidthInPixels) / 2),
          ConstProps.scoreboardTopBorderSegmentHeight + (ConstProps.heightScoreboardBackground / 2));
      mouseClicked = true;
    }
  }

  // Displays the regular smile face when the player is not left clicking
  // EFFECT: Mutates worldScene so that smileUnpressed is displayed in the game
  @Override
  public void onMouseReleased(Posn pos, String buttonPressed) {
    if (buttonPressed.equals("LeftButton")) {
      worldScene.placeImageXY(ConstProps.smileUnpressed,
          ConstProps.tileLeftBorderSegmentWidth + ((widthInTiles * ConstProps.numberedTileWidthInPixels) / 2),
          ConstProps.scoreboardTopBorderSegmentHeight + (ConstProps.heightScoreboardBackground / 2));
      mouseClicked = false;
    }
  }

  // Displays the cell clicked on by the player
  // EFFECT: Mutates timeStart to true if it is false
  // EFFECT: Mutates clickedCell.revealed to true if buttonPressed equals "LeftButton"
  // EFFECT: Mutates worldScene to display the appropriate tile depending on clickedCell's properties if buttonPressed equals "LeftButton"
  // EFFECT: Mutates other cells within the graph of cells making up the gameboard depending on the results of floodfill() if buttonPressed equals "LeftButton"
  // EFFECT: Mutates hitMine to true if clickCell.hasMine is true and buttonPressed equals "LeftButton"
  // EFFECT: Mutates clickedCell.flagged to true if clickedCell.flagged is false and buttonPressed equals "RightButton"
  // EFFECT: Mutates clickedCell.flagged to false if clickedCell.flagged is true and buttonPressed equals "RightButton"
  // EFFECT: Mutates numberFlagged to numberFlagged - 1 if clickedCell.flagged is true and buttonPressed equals "RightButton"
  // EFFECT: Mutates numberFlagged to numberFlagged + 1 if clickedCell.flagged is false and buttonPressed equals "RightButton"
  @Override
  public void onMouseClicked(Posn pos, String buttonPressed) {
    int tileX = (pos.x - ConstProps.tileLeftBorderSegmentWidth) / ConstProps.numberedTileWidthInPixels;
    int tileY = (pos.y - ConstProps.heightScoreboardBackground
        - ConstProps.scoreboardTopBorderSegmentHeight - ConstProps.scoreboardBottomBorderSegmentHeight)
        / ConstProps.numberedTileHeightInPixels;
    Cell clickedCell = this.board.initialCell.getCell(tileX, tileY);
    if (buttonPressed.equals("LeftButton")) {
      if (timeStart == false) {
        timeStart = true;
      }
      if (clickedCell.revealed || clickedCell.flagged) {
        return;
      } else if (clickedCell.hasMine == false) {
        clickedCell.revealed = true;
        worldScene.placeImageXY(ConstProps.determineImageFromCell(clickedCell),
            ConstProps.tileLeftBorderSegmentWidth + (tileX * ConstProps.numberedTileWidthInPixels)
                + (ConstProps.numberedTileWidthInPixels / 2),
            ConstProps.scoreboardTopBorderSegmentHeight + ConstProps.heightScoreboardBackground
                + ConstProps.scoreboardBottomBorderSegmentHeight + (tileY * ConstProps.numberedTileHeightInPixels)
                + (ConstProps.numberedTileHeightInPixels / 2));
        floodFill(clickedCell, tileX, tileY);
      } else if (clickedCell.hasMine) {
        clickedCell.revealed = true;
        hitMine = true;
      } else {
        try {
          throw new MouseClickException("Invalid data in cell clicked");
        } catch (MouseClickException e) {
          e.printStackTrace();
          System.exit(1);
        }
      }
    } else if (buttonPressed.equals("RightButton")) {
      if (!clickedCell.revealed) {
        if (clickedCell.flagged) {
          clickedCell.flagged = false;
          numberFlagged--;
          worldScene.placeImageXY(ConstProps.determineImageFromCell(clickedCell),
              ConstProps.tileLeftBorderSegmentWidth + (tileX * ConstProps.numberedTileWidthInPixels)
                  + (ConstProps.numberedTileWidthInPixels / 2),
              ConstProps.scoreboardTopBorderSegmentHeight + ConstProps.heightScoreboardBackground
                  + ConstProps.scoreboardBottomBorderSegmentHeight + (tileY * ConstProps.numberedTileHeightInPixels)
                  + (ConstProps.numberedTileHeightInPixels / 2));
          worldScene.placeImageXY(createScoreboardMineCounter(),
              (9 + ConstProps.scoreboardLeftBorderSegmentWidth) + (ConstProps.scoreboardTimerWidth / 2),
              (7 + ConstProps.scoreboardTopBorderSegmentHeight) + (ConstProps.scoreBoardTimerHeight / 2));
        } else {
          if ((numMines - numberFlagged) > 0) {
            clickedCell.flagged = true;
            numberFlagged++;
            worldScene.placeImageXY(ConstProps.determineImageFromCell(clickedCell),
                ConstProps.tileLeftBorderSegmentWidth + (tileX * ConstProps.numberedTileWidthInPixels)
                    + (ConstProps.numberedTileWidthInPixels / 2),
                ConstProps.scoreboardTopBorderSegmentHeight + ConstProps.heightScoreboardBackground
                    + ConstProps.scoreboardBottomBorderSegmentHeight + (tileY * ConstProps.numberedTileHeightInPixels)
                    + (ConstProps.numberedTileHeightInPixels / 2));
            worldScene.placeImageXY(createScoreboardMineCounter(),
                (9 + ConstProps.scoreboardLeftBorderSegmentWidth) + (ConstProps.scoreboardTimerWidth / 2),
                (7 + ConstProps.scoreboardTopBorderSegmentHeight) + (ConstProps.scoreBoardTimerHeight / 2));
          }
        }
      }
    }
  }

  // Basically trying at any available moment to short circuit the boolean
  // expression
  // and stop any code execution that isn't necessary

  // Determines if the game has been won by the player or not
  // EFFECT: Mutates result to (result AND gameWon(neighbor, visited)) if neighbor is not null, not contained in visited, not revealed, and does not contain a mine
  // EFFECT: Mutates visited to have neighbor added to it if neighbor is not null, not contained in visited, not revealed, and does not contain a mine
  public boolean gameWon(Cell cell, HashSet<Cell> visited) {
    boolean result = true;
    if (!cell.revealed && !cell.hasMine) {
      return false;
    }
    for (Cell neighbor : cell.neighbors) {
      if (neighbor != null) {
        if (!visited.contains(neighbor)) {
          if (!neighbor.revealed && !neighbor.hasMine) {
            return false;
          }
          visited.add(neighbor);
          result = result && gameWon(neighbor, visited);
          if (result == false) {
            return false;
          }
        }
      }
    }
    return result;
  }

  // Determines if the game has been lost by the player
  public boolean gameLost() {
    return hitMine || displayedTime >= 999;
  }

  // Draws the final scene to the screen when the player either wins or loses the game
  // EFFECT: Mutates timeStart to false if the game is lost
  // EFFECT: Mutates worldScene to display the losing game board if the game is lost
  // EFFECT: Mutates worldScene to display the winning game board if the game is won
  @Override
  public WorldEnd worldEnds() {
    if (gameLost()) {
      timeStart = false;
      worldScene.placeImageXY(createLostGameBoardImage(),
          widthInPixels / 2, heightInPixels / 2);
      return new WorldEnd(true, worldScene);
    } else if (gameWon(board.initialCell, new HashSet<Cell>())) {
      worldScene.placeImageXY(ConstProps.smileWon,
          ConstProps.tileLeftBorderSegmentWidth + ((widthInTiles * ConstProps.numberedTileWidthInPixels) / 2),
          ConstProps.scoreboardTopBorderSegmentHeight + (ConstProps.heightScoreboardBackground / 2));
      return new WorldEnd(true, worldScene);
    } else {
      return new WorldEnd(false, this.makeScene());
    }
  }
}

// Starts and runs the game of MineSweeper. Validates the starting configuration of the game
// to ensure they are valid before starting it
public class MineSweeper {
  /* Template
     *
     * Fields:
     * 
     * Methods:
     * this.runGame(int tileWidth, int tileHeight, int numMines) -- void
     * 
     * Methods on fields:
     * 
     */

  static void runGame(int tileWidth, int tileHeight, int numMines) {
    if (tileWidth < 8) {
      System.out.println("Cannot run a game with a tile width smaller than 8");
      System.exit(1);
    } else if (numMines - 1 > tileHeight * tileWidth) {
      System.out.println("Cannot run a game with more mines than tiles to fit them");
      System.exit(2);
    } else if (numMines < 1) {
      System.out.println("Cannot run a game with 0 or less mines");
      System.exit(3);
    } else if (tileHeight < 1) {
      System.out.println("Cannot run a game with 0 or less height");
      System.exit(4);
    } else if (numMines > 999) {
      System.out.println("Cannot run a game with more than 999 mines");
      System.exit(5);
    }

    int pixelWidth = (tileWidth * ConstProps.numberedTileWidthInPixels) + ConstProps.tileLeftBorderSegmentWidth
        + ConstProps.tileRightBorderSegmentWidth;
    int pixelHeight = (tileHeight * ConstProps.numberedTileHeightInPixels)
        + ConstProps.tileBottomBorderSegmentHeight + ConstProps.scoreboardBottomBorderSegmentHeight
        + ConstProps.heightScoreboardBackground + ConstProps.scoreboardTopBorderSegmentHeight;
    double tickRate = 0.1;
    MineSweeperWorld gameWorld = new MineSweeperWorld(tileWidth, pixelWidth, tileHeight, pixelHeight, numMines,
        tickRate);

    gameWorld.bigBang(pixelWidth, pixelHeight, tickRate);
  }

  public static void main(String[] args) {
    runGame(9, 9, 10);
  }
}

class TestMineSweeper {
  void testCellGetTopMiddleNeighbor(Tester t) {
    Cell testCell = new Cell();
    Cell testCellAbove = new Cell();
    testCell.setTopMiddleNeighbor(testCellAbove);
    t.checkExpect(testCell.getTopMiddleNeighbor(), testCellAbove);

    Cell testCell2 = new Cell();
    Cell testCell2Above = new Cell();
    testCell2.neighbors.set(1, testCell2Above);
    t.checkExpect(testCell2.getTopMiddleNeighbor(), testCell2Above);
  }

  void testCellSetTopMiddleNeighbor(Tester t) {
    Cell testCell = new Cell();
    Cell testCellAbove = new Cell();
    testCell.setTopMiddleNeighbor(testCellAbove);
    t.checkExpect(testCell.neighbors.get(1), testCellAbove);
  }

  void testCellGetBottomMiddleNeighbor(Tester t) {
    Cell testCell = new Cell();
    Cell testCellBelow = new Cell();
    testCell.setBottomMiddleNeighbor(testCellBelow);
    t.checkExpect(testCell.getBottomMiddleNeighbor(), testCellBelow);

    Cell testCell2 = new Cell();
    Cell testCell2Below = new Cell();
    testCell2.neighbors.set(6, testCell2Below);
    t.checkExpect(testCell2.getBottomMiddleNeighbor(), testCell2Below);
  }
  
  void testCellSetBottomMiddleNeighbor(Tester t) {
    Cell testCell = new Cell();
    Cell testCellBelow = new Cell();
    testCell.setBottomMiddleNeighbor(testCellBelow);
    t.checkExpect(testCell.neighbors.get(6), testCellBelow);
  }

  void testCellGetMiddleLeftNeighbor(Tester t) {
    Cell testCell = new Cell();
    Cell testCellLeft = new Cell();
    testCell.setMiddleLeftNeighbor(testCellLeft);
    t.checkExpect(testCell.getMiddleLeftNeighbor(), testCellLeft);

    Cell testCell2 = new Cell();
    Cell testCell2Left = new Cell();
    testCell2.neighbors.set(3, testCell2Left);
    t.checkExpect(testCell2.getMiddleLeftNeighbor(), testCell2Left);
  }
  
  void testCellSetMiddleLeftNeighbor(Tester t) {
    Cell testCell = new Cell();
    Cell testCellLeft = new Cell();
    testCell.setMiddleLeftNeighbor(testCellLeft);
    t.checkExpect(testCell.neighbors.get(3), testCellLeft);
  }

  void testCellGetMiddleRightNeighbor(Tester t) {
    Cell testCell = new Cell();
    Cell testCellRight = new Cell();
    testCell.setMiddleRightNeighbor(testCellRight);
    t.checkExpect(testCell.getMiddleRightNeighbor(), testCellRight);

    Cell testCell2 = new Cell();
    Cell testCell2Right = new Cell();
    testCell2.neighbors.set(4, testCell2Right);
    t.checkExpect(testCell2.getMiddleRightNeighbor(), testCell2Right);
  }
  
  void testCellSetMiddleRightNeighbor(Tester t) {
    Cell testCell = new Cell();
    Cell testCellRight = new Cell();
    testCell.setMiddleRightNeighbor(testCellRight);
    t.checkExpect(testCell.neighbors.get(4), testCellRight);
  }

  void testCellGetTopLeftNeighbor(Tester t) {
    Cell testCell = new Cell();
    Cell testCellAboveLeft = new Cell();
    testCell.setTopLeftNeighbor(testCellAboveLeft);
    t.checkExpect(testCell.getTopLeftNeighbor(), testCellAboveLeft);

    Cell testCell2 = new Cell();
    Cell testCell2AboveLeft = new Cell();
    testCell2.neighbors.set(0, testCell2AboveLeft);
    t.checkExpect(testCell2.getTopLeftNeighbor(), testCell2AboveLeft);
  }
  
  void testCellSetTopLeftNeighbor(Tester t) {
    Cell testCell = new Cell();
    Cell testCellAboveLeft = new Cell();
    testCell.setTopLeftNeighbor(testCellAboveLeft);
    t.checkExpect(testCell.neighbors.get(0), testCellAboveLeft);
  }

  void testCellGetTopRightNeighbor(Tester t) {
    Cell testCell = new Cell();
    Cell testCellAboveRight = new Cell();
    testCell.setTopRightNeighbor(testCellAboveRight);
    t.checkExpect(testCell.getTopRightNeighbor(), testCellAboveRight);

    Cell testCell2 = new Cell();
    Cell testCell2AboveRight = new Cell();
    testCell2.neighbors.set(2, testCell2AboveRight);
    t.checkExpect(testCell2.getTopRightNeighbor(), testCell2AboveRight);
  }
  
  void testCellSetTopRightNeighbor(Tester t) {
    Cell testCell = new Cell();
    Cell testCellAboveRight = new Cell();
    testCell.setTopRightNeighbor(testCellAboveRight);
    t.checkExpect(testCell.neighbors.get(2), testCellAboveRight);
  }

  void testCellGetBottomLeftNeighbor(Tester t) {
    Cell testCell = new Cell();
    Cell testCellBelowLeft = new Cell();
    testCell.setBottomLeftNeighbor(testCellBelowLeft);
    t.checkExpect(testCell.getBottomLeftNeighbor(), testCellBelowLeft);

    Cell testCell2 = new Cell();
    Cell testCell2BelowLeft = new Cell();
    testCell2.neighbors.set(5, testCell2BelowLeft);
    t.checkExpect(testCell2.getBottomLeftNeighbor(), testCell2BelowLeft);
  }
  
  void testCellSetBottomLeftNeighbor(Tester t) {
    Cell testCell = new Cell();
    Cell testCellBelowLeft = new Cell();
    testCell.setBottomLeftNeighbor(testCellBelowLeft);
    t.checkExpect(testCell.neighbors.get(5), testCellBelowLeft);
  }

  void testCellGetBottomRightNeighbor(Tester t) {
    Cell testCell = new Cell();
    Cell testCellBelowRight = new Cell();
    testCell.setBottomRightNeighbor(testCellBelowRight);
    t.checkExpect(testCell.getBottomRightNeighbor(), testCellBelowRight);

    Cell testCell2 = new Cell();
    Cell testCell2BelowRight = new Cell();
    testCell2.neighbors.set(7, testCell2BelowRight);
    t.checkExpect(testCell2.getBottomRightNeighbor(), testCell2BelowRight);
  }
  
  void testCellSetBottomRightNeighbor(Tester t) {
    Cell testCell = new Cell();
    Cell testCellBelowRight = new Cell();
    testCell.setBottomRightNeighbor(testCellBelowRight);
    t.checkExpect(testCell.neighbors.get(7), testCellBelowRight);
  }

  void testCellGetCell(Tester t) {
    // TODO
  }

  void testCellNumMinesAdjacent(Tester t) {
    // TODO
  }

  void testBoardStitchRows(Tester t) {
    // TODO
  }

  void testBoardInitializeBoard(Tester t) {
    // TODO
  }

  void testBoardSwap(Tester t) {
    Board testBoard = new Board(9, 9, 1);
    ArrayList<Cell> testList = new ArrayList<Cell>();
    Cell testCell1 = new Cell();
    Cell testCell2 = new Cell();
    testList.add(0, testCell1);
    testList.add(1, testCell2);
    testBoard.swap(testList, 0, 1);
    t.checkExpect(testList.get(0), testCell2);
    t.checkExpect(testList.get(1), testCell1);
  }

  void testBoardFisherYatesShuffle(Tester t) {
    // TODO
  }

  void testBoardInitializeMines(Tester t) {
    // TODO
  }

  void testConstPropsDetermineImageFromCell(Tester t) {
    // TODO
  }

  void testConstPropsGameLostDetermineImageFromCell(Tester t) {
    // TODO
  }

  void testMineSweeperWorldMakeScene(Tester t) {
    // TODO
  }

  void testMineSweeperWorldInitializeWorldScene(Tester t) {
    // TODO
  }

  void testMineSweeperWorldCreateScoreboardTopBorder(Tester t) {
    // TODO
  }

  void testMineSweeperWorldCreateScoreboardBottomBorder(Tester t) {
    // TODO
  }

  void testMineSweeperWorldPow(Tester t) {
    // TODO
  }

  void testMineSweeperWorldGetNthDigit(Tester t) {
    // TODO
  }

  void testMineSweeperWorldCreateScoreboardMineCounter(Tester t) {
    // TODO
  }

  void testMineSweeperWorldCreateScoreboardTimer(Tester t) {
    // TODO
  }

  void testMineSweeperWorldCreateScoreboard(Tester t) {
    // TODO
  }

  void testMineSweeperWorldCreateLostScoreboard(Tester t) {
    // TODO
  }

  void testMineSweeperWorldCreateTileRows(Tester t) {
    // TODO
  }

  void testMineSweeperWorldCreateLostTileRows(Tester t) {
    // TODO
  }

  void testMineSweeperWorldCreateBottomBorder(Tester t) {
    // TODO
  }

  void testMineSweeperWorldCreateGameBoardImage(Tester t) {
    // TODO
  }

  void testMineSweeperWorldCreateLostGameBoardImage(Tester t) {
    // TODO
  }

  void testMineSweeperWorldOnTick(Tester t) {
    // TODO
  }

  void testMineSweeperWorldFloodFill(Tester t) {
    // TODO
  }

  void testMineSweeperWorldOnMousePressed(Tester t) {
    // TODO
  }

  void testMineSweeperWorldOnMouseReleased(Tester t) {
    // TODO
  }

  void testMineSweeperWorldOnMouseClicked(Tester t) {
    // TODO
  }

  void testMineSweeperWorldGameWon(Tester t) {
    // TODO
  }

  void testMineSweeperWorldGameLost(Tester t) {
    // TODO
  }

  void testMineSweeperWorldWorldEnds(Tester t) {
    // TODO
  }

  void testMineSweeperRunGame(Tester t) {
    // TODO
  }
}