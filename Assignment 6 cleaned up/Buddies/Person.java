// -------------------------------- Predicates --------------------------------

// Template for making predicates for use in higher order functions
interface PersonPred {
    public boolean apply(Person person);
}

// Predicate that removes a given person from a list of people
class DuplicatePersonPred implements PersonPred {
    /* Template
     * 
     * Fields:
     * this.person -- Person
     * 
     * Methods:
     * this.apply(Person person) -- boolean
     * 
     * Methods on fields:
     * this.person.addBuddy(Person buddy) -- void
     * this.person.hasDirectBuddy(Person that) -- boolean
     * this.person.allPartyInvites() -- ILoBuddy
     * this.person.partyCount() -- int
     * this.person.countCommonBuddies(Person that) -- int
     * this.person.hasExtendedBuddy(Person that) -- boolean
     * this.person.maxLikelihood(Person that) -- double
     */

    Person person;

    // Checks to see if the passed person is the same as this.person
    public boolean apply(Person person) {
        return !this.person.equals(person);
    }

    DuplicatePersonPred(Person person) {
        this.person = person;
    }
}

// Predicate that removes a given list of people from another list of people
class DuplicatePeoplePred implements PersonPred {
    /* Template
     * 
     * Fields:
     * this.people -- ILoBuddy
     * 
     * Methods:
     * this.apply(Person person) -- boolean
     * 
     * Methods on fields:
     * this.people.append(Person buddy) -- ILoBuddy
     * this.people.append(ILoBuddy list) -- ILoBuddy
     * this.people.contains(Person person) -- boolean
     * this.people.filter(PersonPred pred) -- ILoBuddy
     * this.people.removeDuplicates() -- ILoBuddy
     * this.people.foldr(MTLoBuddy base, ExtendedBuddyCombinator combin) -- ILoBuddy
     * this.people.foldr(MTLoBuddy base, PartyCountCombinator combin) -- ILoBuddy
     * this.people.foldr(DirectedWeightedGraph base, ConvertToGraphCombinator combin) -- DirectedWeightedGraph
     * this.people.length() -- int
     * this.people.isEmpty() -- boolean
     * this.people.addEdges(ILoAdjacencyList graph, Person src) -- ILoAdjacencyList
     */

    ILoBuddy people;

    // Checks to see if the passed person is the same as anyf of the people
    // in this.people
    public boolean apply(Person person) {
        return !this.people.contains(person);
    }

    DuplicatePeoplePred(ILoBuddy people) {
        this.people = people;
    }
}

// Predicate that removes everything but a given list of people from a list,
// "extracting" them from the list
class ExtractDuplicatePeoplePred implements PersonPred {
    /* Template
     * 
     * Fields:
     * this.people -- ILoBuddy
     * 
     * Methods:
     * this.apply(Person person) -- boolean
     * 
     * Methods on fields:
     * this.people.append(Person buddy) -- ILoBuddy
     * this.people.append(ILoBuddy list) -- ILoBuddy
     * this.people.contains(Person person) -- boolean
     * this.people.filter(PersonPred pred) -- ILoBuddy
     * this.people.removeDuplicates() -- ILoBuddy
     * this.people.foldr(MTLoBuddy base, ExtendedBuddyCombinator combin) -- ILoBuddy
     * this.people.foldr(MTLoBuddy base, PartyCountCombinator combin) -- ILoBuddy
     * this.people.foldr(DirectedWeightedGraph base, ConvertToGraphCombinator combin) -- DirectedWeightedGraph
     * this.people.length() -- int
     * this.people.isEmpty() -- boolean
     * this.people.addEdges(ILoAdjacencyList graph, Person src) -- ILoAdjacencyList
     */

    ILoBuddy people;

    // Checks to see if the passed person is the same as anyf of the people
    // in this.people
    public boolean apply(Person person) {
        return people.contains(person);
    }

    ExtractDuplicatePeoplePred(ILoBuddy people) {
        this.people = people;
    }
}

// -------------------------------- Combinators --------------------------------

// Gets the extended friend list of a person
class ExtendedBuddyCombinator {
    /* Template
     * 
     * Fields:
     * this.visited -- ILoBuddy
     * 
     * Methods:
     * this.apply(Person person, ILoBuddy accum) -- ILoBuddy
     * 
     * Methods on fields:
     * this.visited.append(Person buddy) -- ILoBuddy
     * this.visited.append(ILoBuddy list) -- ILoBuddy
     * this.visited.contains(Person person) -- boolean
     * this.visited.filter(PersonPred pred) -- ILoBuddy
     * this.visited.removeDuplicates() -- ILoBuddy
     * this.visited.foldr(MTLoBuddy base, ExtendedBuddyCombinator combin) -- ILoBuddy
     * this.visited.foldr(MTLoBuddy base, PartyCountCombinator combin) -- ILoBuddy
     * this.visited.foldr(DirectedWeightedGraph base, ConvertToGraphCombinator combin) -- DirectedWeightedGraph
     * this.visited.length() -- int
     * this.visited.isEmpty() -- boolean
     * this.visited.addEdges(ILoAdjacencyList graph, Person src) -- ILoAdjacencyList
     */

    ILoBuddy visited;

    // Recursively combines the buddies of person with their buddies
    // to create an extended buddies list for person
    // EFFECT: Mutates this.visited so as to keep track of who has already been processed
    // thereby preventing cycles and infinite recursion from happening
    public ILoBuddy apply(Person person, ILoBuddy accum) {
        if (person.buddies.filter(new DuplicatePeoplePred(visited)).isEmpty()) {
            visited = visited.append(person);
            return accum.append(person);
        } else {
            visited = visited.append(person);
            return accum.append(person)
                        .append(person.buddies.filter(new DuplicatePeoplePred(visited))
                        .foldr(new MTLoBuddy(), this));
        }
    }
    
    ExtendedBuddyCombinator() {
        this.visited = new MTLoBuddy();
    }
}

class PartyCountCombinator {
    /* Template
     * 
     * Fields:
     * 
     * Methods:
     * this.apply(Person person, ILoBuddy accum) -- ILoBuddy
     * 
     * Methods on fields:
     * 
     */

    // Combines the extended buddy trees of each person in this.person.buddies list
    public ILoBuddy apply(Person person, ILoBuddy accum) {
        return accum.append(person)
                    .append(person.buddies.foldr(new MTLoBuddy(), new ExtendedBuddyCombinator()));
    }
}

class ConvertToGraphCombinator {
    /* Template
     * 
     * Fields:
     * 
     * Methods:
     * this.apply(Person person, DirectedWeightedGraph accum) -- DirectedWeightedGraph
     * 
     * Methods on fields:
     * 
     */

     // Adds the person and their buddies as nodes and edges into accum
    public DirectedWeightedGraph apply(Person person, DirectedWeightedGraph accum) {
        return accum.addNode(person)
                    .addEdges(person, person.buddies)
                    ;
    }
}

class MultiplyWeightsCombinator {
    /* Template
     * 
     * Fields:
     * 
     * Methods:
     * this.apply(Edge edge, double accum) -- double
     * 
     * Methods on fields:
     * 
     */

     // Used to combine all the weights in a path to a target node
    public double apply(Edge edge, double accum) {
        return edge.weight * accum;
    }
}

// -------------------------------- IFuncs --------------------------------

class ConvertListToLikelihoods {
    /* Template
     * 
     * Fields:
     * 
     * Methods:
     * this.apply(Stack stack) -- double
     * 
     * Methods on fields:
     * 
     */

    // Used to convert a list of paths stored as stacks into their respective overall
    // likelihood value
    double apply(Stack stack) {
        return stack.stack.foldr(1, new MultiplyWeightsCombinator());
    }
}

// -------------------------------- Objects --------------------------------

class Edge {
    /* Template
     * 
     * Fields:
     * this.dst -- Person
     * this.weight -- double
     * 
     * Methods:
     * this.dstEquals(Person dst) -- boolean
     * 
     * Methods on fields:
     * this.dst.addBuddy(Person buddy) -- void
     * this.dst.hasDirectBuddy(Person that) -- boolean
     * this.dst.allPartyInvites() -- ILoBuddy
     * this.dst.partyCount() -- int
     * this.dst.countCommonBuddies(Person that) -- int
     * this.dst.hasExtendedBuddy(Person that) -- boolean
     * this.dst.maxLikelihood(Person that) -- double
     */

    Person dst;
    double weight;

    Edge(Person dst, double weight) {
        this.dst = dst;
        this.weight = weight;
    }

    // Checks to see if a given person is the same as the destination node of this
    // edge
    boolean dstEquals(Person dst) {
        return this.dst.equals(dst);
    }
}

class AdjacencyList {
    /* Template
     * 
     * Fields:
     * this.src -- Person
     * this.dsts -- ILoEdge
     * 
     * Methods:
     * this.srcEquals(Person src) -- boolean
     * 
     * Methods on fields:
     * this.src.addBuddy(Person buddy) -- void
     * this.src.hasDirectBuddy(Person that) -- boolean
     * this.src.allPartyInvites() -- ILoBuddy
     * this.src.partyCount() -- int
     * this.src.countCommonBuddies(Person that) -- int
     * this.src.hasExtendedBuddy(Person that) -- boolean
     * this.src.maxLikelihood(Person that) -- double
     * 
     * this.dsts.get(Person dst) -- Edge
     * this.dsts.copyStack(DirectedWeightedGraph graph, Stack copy) -- void
     * this.dsts.findAllPaths(DirectedWeightedGraph graph, Person node, Person target) -- void
     * this.dsts.pop(Stack stack) -- Edge
     * this.dsts.isEmpty() -- boolean
     * this.dsts.contains(Edge edge) -- boolean
     * this.dsts.append(ILoEdge edges) -- ILoEdge
     * this.dsts.foldr(double base, MultiplyWeightsCombinator combin) -- double
     */

    Person src;
    ILoEdge dsts;

    AdjacencyList(Person src) {
        this.src = src;
        this.dsts = new MtLoEdge();
    }

    AdjacencyList(Person src, ILoEdge dsts) {
        this.src = src;
        this.dsts = dsts;
    }

    // Checks to see if the given person is the same as the source / starting node of this
    // Adjacency list
    boolean srcEquals(Person src) {
        return this.src.equals(src);
    }
}

class Stack {
    /* Template
     * 
     * Fields:
     * this.stack -- ILoEdge
     * 
     * Methods:
     * this.isEmpty() -- boolean
     * this.contains(Edge edge) -- boolean
     * this.push(Edge edge) -- void
     * this.push(ILoEdge edges) -- void
     * this.pop() -- Edge
     * this.popCons() -- Edge
     * this.popMt() -- Edge
     * 
     * Methods on fields:
     * this.stack.get(Person dst) -- Edge
     * this.stack.copyStack(DirectedWeightedGraph graph, Stack copy) -- void
     * this.stack.findAllPaths(DirectedWeightedGraph graph, Person node, Person target) -- void
     * this.stack.pop(Stack stack) -- Edge
     * this.stack.isEmpty() -- boolean
     * this.stack.contains(Edge edge) -- boolean
     * this.stack.append(ILoEdge edges) -- ILoEdge
     * this.stack.foldr(double base, MultiplyWeightsCombinator combin) -- double
     */
    ILoEdge stack;

    Stack() {
        this.stack = new MtLoEdge();
    }

    // Checks to see if the stack is empty or not
    public boolean isEmpty() {
        return stack.isEmpty();
    }

    // Checks to see if the given edge is currently contained with the this stack
    public boolean contains(Edge edge) {
        return stack.contains(edge);
    }

    // Pushes the given edge on top of the stack
    public void push(Edge edge) {
        stack = new ConsLoEdge(edge, stack);
    }

    // Pushes the given edges on top of the stack
    public void push(ILoEdge edges) {
        stack = edges.append(stack);
    }

    // double dispatch method to ultimately remove edge from top of stack
    public Edge pop() {
        return stack.pop(this);
    }

    // Removes and returns edge from the top of the stack
    public Edge popCons(ConsLoEdge stack) {
        this.stack = stack.rest;
        return stack.first;
    }

    // Throws an error since a user can't return a value that never existed to begin with
    public Edge popMt(MtLoEdge stack) {
        throw new IllegalArgumentException();
    }
}

// Data structure representing the buddies data in the form of a directed
// weighted cyclic graph
class DirectedWeightedGraph {
    /* Template
     * 
     * Fields:
     * this.graph -- ILoAdjacencyList
     * this.connectionPath -- Stack
     * this.connectionPaths -- ILoStack
     * 
     * Methods:
     * this.contans(Person src) -- boolean
     * this.addNode(Person src) -- DirectedWeightedGraph
     * this.addEdge(Person src, Person dst) -- DirectedWeightedGraph
     * this.addEdges(Person src, ILoBuddy dsts) -- DirectedWeightedGraph
     * this.getEdge(Person src, Person dst) -- Edge
     * this.getAllEdges(Person src) -- ILoEdge
     * this.copyStackMt(Stack copy, MtLoEdge original) -- void
     * this.copyStackCons(Stack copy, ConsLoEdge original) -- void
     * this.copyStack(Stack copy, ILoEdge original) -- void
     * this.findAllPathsMt(Person node, MtLoEdge nodeSubTrees, Person targetNode) -- void
     * this.findAllPathsCons(Person node, ConsLoEdge connectedNodes, Person targetNode) -- void
     * this.findAllPathsHelper(Person node, ILoEdge connectedNodes, Person targetNode) -- void
     * this.findAllPaths(Person node, Person targetNode) -- ILoStack
     * 
     * Methods on fields:
     * this.graph.contains(Person src) -- boolean
     * this.graph.addNode(Person src) -- ConsLoAdjacencyList
     * this.graph.addEdge(Person src, Person dst) -- ILoAdjacencyList
     * this.graph.addEdges(Person src, ILoBuddy dsts) -- ILoAdjacencyList
     * this.graph.addEdgesCons(Person src, ConsLoBuddy dsts) -- ILoAdjacencyList
     * this.graph.addEdgesMt(Person src, MTLoBuddy dsts) -- ILoAdjacencyList
     * this.graph.getEdge(Person src, Person dst) -- Edge
     * this.graph.getAllEdges(Person src) -- ILoEdge
     * 
     * this.connectionPath.isEmpty() -- boolean
     * this.connectionPath.contains(Edge edge) -- boolean
     * this.connectionPath.push(Edge edge) -- void
     * this.connectionPath.push(ILoEdge edges) -- void
     * this.connectionPath.pop() -- Edge
     * this.connectionPath.popCons() -- Edge
     * this.connectionPath.popMt() -- Edge
     * 
     * this.connectionPaths.map(ConvertListToLikelihoods func) -- ILoDouble
     * this.connectionPaths.length() -- int
     */

    ILoAdjacencyList graph;
    Stack connectionPath;
    ILoStack connectionPaths;

    DirectedWeightedGraph() {
        this.graph = new MtLoAdjacencyList();
    }

    DirectedWeightedGraph(ILoAdjacencyList graph, Stack connectionPath, ILoStack connectionPaths) {
        this.graph = graph;
        this.connectionPath = connectionPath;
        this.connectionPaths = connectionPaths;
    }

    // Checks to see if the person is contained in any of the nodes in this.graph
    public boolean contains(Person src) {
        return graph.contains(src);
    }

    // Returns a new graph containing a new node containing the person src
    public DirectedWeightedGraph addNode(Person src) {
        return new DirectedWeightedGraph(graph.addNode(src), this.connectionPath, this.connectionPaths);
    }

    // Returns a new graph that contains a new edge between person (node) src and person (node) dst
    public DirectedWeightedGraph addEdge(Person src, Person dst) {
        return new DirectedWeightedGraph(graph.addEdge(src, dst), this.connectionPath, this.connectionPaths);
    }

    // Returns a new graph that contains a new edge between the person (node) src 
    // and every person (node) within dsts
    public DirectedWeightedGraph addEdges(Person src, ILoBuddy dsts) {
        return new DirectedWeightedGraph(graph.addEdges(src, dsts), this.connectionPath, this.connectionPaths);
    }

    // Returns the edge between person (node) src and person (node) dst
    public Edge getEdge(Person src, Person dst) {
        return graph.getEdge(src, dst);
    }

    // Returns all the edges in which the person (node) src is the source node
    public ILoEdge getAllEdges(Person src) {
        return graph.getAllEdges(src);
    }

    // Finishes the recursion copying the original stack to the new copy
    void copyStackMt(Stack copy, MtLoEdge original) {
        return;
    }
    
    // Copies the first element of the list of edges original to the new copy of the original stack
    // EFFECT: Mutates copy and pushes the first element of original onto it
    void copyStackCons(Stack copy, ConsLoEdge original) {
        copy.push(original.first);
        copyStack(copy, original.rest);
    }

    // Copies the internal list of the original stack to a new copy of the stack
    // EFFECT: Mutates copy to be a reversed version of the original stack
    void copyStack(Stack copy, ILoEdge original) {
        original.copyStack(this, copy);
    }

    // Finishes the recursion traversing the graph
    void findAllPathsMt(Person node, MtLoEdge nodeSubTrees, Person targetNode) {
        return;
    }

    // Upon further inspection, I seem to have thought about this too broadly
    // If you treat it like a normal graph where the edges are calculated
    // independent from one another this is the way to go, however, because of the way the edge 
    // weights are calculated *here* it works out that the shortest path(s) will always be the ones 
    // with the maximum likelihood. If people had different levels of diction and/or hearing 
    // with different people then this would not be the case, however, they always have the same 
    // levels regardless of who they are talking to. 

    // Traverses the graph searching for all the paths to the target node
    // and saving those paths in the form of stacks in the ILoStack connectionPath
    // EFFECT: Mutates connectionPath so as to keep track of the path currently being traversed
    // in the case that it needs to be saved with connectionPaths
    // EFFECT: Mutates connectionPaths so as to save all found paths to the target node
    // EFFECT: Creates a temporary stack that will be mutated to become a copy of the current state
    // of connectionPath
    void findAllPathsCons(Person node, ConsLoEdge connectedNodes, Person targetNode) {
        if (connectedNodes.first.dstEquals(targetNode)) {
            Stack temp = new Stack();
            // Pushing the edge to the target node onto the stack
            // So that its included in the likelihood
            connectionPath.push(connectedNodes.first);
            copyStack(temp, connectionPath.stack);
            // But we don't want it on the stack for the rest of the traversal
            // So pop it back off once we're done
            connectionPath.pop();
            connectionPaths = new ConsLoStack(temp, connectionPaths);
            findAllPathsHelper(node, connectedNodes.rest, targetNode);
        } else if (!connectionPath.contains(connectedNodes.first)) {
            connectionPath.push(connectedNodes.first);
            findAllPathsHelper(connectedNodes.first.dst, graph.getAllEdges(connectedNodes.first.dst), targetNode);
            connectionPath.pop();
            findAllPathsHelper(node, connectedNodes.rest, targetNode);
        }
        else {
            findAllPathsHelper(node, connectedNodes.rest, targetNode);
        }
    }

    // Double dispatch method used in finding all the paths between the given node
    // and target node
    void findAllPathsHelper(Person node, ILoEdge connectedNodes, Person targetNode) {
        connectedNodes.findAllPaths(this, node, targetNode);
    }

    // Returns a list of stacks, each of which contains a path from the node to the
    // targetNode
    ILoStack findAllPaths(Person node, Person targetNode) {
        // Resetting for each search
        connectionPath = new Stack();
        connectionPaths = new MtLoStack();
        findAllPathsHelper(node, graph.getAllEdges(node), targetNode);
        return connectionPaths;
    }
}

// represents a Person with a user name and a list of buddies
class Person {
    /* Template
     * 
     * Fields:
     * this.username -- String
     * this.buddies -- ILoBuddy
     * this.diction -- double
     * this.hearing -- double
     * 
     * Methods:
     * this.addBuddy(Person buddy) -- void
     * this.hasDirectBuddy(Person that) -- boolean
     * this.allPartyInvites() -- ILoBuddy
     * this.partyCount() -- int
     * this.countCommonBuddies(Person that) -- int
     * this.hasExtendedBuddy(Person that) -- boolean
     * this.maxLikelihood(Person that) -- double
     * 
     * Methods on fields:
     * this.buddies.append(Person buddy) -- ILoBuddy
     * this.buddies.append(ILoBuddy list) -- ILoBuddy
     * this.buddies.contains(Person person) -- boolean
     * this.buddies.filter(PersonPred pred) -- ILoBuddy
     * this.buddies.removeDuplicates() -- ILoBuddy
     * this.buddies.foldr(MTLoBuddy base, ExtendedBuddyCombinator combin) -- ILoBuddy
     * this.buddies.foldr(MTLoBuddy base, PartyCountCombinator combin) -- ILoBuddy
     * this.buddies.foldr(DirectedWeightedGraph base, ConvertToGraphCombinator combin) -- DirectedWeightedGraph
     * this.buddies.length() -- int
     * this.buddies.isEmpty() -- boolean
     * this.buddies.addEdges(ILoAdjacencyList graph, Person src) -- ILoAdjacencyList
     */

    String username;
    ILoBuddy buddies;

    double diction;
    double hearing;

    Person(String username) {
        this.username = username;
        this.buddies = new MTLoBuddy();
        this.diction = 0.0;
        this.hearing = 0.0;
    }

    Person(String username, double diction, double hearing) {
        this.username = username;
        this.buddies = new MTLoBuddy();
        this.diction = diction;
        this.hearing = hearing;
    }

    // EFFECT:
    // Change this person's buddy list so that it includes the given person
    void addBuddy(Person buddy) {
        this.buddies = this.buddies.append(buddy);
    }

    // returns true if this Person has that as a direct buddy
    boolean hasDirectBuddy(Person that) {
        return this.buddies.contains(that);
    }

    // Returns the list of every person who will be invited to the party hosted by this person
    ILoBuddy allPartyInvites() {
        return buddies  .foldr(new MTLoBuddy(), new PartyCountCombinator())
                        .append(this)
                        .removeDuplicates();
    }

    // returns the number of people who will show up at the party 
    // given by this person
    int partyCount() {
        // combine all buddy lists together, remove duplicates after
        return allPartyInvites().length();
    }

    // returns the number of people that are direct buddies 
    // of both this and that person
    int countCommonBuddies(Person that) {
        return this.buddies .filter(new ExtractDuplicatePeoplePred(that.buddies))
                            .length();
    }

    // will the given person be invited to a party 
    // organized by this person?
    boolean hasExtendedBuddy(Person that) {
        return allPartyInvites().contains(that);
    }

    static void printBuddies(ILoBuddy list) {
        if (list instanceof ConsLoBuddy) {
            System.out.println(((ConsLoBuddy) list).first.username);
            printBuddies(((ConsLoBuddy) list).rest);
        } else {
            return;
        }
    }
    
    static void printEdges(ILoEdge list) {
        if (list instanceof ConsLoEdge) {
            System.out.println(((ConsLoEdge) list).first.dst.username);
            System.out.println(((ConsLoEdge) list).first.weight);
            printEdges(((ConsLoEdge) list).rest);
        }
        else {
            return;
        }
    }
    
    // Returns the maximum likelihood that a message from this person
    // will reach that person
    double maxLikelihood(Person that) {
        // TODO Make sure this returns zero if they're not connected
        DirectedWeightedGraph graph = new DirectedWeightedGraph();
        return allPartyInvites().foldr(graph, new ConvertToGraphCombinator())
                                .findAllPaths(this, that)
                                .map(new ConvertListToLikelihoods())
                                .max();
    }
    
    public static void main(String[] args) {
        Person anne = new Person("Anne", 0.44, 1.00);
        Person bob = new Person("Bob", 0.61, 0.66);
        Person cole = new Person("Cole", 0.56, 0.89);
        Person dan = new Person("Dan", 0.84, 0.86);
        Person ed = new Person("Ed", 0.44, 0.90);
        Person fay = new Person("Fay", 0.31, 0.78);
        Person gabi = new Person("Gabi", 0.96, 0.53);
        Person hank = new Person("Hank", 0.84, 0.77);
        Person jan = new Person("Jan", 0.64, 0.99);
        Person kim = new Person("Kim", 0.56, 0.77);
        Person len = new Person("Len", 0.53, 0.58);
        Person jake = new Person("Jake", 0.92, 0.74);

        anne.addBuddy(bob);
        anne.addBuddy(cole);

        bob.addBuddy(anne);
        bob.addBuddy(ed);
        bob.addBuddy(hank);

        cole.addBuddy(dan);
        cole.addBuddy(jake);

        jake.addBuddy(gabi);

        dan.addBuddy(cole);

        ed.addBuddy(fay);

        fay.addBuddy(ed);
        fay.addBuddy(gabi);

        gabi.addBuddy(ed);
        gabi.addBuddy(fay);
        gabi.addBuddy(jake);

        jan.addBuddy(kim);
        jan.addBuddy(len);

        kim.addBuddy(jan);
        kim.addBuddy(len);

        len.addBuddy(kim);
        len.addBuddy(jan);

        ConsLoBuddy people = new ConsLoBuddy(jake,
                new ConsLoBuddy(anne, new ConsLoBuddy(bob, new ConsLoBuddy(cole, new ConsLoBuddy(dan,
                        new ConsLoBuddy(ed, new ConsLoBuddy(fay,
                                new ConsLoBuddy(gabi, new ConsLoBuddy(kim, new ConsLoBuddy(len,
                                        new ConsLoBuddy(jan, new ConsLoBuddy(hank, new MTLoBuddy()))))))))))));

        System.out.println("Common friends kim and len: " + kim.countCommonBuddies(len));
        System.out.println("Common friends kim and hank: " + kim.countCommonBuddies(hank));

        //Person.printBuddies(anne.buddies.foldr(new MTLoBuddy(), new ExtendedBuddyCombinator()));

        System.out.println("------------------\nResult: ");
        System.out.println(anne.hasExtendedBuddy(hank));
        System.out.println(anne.partyCount());
        printBuddies(anne.buddies.foldr(new MTLoBuddy(), new ExtendedBuddyCombinator()));
        //DFS searcher = new DFS(new ConsLoBuddy(anne, anne.buddies));
        //System.out.println(searcher.depthFirstSearch(hank).username);
        System.out.println("------------------\nPath: ");
        //printBuddies(searcher.dfsPath(hank, anne));

        DirectedWeightedGraph why = new DirectedWeightedGraph();
        why = people.foldr(why, new ConvertToGraphCombinator());
        ILoStack paths = why.findAllPaths(anne, gabi);
        System.out.println(paths.length());
        //printEdges(((ConsLoStack) ((ConsLoStack) paths).rest).first.stack);
        System.out.println("------------------\nPath2: ");
        printEdges(((ConsLoStack) paths).first.stack);
        System.out.println("------------------\nPath3: ");
        printEdges( (((ConsLoStack) ((ConsLoStack) paths).rest).first).stack);
        System.out.println(anne.maxLikelihood(gabi));
    }
}
