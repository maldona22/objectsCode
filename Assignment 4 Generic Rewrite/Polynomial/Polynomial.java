import tester.Tester;

// Template for making predicates for use in higher order functions
interface IPred<T> {
    boolean apply(T t);
}

// Predicate that checks if a monomial is smaller than another
class SmallerThanMonomial implements IPred<Monomial> {
    /* Template
     *
     * Fields:
     * this.monomial -- Monomial
     * 
     * Methods:
     * this.apply(Monomial t) -- boolean
     * 
     * Methods on fields:
     * this.monomial.equals(Monomial other) -- boolean
     */

    Monomial monomial;

    // Applies the predicate to a monomial
    public boolean apply(Monomial t) {
        return t.degree < monomial.degree;
    }

    // Constructor for SmallerThanMonomial
    SmallerThanMonomial(Monomial monomial) {
        this.monomial = monomial;
    }
}

// Predicate that checks if a monomial is larger than another
class LargerThanMonomial implements IPred<Monomial> {
    /* Template
     *
     * Fields:
     * this.monomial -- Monomial
     * 
     * Methods:
     * this.apply(Monomial t) -- boolean
     * 
     * Methods on fields:
     * this.monomial.equals(Monomial other) -- boolean
     */

    Monomial monomial;

    // Applies the predicate to a monomial
    public boolean apply(Monomial t) {
        return t.degree > monomial.degree;
    }

    // Constructor for LargerThanMonomial
    LargerThanMonomial(Monomial monomial) {
        this.monomial = monomial;
    }
}

// Predicate that checks if a monomials coefficient is not equal to a pre-specified coefficient
class NotEqualToCoefficient implements IPred<Monomial> {
    /* Template
     *
     * Fields:
     * this.coefficient -- int
     * 
     * Methods:
     * this.apply(Monomial t) -- boolean
     * 
     * Methods on fields:
     */
    int coefficient;

    // Applies the predicate to a monomial
    public boolean apply(Monomial t) {
        return coefficient != t.coefficient;
    }

    // Constructor for NotEqualToCoefficient
    NotEqualToCoefficient(int coefficient) {
        this.coefficient = coefficient;
    }
}

// Predicate that checks if a monomials degree is not equal to a pre-specified degree
class NotEqualToDegree implements IPred<Monomial> {
    /* Template
     *
     * Fields:
     * this.degree -- degree
     * 
     * Methods:
     * this.apply(Monomial t) -- boolean
     * 
     * Methods on fields:
     */
    int degree;

    // Applies the predicate to a monomial
    public boolean apply(Monomial t) {
        return degree != t.degree;
    }

    // Constructor for NotEqualToDegree
    NotEqualToDegree(Monomial t) {
        this.degree = t.degree;
    }
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

    // Sorts elements within this based upon the given predicate lambdas
    IList<T> quickSortList(predicateLambda<T, IPred<T>> smallPred,
            predicateLambda<T, IPred<T>> largePred);

    // Maps a predicate check to all the elements within this
    boolean andmap(IPred<T> pred);

    // Computes if there are any duplicate elements in this based upon the given predicate lambda
    boolean containsNoDuplicates(predicateLambda<T, IPred<T>> duplicatePred);
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
     * this.quickSortList(predicateLambda<T,IPred<T>> smallPred, predicateLambda<T,IPred<T>> largePred) -- IList<T>
     * this.andmap(IPred<T> pred) -- boolean
     * this.containsNoDuplicates(predicateLambda<T,IPred<T>> duplicatePred) -- boolean
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

    // Sorts elements within this based upon the given predicate lambdas
    public IList<T> quickSortList(predicateLambda<T, IPred<T>> smallPred,
            predicateLambda<T, IPred<T>> largePred) {
        return new MtList<T>();
    }

    // Maps a predicate check to all the elements within this
    public boolean andmap(IPred<T> pred) {
        return true;
    }

    // Computes if there are any duplicate elements in this based upon the given predicate lambda
    public boolean containsNoDuplicates(predicateLambda<T, IPred<T>> duplicatePred) {
        return true;
    }
}

// A list with one or more elements in it
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
     * this.filter(IPred<T> pred) -- IList<T>
     * this.append(IList<T> list) -- IList<T>
     * this.append(T element) -- IList<T>
     * this.quickSortList(predicateLambda<T,IPred<T>> smallPred, predicateLambda<T,IPred<T>> largePred) -- IList<T>
     * this.andmap(IPred<T> pred) -- boolean
     * this.containsNoDuplicates(predicateLambda<T,IPred<T>> duplicatePred) -- boolean
     * <N> ConsList.makePred(predicateLambda<N, IPred<N>> operator, N pivot) -- IPred<N>
     * 
     * Methods on fields:
     * this.rest.equals(IList<T> list) -- boolean
     * this.rest.equalsMt(MtList<T> list) -- boolean
     * this.rest.equalsCons(ConsList<T> list) -- boolean
     * this.rest.filter(IPred<T> pred) -- IList<T>
     * this.rest.append(IList<T> list) -- IList<T>
     * this.rest.append(T element) -- IList<T>
     * this.rest.quickSortList(predicateLambda<T,IPred<T>> smallPred, predicateLambda<T,IPred<T>> largePred) -- IList<T>
     * this.rest.andmap(IPred<T> pred) -- boolean
     * this.rest.containsNoDuplicates(predicateLambda<T,IPred<T>> duplicatePred) -- boolean
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
        return new ConsList<T>(first, new ConsList<T>(element, new MtList<T>()));
    }

    // Maps a predicate check to all the elements within this
    public boolean andmap(IPred<T> pred) {
        return pred.apply(this.first) && this.rest.andmap(pred);
    }

    // Produces a predicate based upon the lambda given and an object of type N
    static <N> IPred<N> makePred(predicateLambda<N, IPred<N>> operator, N pivot) {
        return operator.op(pivot);
    }

    // Sorts elements within this based upon the given predicate lambdas
    public IList<T> quickSortList(predicateLambda<T, IPred<T>> smallPred,
            predicateLambda<T, IPred<T>> largePred) {
        IList<T> smallerList = this.filter(makePred(smallPred, this.first));
        IList<T> biggerList = this.filter(makePred(largePred, this.first));
        return smallerList.quickSortList(smallPred, largePred).append(this.first)
                .append(biggerList.quickSortList(smallPred, largePred));
    }

    // Computes if there are any duplicate elements in this based upon the given predicate lambda
    public boolean containsNoDuplicates(predicateLambda<T, IPred<T>> duplicatePred) {
        return this.rest.andmap(makePred(duplicatePred, this.first)) && this.rest.containsNoDuplicates(duplicatePred);
    }
}

// Template for predicate lambdas
interface predicateLambda<T, R> {
    public R op(T t);
}

// A single variable with a coefficient and degree exponent
class Monomial {
    /* Template
     *
     * Fields:
     * this.degree -- int
     * this.coefficient -- int
     * 
     * Methods:
     * this.equals(Monomial other) -- boolean
     * 
     * Methods on fields:
     */

    int degree;
    int coefficient;

    // Computes if this is equal to other
    boolean equals(Monomial other) {
        return (this.degree == other.degree && this.coefficient == other.coefficient);
    }

    // Constructor for Monomial
    Monomial(int degree, int coefficient) {
        if (degree >= 0) {
            this.degree = degree;
            this.coefficient = coefficient;
        } else {
            throw new IllegalArgumentException();
        }
    }
}

// Multiple monomials added and/or subtracted from each other into a single term
public class Polynomial {
    /* Template
     *
     * Fields:
     * this.monomials -- IList<Monomial>
     * this.smallPred -- predicateLambda<Monomial, IPred<Monomial>>
     * this.largePred -- predicateLambda<Monomial, IPred<Monomial>>
     * this.duplicatePred -- predicateLambda<Monomial, IPred<Monomial>>
     * 
     * Methods:
     * this.equals(Polynomial other) -- boolean
     * 
     * Methods on fields:
     * this.monomials.equals(IList<T> list) -- boolean
     * this.monomials.equalsMt(MtList<T> list) -- boolean
     * this.monomials.equalsCons(ConsList<T> list) -- boolean
     * this.monomials.filter(IPred<T> pred) -- IList<T>
     * this.monomials.append(IList<T> list) -- IList<T>
     * this.monomials.append(T element) -- IList<T>
     * this.monomials.quickSortList(predicateLambda<T,IPred<T>> smallPred, predicateLambda<T,IPred<T>> largePred) -- IList<T>
     * this.monomials.andmap(IPred<T> pred) -- boolean
     * this.monomials.containsNoDuplicates(predicateLambda<T,IPred<T>> duplicatePred) -- boolean
     */

    IList<Monomial> monomials;
    
    predicateLambda<Monomial, IPred<Monomial>> smallPred = (a) -> new SmallerThanMonomial(a);
    predicateLambda<Monomial, IPred<Monomial>> largePred = (a) -> new LargerThanMonomial(a);
    predicateLambda<Monomial, IPred<Monomial>> duplicatePred = (a) -> new NotEqualToDegree(a);

    // Constructor for Polynomial
    Polynomial(IList<Monomial> monomials) {
        IList<Monomial> sortedMonomials = monomials.filter(new NotEqualToCoefficient(0)).quickSortList(smallPred, largePred);
        if (sortedMonomials.containsNoDuplicates(duplicatePred)) {
            this.monomials = sortedMonomials;
        } else {
            throw new IllegalArgumentException();
        }
    }
    
    // Computes if this is equal to other
    boolean equals(Polynomial other) {
        return this.monomials.equals(other.monomials);
    }
}

class ExamplesPolynomial {
    Monomial deg1 = new Monomial(1, 2);
    Monomial deg3 = new Monomial(3, 4);
    Monomial deg5_1 = new Monomial(5, 1);
    Monomial deg6 = new Monomial(6, 0);
    Monomial deg5_2 = new Monomial(5, 2);
    ConsList<Monomial> list1 = new ConsList<Monomial>(deg1,
            new ConsList<Monomial>(deg3, new ConsList<Monomial>(deg5_1, new MtList<Monomial>())));
    ConsList<Monomial> list2 = new ConsList<Monomial>(deg3,
            new ConsList<Monomial>(deg1, new ConsList<Monomial>(deg5_1, new MtList<Monomial>())));
    ConsList<Monomial> list3 = new ConsList<Monomial>(deg1,
            new ConsList<Monomial>(deg3, new ConsList<Monomial>(deg5_1, new ConsList<Monomial>(deg6, new MtList<Monomial>()))));
    Polynomial testPoly1 = new Polynomial(list1);
    Polynomial testPoly2 = new Polynomial(list2);

    predicateLambda<Monomial, IPred<Monomial>> smallPred = (a) -> new SmallerThanMonomial(a);
    predicateLambda<Monomial, IPred<Monomial>> largePred = (a) -> new LargerThanMonomial(a);
    predicateLambda<Monomial, IPred<Monomial>> duplicatePred = (a) -> new NotEqualToDegree(a);


 
    boolean testPolynomial(Tester t) {
        return t.checkExpect(testPoly1, new Polynomial(new ConsList<Monomial>(deg1,
                new ConsList<Monomial>(deg3, new ConsList<Monomial>(deg5_1, new MtList<Monomial>())))));
    }

    boolean testPolynomialEquals(Tester t) {
        return t.checkExpect(testPoly2.equals(new Polynomial(new ConsList<Monomial>(deg1,
                new ConsList<Monomial>(deg3, new ConsList<Monomial>(deg5_1, new MtList<Monomial>()))))), true);
    }

    boolean testConsListEquals(Tester t) {
        return t.checkExpect(list1.equals(list2.quickSortList(smallPred, largePred)), true);
    }

    boolean testMtListEquals(Tester t) {
        MtList<Monomial> testList = new MtList<Monomial>();
        MtList<Monomial> testList2 = new MtList<Monomial>();
        return t.checkExpect(testList2.equals(testList), true);
    }

    boolean testFilter(Tester t) {
        return t.checkExpect(list1.equals(list3.filter(new NotEqualToCoefficient(0))), true);
    }

    boolean testConsListAppendList(Tester t) {
        ConsList<Monomial> testList = new ConsList<Monomial>(deg1,
                new ConsList<Monomial>(deg3,
                        new ConsList<Monomial>(deg5_1, new ConsList<Monomial>(deg6, new MtList<Monomial>()))));
        return t.checkExpect(testList.equals(list1.append(new ConsList<Monomial>(deg6, new MtList<Monomial>()))), true);
    }

    boolean testMtListAppendList(Tester t) {
        return t.checkExpect((new MtList<Monomial>()).append(list1).equals(list1), true);
    }
    
    boolean testConsListAppendElement(Tester t) {
        ConsList<Monomial> testList = new ConsList<Monomial>(deg1,
                new ConsList<Monomial>(deg3,
                        new ConsList<Monomial>(deg5_1, new ConsList<Monomial>(deg6, new MtList<Monomial>()))));
        return t.checkExpect(testList.equals(list1.append(deg6)), true);
    }

    boolean testMtListAppendElement(Tester t) {
        return t.checkExpect(
                (new MtList<Monomial>()).append(deg1).equals(new ConsList<Monomial>(deg1, new MtList<Monomial>())),
                true);
    }

    boolean testContainsNoDuplicates(Tester t) {
        IList<Monomial> testList = list1.append(deg5_2);
        return t.checkExpect(testList.containsNoDuplicates(duplicatePred), false);
    }

    boolean testQuickSortList(Tester t) {
        return t.checkExpect(list1.equals(list2.quickSortList(smallPred, largePred)), true);
    }

    boolean testSmallerThanMonomialTrue(Tester t) {
        IPred<Monomial> testPred = new SmallerThanMonomial(deg3);
        return t.checkExpect(testPred.apply(deg1), true);
    }

    boolean testSmallerThanMonomialFalse(Tester t) {
        IPred<Monomial> testPred = new SmallerThanMonomial(deg3);
        return t.checkExpect(testPred.apply(deg5_1), false);
    }

    boolean testLargerThanMonomialTrue(Tester t) {
        IPred<Monomial> testPred = new LargerThanMonomial(deg3);
        return t.checkExpect(testPred.apply(deg5_1), true);
    }

    boolean testLargerThanMonomialFalse(Tester t) {
        IPred<Monomial> testPred = new LargerThanMonomial(deg3);
        return t.checkExpect(testPred.apply(deg1), false);
    }

    boolean testNotEqualToCoefficientTrue(Tester t) {
        IPred<Monomial> testPred = new NotEqualToCoefficient(2);
        return t.checkExpect(testPred.apply(deg5_1), true);
    }

    boolean testNotEqualToCoefficientFalse(Tester t) {
        IPred<Monomial> testPred = new NotEqualToCoefficient(2);
        return t.checkExpect(testPred.apply(deg1), false);
    }

    boolean testNotEqualToDegreeTrue(Tester t) {
        IPred<Monomial> testPred = new NotEqualToDegree(deg3);
        return t.checkExpect(testPred.apply(deg1), true);
    }

    boolean testNotEqualToDegreeFalse(Tester t) {
        IPred<Monomial> testPred = new NotEqualToDegree(deg5_1);
        return t.checkExpect(testPred.apply(deg5_2), false);
    }
}