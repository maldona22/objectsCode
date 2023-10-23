class Direction {
    String description;
    int miles;

    Direction(String description, int miles) {
        this.description = description;
        this.miles = miles;
    }

    public boolean isSplittable(int mileSplit) {
        return miles >= mileSplit;
    }
}

interface ILoDirection { }
class ConsLoDirection implements ILoDirection {
    Direction first;
    ILoDirection rest;

    ConsLoDirection(Direction first, ILoDirection rest) {
        this.first = first;
        this.rest = rest;
    }
}

class MtLoDirection implements ILoDirection {
    MtLoDirection() { }
}
class RoadTripChunk {
    String driver;
    ConsLoDirection directions;

    RoadTripChunk(String driver, ConsLoDirection directions) {
        this.driver = driver;
        this.directions = directions;
    }
}

interface ILoRoadTripChunk {
}

class ConsLoRoadTripChunk implements ILoRoadTripChunk{
    RoadTripChunk first;
    ILoRoadTripChunk rest;

    ConsLoRoadTripChunk(RoadTripChunk first, ILoRoadTripChunk rest) {
        this.first = first;
        this.rest = rest;
    }
}

class MtLoRoadTripChunk implements ILoRoadTripChunk { }

public class RoadTrip {
    String driver1;
    String driver2;
    ConsLoDirection directions;
    ConsLoRoadTripChunk rtChunks;

    boolean longDir = false;
    String longDirDesc;
    int overallMileSplit;



    RoadTrip(String driver1, String driver2, ConsLoDirection directions) {
        this.driver1 = driver1;
        this.driver2 = driver2;
        this.directions = directions;
    }

    public static void printDirections(ConsLoDirection dirs){
    if (dirs.rest instanceof ConsLoDirection) {
        System.out.println(dirs.first.description + " " + dirs.first.miles +"\n");
        printDirections((ConsLoDirection) dirs.rest);
    }
    else {
        System.out.println(dirs.first.description + " " + dirs.first.miles);
       }
    }

    public ConsLoDirection splitUpDirection(ConsLoDirection dirs, int mileSplit, boolean isDriverOneActive) {
        Direction firstDir = dirs.first;
        String activeDriver;
        if (isDriverOneActive)
        {
            activeDriver = driver1;
        }
        else
        {
            activeDriver = driver2;
        }
        if (dirs.rest instanceof ConsLoDirection)
        {
            if (firstDir.isSplittable(mileSplit)) {
                if (longDir) {
                    Direction newFirstDir = new Direction("Switch with " + activeDriver, mileSplit);
                    Direction newSecondDir = new Direction(longDirDesc, firstDir.miles - mileSplit);
                    return new ConsLoDirection(newFirstDir, new ConsLoDirection(newSecondDir, dirs.rest));
                } else {
                    if (firstDir.miles > overallMileSplit) {
                        longDir = true;
                        longDirDesc = firstDir.description;

                        Direction newFirstDir = new Direction("Switch with " + activeDriver, mileSplit);
                        Direction newSecondDir = new Direction(longDirDesc, firstDir.miles - mileSplit);
                        return new ConsLoDirection(newFirstDir,
                                new ConsLoDirection(newSecondDir, dirs.rest));
                    } 
                      else if (firstDir.miles > mileSplit) {
                        Direction newFirstDir = new Direction("Switch with " + activeDriver, mileSplit);
                        Direction newSecondDir = new Direction(firstDir.description, firstDir.miles - mileSplit);
                        return new ConsLoDirection(newFirstDir,
                                new ConsLoDirection(newSecondDir, dirs.rest));
                      }
                    else if (firstDir.miles == mileSplit) {
                        Direction newFirstDir = new Direction(firstDir.description, mileSplit);
                        Direction newSecondDir = new Direction("Switch with " + activeDriver, 0);
                        return new ConsLoDirection(newFirstDir,
                                new ConsLoDirection(newSecondDir, dirs.rest));
                    }
                }
            }
            else {
                longDir = false;
                longDirDesc = null;
                return dirs;
            }
        }
        return dirs;
    }

    public ConsLoDirection splitUpDirections(int mileSplit, int milesTraversed, boolean isDriverOneActive, ConsLoDirection unProcessedDirs,
            ConsLoDirection processedDirs) {
        overallMileSplit = mileSplit;
        ConsLoDirection firstProcessed = splitUpDirection(unProcessedDirs, mileSplit - milesTraversed,
                !isDriverOneActive);

        printDirections(firstProcessed);
        if (unProcessedDirs.rest instanceof ConsLoDirection) {
            if ((mileSplit - milesTraversed - firstProcessed.first.miles) == 0) {
                return splitUpDirections(mileSplit, 0, !isDriverOneActive, (ConsLoDirection) firstProcessed.rest,
                        new ConsLoDirection(firstProcessed.first, processedDirs));
            } else {
                return splitUpDirections(mileSplit, milesTraversed + firstProcessed.first.miles, isDriverOneActive,
                        (ConsLoDirection) firstProcessed.rest,
                        new ConsLoDirection(firstProcessed.first, processedDirs));
            }
        } else {
            return new ConsLoDirection(firstProcessed.first, processedDirs);
        }
    }

    public ConsLoRoadTripChunk convertDirs(ConsLoRoadTripChunk chunks, ILoDirection dirs, int milesTraversed,
            int mileSplit, boolean isDriverOneActive) {
        if (dirs instanceof ConsLoDirection) {
            if (((ConsLoDirection) dirs).rest instanceof ConsLoDirection) {
                int newMilesTraveresed = ((ConsLoDirection) dirs).first.miles + milesTraversed;
                if (newMilesTraveresed == mileSplit) {
                    chunks.first.directions = new ConsLoDirection(((ConsLoDirection) dirs).first,
                            chunks.first.directions);
                    return convertDirs(
                            new ConsLoRoadTripChunk(new RoadTripChunk(!isDriverOneActive ? driver1 : driver2,
                                    ((ConsLoDirection) directions.rest)), chunks),
                            ((ConsLoDirection) dirs).rest, 0, mileSplit, !isDriverOneActive);
                } else {
                    chunks.first.directions = new ConsLoDirection(((ConsLoDirection) dirs).first,
                            chunks.first.directions);
                    return convertDirs(
                            chunks,
                            ((ConsLoDirection) dirs).rest, newMilesTraveresed, mileSplit, !isDriverOneActive);
                }
            } else {
                chunks.first.directions = new ConsLoDirection(((ConsLoDirection) dirs).first,
                        chunks.first.directions);
                return chunks;
            }
        } else {
            return chunks;
        }
    }
    
    public ConsLoRoadTripChunk splitUpTrip(RoadTrip rt, int mileSplit) {
        ConsLoDirection dirs = rt.splitUpDirections(mileSplit, rt.directions.first.miles, true, rt.directions,
                new ConsLoDirection(rt.directions.first, new MtLoDirection()));
        printDirections(dirs);
        return convertDirs(new ConsLoRoadTripChunk(new RoadTripChunk(driver1, new ConsLoDirection(dirs.first, new MtLoDirection())), new MtLoRoadTripChunk()), dirs.rest, 0, mileSplit, true);
    }
    
    public static void main(String[] args) {
        Direction testDir1 = new Direction("Make a left at Alberquerque", 13);
        Direction testDir2 = new Direction("Make a right at the fork", 2);
        Direction testDir3 = new Direction("Make a left at the next fork", 3);
        Direction testDir4 = new Direction("Take the overpass", 45);
        Direction testDir5 = new Direction("Destination on left", 12);
        ConsLoDirection testDirs = new ConsLoDirection(testDir2, new ConsLoDirection(testDir3,
                new ConsLoDirection(testDir4, new ConsLoDirection(testDir5, new MtLoDirection()))));
        RoadTrip testRT = new RoadTrip("Hazel", "Henry", testDirs);
        // Have to do the first one ourselves to make it work recursively?
        ConsLoDirection testResults = testRT.splitUpDirections(15, 13, true, testDirs,
                new ConsLoDirection(testDir1, new MtLoDirection()));
        
        System.out.println("\nresults\n");
        
    }
}