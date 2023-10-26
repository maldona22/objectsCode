import tester.Tester;

interface IPred<T> {
    boolean apply(T t);
}

class SmallerThanMonomial implements IPred<Monomial> {
    Monomial monomial;

    public boolean apply(Monomial t) {
        return t.degree < monomial.degree;
    }

    SmallerThanMonomial(Monomial monomial) {
        this.monomial = monomial;
    }
}

class LargerThanMonomial implements IPred<Monomial> {
    Monomial monomial;

    public boolean apply(Monomial t) {
        return t.degree > monomial.degree;
    }

    LargerThanMonomial(Monomial monomial) {
        this.monomial = monomial;
    }
}

class NotEqualToCoefficient implements IPred<Monomial> {
    int coefficient;

    public boolean apply(Monomial t) {
        return coefficient != t.coefficient;
    }

    NotEqualToCoefficient(int coefficient) {
        this.coefficient = coefficient;
    }
}

class NotEqualToDegree implements IPred<Monomial> {
    int degree;

    public boolean apply(Monomial t) {
        return degree != t.degree;
    }

    NotEqualToDegree(Monomial t) {
        this.degree = t.degree;
    }
}

interface IList<T> {
    boolean equals(IList<T> list);

    boolean equalsCons(ConsList<T> list);

    boolean equalsMt(MtList<T> list);

    IList<T> filter(IPred<T> pred);

    IList<T> append(IList<T> list);

    IList<T> append(T element);

    IList<T> quickSortList(predicateLambda<T, IPred<T>> smallPred,
            predicateLambda<T, IPred<T>> largePred);

    int length();

    boolean andmap(IPred<T> pred);

    boolean containsNoDuplicates(predicateLambda<T, IPred<T>> duplicatePred);
}
    
class MtList<T> implements IList<T> {
    public boolean equals(IList<T> list) {
        return list.equalsMt(this);
    }

    public boolean equalsMt(MtList<T> list) {
        return true;
    }

    public boolean equalsCons(ConsList<T> list) {
        return false;
    }

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

    public IList<T> quickSortList(predicateLambda<T, IPred<T>> smallPred,
            predicateLambda<T, IPred<T>> largePred) {
        return new MtList<T>();
    }

    public boolean andmap(IPred<T> pred) {
        return true;
    }

    public boolean containsNoDuplicates(predicateLambda<T, IPred<T>> duplicatePred) {
        return true;
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
        return new ConsList<T>(first, this.rest.append(list));
    }

    public IList<T> append(T element) {
        return new ConsList<T>(first, new ConsList<T>(element, new MtList<T>()));
    }

    public int length() {
        return 1 + this.rest.length();
    }

    public boolean andmap(IPred<T> pred) {
        return pred.apply(this.first) && this.rest.andmap(pred);
    }

    static <N> IPred<N> makePred(predicateLambda<N, IPred<N>> operator, N pivot) {
        return operator.op(pivot);
    }
    
    public IList<T> quickSortList(predicateLambda<T, IPred<T>> smallPred,
            predicateLambda<T, IPred<T>> largePred) {
        IList<T> smallerList = this.filter(makePred(smallPred, this.first));
        IList<T> biggerList = this.filter(makePred(largePred, this.first));
        return smallerList.quickSortList(smallPred, largePred).append(this.first)
                .append(biggerList.quickSortList(smallPred, largePred));
    }

    public boolean containsNoDuplicates(predicateLambda<T, IPred<T>> duplicatePred) {
        return this.rest.andmap(makePred(duplicatePred, this.first)) && this.rest.containsNoDuplicates(duplicatePred);
    }
}

interface predicateLambda<T,R> {
    public R op(T t);
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
    
    predicateLambda<Monomial, IPred<Monomial>> smallPred = (a) -> new SmallerThanMonomial(a);
    predicateLambda<Monomial, IPred<Monomial>> largePred = (a) -> new LargerThanMonomial(a);
    predicateLambda<Monomial, IPred<Monomial>> duplicatePred = (a) -> new NotEqualToDegree(a); 

    Polynomial(IList<Monomial> monomials) {
        IList<Monomial> sortedMonomials = monomials.quickSortList(smallPred, largePred);
        if (sortedMonomials.containsNoDuplicates(duplicatePred)) {
            this.monomials = sortedMonomials;
        } else {
            // TODO come up with error message for this
            throw new IllegalArgumentException();
        }
    }
    
    boolean equals(Polynomial other) {
        IList<Monomial> thisFiltered = this.monomials.filter(new NotEqualToCoefficient(0));
        IList<Monomial> otherFiltered = other.monomials.filter(new NotEqualToCoefficient(0));

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
        printPoly(testPoly1.monomials);
        System.out.println("Test Poly 2:");
        printPoly(testPoly2.monomials);
        System.out.println("Testing");
        printPoly(testPoly1.monomials.filter(new NotEqualToCoefficient(0)));
        printPoly(testPoly2.monomials.filter(new NotEqualToCoefficient(0)));
        System.out.println(testPoly1.equals(testPoly2));
    }
}

class ExamplesPolynomial {
    Monomial deg1 = new Monomial(1, 2);
    Monomial deg3 = new Monomial(3, 4);
    Monomial deg5 = new Monomial(5, 1);
    ConsList<Monomial> list1 = new ConsList<Monomial>(deg1,
            new ConsList<Monomial>(deg3, new ConsList<Monomial>(deg5, new MtList<Monomial>())));
    ConsList<Monomial> list2 = new ConsList<Monomial>(deg3,
            new ConsList<Monomial>(deg1, new ConsList<Monomial>(deg5, new MtList<Monomial>())));
    Polynomial testPoly1 = new Polynomial(list1);
    Polynomial testPoly2 = new Polynomial(list2);
 
    boolean testPolynomial(Tester t) {
        return t.checkExpect(testPoly2, new Polynomial(new ConsList<Monomial>(deg1,
                new ConsList<Monomial>(deg3, new ConsList<Monomial>(deg5, new MtList<Monomial>())))));
    }

}