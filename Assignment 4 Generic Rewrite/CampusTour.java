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
    boolean equals(ITourLocation other);
}

abstract class ATourLocation implements ITourLocation {
    String speech; // the speech to give at this spot on the tour

    ATourLocation(String speech) {
        this.speech = speech;
    }

    abstract public boolean equals(ATourLocation other);
    
    public boolean equals(ITourLocation other) {
        if (other instanceof ATourLocation) {
            return this.equals((ATourLocation)other);
        } else {
            return false;
        }
    }
}

// the end of the tour
class TourEnd extends ATourLocation {
  ICampusLocation location;

  TourEnd(String speech, ICampusLocation location) {
      super(speech);
      this.location = location;
  }
  
  public boolean equals(ATourLocation other) {
      if (other instanceof TourEnd) {
          return this.speech.equals(other.speech) &&
                  this.location.equals(((TourEnd) other).location);
      } else {
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

  public boolean equals(ATourLocation other) {
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

    public boolean equals(ATourLocation other) {
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
  

}

class ExamplesCampus {
    //CampusTour tour3 = new CampusTour(8, mandatory1);
    //CampusTour tour4 = new CampusTour(8, new Mandatory("This is campus gym", new Building("This is the Richie Regan Recreation Center", new Address("South Orange Ave", 400)), mandatory1));
    public static void main(String[] args) {
        // TODO: add tests
        //Examples Address
        MtLoCampusLocation empty = new MtLoCampusLocation();
        Address setonHall = new Address("South Orange Ave", 400);

        Address jubileeHallAddress = new Address("South Orange Ave", 395);
        Building jubilee = new Building("Jubliee Hall", jubileeHallAddress);

        Address mcnultyHallAddress = new Address("South Orange Ave", 380);
        Building mcNulty = new Building("McNulty Hall", mcnultyHallAddress);

        Address studentCenterAddress = new Address("Chapel Hill Road", 233);
        Building studentCenter = new Building("Student Center", studentCenterAddress);

        Address duffyHallAddress = new Address("Chapel Hill Road", 196);
        Building duffy = new Building("Duffy Hall", duffyHallAddress);

        Building rec = new Building("Richie Regan Recreation Center", setonHall);
        Building dunkin = new Building("Dunkin Donuts", setonHall);
        Building boland = new Building("Boland Hall", setonHall);
        
        
        //Examples Quad
        Quad quad1Order1 = new Quad("Buildings around Duffy Hall",
                new ConsLoCampusLocation(dunkin, new ConsLoCampusLocation(rec, new MtLoCampusLocation())));
        Quad quad1Order2 = new Quad("Buildings around Duffy Hall",
                new ConsLoCampusLocation(rec, new ConsLoCampusLocation(dunkin, new MtLoCampusLocation())));
        Quad quad2 = new Quad("Buildings around McNulty Hall", new ConsLoCampusLocation(jubilee, new ConsLoCampusLocation(boland, new ConsLoCampusLocation(studentCenter, new MtLoCampusLocation()))));
        //Examples ToursEnd
        TourEnd endTour1 = new TourEnd("McNulty Hall is the last spot on the tour", quad2);
        TourEnd endTour2 = new TourEnd("The Student Center is the last spot on our tour", studentCenter);
        //Examples Mandatory
        Mandatory mandatory1Order1 = new Mandatory("This is where we offer many student services", quad1Order1, endTour1);
        Mandatory mandatory1Order2 = new Mandatory("This is where we offer many student services", quad1Order2,
                endTour1);

        BranchingTour branching1Order1 = new BranchingTour("default speech", mandatory1Order1, mandatory1Order2);
        BranchingTour branching1Order2 = new BranchingTour("default speech", mandatory1Order2, mandatory1Order1);


        Mandatory mandatoryStart1 = new Mandatory("This is classes are held", jubilee, branching1Order1);
        Mandatory mandatoryStart2 = new Mandatory("This is classes are held", jubilee, branching1Order1);

        //Mandatory mandatory2 = new Mandatory("This is the campus gym", rec, mandatory1);
        //Examples Branching Tour
        BranchingTour branch1= new BranchingTour("We can end our tour at McNulty Hall or the Student Center", endTour1, endTour2);
        //BranchingTour branch2 = new BranchingTour("We can stop by the rec center or duffy hall", mandatory1, mandatory2);
        //Examples Campus Tour
        CampusTour tour1 = new CampusTour(600, mandatoryStart1);
        CampusTour tour2 = new CampusTour(600, mandatoryStart2);

        System.out.println(tour1.sameTour(tour2));
        
    }
}