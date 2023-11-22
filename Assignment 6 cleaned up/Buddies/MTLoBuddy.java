
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

    // Checks to see if person is contained in this list
    public boolean contains(Person person) {
        return false;
    }

    // Removes any duplicates from this list
    public ILoBuddy removeDuplicates() {
        return new MTLoBuddy();
    }

    // Returns a new list with certain people removed depending upon the predicate
    // passed in
    public ILoBuddy filter(PersonPred pred) {
        return new MTLoBuddy();
    }

    // Adds the given list of people, list, to the end of this list
    public ILoBuddy append(ILoBuddy list) {
        return list;
    }

    // Adds the given person, buddy, to the end of this list
    public ILoBuddy append(Person person) {
        return new ConsLoBuddy(person, new MTLoBuddy());
    }

    // Accumulates all of the extended buddies into a singular list
    public ILoBuddy foldr(MTLoBuddy base, ExtendedBuddyCombinator combin) {
        return base;
    }

    // Accumulates all of the extended buddies of every buddy into a singular list
    public ILoBuddy foldr(MTLoBuddy base, PartyCountCombinator combin) {
        return base;
    }

    // Accumulates all the people in this list into the graph in the form of nodes
    // and edges
    public DirectedWeightedGraph foldr(DirectedWeightedGraph base, ConvertToGraphCombinator combin) {
        return base;
    }

    // Returns the length of this list
    public int length() {
        return 0;
    }

    // Returns whether this list is empty
    // Since this is a MtLoBuddy, it will always return true
    public boolean isEmpty() {
        return true;
    }

     // Double dispatch method
    // Adds all of the people within this list as destination nodes
    // for edges starting from src
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

    // Returns the edge whose destination node is the same as the person dst
    // Since there are none in this, throws an exception
    public Edge get(Person dst) {
        throw new IllegalArgumentException();
    }

    // Double dispatch method
    // Copies this list into the new copy stack being made
    public void copyStack(DirectedWeightedGraph graph, Stack copy) {
        graph.copyStackMt(copy, this);
    }

    // Double dispatch method
    // Starts traversal through the graph using the edges held in this list
    public void findAllPaths(DirectedWeightedGraph graph, Person node, Person target) {
        graph.findAllPathsMt(node, this, target);
    }

    // Double dispatch method
    // Removes and returns the first element held in this list
    public Edge pop(Stack stack) {
        return stack.popMt(this);
    }

    // Checks to see if this list is empty or not
    // Since this is a MtLoEdge, it will always return true
    public boolean isEmpty() {
        return true;
    }

    // Checks to see if this list contains the given edge
    public boolean contains(Edge edge) {
        return false;
    }

    // Adds the list of edges to the end of this list
    public ILoEdge append(ILoEdge edges) {
        return edges;
    }

    // Accumulates the weights of the edges within this list into a single overall
    // likelihood
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

    // Checks to see if this list contains the given person src
    public boolean contains(Person src) {
        return false;
    }

    // Adds a new adjacency list to this list using the given person src
    // As its source node
    public ConsLoAdjacencyList addNode(Person src) {
        return new ConsLoAdjacencyList(new AdjacencyList(src), this);
    }

    
    public AdjacencyList get(Person src) {
        throw new IllegalArgumentException();
    }

    // Adds an edge between the given source and destination
    public ILoAdjacencyList addEdge(Person src, Person dst) {
        return new MtLoAdjacencyList();
    }

    // Double dispatch method
    // Adds the people in the dsts as new edges using the given person src as
    // their source node
    public ILoAdjacencyList addEdges(Person src, ILoBuddy dsts) {
        return dsts.addEdges(this, src);
    }

    // Double disatch method
    // Adds the first person in the list dsts as a new edge to this list
    public ILoAdjacencyList addEdgesCons(Person src, ConsLoBuddy dsts) {
        return new MtLoAdjacencyList();
    }

    // Double dispatch method
    // There is nothing in the destinations list to add, so just return this
    public ILoAdjacencyList addEdgesMt(Person src, MTLoBuddy dsts) {
        return new MtLoAdjacencyList();
    }

    // Returns the edge between the given source and destination nodes
    // Since there are no edges to return, throws an exception
    public Edge getEdge(Person src, Person dst) {
        throw new IllegalArgumentException();
    }

    // Returns all the edges connected to the given source node
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

    // Compares the first element of this list and the max accumulator
    // Returns the largest of the two and compares that number with the rest of the list
    // Since this list is empty, returns the final return value, the maximum number accumulator
    public double maxHelper(double max) {
        return max;
    }

    // Since this is an empty list, there is nothing to compare
    // Hence, this throws an exception
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

    // Converts all the paths within the list a list containing all of their respective
    // overall likelihoods
    public ILoDouble map(ConvertListToLikelihoods func) {
        return new MtLoDouble();
    }

    // Returns the length of this list
    public int length() {
        return 0;
    }
}