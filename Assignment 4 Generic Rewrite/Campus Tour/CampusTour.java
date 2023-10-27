import tester.Tester;

interface IList<T> {
    // Double dispatch method for equals
    // Computes if this is equal to list
    boolean equals(IList<T> list);
    boolean equalsCons(ConsList<T> list);
    boolean equalsMt(MtList<T> list);
}
    
class MtList<T> implements IList<T> {
    /* Template
     *
     * Fields:
     * 
     * Methods:
     * this.equals(IList<T> list) -- boolean
     * this.equalsMt(MtList<T> list) -- boolean
     * this.equalsCons(ConsList<T> list) -- boolean
     * 
     * Methods on fields:
     */

    // Double dispatch methods for equals()
    // Computes if this and list are equal
    public boolean equals(IList<T> list) {
        return list.equalsMt(this);
    }
    // Double dispatch methods for equals()
    // Computes if this and an MtList<T> are equal
    public boolean equalsMt(MtList<T> list) {
        return true;
    }
    // Double dispatch methods for equals()
    // Computes if this and a ConsList<T> are equal
    public boolean equalsCons(ConsList<T> list) {
        return false;
    }
}

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
     * 
     * Methods on fields:
     * this.rest.equals(IList<T> list) -- boolean
     * this.rest.equalsMt(MtList<T> list) -- boolean
     * this.rest.equalsCons(ConsList<T> list) -- boolean
     */

    T first;
    IList<T> rest;

    // Constructor for a ConsList<T>
    ConsList(T first, IList<T> rest) {
        this.first = first;
        this.rest = rest;
    }

    // Double dispatch methods for equals()
    // Computes if this and list are equal
    public boolean equals(IList<T> list) {
        return list.equalsCons(this);
    }

    // Double dispatch methods for equals()
    // Computes if this and an MtList<T> are equal
    public boolean equalsMt(MtList<T> list) {
        return false;
    }

    // Double dispatch methods for equals()
    // Computes if this and a ConsList<T> are equal
    public boolean equalsCons(ConsList<T> list) {
        if (this.first.equals(list.first)) {
            return this.rest.equals(list.rest);
        } else {
            return false;
        }
    }
}

class Address {
    /* Template
     *
     * Fields:
     * this.street -- String
     * this.number -- int
     * 
     * Methods:
     * this.equals(Address other) -- boolean
     * 
     * Methods on fields:
     */

    String street;
    int number;

    // Constructor for an Address
    Address(String street, int number) {
        this.number = number;
        this.street = street;
    }
    
    // Checks equality of two addresses
    boolean equals(Address other) {
        return this.street.equals(other.street) &&
                this.number == other.number;
    }
}

interface ICampusLocation {
    // Double dispatch methods for equals()
    // Computes if this and other are equal
    boolean equals(ICampusLocation other);

    // Double dispatch methods for equals()
    // Computes if this and a Building other are equal
    boolean equalsBuilding(Building other);

    // Double dispatch methods for equals()
    // Computes if this and a Quad other are equal
    boolean equalsQuad(Quad other);
}

class Building implements ICampusLocation {
    /* Template
     *
     * Fields:
     * this.name -- String
     * this.address -- Address
     * 
     * Methods:
     * this.equals(ICampusLocation other) -- boolean
     * this.equalsBuilding(Building other) -- boolean
     * this.equalsQuad(Quad other) -- boolean
     * 
     * Methods on fields:
     * this.address.equals(Address other) -- boolean
     */

    String name;
    Address address;

    // Constructor for Building
    Building(String name, Address address) {
        this.name = name;
        this.address = address;
    }

    // Double dispatch methods for equals()
    // Computes if this and other are equal
    public boolean equals(ICampusLocation other) {
        return other.equalsBuilding(this);
    }

    // Double dispatch methods for equals()
    // Computes if this and other are equal
    public boolean equalsBuilding(Building other) {
        return this.name.equals(other.name) &&
                this.address.equals(other.address);
    }

    // Double dispatch methods for equals()
    // Computes if this and a Quad are equal
    public boolean equalsQuad(Quad other) {
        return false;
    }
}

// a spot on the tour
interface ITourLocation {
    // Double dispatch methods for equals()
    // Computes if this and other are equal
    boolean equals(ITourLocation other);

    // Double dispatch methods for equals()
    // Computes if this and other are equal
    boolean equalsITourLocation(ITourLocation other);

    // Double dispatch methods for equals()
    // Computes if this and and a instance of ATourLocation are equal
    boolean equalsATourLocaton(ATourLocation other);
}

abstract class ATourLocation implements ITourLocation {
    /* Template
     *
     * Fields:
     * this.speech -- String
     * 
     * Methods:
     * this.equals(ATourLocation other) -- boolean
     * this.equalsTourEnd(TourEnd other) -- boolean
     * this.equalsMandatory(Mandatory other) -- boolean
     * this.equalsBranchingTour(BranchingTour other) -- boolean
     * this.equalsITourLocation(ITourLocation other) -- boolean
     * this.equalsATourLocation(ATourLocation other) -- boolean
     * this.equals(ITourLocation other) -- boolean
     * 
     * Methods on fields:
     */

    String speech; // the speech to give at this spot on the tour

    // Constructor for ATourLocation
    ATourLocation(String speech) {
        this.speech = speech;
    }

    // Double dispatch methods for equals()
    // Computes if this and other are equal
    abstract public boolean equals(ATourLocation other);

    // Double dispatch methods for equals()
    // Computes if this and a TourEnd are equal
    abstract public boolean equalsTourEnd(TourEnd other);

    // Double dispatch methods for equals()
    // Computes if this and a Mandatory are equal
    abstract public boolean equalsMandatory(Mandatory other);

    // Double dispatch methods for equals()
    // Computes if this and a BranchingTour are equal
    abstract public boolean equalsBranchingTour(BranchingTour other);
    
    // Double dispatch methods for equals()
    // Computes if this and an ITourLocation are equal
    public boolean equalsITourLocation(ITourLocation other) {
        return false;
    }

    // Double dispatch methods for equals()
    // Computes if this and another ATourLocation are equal
    public boolean equalsATourLocaton(ATourLocation other) {
        return this.equals(other);
    }

    // Double dispatch methods for equals()
    // Computes if this and other are equal
    public boolean equals(ITourLocation other) {
        return other.equalsATourLocaton(this);
    }
}

// the end of the tour
class TourEnd extends ATourLocation {
    /* Template
     *
     * Fields:
     * this.speech -- String
     * this.location -- ICampusLocation
     * 
     * Methods:
     * this.equals(ATourLocation other) -- boolean
     * this.equalsTourEnd(TourEnd other) -- boolean
     * this.equalsMandatory(Mandatory other) -- boolean
     * this.equalsBranchingTour(BranchingTour other) -- boolean
     * this.equalsITourLocation(ITourLocation other) -- boolean
     * this.equalsATourLocation(ATourLocation other) -- boolean
     * this.equals(ITourLocation other) -- boolean
     * 
     * Methods on fields:
     * this.location.equals(ICampusLocation other) -- boolean
     * this.location.equalsBuilding(Building other) -- boolean
     * this.location.equalsQuad(Quad other) -- boolean
     */
    ICampusLocation location;

    // Constructor for TourEnd
    TourEnd(String speech, ICampusLocation location) {
        super(speech);
        this.location = location;
    }

    // Double dispatch methods for equals()
    // Computes if this and another ATourLocation are equal
    public boolean equals(ATourLocation other) {
        return other.equalsTourEnd(this);
    }
    
    // Double dispatch methods for equals()
    // Computes if this and other are equal
    public boolean equalsTourEnd(TourEnd other) {
        return this.speech.equals(other.speech) &&
                this.location.equals(other.location);
    }
    
    // Double dispatch methods for equals()
    // Computes if this and a Mandatory are equal
    public boolean equalsMandatory(Mandatory other) {
        return false;
    }

    // Double dispatch methods for equals()
    // Computes if this and a BranchingTour are equal
    public boolean equalsBranchingTour(BranchingTour other) {
        return false;
    }
}

//a mandatory spot on the tour with the next place to go
class Mandatory extends ATourLocation {
    /* Template
     *
     * Fields:
     * this.speech -- String
     * this.location -- ICampusLocation
     * this.next -- ITourLocation
     * 
     * Methods:
     * this.equals(ATourLocation other) -- boolean
     * this.equalsTourEnd(TourEnd other) -- boolean
     * this.equalsMandatory(Mandatory other) -- boolean
     * this.equalsBranchingTour(BranchingTour other) -- boolean
     * this.equalsITourLocation(ITourLocation other) -- boolean
     * this.equalsATourLocation(ATourLocation other) -- boolean
     * this.equals(ITourLocation other) -- boolean
     * 
     * Methods on fields:
     * this.next.equalsITourLocation(ITourLocation other) -- boolean
     * this.next.equalsATourLocation(ATourLocation other) -- boolean
     * this.next.equals(ITourLocation other) -- boolean
     * this.location.equals(ICampusLocation other) -- boolean
     * this.location.equalsBuilding(Building other) -- boolean
     * this.location.equalsQuad(Quad other) -- boolean
     */
    ICampusLocation location;
    ITourLocation next;

    // Constructor for Mandatory
    Mandatory(String speech, ICampusLocation location, ITourLocation next) {
        super(speech);
        this.location = location;
        this.next = next;
    }

    // Double dispatch methods for equals()
    // Computes if this and another ATourLocation are equal
    public boolean equals(ATourLocation other) {
        return other.equalsMandatory(this);
    }

    // Double dispatch methods for equals()
    // Computes if this and other are equal
    public boolean equalsMandatory(Mandatory other) {
        return this.location.equals(other.location) &&
                this.next.equals(other.next) &&
                this.speech.equals(other.speech);
    }
    
    // Double dispatch methods for equals()
    // Computes if this and a TourEnd are equal
    public boolean equalsTourEnd(TourEnd other) {
        return false;
    }

    // Double dispatch methods for equals()
    // Computes if this and a BranchingTour are equal
    public boolean equalsBranchingTour(BranchingTour other) {
        return false;
    }
}

class BranchingTour extends ATourLocation {
    /* Template
     *
     * Fields:
     * this.speech -- String
     * this.option1 -- ITourLocation
     * this.option2 -- ITourLocation
     * 
     * Methods:
     * this.equals(ATourLocation other) -- boolean
     * this.equalsTourEnd(TourEnd other) -- boolean
     * this.equalsMandatory(Mandatory other) -- boolean
     * this.equalsBranchingTour(BranchingTour other) -- boolean
     * this.equalsITourLocation(ITourLocation other) -- boolean
     * this.equalsATourLocation(ATourLocation other) -- boolean
     * this.equals(ITourLocation other) -- boolean
     * 
     * Methods on fields:
     * this.option1.equalsITourLocation(ITourLocation other) -- boolean
     * this.option1.equalsATourLocation(ATourLocation other) -- boolean
     * this.option1.equals(ITourLocation other) -- boolean
     * this.option2.equalsITourLocation(ITourLocation other) -- boolean
     * this.option2.equalsATourLocation(ATourLocation other) -- boolean
     * this.option2.equals(ITourLocation other) -- boolean
     */

    ITourLocation option1;
    ITourLocation option2;

    // Constructor for BranchingTour
    BranchingTour(String speech, ITourLocation option1, ITourLocation option2) {
        super(speech);
        this.option1 = option1;
        this.option2 = option2;
    }

    // Double dispatch methods for equals()
    // Computes if this and another ATourLocation are equal
    public boolean equals(ATourLocation other) {
        return other.equalsBranchingTour(this);
    }

    // Double dispatch methods for equals()
    // Computes if this and a Mandatory are equal
    public boolean equalsMandatory(Mandatory other) {
        return false;
    }

    // Double dispatch methods for equals()
    // Computes if this and a TourEnd are equal
    public boolean equalsTourEnd(TourEnd other) {
        return false;
    }

    // Double dispatch methods for equals()
    // Computes if this and other are equal
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
    /* Template
     *
     * Fields:
     * this.name -- String
     * this.surroundings -- IList<ICampusLocation>
     * 
     * Methods:
     * this.equals(ICampusLocation other) -- boolean
     * this.equalsBuilding(Building other) -- boolean
     * this.equalsQuad(Quad other) -- boolean
     * 
     * Methods on fields:
     * this.surroundings.equals(IList<T> list) -- boolean
     * this.surroundings.equalsMt(MtList<T> list) -- boolean
     * this.surroundings.equalsCons(ConsList<T> list) -- boolean
     */

    String name;
    IList<ICampusLocation> surroundings; // in clockwise order, starting north

    // Constructor for Quad
    Quad(String name, IList<ICampusLocation> surroundings) {
        this.name = name;
        this.surroundings = surroundings;
    }

    // Double dispatch methods for equals()
    // Computes if this and another ICampusLocation are equal
    public boolean equals(ICampusLocation other) {
        return other.equalsQuad(this);
    }

    // Double dispatch methods for equals()
    // Computes if this and other are equal
    public boolean equalsQuad(Quad other) {
        return this.name.equals(other.name) &&
                this.surroundings.equals(other.surroundings);
    }
    
    // Double dispatch methods for equals()
    // Computes if this and a Building are equal
    public boolean equalsBuilding(Building other) {
        return false;
    }
}

class CampusTour {
    /* Template
     *
     * Fields:
     * this.startTime -- int
     * this.startingLocation -- ITourLocation
     * 
     * Methods:
     * this.sameTour(CampusTour other) -- boolean
     * 
     * Methods on fields:
     * this.startingLocation.equalsITourLocation(ITourLocation other) -- boolean
     * this.startingLocation.equalsATourLocation(ATourLocation other) -- boolean
     * this.startingLocation.equals(ITourLocation other) -- boolean
     */
    int startTime; // minutes from midnight
    ITourLocation startingLocation;

    // Constructor for Campus Tour
    CampusTour(int startTime, ITourLocation startingLocation) {
        this.startTime = startTime;
        this.startingLocation = startingLocation;
    }

    // Computes if this CampusTour is the same as other
    boolean sameTour(CampusTour other) {
        return this.startTime == other.startTime &&
                this.startingLocation.equals(other.startingLocation);
    }
}

class ExamplesCampus {
    //CampusTour tour3 = new CampusTour(8, mandatory1);
    //CampusTour tour4 = new CampusTour(8, new Mandatory("This is campus gym", new Building("This is the Richie Regan Recreation Center", new Address("South Orange Ave", 400)), mandatory1));

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

    ConsList<ICampusLocation> locationList1 = new ConsList<ICampusLocation>(dunkin,
            new ConsList<ICampusLocation>(rec, new MtList<ICampusLocation>()));
    ConsList<ICampusLocation> locationList2 = new ConsList<ICampusLocation>(jubilee, new ConsList<ICampusLocation>(
            boland, new ConsList<ICampusLocation>(studentCenter, new MtList<ICampusLocation>())));
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
    BranchingTour branch1 = new BranchingTour("We can end our tour at McNulty Hall or the Student Center", endTour1, endTour2);
    //BranchingTour branch2 = new BranchingTour("We can stop by the rec center or duffy hall", mandatory1, mandatory2);

    //Examples Campus Tour
    CampusTour tour1 = new CampusTour(600, mandatoryStart1);
    CampusTour tour2 = new CampusTour(600, mandatoryStart2);

    boolean testMtListEquals(Tester t) {
        return t.checkExpect((new MtList<ICampusLocation>()).equals(new MtList<ICampusLocation>()), true) &&
                t.checkExpect((new MtList<ICampusLocation>()).equals(locationList1), false);
    };

    boolean testMtListEqualsMt(Tester t) {
        return t.checkExpect((new MtList<ICampusLocation>()).equalsMt(new MtList<ICampusLocation>()), true);
    };

    boolean testMtListEqualsCons(Tester t) {
        return t.checkExpect((new MtList<ICampusLocation>()).equalsCons(locationList1), false);
    };

    boolean testConsListEquals(Tester t) {
        return t.checkExpect(locationList1.equals(new ConsList<ICampusLocation>(dunkin,
            new ConsList<ICampusLocation>(rec, new MtList<ICampusLocation>()))), true) &&
            t.checkExpect(locationList1.equals(locationList2), false) &&
                t.checkExpect(locationList1.equals(new MtList<ICampusLocation>()), false);
    };
    
    boolean testConsListEqualsMt(Tester t) {
        return t.checkExpect(locationList1.equalsMt(new MtList<ICampusLocation>()), false);
    };

    boolean testConsListEqualsCons(Tester t) {
        return t.checkExpect(locationList1.equalsCons(new ConsList<ICampusLocation>(dunkin,
            new ConsList<ICampusLocation>(rec, new MtList<ICampusLocation>()))), true) &&
            t.checkExpect(locationList1.equalsCons(locationList2), false);
    };
    
    boolean testAddressEquals(Tester t) {
        return t.checkExpect(jubileeHallAddress.equals(duffyHallAddress), false) &&
            t.checkExpect(jubileeHallAddress.equals(new Address("South Orange Ave", 395)), true);
    };

    boolean testBuildEquals(Tester t) {
        return t.checkExpect(jubilee.equals(mcNulty), false) &&
                t.checkExpect(mcNulty.equals(new Building("McNulty Hall", mcnultyHallAddress)), true) &&
                t.checkExpect(duffy.equals(quad2), false);
    };

    boolean testBuildEqualsBuilding(Tester t) {
        return t.checkExpect(jubilee.equalsBuilding(mcNulty), false) &&
                t.checkExpect(mcNulty.equalsBuilding(new Building("McNulty Hall", mcnultyHallAddress)), true);
    };

    boolean testBuildEqualsQuad(Tester t) {
        return t.checkExpect(duffy.equalsQuad(quad2), false);
    };

    boolean testQuadEquals(Tester t) {
        return t.checkExpect(quad2.equals(new Quad("Buildings around McNulty Hall", new ConsList<ICampusLocation>(jubilee, new ConsList<ICampusLocation>(boland, new ConsList<ICampusLocation>(studentCenter, new MtList<ICampusLocation>()))))), true) &&
            t.checkExpect(quad2.equals(quad1Order1), false) &&
                t.checkExpect(quad2.equals(boland), false);
    };

    boolean testQuadEqualsBuilding(Tester t) {
        return t.checkExpect(quad2.equalsBuilding(boland), false);
    };

    boolean testQuadEqualsQuad(Tester t) {
        return t.checkExpect(quad2.equalsQuad(new Quad("Buildings around McNulty Hall", new ConsList<ICampusLocation>(jubilee, new ConsList<ICampusLocation>(boland, new ConsList<ICampusLocation>(studentCenter, new MtList<ICampusLocation>()))))), true) &&
                t.checkExpect(quad2.equalsQuad(quad1Order1), false);
    };

    boolean testTourEndEquals(Tester t) {
        return t.checkExpect(endTour1.equals(new TourEnd("McNulty Hall is the last spot on the tour", quad2)), true) &&
                t.checkExpect(endTour1.equals(endTour2), false) &&
                t.checkExpect(endTour1.equals(mandatory1Order1), false) &&
                t.checkExpect(endTour1.equals(branch1), false);
    };

    boolean testTourEndEqualsTourEnd(Tester t) {
        return t.checkExpect(endTour1.equalsTourEnd(new TourEnd("McNulty Hall is the last spot on the tour", quad2)), true) &&
                t.checkExpect(endTour1.equalsTourEnd(endTour2), false);
    };

    boolean testTourEndEqualsMandatory(Tester t) {
        return t.checkExpect(endTour1.equalsMandatory(mandatory1Order1), false);
    };

    boolean testTourEndEqualsBranchingTour(Tester t) {
        return t.checkExpect(endTour1.equalsBranchingTour(branch1), false);
    };

    boolean testMandatoryEquals(Tester t) {
        return t.checkExpect(mandatoryStart1.equals(new Mandatory("This is classes are held", jubilee, branching1Order1)), true) &&
                t.checkExpect(mandatoryStart1.equals(mandatory1Order2), false) &&
                t.checkExpect(mandatoryStart1.equals(endTour2), false) &&
                t.checkExpect(mandatoryStart1.equals(branch1), false);
    };

    boolean testMandatoryEqualsTourEnd(Tester t) {
        return t.checkExpect(mandatoryStart1.equalsTourEnd(endTour2), false);
    };

    boolean testMandatoryEqualsMandatory(Tester t) {
        return t.checkExpect(mandatoryStart1.equalsMandatory(new Mandatory("This is classes are held", jubilee, branching1Order1)), true) &&
                t.checkExpect(mandatoryStart1.equalsMandatory(mandatory1Order2), false);
    };

    boolean testMandatoryEqualsBranchingTour(Tester t) {
        return t.checkExpect(mandatoryStart1.equalsBranchingTour(branch1), false);
    };

    boolean testBranchingTourEquals(Tester t) {
        return t.checkExpect(branching1Order1.equals(new BranchingTour("default speech", mandatory1Order1, mandatory1Order2)), true) &&
                t.checkExpect(branching1Order1.equals(branching1Order2), true) &&
                t.checkExpect(branching1Order1.equals(branch1), false) &&
                t.checkExpect(branching1Order2.equals(mandatoryStart1), false) &&
                t.checkExpect(branching1Order2.equals(endTour1), false);
    };

    boolean testBranchingTourEqualsTourEnd(Tester t) {
        return t.checkExpect(branching1Order2.equalsTourEnd(endTour1), false);
    };

    boolean testBranchingTourEqualsMandatory(Tester t) {
        return t.checkExpect(branching1Order2.equalsMandatory(mandatoryStart1), false);
    };

    boolean testBranchingTourEqualsBranchingTour(Tester t) {
        return t.checkExpect(branching1Order1.equalsBranchingTour(new BranchingTour("default speech", mandatory1Order1, mandatory1Order2)), true) &&
                t.checkExpect(branching1Order1.equalsBranchingTour(branching1Order2), true) &&
                t.checkExpect(branching1Order1.equalsBranchingTour(branch1), false);
    };
    
    boolean testSameTour(Tester t){
        return t.checkExpect(tour1.sameTour(tour2), true) &&
                t.checkExpect(tour1.sameTour(tour1), true) &&
                t.checkExpect(tour2.sameTour(tour2), true);
    }
    boolean testEquals(Tester t){
        return t.checkExpect(mandatory1Order1.equals(mandatory1Order2), false) &&
                t.checkExpect(mandatoryStart1.equals(mandatoryStart2),true) &&

                t.checkExpect(quad1Order1.equals(quad1Order2), false) &&
                t.checkExpect(quad2.equals(quad2), true) &&

                t.checkExpect(branching1Order1.equals(branching1Order2), true) &&
                t.checkExpect(branch1.equals(branching1Order2), false) &&

                t.checkExpect(mcNulty.equals(duffy), false) &&
                t.checkExpect(rec.equals(rec), true) &&

                t.checkExpect(duffyHallAddress.equals(jubileeHallAddress), false) &&
                t.checkExpect(jubileeHallAddress.equals(new Address("Chapel Hill Road", 196)), true) &&

                t.checkExpect(endTour1.equals(endTour2), false) &&
                t.checkExpect(endTour1.equals(new TourEnd("McNulty Hall is the last spot on the tour", quad2)), true);
    }
}
