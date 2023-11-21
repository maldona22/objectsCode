
// represents a list of Person's buddies
interface ILoBuddy {

    ILoBuddy append(Person buddy);

    ILoBuddy append(ILoBuddy list);

    boolean contains(Person person);

    ILoBuddy filter(PersonPred pred);

    ILoBuddy removeDuplicates();

    ILoBuddy foldr(MTLoBuddy base, ExtendedBuddyCombinator combin);

    ILoBuddy foldr(MTLoBuddy base, PartyCountCombinator combin);

    DirectedWeightedGraph foldr(DirectedWeightedGraph base, ConvertToGraphCombinator combin);

    int length();

    boolean isEmpty();

    ILoAdjacencyList addEdges(ILoAdjacencyList graph, Person src);
}

interface ILoEdge {
    Edge get(Person dst);

    void copyStack(DirectedWeightedGraph graph, Stack copy);

    void findAllPaths(DirectedWeightedGraph graph, Person node, Person target);

    Edge pop(Stack stack);

    boolean isEmpty();

    boolean contains(Edge edge);

    ILoEdge append(ILoEdge edges);

    double foldr(double base, MultiplyWeightsCombinator combin);
}

interface ILoAdjacencyList {
    boolean contains(Person src);

    ConsLoAdjacencyList addNode(Person src);

    ILoAdjacencyList addEdge(Person src, Person dst);

    ILoAdjacencyList addEdges(Person src, ILoBuddy dsts);

    ILoAdjacencyList addEdgesCons(Person src, ConsLoBuddy dsts);

    ILoAdjacencyList addEdgesMt(Person src, MTLoBuddy dsts);

    Edge getEdge(Person src, Person dst);

    ILoEdge getAllEdges(Person src);
}

interface ILoDouble {
    double max();
    double maxHelper(double max);
}

interface ILoStack {
    ILoDouble map(ConvertListToLikelihoods func);

    int length();
}