import tester.*;
/* A Course has a name and preReqs, which is an IList<Course>
(see Lecture 15 for the data definition of parametrized lists).

Design the Course class and any other necessary classes and interfaces.
*/
interface IFunc<A, R> {
    R apply(A arg);
}

interface IListVisitor<T, R> extends IFunc<IList<T>, R> {
    R apply(MtList<T> mtList);
    R apply(ConsList<T> consList);
}

interface IList<T> extends IFunc<IListVisitor<T, Integer>, Integer> {
    /* 
    int getDeepestPathLength();
    boolean hasPreReqs(String preReqName);
    */
    boolean ormap(IPred<T> pred);
    int accept(IListVisitor<T, Integer> visitor);
    boolean accept(Ormap<T> t);
    }
    
    class MtList<T> implements IList<T> {
    
    public int getDeepestPathLength() {
        return 0;
    }
    
    public boolean hasPreReqs(String preReqName) {
        return false;
    }
    
    public boolean ormap(IPred<T> pred) {
        return false;
    }
    
    public int accept(IListVisitor<T, Integer> visitor) {
        return visitor.apply(this);
    }
    public boolean accept(Ormap<T> t) {
        return false;
    }
    public Integer apply(IListVisitor<T, Integer> arg) {
        return arg.apply(this);
    }
    }
    
    class ConsList<T> implements IList<T> {
        T first;
        IList<T> rest;

        ConsList(T first, IList<T> rest) {
        this.first = first;
        this.rest = rest;
    }

    /*
    public int getDeepestPathLength() {
        return Math.max(first.getDeepestPathLength(), rest.getDeepestPathLength());
    }
    
    public boolean hasPreReqs(String preReqName) {
        return first.hasPreReq(preReqName) || rest.hasPreReq(preReqName);
    }
    */
    
    public boolean ormap(IPred<T> pred) {
        return pred.apply(first) || rest.ormap(pred);
    }
    
    
    public int accept(IListVisitor<T, Integer> visitor) {
        return visitor.apply(this);
    }
    public boolean accept(Ormap<T> t) {
        return t.apply(this);
    }
    
    public Integer apply(IListVisitor<T, Integer> arg) {
        return arg.apply(this);
    }
    }
    
class Course {
    String name;
    IList<Course> preReqs;
    
    public Course(String name, IList<Course> preReqs) {
        this.name = name;
        this.preReqs = preReqs;
    }
    public boolean hasPrereq(String preReqName) {
        return new HasPreReq(preReqName).apply(this.preReqs);
    }
    public int getDeepestPathLength() {
        return new DeepestPathLength().apply(this.preReqs);
    }
    
    public boolean ormap(IPred<Course> pred) {
        return new Ormap<Course>(pred).apply(this.preReqs);
    }
    
    public Boolean accept(HasPreReq hasPreReq) {
        return hasPreReq.apply(this);
    }
}
    
class DeepestPathLength implements IListVisitor<Course, Integer> {
    public Integer apply(MtList<Course> mtList) {
        return 0;
    }
    public Integer apply(ConsList<Course> consList) {
        return Math.max(consList.first.getDeepestPathLength(), consList.rest.accept(this));
    }
    
    public Integer apply(IList<Course> arg) {
        return this.apply(arg);
    }
}
    
interface IPred<T> extends IFunc<T, Boolean> { }
    
class HasPreReq implements IPred<Course> {
    String preReqName;
    
    public HasPreReq(String preReqName) {
        this.preReqName = preReqName;
    }
    
    public Boolean apply(MtList<Course> mtList) {
        return false;
    }
    
    public Boolean apply(ConsList<Course> consList) {
        return consList.first.hasPrereq(preReqName) || consList.rest.accept(this);
    }
    
    public Boolean apply(Course courses) {
        return courses.accept(this);
    }
    
    public Boolean apply(IList<Course> arg) {
        return arg.accept(this);
    }
}
    
class Ormap<T> implements IListVisitor<T, Boolean> {
    IPred<T> pred;
    
    public Ormap(IPred<T> pred) {
        this.pred = pred;
    }
    
    public Boolean apply(MtList<T> mtList) {
        return false;
    }
    public Boolean apply(ConsList<T> consList) {
        return pred.apply(consList.first) || consList.rest.accept(this);
    }
    
    public Boolean apply(IList<T> arg) {
        return arg.accept(this);
    }
}
    
    
    class ExamplesCourse {
    //ExampleCourse
    Course CSAS1114 = new Course("Intro to Programming I", new MtList<Course>());
    Course CSAS1115 = new Course("Intro to Programming II", new ConsList<Course>(CSAS1114, new MtList<Course>()));
    Course CSAS2123 = new Course("Intro to Objects I", new ConsList<Course>(CSAS1114, new ConsList<Course>(CSAS1115, new MtList<Course>())));
    Course CSAS2125 = new Course("Computer Systems Assmebly Programming", new ConsList<Course>(CSAS2123, new ConsList<Course>(CSAS1114, new MtList<Course>())));
    Course CSAS2126 = new Course("Data Structure and Algorithm Analysis", new ConsList<Course>(CSAS2125, new ConsList<Course>(CSAS2123, new ConsList<Course>(CSAS1115, new ConsList<Course>(CSAS1114, new MtList<Course>())))));
    Course PHIL1204 = new Course("Symbolic Logic", new ConsList<Course>(CSAS2126, new MtList<Course>()));
    
    boolean testHasPreReq(Tester t) {
        return t.checkExpect(new HasPreReq(CSAS1114.name).apply(CSAS2123), true) &&
                t.checkExpect(new HasPreReq(CSAS1114.name).apply(CSAS1115), true) &&
                t.checkExpect(new HasPreReq(CSAS2125.name).apply(CSAS2126), true) &&
                t.checkExpect(new HasPreReq("Software Engineering").apply(CSAS2126), false);
    }
    boolean testDeepestPathLength(Tester t) {
        return t.checkExpect(new DeepestPathLength().apply(CSAS1114), 0) &&
                t.checkExpect(new DeepestPathLength().apply(CSAS1115), 1) &&
                t.checkExpect(new DeepestPathLength().apply(CSAS2123), 2) &&
                t.checkExpect(new DeepestPathLength().apply(CSAS2125), 2) &&
                t.checkExpect(new DeepestPathLength().apply(CSAS2126), 4) &&
                t.checkExpect(new DeepestPathLength().apply(PHIL1204), 4);
    }
    }
    
    /*
    Notation: When you see notation of the form ClassName#methodName(...), it means
        methodName is a method defined in the class ClassName, and the arguments are
        left for you to determine. For example, String#substring(...).
    
        IFunc<T, R> T is input, R is return type
    
        need to add accept method to each class want to visit
        <R> R foldr(IBiFunc<T, R, R> bf, R base); //<R> is type parameter, takes in list, base elements, combine function
    */