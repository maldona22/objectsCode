class Utils {
    ILoMonomial filterSmaller(int degree, ILoMonomial list) {
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

    ILoMonomial filterLarger(int degree, ILoMonomial list) {
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
    
    ILoMonomial quickSortList(ILoMonomial list) {
        if (list instanceof MtLoMonomial) {
            return new MtLoMonomial();
        }
        else {
            new smallerList = 
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
}

public class Polynomial {
    
}