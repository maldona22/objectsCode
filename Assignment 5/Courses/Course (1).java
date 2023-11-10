import tester.*;
/* A Course has a name and preReqs, which is an IList<Course>
(see Lecture 15 for the data definition of parametrized lists).

Design the Course class and any other necessary classes and interfaces.
*/
interface IFunc<A, R> {
    R apply(A arg);
}

interface IListVisitor<T, R> extends IFunc<IList<T>, R> {
    R visitMtList(MtList<T> mtList);
    R visitConsList(ConsList<T> consList);
}

interface IList<T> {
    /* 
    int getDeepestPathLength();
    boolean hasPreReqs(String preReqName);
    */
    boolean ormap(IPred<T> pred);
    int accept(IListVisitor<T, Integer> visitor);

    boolean accept(Ormap<T> t);

    boolean isEmpty();

    <X> IList<X> map(IFunc<T, X> func);
}
    
class MtList<T> implements IList<T> {
    
    public boolean ormap(IPred<T> pred) {
        return false;
    }
    
    public int accept(IListVisitor<T, Integer> visitor) {
        return visitor.visitMtList(this);
    }

    public boolean accept(Ormap<T> t) {
        return t.visitMtList(this);
    }
    
    public boolean isEmpty() {
        return true;
    }

    public <X> IList<X> map(IFunc<T, X> func) {
        return new MtList<X>();
    }
}
    
class ConsList<T> implements IList<T> {
        T first;
        IList<T> rest;

        ConsList(T first, IList<T> rest) {
        this.first = first;
        this.rest = rest;
    }
    
    public boolean ormap(IPred<T> pred) {
        return pred.apply(first) || rest.ormap(pred);
    }
    
    public int accept(IListVisitor<T, Integer> visitor) {
        return visitor.visitConsList(this);
    }

    public boolean accept(Ormap<T> t) {
        return t.visitConsList(this);
    }
    
    public boolean isEmpty() {
        return false;
    }

    public <X> IList<X> map(IFunc<T, X> func) {
        return new ConsList<X>(func.apply(first), this.rest.map(func));
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
        return new HasPreReq(preReqName).apply(this);
    }
    public int getDeepestPathLength() {
        return new DeepestPathLength().apply(this);
    }
    
    public boolean ormap(IPred<Course> pred) {
        return new Ormap<Course>(pred).apply(this.preReqs);
    }
    
    public Boolean accept(HasPreReq hasPreReq) {
        return hasPreReq.apply(this);
    }

    public int accept(DeepestPathLength deepestPathLength) {
        return deepestPathLength.apply(this);
    }
}
    
class DeepestPathLength implements IFunc<Course, Integer>{
    public Integer apply(Course course) {
        if (course.preReqs.isEmpty()) {
            return 1;
        }
        else {
            MaxVisitor visitor = new MaxVisitor()
            return 1 + visitor.apply(course.preReqs.map(this));
        }
    }
}
    
interface IPred<T> extends IFunc<T, Boolean> { }
    
class HasPreReq implements IPred<Course> {
    String preReqName;

    public HasPreReq(String preReqName) {
        this.preReqName = preReqName;
    }

    public Boolean apply(Course course) {
        return course.name.equals(preReqName) || new Ormap<Course>(this).apply(course.preReqs);
    }
}

class HasAnyPreReq implements IPred<Course> {
    public Boolean apply(Course course) {
        return course.preReqs.isEmpty();
    }
}
    
class Ormap<T> implements IListVisitor<T, Boolean> {
    IPred<T> pred;

    public Ormap(IPred<T> pred) {
        this.pred = pred;
    }

    public Boolean visitMtList(MtList<T> mtList) {
        return false;
    }

    public Boolean visitConsList(ConsList<T> consList) {
        return pred.apply(consList.first) || consList.rest.accept(this);
    }

    public Boolean apply(IList<T> arg) {
        return arg.accept(this);
    }
}

class MaxVisitor implements IListVisitor<Integer, Integer> {

    public Integer visitMtList(MtList<Integer> mtList) {
        return Integer.MIN_VALUE;
    }

    public Integer visitConsList(ConsList<Integer> consList) {
        return Math.max(consList.first, consList.rest.accept(this));
    }

    public Integer apply(IList<Integer> list) {
        return list.accept(this);
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