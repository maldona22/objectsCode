class UtilsPolynomial {
    static ILoMonomial append(ILoMonomial left, ILoMonomial right) {
        if (left instanceof MtLoMonomial) {
            return right;
        } else {
            return new ConsLoMonomial(((ConsLoMonomial) left).first, append(((ConsLoMonomial) left).rest, right));
        }
    }
    
    static ILoMonomial filterSmaller(int degree, ILoMonomial list) {
        if (list instanceof MtLoMonomial) {
            return new MtLoMonomial();
        }
        if (degree > ((ConsLoMonomial) list).first.degree) {
            return new ConsLoMonomial(((ConsLoMonomial) list).first,
                    filterSmaller(degree, ((ConsLoMonomial) list).rest));
        } else {
            return filterSmaller(degree, ((ConsLoMonomial) list).rest);
        }
    }

    static ILoMonomial filterLarger(int degree, ILoMonomial list) {
        if (list instanceof MtLoMonomial) {
            return new MtLoMonomial();
        }
        if (degree <= ((ConsLoMonomial) list).first.degree) {
            return new ConsLoMonomial(((ConsLoMonomial) list).first,
                    filterLarger(degree, ((ConsLoMonomial) list).rest));
        } else {
            return filterLarger(degree, ((ConsLoMonomial) list).rest);
        }
    }

    static ILoMonomial quickSortList(ILoMonomial list) {
        if (list instanceof MtLoMonomial) {
            return new MtLoMonomial();
        } else {
            ILoMonomial smallerList = filterSmaller(((ConsLoMonomial) list).first.degree, ((ConsLoMonomial) list).rest);
            ILoMonomial biggerList = filterLarger(((ConsLoMonomial) list).first.degree, ((ConsLoMonomial) list).rest);
            return append(quickSortList(smallerList),
                    new ConsLoMonomial(((ConsLoMonomial) list).first, quickSortList(biggerList)));
        }
    }

    static boolean containsNoDuplicates(ILoMonomial list) {
        // Just need to preload it with a value thats not going to be valid
        // since we check for negative degrees this will never be a degree we compare against
        return checkForDuplicateDegrees(Integer.MIN_VALUE, list);
    }

    private static boolean checkForDuplicateDegrees(int prevDegree, ILoMonomial list) {
        if (list instanceof MtLoMonomial) {
            return true;
        } else {
            if (prevDegree != ((ConsLoMonomial) list).first.degree) {
                return checkForDuplicateDegrees(((ConsLoMonomial) list).first.degree, ((ConsLoMonomial) list).rest);
            } else {
                return false;
            }
        }
    }
    
    static ILoMonomial filterZeroCoefficients(ILoMonomial list) {
        if (list instanceof MtLoMonomial) {
            return new MtLoMonomial();
        }
        if (((ConsLoMonomial) list).first.coefficient != 0) {
            return new ConsLoMonomial(((ConsLoMonomial) list).first,
                    filterZeroCoefficients(((ConsLoMonomial) list).rest));
        } else {
            return filterZeroCoefficients(((ConsLoMonomial) list).rest);
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

interface ILoMonomial {
    boolean equals(ILoMonomial other);
}

class MtLoMonomial implements ILoMonomial {
    public boolean equals(ILoMonomial other) {
        return (other instanceof MtLoMonomial);
    }
}

class ConsLoMonomial implements ILoMonomial {
    Monomial first;
    ILoMonomial rest;

    ConsLoMonomial(Monomial first, ILoMonomial rest) {
        this.first = first;
        this.rest = rest;
    }

    public boolean equals(ILoMonomial other) {
        if (other instanceof ConsLoMonomial) {
            if (this.first.equals(((ConsLoMonomial) other).first)) {
                return this.rest.equals(((ConsLoMonomial) other).rest);
            } else {
                return false;
            }
        }
        else {
            return false;
        }
    }
    /*
    public boolean equals(ILoMonomial other) {
        if (other instanceof ConsLoMonomial) {
            if (this.first.equals(((ConsLoMonomial)other).first)) {
                if ( (((ConsLoMonomial)other).rest instanceof ConsLoMonomial) && 
                    (this.rest instanceof ConsLoMonomial)) {
                    return ((ConsLoMonomial)this.rest).equals(((ConsLoMonomial)other).rest);
                }
                else if ( (((ConsLoMonomial)other).rest instanceof MtLoMonomial) && 
                        (this.rest instanceof MtLoMonomial)) {
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
    */
}

public class Polynomial {
    ILoMonomial monomials;
    
    Polynomial(ILoMonomial monomials) {
        ILoMonomial sortedMonomials = UtilsPolynomial.quickSortList(monomials);
        if (UtilsPolynomial.containsNoDuplicates(sortedMonomials)) {
            this.monomials = sortedMonomials;
        } else {
            // TODO come up with error message for this
            throw new IllegalArgumentException();
        }
    }
    
    boolean equals(Polynomial other) {
        ILoMonomial thisFiltered = UtilsPolynomial.filterZeroCoefficients(this.monomials);
        ILoMonomial otherFiltered = UtilsPolynomial.filterZeroCoefficients(other.monomials);

        return thisFiltered.equals(otherFiltered);
    }

    static void printPoly(ILoMonomial poly) {
        if (poly instanceof ConsLoMonomial) {
            System.out.println( ((ConsLoMonomial)poly).first.coefficient + " " + ((ConsLoMonomial) poly).first.degree);
            printPoly(((ConsLoMonomial) poly).rest);
        }
        else {
            return;
        }
    }

    public static void main(String[] args) {
        Monomial deg1 = new Monomial(1, 2);
        Monomial deg3 = new Monomial(3, 4);
        Monomial deg5 = new Monomial(5, 1);
        ConsLoMonomial list1 = new ConsLoMonomial(deg1,
                new ConsLoMonomial(deg3, new ConsLoMonomial(deg5, new MtLoMonomial())));
        ConsLoMonomial list2 = new ConsLoMonomial(deg3,
                new ConsLoMonomial(deg1, new ConsLoMonomial(deg5, new MtLoMonomial())));
        Polynomial testPoly1 = new Polynomial(list1);
        Polynomial testPoly2 = new Polynomial(list2);

        System.out.println("Test Poly 1: ");
        printPoly(list1);
        System.out.println("Test Poly 2:");
        printPoly(list2);
        System.out.println("Testing");
        printPoly(UtilsPolynomial.filterZeroCoefficients(testPoly1.monomials));
        printPoly(UtilsPolynomial.filterZeroCoefficients(testPoly2.monomials));
        //printPoly(UtilsPolynomial.quickSortList(list2));
        System.out.println(testPoly1.equals(testPoly2));
    }
}