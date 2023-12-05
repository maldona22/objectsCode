import javalib.impworld.*;
import javalib.worldimages.*;

class TestWorld extends World {
    final FrozenImage unknownAdjMines = new FrozenImage(new VisiblePinholeImage(new FromFileImage("Unknown.png")).movePinhole(-7.5, -7.5));
    WorldScene worldScene = new WorldScene(100, 100);

    @Override
    public WorldScene makeScene() {
        worldScene.placeImageXY(unknownAdjMines, 50, 50);
      return worldScene;
    }
}


public class Test {
    public static void main(String[] args) {
        System.out.println(15 / 2.0);
        TestWorld test = new TestWorld();
        test.bigBang(100, 100);
    }
}
