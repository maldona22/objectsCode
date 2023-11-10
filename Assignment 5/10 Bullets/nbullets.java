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


class ConstProps {
    final double bulletSpeed = 8.0;
    final int worldWidth = 800;
    final int worldHeight = 450;
    final int RIGHT_EDGE = worldWidth;
    final int LEFT_EDGE = 0;
    final int TOP_EDGE = 0;
    final int BOTTOM_EDGE = worldHeight;
    final int initialBulletRadius = 2;
    final int bulletRadiusGrowth = 2;
    final int maxBulletRadius = 10;
    final Color bulletColor = new Color(255, 0, 0);
    final Color shipColor = new Color(0, 0, 255);
    final int shipRadius = (worldHeight / 30);
    final double shipSpeed = (bulletSpeed / 2.0);
    final double shipVelocityLeft = -shipSpeed;
    final double shipVelocityRight = shipSpeed;
    final double shipSpawnLocationYMin = worldHeight / 7;
    final double shipSpawnLocationYMax = worldHeight - shipSpawnLocationYMin;
    final Color fontColor = new Color(255, 255, 255);
    final int fontSize = 13;
    final int playerRadius = 25;
    private final double playerStartHypotenuse = Math.sqrt(Math.pow(worldHeight, 2) + Math.pow(worldWidth / 2.0, 2));
    private final double playerStartAngle = ((Math.PI / 2) - Math.asin((worldWidth / 2.0) / playerStartHypotenuse)) * -1.0;
    final PolarVector playerStart = new PolarVector(playerStartHypotenuse, playerStartAngle, this);
    final double tickRate = 1.0 / 28.0;
    final int shipSpawnRate = ((int) (1 / tickRate));
    final int minimumShipsSpawned = 1;
    final int maximumShipsSpawned = 3;
    final WorldImage alienImage = new CircleImage(shipRadius, OutlineMode.SOLID, new Color(0, 0, 255));
    final WorldImage playerImage = new CircleImage(playerRadius, OutlineMode.SOLID, new Color(0, 255, 0));
    final boolean greaterThanEq(double a, double b, double epsilon) {
        return (a + epsilon >= b) || (a - epsilon >= b);
    }

    final boolean lessThanEq(double a, double b, double epsilon) {
        return (a + epsilon <= b) || (a - epsilon <= b);
    }

    final boolean doubleEq(double a, double b, double epsilon) {
        return Math.abs(a - b) < epsilon;
    }

    final double defaultEpsilon = 0.001;

    ConstProps() {
        
    }
}

class PolarVector {
    double rComponent;
    double thetaComponent;
    ConstProps constProps;
    
    PolarVector(double rComponent, double thetaComponent, ConstProps constProps) {
        this.rComponent = rComponent;
        this.thetaComponent = thetaComponent;
        this.constProps = constProps;
    }
    
    boolean equals(PolarVector other) {
        return constProps.doubleEq(this.rComponent, other.rComponent, constProps.defaultEpsilon) &&
                constProps.doubleEq(this.thetaComponent, other.thetaComponent, constProps.defaultEpsilon);
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
        return new PolarVector(this.rComponent, this.thetaComponent * -1, this.constProps).convertToCartesian();
    }
}
    
class CartesianVector {
    Double xComponent;
    Double yComponent;
    
    CartesianVector(double xComponent, double yComponent) {
        this.xComponent = xComponent;
        this.yComponent = yComponent;
    }
}

class Bullet {
    int multiplier;
    int radius;

    PolarVector coords;
    PolarVector velocityVector;
    PolarVector startPositionVector;

    ConstProps constProps;

    Bullet(PolarVector coords, PolarVector velocityVector, PolarVector startPositionPolarVector, int radius,
            int multiplier, ConstProps constProps) {
        this.velocityVector = velocityVector;
        this.coords = coords;
        this.multiplier = multiplier;
        this.startPositionVector = startPositionPolarVector;
        this.radius = radius;
        this.constProps = constProps;
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
                coords.thetaComponent, constProps);
        return new Bullet(newCoords, velocityVector, startPositionVector, radius, multiplier, this.constProps);
    }
    
    PolarVector addPolarVectors(PolarVector a, PolarVector b) {
        return new PolarVector(
                Math.sqrt(Math.pow(a.rComponent, 2) + Math.pow(b.rComponent, 2)
                        + (2 * a.rComponent * b.rComponent * Math.cos(b.thetaComponent - a.thetaComponent))),
                a.thetaComponent + Math.atan2(b.rComponent * Math.sin(b.thetaComponent - a.thetaComponent),
                        a.rComponent + (b.rComponent * Math.cos(b.thetaComponent - a.thetaComponent))), this.constProps);
    }

    PolarVector addPolarVectors(PolarVector a) {
        return addPolarVectors(this.coords, a);
    }

    IList<Bullet> multiplyHelper(int multipler, double angleOffset, int loop) {
        if (loop > 0) {
            return new ConsList<Bullet>(
                    new Bullet(new PolarVector(0.1, angleOffset * loop, this.constProps),
                            new PolarVector(constProps.bulletSpeed, angleOffset * loop, this.constProps),
                            getCurrentPosPolar(),
                            updateBulletRadius(multipler),
                            multipler, this.constProps),
                    multiplyHelper(multipler, angleOffset, loop - 1));
        } else {
            return new MtList<Bullet>();
        }
    }
    
    IList<Bullet> multiply() {
        return multiplyHelper(this.multiplier + 1, (2 * Math.PI) / this.multiplier, this.multiplier + 1);
    }

    boolean collidedWithAlien(Alien alien) {
        return constProps.lessThanEq(this.getCurrentPosPolar().distanceBetweenPoints(alien.coords), this.radius + constProps.shipRadius, constProps.defaultEpsilon);
    }

    int updateBulletRadius(int multiplier) {
        int bulletRadius = constProps.initialBulletRadius * multiplier;
        if (bulletRadius > constProps.maxBulletRadius) {
            return constProps.maxBulletRadius;
        } else {
            return bulletRadius;
        }
    }
    
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
    ConstProps constProps;
    public WorldScene apply(Alien alien, WorldScene scene) {
        return scene.placeImageXY(constProps.alienImage, alien.coords.xComponent.intValue(),
                alien.coords.yComponent.intValue());
    }
    
    DrawAlienCombin(ConstProps constProps) {
        this.constProps = constProps;
    }
}

class CollisionProcessing {
    public IList<Alien> checkBulletCollidedWith(IList<Alien> aliens, Bullet bullet) {
        return aliens.filter(new AlienCollisionPred(bullet));
    }

    public IList<Alien> checkBulletsCollidedWith(IList<Alien> aliens, IList<Bullet> bullets) {
        return bullets.foldl(new MtList<Alien>(), new AlienCollisionsCombin(aliens));
    }

    public IList<Bullet> checkAlienCollidedWith(IList<Bullet> bullets, Alien alien) {
        return bullets.filter(new BulletCollisionPred(alien));
    }

    public IList<Bullet> checkAliensCollidedWith(IList<Bullet> bullets, IList<Alien> aliens) {    
        return aliens.foldl(new MtList<Bullet>(), new BulletCollisionsCombin(bullets));
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
    ConstProps constProps;
    public boolean apply(Bullet bullet) {
        CartesianVector currentPos = bullet.getCurrentPosPolar().translateToCartesianVector();
        boolean OnScreenX = constProps.lessThanEq(currentPos.xComponent, constProps.RIGHT_EDGE,
                constProps.defaultEpsilon)
                && constProps.greaterThanEq(currentPos.xComponent, constProps.LEFT_EDGE, constProps.defaultEpsilon);
        boolean OnScreeny = constProps.lessThanEq(currentPos.yComponent, constProps.BOTTOM_EDGE,
                constProps.defaultEpsilon)
                && constProps.greaterThanEq(currentPos.yComponent, constProps.TOP_EDGE, constProps.defaultEpsilon);
        return (OnScreenX && OnScreeny);
    }
    
    BulletOnScreenPred(ConstProps constProps) {
        this.constProps = constProps;
    }
}

class AlienOnScreenPred implements IPred<Alien> {
    ConstProps constProps;

    public boolean apply(Alien alien) {
        CartesianVector currentPos = alien.coords;
        boolean OnScreenX = constProps.lessThanEq(currentPos.xComponent, constProps.RIGHT_EDGE,
                constProps.defaultEpsilon)
                && constProps.greaterThanEq(currentPos.xComponent, constProps.LEFT_EDGE, constProps.defaultEpsilon);
        boolean OnScreeny = constProps.lessThanEq(currentPos.yComponent, constProps.BOTTOM_EDGE,
                constProps.defaultEpsilon)
                && constProps.greaterThanEq(currentPos.yComponent, constProps.TOP_EDGE, constProps.defaultEpsilon);
        return (OnScreenX && OnScreeny);
    }
    
    AlienOnScreenPred(ConstProps constProps) {
        this.constProps = constProps;
    }
}

class NBulletsWorld extends World {
    CollisionProcessing collisionProcessing;
    ConstProps constProps;
    WorldScene worldScene;
    IList<Bullet> bullets;
    IList<Alien> aliens;
    int playerBulletsLeft;
    int shipSpawnTimer;
    Random rng;

    NBulletsWorld(int numberOfBullets, int shipSpawnTimer, Random randomNumberGenerator, CollisionProcessing collisionProcessing, WorldScene scene, IList<Bullet> bullets, IList<Alien> aliens, ConstProps constProps) {
        if (numberOfBullets >= 0) {
            this.playerBulletsLeft = numberOfBullets;
            this.worldScene = new WorldScene(constProps.worldWidth, constProps.worldHeight);
            this.bullets = bullets;
            this.aliens = aliens;
            this.shipSpawnTimer = shipSpawnTimer;
            this.rng = randomNumberGenerator;
            this.collisionProcessing = collisionProcessing;
            this.constProps = constProps;
        } else {
            throw new IllegalArgumentException();
        }
    }

    NBulletsWorld(int numberOfBullets, Random randomNumberGenerator) {
        this(numberOfBullets, 0, randomNumberGenerator, new CollisionProcessing(), null,
                new MtList<Bullet>(), new MtList<Alien>(), new ConstProps());
    }
    
    NBulletsWorld(int numberOfBullets) {
        this(numberOfBullets, 0, new Random(), new CollisionProcessing(), null, new MtList<Bullet>(), new MtList<Alien>(), new ConstProps());
    }

    public IList<Alien> spawnNewAliensHelper(Random rng, int aliensLeft) {
        if (aliensLeft > 0) {
            if (rng.nextInt(2) == 0) {
                return new ConsList<Alien>(
                        new Alien(new CartesianVector(constProps.shipVelocityRight, 0), new CartesianVector(
                                constProps.LEFT_EDGE,
                                rng.nextInt((int) (constProps.shipSpawnLocationYMax - constProps.shipSpawnLocationYMin))
                                        + constProps.shipSpawnLocationYMin)),
                        spawnNewAliensHelper(rng, aliensLeft - 1));
            } else {
                return new ConsList<Alien>(
                        new Alien(new CartesianVector(constProps.shipVelocityLeft, 0), new CartesianVector(
                                constProps.RIGHT_EDGE,
                                rng.nextInt((int) (constProps.shipSpawnLocationYMax - constProps.shipSpawnLocationYMin))
                                        + constProps.shipSpawnLocationYMin)),
                        spawnNewAliensHelper(rng, aliensLeft - 1));
            }
        } else {
            if (rng.nextInt(2) == 0) {
                return new ConsList<Alien>(new Alien(new CartesianVector(constProps.shipVelocityRight, 0),
                        new CartesianVector(constProps.LEFT_EDGE,
                                rng.nextInt((int) (constProps.shipSpawnLocationYMax - constProps.shipSpawnLocationYMin))
                                        + constProps.shipSpawnLocationYMin)),
                        new MtList<Alien>());
            } else {
                return new ConsList<Alien>(new Alien(new CartesianVector(constProps.shipVelocityLeft, 0),
                        new CartesianVector(constProps.RIGHT_EDGE,
                                rng.nextInt((int) (constProps.shipSpawnLocationYMax - constProps.shipSpawnLocationYMin))
                                        + constProps.shipSpawnLocationYMin)),
                        new MtList<Alien>());
            }
        }
    }
    
    public Bullet createNewShot() {
        return new Bullet(new PolarVector(0, Math.PI/2.0, constProps), new PolarVector(constProps.bulletSpeed, Math.PI/2.0, constProps), constProps.playerStart, constProps.initialBulletRadius, 2, constProps);
    }

    public IList<Alien> spawnNewAliens(Random rng) {
        return spawnNewAliensHelper(rng, rng.nextInt(constProps.maximumShipsSpawned) + 1); 
    }

    @Override
    public WorldScene makeScene() {
        CartesianVector convertedCoords = constProps.playerStart.translateToCartesianVector();
        
        return aliens.foldl(bullets.foldl(worldScene, new DrawBulletsCombin()), new DrawAlienCombin(constProps))
                .placeImageXY(constProps.playerImage, convertedCoords.xComponent.intValue(),
                        convertedCoords.yComponent.intValue());
    }
    
    @Override
    public World onTick() {
        IList<Bullet> explodedBullets = collisionProcessing.checkAliensCollidedWith(bullets, aliens);
        IList<Bullet> newBullets = bullets.filter(new BulletOnScreenPred(constProps))
                                            .foldl(new MtList<Bullet>(), new BulletMultiplyCombin(explodedBullets))
                                            .map(new OnTickUpdateBulletCoordsFunc());
        IList<Alien> newAliens = aliens.filter(new AlienOnScreenPred(constProps))
                                        .filter(new DuplicateListPred<Alien>(
                                                collisionProcessing.checkBulletsCollidedWith(aliens, bullets)))
                                        .map(new OnTickUpdateAliensCoordsFunc())
                                        ;
        if (shipSpawnTimer == constProps.shipSpawnRate) {
            return new NBulletsWorld(playerBulletsLeft, 0, rng, collisionProcessing, worldScene, newBullets, newAliens.append(spawnNewAliens(rng)), constProps);
        } else {
            return new NBulletsWorld(playerBulletsLeft, shipSpawnTimer + 1, rng, collisionProcessing, worldScene, newBullets, newAliens, constProps);
        }
    }

    public WorldScene makeLastScene() {
        return worldScene.placeImageXY(new TextImage("YOU WIN", 48, new Color(255,255,255)), constProps.worldWidth/2, constProps.worldHeight/2);
    }
    
    @Override
    public WorldEnd worldEnds() {
        if (playerBulletsLeft == 0 && bullets.equals(new MtList<Bullet>())) {
            return new WorldEnd(true, this.makeLastScene());
        } else {
            return new WorldEnd(false, this.makeScene());
        }
    }

    @Override
    public World onKeyEvent(String s) {
        System.out.println(playerBulletsLeft);
        if (s.equals(" ") && playerBulletsLeft > 0) {
            return new NBulletsWorld(playerBulletsLeft - 1, shipSpawnTimer, rng, collisionProcessing, worldScene,
                    new ConsList<Bullet>(createNewShot(), bullets),
                    aliens, constProps);
        }
        return new NBulletsWorld(playerBulletsLeft, shipSpawnTimer, rng, collisionProcessing, worldScene, bullets,
                aliens, constProps);
    }
}

public class nbullets {
    public static void main(String[] args) {
        ConstProps constProps = new ConstProps();
        NBulletsWorld gameWorld = new NBulletsWorld(10);
        gameWorld.bigBang(constProps.worldWidth, constProps.worldHeight, constProps.tickRate);
    }
}