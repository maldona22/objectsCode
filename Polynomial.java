class Utils {
    private static ILoMonomial append(ILoMonomial left, ILoMonomial right) {
        if (left instanceof MtLoMonomial) {
            return right;
        } else {
            return new ConsLoMonomial(((ConsLoMonomial) left).first, append(((ConsLoMonomial) left).rest, right));
        }
    }

    private static ILoMonomial filterSmaller(int degree, ILoMonomial list) {
        if (list instanceof MtLoMonomial) {
            return new MtLoMonomial();
        }
        if (degree <= ((ConsLoMonomial) list).first.degree) {
            return new ConsLoMonomial(((ConsLoMonomial) list).first,
                    filterSmaller(degree, ((ConsLoMonomial) list).rest));
        } else {
            return filterSmaller(degree, ((ConsLoMonomial) list).rest);
        }
    }

    private static ILoMonomial filterLarger(int degree, ILoMonomial list) {
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

    private static boolean checkForDuplicateDegrees(int degree, ILoMonomial list) {
        if (list instanceof MtLoMonomial) {
            return true;
        } else {
            if (degree == ((ConsLoMonomial) list).first.degree) {
                return checkForDuplicateDegrees(degree, ((ConsLoMonomial) list).rest);
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

}

class MtLoMonomial implements ILoMonomial {

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
            if (this.first.equals(((ConsLoMonomial)other).first)) {
                if ( (((ConsLoMonomial)other).rest instanceof ConsLoMonomial) && 
                    (this.rest instanceof ConsLoMonomial)) {
                    return this.rest.equals(((ConsLoMonomial)other).rest);
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
}

public class Polynomial {
    ILoMonomial monomials;
    
    Polynomial(ILoMonomial monomials) {
        ILoMonomial sortedMonomials = Utils.quickSortList(monomials);
        if (Utils.containsNoDuplicates(sortedMonomials)) {
            this.monomials = sortedMonomials;
        } else {
            // TODO come up with error message for this
            throw new IllegalArgumentException();
        }
    }
    
    boolean equals(Polynomial other) {
        ILoMonomial thisFiltered = Utils.filterZeroCoefficients(this.monomials);
        ILoMonomial otherFiltered = Utils.filterZeroCoefficients(other.monomials);

        return thisFiltered.equals(otherFiltered);
    }
}