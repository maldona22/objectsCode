
// represents an empty list of Person's buddies
public class MTLoBuddy implements ILoBuddy {
    /* Template
     * 
     * Fields:
     * 
     * Methods:
     * this.append(Person buddy) -- ILoBuddy
     * this.append(ILoBuddy list) -- ILoBuddy
     * this.contains(Person person) -- boolean
     * this.filter(PersonPred pred) -- ILoBuddy
     * this.removeDuplicates() -- ILoBuddy
     * this.foldr(MTLoBuddy base, ExtendedBuddyCombinator combin) -- ILoBuddy
     * this.foldr(MTLoBuddy base, PartyCountCombinator combin) -- ILoBuddy
     * this.foldr(DirectedWeightedGraph base, ConvertToGraphCombinator combin) -- DirectedWeightedGraph
     * this.length() -- int
     * this.isEmpty() -- boolean
     * this.addEdges(ILoAdjacencyList graph, Person src) -- ILoAdjacencyList
     * 
     * Methods on fields:
     * 
     */

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
    /* Template
     * 
     * Fields:
     * 
     * Methods:
     * this.get(Person dst) -- Edge
     * this.copyStack(DirectedWeightedGraph graph, Stack copy) -- void
     * this.findAllPaths(DirectedWeightedGraph graph, Person node, Person target) -- void
     * this.pop(Stack stack) -- Edge
     * this.isEmpty() -- boolean
     * this.contains(Edge edge) -- boolean
     * this.append(ILoEdge edges) -- ILoEdge
     * this.foldr(double base, MultiplyWeightsCombinator combin) -- double
     * 
     * Methods on fields:
     * 
     */

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
    /* Template
     * 
     * Fields:
     * 
     * Methods:
     * this.contains(Person src) -- boolean
     * this.addNode(Person src) -- ConsLoAdjacencyList
     * this.addEdge(Person src, Person dst) -- ILoAdjacencyList
     * this.addEdges(Person src, ILoBuddy dsts) -- ILoAdjacencyList
     * this.addEdgesCons(Person src, ConsLoBuddy dsts) -- ILoAdjacencyList
     * this.addEdgesMt(Person src, MTLoBuddy dsts) -- ILoAdjacencyList
     * this.getEdge(Person src, Person dst) -- Edge
     * this.getAllEdges(Person src) -- ILoEdge
     * 
     * Methods on fields:
     * 
     */

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
        return dsts.addEdges(this, src);
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
        return new MtLoEdge();
    }
}

class MtLoDouble implements ILoDouble {
    /* Template
     * 
     * Fields:
     * 
     * Methods:
     * this.maxHelper(double max) -- double
     * this.max() -- double
     * 
     * Methods on fields:
     * 
     */

    public double maxHelper(double max) {
        return max;
    }

    public double max() {
        throw new IllegalArgumentException();
    }
}

class MtLoStack implements ILoStack {
    /* Template
     * 
     * Fields:
     * 
     * Methods:
     * this.map(ConvertListToLikelihoods func) -- ILoDouble
     * this.length() -- int
     * 
     * Methods on fields:
     * 
     */

    public ILoDouble map(ConvertListToLikelihoods func) {
        return new MtLoDouble();
    }

    public int length() {
        return 0;
    }
}