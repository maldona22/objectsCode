import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.HashSet;
import java.awt.Color;

import javalib.impworld.*;
import javalib.worldimages.*;
import tester.Tester;


class Cell {
  // neighboring cells are indexed depending on location around this, labeled X here
  // 0  1  2
  // 3  X  4
  // 5  6  7
  // This is an implementation detail that doesn't need to be remembered by its users
  // Users just use the designated setters and getters, or the neighbors iterator when
  // the location around the cell is irrelevant
  ArrayList<Cell> neighbors;
  int numOfMinesAdjacent;
  boolean hasMine;
  boolean revealed;
  boolean flagged;

  Cell() {
    // Most amount of neighbhors a cell can have is 8, so just prealloc it to begin with
    this.neighbors = new ArrayList<Cell>(8);
    // I greatly dislike doing this
    // TODO figure out a way to set the *size* and not the *capacity* without this nonsense
    for (int i = 0; i < 8; i++) {
      neighbors.add(i, null);
    }

    this.hasMine = false;
    this.flagged = false;
    this.revealed = false;
  }

  Cell getTopMiddleNeighbor() {
    return neighbors.get(1);
  }

  void setTopMiddleNeighbor(Cell cell) {
    neighbors.set(1, cell);
  }

  Cell getBottomMiddleNeighbor() {
    return neighbors.get(6);
  }

  void setBottomMiddleNeighbor(Cell cell) {
    neighbors.set(6, cell);
  }

  Cell getMiddleLeftNeighbor() {
    return neighbors.get(3);
  }

  void setMiddleLeftNeighbor(Cell cell) {
    neighbors.set(3, cell);
  }

  Cell getMiddleRightNeighbor() {
    return neighbors.get(4);
  }

  void setMiddleRightNeighbor(Cell cell) {
    neighbors.set(4, cell);
  }

  Cell getTopLeftNeighbor() {
    return neighbors.get(0);
  }

  void setTopLeftNeighbor(Cell cell) {
    neighbors.set(0, cell);
  }

  Cell getTopRightNeighbor() {
    return neighbors.get(2);
  }

  void setTopRightNeighbor(Cell cell) {
    neighbors.set(2, cell);
  }

  Cell getBottomLeftNeighbor() {
    return neighbors.get(5);
  }

  void setBottomLeftNeighbor(Cell cell) {
    neighbors.set(5, cell);
  }

  Cell getBottomRightNeighbor() {
    return neighbors.get(7);
  }

  void setBottomRightNeighbor(Cell cell) {
    neighbors.set(7, cell);
  }

  Cell getCell(int x, int y) {
    if (y > 0) {
      return this.getBottomMiddleNeighbor().getCell(x, y - 1);
    } else if (x > 0) {
      return this.getMiddleRightNeighbor().getCell(x - 1, y);
    } else {
      return this;
    }
  }

  int numOfMinesAdjacent() {
    int numOfMines = 0;
    for (Cell cell : neighbors) {
      if (cell == null) {
        continue;
      }
      else if (cell.hasMine) {
        numOfMines++;
      }
    }
    return numOfMines;
  }
}

// Acting as a sentinel of sorts for the first cell in the game board
class Board {
  // TODO I hacked this together initially using these values instead of the ones held
  // in the world struct, double check all usages of WIDTH and HEIGHT and remove
  // in appropriate ones
    final int WIDTH;
    final int HEIGHT;
    Cell initialCell;

    Board(int width, int height, int numMines) {
        this.WIDTH = width;
        this.HEIGHT = height;
        this.initialCell = new Cell();
        initializeBoard(initialCell);
        initializeMines(numMines);
    }

    void stitchRows(Cell topRow, Cell bottomRow) {
    Cell currentTopCell = topRow;
    Cell currentBottomCell = bottomRow;
    for (int i = 0; i < WIDTH; i++) {
      currentTopCell.setBottomMiddleNeighbor(currentBottomCell);
      currentBottomCell.setTopMiddleNeighbor(currentTopCell);
      
      if (i == 0) {
        currentTopCell.setBottomRightNeighbor(currentBottomCell.getMiddleRightNeighbor());
        currentBottomCell.setTopRightNeighbor(currentTopCell.getMiddleRightNeighbor());
      }
      else if (i == WIDTH - 1) {
        currentTopCell.setBottomLeftNeighbor(currentBottomCell.getMiddleLeftNeighbor());
        currentBottomCell.setTopLeftNeighbor(currentTopCell.getMiddleLeftNeighbor());
      }
      else {
        currentTopCell.setBottomLeftNeighbor(currentBottomCell.getMiddleLeftNeighbor());
        currentTopCell.setBottomRightNeighbor(currentBottomCell.getMiddleRightNeighbor());
        currentBottomCell.setTopLeftNeighbor(currentTopCell.getMiddleLeftNeighbor());
        currentBottomCell.setTopRightNeighbor(currentTopCell.getMiddleRightNeighbor());
      }

      currentTopCell = currentTopCell.getMiddleRightNeighbor();
      currentBottomCell = currentBottomCell.getMiddleRightNeighbor();
    }
  }

  private void initializeBoard(Cell cell) {
    Cell topRow;
    Cell bottomRow;

    // Preloading the for loop by doing the one extraneous case "manually"
    // AKA when the topRow doesn't exist yet
    Cell firstCell = cell;
    Cell previousCell = firstCell;
    // Starting at one since previousCell is already taking the 0th indexed position in the row
    for (int j = 1; j < WIDTH; j++) {
      Cell newCell = new Cell();
      newCell.setMiddleLeftNeighbor(previousCell);
      previousCell.setMiddleRightNeighbor(newCell);
      previousCell = newCell;
    }
    topRow = firstCell;

    // Starting at one since the first row was already computed above
    for(int i = 1; i < HEIGHT; i++) {
      firstCell = new Cell();
      previousCell = firstCell;
      // Starting at one since previousCell is already taking the 0th indexed position in the row
      for(int j = 1; j < WIDTH; j++) {
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

    private <T> void swap(ArrayList<T> list, final int indexA, final int indexB) {
        T temp = list.get(indexA);
        list.set(indexA, list.get(indexB));
        list.set(indexB, temp);
    }

    private void fisherYatesShuffle(ArrayList<Integer> list, final int numItems) {
        Random rand = new Random();
        for (int i = 0; i <= numItems - 2; i++) {
            int j = i + rand.nextInt(numItems - i);
            swap(list, i, j);
        }
    }

    // Initializes the mines in a consistent O(N)
    // Confirmed with profiler (VisualVM)
    private void initializeMines(final int numMines) {
        int size = WIDTH * HEIGHT;
        ArrayList<Integer> boardAsInts = new ArrayList<Integer>(size);
        for (int i = 0; i < size; i++) {
            boardAsInts.add(i);
        }
        fisherYatesShuffle(boardAsInts, size);
        for (int i = 0; i < numMines; i++) {
          int y = boardAsInts.get(i) / WIDTH;
          int x = boardAsInts.get(i) % WIDTH;
          initialCell.getCell(x, y).hasMine = true;
        }
        
        for (int i = 0; i < HEIGHT; i++) {
          for (int j = 0; j < WIDTH; j++) {
            Cell currentCell  = initialCell.getCell(j, i);
            currentCell.numOfMinesAdjacent = currentCell.numOfMinesAdjacent();
          }
        }
    }
}

final class ConstProps {
    final static int numberedTileWidthInPixels = 24;
    final static int numberedTileHeightInPixels = 24;
    final static int heightScoreboardBackground = 48;
    final static Color scoreboardBackgroundColor = new Color(192, 192, 192);

    final static FromFileImage scoreboardBottomBorderSegment = new FromFileImage("ScoreboardBottomBorderSegment.png");
    final static FromFileImage scoreboardTopBorderSegment = new FromFileImage("ScoreboardTopBorderSegment.png");
    final static FromFileImage scoreboardLeftBorderSegment = new FromFileImage("ScoreboardLeftBorder.png");
    final static FromFileImage scoreboardRightBorderSegment = new FromFileImage("ScoreboardRightBorder.png");
    final static int scoreboardLeftBorderSegmentWidth = 15;
    final static int scoreboardRightBorderSegmentWidth = 15;
    final static int scoreboardTopBorderSegmentHeight = 15;

    final static FromFileImage scoreboardMineCounterZero = new FromFileImage("Scoreboard0.png");
    final static FromFileImage scoreboardMineCounterOne = new FromFileImage("Scoreboard1.png");
    final static FromFileImage scoreboardMineCounterTwo = new FromFileImage("Scoreboard2.png");
    final static FromFileImage scoreboardMineCounterThree = new FromFileImage("Scoreboard3.png");
    final static FromFileImage scoreboardMineCounterFour = new FromFileImage("Scoreboard4.png");
    final static FromFileImage scoreboardMineCounterFive = new FromFileImage("Scoreboard5.png");
    final static FromFileImage scoreboardMineCounterSix = new FromFileImage("Scoreboard6.png");
    final static FromFileImage scoreboardMineCounterSeven = new FromFileImage("Scoreboard7.png");
    final static FromFileImage scoreboardMineCounterEight = new FromFileImage("Scoreboard8.png");
    final static FromFileImage scoreboardMineCounterNine = new FromFileImage("Scoreboard9.png");

    final static ArrayList<FromFileImage> mineCounterImages = new ArrayList<FromFileImage>(
        Arrays.asList(scoreboardMineCounterZero, scoreboardMineCounterOne, scoreboardMineCounterTwo,
            scoreboardMineCounterThree, scoreboardMineCounterFour, scoreboardMineCounterFive, scoreboardMineCounterSix,
            scoreboardMineCounterSeven, scoreboardMineCounterEight, scoreboardMineCounterNine));
    final static int mineCounterWidth = 60;
    final static int mineCounterHeight = 34;
    final static int scoreboardTimerWidth = 60;
    final static int scoreBoardTimerHeight = 34;

    final static FromFileImage tileLeftBorderSegment = new FromFileImage("TileLeftBorderSegment.png");
    final static int tileLeftBorderSegmentWidth = 15;
    final static FromFileImage tileRightBorderSegment = new FromFileImage("TileRightBorderSegment.png");
    final static int tileRightBorderSegmentWidth = 15;
    final static FromFileImage tileBottomBorderLeftCorner = new FromFileImage("TileLeftBorderBottomCorner.png");
    final static FromFileImage tileBottomBorderRightCorner = new FromFileImage("TileRightBorderBottomCorner.png");
    final static FromFileImage tileBottomBorderSegment = new FromFileImage("TileBottomBorderSegment.png");
    final static int tileBottomBorderSegmentHeight = 15;
    final static int scoreboardBottomBorderSegmentHeight = 15;

    final static FromFileImage smileUnpressed = new FromFileImage("SmileUnpressed.png");
    final static FromFileImage smilePressed = new FromFileImage("SmilePressed.png");
    final static FromFileImage smileClick = new FromFileImage("SmileTilePressed.png");
    final static FromFileImage smileLost = new FromFileImage("SmileLost.png");
    final static FromFileImage smileWon = new FromFileImage("SmileWon.png");

    final static FromFileImage zeroAdjMines = new FromFileImage("0.png");
    final static FromFileImage oneAdjMines = new FromFileImage("1.png");
    final static FromFileImage twoAdjMines = new FromFileImage("2.png");
    final static FromFileImage threeAdjMines = new FromFileImage("3.png");
    final static FromFileImage fourAdjMines = new FromFileImage("4.png");
    final static FromFileImage fiveAdjMines = new FromFileImage("5.png");
    final static FromFileImage sixAdjMines = new FromFileImage("6.png");
    final static FromFileImage sevenAdjMines = new FromFileImage("7.png");
    final static FromFileImage eightAdjMines = new FromFileImage("8.png");
    final static FromFileImage unknownAdjMines = new FromFileImage("Unknown.png");

    final static ArrayList<FromFileImage> numberedTileImages = new ArrayList<FromFileImage>(
            Arrays.asList(zeroAdjMines, oneAdjMines, twoAdjMines, threeAdjMines, fourAdjMines, fiveAdjMines,
            sixAdjMines, sevenAdjMines, eightAdjMines));
    
    final static FromFileImage flag = new FromFileImage("Flag.png");
    final static FromFileImage unflaggedMine = new FromFileImage("MineUnflagged.png");
    final static FromFileImage flaggedMine = new FromFileImage("MineFlagged.png");
    final static FromFileImage detonatedMine = new FromFileImage("Detonated.png");
    
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
        }
        else {
          return unknownAdjMines;
        }
      }
    }

    static WorldImage gameLostDetermineImageFromCell(Cell cell) {
      // Fix this to only handle valid cases and the else throwing a exception
      if (cell.hasMine) {
        if(cell.revealed == false) {
          if(cell.flagged) {
            return flaggedMine;
          }
          else {
            return unflaggedMine;
          }
        }
        else {
          return detonatedMine;
        }
      } else if (cell.revealed == false) {
        if (cell.flagged) {
          return flag;
        }
        else {
          return unknownAdjMines;
        }
      } else {
        return numberedTileImages.get(cell.numOfMinesAdjacent);
      }
    }
}

class MouseClickException extends Exception {
    public MouseClickException() {
        super();
    }

    public MouseClickException(String errorMessage) {
        super(errorMessage);
    }
}

class MineSweeperWorld extends World {
    Board board;
    int widthInTiles;
    int widthInPixels;
    int heightInTiles;
    int heightInPixels;
    int numMines;
    double tickRate;
    double timer;
    int displayedTime;
    boolean timeStart;
    int numberFlagged;
    boolean mouseClicked;
    WorldScene worldScene;
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
    // Additionally, according to the profiler I used (VisualVM) >99% of cpu time was spent
    // creating the images to draw to the screen
    // Currently, the WorldScene is initialized and ended using the original method (since it only 
    // needs to be done once), and every change after the initial screen is using the new method
    // where the WorldScene is updated via mutation changing only what is actually changing 
    // rather than drawing every image on the screen over and over again
    @Override
    public WorldScene makeScene() {
      return worldScene;
    }

    public WorldScene initializeWorldScene() {
      WorldScene newScene = new WorldScene(widthInPixels, heightInPixels);
      newScene.placeImageXY(
          new RectangleImage(widthInPixels, heightInPixels, OutlineMode.SOLID, new Color(255, 255, 255)),
          widthInPixels / 2, heightInPixels / 2);
      newScene.placeImageXY(createGameBoardImage(),
          widthInPixels / 2,
          heightInPixels/ 2);
      return newScene;
    }

    BesideImage createScoreboardTopBorder() {
      BesideImage scoreBoardTopBorder;
      scoreBoardTopBorder = new BesideImage(ConstProps.scoreboardTopBorderSegment,
          ConstProps.scoreboardTopBorderSegment);
      for (int j = 2; j < widthInTiles; j++) {
        scoreBoardTopBorder = new BesideImage(scoreBoardTopBorder, ConstProps.scoreboardTopBorderSegment);
      }
      return scoreBoardTopBorder;
    }
    
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
    int pow(int num, int exp) {
      if (exp > 0) {
        return num * pow(num, exp - 1);
      }
      else {
        return 1;
      }
    }

    int getNthDigit(int num, int nthPos) {
      return (num / pow(10, nthPos - 1)) % 10;
    }

    // TODO add check in runGame to make sure number of mines doesn't exceed triple digits?
    // TODO need to add check to make sure that numberFlagged doesn't exceed numMines
    BesideImage createScoreboardMineCounter() {
      // In reverse order
      WorldImage firstDigit = ConstProps.mineCounterImages.get(getNthDigit(numMines - numberFlagged, 3));
      WorldImage secondDigit = ConstProps.mineCounterImages.get(getNthDigit(numMines - numberFlagged, 2));
      WorldImage thirdDigit = ConstProps.mineCounterImages.get(getNthDigit(numMines - numberFlagged, 1));
      return new BesideImage(firstDigit, secondDigit, thirdDigit);
    }

    BesideImage createScoreboardTimer() {
      // In reverse order
      WorldImage firstDigit = ConstProps.mineCounterImages.get(getNthDigit(displayedTime, 3));
      WorldImage secondDigit = ConstProps.mineCounterImages.get(getNthDigit(displayedTime, 2));
      WorldImage thirdDigit = ConstProps.mineCounterImages.get(getNthDigit(displayedTime, 1));
      return new BesideImage(firstDigit, secondDigit, thirdDigit);
    }

    BesideImage createScoreboard() {
      RectangleImage scoreboardBackground = new RectangleImage((widthInTiles * ConstProps.numberedTileWidthInPixels),
          ConstProps.heightScoreboardBackground, OutlineMode.SOLID,
          ConstProps.scoreboardBackgroundColor);
      OverlayImage smile = mouseClicked ? new OverlayImage(ConstProps.smileClick, scoreboardBackground)
          : new OverlayImage(ConstProps.smileUnpressed, scoreboardBackground);
      BesideImage mineCounter = createScoreboardMineCounter();
      OverlayOffsetImage mineCounterWithSmile = new OverlayOffsetImage(mineCounter,
          ((widthInTiles * ConstProps.numberedTileWidthInPixels) / 2.0) - (ConstProps.mineCounterWidth / 2.0) - 9, 0, smile);
      BesideImage timer = createScoreboardTimer();
      OverlayOffsetImage mineCounterWithSmileAndTimer = new OverlayOffsetImage(timer, -(( (widthInTiles * ConstProps.numberedTileWidthInPixels) / 2.0) - (ConstProps.mineCounterWidth / 2.0) - 9), 0, mineCounterWithSmile);
      BesideImage scoreBoard = new BesideImage(ConstProps.scoreboardLeftBorderSegment,
          new AboveImage(createScoreboardTopBorder(), mineCounterWithSmileAndTimer, createScoreboardBottomBorder()),
          ConstProps.scoreboardRightBorderSegment);
      return scoreBoard;
    }

    BesideImage createLostScoreboard() {
      RectangleImage scoreboardBackground = new RectangleImage((widthInTiles * ConstProps.numberedTileWidthInPixels),
          ConstProps.heightScoreboardBackground, OutlineMode.SOLID,
          ConstProps.scoreboardBackgroundColor);
      OverlayImage smile = new OverlayImage(ConstProps.smileLost, scoreboardBackground);
      BesideImage mineCounter = createScoreboardMineCounter();
      OverlayOffsetImage mineCounterWithSmile = new OverlayOffsetImage(mineCounter,
          ((widthInTiles * ConstProps.numberedTileWidthInPixels) / 2.0) - (ConstProps.mineCounterWidth / 2.0) - 9, 0, smile);
      BesideImage timer = createScoreboardTimer();
      OverlayOffsetImage mineCounterWithSmileAndTimer = new OverlayOffsetImage(timer, -(((widthInTiles * ConstProps.numberedTileWidthInPixels) / 2.0) - (ConstProps.mineCounterWidth / 2.0) - 9), 0, mineCounterWithSmile);
      BesideImage scoreBoard = new BesideImage(ConstProps.scoreboardLeftBorderSegment,
          new AboveImage(createScoreboardTopBorder(), mineCounterWithSmileAndTimer, createScoreboardBottomBorder()),
          ConstProps.scoreboardRightBorderSegment);
      return scoreBoard;
    }
    
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

    BesideImage createBottomBorder() {
      BesideImage lastRow = new BesideImage(ConstProps.tileBottomBorderSegment);
      for (int i = 1; i < widthInTiles; i++) {
        lastRow = new BesideImage(lastRow, ConstProps.tileBottomBorderSegment);
      }
      lastRow = new BesideImage(ConstProps.tileBottomBorderLeftCorner, lastRow, ConstProps.tileBottomBorderRightCorner);
      return lastRow;
    }
    
    AboveImage createGameBoardImage() {
      return new AboveImage(createTileRows(), createBottomBorder());
    }

    AboveImage createLostGameBoardImage() {
      return new AboveImage(createLostTileRows(), createBottomBorder());
    }

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
    
    public void floodFill(Cell cell, int tileX, int tileY) {
      Cell topCell = cell.getTopMiddleNeighbor();
      Cell bottomCell = cell.getBottomMiddleNeighbor();
      Cell leftCell = cell.getMiddleLeftNeighbor();
      Cell rightCell = cell.getMiddleRightNeighbor();
      if (topCell != null) {
        if (topCell.hasMine == false && !topCell.revealed) {
          topCell.revealed = true;
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
    
    @Override
    public void onMousePressed(Posn pos, String buttonPressed) {
      if (buttonPressed.equals("LeftButton")) {
        worldScene.placeImageXY(ConstProps.smileClick, ConstProps.tileLeftBorderSegmentWidth + ((widthInTiles * ConstProps.numberedTileWidthInPixels) / 2), ConstProps.scoreboardTopBorderSegmentHeight + (ConstProps.heightScoreboardBackground / 2));
        mouseClicked = true;
      }
    }

    @Override
    public void onMouseReleased(Posn pos, String buttonPressed) {
      if (buttonPressed.equals("LeftButton")) {
        worldScene.placeImageXY(ConstProps.smileUnpressed, ConstProps.tileLeftBorderSegmentWidth + ((widthInTiles * ConstProps.numberedTileWidthInPixels) / 2), ConstProps.scoreboardTopBorderSegmentHeight + (ConstProps.heightScoreboardBackground / 2));
        mouseClicked = false;
      }
    }

    @Override
    public void onMouseClicked(Posn pos, String buttonPressed) {
      int tileX = (pos.x - ConstProps.tileLeftBorderSegmentWidth) / ConstProps.numberedTileWidthInPixels;
      int tileY = (pos.y - ConstProps.heightScoreboardBackground
          - ConstProps.scoreboardTopBorderSegmentHeight - ConstProps.scoreboardBottomBorderSegmentHeight)
          / ConstProps.numberedTileHeightInPixels;
      // TODO even though it shouldn't be possible maybe just check and make sure the coordinates are
      // valid just in case
      Cell clickedCell = this.board.initialCell.getCell(tileX, tileY);
      if (buttonPressed.equals("LeftButton")) {
        if (timeStart == false) {
          timeStart = true;
        }
        if (clickedCell.revealed || clickedCell.flagged) {
          return;
        } else if (clickedCell.hasMine == false) {
          clickedCell.revealed = true;
          worldScene.placeImageXY(ConstProps.determineImageFromCell(clickedCell), ConstProps.tileLeftBorderSegmentWidth + (tileX * ConstProps.numberedTileWidthInPixels) + (ConstProps.numberedTileWidthInPixels / 2), ConstProps.scoreboardTopBorderSegmentHeight + ConstProps.heightScoreboardBackground + ConstProps.scoreboardBottomBorderSegmentHeight + (tileY * ConstProps.numberedTileHeightInPixels) + (ConstProps.numberedTileHeightInPixels / 2));
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
            worldScene.placeImageXY(ConstProps.determineImageFromCell(clickedCell), ConstProps.tileLeftBorderSegmentWidth + (tileX * ConstProps.numberedTileWidthInPixels) + (ConstProps.numberedTileWidthInPixels / 2), ConstProps.scoreboardTopBorderSegmentHeight + ConstProps.heightScoreboardBackground + ConstProps.scoreboardBottomBorderSegmentHeight + (tileY * ConstProps.numberedTileHeightInPixels) + (ConstProps.numberedTileHeightInPixels / 2));
            worldScene.placeImageXY(createScoreboardMineCounter(), (9 + ConstProps.scoreboardLeftBorderSegmentWidth) + (ConstProps.scoreboardTimerWidth / 2), (7 + ConstProps.scoreboardTopBorderSegmentHeight) + (ConstProps.scoreBoardTimerHeight / 2));
          } else {
            clickedCell.flagged = true;
            numberFlagged++;
            worldScene.placeImageXY(ConstProps.determineImageFromCell(clickedCell), ConstProps.tileLeftBorderSegmentWidth + (tileX * ConstProps.numberedTileWidthInPixels) + (ConstProps.numberedTileWidthInPixels / 2), ConstProps.scoreboardTopBorderSegmentHeight + ConstProps.heightScoreboardBackground + ConstProps.scoreboardBottomBorderSegmentHeight + (tileY * ConstProps.numberedTileHeightInPixels) + (ConstProps.numberedTileHeightInPixels / 2));
            worldScene.placeImageXY(createScoreboardMineCounter(), (9 + ConstProps.scoreboardLeftBorderSegmentWidth) + (ConstProps.scoreboardTimerWidth / 2), (7 + ConstProps.scoreboardTopBorderSegmentHeight) + (ConstProps.scoreBoardTimerHeight / 2));
          }
        }
      }
    }
    
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

    @Override
    public WorldEnd worldEnds() {
      if (hitMine) {
        timeStart = false;
        worldScene.placeImageXY(createLostGameBoardImage(),
            widthInPixels / 2, heightInPixels / 2);
        return new WorldEnd(true, worldScene);
      } 
      else if (gameWon(board.initialCell, new HashSet<Cell>())) {
        worldScene.placeImageXY(ConstProps.smileWon, ConstProps.tileLeftBorderSegmentWidth + ((widthInTiles * ConstProps.numberedTileWidthInPixels) / 2), ConstProps.scoreboardTopBorderSegmentHeight + (ConstProps.heightScoreboardBackground / 2));
        return new WorldEnd(true, worldScene);
      } else {
        return new WorldEnd(false, this.makeScene());
      }
    }
}

public class MineSweeper {
  static void runGame(int tileWidth, int tileHeight, int numMines) {
    if (tileWidth < 8) {
      System.out.println("Cannot run a game with a tile width smaller than 8");
      System.exit(1);
    }
    
    int pixelWidth = (tileWidth * ConstProps.numberedTileWidthInPixels) + ConstProps.tileLeftBorderSegmentWidth + ConstProps.tileRightBorderSegmentWidth;
    int pixelHeight = (tileHeight * ConstProps.numberedTileHeightInPixels)
        + ConstProps.tileBottomBorderSegmentHeight + ConstProps.scoreboardBottomBorderSegmentHeight
        + ConstProps.heightScoreboardBackground + ConstProps.scoreboardTopBorderSegmentHeight;
    double tickRate = 0.5;
    MineSweeperWorld gameWorld = new MineSweeperWorld(tileWidth, pixelWidth, tileHeight, pixelHeight, numMines, tickRate);
    
    gameWorld.bigBang(pixelWidth, pixelHeight, 0.1);
  }

  public static void main(String[] args) {
    runGame(30, 16, 2);
  }
}