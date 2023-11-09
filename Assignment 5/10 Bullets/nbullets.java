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

    // Maps a predicate check to all the elements within this
    boolean andmap(IPred<T> pred);

    public <B, C> B foldlAux(Combinator<T, B> combinator, B accum);

    <B, C> B foldl(B base, Combinator<T, B> combinator);

    <X> IList<X> map(IFunc<T, X> func);

    public IList<T> removeDuplicates();

    public boolean contains(T t);

    public IList<T> removeObject(T t);

    public int length();
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

    // Maps a predicate check to all the elements within this
    public boolean andmap(IPred<T> pred) {
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

    public IList<T> removeObject(T t) {
        return new MtList<T>();
    }

    public int length() {
        return 0;
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

    public IList<T> removeObject(T t) {
        if (this.first.equals(t)) {
            return rest;
        } else {
            return new ConsList<T>(first, rest.removeObject(t));
        }
    }
    
    public int length() {
        return 1 + this.rest.length();
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

    static private final double playerStartHypotenuse = Math
            .sqrt(Math.pow(worldHeight, 2) + Math.pow(worldWidth / 2.0, 2));

    static private final double playerStartAngle = ((Math.PI / 2)
            - Math.asin((worldWidth / 2.0) / playerStartHypotenuse)) * -1.0;
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

    static final boolean doubleEq(double a, double b, double epsilon) {
        return Math.abs(a - b) < epsilon;
    }

    static final double defaultEpsilon = 0.001;
}

class PolarVector implements Vector {
    double rComponent;
    double thetaComponent;
    //Coordinate startPoint;
    
    PolarVector(double rComponent, double thetaComponent) {
        this.rComponent = rComponent;
        this.thetaComponent = thetaComponent;
        //this.startPoint = startPoint;
    }
    
    boolean equals(PolarVector other) {
        return ConstProps.doubleEq(this.rComponent, other.rComponent, ConstProps.defaultEpsilon) &&
                ConstProps.doubleEq(this.thetaComponent, other.thetaComponent, ConstProps.defaultEpsilon);
    }
    
    private CartesianVector convertToCartesian() {
        return new CartesianVector(rComponent * Math.cos(thetaComponent), rComponent * Math.sin(thetaComponent));
    }
    
    double distanceBetweenPoints(CartesianVector a) {
        CartesianVector converted = this.translateToCartesianVector();
        Double distance = Math.sqrt(
                Math.pow(a.xComponent - converted.xComponent, 2) + Math.pow(a.yComponent - converted.yComponent, 2));
        return distance;
    }

    CartesianVector translateToCartesianVector() {
        return new PolarVector(this.rComponent, this.thetaComponent * -1).convertToCartesian();
    }
}
    
class CartesianVector implements Vector {
    Double xComponent;
    Double yComponent;
    //Coordinate startPoint;
    
    CartesianVector(double xComponent, double yComponent) {
        this.xComponent = xComponent;
        this.yComponent = yComponent;
    }
}

// TODO just fix the bullets in general
class Bullet {
    // Doesn't cap? Appears to at times but always seems to get larger, most I've gotten was 25
    // Determines the number of bullets that are released by killed alien
    // AKA how much to multiple this bullet by
    // Evenly spread across in a circle (2 bullets make a -, 3 make a Y, 4 make a +, etc. etc.)
    int multiplier;
    int radius;

    PolarVector coords;
    PolarVector velocityVector;
    PolarVector startPositionVector;

    Bullet(PolarVector coords, PolarVector velocityVector, PolarVector startPositionPolarVector, int radius,
            int multiplier) {
        this.velocityVector = velocityVector;
        this.coords = coords;
        this.multiplier = multiplier;
        this.startPositionVector = startPositionPolarVector;
        this.radius = radius;
    }
    
    boolean equals(Bullet other) {
        return this.multiplier == other.multiplier &&
                this.radius == other.radius &&
                this.coords.equals(other.coords) &&
                this.velocityVector.equals(other.velocityVector) &&
                this.startPositionVector.equals(other.startPositionVector);
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

    PolarVector addPolarVectors(PolarVector a) {
        return addPolarVectors(this.coords, a);
    }

    IList<Bullet> multiplyHelper(int multipler, double angleOffset, int loop) {
        if (loop > 0) {
            return new ConsList<Bullet>(
                    new Bullet(new PolarVector(0.1, angleOffset * loop),
                            new PolarVector(ConstProps.bulletSpeed, angleOffset * loop),
                            getCurrentPosPolar(),
                            updateBulletRadius(multipler),
                            multipler),
                    multiplyHelper(multipler, angleOffset, loop - 1));
        } else {
            return new MtList<Bullet>();
        }
    }
    
    IList<Bullet> multiply() {
        return multiplyHelper(this.multiplier + 1, (2 * Math.PI) / this.multiplier, this.multiplier + 1);
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

class Alien {
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
}

class DrawBulletsCombin implements Combinator<Bullet, WorldScene> {
    public WorldScene apply(Bullet bullet, WorldScene scene) {
        CartesianVector convertedCoords = bullet.getCurrentPosPolar().translateToCartesianVector();
        return scene.placeImageXY(bullet.createBulletImage(), convertedCoords.xComponent.intValue(),
                convertedCoords.yComponent.intValue());
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
        return scene.placeImageXY(ConstProps.alienImage, alien.coords.xComponent.intValue(), alien.coords.yComponent.intValue());
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
        return aliens.filter(new AlienCollisionPred(bullet)).removeDuplicates();
    }

    public IList<Alien> checkBulletsCollidedWith(IList<Alien> aliens, IList<Bullet> bullets) {
        return bullets.foldl(new MtList<Alien>(), new AlienCollisionsCombin(aliens)).removeDuplicates();
    }

    public IList<Bullet> checkAlienCollidedWith(IList<Bullet> bullets, Alien alien) {
        return bullets.filter(new BulletCollisionPred(alien)).removeDuplicates();
    }

    public IList<Bullet> checkAliensCollidedWith(IList<Bullet> bullets, IList<Alien> aliens) {    
        return aliens.foldl(new MtList<Bullet>(), new BulletCollisionsCombin(bullets)).removeDuplicates();
    }

    public IList<Bullet> getMultipliedBullets(IList<Bullet> explodedBullets) {
        if (explodedBullets instanceof ConsList<Bullet>) {
            return ((ConsList<Bullet>) explodedBullets).first.multiply()
                    .append(getMultipliedBullets(((ConsList<Bullet>) explodedBullets).rest));
        }
        else {
            return new MtList<Bullet>();
        }
    }
}

class BulletCollisionPred implements IPred<Bullet> {
    Alien alien;

    public boolean apply(Bullet bullet) {
        return bullet.collidedWithAlien(alien);
    }

    BulletCollisionPred(Alien alien) {
        this.alien = alien;
    }
}

class BulletCollisionsCombin implements Combinator<Alien, IList<Bullet>> {
    IList<Bullet> bullets;
    CollisionProcessing processing = new CollisionProcessing();

    public IList<Bullet> apply(Alien alien, IList<Bullet> accum) {
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
        return processing.checkBulletCollidedWith(aliens, bullet).append(accum);
    }

    AlienCollisionsCombin(IList<Alien> aliens) {
        this.aliens = aliens;
    }
}

class AlienCollisionPred implements IPred<Alien> {
    Bullet bullet;

    public boolean apply(Alien alien) {
        return bullet.collidedWithAlien(alien);
    }
    
    AlienCollisionPred(Bullet bullet) {
        this.bullet = bullet;
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

class BulletOnScreenPred implements IPred<Bullet> {
    public boolean apply(Bullet bullet) {
        CartesianVector currentPos = bullet.getCurrentPosPolar().translateToCartesianVector();

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
        boolean OnScreenX = ConstProps.lessThanEq(currentPos.xComponent, ConstProps.RIGHT_EDGE, ConstProps.defaultEpsilon) && ConstProps.greaterThanEq(currentPos.xComponent, ConstProps.LEFT_EDGE, ConstProps.defaultEpsilon);
        boolean OnScreeny = ConstProps.lessThanEq(currentPos.yComponent, ConstProps.BOTTOM_EDGE, ConstProps.defaultEpsilon) && ConstProps.greaterThanEq(currentPos.yComponent, ConstProps.TOP_EDGE, ConstProps.defaultEpsilon);
        return (OnScreenX && OnScreeny);
    }
}

class NBulletsWorld extends World {
    CollisionProcessing collisionProcessing;
    WorldScene worldScene;
    IList<Bullet> bullets;
    IList<Alien> aliens;
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
        return new Bullet(new PolarVector(0, Math.PI/2.0), new PolarVector(ConstProps.bulletSpeed, Math.PI/2), ConstProps.playerStart, ConstProps.initialBulletRadius, 2);
    }

    public IList<Alien> spawnNewAliens(Random rng) {
        return spawnNewAliensHelper(rng, rng.nextInt(ConstProps.maximumShipsSpawned) + 1); 
    }

    @Override
    public WorldScene makeScene() {
        // TODO fix this to align with the draw order Jason said in instructions
        CartesianVector convertedCoords = ConstProps.playerStart.translateToCartesianVector();
        
        return aliens.foldl(bullets.foldl(worldScene, new DrawBulletsCombin()), new DrawAlienCombin())
                .placeImageXY(ConstProps.playerImage, convertedCoords.xComponent.intValue(),
                        convertedCoords.yComponent.intValue());
    }
    
    @Override
    public World onTick() {
        // TODO if theres time spawn the aliens off the screen then have them start moving inwards
        // would need to change the predicates checking if they're off screen then
        // would make them spawning in look more smooth
        if (shipSpawnTimer == ConstProps.shipSpawnRate) {
            IList<Bullet> step1 = bullets.filter(new BulletOnScreenPred());
            IList<Bullet> explodedBullets = collisionProcessing.checkAliensCollidedWith(bullets, aliens).removeDuplicates();
            IList<Bullet> bulletsRemoved = step1.filter(new DuplicateListPred<Bullet>(explodedBullets));
            IList<Bullet> addMultiplied = bulletsRemoved.append(collisionProcessing.getMultipliedBullets(explodedBullets).removeDuplicates());
            
            IList<Bullet> step2 = step1.foldl(new MtList<Bullet>(),
                    new BulletMultiplyCombin(collisionProcessing.checkAliensCollidedWith(bullets, aliens)));

            IList<Bullet> step3 = addMultiplied.map(new OnTickUpdateBulletCoordsFunc());
            return new NBulletsWorld(playerBulletsLeft, 0, rng, collisionProcessing, worldScene,
                // replace this again once done testing
                step3,
                    aliens.filter(new AlienOnScreenPred())
                            .filter(new DuplicateListPred<Alien>(
                                    collisionProcessing.checkBulletsCollidedWith(aliens, bullets)))
                            .map(new OnTickUpdateAliensCoordsFunc())
                            .append(spawnNewAliens(rng)));
        } else {
            IList<Bullet> step1 = bullets.filter(new BulletOnScreenPred());
            IList<Bullet> explodedBullets = collisionProcessing.checkAliensCollidedWith(bullets, aliens);
            IList<Bullet> bulletsRemoved = step1.filter(new DuplicateListPred<Bullet>(explodedBullets));
            IList<Bullet> addMultiplied = bulletsRemoved.append(collisionProcessing.getMultipliedBullets(explodedBullets));
            
            //IList<Bullet> step2 = step1.foldl(new MtList<Bullet>(), new BulletMultiplyCombin(collisionProcessing.checkAliensCollidedWith(bullets, aliens)));
            IList<Bullet> step3 = addMultiplied.map(new OnTickUpdateBulletCoordsFunc());
            /* 
            IList<Bullet> step2 = step1.foldl(new MtList<Bullet>(),
                            new BulletMultiplyCombin(bullets.filter(new DuplicateListPred<Bullet>(
                            collisionProcessing.checkAliensCollidedWith(bullets, aliens)))));
                            */
            
            return new NBulletsWorld(playerBulletsLeft, shipSpawnTimer + 1, rng, collisionProcessing, worldScene,
                step3,
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
    public static void main(String[] args) {
        NBulletsWorld gameWorld = new NBulletsWorld(10);
        gameWorld.bigBang(ConstProps.worldWidth, ConstProps.worldHeight, ConstProps.tickRate);
    }
}