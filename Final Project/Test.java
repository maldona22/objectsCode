import javalib.impworld.*;
import javalib.worldimages.*;
import java.awt.Color;

class TestWorld extends World {
    final FromFileImage unknownAdjMines = new FromFileImage("Unknown.png");
    final FromFileImage tileLeftBorderSegment = new FromFileImage("TileLeftBorderSegment.png");
    final FromFileImage tileRightBorderSegment = new FromFileImage("TileRightBorderSegment.png");
    final FromFileImage scoreboardBottomBorderSegment = new FromFileImage("ScoreboardBottomBorderSegment.png");
    final FromFileImage scoreboardTopBorderSegment = new FromFileImage("ScoreboardTopBorderSegment.png");
    final FromFileImage scoreboardLeftBorderSegment = new FromFileImage("ScoreboardLeftBorder.png");
    final FromFileImage scoreboardRightBorderSegment = new FromFileImage("ScoreboardRightBorder.png");
    final FromFileImage tileBottomBorderLeftCorner = new FromFileImage("TileLeftBorderBottomCorner.png");
    final FromFileImage tileBottomBorderRightCorner = new FromFileImage("TileRightBorderBottomCorner.png");
    final FromFileImage tileBottomBorderSegment = new FromFileImage("TileBottomBorderSegment.png");
    final FromFileImage smileUnpressed = new FromFileImage("SmileUnpressed.png");

    WorldScene worldScene = new WorldScene(1250, 750);

    @Override
    public WorldScene makeScene() {
        BesideImage firstRow;
        BesideImage scoreboardBottomBorder;
        AboveImage tileRows;
        int heightScoreboardBackground = 48;
        Color scoreboardBackgroundColor = new Color(192, 192, 192);

        RectangleImage background = new RectangleImage((30 * 24), heightScoreboardBackground, OutlineMode.SOLID,
                scoreboardBackgroundColor);
        OverlayImage smile = new OverlayImage(smileUnpressed, background);
        
        BesideImage scoreBoardTopBorder;
        scoreBoardTopBorder = new BesideImage(scoreboardTopBorderSegment, scoreboardTopBorderSegment);
        for (int j = 2; j < 30; j++) {
            scoreBoardTopBorder = new BesideImage(scoreBoardTopBorder, scoreboardTopBorderSegment);
        }

        scoreboardBottomBorder = new BesideImage(scoreboardBottomBorderSegment, scoreboardBottomBorderSegment);
        for (int j = 2; j < 30; j++) {
            scoreboardBottomBorder = new BesideImage(scoreboardBottomBorder, scoreboardBottomBorderSegment);
        }
        AboveImage topBottomBorders = new AboveImage(scoreBoardTopBorder, smile, scoreboardBottomBorder);
        BesideImage scoreBoard = new BesideImage(scoreboardLeftBorderSegment, topBottomBorders,
                scoreboardRightBorderSegment);

                
        firstRow = new BesideImage(unknownAdjMines, unknownAdjMines);
        for (int j = 2; j < 30; j++) {
            firstRow = new BesideImage(firstRow, unknownAdjMines);
        }
        firstRow = new BesideImage(tileLeftBorderSegment, firstRow, tileRightBorderSegment);
        tileRows = new AboveImage(firstRow);
        for (int i = 1; i < 16; i++) {
            BesideImage row = new BesideImage(unknownAdjMines, unknownAdjMines);
            for (int j = 2; j < 30; j++) {
                row = new BesideImage(row, unknownAdjMines);
            }
            row = new BesideImage(tileLeftBorderSegment, row, tileRightBorderSegment);
            tileRows = new AboveImage(tileRows, row);
        }
        AboveImage testCombine = new AboveImage(scoreBoard, tileRows);
        BesideImage lastRow = new BesideImage(tileBottomBorderSegment, tileBottomBorderSegment);
        for (int i = 2; i < 30; i++) {
            lastRow = new BesideImage(lastRow, tileBottomBorderSegment);
        }
        lastRow = new BesideImage(tileBottomBorderLeftCorner, lastRow, tileBottomBorderRightCorner);
        AboveImage gameBoard = new AboveImage(testCombine, lastRow);

        worldScene.placeImageXY(new RectangleImage(1250, 750, OutlineMode.SOLID, new Color(0,0,0)), 1250/2, 750/2);
        worldScene.placeImageXY(gameBoard, 1250/2, 750/2);
      return worldScene;
    }
}


public class Test {
    public static void main(String[] args) {
        TestWorld test = new TestWorld();
        test.bigBang(1250, 750);
    }
}
