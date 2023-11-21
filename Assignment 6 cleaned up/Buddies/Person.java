// -------------------------------- Predicates --------------------------------

interface PersonPred {
    public boolean apply(Person person);
}


class DuplicatePersonPred implements PersonPred {
    Person person;

    public boolean apply(Person person) {
        return !this.person.equals(person);
    }

    DuplicatePersonPred(Person person) {
        this.person = person;
    }
}

class DuplicatePeoplePred implements PersonPred {
    ILoBuddy people;

    public boolean apply(Person person) {
        return !this.people.contains(person);
    }

    DuplicatePeoplePred(ILoBuddy people) {
        this.people = people;
    }
}

class ExtractDuplicatePeoplePred implements PersonPred {
    ILoBuddy people;

    public boolean apply(Person person) {
        return people.contains(person);
    }

    ExtractDuplicatePeoplePred(ILoBuddy people) {
        this.people = people;
    }
}

// -------------------------------- Combinators --------------------------------

class ExtendedBuddyCombinator {
    ILoBuddy visited;

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
    public ILoBuddy apply(Person person, ILoBuddy accum) {
        return accum.append(person)
                    .append(person.buddies.foldr(new MTLoBuddy(), new ExtendedBuddyCombinator()));
    }
}

class ConvertToGraphCombinator {
    public DirectedWeightedGraph apply(Person person, DirectedWeightedGraph accum) {
        return accum.addNode(person)
                    .addEdges(person, person.buddies)
                    ;
    }
}

class MultiplyWeightsCombinator {
    public double apply(Edge edge, double accum) {
        return edge.weight * accum;
    }
}

// -------------------------------- IFuncs --------------------------------

// TODO change test names to ConvertListToLikelihoods
class ConvertListToLikelihoods {
    double apply(Stack stack) {
        return stack.stack.foldr(1, new MultiplyWeightsCombinator());
    }
}

// -------------------------------- Objects --------------------------------
class Edge {
    Person dst;
    double weight;

    Edge(Person dst, double weight) {
        this.dst = dst;
        this.weight = weight;
    }

    boolean dstEquals(Person dst) {
        return this.dst.equals(dst);
    }
}

class AdjacencyList {
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

    boolean srcEquals(Person src) {
        return this.src.equals(src);
    }
}

class Stack {
    ILoEdge stack;

    Stack() {
        this.stack = new MtLoEdge();
    }

    public boolean isEmpty() {
        return stack.isEmpty();
    }

    public boolean contains(Edge edge) {
        return stack.contains(edge);
    }

    public void push(Edge edge) {
        stack = new ConsLoEdge(edge, stack);
    }

    public void push(ILoEdge edges) {
        stack = edges.append(stack);
    }

    public Edge pop() {
        return stack.pop(this);
    }

    public Edge popCons(ConsLoEdge stack) {
        this.stack = stack.rest;
        return stack.first;
    }

    public Edge popMt(MtLoEdge stack) {
        throw new IllegalAccessError();
    }
}

class DirectedWeightedGraph {
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

    public boolean contains(Person src) {
        return graph.contains(src);
    }

    public DirectedWeightedGraph addNode(Person src) {
        return new DirectedWeightedGraph(graph.addNode(src), this.connectionPath, this.connectionPaths);
    }

    public DirectedWeightedGraph addEdge(Person src, Person dst) {
        return new DirectedWeightedGraph(graph.addEdge(src, dst), this.connectionPath, this.connectionPaths);
    }

    public DirectedWeightedGraph addEdges(Person src, ILoBuddy dsts) {
        return new DirectedWeightedGraph(graph.addEdges(src, dsts), this.connectionPath, this.connectionPaths);
    }

    public Edge getEdge(Person src, Person dst) {
        return graph.getEdge(src, dst);
    }

    public ILoEdge getAllEdges(Person src) {
        return getAllEdges(src);
    }

    void copyStackMt(Stack copy, MtLoEdge original) {
        return;
    }

    void copyStackCons(Stack copy, ConsLoEdge original) {
        copy.push(original.first);
        copyStack(copy, original.rest);
    }

    void copyStack(Stack copy, ILoEdge original) {
        original.copyStack(this, copy);
    }

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

    void findAllPathsHelper(Person node, ILoEdge connectedNodes, Person targetNode) {
        connectedNodes.findAllPaths(this, node, targetNode);
    }

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
