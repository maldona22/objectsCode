import tester.Tester;

import java.awt.Color;
import java.util.Random;

import javalib.funworld.*;
import javalib.worldimages.*;

// Template for making predicates for use in higher order functions
interface IPred<T> {
    boolean apply(T t);
}

interface IFunc<T, X> {
    X apply(T t);
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

    <X> IList<X> map(IFunc<T, X> func);

    public IList<T> removeDuplicates();

    public boolean contains(T t);
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

    public <B, C> B foldl(B base, Combinator<T, B> combinator) {
        return foldlAux(combinator, base);
    }
    
    public <X> IList<X> map(IFunc<T, X> func) {
        return new MtList<X>();
    }
    
    public boolean contains(T t) {
        return false;
    }

    public IList<T> removeDuplicates() {
        return new MtList<T>();
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

    public <B, C> B foldlAux(Combinator<T, B> combinator, B accum) {
        return this.rest.foldlAux(combinator, combinator.apply(first, accum));
    }

    public <B, C> B foldl(B base, Combinator<T, B> combinator) {
        return foldlAux(combinator, base);
    }

    public <X> IList<X> map(IFunc<T, X> func) {
        return new ConsList<X>(func.apply(first), this.rest.map(func));
    }

    public boolean contains(T t) {
        return first.equals(t) || rest.contains(t);
    }

    public IList<T> removeDuplicates() {
        return new ConsList<T>(first, rest.filter(new DuplicatePred<T>(first)).removeDuplicates());
    }
}

class DuplicatePred<T> implements IPred<T>{
    T x;

    public boolean apply(T y) {
        return !x.equals(y);
    }

    DuplicatePred(T x) {
        this.x = x;
    }
}

class DuplicateListPred<T> implements IPred<T> {
    IList<T> x;

    public boolean apply(T y) {
        return !(x.contains(y));
    }

    DuplicateListPred(IList<T> x) {
        this.x = x;
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
    static final int RIGHT_EDGE = worldWidth;
    static final int LEFT_EDGE = 0;
    static final int TOP_EDGE = 0;
    static final int BOTTOM_EDGE = worldHeight;

    static final int initialBulletRadius = 2;
    static final int bulletRadiusGrowth = 2;
    static final int maxBulletRadius = 10;
    static final Color bulletColor = new Color(255, 0, 0);
    static final Color shipColor = new Color(0, 0, 255);
    static final int shipRadius = (worldHeight / 30);
    static final double shipSpeed = (bulletSpeed / 2.0);
    static final double shipVelocityLeft = -shipSpeed;
    static final double shipVelocityRight = shipSpeed;
    static final double shipSpawnLocationYMin = worldHeight / 7;
    static final double shipSpawnLocationYMax = worldHeight - shipSpawnLocationYMin;

    static final Color fontColor = new Color(255, 255, 255);
    static final int fontSize = 13;
    static final int playerRadius = 25;

    //static final CartesianVector initialPlayerStartingPoint = new CartesianVector(worldWidth / 2.0, worldHeight);
    static final double playerStartHypotenuse = Math
            .sqrt(Math.pow(worldHeight, 2) + Math.pow(worldWidth / 2.0, 2));
    // this should need to be subtracted from 90 but it doesn't??
    // need to figure out what the fuck coordinate system this thing works with
    // TODO fix this shit
    static final double playerStartAngle = ((Math.PI / 2) - Math.asin((worldWidth / 2.0) / playerStartHypotenuse)) * -1.0;
    static final PolarVector playerStart = new PolarVector(playerStartHypotenuse,
            playerStartAngle);

    static final double tickRate = 1.0 / 28.0;
    static final int shipSpawnRate = (int) (1 / tickRate);
    static final int minimumShipsSpawned = 1;
    static final int maximumShipsSpawned = 3;

    // TODO change the default alien image
    static final WorldImage alienImage = new CircleImage(ConstProps.shipRadius, OutlineMode.SOLID,
            new Color(0, 0, 255));
    // TODO change the default player image
    static final WorldImage playerImage = new CircleImage(ConstProps.playerRadius, OutlineMode.SOLID,
            new Color(0, 255, 0));

    static final boolean greaterThanEq(double a, double b, double epsilon) {
        return (a + epsilon >= b) || (a - epsilon >= b);
    }

    static final boolean lessThanEq(double a, double b, double epsilon) {
        return (a + epsilon <= b) || (a - epsilon <= b);
    }

    static final double defaultEpsilon = 0.001;
    /* 
    static final PolarVector translateToJavaLibCoords(PolarVector vector) {
        // TODO figure out what the translation amount for r and theta is
    }
    */
    
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
    
    
    private CartesianVector convertToCartesian() {
        return new CartesianVector(rComponent * Math.cos(thetaComponent), rComponent * Math.sin(thetaComponent));
    }
    
    
    double distanceBetweenPoints(CartesianVector a) {
        //distance formula
        CartesianVector converted = this.translateToCartesianVector();
        return Math.sqrt(
                Math.pow(a.xComponent - converted.xComponent, 2) + Math.pow(a.yComponent - converted.yComponent, 2));
    }

    PolarVector translateVector() {
        return new PolarVector(this.rComponent, this.thetaComponent * -1);
    }

    CartesianVector translateToCartesianVector() {
        return this.translateVector().convertToCartesian();
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

}

class StartPosVector {
    boolean isPlayer;
    PolarVector vector;

    StartPosVector(boolean isPlayer, PolarVector vector) {
        this.isPlayer = isPlayer;
        this.vector = vector;
    }
}

// TODO just fix the bullets in general
class Bullet extends NPC {
    // Doesn't cap? Appears to at times but always seems to get larger, most I've gotten was 25
    // Determines the number of bullets that are released by killed alien
    // AKA how much to multiple this bullet by
    // Evenly spread across in a circle (2 bullets make a -, 3 make a Y, 4 make a +, etc. etc.)
    int multiplier;
    int radius;
    //PolarPt coordsPolar;
    PolarVector coords;
    PolarVector velocityVector;
    PolarVector startPositionVector;
    PolarVector currentPositionVector;

    Bullet(PolarVector coords, PolarVector velocityVector, PolarVector startPositionPolarVector, int radius, int multiplier) {
        this.velocityVector = velocityVector;
        this.coords = coords;
        this.multiplier = multiplier;
        this.startPositionVector = startPositionPolarVector;
        this.radius = radius;
    }

    PolarVector getCurrentPosPolar() {
        return this.addPolarVectors(startPositionVector);
    }

    Bullet onTickUpdateCoords() {
        // Since the bullets never change angles here we only have to account for the added distance traveled
        // However the infrastructure is here to change angles if we wanted to in the code
        PolarVector newCoords = new PolarVector(coords.rComponent + velocityVector.rComponent,
                coords.thetaComponent);
        return new Bullet(newCoords, velocityVector, startPositionVector, radius, multiplier);
    }
    
    static PolarVector addPolarVectors(PolarVector a, PolarVector b) {
        return new PolarVector(
                Math.sqrt(Math.pow(a.rComponent, 2) + Math.pow(b.rComponent, 2)
                        + (2 * a.rComponent * b.rComponent * Math.cos(b.thetaComponent - a.thetaComponent))),
                a.thetaComponent + Math.atan2(b.rComponent * Math.sin(b.thetaComponent - a.thetaComponent),
                        a.rComponent + (b.rComponent * Math.cos(b.thetaComponent - a.thetaComponent))));
    }
    
    /* 
    PolarVector addPolarVectors(StartPosVector a) {
        if (a.isPlayer) {
            return this.coords;
        }
        else {
            return addPolarVectors(this.coords, a.vector);
        }
    }
*/

    PolarVector addPolarVectors(PolarVector a) {
        return addPolarVectors(this.coords, a);
    }

    IList<Bullet> multiplyHelper(int multipler, double angleOffset, int loop) {
        if (loop > 0) {
            return new ConsList<Bullet>(
                    new Bullet(new PolarVector(0, angleOffset * loop),
                    new PolarVector(ConstProps.bulletSpeed, angleOffset * loop),
                            getCurrentPosPolar(),
                            updateBulletRadius(multipler + 1),
                            multipler + 1),
                    multiplyHelper(multipler, angleOffset, loop - 1)
                    );
        } else {
            return new MtList<Bullet>();
        }
    }
    
    IList<Bullet> multiply() {
        double angleOffset = (2 * Math.PI) / this.multiplier;
        return multiplyHelper(multiplier, angleOffset, multiplier);
    }

    boolean collidedWithAlien(Alien alien) {
        return ConstProps.lessThanEq(this.getCurrentPosPolar().distanceBetweenPoints(alien.coords), this.radius + ConstProps.shipRadius, ConstProps.defaultEpsilon);
    }

    int updateBulletRadius(int multiplier) {
        int bulletRadius = ConstProps.initialBulletRadius * multiplier;
        if (bulletRadius > ConstProps.maxBulletRadius) {
            return ConstProps.maxBulletRadius;
        }
        else {
            return bulletRadius;
        }
    }
    // TODO change the default bullet image
    WorldImage createBulletImage() {
        return new CircleImage(this.radius, OutlineMode.SOLID, new Color(255, 0, 0));
    }
}

class Pair<T, X> {
    T t;
    X x;

    Pair(T t, X x) {
        this.t = t;
        this.x = x;
    }
}

class Alien extends NPC {
    CartesianVector velocityVector;
    CartesianVector coords;

    Alien(CartesianVector velocityVector, CartesianVector coords) {
        this.velocityVector = velocityVector;
        this.coords = coords;
    }

    Alien onTickUpdateCoords() {
        return new Alien(velocityVector,
                new CartesianVector(coords.xComponent + velocityVector.xComponent, coords.yComponent));
    }

    //boolean collidedWithAlien()
}

class Player {

}

class DrawBulletsCombin implements Combinator<Bullet, WorldScene> {
    public WorldScene apply(Bullet bullet, WorldScene scene) {
        PolarVector currentPos = bullet.getCurrentPosPolar();
        CartesianVector convertedCoords = currentPos.translateToCartesianVector();
        //System.out.println("currentPos   r: " + currentPos.rComponent + " theta: " + currentPos.thetaComponent);
        //System.out.println("converted    x: " + convertedCoords.xComponent + " y: " + convertedCoords.yComponent);
        return scene.placeImageXY(bullet.createBulletImage(), (int) convertedCoords.xComponent,
                (int) convertedCoords.yComponent);
    }
}

// Could probably reduce the code here if we make ontickupdatecoords an interface method
// Then just make the func take in an npc
class OnTickUpdateBulletCoordsFunc implements IFunc<Bullet, Bullet> {
    public Bullet apply(Bullet bullet) {
        return bullet.onTickUpdateCoords();
    }
}

class OnTickUpdateAliensCoordsFunc implements IFunc<Alien, Alien> {
    public Alien apply(Alien alien) {
        return alien.onTickUpdateCoords();
    }
}

class DrawAlienCombin implements Combinator<Alien, WorldScene> {
    public WorldScene apply(Alien alien, WorldScene scene) {
        return scene.placeImageXY(ConstProps.alienImage, (int) alien.coords.xComponent, (int) alien.coords.yComponent);
    }
}

class CollisionProcessing {
    // que carajo
    // pour que carajo necesito hacer esta mierda

    // Note to abi
    // I have no idea if I even need to do this because we're doing things recursively
    // and not with mutation?
    // Look at every step something can collide with multiple other things
    // And if we're using these specifically for removing things from memory
    // then accidentally trying to delete null is a big risk
    // We might not need to check every time, we might absolutely need to check every time
    // I really hope its the former because I hate this and it would be extremely slow
    public IList<Alien> checkBulletCollidedWith(IList<Alien> aliens, Bullet bullet) {
        return aliens.foldl(new MtList<Alien>(), new AlienCollisionCombin(bullet)).removeDuplicates();
    }

    public IList<Alien> checkBulletsCollidedWith(IList<Alien> aliens, IList<Bullet> bullets) {
        return bullets.foldl(new MtList<Alien>(), new AlienCollisionsCombin(aliens)).removeDuplicates();
    }

    public IList<Bullet> checkAlienCollidedWith(IList<Bullet> bullets, Alien alien) {
        return bullets.foldl(new MtList<Bullet>(), new BulletCollisionCombin(alien)).removeDuplicates();
    }

    public IList<Bullet> checkAliensCollidedWith(IList<Bullet> bullets, IList<Alien> aliens) {
        return aliens.foldl(new MtList<Bullet>(), new BulletCollisionsCombin(bullets)).removeDuplicates();
    }
}

// TODO the single collision classes can probably be rewritten as filters rather than folds?
// if time try this
class BulletCollisionCombin implements Combinator<Bullet, IList<Bullet>> {
    Alien alien;

    BulletCollisionCombin(Alien alien) {
        this.alien = alien;
    }

    public IList<Bullet> apply(Bullet bullet, IList<Bullet> accum) {
        if (bullet.collidedWithAlien(alien)) {
            return new ConsList<Bullet>(bullet, accum);
        } else {
            return accum;
        }
    }
}

class BulletCollisionsCombin implements Combinator<Alien, IList<Bullet>> {
    IList<Bullet> bullets;

    public IList<Bullet> apply(Alien alien, IList<Bullet> accum) {
        CollisionProcessing processing = new CollisionProcessing();
        return processing.checkAlienCollidedWith(bullets, alien).append(accum);
    }

    BulletCollisionsCombin(IList<Bullet> bullets) {
        this.bullets = bullets;
    }
}

class AlienCollisionsCombin implements Combinator<Bullet, IList<Alien>> {
    IList<Alien> aliens;

    public IList<Alien> apply(Bullet bullet, IList<Alien> accum) {
        CollisionProcessing processing = new CollisionProcessing();
        return processing.checkBulletCollidedWith(aliens, bullet);
    }

    AlienCollisionsCombin(IList<Alien> aliens) {
        this.aliens = aliens;
    }
}

class AlienCollisionCombin implements Combinator<Alien, IList<Alien>> {
    Bullet bullet;

    AlienCollisionCombin(Bullet bullet) {
        this.bullet = bullet;
    }

    public IList<Alien> apply(Alien alien, IList<Alien> accum) {
        if (bullet.collidedWithAlien(alien)) {
            return new ConsList<Alien>(alien, accum);
        } else {
            return accum;
        }
    }
}

class BulletMultiplyCombin implements Combinator<Bullet, IList<Bullet>> {
    IList<Bullet> explodedBullets;

    public IList<Bullet> apply(Bullet bullet, IList<Bullet> accum) {
        if (explodedBullets.contains(bullet)) {
            return bullet.multiply().append(accum);
        } else {
            return new ConsList<Bullet>(bullet, accum);
        }
    }
    
    BulletMultiplyCombin(IList<Bullet> explodedBullets) {
        this.explodedBullets = explodedBullets;
    }
}

class AppendCombin implements Combinator<IList<Pair<Bullet, Alien>>, IList<Pair<Bullet, Alien>>> {
    public IList<Pair<Bullet, Alien>> apply(IList<Pair<Bullet, Alien>> list, IList<Pair<Bullet, Alien>> accum) {
        return list.append(accum);
    }
}

class BulletOnScreenPred implements IPred<Bullet> {
    public boolean apply(Bullet bullet) {
        CartesianVector currentPos = bullet.getCurrentPosPolar().translateToCartesianVector();
        System.out.println("x: " + currentPos.xComponent + " y: " + currentPos.yComponent);
        // TODO account for the radius of the bullet here
        boolean OnScreenX = ConstProps.lessThanEq(currentPos.xComponent, ConstProps.RIGHT_EDGE, ConstProps.defaultEpsilon)
                && ConstProps.greaterThanEq(currentPos.xComponent, ConstProps.LEFT_EDGE, ConstProps.defaultEpsilon);
        boolean OnScreeny = ConstProps.lessThanEq(currentPos.yComponent, ConstProps.BOTTOM_EDGE, ConstProps.defaultEpsilon)
                && ConstProps.greaterThanEq(currentPos.yComponent, ConstProps.TOP_EDGE, ConstProps.defaultEpsilon);
        return (OnScreenX && OnScreeny);
    }
}

class AlienOnScreenPred implements IPred<Alien> {
    public boolean apply(Alien alien) {
        CartesianVector currentPos = alien.coords;
        // TODO account for the radius of the alien here
        // TODO change this inequality for the epsilon versions
        Boolean OnScreenX = (currentPos.xComponent <= ConstProps.RIGHT_EDGE) && (currentPos.xComponent >= ConstProps.LEFT_EDGE);
        Boolean OnScreeny = (currentPos.yComponent <= ConstProps.BOTTOM_EDGE) && (currentPos.yComponent >= ConstProps.TOP_EDGE);
        return (OnScreenX && OnScreeny);
    }
}

class NBulletsWorld extends World {
    int playerBulletsLeft;
    int shipSpawnTimer;
    Random rng;

    NBulletsWorld(int numberOfBullets, int shipSpawnTimer, Random randomNumberGenerator, CollisionProcessing collisionProcessing, WorldScene scene, IList<Bullet> bullets, IList<Alien> aliens) {
        if (numberOfBullets > 0) {
            this.playerBulletsLeft = numberOfBullets;
            this.worldScene = scene;
            this.bullets = bullets;
            this.aliens = aliens;
            this.shipSpawnTimer = shipSpawnTimer;
            this.rng = randomNumberGenerator;
            this.collisionProcessing = collisionProcessing;
        } else {
            throw new IllegalArgumentException();
        }
    }

    NBulletsWorld(int numberOfBullets, Random randomNumberGenerator) {
        this(numberOfBullets, 0, randomNumberGenerator, new CollisionProcessing(), new WorldScene(ConstProps.worldWidth, ConstProps.worldHeight),
                new MtList<Bullet>(), new MtList<Alien>());
    }
    
    NBulletsWorld(int numberOfBullets) {
        this(numberOfBullets, 0, new Random(), new CollisionProcessing(), new WorldScene(ConstProps.worldWidth, ConstProps.worldHeight), new MtList<Bullet>(), new MtList<Alien>());
    }
    
    // TODO remove the world scene from the world struct, not needed makescene handles it
    WorldScene worldScene = new WorldScene(ConstProps.worldWidth, ConstProps.worldHeight);
    IList<Bullet> bullets;
    IList<Alien> aliens;

    CollisionProcessing collisionProcessing;

    public void printBullets(IList<Bullet> list) {
        if (list instanceof ConsList<Bullet>) {
            System.out.println("r: " + ((ConsList<Bullet>) list).first.coords.rComponent + " theta: "
                    + ((ConsList<Bullet>) list).first.coords.thetaComponent);
            printBullets(((ConsList<Bullet>)list).rest);
        }
        else {
            return;
        }
    }

    public IList<Alien> spawnNewAliensHelper(Random rng, int aliensLeft) {
        if (aliensLeft > 0) {
            if (rng.nextInt(2) == 0) {
                return new ConsList<Alien>(
                        new Alien(new CartesianVector(ConstProps.shipVelocityRight, 0), new CartesianVector(
                                ConstProps.LEFT_EDGE,
                                rng.nextInt((int) (ConstProps.shipSpawnLocationYMax - ConstProps.shipSpawnLocationYMin))
                                        + ConstProps.shipSpawnLocationYMin)),
                        spawnNewAliensHelper(rng, aliensLeft - 1));
            } else {
                return new ConsList<Alien>(
                        new Alien(new CartesianVector(ConstProps.shipVelocityLeft, 0), new CartesianVector(
                                ConstProps.RIGHT_EDGE,
                                rng.nextInt((int) (ConstProps.shipSpawnLocationYMax - ConstProps.shipSpawnLocationYMin))
                                        + ConstProps.shipSpawnLocationYMin)),
                        spawnNewAliensHelper(rng, aliensLeft - 1));
            }
        } else {
            if (rng.nextInt(2) == 0) {
                return new ConsList<Alien>(new Alien(new CartesianVector(ConstProps.shipVelocityRight, 0),
                        new CartesianVector(ConstProps.LEFT_EDGE,
                                rng.nextInt((int) (ConstProps.shipSpawnLocationYMax - ConstProps.shipSpawnLocationYMin))
                                        + ConstProps.shipSpawnLocationYMin)),
                        new MtList<Alien>());
            } else {
                return new ConsList<Alien>(new Alien(new CartesianVector(ConstProps.shipVelocityLeft, 0),
                        new CartesianVector(ConstProps.RIGHT_EDGE,
                                rng.nextInt((int) (ConstProps.shipSpawnLocationYMax - ConstProps.shipSpawnLocationYMin))
                                        + ConstProps.shipSpawnLocationYMin)),
                        new MtList<Alien>());
            }
        }
    }
    
    public Bullet createNewShot() {
        // TODO just fix the bullet angles in general
        if (bullets instanceof ConsList<Bullet>) {
            System.out.println("there are bullets");
            printBullets(bullets);
        }
        else {
            System.out.println("nope");
        }
        return new Bullet(new PolarVector(0, Math.PI/2.0), new PolarVector(ConstProps.bulletSpeed, Math.PI/2), ConstProps.playerStart, ConstProps.initialBulletRadius, 1);
    }

    public IList<Alien> spawnNewAliens(Random rng) {
        return spawnNewAliensHelper(rng, rng.nextInt(ConstProps.maximumShipsSpawned) + 1); 
    }

    @Override
    public WorldScene makeScene() {
        // TODO fix this to align with the draw order Jason said in instructions
        CartesianVector convertedCoords = ConstProps.playerStart.translateToCartesianVector();
        //System.out.println("x: " + convertedCoords.xComponent + " y: " + convertedCoords.yComponent);
        //CartesianVector convertedCoords = (new PolarVector(ConstProps.translatedPlayerStartHypotenuse, 0)).convertToCartesian();
        return aliens.foldl(bullets.foldl(worldScene, new DrawBulletsCombin()), new DrawAlienCombin())
                     .placeImageXY(ConstProps.playerImage, (int) convertedCoords.xComponent,
                        (int) convertedCoords.yComponent);
    }
    @Override
    public World onTick() {
        // TODO if theres time spawn the aliens off the screen then have them start moving inwards
        // would need to change the predicates checking if they're off screen then
        // would make them spawning in look more smooth
        if (shipSpawnTimer == ConstProps.shipSpawnRate) {
            IList<Bullet> step1 = bullets.filter(new BulletOnScreenPred());
            IList<Bullet> step2 = step1.foldl(new MtList<Bullet>(),
                                    new BulletMultiplyCombin(collisionProcessing.checkAliensCollidedWith(bullets, aliens)));
            IList<Bullet> step3 = step2.map(new OnTickUpdateBulletCoordsFunc());
            return new NBulletsWorld(playerBulletsLeft, 0, rng, collisionProcessing, worldScene,
                // replace this again once done testing
                step3,
                /* 
                    bullets.filter(new BulletOnScreenPred())
                            .foldl(new MtList<Bullet>(),
                                    new BulletMultiplyCombin(bullets.filter(new DuplicateListPred<Bullet>(
                                            collisionProcessing.checkAliensCollidedWith(bullets, aliens)))))
                            .map(new OnTickUpdateBulletCoordsFunc()),
                */
                    aliens.filter(new AlienOnScreenPred())
                            .filter(new DuplicateListPred<Alien>(
                                    collisionProcessing.checkBulletsCollidedWith(aliens, bullets)))
                            .map(new OnTickUpdateAliensCoordsFunc())
                            .append(spawnNewAliens(rng)));
        } else {
            IList<Bullet> step1 = bullets.filter(new BulletOnScreenPred());
            IList<Bullet> step2 = step1.foldl(new MtList<Bullet>(),
                    new BulletMultiplyCombin(collisionProcessing.checkAliensCollidedWith(bullets, aliens)));
            IList<Bullet> step3 = step2.map(new OnTickUpdateBulletCoordsFunc());
            /* 
            IList<Bullet> step2 = step1.foldl(new MtList<Bullet>(),
                            new BulletMultiplyCombin(bullets.filter(new DuplicateListPred<Bullet>(
                            collisionProcessing.checkAliensCollidedWith(bullets, aliens)))));
                            */
            
            return new NBulletsWorld(playerBulletsLeft, shipSpawnTimer + 1, rng, collisionProcessing, worldScene,
                step3,
                /* 
                    bullets.foldl(new MtList<Bullet>(),
                            new BulletMultiplyCombin(bullets.filter(new DuplicateListPred<Bullet>(
                                    collisionProcessing.checkAliensCollidedWith(bullets, aliens)))))
                            .map(new OnTickUpdateBulletCoordsFunc()),
                    */
                    aliens.filter(
                            new DuplicateListPred<Alien>(collisionProcessing.checkBulletsCollidedWith(aliens, bullets)))
                            .map(new OnTickUpdateAliensCoordsFunc()));
        }
    }

    // TODO need to write a game ending scene
    /* 
    public WorldScene makeLastScene() {
        
    }
    
    @Override
    public WorldEnd worldEnds() {
        if (playerBulletsLeft == 0 && bullets.equals(new MtList<Bullet>())) {
            return new WorldEnd(true, this.makeLastScene());
        } else {
            return new WorldEnd(false, this.makeScene());
        }
    }
    */
    @Override
    public World onKeyEvent(String s) {
        // TODO Add in shots when spacebar pressed
        if (s.equals(" ")) {
            return new NBulletsWorld(playerBulletsLeft - 1, shipSpawnTimer, rng, collisionProcessing, worldScene,
                    bullets.append(createNewShot()),
                    aliens);
        }
        return new NBulletsWorld(playerBulletsLeft, shipSpawnTimer, rng, collisionProcessing, worldScene, bullets,
                aliens);
    }
}

public class nbullets {

    WorldScene drawBullets(WorldScene scene, IList<Bullet> bullets) {
        return bullets.foldl(scene, new DrawBulletsCombin());
    }

    public static void main(String[] args) {
        NBulletsWorld gameWorld = new NBulletsWorld(10);
        gameWorld.bigBang(ConstProps.worldWidth, ConstProps.worldHeight, ConstProps.tickRate);
    }
}