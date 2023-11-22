
// represents a list of Person's buddies
interface ILoBuddy {
    // Adds the given person, buddy, to the end of this list
    ILoBuddy append(Person buddy);

    // Adds the given list of people, list, to the end of this list
    ILoBuddy append(ILoBuddy list);

    // Checks to see if person is contained in this list
    boolean contains(Person person);

    // Returns a new list with certain people removed depending upon the predicate
    // passed in
    ILoBuddy filter(PersonPred pred);

    // Removes any duplicates from this list
    ILoBuddy removeDuplicates();

    // Accumulates all of the extended buddies into a singular list
    ILoBuddy foldr(MTLoBuddy base, ExtendedBuddyCombinator combin);

    // Accumulates all of the extended buddies of every buddy into a singular list
    ILoBuddy foldr(MTLoBuddy base, PartyCountCombinator combin);

    // Accumulates all the people in this list into the graph in the form of nodes
    // and edges
    DirectedWeightedGraph foldr(DirectedWeightedGraph base, ConvertToGraphCombinator combin);

    // Returns the length of this list
    int length();

    // Returns whether this list is empty
    // Since this is a ConsLoBuddy, it will always return false
    boolean isEmpty();

    // Double dispatch method
    // Adds all of the people within this list as destination nodes
    // for edges starting from src
    ILoAdjacencyList addEdges(ILoAdjacencyList graph, Person src);
}

interface ILoEdge {
    // Returns the edge whose destination node is the same as the person dst
    Edge get(Person dst);

    // Double dispatch method
    // Copies this list into the new copy stack being made
    void copyStack(DirectedWeightedGraph graph, Stack copy);

    // Double dispatch method
    // Starts traversal through the graph using the edges held in this list
    void findAllPaths(DirectedWeightedGraph graph, Person node, Person target);

    // Double dispatch method
    // Removes and returns the first element held in this list
    Edge pop(Stack stack);

    // Checks to see if this list is empty or not
    // Since this is a ConsLoEdge, it will always return false
    boolean isEmpty();

    // Checks to see if this list contains the given edge
    boolean contains(Edge edge);

    // Adds the list of edges to the end of this list
    ILoEdge append(ILoEdge edges);

    // Accumulates the weights of the edges within this list into a single overall
    // likelihood
    double foldr(double base, MultiplyWeightsCombinator combin);
}

interface ILoAdjacencyList {
    // Checks to see if this list contains the given person src
    boolean contains(Person src);

    // Adds a new adjacency list to this list using the given person src
    // As its source node
    ConsLoAdjacencyList addNode(Person src);

    // Adds an edge between the given source and destination
    ILoAdjacencyList addEdge(Person src, Person dst);

    // Double dispatch method
    // Adds the people in the dsts as new edges using the given person src as
    // their source node
    ILoAdjacencyList addEdges(Person src, ILoBuddy dsts);

    // Double disatch method
    // Adds the first person in the list dsts as a new edge to this list
    ILoAdjacencyList addEdgesCons(Person src, ConsLoBuddy dsts);

    // Double dispatch method
    // There is nothing in the destinations list to add, so just return this
    ILoAdjacencyList addEdgesMt(Person src, MTLoBuddy dsts);

    // Returns the edge between the given source and destination nodes
    Edge getEdge(Person src, Person dst);
    
    // Returns all the edges connected to the given source node
    ILoEdge getAllEdges(Person src);
}

interface ILoDouble {
    // Compares the first element of this list and the max accumulator
    // Returns the largest of the two and compares that number with the rest of the list
    double max();

    // Returns the maximum value held within this list
    double maxHelper(double max);
}

interface ILoStack {
    // Converts all the paths within the list a list containing all of their respective
    // overall likelihoods
    ILoDouble map(ConvertListToLikelihoods func);

    // Returns the length of this list
    int length();
}