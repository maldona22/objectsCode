// represents a list of Person's buddies
class ConsLoBuddy implements ILoBuddy {
    /* Template
     * 
     * Fields:
     * this.first -- Person
     * this.rest -- ILoBuddy
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
     * this.rest.append(Person buddy) -- ILoBuddy
     * this.rest.append(ILoBuddy list) -- ILoBuddy
     * this.rest.contains(Person person) -- boolean
     * this.rest.filter(PersonPred pred) -- ILoBuddy
     * this.rest.removeDuplicates() -- ILoBuddy
     * this.rest.foldr(MTLoBuddy base, ExtendedBuddyCombinator combin) -- ILoBuddy
     * this.rest.foldr(MTLoBuddy base, PartyCountCombinator combin) -- ILoBuddy
     * this.rest.foldr(DirectedWeightedGraph base, ConvertToGraphCombinator combin) -- DirectedWeightedGraph
     * this.rest.length() -- int
     * this.rest.isEmpty() -- boolean
     * this.rest.addEdges(ILoAdjacencyList graph, Person src) -- ILoAdjacencyList
     */

    Person first;
    ILoBuddy rest;

    ConsLoBuddy(Person first, ILoBuddy rest) {
        this.first = first;
        this.rest = rest;
    }

    // Adds the given person, buddy, to the end of this list
    public ILoBuddy append(Person buddy) {
        return new ConsLoBuddy(first, this.rest.append(buddy));
    };

    // Adds the given list of people, list, to the end of this list
    public ILoBuddy append(ILoBuddy list) {
        return new ConsLoBuddy(first, this.rest.append(list));
    }

    // Checks to see if person is contained in this list
    public boolean contains(Person person) {
        return this.first.equals(person) || this.rest.contains(person);
    }

    // Returns a new list with certain people removed depending upon the predicate
    // passed in
    public ILoBuddy filter(PersonPred pred) {
        if (pred.apply(first)) {
            return new ConsLoBuddy(first, rest.filter(pred));
        } else {
            return rest.filter(pred);
        }
    }

    // Removes any duplicates from this list
    public ILoBuddy removeDuplicates() {
        return new ConsLoBuddy(first, rest.filter(new DuplicatePersonPred(first)).removeDuplicates());
    }

    // Accumulates all of the extended buddies into a singular list
    public ILoBuddy foldr(MTLoBuddy base, ExtendedBuddyCombinator combin) {
        return combin.apply(first, this.rest.foldr(base, combin));
    }

    // Accumulates all of the extended buddies of every buddy into a singular list
    public ILoBuddy foldr(MTLoBuddy base, PartyCountCombinator combin) {
        return combin.apply(first, this.rest.foldr(base, combin));
    }

    // Accumulates all the people in this list into the graph in the form of nodes
    // and edges
    public DirectedWeightedGraph foldr(DirectedWeightedGraph base, ConvertToGraphCombinator combin) {
        return combin.apply(first, this.rest.foldr(base, combin));
    }

    // Returns the length of this list
    public int length() {
        return 1 + this.rest.length();
    }

    // Returns whether this list is empty
    // Since this is a ConsLoBuddy, it will always return false
    public boolean isEmpty() {
        return false;
    }
    
    // Double dispatch method
    // Adds all of the people within this list as destination nodes
    // for edges starting from src
    public ILoAdjacencyList addEdges(ILoAdjacencyList graph, Person src) {
        return graph.addEdgesCons(src, this);
    }
}

class ConsLoEdge implements ILoEdge {
    /* Template
     * 
     * Fields:
     * this.first -- Edge
     * this.rest -- ILoEdge
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
     * this.rest.get(Person dst) -- Edge
     * this.rest.copyStack(DirectedWeightedGraph graph, Stack copy) -- void
     * this.rest.findAllPaths(DirectedWeightedGraph graph, Person node, Person target) -- void
     * this.rest.pop(Stack stack) -- Edge
     * this.rest.isEmpty() -- boolean
     * this.rest.contains(Edge edge) -- boolean
     * this.rest.append(ILoEdge edges) -- ILoEdge
     * this.rest.foldr(double base, MultiplyWeightsCombinator combin) -- double
     */

    Edge first;
    ILoEdge rest;

    ConsLoEdge(Edge first, ILoEdge rest) {
        this.first = first;
        this.rest = rest;
    }

    // Returns the edge whose destination node is the same as the person dst
    public Edge get(Person dst) {
        if (this.first.dst.equals(dst)) {
            return this.first;
        } else {
            return this.rest.get(dst);
        }
    }

    // Double dispatch method
    // Copies this list into the new copy stack being made
    public void copyStack(DirectedWeightedGraph graph, Stack copy) {
        graph.copyStackCons(copy, this);
    }

    // Double dispatch method
    // Starts traversal through the graph using the edges held in this list
    public void findAllPaths(DirectedWeightedGraph graph, Person node, Person target) {
        graph.findAllPathsCons(node, this, target);
    }

    // Double dispatch method
    // Removes and returns the first element held in this list
    public Edge pop(Stack stack) {
        return stack.popCons(this);
    }

    // Checks to see if this list is empty or not
    // Since this is a ConsLoEdge, it will always return false
    public boolean isEmpty() {
        return false;
    }

    // Checks to see if this list contains the given edge
    public boolean contains(Edge edge) {
        return this.first.equals(edge) || this.rest.contains(edge);
    }

    // Adds the list of edges to the end of this list
    public ILoEdge append(ILoEdge edges) {
        return new ConsLoEdge(first, this.rest.append(edges));
    }

    // Accumulates the weights of the edges within this list into a single overall
    // likelihood
    public double foldr(double base, MultiplyWeightsCombinator combin) {
        return combin.apply(first, this.rest.foldr(base, combin));
    }
}

class ConsLoAdjacencyList implements ILoAdjacencyList {
    /* Template
     * 
     * Fields:
     * this.first -- AdjacencyList
     * this.rest -- ILoAdjacencyList
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
     * this.rest.contains(Person src) -- boolean
     * this.rest.addNode(Person src) -- ConsLoAdjacencyList
     * this.rest.addEdge(Person src, Person dst) -- ILoAdjacencyList
     * this.rest.addEdges(Person src, ILoBuddy dsts) -- ILoAdjacencyList
     * this.rest.addEdgesCons(Person src, ConsLoBuddy dsts) -- ILoAdjacencyList
     * this.rest.addEdgesMt(Person src, MTLoBuddy dsts) -- ILoAdjacencyList
     * this.rest.getEdge(Person src, Person dst) -- Edge
     * this.rest.getAllEdges(Person src) -- ILoEdge
     */

    AdjacencyList first;
    ILoAdjacencyList rest;

    ConsLoAdjacencyList(AdjacencyList first, ILoAdjacencyList rest) {
        this.first = first;
        this.rest = rest;
    }

    // Checks to see if this list contains the given person src
    public boolean contains(Person src) {
        return this.first.src.equals(src) || this.rest.contains(src);
    }

    // Adds a new adjacency list to this list using the given person src
    // As its source node
    public ConsLoAdjacencyList addNode(Person src) {
        return new ConsLoAdjacencyList(new AdjacencyList(src), this);
    }

    // Adds an edge between the given source and destination
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

    // Double dispatch method
    // There is nothing in the destinations list to add, so just return this
    public ILoAdjacencyList addEdgesMt(Person src, MTLoBuddy dsts) {
        return this;
    }

    // Double disatch method
    // Adds the first person in the list dsts as a new edge to this list
    public ILoAdjacencyList addEdgesCons(Person src, ConsLoBuddy dsts) {
        return this.addEdge(src, dsts.first)
                .addEdges(src, dsts.rest);
    }

    // Double dispatch method
    // Adds the people in the dsts as new edges using the given person src as
    // their source node
    public ILoAdjacencyList addEdges(Person src, ILoBuddy dsts) {
        return dsts.addEdges(this, src);
    }

    // Returns the edge between the given source and destination nodes
    public Edge getEdge(Person src, Person dst) {
        if (this.first.srcEquals(src)) {
            return this.first.dsts.get(dst);
        } else {
            return this.rest.getEdge(src, dst);
        }
    }

    // Returns all the edges connected to the given source node
    public ILoEdge getAllEdges(Person src) {
        if (this.first.srcEquals(src)) {
            return this.first.dsts;
        } else {
            return this.rest.getAllEdges(src);
        }
    }
}

class ConsLoDouble implements ILoDouble {
    /* Template
     * 
     * Fields:
     * this.first -- double
     * this.rest -- ILoDouble
     * 
     * Methods:
     * this.maxHelper(double max) -- double
     * this.max() -- double
     * 
     * Methods on fields:
     * this.rest.maxHelper(double max) -- double
     * this.rest.max() -- double
     */

    double first;
    ILoDouble rest;

    ConsLoDouble(double first, ILoDouble rest) {
        this.first = first;
        this.rest = rest;
    }

    // Compares the first element of this list and the max accumulator
    // Returns the largest of the two and compares that number with the rest of the list
    public double maxHelper(double max) {
        if (this.first > max) {
            return this.rest.maxHelper(this.first);
        }
        else {
            return this.rest.maxHelper(max);
        }
    }

    // Returns the maximum value held within this list
    public double max() {
        return maxHelper(this.first);
    }
}

class ConsLoStack implements ILoStack {
    /* Template
     * 
     * Fields:
     * this.first -- Stack
     * this.rest -- ILoStack
     * 
     * Methods:
     * this.map(ConvertListToLikelihoods func) -- ILoDouble
     * this.length() -- int
     * 
     * Methods on fields:
     * this.rest.map(ConvertListToLikelihoods func) -- ILoDouble
     * this.rest.length() -- int
     */

    Stack first;
    ILoStack rest;

    ConsLoStack(Stack first, ILoStack rest) {
        this.first = first;
        this.rest = rest;
    }

    // Converts all the paths within the list a list containing all of their respective
    // overall likelihoods
    public ILoDouble map(ConvertListToLikelihoods func) {
        return new ConsLoDouble(func.apply(this.first), this.rest.map(func));
    }

    // Returns the length of this list
    public int length() {
        return 1 + this.rest.length();
    }
}