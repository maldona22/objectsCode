import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
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
  // Users just use the designated setters and getters
  // Hence why this is made private
  private ArrayList<Cell> neighbors;
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

  // Ones I probably wont need as much
  // TODO remember to desiginate all the random getters and setters that arent the normals
  // for testing only?
  // Actually maybe its just better to delete the ones that aren't used
  // and call it a day

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

  // TODO test the everliving fuck outta this shit
  // I strongly suspect this code isn't actually correct
  // and only seems to work
  // I think the repeatable case is clicking the bottom most right most cell always crashes
  // I think anyways
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
      if (cell.hasMine) {
        numOfMines++;
      }
    }
    return numOfMines;
  }
}

// TODO Figure out if this class even needs to exist?
// A cell kinda already is a board in of itself
// Maybe I should just put this all into the cell class?
// Might be a bad idea if I use sentinels though
// Give it some thought
class Board {
  // TODO I hacked this together initially using these values instead of the ones held
  // in the world struct, double check all usages of WIDTH and HEIGHT and remove
  // in appropriate ones
    final int WIDTH;
    final int HEIGHT;
    // TODO Maybe change this into a sentinel so that getCell works properly?
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
      if (i == 0) {
        currentTopCell.setBottomMiddleNeighbor(currentBottomCell);
        currentTopCell.setBottomRightNeighbor(currentBottomCell.getMiddleRightNeighbor());
        currentBottomCell.setTopMiddleNeighbor(currentTopCell);
        currentBottomCell.setTopRightNeighbor(currentTopCell.getMiddleRightNeighbor());
      }
      else if (i == WIDTH - 1) {
        currentTopCell.setBottomMiddleNeighbor(currentBottomCell);
        currentTopCell.setBottomLeftNeighbor(currentBottomCell.getMiddleLeftNeighbor());
        currentBottomCell.setTopMiddleNeighbor(currentTopCell);
        currentBottomCell.setTopLeftNeighbor(currentTopCell.getMiddleLeftNeighbor());
      }
      else {
        currentTopCell.setBottomMiddleNeighbor(currentBottomCell);
        currentTopCell.setBottomLeftNeighbor(currentBottomCell.getMiddleLeftNeighbor());
        currentTopCell.setBottomRightNeighbor(currentBottomCell.getMiddleRightNeighbor());
        currentBottomCell.setTopMiddleNeighbor(currentTopCell);
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
  // TODO should this be placed into constprops instead?

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

    // Seems to initialize the mines in a consistent O(N)
    // Basically O(2*Size + graph traversal)
    // Need to profile to double check consistency
    // Seems to take the same amount time at max mines compared to 1 mine though
    // So at a glance I think its working as intended
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
    }
}

class ConstProps {
    final FrozenImage zeroAdjMines = new FrozenImage(new FromFileImage("0.png"));
    final FrozenImage oneAdjMines = new FrozenImage(new FromFileImage("1.png"));
    final FrozenImage twoAdjMines = new FrozenImage(new FromFileImage("2.png"));
    final FrozenImage threeAdjMines = new FrozenImage(new FromFileImage("3.png"));
    final FrozenImage fourAdjMines = new FrozenImage(new FromFileImage("4.png"));
    final FrozenImage fiveAdjMines = new FrozenImage(new FromFileImage("5.png"));
    final FrozenImage sixAdjMines = new FrozenImage(new FromFileImage("6.png"));
    final FrozenImage sevenAdjMines = new FrozenImage(new FromFileImage("7.png"));
    final FrozenImage eightAdjMines = new FrozenImage(new FromFileImage("8.png"));
    final FrozenImage unknownAdjMines = new FrozenImage(new FromFileImage("Unknown.png"));
    final FrozenImage flag = new FrozenImage(new FromFileImage("Flag.png"));
    final FrozenImage mine = new FrozenImage(new FromFileImage("Mine.png"));

    final ArrayList<FrozenImage> numberedTileImages = new ArrayList<FrozenImage>(
            Arrays.asList(zeroAdjMines, oneAdjMines, twoAdjMines, threeAdjMines, fourAdjMines, fiveAdjMines,
                    sixAdjMines, sevenAdjMines, eightAdjMines));
    // Gonna just hope that everything is done in pixels here since all the pngs
    // above are 15 pixels by 15 pixels
    final int tileWidthInPixels = 15;
    final int tileHeightInPixels = 15;

    void placeNumberedTileImage(WorldScene scene, int numMinesAdj, int tileX, int tileY) {
      scene.placeImageXY(this.numberedTileImages.get(numMinesAdj),
          (tileX * this.tileWidthInPixels) + (this.tileWidthInPixels / 2),
          (tileY * this.tileHeightInPixels) + (this.tileHeightInPixels / 2));
    }
    
    void placeMineTileImage(WorldScene scene, int tileX, int tileY) {
      scene.placeImageXY(this.mine,
          (tileX * this.tileWidthInPixels) + (this.tileWidthInPixels / 2),
          (tileY * this.tileHeightInPixels) + (this.tileHeightInPixels / 2));
    }

    void placeFlagTileImage(WorldScene scene, int tileX, int tileY) {
      scene.placeImageXY(this.flag,
          (tileX * this.tileWidthInPixels) + (this.tileWidthInPixels / 2),
          (tileY * this.tileHeightInPixels) + (this.tileHeightInPixels / 2));
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
    int heightInTiles;
    WorldScene worldScene;
    ConstProps constProps;

    MineSweeperWorld(int widthInTiles, int heightInTiles, int numMines, ConstProps constProps) {
      this.widthInTiles = widthInTiles;
      this.heightInTiles = heightInTiles;
      this.constProps = constProps;

      this.board = new Board(widthInTiles, heightInTiles, numMines);
      this.worldScene = initializeWorldScene();
    }

    public WorldScene initializeWorldScene() {
        WorldScene newScene = new WorldScene(this.constProps.tileWidthInPixels * board.WIDTH,
            this.constProps.tileHeightInPixels * board.HEIGHT);
        for (int i = 0; i < heightInTiles; i++) {
            for (int j = 0; j < widthInTiles; j++) {
                newScene.placeImageXY(constProps.unknownAdjMines, (j * constProps.tileWidthInPixels) + (constProps.tileWidthInPixels / 2), (i * constProps.tileHeightInPixels) + (constProps.tileHeightInPixels / 2));
            }
        }
        return newScene;
    }

    @Override
    public WorldScene makeScene() {
        return worldScene;
    }

    @Override
    public void onMouseClicked(Posn pos, String buttonPressed) {
      // Intentionally doing integer division here, want the results truncated
      // Means I don't have to do bounds checking? Hopefully?
      int tileX = pos.x / this.constProps.tileWidthInPixels;
      int tileY = pos.y / this.constProps.tileHeightInPixels;
      Cell clickedCell = this.board.initialCell.getCell(tileX, tileY);
      if (clickedCell.flagged) {
        return;
      } else if (clickedCell.hasMine == false) {
        // TODO display number png
        int numMinesAdj = clickedCell.numOfMinesAdjacent();
        // TODO Need to check if I can just place an image over another image
        // or if I need to specifically call a modifying function
        constProps.placeNumberedTileImage(worldScene, numMinesAdj, tileX, tileY);
        //worldScene.placeImageXY(constProps.numberedTileImages.get(numMinesAdj), tileX * constProps.tileWidthInPixels,
            //tileY * constProps.tileHeightInPixels);
      } else if (clickedCell.hasMine) {
        // TODO Call game over code
        constProps.placeMineTileImage(worldScene, tileX, tileY);
      } else {
        try {
          throw new MouseClickException("Invalid data in cell clicked");
        } catch (MouseClickException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
          System.exit(1);
        }
      }
    }
}

public class MineSweeper {

  static void runGame(int tileWidth, int tileHeight, int numMines) {
    ConstProps constProps = new ConstProps();
    MineSweeperWorld gameWorld = new MineSweeperWorld(tileWidth, tileHeight, numMines, constProps);
    int pixelWidth = tileWidth * constProps.tileWidthInPixels;
    int pixelHeight = tileHeight * constProps.tileHeightInPixels;
    gameWorld.bigBang(pixelWidth, pixelHeight);
  }
  public static void main(String[] args) {
    runGame(16, 30, 99);
  }
}