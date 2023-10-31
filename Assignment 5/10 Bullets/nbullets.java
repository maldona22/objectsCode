import tester.Tester;

import java.awt.Color;

import javalib.funworld.*;
import javalib.worldimages.*;

// Template for making predicates for use in higher order functions
interface IPred<T> {
    boolean apply(T t);
}

// Template for making a List containing elements of type T
interface IList<T> {
    // Double dispatch method for equals()
    // Computes if this is equal to list
    boolean equals(IList<T> list);

    // Double dispatch method for equals()
    // Computes if this is equal to a ConsList
    boolean equalsCons(ConsList<T> list);

    // Double dispatch method for equals()
    // Computes if this is equal to a MtList
    boolean equalsMt(MtList<T> list);

    // Returns a list that only contains elements that satisfy the given predicate
    IList<T> filter(IPred<T> pred);

    // Combines two IList's together
    IList<T> append(IList<T> list);

    // Adds an object of type T to the end of this
    IList<T> append(T element);

    // Sorts elements within this based upon the given predicate lambdas
    IList<T> quickSortList(PredicateLambda<T, IPred<T>> smallPred,
            PredicateLambda<T, IPred<T>> largePred);

    // Maps a predicate check to all the elements within this
    boolean andmap(IPred<T> pred);

    // Computes if there are any duplicate elements in this based upon the given predicate lambda
    boolean containsNoDuplicates(PredicateLambda<T, IPred<T>> duplicatePred);

    public <B, C> B foldlAux(Combinator<T, B> combinator, B accum);

    <B, C> B foldl(B base, Combinator<T, B> combinator);
}

// An empty list
class MtList<T> implements IList<T> {
    /* Template
     *
     * Fields:
     * 
     * Methods:
     * this.equals(IList<T> list) -- boolean
     * this.equalsMt(MtList<T> list) -- boolean
     * this.equalsCons(ConsList<T> list) -- boolean
     * this.filter(IPred<T> pred) -- IList<T>
     * this.append(IList<T> list) -- IList<T>
     * this.append(T element) -- IList<T>
     * this.quickSortList(predicateLambda<T,IPred<T>> smallPred, predicateLambda<T,IPred<T>> largePred) -- IList<T>
     * this.andmap(IPred<T> pred) -- boolean
     * this.containsNoDuplicates(predicateLambda<T,IPred<T>> duplicatePred) -- boolean
     * 
     * Methods on fields:
     */

    // Double dispatch method for equals()
    // Computes if this is equal to list
    public boolean equals(IList<T> list) {
        return list.equalsMt(this);
    }

    // Double dispatch method for equals()
    // Computes if this is equal to another MtList
    public boolean equalsMt(MtList<T> list) {
        return true;
    }

    // Double dispatch method for equals()
    // Computes if this is equal to a ConsList
    public boolean equalsCons(ConsList<T> list) {
        return false;
    }

    // Returns a list that only contains elements that satisfy the given predicate
    // Since this only contains itself, and an empty list will always satifsy the predicate
    // Returns itself
    public IList<T> filter(IPred<T> pred) {
        return this;
    }

    // Combines two IList's together
    public IList<T> append(IList<T> list) {
        return list;
    }

    // Adds an object of type T to the end of this
    public IList<T> append(T element) {
        return new ConsList<T>(element, new MtList<T>());
    }

    // Sorts elements within this based upon the given predicate lambdas
    public IList<T> quickSortList(PredicateLambda<T, IPred<T>> smallPred,
            PredicateLambda<T, IPred<T>> largePred) {
        return new MtList<T>();
    }

    // Maps a predicate check to all the elements within this
    public boolean andmap(IPred<T> pred) {
        return true;
    }

    // Computes if there are any duplicate elements in this based upon the given predicate lambda
    public boolean containsNoDuplicates(PredicateLambda<T, IPred<T>> duplicatePred) {
        return true;
    }

    public <B, C> B foldlAux(Combinator<T,B> combinator, B accum) {
        return accum;
    }

    public <B, C> B foldl(B base, Combinator<T,B> combinator) {
        return foldlAux(combinator, base);
    }
}

// A list with one or more elements in it
class ConsList<T> implements IList<T> {
    /* Template
     *
     * Fields:
     * this.first -- T
     * this.rest -- IList<T>
     * 
     * Methods:
     * this.equals(IList<T> list) -- boolean
     * this.equalsMt(MtList<T> list) -- boolean
     * this.equalsCons(ConsList<T> list) -- boolean
     * this.filter(IPred<T> pred) -- IList<T>
     * this.append(IList<T> list) -- IList<T>
     * this.append(T element) -- IList<T>
     * this.quickSortList(predicateLambda<T,IPred<T>> smallPred, predicateLambda<T,IPred<T>> largePred) -- IList<T>
     * this.andmap(IPred<T> pred) -- boolean
     * this.containsNoDuplicates(predicateLambda<T,IPred<T>> duplicatePred) -- boolean
     * <N> ConsList.makePred(predicateLambda<N, IPred<N>> operator, N pivot) -- IPred<N>
     * 
     * Methods on fields:
     * this.rest.equals(IList<T> list) -- boolean
     * this.rest.equalsMt(MtList<T> list) -- boolean
     * this.rest.equalsCons(ConsList<T> list) -- boolean
     * this.rest.filter(IPred<T> pred) -- IList<T>
     * this.rest.append(IList<T> list) -- IList<T>
     * this.rest.append(T element) -- IList<T>
     * this.rest.quickSortList(predicateLambda<T,IPred<T>> smallPred, predicateLambda<T,IPred<T>> largePred) -- IList<T>
     * this.rest.andmap(IPred<T> pred) -- boolean
     * this.rest.containsNoDuplicates(predicateLambda<T,IPred<T>> duplicatePred) -- boolean
     */

    T first;
    IList<T> rest;

    // Constructor for ConsList
    ConsList(T first, IList<T> rest) {
        this.first = first;
        this.rest = rest;
    }

    // Double dispatch method for equals()
    // Computes if this is equal to list
    public boolean equals(IList<T> list) {
        return list.equalsCons(this);
    }

    // Double dispatch method for equals()
    // Computes if this is equal to an MtList
    public boolean equalsMt(MtList<T> list) {
        return false;
    }

    // Double dispatch method for equals()
    // Computes if this is equal to list
    public boolean equalsCons(ConsList<T> list) {
        if (this.first.equals(list.first)) {
            return this.rest.equals(list.rest);
        } else {
            return false;
        }
    }

    // Returns a list that only contains elements that satisfy the given predicate
    public IList<T> filter(IPred<T> pred) {
        if (pred.apply(this.first)) {
            return new ConsList<T>(this.first, this.rest.filter(pred));
        } else {
            return this.rest.filter(pred);
        }
    }

    // Combines two IList's together
    public IList<T> append(IList<T> list) {
        return new ConsList<T>(first, this.rest.append(list));
    }

    // Adds an object of type T to the end of this
    public IList<T> append(T element) {
        return new ConsList<T>(first, new ConsList<T>(element, new MtList<T>()));
    }

    // Maps a predicate check to all the elements within this
    public boolean andmap(IPred<T> pred) {
        return pred.apply(this.first) && this.rest.andmap(pred);
    }

    // Produces a predicate based upon the lambda given and an object of type N
    static <N> IPred<N> makePred(PredicateLambda<N, IPred<N>> operator, N pivot) {
        return operator.op(pivot);
    }

    // Sorts elements within this based upon the given predicate lambdas
    public IList<T> quickSortList(PredicateLambda<T, IPred<T>> smallPred,
            PredicateLambda<T, IPred<T>> largePred) {
        IList<T> smallerList = this.filter(makePred(smallPred, this.first));
        IList<T> biggerList = this.filter(makePred(largePred, this.first));
        return smallerList.quickSortList(smallPred, largePred).append(this.first)
                .append(biggerList.quickSortList(smallPred, largePred));
    }

    // Computes if there are any duplicate elements in this based upon the given predicate lambda
    public boolean containsNoDuplicates(PredicateLambda<T, IPred<T>> duplicatePred) {
        return this.rest.andmap(makePred(duplicatePred, this.first)) && this.rest.containsNoDuplicates(duplicatePred);
    }

    public <B, C> B foldlAux(Combinator<T,B> combinator, B accum) {
        return this.rest.foldlAux(combinator, combinator.apply(first, accum));
    }

    public <B, C> B foldl(B base, Combinator<T,B> combinator) {
        return foldlAux(combinator, base);
    }
}

interface Combinator<T,B> {
    public B apply(T t, B b);
}

// Template for predicate lambdas
interface PredicateLambda<T, R> {
    public R op(T t);
}

interface Coordinate {

}

interface Vector {

}

class ConstProps {
    static final double bulletSpeed = 8.0;
    static final int worldWidth = 800;
    static final int worldHeight = 450;
    static final int initialBulletRadius = 2;
    static final int bulletRadiusGrowth = 2;
    static final int maxBulletRadius = 10;
    static final Color bulletColor = new Color(255, 0, 0);
    static final Color shipColor = new Color(0, 0, 255);
    static final int shipRadius = (worldHeight / 30);
    static final double shipSpeed = (bulletSpeed / 2.0);
    static final Color fontColor = new Color(255, 255, 255);
    static final int fontSize = 13;
    static final int playerRadius = 25;
    static final CartesianVector initialPlayerStartingPoint = new CartesianVector(worldWidth / 2.0, worldHeight);
    static final double tickRate = 1.0 / 28.0;
    static final int shipSpawnRate = 1;
    static final int minimumShipsSpawned = 1;
    static final int maximumShipsSpawned = 3;

    //static final WorldImage bulletImage = new CircleImage(ConstProps.initialBulletRadius, OutlineMode.SOLID, new Color(255, 0, 0));
}
// Only ever need to keep track of 3 vectors max for any given bullet
class PolarVector implements Vector {
    double rComponent;
    double thetaComponent;
    //Coordinate startPoint;

    PolarVector(double rComponent, double thetaComponent) {
        this.rComponent = rComponent;
        this.thetaComponent = thetaComponent;
        //this.startPoint = startPoint;
    }

    CartesianVector convertToCartesian() {
        return new CartesianVector(rComponent * Math.cos(thetaComponent), rComponent * Math.sin(thetaComponent));
    }
}

class CartesianVector implements Vector {
    double xComponent;
    double yComponent;
    //Coordinate startPoint;

    CartesianVector(double xComponent, double yComponent) {
        this.xComponent = xComponent;
        this.yComponent = yComponent;
    }
}

abstract class NPC {
    /*
     Coordinate coords;
    
    NPC(Coordinate coords) {
        this.coords = coords;
    }
     */

    // Might change this to be specific with just aliens, since we can keep them in a separate list
    // double dispatch might not be necessary
    //abstract boolean collidedWith(NPC other);

    //abstract boolean collidedWithAlien(Alien other);

    //abstract boolean collidedWithBullet(Bullet other);
}

class Bullet extends NPC {
    // Doesn't cap? Appears to at times but always seems to get larger, most I've gotten was 25
    // Determines the number of bullets that are released by killed alien
    // AKA how much to multiple this bullet by
    // Evenly spread across in a circle (2 bullets make a -, 3 make a Y, 4 make a +, etc. etc.)
    int multiplier;
    //PolarPt coordsPolar;
    PolarVector coords;
    PolarVector velocityVector;
    PolarVector startPositionVector;

    Bullet(PolarVector velocityVector, PolarVector positionalVector, int multiplier) {
        this.velocityVector = velocityVector;
        this.multiplier = multiplier;
    }

    PolarVector getCurrentPosPolar() {
        return addPolarVectors(startPositionVector,
                new PolarVector(coords.rComponent, coords.thetaComponent));
    }
    
    PolarVector addPolarVectors(PolarVector a, PolarVector b) {
        return new PolarVector(Math.sqrt(Math.pow(a.rComponent, 2) + Math.pow(b.rComponent, 2) + (2 * a.rComponent * b.rComponent * Math.cos(b.thetaComponent - a.thetaComponent))),
            a.thetaComponent + Math.atan2(b.rComponent * Math.sin(b.thetaComponent - a.thetaComponent), a.rComponent + (b.rComponent * Math.cos(b.thetaComponent - a.thetaComponent))));
    }

    IList<Bullet> multiplyHelper(int multipler, double angleOffset, int loop) {
        if (loop != 0) {
            return new ConsList<Bullet>(
                    new Bullet(new PolarVector(ConstProps.bulletSpeed, angleOffset * loop),
                            getCurrentPosPolar(),
                            multipler),
                    multiplyHelper(multipler, angleOffset, loop - 1));
        } else {
            return new MtList<Bullet>();
        }
    }

    void setCoords(PolarVector vector) {
        this.coords = vector;
    }
    
    IList<Bullet> multiply() {
        double angleOffset = 360.0 / this.multiplier;
        return multiplyHelper(multiplier, angleOffset, multiplier);
    }
}



class Alien extends NPC {

}

class Player {

}

class DrawBulletsCombin implements Combinator<Bullet, WorldScene> {
    public WorldScene apply(Bullet bullet, WorldScene scene) {
        CartesianVector convertedCoords = bullet.coords.convertToCartesian();
        WorldImage bulletImage = new CircleImage(ConstProps.initialBulletRadius, OutlineMode.SOLID,
                new Color(255, 0, 0));
        return scene.placeImageXY(bulletImage, (int) convertedCoords.xComponent, (int) convertedCoords.yComponent);
    }
}

class DrawAlienCombin implements Combinator<Alien, WorldScene> {
    public WorldScene apply(Bullet bullet, WorldScene scene) {
        
    }
}

public class nbullets {

    WorldScene drawBullets(WorldScene scene, IList<Bullet> bullets) {
        return bullets.foldl(scene, new DrawBulletsCombin());
    }

    public static void main(String[] args) {
        WorldScene worldScene = new WorldScene(ConstProps.worldWidth, ConstProps.worldHeight);
        // TODO change the default bullet image
        WorldImage bulletImage = new CircleImage(ConstProps.initialBulletRadius, OutlineMode.SOLID, new Color(255, 0, 0));
        // TODO change the default alien image
        WorldImage alienImage = new CircleImage(ConstProps.shipRadius, OutlineMode.SOLID, new Color(0, 0, 255));
        // TODO change the default player image
        WorldImage playerImage = new CircleImage(ConstProps.playerRadius, OutlineMode.SOLID, new Color(0, 255, 0));


        Bullet bullet1 = new Bullet(null, null, 1);
        Bullet bullet2 = new Bullet(null, null, 1);
        Bullet bullet3 = new Bullet(null, null, 1);
        bullet1.coords = new PolarVector(100, Math.PI/4);
        bullet2.setCoords(new PolarVector(75, Math.PI/4));
        bullet3.coords = new PolarVector(50, Math.PI/4);
        World gameWorld = new World() {
            IList<Bullet> bullets = new ConsList<Bullet>(bullet1,new ConsList<Bullet>(bullet2,new ConsList<Bullet>(bullet3, new MtList<Bullet>())));
            //IList<Alien> aliens;
            @Override
            public WorldScene makeScene() {
                return bullets.foldl(worldScene, new DrawBulletsCombin())
                        .placeImageXY(playerImage, (int) ConstProps.initialPlayerStartingPoint.xComponent,
                                (int) ConstProps.initialPlayerStartingPoint.yComponent);
            }
            @Override
            public World onTick() {
                // TODO Auto-generated method stub
                return super.onTick();
            }
            @Override
            public World onKeyEvent(String s) {
                // TODO Auto-generated method stub
                return super.onKeyEvent(s);
            }
        };
        gameWorld.bigBang(ConstProps.worldWidth, ConstProps.worldHeight, 1);
    }
}