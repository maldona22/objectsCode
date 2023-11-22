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

    public ILoDouble map(ConvertListToLikelihoods func) {
        return new ConsLoDouble(func.apply(this.first), this.rest.map(func));
    }

    public int length() {
        return 1 + this.rest.length();
    }
}