import tester.*;

class ExampleData {
    Person anne;
    Person bob;
    Person cole;
    Person dan;
    Person ed;
    Person fay;
    Person gabi;
    Person hank;
    Person jan;
    Person kim;
    Person len;
    Person jake;

    Edge kimToJan;
    Edge kimToLen;
    ILoEdge kimEdges;

    Edge janToKim;
    Edge janToLen;
    ILoEdge janEdges;

    Edge lenToKim;
    Edge lenToJan;
    ILoEdge lenEdges;

    AdjacencyList kimAdjacencyList;
    AdjacencyList janAdjacencyList;
    AdjacencyList lenAdjacencyList;


    ConsLoAdjacencyList unfinishAdjacencyList;
    ConsLoAdjacencyList graphAdjacencyList;

    Stack pathKimToJan;

    DirectedWeightedGraph graph;

    ConsLoDouble listOfNums;

    ExampleData() {
        anne = new Person("Anne", 0.44, 1.00);
        bob = new Person("Bob", 0.61, 0.66);
        cole = new Person("Cole", 0.56, 0.89);
        dan = new Person("Dan", 0.84, 0.86);
        ed = new Person("Ed", 0.44, 0.90);
        fay = new Person("Fay", 0.31, 0.78);
        gabi = new Person("Gabi", 0.96, 0.53);
        hank = new Person("Hank", 0.84, 0.77);
        jan = new Person("Jan", 0.64, 0.99);
        kim = new Person("Kim", 0.56, 0.77);
        len = new Person("Len", 0.53, 0.58);
        jake = new Person("Jake", 0.92, 0.74);

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

        kimToJan = new Edge(jan, kim.diction * jan.hearing);
        kimToLen = new Edge(len, kim.diction * len.hearing);
        kimEdges = new ConsLoEdge(kimToJan, new ConsLoEdge(kimToLen, new MtLoEdge()));

        janToKim = new Edge(kim, jan.diction * kim.hearing);
        janToLen = new Edge(len, jan.diction * len.hearing);
        janEdges = new ConsLoEdge(janToKim, new ConsLoEdge(janToLen, new MtLoEdge()));

        lenToKim = new Edge(kim, len.diction * kim.hearing);
        lenToJan = new Edge(jan, len.diction * jan.hearing);
        lenEdges = new ConsLoEdge(lenToKim, new ConsLoEdge(lenToJan, new MtLoEdge()));

        kimAdjacencyList = new AdjacencyList(kim, kimEdges);
        janAdjacencyList = new AdjacencyList(jan, janEdges);
        lenAdjacencyList = new AdjacencyList(len, lenEdges);

        unfinishAdjacencyList = new ConsLoAdjacencyList(janAdjacencyList,
                new ConsLoAdjacencyList(lenAdjacencyList, new MtLoAdjacencyList()));
        graphAdjacencyList = new ConsLoAdjacencyList(kimAdjacencyList, new ConsLoAdjacencyList(janAdjacencyList,
                new ConsLoAdjacencyList(lenAdjacencyList, new MtLoAdjacencyList())));

        pathKimToJan = new Stack();
        pathKimToJan.push(kimToLen);
        pathKimToJan.push(lenToJan);

        graph = new DirectedWeightedGraph();

        graph = anne.buddies.append(anne).foldr(graph, new ConvertToGraphCombinator());

        listOfNums = new ConsLoDouble(0.9, new ConsLoDouble(0.76, new MtLoDouble()));
    }
}

// runs tests for the buddies problem
public class ExamplesBuddies {
    public boolean testDuplicatePersonPredicate(Tester t) {
        ExampleData data = new ExampleData();
        return t.checkExpect(data.anne.buddies.filter(new DuplicatePersonPred(data.bob)),
                (new ConsLoBuddy(data.cole, new MTLoBuddy())));
    }

    public void testDuplicatePeoplePredicate(Tester t) {
        ExampleData data = new ExampleData();
        t.checkExpect(data.anne.buddies.filter(new DuplicatePeoplePred(new ConsLoBuddy(data.bob, new MTLoBuddy()))),
                (new ConsLoBuddy(data.cole, new MTLoBuddy())));
    }

    public void testExtractPeoplePredicate(Tester t) {
       ExampleData data = new ExampleData();
        t.checkExpect(data.anne.buddies.filter(new ExtractDuplicatePeoplePred(new ConsLoBuddy(data.bob, new MTLoBuddy()))),
                (new ConsLoBuddy(data.bob, new MTLoBuddy())));
    }

    public void testExtendedBuddyCombinator(Tester t) {
        ExampleData data = new ExampleData();
        t.checkExpect(data.anne.buddies.foldr(new MTLoBuddy(), new ExtendedBuddyCombinator()).removeDuplicates(), new ConsLoBuddy(data.cole, new ConsLoBuddy(data.jake, new ConsLoBuddy(data.gabi, new ConsLoBuddy(data.fay, new ConsLoBuddy(data.ed, new ConsLoBuddy(data.dan, new ConsLoBuddy(data.bob, new ConsLoBuddy(data.hank, new ConsLoBuddy(data.anne, new MTLoBuddy()))))))))));
    }

    public void testPartyCountCombinator(Tester t) {
        ExampleData data = new ExampleData();
        t.checkExpect(data.anne.partyCount(), 9);
    }

    public void testConvertToGraphCombinator(Tester t) {
        //ExampleData data = new ExampleData();

    }

    public void testMultiplyWeightsCombinator(Tester t) {

    }

    public void testConvertListToLikelihoods(Tester t) {

    }

    public void testConsLoBuddyAppendBuddy(Tester t) {
        ExampleData data = new ExampleData();
        t.checkExpect(data.anne.buddies.append(data.ed),
                new ConsLoBuddy(data.bob, new ConsLoBuddy(data.cole, new ConsLoBuddy(data.ed, new MTLoBuddy()))));
    }

    public void testConsLoBuddyAppendILoBuddy(Tester t) {
        ExampleData data = new ExampleData();
        t.checkExpect(data.anne.buddies.append(new ConsLoBuddy(data.ed, new ConsLoBuddy(data.fay, new MTLoBuddy()))),
                new ConsLoBuddy(data.bob, new ConsLoBuddy(data.cole,
                        new ConsLoBuddy(data.ed, new ConsLoBuddy(data.fay, new MTLoBuddy())))));
                
        t.checkExpect(data.anne.buddies.append(new MTLoBuddy()), data.anne.buddies);
    }

    public void testConsLoBuddyContains(Tester t) {
        ExampleData data = new ExampleData();
        t.checkExpect(data.anne.buddies.contains(data.bob), true);
        t.checkExpect(data.anne.buddies.contains(data.ed), false);
    }

    public void testConsLoBuddyFilter(Tester t) {
        ExampleData data = new ExampleData();
        t.checkExpect(data.anne.buddies.filter(new DuplicatePersonPred(data.bob)),
                new ConsLoBuddy(data.cole, new MTLoBuddy()));
        t.checkExpect(data.anne.buddies.filter(
                new DuplicatePeoplePred(new ConsLoBuddy(data.bob, new ConsLoBuddy(data.cole, new MTLoBuddy())))),
                new MTLoBuddy());
        t.checkExpect(
                data.anne.buddies.filter(new ExtractDuplicatePeoplePred(new ConsLoBuddy(data.bob, new MTLoBuddy()))), new ConsLoBuddy(data.bob, new MTLoBuddy()));
    }

    public void testConsLoBuddyRemoveDuplicates(Tester t) {
        ExampleData data = new ExampleData();
        t.checkExpect(data.anne.buddies.append(data.bob).removeDuplicates(), data.anne.buddies);
        t.checkExpect(data.fay.buddies.removeDuplicates(), data.fay.buddies);
    }

    public void testConsLoBuddyFoldrExtendedBuddyCombinator(Tester t) {

    }
    
    public void testConsLoBuddyFoldrPartyCountCombinator(Tester t) {

    }
    
    public void testConsLoBuddyFoldrConvertToGraphCombinator(Tester t) {

    }
    
    public void testConsLoBuddyLength(Tester t) {
        ExampleData data = new ExampleData();
        t.checkExpect(data.anne.buddies.length(), 2);
        t.checkExpect((new MTLoBuddy()).length(), 0);
    }
    
    public void testConsLoBuddyIsEmpty(Tester t) {
        ExampleData data = new ExampleData();
        t.checkExpect(data.anne.buddies.isEmpty(), false);
        t.checkExpect((new MTLoBuddy()).isEmpty(), true);
    }
    
    public void testConsLoBuddyAddEdges(Tester t) {
        ExampleData data = new ExampleData();
        ILoAdjacencyList unfinishedGraph = new ConsLoAdjacencyList(data.janAdjacencyList,
                new ConsLoAdjacencyList(data.lenAdjacencyList, new MtLoAdjacencyList()));
        t.checkExpect(data.kim.buddies.addEdges(unfinishedGraph, data.kim), data.graphAdjacencyList);
    }
    
    public void testConsLoEdgeGet(Tester t) {
        ExampleData data = new ExampleData();
        t.checkExpect(data.kimEdges.get(data.jan), data.kimToJan);
    }
    
    public void testConsLoEdgeCopyStack(Tester t) {
        ExampleData data = new ExampleData();
        Stack temp = new Stack();
        data.pathKimToJan.stack.copyStack(data.graph, temp);
        t.checkExpect(temp, new ConsLoEdge(data.kimToLen, new ConsLoEdge(data.lenToJan, new MtLoEdge())));
    }
    
    public void testConsLoEdgeFindAllPaths(Tester t) {

    }
    
    public void testConsLoEdgePop(Tester t) {
        ExampleData data = new ExampleData();
        data.pathKimToJan.stack.pop(data.pathKimToJan);
        t.checkExpect(data.pathKimToJan.stack, new ConsLoEdge(data.kimToLen, new MtLoEdge()));
    }
    
    public void testConsLoEdgeIsEmpty(Tester t) {
        ExampleData data = new ExampleData();
        t.checkExpect(data.kimEdges.isEmpty(), false);
        t.checkExpect((new MtLoEdge()).isEmpty(), true);
    }
    
    public void testConsLoEdgeContains(Tester t) {
        ExampleData data = new ExampleData();
        t.checkExpect(data.kimEdges.contains(data.kimToJan), true);
        t.checkExpect(data.kimEdges.contains(data.janToKim), false);
    }
    
    public void testConsLoEdgeAppend(Tester t) {
        ExampleData data = new ExampleData();
        t.checkExpect(data.kimEdges.append(data.janEdges), new ConsLoEdge(data.kimToJan, new ConsLoEdge(data.kimToLen, new ConsLoEdge(data.janToKim, new ConsLoEdge(data.janToLen, new MtLoEdge())))));
    }
    
    public void testConsLoEdgeFoldrMultiplyWeightsCombinator(Tester t) {

    }
    
    public void testConsLoAdjacencyListContains(Tester t) {
        ExampleData data = new ExampleData();
        t.checkExpect(data.graphAdjacencyList.contains(data.kim), true);
        t.checkExpect(data.graphAdjacencyList.contains(data.jake), false);
    }
    
    public void testConsLoAdjacencyListAddNode(Tester t) {
        ExampleData data = new ExampleData();
        t.checkExpect(data.graphAdjacencyList.addNode(data.jake),
                new ConsLoAdjacencyList(new AdjacencyList(data.jake), data.graphAdjacencyList));
    }
    
    public void testConsLoAdjacencyListAddEdge(Tester t) {
        ExampleData data = new ExampleData();
        ConsLoAdjacencyList lists = new ConsLoAdjacencyList(new AdjacencyList(data.kim, new ConsLoEdge(data.kimToLen, new MtLoEdge())), new MtLoAdjacencyList());
        t.checkExpect(lists.addEdge(data.kim, data.jan),
                new ConsLoAdjacencyList(data.kimAdjacencyList, new MtLoAdjacencyList()));
    }
    
    public void testConsLoAdjacencyListAddEdgesMt(Tester t) {
        ExampleData data = new ExampleData();
        ILoAdjacencyList testResult = data.graphAdjacencyList.addEdgesMt(data.kim, new MTLoBuddy());
        t.checkExpect(testResult, data.graphAdjacencyList);
    }
    
    public void testConsLoAdjacencyListAddEdgesCons(Tester t) {
        ExampleData data = new ExampleData();
        ILoAdjacencyList testResult = data.unfinishAdjacencyList.addEdgesCons(data.kim, new ConsLoBuddy(data.len, new ConsLoBuddy(data.jan, new MTLoBuddy())));
        t.checkExpect(testResult, data.graphAdjacencyList);
    }
    
    public void testConsLoAdjacencyListAddEdges(Tester t) {
        ExampleData data = new ExampleData();
        ILoAdjacencyList testResult = data.unfinishAdjacencyList.addEdgesCons(data.kim, new ConsLoBuddy(data.len, new ConsLoBuddy(data.jan, new MTLoBuddy())));
        t.checkExpect(testResult, data.graphAdjacencyList);
    }
    
    public void testConsLoAdjacencyListGetEdge(Tester t) {
        ExampleData data = new ExampleData();
        t.checkExpect(data.graphAdjacencyList.getEdge(data.kim, data.jan), data.kimToJan);
    }
    
    public void testConsLoAdjacencyListGetAllEdges(Tester t) {
        ExampleData data = new ExampleData();
        t.checkExpect(data.graphAdjacencyList.getAllEdges(data.kim), data.kimEdges);
    }
    
    public void testConsLoDoubleMaxHelper(Tester t) {
        ExampleData data = new ExampleData();
        t.checkExpect(data.listOfNums.maxHelper(1.2), 1.2);
        t.checkExpect(data.listOfNums.maxHelper(0.1), 0.9);
    }
    
    public void testConsLoDoubleMax(Tester t) {
        ExampleData data = new ExampleData();
        t.checkExpect(data.listOfNums.max(), 0.9);
    }
    
    public void testConsLoStackMapConvertListToWeights(Tester t) {

    }
    
    public void testMtLoBuddyContains(Tester t) {
        ExampleData data = new ExampleData();
        t.checkExpect((new MTLoBuddy()).contains(data.bob), false);
    }

    public void testMtLoBuddyRemoveDuplicates(Tester t) {
        MTLoBuddy list = new MTLoBuddy();
        t.checkExpect(list.removeDuplicates(), list);
    }
    
    public void testMtLoBuddyFilter(Tester t) {
        ExampleData data = new ExampleData();
        MTLoBuddy emptyList = new MTLoBuddy();
        t.checkExpect(emptyList.filter(new DuplicatePersonPred(data.anne)), emptyList);
        t.checkExpect(emptyList.filter(
                new DuplicatePeoplePred(new ConsLoBuddy(data.anne, new ConsLoBuddy(data.bob, new MTLoBuddy())))),
                emptyList);
        t.checkExpect(emptyList.filter(new ExtractDuplicatePeoplePred(new ConsLoBuddy(data.anne, new ConsLoBuddy(data.bob, new MTLoBuddy())))), emptyList);
    }
    
    public void testMtLoBuddyAppendILoBuddy(Tester t) {
        ExampleData data = new ExampleData();
        t.checkExpect((new MTLoBuddy()).append(data.anne.buddies), data.anne.buddies);
        t.checkExpect((new MTLoBuddy()).append(new MTLoBuddy()), new MTLoBuddy());
    }
    
    public void testMtLoBuddyAppendPerson(Tester t) {
        ExampleData data = new ExampleData();
        t.checkExpect((new MTLoBuddy()).append(data.anne), new ConsLoBuddy(data.anne, new MTLoBuddy()));
    }
    
    public void testMtLoBuddyFoldrExtendedBuddyCombinator(Tester t) {
        t.checkExpect((new MTLoBuddy()).foldr(new MTLoBuddy(), new ExtendedBuddyCombinator()), new MTLoBuddy());
    }
    
    public void testMtLoBuddyFoldrPartyCountCombinator(Tester t) {
        t.checkExpect((new MTLoBuddy()).foldr(new MTLoBuddy(), new PartyCountCombinator()), new MTLoBuddy());
    }
    
    public void testMtLoBuddyFoldrConvertToGraphCombinator(Tester t) {
        DirectedWeightedGraph tempGraph = new DirectedWeightedGraph();
        t.checkExpect((new MTLoBuddy()).foldr(tempGraph, new ConvertToGraphCombinator()), tempGraph);
    }
    
    public void testMtLoBuddyLength(Tester t) {
        t.checkExpect((new MTLoBuddy()).length(), 0);
    }
    
    public void testMtLoBuddyIsEmpty(Tester t) {
        t.checkExpect((new MTLoBuddy()).isEmpty(), true);
    }
    
    public void testMtLoBuddyAddEdges(Tester t) {
        ExampleData data = new ExampleData();
        t.checkExpect((new MTLoBuddy()).addEdges(data.graphAdjacencyList, data.anne), data.graphAdjacencyList);
    }
    
    public void testMtLoEdgeGet(Tester t) {
        ExampleData data = new ExampleData();
        t.checkException(new IllegalArgumentException(), (new MtLoEdge()), "get", data.anne);
    }
    
    public void testMtLoEdgeCopyStack(Tester t) {
        ExampleData data = new ExampleData();
        Stack temp = new Stack();
        (new MtLoEdge()).copyStack(data.graph, temp);
        t.checkExpect(temp.stack, new MtLoEdge());
    }
    
    public void testMtLoEdgeFindAllPaths(Tester t) {

    }
    
    public void testMtLoEdgePop(Tester t) {
        ExampleData data = new ExampleData();
        //(new MtLoEdge()).pop(data.pathKimToJan);
        t.checkException(new IllegalArgumentException(), (new MtLoEdge()), "pop", data.pathKimToJan);
    }
    
    public void testMtLoEdgeIsEmpty(Tester t) {
        t.checkExpect((new MtLoEdge()).isEmpty(), true);
    }
    
    public void testMtLoEdgeContains(Tester t) {
        ExampleData data = new ExampleData();
        t.checkExpect((new MtLoEdge()).contains(data.janToKim), false);
    }
    
    public void testMtLoEdgeAppend(Tester t) {
        ExampleData data = new ExampleData();
        t.checkExpect((new MtLoEdge()).append(data.janEdges), data.janEdges);
    }
    
    public void testMtLoEdgeFoldrMultiplyWeightsCombinator(Tester t) {
        t.checkExpect((new MtLoEdge()).foldr(1, new MultiplyWeightsCombinator()), 1.0);
    }
    
    public void testMtLoAdjacencyListContains(Tester t) {
        ExampleData data = new ExampleData();
        t.checkExpect((new MtLoAdjacencyList()).contains(data.hank), false);
    }
    
    public void testMtLoAdjacencyListAddNode(Tester t) {
        ExampleData data = new ExampleData();
        t.checkExpect((new MtLoAdjacencyList()).addNode(data.cole), new ConsLoAdjacencyList(new AdjacencyList(data.cole), new MtLoAdjacencyList()));
    }
    
    public void testMtLoAdjacencyListGet(Tester t) {
        ExampleData data = new ExampleData();
        t.checkException(new IllegalArgumentException(), (new MtLoAdjacencyList()), "get", data.dan);
    }
    
    public void testMtLoAdjacencyListAddEdge(Tester t) {
        ExampleData data = new ExampleData();
        t.checkExpect((new MtLoAdjacencyList()).addEdge(data.kim, data.anne), new MtLoAdjacencyList());
    }
    
    public void testMtLoAdjacencyListAddEdges(Tester t) {
        ExampleData data = new ExampleData();
        t.checkExpect((new MtLoAdjacencyList()).addEdges(data.kim, data.anne.buddies), new MtLoAdjacencyList());
    }
    
    public void testMtLoAdjacencyListAddEdgesCons(Tester t) {
        ExampleData data = new ExampleData();
        t.checkExpect((new MtLoAdjacencyList()).addEdgesCons(data.kim, new ConsLoBuddy(data.anne, new ConsLoBuddy(data.hank, new MTLoBuddy()))), new MtLoAdjacencyList());
    }
    
    public void testMtLoAdjacencyListAddEdgesMt(Tester t) {
        ExampleData data = new ExampleData();
        t.checkExpect((new MtLoAdjacencyList()).addEdgesMt(data.kim, new MTLoBuddy()), new MtLoAdjacencyList());
    }
    
    public void testMtLoAdjacencyListGetEdge(Tester t) {
        ExampleData data = new ExampleData();
        MtLoAdjacencyList mtList = new MtLoAdjacencyList();
        t.checkException(new IllegalArgumentException(), mtList, "getEdge", data.kim, data.dan);
    }
    
    public void testMtLoAdjacencyListGetAllEdges(Tester t) {
        ExampleData data = new ExampleData();
        t.checkExpect((new MtLoAdjacencyList()).getAllEdges(data.kim), new MtLoEdge());
    }
    
    public void testMtLoDoubleMaxHelper(Tester t) {
        t.checkExpect((new MtLoDouble()).maxHelper(2), 2.0);
    }

    public void testMtLoDoubleMax(Tester t) {
        t.checkException(new IllegalArgumentException(), (new MtLoDouble()), "max");
    }
    
    public void testMtLoStackMapConvertListToLikelihoods(Tester t) {
        t.checkExpect((new MtLoStack()).map(new ConvertListToLikelihoods()), new MtLoDouble());
    }

    public void testDstEquals(Tester t) {
        ExampleData data = new ExampleData();
        t.checkExpect(data.kimToJan.dstEquals(data.jan), true);
        t.checkExpect(data.kimToJan.dstEquals(data.dan), false);
    }

    public void testSrcEquals(Tester t) {
        ExampleData data = new ExampleData();
        t.checkExpect(data.kimAdjacencyList.srcEquals(data.kim), true);
        t.checkExpect(data.kimAdjacencyList.srcEquals(data.jake), false);
    }

    public void testStackIsEmpty(Tester t) {
        ExampleData data = new ExampleData();
        t.checkExpect(data.pathKimToJan.isEmpty(), false);
        t.checkExpect((new Stack()).isEmpty(), true);
    }

    public void testStackContains(Tester t) {
        ExampleData data = new ExampleData();
        t.checkExpect(data.pathKimToJan.contains(data.kimToLen), true);
        t.checkExpect(data.pathKimToJan.contains(data.janToLen), false);
    }

    public void testStackPushEdge(Tester t) {
        ExampleData data = new ExampleData();
        Stack tempStack = new Stack();
        tempStack.push(data.kimToLen);
        tempStack.push(data.lenToJan);
        t.checkExpect(tempStack, data.pathKimToJan);
    }

    public void testStackPushILoEdge(Tester t) {
        ExampleData data = new ExampleData();
        Stack tempStack = new Stack();
        tempStack.push(new ConsLoEdge(data.lenToJan, new ConsLoEdge(data.kimToLen, new MtLoEdge())));
        t.checkExpect(tempStack, data.pathKimToJan);
    }

    public void testStackPop(Tester t) {
        ExampleData data = new ExampleData();
        Stack tempStack = new Stack();
        tempStack.push(data.kimToLen);
        t.checkExpect(data.pathKimToJan.pop(), data.lenToJan);
        t.checkExpect(data.pathKimToJan, tempStack);
    }

    public void testStackPopCons(Tester t) {
        ExampleData data = new ExampleData();
        Stack tempStack = new Stack();
        ConsLoEdge tempEdges = new ConsLoEdge(data.lenToJan, new ConsLoEdge(data.kimToLen, new MtLoEdge()));
        tempStack.push(data.kimToLen);

        t.checkExpect(data.pathKimToJan.popCons(tempEdges), data.lenToJan);
        t.checkExpect(data.pathKimToJan, tempStack);
    }

    public void testStackPopMt(Tester t) {
        ExampleData data = new ExampleData();
        t.checkException(new IllegalArgumentException(), data.pathKimToJan, "popMt", (new MtLoEdge()));
    }
    
    public void testDWGContains(Tester t) {
        ExampleData data = new ExampleData();
        t.checkExpect(data.graph.contains(data.anne), true);
        t.checkExpect(data.graph.contains(new Person("king")), false);
    }

    public void testDWGAddEdge(Tester t) {
        ExampleData data = new ExampleData();

        DirectedWeightedGraph actualResult = new DirectedWeightedGraph();
        actualResult = data.anne.buddies.append(data.anne).foldr(data.graph, new ConvertToGraphCombinator());
        actualResult = actualResult.addEdge(data.kim, data.cole);

        data.kim.addBuddy(data.cole);
        DirectedWeightedGraph expectedResult = new DirectedWeightedGraph();
        expectedResult = data.anne.buddies.append(data.anne).foldr(data.graph, new ConvertToGraphCombinator());

        t.checkExpect(actualResult, expectedResult);
    }

    public void testDWGAddEdges(Tester t) {
        ExampleData data = new ExampleData();

        DirectedWeightedGraph actualResult = new DirectedWeightedGraph();
        actualResult = data.anne.buddies.append(data.anne).foldr(data.graph, new ConvertToGraphCombinator());
        actualResult = actualResult.addEdges(data.kim, new ConsLoBuddy(data.cole, new ConsLoBuddy(data.dan, new MTLoBuddy())));

        data.kim.addBuddy(data.cole);
        data.kim.addBuddy(data.dan);
        DirectedWeightedGraph expectedResult = new DirectedWeightedGraph();
        expectedResult = data.anne.buddies.append(data.anne).foldr(data.graph, new ConvertToGraphCombinator());

        t.checkExpect(actualResult, expectedResult);
    }
    
    public void testDWGGetEdge(Tester t) {
        ExampleData data = new ExampleData();
        t.checkExpect(data.graph.getEdge(data.anne, data.bob), new Edge(data.bob, data.anne.diction*data.bob.hearing));
    }
    
    public void testDWGGetAllEdges(Tester t) {
        ExampleData data = new ExampleData();
        t.checkExpect(data.graph.getAllEdges(data.kim), new MtLoEdge());
        t.checkExpect(data.graph.getAllEdges(data.anne), new ConsLoEdge(new Edge(data.bob, data.anne.diction*data.bob.hearing), new ConsLoEdge(new Edge(data.cole, data.anne.diction*data.cole.hearing), new MtLoEdge())));
    }
    
    public void testDWGCopyStackMt(Tester t) {
        ExampleData data = new ExampleData();
        Stack tempStack = new Stack();
        data.graph.copyStackMt(tempStack, new MtLoEdge());
        t.checkExpect(tempStack, new Stack());
    }
    
    public void testDWGCopyStackCons(Tester t) {
        ExampleData data = new ExampleData();
        Stack tempStack = new Stack();
        ConsLoEdge edges = new ConsLoEdge(data.kimToLen, new ConsLoEdge(data.lenToJan, new MtLoEdge()));
        data.graph.copyStackCons(tempStack, edges);
        t.checkExpect(tempStack, new ConsLoEdge(data.kimToLen, new ConsLoEdge(data.lenToJan, new MtLoEdge())));
    }
    
    public void testDWGCopyStack(Tester t) {
        ExampleData data = new ExampleData();
        Stack tempStack = new Stack();
        data.graph.copyStack(tempStack, data.pathKimToJan.stack);
        t.checkExpect(tempStack, new ConsLoEdge(data.kimToLen, new ConsLoEdge(data.lenToJan, new MtLoEdge())));
    }
    
    public void testDWGFindAllPathsHelper(Tester t) {

    }
    
    public void testDWGFindAllPathsCons(Tester t) {

    }
    
    public void testDWGFindAllPathsMt(Tester t) {

    }
    
    public void testDWGFindAllPaths(Tester t) {

    }
    
    public void testAddBuddy(Tester t) {
        ExampleData data = new ExampleData();
        ILoBuddy before = data.kim.buddies;
        data.kim.addBuddy(data.dan);
        t.checkExpect(data.kim.buddies, before.append(data.dan));
    }

    public void testHasDirectBuddy(Tester t) {
        ExampleData data = new ExampleData();
        t.checkExpect(data.anne.hasDirectBuddy(data.bob), true);
        t.checkExpect(data.anne.hasDirectBuddy(data.len), false);
    }

    public void testAllPartyInvites(Tester t) {

    }

    public void testPartyCount(Tester t) {
        ExampleData data = new ExampleData();
        t.checkExpect(data.anne.partyCount(), 9);
    }

    public void testCountCommonBuddies(Tester t) {
        ExampleData data = new ExampleData();
        t.checkExpect(data.kim.countCommonBuddies(data.jan), 1);
    }

    public void testHasExtendedBuddy(Tester t) {

    }

    public void testMaximumLikelihood(Tester t) {
        
    }
}