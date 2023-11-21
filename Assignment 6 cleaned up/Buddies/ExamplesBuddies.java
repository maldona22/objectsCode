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

        graph = anne.buddies.append(anne).foldr(graph, new ConvertToGraphCombinator());

        listOfNums = new ConsLoDouble(0.9, new ConsLoDouble(0.76, new MtLoDouble()));
    }
}

// runs tests for the buddies problem
public class ExamplesBuddies {
    void testDuplicatePersonPredicate(Tester t) {
        ExampleData data = new ExampleData();
        t.checkExpect(data.anne.buddies.filter(new DuplicatePersonPred(data.bob)).equals
                (new ConsLoBuddy(data.cole, new MTLoBuddy())), true);
    }

    void testDuplicatePeoplePredicate(Tester t) {
        ExampleData data = new ExampleData();
        t.checkExpect(data.anne.buddies.filter(new DuplicatePeoplePred(new ConsLoBuddy(data.bob, new MTLoBuddy()))).equals
                (new ConsLoBuddy(data.cole, new MTLoBuddy())), true);
    }

    void testExtractPeoplePredicate(Tester t) {
       ExampleData data = new ExampleData();
        t.checkExpect(data.anne.buddies.filter(new ExtractDuplicatePeoplePred(new ConsLoBuddy(data.bob, new MTLoBuddy()))).equals
                (new ConsLoBuddy(data.bob, new MTLoBuddy())), true); 
    }

    void testExtendedBuddyCombinator(Tester t) {
        ExampleData data = new ExampleData();
        t.checkExpect(data.anne.buddies.foldr(new MTLoBuddy(), new ExtendedBuddyCombinator()).removeDuplicates(), new ConsLoBuddy(data.cole, new ConsLoBuddy(data.jake, new ConsLoBuddy(data.gabi, new ConsLoBuddy(data.fay, new ConsLoBuddy(data.ed, new ConsLoBuddy(data.dan, new ConsLoBuddy(data.bob, new ConsLoBuddy(data.hank, new ConsLoBuddy(data.anne, new MTLoBuddy()))))))))));
    }

    void testPartyCountCombinator(Tester t) {
        ExampleData data = new ExampleData();
        t.checkExpect(data.anne.partyCount(), 9);
    }

    void testConvertToGraphCombinator(Tester t) {
        //ExampleData data = new ExampleData();

    }

    void testMultiplyWeightsCombinator(Tester t) {

    }

    void testConvertListToLikelihoods(Tester t) {

    }

    void testConsLoBuddyAppendBuddy(Tester t) {
        ExampleData data = new ExampleData();
        t.checkExpect(data.anne.buddies.append(data.ed),
                new ConsLoBuddy(data.bob, new ConsLoBuddy(data.cole, new ConsLoBuddy(data.ed, new MTLoBuddy()))));
    }

    void testConsLoBuddyAppendILoBuddy(Tester t) {
        ExampleData data = new ExampleData();
        t.checkExpect(data.anne.buddies.append(new ConsLoBuddy(data.ed, new ConsLoBuddy(data.fay, new MTLoBuddy()))),
                new ConsLoBuddy(data.bob, new ConsLoBuddy(data.cole,
                        new ConsLoBuddy(data.ed, new ConsLoBuddy(data.fay, new MTLoBuddy())))));
                
        t.checkExpect(data.anne.buddies.append(new MTLoBuddy()), data.anne.buddies);
    }

    void testConsLoBuddyContains(Tester t) {
        ExampleData data = new ExampleData();
        t.checkExpect(data.anne.buddies.contains(data.bob), true);
        t.checkExpect(data.anne.buddies.contains(data.ed), false);
    }

    void testConsLoBuddyFilter(Tester t) {
        ExampleData data = new ExampleData();
        t.checkExpect(data.anne.buddies.filter(new DuplicatePersonPred(data.bob)),
                new ConsLoBuddy(data.cole, new MTLoBuddy()));
        t.checkExpect(data.anne.buddies.filter(
                new DuplicatePeoplePred(new ConsLoBuddy(data.bob, new ConsLoBuddy(data.cole, new MTLoBuddy())))),
                new MTLoBuddy());
        t.checkExpect(
                data.anne.buddies.filter(new ExtractDuplicatePeoplePred(new ConsLoBuddy(data.bob, new MTLoBuddy()))), new ConsLoBuddy(data.bob, new MTLoBuddy()));
    }

    void testConsLoBuddyRemoveDuplicates(Tester t) {
        ExampleData data = new ExampleData();
        t.checkExpect(data.anne.buddies.append(data.bob).removeDuplicates(), data.anne.buddies);
        t.checkExpect(data.fay.buddies.removeDuplicates(), data.fay.buddies);
    }

    void testConsLoBuddyFoldrExtendedBuddyCombinator(Tester t) {

    }
    
    void testConsLoBuddyFoldrPartyCountCombinator(Tester t) {

    }
    
    void testConsLoBuddyFoldrConvertToGraphCombinator(Tester t) {

    }
    
    void testConsLoBuddyLength(Tester t) {
        ExampleData data = new ExampleData();
        t.checkExpect(data.anne.buddies.length(), 2);
        t.checkExpect((new MTLoBuddy()).length(), 0);
    }
    
    void testConsLoBuddyIsEmpty(Tester t) {
        ExampleData data = new ExampleData();
        t.checkExpect(data.anne.buddies.isEmpty(), false);
        t.checkExpect((new MTLoBuddy()).isEmpty(), true);
    }
    
    void testConsLoBuddyAddEdges(Tester t) {
        ExampleData data = new ExampleData();
        ILoAdjacencyList unfinishedGraph = new ConsLoAdjacencyList(data.janAdjacencyList,
                new ConsLoAdjacencyList(data.lenAdjacencyList, new MtLoAdjacencyList()));
        t.checkExpect(data.kim.buddies.addEdges(unfinishedGraph, data.kim), data.graphAdjacencyList);
    }
    
    void testConsLoEdgeGet(Tester t) {
        ExampleData data = new ExampleData();
        t.checkExpect(data.kimEdges.get(data.jan), data.kimToJan);
    }
    
    void testConsLoEdgeCopyStack(Tester t) {
        ExampleData data = new ExampleData();
        Stack temp = new Stack();
        data.pathKimToJan.stack.copyStack(data.graph, temp);
        t.checkExpect(temp, data.pathKimToJan);
    }
    
    void testConsLoEdgeFindAllPaths(Tester t) {

    }
    
    void testConsLoEdgePop(Tester t) {
        ExampleData data = new ExampleData();
        data.pathKimToJan.stack.pop(data.pathKimToJan);
        t.checkExpect(data.pathKimToJan.stack, new ConsLoEdge(data.kimToLen, new MtLoEdge()));
    }
    
    void testConsLoEdgeIsEmpty(Tester t) {
        ExampleData data = new ExampleData();
        t.checkExpect(data.kimEdges.isEmpty(), false);
        t.checkExpect((new MtLoEdge()).isEmpty(), true);
    }
    
    void testConsLoEdgeContains(Tester t) {
        ExampleData data = new ExampleData();
        t.checkExpect(data.kimEdges.contains(data.kimToJan), true);
        t.checkExpect(data.kimEdges.contains(data.janToKim), false);
    }
    
    void testConsLoEdgeAppend(Tester t) {
        ExampleData data = new ExampleData();
        t.checkExpect(data.kimEdges.append(data.janEdges), new ConsLoEdge(data.kimToJan, new ConsLoEdge(data.kimToLen, new ConsLoEdge(data.janToKim, new ConsLoEdge(data.janToLen, new MtLoEdge())))));
    }
    
    void testConsLoEdgeFoldrMultiplyWeightsCombinator(Tester t) {

    }
    
    void testConsLoAdjacencyListContains(Tester t) {
        ExampleData data = new ExampleData();
        t.checkExpect(data.graphAdjacencyList.contains(data.kim), true);
        t.checkExpect(data.graphAdjacencyList.contains(data.jake), false);
    }
    
    void testConsLoAdjacencyListAddNode(Tester t) {
        ExampleData data = new ExampleData();
        t.checkExpect(data.graphAdjacencyList.addNode(data.jake),
                new ConsLoAdjacencyList(new AdjacencyList(data.jake), data.graphAdjacencyList));
    }
    
    void testConsLoAdjacencyListAddEdge(Tester t) {
        ExampleData data = new ExampleData();
        ConsLoAdjacencyList lists = new ConsLoAdjacencyList(new AdjacencyList(data.kim, new ConsLoEdge(data.kimToLen, new MtLoEdge())), new MtLoAdjacencyList());
        t.checkExpect(lists.addEdge(data.kim, data.jan),
                new ConsLoAdjacencyList(data.kimAdjacencyList, new MtLoAdjacencyList()));
    }
    
    void testConsLoAdjacencyListAddEdgesMt(Tester t) {
        ExampleData data = new ExampleData();
        ILoAdjacencyList testResult = data.graphAdjacencyList.addEdgesMt(data.kim, new MTLoBuddy());
        t.checkExpect(testResult, data.graphAdjacencyList);
    }
    
    void testConsLoAdjacencyListAddEdgesCons(Tester t) {
        ExampleData data = new ExampleData();
        ILoAdjacencyList testResult = data.unfinishAdjacencyList.addEdgesCons(data.kim, new ConsLoBuddy(data.len, new ConsLoBuddy(data.jan, new MTLoBuddy())));
        t.checkExpect(testResult, data.graphAdjacencyList);
    }
    
    void testConsLoAdjacencyListAddEdges(Tester t) {
        ExampleData data = new ExampleData();
        ILoAdjacencyList testResult = data.unfinishAdjacencyList.addEdgesCons(data.kim, new ConsLoBuddy(data.len, new ConsLoBuddy(data.jan, new MTLoBuddy())));
        t.checkExpect(testResult, data.graphAdjacencyList);
    }
    
    void testConsLoAdjacencyListGetEdge(Tester t) {
        ExampleData data = new ExampleData();
        t.checkExpect(data.graphAdjacencyList.getEdge(data.kim, data.jan), data.kimToJan);
    }
    
    void testConsLoAdjacencyListGetAllEdges(Tester t) {
        ExampleData data = new ExampleData();
        t.checkExpect(data.graphAdjacencyList.getAllEdges(data.kim), data.kimEdges);
    }
    
    void testConsLoDoubleMaxHelper(Tester t) {
        ExampleData data = new ExampleData();
        t.checkExpect(data.listOfNums.maxHelper(1.2), 1.2);
        t.checkExpect(data.listOfNums.maxHelper(0.1), 0.9);
    }
    
    void testConsLoDoubleMax(Tester t) {
        ExampleData data = new ExampleData();
        t.checkExpect(data.listOfNums.max(), 0.9);
    }
    
    void testConsLoStackMapConvertListToWeights(Tester t) {

    }
    
    void testMtLoBuddyContains(Tester t) {
        ExampleData data = new ExampleData();
        t.checkExpect((new MTLoBuddy()).contains(data.bob), false);
    }

    void testMtLoBuddyRemoveDuplicates(Tester t) {
        MTLoBuddy list = new MTLoBuddy();
        t.checkExpect(list.removeDuplicates(), list);
    }
    
    void testMtLoBuddyFilter(Tester t) {
        ExampleData data = new ExampleData();
        MTLoBuddy emptyList = new MTLoBuddy();
        t.checkExpect(emptyList.filter(new DuplicatePersonPred(data.anne)), emptyList);
        t.checkExpect(emptyList.filter(
                new DuplicatePeoplePred(new ConsLoBuddy(data.anne, new ConsLoBuddy(data.bob, new MTLoBuddy())))),
                emptyList);
        t.checkExpect(emptyList.filter(new ExtractDuplicatePeoplePred(new ConsLoBuddy(data.anne, new ConsLoBuddy(data.bob, new MTLoBuddy())))), emptyList);
    }
    
    void testMtLoBuddyAppendILoBuddy(Tester t) {
        ExampleData data = new ExampleData();
        t.checkExpect((new MTLoBuddy()).append(data.anne.buddies), data.anne.buddies);
        t.checkExpect((new MTLoBuddy()).append(new MTLoBuddy()), new MTLoBuddy());
    }
    
    void testMtLoBuddyAppendPerson(Tester t) {
        ExampleData data = new ExampleData();
        t.checkExpect((new MTLoBuddy()).append(data.anne), new ConsLoBuddy(data.anne, new MTLoBuddy()));
    }
    
    void testMtLoBuddyFoldrExtendedBuddyCombinator(Tester t) {
        t.checkExpect((new MTLoBuddy()).foldr(new MTLoBuddy(), new ExtendedBuddyCombinator()), new MTLoBuddy());
    }
    
    void testMtLoBuddyFoldrPartyCountCombinator(Tester t) {
        t.checkExpect((new MTLoBuddy()).foldr(new MTLoBuddy(), new PartyCountCombinator()), new MTLoBuddy());
    }
    
    void testMtLoBuddyFoldrConvertToGraphCombinator(Tester t) {
        DirectedWeightedGraph tempGraph = new DirectedWeightedGraph();
        t.checkExpect((new MTLoBuddy()).foldr(tempGraph, new ConvertToGraphCombinator()), tempGraph);
    }
    
    void testMtLoBuddyLength(Tester t) {
        t.checkExpect((new MTLoBuddy()).length(), 0);
    }
    
    void testMtLoBuddyIsEmpty(Tester t) {
        t.checkExpect((new MTLoBuddy()).isEmpty(), true);
    }
    
    void testMtLoBuddyAddEdges(Tester t) {
        ExampleData data = new ExampleData();
        t.checkExpect((new MTLoBuddy()).addEdges(data.graphAdjacencyList, data.anne), data.graphAdjacencyList);
    }
    
    void testMtLoEdgeGet(Tester t) {
        ExampleData data = new ExampleData();
        t.checkException(new IllegalArgumentException(), (new MtLoEdge()), "get", data.anne);
    }
    
    void testMtLoEdgeCopyStack(Tester t) {
        ExampleData data = new ExampleData();
        Stack temp = new Stack();
        (new MtLoEdge()).copyStack(data.graph, temp);
        t.checkExpect(temp.stack, new MtLoEdge());
    }
    
    void testMtLoEdgeFindAllPaths(Tester t) {

    }
    
    void testMtLoEdgePop(Tester t) {
        ExampleData data = new ExampleData();
        (new MtLoEdge()).pop(data.pathKimToJan);
        t.checkException(new IllegalAccessException(), (new MtLoEdge()), "pop", data.pathKimToJan);
    }
    
    void testMtLoEdgeIsEmpty(Tester t) {
        t.checkExpect((new MtLoEdge()).isEmpty(), true);
    }
    
    void testMtLoEdgeContains(Tester t) {
        ExampleData data = new ExampleData();
        t.checkExpect((new MtLoEdge()).contains(data.janToKim), false);
    }
    
    void testMtLoEdgeAppend(Tester t) {
        ExampleData data = new ExampleData();
        t.checkExpect((new MtLoEdge()).append(data.janEdges), data.janEdges);
    }
    
    void testMtLoEdgeFoldrMultiplyWeightsCombinator(Tester t) {
        t.checkExpect((new MtLoEdge()).foldr(1, new MultiplyWeightsCombinator()), 1);
    }
    
    void testMtLoAdjacencyListContains(Tester t) {
        ExampleData data = new ExampleData();
        t.checkExpect((new MtLoAdjacencyList()).contains(data.hank), false);
    }
    
    void testMtLoAdjacencyListAddNode(Tester t) {
        ExampleData data = new ExampleData();
        t.checkExpect((new MtLoAdjacencyList()).addNode(data.cole), new ConsLoAdjacencyList(new AdjacencyList(data.cole), new MtLoAdjacencyList()));
    }
    
    void testMtLoAdjacencyListGet(Tester t) {
        ExampleData data = new ExampleData();
        t.checkException(new IllegalArgumentException(), (new MtLoAdjacencyList()), "get", data.dan);
    }
    
    void testMtLoAdjacencyListAddEdge(Tester t) {
        ExampleData data = new ExampleData();
        t.checkExpect((new MtLoAdjacencyList()).addEdge(data.kim, data.anne), new MtLoAdjacencyList());
    }
    
    void testMtLoAdjacencyListAddEdges(Tester t) {
        ExampleData data = new ExampleData();
        t.checkExpect((new MtLoAdjacencyList()).addEdges(data.kim, data.anne.buddies), new MtLoAdjacencyList());
    }
    
    void testMtLoAdjacencyListAddEdgesCons(Tester t) {
        ExampleData data = new ExampleData();
        t.checkExpect((new MtLoAdjacencyList()).addEdgesCons(data.kim, new ConsLoBuddy(data.anne, new ConsLoBuddy(data.hank, new MTLoBuddy()))), new MtLoAdjacencyList());
    }
    
    void testMtLoAdjacencyListAddEdgesMt(Tester t) {
        ExampleData data = new ExampleData();
        t.checkExpect((new MtLoAdjacencyList()).addEdgesMt(data.kim, new MTLoBuddy()), new MtLoAdjacencyList());
    }
    
    void testMtLoAdjacencyListGetEdge(Tester t) {
        ExampleData data = new ExampleData();
        t.checkException(new IllegalArgumentException(), (new MtLoAdjacencyList()), "getEdge", data.kim, data.dan);
    }
    
    void testMtLoAdjacencyListGetAllEdges(Tester t) {
        ExampleData data = new ExampleData();
        t.checkException(new IllegalArgumentException(), (new MtLoAdjacencyList()), "getAllEdges", data.kim);
    }
    
    void testMtLoDoubleMaxHelper(Tester t) {
        t.checkExpect((new MtLoDouble()).maxHelper(2), 2);
    }

    void testMtLoDoubleMax(Tester t) {
        t.checkException(new IllegalAccessException(), (new MtLoDouble()), "max");
    }
    
    void testMtLoStackMapConvertListToLikelihoods(Tester t) {
        t.checkExpect((new MtLoStack()).map(new ConvertListToLikelihoods()), new MtLoDouble());
    }

    void testDstEquals(Tester t) {
        ExampleData data = new ExampleData();
        t.checkExpect(data.kimToJan.dstEquals(data.jan), true);
        t.checkExpect(data.kimToJan.dstEquals(data.dan), false);
    }

    void testSrcEquals(Tester t) {
        ExampleData data = new ExampleData();
        t.checkExpect(data.kimAdjacencyList.srcEquals(data.kim), true);
        t.checkExpect(data.kimAdjacencyList.srcEquals(data.jake), false);
    }

    void testStackIsEmpty(Tester t) {
        ExampleData data = new ExampleData();
        t.checkExpect(data.pathKimToJan.isEmpty(), false);
        t.checkExpect((new Stack()).isEmpty(), true);
    }

    void testStackContains(Tester t) {
        ExampleData data = new ExampleData();
        t.checkExpect(data.pathKimToJan.contains(data.kimToLen), true);
        t.checkExpect(data.pathKimToJan.contains(data.janToLen), false);
    }

    void testStackPushEdge(Tester t) {
        ExampleData data = new ExampleData();
        Stack tempStack = new Stack();
        tempStack.push(data.kimToLen);
        tempStack.push(data.lenToJan);
        t.checkExpect(tempStack, data.pathKimToJan);
    }

    void testStackPushILoEdge(Tester t) {
        ExampleData data = new ExampleData();
        Stack tempStack = new Stack();
        tempStack.push(new ConsLoEdge(data.lenToJan, new ConsLoEdge(data.kimToLen, new MtLoEdge())));
        t.checkExpect(tempStack, data.pathKimToJan);
    }

    void testStackPop(Tester t) {
        ExampleData data = new ExampleData();
        Stack tempStack = new Stack();
        tempStack.push(data.kimToLen);
        t.checkExpect(data.pathKimToJan.pop(), data.lenToJan);
        t.checkExpect(data.pathKimToJan, tempStack);
    }

    void testStackPopCons(Tester t) {
        ExampleData data = new ExampleData();
        Stack tempStack = new Stack();
        ConsLoEdge tempEdges = new ConsLoEdge(data.lenToJan, new ConsLoEdge(data.kimToLen, new MtLoEdge()));
        tempStack.push(data.kimToLen);

        t.checkExpect(data.pathKimToJan.popCons(tempEdges), data.lenToJan);
        t.checkExpect(data.pathKimToJan, tempStack);
    }

    void testStackPopMt(Tester t) {
        ExampleData data = new ExampleData();
        t.checkException(new IllegalAccessException(), data.pathKimToJan, "popMt", (new MtLoEdge()));
    }
    
    void testDWGContains(Tester t) {
        ExampleData data = new ExampleData();
        t.checkExpect(data.graph.contains(data.kim), true);
        t.checkExpect(data.graph.contains(new Person("king")), false);
    }

    void testDWGAddEdge(Tester t) {
        ExampleData data = new ExampleData();

        DirectedWeightedGraph actualResult = new DirectedWeightedGraph();
        actualResult = data.anne.buddies.append(data.anne).foldr(data.graph, new ConvertToGraphCombinator());
        actualResult = actualResult.addEdge(data.kim, data.cole);

        data.kim.addBuddy(data.cole);
        DirectedWeightedGraph expectedResult = new DirectedWeightedGraph();
        expectedResult = data.anne.buddies.append(data.anne).foldr(data.graph, new ConvertToGraphCombinator());

        t.checkExpect(actualResult, expectedResult);
    }

    void testDWGAddEdges(Tester t) {
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
    
    void testDWGGetEdge(Tester t) {
        ExampleData data = new ExampleData();
        t.checkExpect(data.graph.getEdge(data.kim, data.jan), data.kimToJan);
    }
    
    void testDWGGetAllEdges(Tester t) {
        ExampleData data = new ExampleData();
        t.checkExpect(data.graph.getAllEdges(data.kim), data.kimEdges);
    }
    
    void testDWGCopyStackMt(Tester t) {
        ExampleData data = new ExampleData();
        Stack tempStack = new Stack();
        data.graph.copyStackMt(tempStack, new MtLoEdge());
        t.checkExpect(tempStack, new Stack());
    }
    
    void testDWGCopyStackCons(Tester t) {
        ExampleData data = new ExampleData();
        Stack tempStack = new Stack();
        ConsLoEdge edges = new ConsLoEdge(data.lenToJan, new ConsLoEdge(data.kimToLen, new MtLoEdge()));
        data.graph.copyStackCons(tempStack, edges);
        t.checkExpect(tempStack, data.pathKimToJan);
    }
    
    void testDWGCopyStack(Tester t) {
        ExampleData data = new ExampleData();
        Stack tempStack = new Stack();
        data.graph.copyStack(tempStack, data.pathKimToJan.stack);
        t.checkExpect(tempStack, data.pathKimToJan);
    }
    
    void testDWGFindAllPathsHelper(Tester t) {

    }
    
    void testDWGFindAllPathsCons(Tester t) {

    }
    
    void testDWGFindAllPathsMt(Tester t) {

    }
    
    void testDWGFindAllPaths(Tester t) {

    }
    
    void testAddBuddy(Tester t) {
        ExampleData data = new ExampleData();
        ILoBuddy before = data.kim.buddies;
        data.kim.addBuddy(data.dan);
        t.checkExpect(data.kim.buddies, before.append(data.dan));
    }

    void testHasDirectBuddy(Tester t) {
        ExampleData data = new ExampleData();
        t.checkExpect(data.anne.hasDirectBuddy(data.bob), true);
        t.checkExpect(data.anne.hasDirectBuddy(data.len), false);
    }

    void testAllPartyInvites(Tester t) {

    }

    void testPartyCount(Tester t) {
        ExampleData data = new ExampleData();
        t.checkExpect(data.anne.partyCount(), 9);
    }

    void testCountCommonBuddies(Tester t) {
        ExampleData data = new ExampleData();
        t.checkExpect(data.kim.countCommonBuddies(data.jan), 1);
    }

    void testHasExtendedBuddy(Tester t) {

    }

    void testMaximumLikelihood(Tester t) {
        
    }
}