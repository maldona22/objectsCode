
// represents an empty list of Person's buddies
class MTLoBuddy implements ILoBuddy {

    public boolean contains(Person person) {
        return false;
    }

    public ILoBuddy removeDuplicates() {
        return new MTLoBuddy();
    }

    public ILoBuddy filter(PersonPred pred) {
        return new MTLoBuddy();
    }

    public ILoBuddy append(ILoBuddy list) {
        return list;
    }

    public ILoBuddy append(Person person) {
        return new ConsLoBuddy(person, new MTLoBuddy());
    }

    public ILoBuddy foldr(MTLoBuddy base, ExtendedBuddyCombinator combin) {
        return base;
    }

    public ILoBuddy foldr(MTLoBuddy base, PartyCountCombinator combin) {
        return base;
    }

    public DirectedWeightedGraph foldr(DirectedWeightedGraph base, ConvertToGraphCombinator combin) {
        return base;
    }

    public int length() {
        return 0;
    }

    public boolean isEmpty() {
        return true;
    }

    public ILoAdjacencyList addEdges(ILoAdjacencyList graph, Person src) {
        return graph.addEdgesMt(src, this);
    }
}

class MtLoEdge implements ILoEdge {
    public Edge get(Person dst) {
        throw new IllegalArgumentException();
    }

    public void copyStack(DirectedWeightedGraph graph, Stack copy) {
        graph.copyStackMt(copy, this);
    }

    public void findAllPaths(DirectedWeightedGraph graph, Person node, Person target) {
        graph.findAllPathsMt(node, this, target);
    }

    public Edge pop(Stack stack) {
        return stack.popMt(this);
    }

    public boolean isEmpty() {
        return true;
    }

    public boolean contains(Edge edge) {
        return false;
    }

    public ILoEdge append(ILoEdge edges) {
        return edges;
    }

    public double foldr(double base, MultiplyWeightsCombinator combin) {
        return base;
    }
}

class MtLoAdjacencyList implements ILoAdjacencyList {

    public boolean contains(Person src) {
        return false;
    }

    public ConsLoAdjacencyList addNode(Person src) {
        return new ConsLoAdjacencyList(new AdjacencyList(src), this);
    }

    public AdjacencyList get(Person src) {
        throw new IllegalArgumentException();
    }

    public ILoAdjacencyList addEdge(Person src, Person dst) {
        return new MtLoAdjacencyList();
    }

    public ILoAdjacencyList addEdges(Person src, ILoBuddy dsts) {
        return new MtLoAdjacencyList();
    }

    public ILoAdjacencyList addEdgesCons(Person src, ConsLoBuddy dsts) {
        return new MtLoAdjacencyList();
    }

    public ILoAdjacencyList addEdgesMt(Person src, MTLoBuddy dsts) {
        return new MtLoAdjacencyList();
    }

    public Edge getEdge(Person src, Person dst) {
        throw new IllegalArgumentException();
    }

    public ILoEdge getAllEdges(Person src) {
        throw new IllegalArgumentException();
    }
}

class MtLoDouble implements ILoDouble {
    public double maxHelper(double max) {
        return max;
    }

    public double max() {
        throw new IllegalAccessError();
    }
}

class MtLoStack implements ILoStack {
    public ILoDouble map(ConvertListToWeights func) {
        return new MtLoDouble();
    }
}