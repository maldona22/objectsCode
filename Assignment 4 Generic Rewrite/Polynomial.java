
interface IPred<T> {
    boolean apply(T t);
}

class LargerThan implements IPred<Integer> {
    Integer num;

    public boolean apply(Integer t) {
        return t > num;
    }

    // TODO put checks on degree here like in the rest of the code
    LargerThan(Integer number) {
        this.num = number;
    }
}

class SmallerThanMonomial implements IPred<Monomial> {
    Monomial monomial;

    public boolean apply(Monomial t) {
        return t.degree < monomial.degree;
    }

    // TODO put checks on degree here like in the rest of the code
    SmallerThanMonomial(Monomial monomial) {
        this.monomial = monomial;
    }
}

class LargerThanMonomial implements IPred<Monomial> {
    Monomial monomial;

    public boolean apply(Monomial t) {
        return t.degree > monomial.degree;
    }

    // TODO put checks on degree here like in the rest of the code
    LargerThanMonomial(Monomial monomial) {
        this.monomial = monomial;
    }
}

class EqualToCoefficient implements IPred<Monomial> {
    int coefficient;

    public boolean apply(Monomial t) {
        return coefficient == t.coefficient;
    }

    // TODO put checks on degree here like in the rest of the code
    EqualToCoefficient(int coefficient) {
        this.coefficient = coefficient;
    }
}

interface IComparator<T> {
    int compare(T x, T y);
}

interface IList<T> {
    IList<T> filter(IPred<T> pred);

    IList<T> append(IList<T> list);

    IList<T> append(T element);

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
}

class ConsList<T> implements IList<T> {
    T first;
    IList<T> rest;

    ConsList(T first, IList<T> rest) {
        this.first = first;
        this.rest = rest;
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

class UtilsPolynomial {

    static IList<Monomial> quickSortList(IList<Monomial> list) {
        if (list instanceof MtList<Monomial>) {
            return new MtList<Monomial>();
        } else {
            IList<Monomial> smallerList = list.filter(new SmallerThanMonomial(((ConsList<Monomial>) list).first));
            IList<Monomial> biggerList = list.filter(new LargerThanMonomial(((ConsList<Monomial>) list).first));
            return quickSortList(smallerList).append(((ConsList<Monomial>) list).first)
                    .append(quickSortList(biggerList));
        }
    }

    static boolean containsNoDuplicates(IList<Monomial> list) {
        // Just need to preload it with a value thats not going to be valid
        // since we check for negative degrees this will never be a degree we compare against
        return checkForDuplicateDegrees(Integer.MIN_VALUE, list);
    }

    private static boolean checkForDuplicateDegrees(int prevDegree, IList<Monomial> list) {
        if (list instanceof MtList<Monomial>) {
            return true;
        } else {
            if (prevDegree != ((ConsList<Monomial>) list).first.degree) {
                return checkForDuplicateDegrees(((ConsList<Monomial>) list).first.degree, ((ConsList<Monomial>) list).rest);
            } else {
                return false;
            }
        }
    }
}

class Monomial {
    int degree;
    int coefficient;

    boolean equals(Monomial other) {
        return (this.degree == other.degree && this.coefficient == other.coefficient);
    }

    Monomial(int degree, int coefficient) {
        if (degree >= 0) {
            this.degree = degree;
            this.coefficient = coefficient;
        }
        else {
            throw new IllegalArgumentException();
        }
    }
}

public class Polynomial {
    IList<Monomial> monomials;
    
    Polynomial(IList<Monomial> monomials) {
        IList<Monomial> sortedMonomials = UtilsPolynomial.quickSortList(monomials);
        if (UtilsPolynomial.containsNoDuplicates(sortedMonomials)) {
            this.monomials = sortedMonomials;
        } else {
            // TODO come up with error message for this
            throw new IllegalArgumentException();
        }
    }
    
    boolean equals(Polynomial other) {
        IList<Monomial> thisFiltered = this.monomials.filter(new EqualToCoefficient(0));
        IList<Monomial> otherFiltered = other.monomials.filter(new EqualToCoefficient(0));

        return thisFiltered.equals(otherFiltered);
    }

    static void printPoly(IList<Monomial> poly) {
        if (poly instanceof ConsList<Monomial>) {
            System.out.println( ((ConsList<Monomial>)poly).first.coefficient + " " + ((ConsList<Monomial>) poly).first.degree);
            printPoly(((ConsList<Monomial>) poly).rest);
        }
        else {
            return;
        }
    }

    public static void main(String[] args) {
        Monomial deg1 = new Monomial(1, 2);
        Monomial deg3 = new Monomial(3, 4);
        Monomial deg5 = new Monomial(5, 1);
        ConsList<Monomial> list1 = new ConsList<Monomial>(deg1,
                new ConsList<Monomial>(deg3, new ConsList<Monomial>(deg5, new MtList<Monomial>())));
        ConsList<Monomial> list2 = new ConsList<Monomial>(deg3,
                new ConsList<Monomial>(deg1, new ConsList<Monomial>(deg5, new MtList<Monomial>())));
        Polynomial testPoly1 = new Polynomial(list1);
        Polynomial testPoly2 = new Polynomial(list2);

        System.out.println("Test Poly 1: ");
        printPoly(list1);
        System.out.println("Test Poly 2:");
        printPoly(list2);
        System.out.println("Testing");
        printPoly(testPoly1.monomials.filter(new EqualToCoefficient(0)));
        printPoly(testPoly2.monomials.filter(new EqualToCoefficient(0)));
        System.out.println(testPoly1.equals(testPoly2));
    }
}