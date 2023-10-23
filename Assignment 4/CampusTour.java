// a campus tour

class UtilsCampus {

}

class Address {
  String street;
  int number;

  Address(String street, int number) {
      this.number = number;
      this.street = street;
  }
  
  boolean equals(Address other) {
    return this.street.equals(other.street) &&
            this.number == other.number;
  }
}

interface ICampusLocation {
    boolean equals(ICampusLocation other);
}

class Building implements ICampusLocation {
    String name;
    Address address;

    Building(String name, Address address) {
        this.name = name;
        this.address = address;
    }

    public boolean equals(ICampusLocation other) {
        if (other instanceof Building) {
            return this.name.equals(((Building)other).name) &&
                this.address.equals(((Building)other).address);
        }
        else {
            return false;
        }
    }
}

// a spot on the tour
interface ITourLocation {
}

abstract class ATourLocation implements ITourLocation {
  String speech; // the speech to give at this spot on the tour

  ATourLocation(String speech) {
      this.speech = speech;
  }

  abstract boolean equals(ATourLocation other);
}

// the end of the tour
class TourEnd extends ATourLocation {
  ICampusLocation location;

  TourEnd(String speech, ICampusLocation location) {
      super(speech);
      this.location = location;
  }
  
  boolean equals(ATourLocation other) {
      if (other instanceof TourEnd) {
          return this.speech.equals(other.speech) &&
                  this.location.equals(((TourEnd)other).location);
      }
      else {
          return false;
    }
  }
}

//a mandatory spot on the tour with the next place to go
class Mandatory extends ATourLocation {
  ICampusLocation location;
  ITourLocation next;

  Mandatory(String speech, ICampusLocation location, ITourLocation next) {
      super(speech);
      this.location = location;
      this.next = next;
  }

  boolean equals(ATourLocation other) {
      if (other instanceof Mandatory) {
          return this.location.equals(((Mandatory) other).location) &&
                  this.next.equals(((Mandatory) other).next) &&
                  this.speech.equals(other.speech);
      }
      else {
          return false;
    }
  }
}

// up to the tour guide where to go next
// TODO fix so that the order of option 1 and option 2 is arbitary
// I think I fixed the todo, need to test it to be sure
class BranchingTour extends ATourLocation {
    ITourLocation option1;
    ITourLocation option2;

    BranchingTour(String speech, ITourLocation option1, ITourLocation option2) {
        super(speech);
        this.option1 = option1;
        this.option2 = option2;
    }

    boolean equals(ATourLocation other) {
        if (other instanceof BranchingTour) {
            if (this.option1.equals(((BranchingTour) other).option1)) {
                return this.option2.equals(((BranchingTour) other).option2);
            }
            else {
                if (this.option1.equals(((BranchingTour) other).option2)) {
                    return this.option2.equals(((BranchingTour) other).option1);
                }
                else {
                    return false;
                }
            }
        }
        else {
            return false;
        }
    }
}

interface ILoCampusLocation {
    boolean equals(ILoCampusLocation other);
}

class MtLoCampusLocation implements ILoCampusLocation {
    public boolean equals(ILoCampusLocation other) {
        return other instanceof MtLoCampusLocation;
    }
}

class ConsLoCampusLocation implements ILoCampusLocation {
    ICampusLocation first;
    ILoCampusLocation rest;

    ConsLoCampusLocation(ICampusLocation first, ILoCampusLocation rest) {
        this.first = first;
        this.rest = rest;
    }

    public boolean equals(ILoCampusLocation other) {
        if (other instanceof ConsLoCampusLocation) {
            if (this.first.equals(((ConsLoCampusLocation)other).first)) {
                if ( (((ConsLoCampusLocation)other).rest instanceof ConsLoCampusLocation) && 
                    (this.rest instanceof ConsLoCampusLocation)) {
                    return this.rest.equals(((ConsLoCampusLocation)other).rest);
                }
                else if ( (((ConsLoCampusLocation)other).rest instanceof MtLoCampusLocation) && 
                        (this.rest instanceof MtLoCampusLocation)) {
                    return true;
                }
                else {
                    return false;
                }
            }
            else {
                return false;
            }
        }
        else {
            return false;
        }       
    }
}

class Quad implements ICampusLocation {
  String name;
  ILoCampusLocation surroundings; // in clockwise order, starting north

  Quad(String name, ILoCampusLocation surroundings) {
      this.name = name;
      this.surroundings = surroundings;
  }

  public boolean equals(ICampusLocation other) {
      if (other instanceof ICampusLocation) {
          // TODO fix this to be a proper equality function for the list "surroundings"
          // Also think I fixed this one as well, need to test though
          return this.name.equals(((Quad)other).name) &&
                  this.surroundings.equals(((Quad)other).surroundings);
      }
      else {
          return false;
    }
  }
}


class CampusTour {
  int startTime; // minutes from midnight
  ITourLocation startingLocation;

  CampusTour(int startTime, ITourLocation startingLocation) {
    this.startTime = startTime;
    this.startingLocation = startingLocation;
  }

  // is this tour the same tour as the given one?
  boolean sameTour(CampusTour other) {
      return this.startTime == other.startTime &&
              this.startingLocation.equals(other.startingLocation);
  }
  
public static void main(String[] args) {
    
    CampusTour testTour = new CampusTour(600, null)
  }
}

class ExamplesCampus {



}