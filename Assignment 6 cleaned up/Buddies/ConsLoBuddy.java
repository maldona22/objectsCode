// represents a list of Person's buddies
class ConsLoBuddy implements ILoBuddy {

    Person first;
    ILoBuddy rest;

    ConsLoBuddy(Person first, ILoBuddy rest) {
        this.first = first;
        this.rest = rest;
    }

    public ILoBuddy append(Person buddy) {
        return new ConsLoBuddy(first, this.rest.append(buddy));
    };

    public ILoBuddy append(ILoBuddy list) {
        return new ConsLoBuddy(first, this.rest.append(list));
    }

    public boolean contains(Person person) {
        return this.first.equals(person) || this.rest.contains(person);
    }

    public ILoBuddy filter(PersonPred pred) {
        if (pred.apply(first)) {
            return new ConsLoBuddy(first, rest.filter(pred));
        } else {
            return rest.filter(pred);
        }
    }

    public ILoBuddy removeDuplicates() {
        return new ConsLoBuddy(first, rest.filter(new DuplicatePersonPred(first)).removeDuplicates());
    }

    public ILoBuddy foldr(MTLoBuddy base, ExtendedBuddyCombinator combin) {
        return combin.apply(first, this.rest.foldr(base, combin));
    }

    public ILoBuddy foldr(MTLoBuddy base, PartyCountCombinator combin) {
        return combin.apply(first, this.rest.foldr(base, combin));
    }

    public DirectedWeightedGraph foldr(DirectedWeightedGraph base, ConvertToGraphCombinator combin) {
        return combin.apply(first, this.rest.foldr(base, combin));
    }

    public int length() {
        return 1 + this.rest.length();
    }

    public boolean isEmpty() {
        return false;
    }
    
    public ILoAdjacencyList addEdges(ILoAdjacencyList graph, Person src) {
        return graph.addEdgesCons(src, this);
    }
}

class ConsLoEdge implements ILoEdge {
    Edge first;
    ILoEdge rest;

    ConsLoEdge(Edge first, ILoEdge rest) {
        this.first = first;
        this.rest = rest;
    }

    public Edge get(Person dst) {
        if (this.first.dst.equals(dst)) {
            return this.first;
        } else {
            return this.rest.get(dst);
        }
    }

    public void copyStack(DirectedWeightedGraph graph, Stack copy) {
        graph.copyStackCons(copy, this);
    }

    public void findAllPaths(DirectedWeightedGraph graph, Person node, Person target) {
        graph.findAllPathsCons(node, this, target);
    }

    public Edge pop(Stack stack) {
        return stack.popCons(this);
    }

    public boolean isEmpty() {
        return false;
    }

    public boolean contains(Edge edge) {
        return this.first.equals(edge) || this.rest.contains(edge);
    }

    public ILoEdge append(ILoEdge edges) {
        return new ConsLoEdge(first, this.rest.append(edges));
    }

    public double foldr(double base, MultiplyWeightsCombinator combin) {
        return combin.apply(first, this.rest.foldr(base, combin));
    }
}

class ConsLoAdjacencyList implements ILoAdjacencyList {
    AdjacencyList first;
    ILoAdjacencyList rest;

    ConsLoAdjacencyList(AdjacencyList first, ILoAdjacencyList rest) {
        this.first = first;
        this.rest = rest;
    }

    public boolean contains(Person src) {
        return this.first.src.equals(src) || this.rest.contains(src);
    }

    public ConsLoAdjacencyList addNode(Person src) {
        return new ConsLoAdjacencyList(new AdjacencyList(src), this);
    }

    public ILoAdjacencyList addEdge(Person src, Person dst) {
        if (this.first.srcEquals(src)) {
            double weight = src.diction * dst.hearing;
            return new ConsLoAdjacencyList(
                    new AdjacencyList(src, new ConsLoEdge(new Edge(dst, weight), this.first.dsts)),
                    this.rest.addEdge(src, dst));
        } else {
            return new ConsLoAdjacencyList(this.first, this.rest.addEdge(src, dst));
        }
    }

    public ILoAdjacencyList addEdgesMt(Person src, MTLoBuddy dsts) {
        return this;
    }

    public ILoAdjacencyList addEdgesCons(Person src, ConsLoBuddy dsts) {
        return this.addEdge(src, dsts.first)
                .addEdges(src, dsts.rest);
    }

    public ILoAdjacencyList addEdges(Person src, ILoBuddy dsts) {
        return dsts.addEdges(this, src);
    }

    public Edge getEdge(Person src, Person dst) {
        if (this.first.srcEquals(src)) {
            return this.first.dsts.get(dst);
        } else {
            return this.rest.getEdge(src, dst);
        }
    }

    public ILoEdge getAllEdges(Person src) {
        if (this.first.srcEquals(src)) {
            return this.first.dsts;
        } else {
            return this.rest.getAllEdges(src);
        }
    }
}

class ConsLoDouble implements ILoDouble{
    double first;
    ILoDouble rest;

    ConsLoDouble(double first, ILoDouble rest) {
        this.first = first;
        this.rest = rest;
    }

    public double maxHelper(double max) {
        if (this.first > max) {
            return this.rest.maxHelper(this.first);
        }
        else {
            return this.rest.maxHelper(max);
        }
    }

    public double max() {
        return maxHelper(this.first);
    }
}

class ConsLoStack implements ILoStack {
    Stack first;
    ILoStack rest;

    ConsLoStack(Stack first, ILoStack rest) {
        this.first = first;
        this.rest = rest;
    }

    public ILoDouble map(ConvertListToWeights func) {
        return new ConsLoDouble(func.apply(this.first), this.rest.map(func));
    }
}