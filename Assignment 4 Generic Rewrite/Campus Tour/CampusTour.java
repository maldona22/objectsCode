// a campus tour
interface IPred<T> {
    boolean apply(T t);
}

interface IList<T> {
    IList<T> filter(IPred<T> pred);

    IList<T> append(IList<T> list);

    IList<T> append(T element);

    boolean equals(IList<T> list);

    boolean equalsCons(ConsList<T> list);

    boolean equalsMt(MtList<T> list);

    int length();
}
    
class MtList<T> implements IList<T> {
    public IList<T> filter(IPred<T> pred) {
        return this;
    }

    public IList<T> append(IList<T> list) {
        return list;
    }

    public IList<T> append(T element) {
        return new ConsList<T>(element, new MtList<T>());
    }

    public int length() {
        return 0;
    }

    public boolean equals(IList<T> list) {
        return list.equalsMt(this);
    }

    public boolean equalsMt(MtList<T> list) {
        return true;
    }

    public boolean equalsCons(ConsList<T> list) {
        return false;
    }
}

class ConsList<T> implements IList<T> {
    T first;
    IList<T> rest;

    ConsList(T first, IList<T> rest) {
        this.first = first;
        this.rest = rest;
    }

    public boolean equals(IList<T> list) {
        return list.equalsCons(this);
    }

    public boolean equalsMt(MtList<T> list) {
        return false;
    }

    public boolean equalsCons(ConsList<T> list) {
        if (this.first.equals(list.first)) {
            return this.rest.equals(list.rest);
        }
        else {
            return false;
        }
    }
    
    public IList<T> filter(IPred<T> pred) {
        if (pred.apply(this.first)) {
            return new ConsList<T>(this.first, this.rest.filter(pred));
        } else {
            return this.rest.filter(pred);
        }
    }
    
    public IList<T> append(IList<T> list) {
        return new ConsList<T>(first, rest.append(list));
    }

    public IList<T> append(T element) {
        return new ConsList<T>(first, new ConsList<T>(element, new MtList<T>()));
    }

    public int length() {
        return 1 + this.rest.length();
    }
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

    boolean equalsBuilding(Building other);

    boolean equalsQuad(Quad other);
}

class Building implements ICampusLocation {
    String name;
    Address address;

    Building(String name, Address address) {
        this.name = name;
        this.address = address;
    }

    public boolean equals(ICampusLocation other) {
        return other.equalsBuilding(this);
    }

    public boolean equalsBuilding(Building other) {
        return this.name.equals(other.name) &&
                this.address.equals(other.address);
    }

    public boolean equalsQuad(Quad other) {
        return false;
    }
}

// a spot on the tour
interface ITourLocation {
    boolean equals(ITourLocation other);

    boolean equalsITourLocation(ITourLocation other);

    boolean equalsATourLocaton(ATourLocation other);
}

abstract class ATourLocation implements ITourLocation {
    String speech; // the speech to give at this spot on the tour

    ATourLocation(String speech) {
        this.speech = speech;
    }

    abstract public boolean equals(ATourLocation other);

    abstract public boolean equalsTourEnd(TourEnd other);

    abstract public boolean equalsMandatory(Mandatory other);

    abstract public boolean equalsBranchingTour(BranchingTour other);
    
    public boolean equalsITourLocation(ITourLocation other) {
        return false;
    }

    public boolean equalsATourLocaton(ATourLocation other) {
        return this.equals(other);
    }
    public boolean equals(ITourLocation other) {
        return other.equalsATourLocaton(this);
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
      return other.equalsTourEnd(this);
  }
  
  public boolean equalsTourEnd(TourEnd other) {
      return this.speech.equals(other.speech) &&
              this.location.equals(other.location);
  }
  
  public boolean equalsMandatory(Mandatory other) {
      return false;
  }

  public boolean equalsBranchingTour(BranchingTour other) {
      return false;
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
      return other.equalsMandatory(this);
  }

  public boolean equalsMandatory(Mandatory other) {
      return this.location.equals(other.location) &&
              this.next.equals(other.next) &&
              this.speech.equals(other.speech);
  }
  
  public boolean equalsTourEnd(TourEnd other) {
      return false;
  }

  public boolean equalsBranchingTour(BranchingTour other) {
      return false;
  }
}

class BranchingTour extends ATourLocation {
    ITourLocation option1;
    ITourLocation option2;

    BranchingTour(String speech, ITourLocation option1, ITourLocation option2) {
        super(speech);
        this.option1 = option1;
        this.option2 = option2;
    }

    public boolean equals(ATourLocation other) {
        return other.equalsBranchingTour(this);
    }

    public boolean equalsMandatory(Mandatory other) {
        return false;
    }

    public boolean equalsTourEnd(TourEnd other) {
        return false;
    }

    public boolean equalsBranchingTour(BranchingTour other) {
        if (this.option1.equals(other.option1)) {
            return this.option2.equals(other.option2);
        } else {
            if (this.option1.equals(other.option2)) {
                return this.option2.equals(other.option1);
            }
        }
        return false;
    }
}

class Quad implements ICampusLocation {
  String name;
  IList<ICampusLocation> surroundings; // in clockwise order, starting north

  Quad(String name, IList<ICampusLocation> surroundings) {
      this.name = name;
      this.surroundings = surroundings;
  }

  public boolean equals(ICampusLocation other) {
      return other.equalsQuad(this);
  }

  public boolean equalsQuad(Quad other) {
      return this.name.equals(other.name) &&
              this.surroundings.equals(other.surroundings);
  }
  
  public boolean equalsBuilding(Building other) {
      return false;
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
                new ConsList<ICampusLocation>(dunkin, new ConsList<ICampusLocation>(rec, new MtList<ICampusLocation>())));
        Quad quad1Order2 = new Quad("Buildings around Duffy Hall",
                new ConsList<ICampusLocation>(rec, new ConsList<ICampusLocation>(dunkin, new MtList<ICampusLocation>())));
        Quad quad2 = new Quad("Buildings around McNulty Hall", new ConsList<ICampusLocation>(jubilee, new ConsList<ICampusLocation>(boland, new ConsList<ICampusLocation>(studentCenter, new MtList<ICampusLocation>()))));
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