import javalib.worldimages.*;
import javalib.worldcanvas.*;
import javalib.funworld.*;
import java.awt.*;

interface ITree {

  ConsLoPairWidth getWidthList();

  ITree rotateTree(double angleDiff);

  double getAngleDiff(double angleRotation);

  double getWidth();

  boolean isDrooping();

  ITree combine(int leftLength, int rightLength, double leftTheta, double rightTheta, ITree otherTree);
  
  WorldImage draw();
}

interface ILoPairWidth { }

class ConsLoPairWidth implements ILoPairWidth {
  PairWidth first;
  ILoPairWidth rest;

  ConsLoPairWidth(PairWidth first, ILoPairWidth rest) {
    this.first = first;
    this.rest = rest;
  }
}

class MtLoPairWidth implements ILoPairWidth { }

class PairWidth {
  double leftWidth;
  double rightWidth;

  PairWidth(double leftWidth, double rightWidth) {
    this.leftWidth = leftWidth;
    this.rightWidth = rightWidth;
  }
}

class utils {

  static ILoPairWidth addValueToList(ILoPairWidth widths, double value) {
    if (widths instanceof MtLoPairWidth) {
      return new MtLoPairWidth();
    } else {
      PairWidth firstWidth = ((ConsLoPairWidth) widths).first;
      return new ConsLoPairWidth(
          new PairWidth(firstWidth.leftWidth + value,
              firstWidth.rightWidth + value),
          addValueToList(((ConsLoPairWidth) widths).rest, value));
    }
  }
  
  static ILoPairWidth consLists(ILoPairWidth firstList, ILoPairWidth secondList) {
    if (firstList instanceof ConsLoPairWidth) {
      return new ConsLoPairWidth(((ConsLoPairWidth) firstList).first,
          consLists(((ConsLoPairWidth) firstList).rest, secondList));
    } else {
      return secondList;
    }
  }

  static void printList(ILoPairWidth lists) {
    if (lists instanceof ConsLoPairWidth) {
      System.out.println("Left width: " + ((ConsLoPairWidth) lists).first.leftWidth);
      System.out.println("Right width: " + ((ConsLoPairWidth) lists).first.rightWidth);
      printList(((ConsLoPairWidth) lists).rest);
    }
    else {
      return;
    }
  }

  private static double findLargestLeft(ILoPairWidth widths, double largestLeft) {
    if (widths instanceof ConsLoPairWidth) {
      // Since were using the pair widths as a pair of cartesian X values
      // the "largest" left is mathematically actually the smallest value
      // Hence the weird at first glance comparisons
      if (((ConsLoPairWidth) widths).first.leftWidth > largestLeft) {
        return findLargestLeft(((ConsLoPairWidth) widths).rest, largestLeft);
      } else {
        return findLargestLeft(((ConsLoPairWidth) widths).rest, ((ConsLoPairWidth) widths).first.leftWidth);
      }
    } else {
      return largestLeft;
    }
  }
  
  static double findLargestLeft(ILoPairWidth widths) {
    // As mentioned above since we're looking for the mathematically smallest value
    // The default compare to value needs to be +INF
    return findLargestLeft(widths, Double.POSITIVE_INFINITY);
  }

  private static double findLargestRight(ILoPairWidth widths, double largestRight) {
    if (widths instanceof ConsLoPairWidth) {
      if (((ConsLoPairWidth) widths).first.rightWidth < largestRight) {
        return findLargestRight(((ConsLoPairWidth) widths).rest, largestRight);
      } else {
        return findLargestRight(((ConsLoPairWidth) widths).rest, ((ConsLoPairWidth) widths).first.rightWidth);
      }
    } else {
      return largestRight;
    }
  }

  static double findLargestRight(ILoPairWidth widths) {
    return findLargestRight(widths, Double.NEGATIVE_INFINITY);
  }

  static boolean pointsUpwards(double angle) {
    return (angle >= 0 && angle <= 180);
  }
}

class Leaf implements ITree {
  int size; // represents the radius of the leaf
  Color color; // the color to draw it

  Leaf(int size, Color color) {
    this.size = size;
    this.color = color;
  }

  public ConsLoPairWidth getWidthList() {
    return new ConsLoPairWidth(new PairWidth(((double) size) * -1, (double) size), new MtLoPairWidth());
  }

  public double getWidth() {
    return size * 2;
  }

  public boolean isDrooping() {
    return false;
  }

  public double getAngleDiff(double angleRotation) {
    return angleRotation;
  }

  public ITree rotateTree(double angleDiff) {
    return this;
  }

  public ITree combine(int leftLength, int rightLength, double leftTheta, double rightTheta, ITree otherTree) {
    return new Branch(leftLength, rightLength, leftTheta, rightTheta, this,
        otherTree.rotateTree(otherTree.getAngleDiff(rightTheta)));
  }
  
  public WorldImage draw() {
    return new CircleImage(size, "solid", color);
  }
}
 
class Stem implements ITree {
  // How long this stick is
  int length;
  // The angle (in degrees) of this stem, relative to the +x axis
  double theta;
  // The rest of the tree
  ITree tree;

  Stem(int length, double theta, ITree tree) {
    this.length = length;
    this.theta = theta;
    this.tree = tree;
  }

  public ConsLoPairWidth getWidthList() {
    double stemWidth = length * Math.cos(Math.toRadians(theta));
    if (theta > 90 && theta < 270) {
      stemWidth = stemWidth * -1;
    }

    ConsLoPairWidth treeWidths = tree.getWidthList();

    return (ConsLoPairWidth) utils.addValueToList(treeWidths, stemWidth);
  }

  public double getWidth() {
    ConsLoPairWidth widthList = this.getWidthList();
    return (-1 * utils.findLargestLeft(widthList))
        + utils.findLargestRight(widthList);
  }

  public boolean isDrooping() {
    if (utils.pointsUpwards(theta)) {
      return tree.isDrooping();
    } else {
      return true;
    }
  }
  
  public double getAngleDiff(double angleRotation) {
    return angleRotation - theta;
  }

  public ITree rotateTree(double angleDiff) {
    return new Stem(length, theta + angleDiff, tree.rotateTree(angleDiff));
  }

  public ITree combine(int leftLength, int rightLength, double leftTheta, double rightTheta, ITree otherTree) {
    return new Branch(leftLength, rightLength, leftTheta, rightTheta, this.rotateTree(this.getAngleDiff(leftTheta)),
        otherTree.rotateTree(otherTree.getAngleDiff(rightTheta)));
  }
  
  public WorldImage draw() {
    // TODO figure out how to handle the colors here
    // TODO have this overlay the variables tree worldimage
    return new OverlayImage( (new LineImage( new Posn((int) (length * Math.cos(Math.toRadians(theta))), (int) (length * Math.sin(Math.toRadians(theta)))), new Color(255, 0, 0))).movePinhole( (-1 * (length * Math.cos(Math.toRadians(theta))) / 2), (-1 * (length * Math.sin(Math.toRadians(theta))) / 2))
    , tree.draw());
  }

}
 
class Branch implements ITree {
  // How long the left and right branches are
  int leftLength;
  int rightLength;
  // The angle (in degrees) of the two branches, relative to the +x axis,
  double leftTheta;
  double rightTheta;
  // The remaining parts of the tree
  ITree left;
  ITree right;

  Branch(int leftLength, int rightLength, double leftTheta, double rightTheta, ITree left, ITree right) {
    this.leftLength = leftLength;
    this.rightLength = rightLength;
    this.leftTheta = leftTheta;
    this.rightTheta = rightTheta;
    this.left = left;
    this.right = right;
  }

  public ConsLoPairWidth getWidthList() {
    ConsLoPairWidth leftPairWidth = this.left.getWidthList();
    ConsLoPairWidth rightPairWidth = this.right.getWidthList();

    return (ConsLoPairWidth) utils.consLists(utils.addValueToList(leftPairWidth, (leftLength * Math.cos(Math.toRadians(leftTheta)))), utils.addValueToList(rightPairWidth, rightLength * Math.cos(Math.toRadians(rightTheta))));
  }

  public double getWidth() {
    ConsLoPairWidth widthList = this.getWidthList();
    System.out.println("Branch width list: ");
    utils.printList(widthList);
    System.out.println("Largest left" + utils.findLargestLeft(widthList));
    System.out.println("Largest right" + utils.findLargestRight(widthList));
    return (-1 * utils.findLargestLeft(widthList))
        + utils.findLargestRight(widthList);
  }
  
  public boolean isDrooping() {
    if (utils.pointsUpwards(leftTheta) && utils.pointsUpwards(rightTheta)) {
      return (left.isDrooping() && right.isDrooping());
    } else {
      return true;
    }
  }

  public double getAngleDiff(double angleRotation) {
    return angleRotation - leftTheta;
  }
  
  public ITree rotateTree(double angleDiff) {
    /* 
    double angleDiffRightToLeft = leftTheta - rightTheta;
    double newRightAngle = rightTheta + angleDiff;
    double newLeftAngle = newRightAngle + angleDiffRightToLeft;
    
    return new Branch(leftLength, rightLength, newLeftAngle, newRightAngle,
        left.rotateTree(angleDiff),
        right.rotateTree(angleDiff));
    */
    return new Branch(leftLength, rightLength, leftTheta + angleDiff, rightTheta + angleDiff,
        left.rotateTree(leftTheta + angleDiff),
        right.rotateTree(rightTheta + angleDiff));
  }
  
  public ITree combine(int leftLength, int rightLength, double leftTheta, double rightTheta, ITree otherTree) {
    double leftAdjustedAngle;
    double rightAdjustedAngle = 0;
    if (leftTheta > 270) {
      leftAdjustedAngle = leftTheta - 270;
    }
    else if (leftTheta > 180) {
      leftAdjustedAngle = leftTheta - 180;
    }
    else if (leftTheta > 90) {
      leftAdjustedAngle = leftTheta - 90;
    }
    else {
      leftAdjustedAngle = leftTheta;
    }
    if (rightTheta < 90) {
      rightAdjustedAngle = (90 - rightTheta) * -1;
    }
    return new Branch(leftLength, rightLength, leftTheta, rightTheta, this.rotateTree(leftAdjustedAngle),
          otherTree.rotateTree(rightAdjustedAngle));
  }
  
  public WorldImage draw() {
    WorldImage leftSide = new OverlayImage( (new LineImage(
        new Posn((int) (leftLength * Math.cos(Math.toRadians(leftTheta))), (int) (leftLength * Math.sin(Math.toRadians(leftTheta)))),
        new Color(255, 0, 0)))
        .movePinhole((-1 * (leftLength * Math.cos(Math.toRadians(leftTheta))) / 2), (-1 * (leftLength * Math.sin(Math.toRadians(leftTheta))) / 2)),
        left.draw())
        .movePinhole((int) (leftLength * Math.cos(Math.toRadians(leftTheta))), (int) (leftLength * Math.sin(Math.toRadians(leftTheta))));
        
        WorldImage rightSide = new OverlayImage( (new LineImage(
        new Posn((int) (rightLength * Math.cos(Math.toRadians(rightTheta))), (int) (rightLength * Math.sin(Math.toRadians(rightTheta)))),
        new Color(0, 0, 255)))
            .movePinhole((-1 * (rightLength * Math.cos(Math.toRadians(rightTheta))) / 2),
                (-1 * (rightLength * Math.sin(Math.toRadians(rightTheta))) / 2)),
            right.draw())
            .movePinhole((int) (rightLength * Math.cos(Math.toRadians(rightTheta))), (int) (rightLength * Math.sin(Math.toRadians(rightTheta))));
        
        return new OverlayImage(rightSide, leftSide);
  }
}

class testing {
  public static void main(String[] args) {
    WorldCanvas c = new WorldCanvas(500, 500);
    WorldScene s = new WorldScene(500, 500);
    ITree TREE1 = new Branch(30, 30, 135, 40, new Leaf(10, Color.RED), new Leaf(15, Color.BLUE));
    ITree TREE2 = new Branch(30, 30, 115, 65, new Leaf(15, Color.GREEN), new Leaf(8, Color.ORANGE));

    c.drawScene(s.placeImageXY(TREE1.draw(), 250, 250));
    //c.drawScene(s.placeImageXY(TREE1.combine(40, 50, 150, 30, TREE2).draw(), 250, 250));
    //c.drawScene(s.placeImageXY((new Branch(40, 50, 150, 30, TREE1, TREE2)).draw(), 250, 250));
    c.show();
  }
}