import tester.*;

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

    // Folds the list into a single accumulator based upon the combinator
    public <B, C> B foldlAux(Combinator<T, B> combinator, B accum);

    // Folds the list into a single accumulator based upon the combinator
    <B, C> B foldl(B base, Combinator<T, B> combinator);

    // Applies the function func to every element within this list
    <X> IList<X> map(IFunc<T, X> func);

    // Removes all the duplicates contained within this list
    public IList<T> removeDuplicates();

    // Checks to see if the element t is contained within this list
    public boolean contains(T t);

    // Removes the given object t from this list
    public IList<T> removeObject(T t);

    // Returns the length of this list
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
     * this.andmap(IPred<T> pred) -- boolean
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

    // Folds the list into a single accumulator based upon the combinator
    public <B, C> B foldlAux(Combinator<T, B> combinator, B accum) {
        return accum;
    }

    // Folds the list into a single accumulator based upon the combinator
    public <B, C> B foldl(B base, Combinator<T, B> combinator) {
        return foldlAux(combinator, base);
    }
    
    // Applies the function func to every element within this list
    public <X> IList<X> map(IFunc<T, X> func) {
        return new MtList<X>();
    }
    
    // Checks to see if the element t is contained within this list
    public boolean contains(T t) {
        return false;
    }

    // Removes all the duplicates contained within this list
    public IList<T> removeDuplicates() {
        return new MtList<T>();
    }

    // Removes the given object t from this list
    public IList<T> removeObject(T t) {
        return new MtList<T>();
    }

    // Returns the length of this list
    public int length() {
        return 0;
    }
}

// A list with one or more elements in it
class ConsList<T> implements IList<T> {
    /* Template
     *
     * Fields:
     * this.first - T
     * this.rest - IList<T>
     * 
     * Methods:
     * this.equals(IList<T> list) -- boolean
     * this.equalsMt(MtList<T> list) -- boolean
     * this.equalsCons(ConsList<T> list) -- boolean
     * this.filter(IPred<T> pred) -- IList<T>
     * this.append(IList<T> list) -- IList<T>
     * this.append(T element) -- IList<T>
     * this.andmap(IPred<T> pred) -- boolean
     * 
     * Methods on fields:
     * this.rest.equals(IList<T> list) -- boolean
     * this.rest.equalsMt(MtList<T> list) -- boolean
     * this.rest.equalsCons(ConsList<T> list) -- boolean
     * this.rest.filter(IPred<T> pred) -- IList<T>
     * this.rest.append(IList<T> list) -- IList<T>
     * this.rest.append(T element) -- IList<T>
     * this.rest.andmap(IPred<T> pred) -- boolean
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
        return this.append(new ConsList<T>(element, new MtList<T>()));
    }

    // Maps a predicate check to all the elements within this
    public boolean andmap(IPred<T> pred) {
        return pred.apply(this.first) && this.rest.andmap(pred);
    }

    // Folds the list into a single accumulator based upon the combinator
    public <B, C> B foldlAux(Combinator<T, B> combinator, B accum) {
        return this.rest.foldlAux(combinator, combinator.apply(first, accum));
    }

    // Folds the list into a single accumulator based upon the combinator
    public <B, C> B foldl(B base, Combinator<T, B> combinator) {
        return foldlAux(combinator, base);
    }

    // Applies the function func to every element within this list
    public <X> IList<X> map(IFunc<T, X> func) {
        return new ConsList<X>(func.apply(first), this.rest.map(func));
    }

    // Checks to see if the element t is contained within this list
    public boolean contains(T t) {
        return first.equals(t) || rest.contains(t);
    }

    // Removes all the duplicates contained within this list
    public IList<T> removeDuplicates() {
        return new ConsList<T>(first, rest.filter(new ExtractObjectPred<T>(first)).removeDuplicates());
    }

    // Removes the given object t from this list
    public IList<T> removeObject(T t) {
        if (this.first.equals(t)) {
            return rest;
        } else {
            return new ConsList<T>(first, rest.removeObject(t));
        }
    }

    // Returns the length of this list
    public int length() {
        return 1 + this.rest.length();
    }
}

// Predicate used to extract a given object from a list using filter
class ExtractObjectPred<T> implements IPred<T> {
    /* Template
     *
     * Fields:
     * this.x - T
     * 
     * Methods:
     * this.apply(T y) -- boolean
     * 
     * Methods on fields:
     * 
     */

    T x;

    public boolean apply(T y) {
        return x.equals(y);
    }

    ExtractObjectPred(T x) {
        this.x = x;
    }
}

// Predicate used to extract a given list of objects from a list using filter
class ExtractListPred<T> implements IPred<T> {
    /* Template
     *
     * Fields:
     * this.x - IList<T>
     * 
     * Methods:
     * this.apply(T y) -- boolean
     * 
     * Methods on fields:
     * this.x.equals(IList<T> list) -- boolean
     * this.x.equalsMt(MtList<T> list) -- boolean
     * this.x.equalsCons(ConsList<T> list) -- boolean
     * this.x.filter(IPred<T> pred) -- IList<T>
     * this.x.append(IList<T> list) -- IList<T>
     * this.x.append(T element) -- IList<T>
     * this.x.andmap(IPred<T> pred) -- boolean
     * 
     */

    IList<T> x;

    public boolean apply(T y) {
        return (x.contains(y));
    }

    ExtractListPred(IList<T> x) {
        this.x = x;
    }
}

interface Combinator<T, B> {
    public B apply(T t, B b);
}

// Combines all the classes in which the given student is enrolled in
class InClassCombin implements Combinator<Course, IList<Student>> {
    /* Template
     *
     * Fields:
     * this.student -- Student
     * 
     * Methods:
     * this.apply -- IList<Student>
     * 
     * Methods on fields:
     * this.student.enroll(Course c) -- void
     * this.student.classmates(Student c) -- boolean
     * 
     */

    Student classmate;

    public IList<Student> apply(Course course, IList<Student> accum) {
        return course.students.filter(new ExtractObjectPred<Student>(classmate)).append(accum);
    }
    
    InClassCombin(Student classmate) {
        this.classmate = classmate;
    }
}

class Student {
    /* Template
     *
     * Fields:
     * this.name -- String
     * this.id -- int
     * this.courses -- IList<Course>
     * 
     * Methods:
     * this.enroll(Course c) -- void
     * this.classmates(Student c) -- boolean
     * 
     * Methods on fields:
     * this.courses.equals(IList<T> list) -- boolean
     * this.courses.equalsMt(MtList<T> list) -- boolean
     * this.courses.equalsCons(ConsList<T> list) -- boolean
     * this.courses.filter(IPred<T> pred) -- IList<T>
     * this.courses.append(IList<T> list) -- IList<T>
     * this.courses.append(T element) -- IList<T>
     * this.courses.andmap(IPred<T> pred) -- boolean
     * 
     */

    String name;
    int id;
    IList<Course> courses;

    Student(String name, int id) {
        this.name = name;
        this.id = id;
        this.courses = new MtList<Course>();
    }

    // Enrolls this student into the given course
    void enroll(Course c) {
        this.courses = this.courses.append(c);
        c.students = c.students.append(this);
    }

    // Returns whether this student is classmates with student c
    boolean classmates(Student c) {
        return !(this.courses.foldl(new MtList<Student>(), new InClassCombin(c)).length() == 0);
    }
}

class Instructor {
    /* Template
     *
     * Fields:
     * this.name -- String
     * this.courses -- IList<Course>
     * 
     * Methods:
     * this.dejavu(Student c) -- boolean
     * 
     * Methods on fields:
     * this.courses.equals(IList<T> list) -- boolean
     * this.courses.equalsMt(MtList<T> list) -- boolean
     * this.courses.equalsCons(ConsList<T> list) -- boolean
     * this.courses.filter(IPred<T> pred) -- IList<T>
     * this.courses.append(IList<T> list) -- IList<T>
     * this.courses.append(T element) -- IList<T>
     * this.courses.andmap(IPred<T> pred) -- boolean
     * 
     */
    String name;
    IList<Course> courses;

    Instructor(String name) {
        this.name = name;
        this.courses = new MtList<Course>();
    }

    // Returns whether this student is in more than one of the teachers classes
    boolean dejavu(Student c) {
        return this.courses.foldl(new MtList<Student>(), new InClassCombin(c)).length() > 1;
    }
}

class Course {
    /* Template
     *
     * Fields:
     * this.name -- String
     * this.prof -- Instructor
     * this.students -- IList<Course>
     * 
     * Methods:
     * this.enroll(Course c) -- void
     * this.classmates(Student c) -- boolean
     * 
     * Methods on fields:
     * this.students.equals(IList<T> list) -- boolean
     * this.students.equalsMt(MtList<T> list) -- boolean
     * this.students.equalsCons(ConsList<T> list) -- boolean
     * this.students.filter(IPred<T> pred) -- IList<T>
     * this.students.append(IList<T> list) -- IList<T>
     * this.students.append(T element) -- IList<T>
     * this.students.andmap(IPred<T> pred) -- boolean
     * 
     * this.prof.dejavu(Student c) -- boolean
     */

    String name;
    Instructor prof;
    IList<Student> students;

    Course(String name, Instructor prof) {
        this.name = name;
        this.prof = prof;
        this.students = new MtList<Student>();
        this.prof.courses = this.prof.courses.append(this);
    }
}

public class Registrar {

}

class Example {
    Instructor jasonHemann = new Instructor("Jason Hemann");
    Instructor marcoMorazon = new Instructor("Marco Morazon");
    Instructor vicenteMedina = new Instructor("Vincene Medina");
    Instructor eugeneReyonolds = new Instructor("Eugene Reynolds");
    Course CSAS1114 = new Course("Intro to Programming I", marcoMorazon);
    Course CSAS1115 = new Course("Intro to Programming II", marcoMorazon);
    Course CSAS2123 = new Course("Into to Objects I", jasonHemann);
    Course CSAS2126 = new Course("Data Structures", jasonHemann);
    Course MATH2711 = new Course("Statisics", eugeneReyonolds);
    Course PHIL1204 = new Course("Symbolic Logic", vicenteMedina);

    Student student1 = new Student("Sue", 127489);
    Student student2 = new Student("Tim", 187501);
    Student student3 = new Student("John", 213480);
    Student student4 = new Student("Amy", 226789);
    Student student5 = new Student("Craig", 324057);
}

class ExamplesRegistrar {
    boolean testEnrollOneClass(Tester t) {
        Example exp = new Example();
        exp.student1.enroll(exp.CSAS1114);
        exp.student2.enroll(exp.CSAS1115);
        exp.student3.enroll(exp.MATH2711);
        return t.checkExpect(exp.student1.courses, new ConsList<Course>(exp.CSAS1114, new MtList<Course>())) &&
                t.checkExpect(exp.student2.courses, new ConsList<Course>(exp.CSAS1115, new MtList<Course>())) &&
                t.checkExpect(exp.student3.courses, new ConsList<Course>(exp.MATH2711, new MtList<Course>()));
    }

    boolean testEnrollTwoClasses(Tester t) {
        Example exp = new Example();
        exp.student1.enroll(exp.CSAS1114);
        exp.student1.enroll(exp.MATH2711);

        exp.student2.enroll(exp.CSAS1115);
        exp.student2.enroll(exp.PHIL1204);

        exp.student3.enroll(exp.MATH2711);
        exp.student3.enroll(exp.CSAS1114);
        return t.checkExpect(exp.student1.courses, new ConsList<Course> (exp.CSAS1114, new ConsList<Course>(exp.MATH2711, new MtList<Course>()))) &&
                t.checkExpect(exp.student2.courses, new ConsList<Course> (exp.CSAS1115,  new ConsList<Course>(exp.PHIL1204, new MtList<Course>()))) &&
                t.checkExpect(exp.student3.courses, new ConsList<Course> (exp.MATH2711, new ConsList<Course>(exp.CSAS1114, new MtList<Course>())));
    }
    boolean testEnrollMoreThanTwoClasses(Tester t) {
        Example exp = new Example();
        exp.student4.enroll(exp.CSAS1114);
        exp.student4.enroll(exp.CSAS1115);
        exp.student4.enroll(exp.CSAS2123);
        exp.student4.enroll(exp.CSAS2126);
        exp.student4.enroll(exp.MATH2711);
        exp.student4.enroll(exp.PHIL1204);

        exp.student5.enroll(exp.CSAS2123);
        exp.student5.enroll(exp.CSAS2126);

        return t.checkExpect(exp.student4.courses,
                    new ConsList<Course>(exp.CSAS1114,
                        new ConsList<Course>(exp.CSAS1115,
                                new ConsList<Course>(exp.CSAS2123,
                                        new ConsList<Course>(exp.CSAS2126,
                                                new ConsList<Course>(exp.MATH2711,
                                                        new ConsList<Course>(exp.PHIL1204,
                                                                new MtList<Course>()))))))) &&
                t.checkExpect(exp.student5.courses,
                    new ConsList<Course>(exp.CSAS2123,
                            new ConsList<Course>(exp.CSAS2126,
                                    new MtList<Course>())));

        }

        boolean testClassmates(Tester t) {
            Example exp = new Example();
            exp.student1.enroll(exp.CSAS1114);
            exp.student1.enroll(exp.MATH2711);

            exp.student2.enroll(exp.CSAS1115);
            exp.student2.enroll(exp.PHIL1204);

            exp.student3.enroll(exp.MATH2711);
            exp.student3.enroll(exp.CSAS1114);

            exp.student4.enroll(exp.CSAS1114);
            exp.student4.enroll(exp.CSAS1115);
            exp.student4.enroll(exp.CSAS2123);
            exp.student4.enroll(exp.CSAS2126);
            exp.student4.enroll(exp.MATH2711);
            exp.student4.enroll(exp.PHIL1204);

            exp.student5.enroll(exp.CSAS2123);
            exp.student5.enroll(exp.CSAS2126);

            return  t.checkExpect(exp.student4.classmates(exp.student5), true) &&
                    t.checkExpect(exp.student3.classmates(exp.student1), true) &&
                    t.checkExpect(exp.student1.classmates(exp.student2), false) &&
                    t.checkExpect(exp.student1.classmates(exp.student3), true);
        }


    boolean testDejaVu(Tester t) {
        Example exp = new Example();
        exp.student1.enroll(exp.CSAS1114);
        exp.student1.enroll(exp.MATH2711);

        exp.student2.enroll(exp.CSAS1115);
        exp.student2.enroll(exp.PHIL1204);

        exp.student3.enroll(exp.MATH2711);
        exp.student3.enroll(exp.CSAS1114);

        exp.student4.enroll(exp.CSAS1114);
        exp.student4.enroll(exp.CSAS1115);
        exp.student4.enroll(exp.CSAS2123);
        exp.student4.enroll(exp.CSAS2126);
        exp.student4.enroll(exp.MATH2711);
        exp.student4.enroll(exp.PHIL1204);

        exp.student5.enroll(exp.CSAS2123);
        exp.student5.enroll(exp.CSAS2126);

        return t.checkExpect(exp.marcoMorazon.dejavu(exp.student4), true) &&
                t.checkExpect(exp.jasonHemann.dejavu(exp.student5), true) &&
                t.checkExpect(exp.vicenteMedina.dejavu(exp.student2), false) &&
                t.checkExpect(exp.eugeneReyonolds.dejavu(exp.student3), false) &&
                t.checkExpect(exp.jasonHemann.dejavu(exp.student1), false);
    }
}
