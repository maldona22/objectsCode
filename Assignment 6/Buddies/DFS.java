/*public class DFS {
    private Stack stack;
    private ConsLoBuddy tree;
    private ILoBuddy visited;
    private Dictionary parentMapping;

    DFS(ILoBuddy tree) {
        //stack = new Stack();
        if (tree.isEmpty()) {
            throw new IllegalArgumentException();
        }
        this.tree = (ConsLoBuddy) tree;
        //this.visited = new MTLoBuddy();
    }

    public ILoBuddy tracePath(Person current, ILoBuddy accum) {
        if (!(current == null)) {
            return tracePath(parentMapping.getParent(current), new ConsLoBuddy(current, accum));
        }
        else {
            return accum;
        }
    }
    
    public Person dfsHelper(Person person) {
        if (!stack.isEmpty()) {
            Person parent = stack.pop();
            if (visited.contains(parent)) {
                return dfsHelper(person);
            } else if (person.equals(parent)) {
                return parent;
            }
            else {
                visited = visited.append(parent);
                stack.push(parent.buddies);
                if (parent.buddies.contains(person)) {
                    System.out.println("yeah");
                    System.out.println(parent.username);
                }
                parentMapping.add(parent.buddies.filter(new DuplicatePersonPred(tree.first)), parent);
                return dfsHelper(person);
            }
        }
        else {
            return null;
        }
    }

    public Person depthFirstSearch(Person person) {
        // Initialize a new stack, visited list, for each search through the tree
        stack = new Stack();
        visited = new MTLoBuddy();
        parentMapping = new Dictionary();
        if (person.equals(tree.first)) {
            return person;
        } else {
            stack.push(tree.first);
            parentMapping.add(new ConsLoBuddy(tree.first, new MTLoBuddy()), null);
            return dfsHelper(person);
        }
    }
    
    public void printDict(ILoPair dictionary) {
        if (dictionary instanceof ConsLoPair) {
            Person child = ((ConsLoPair) dictionary).first.child;
            Person parent = ((ConsLoPair) dictionary).first.parent;
            System.out.println("Child: " + child.username);
            if (!(parent == null)) {
                System.out.println("Parent: " + parent.username);
                System.out.println(parent.equals(child));
            }
            else {
                System.out.println("Parent: null");
            }
            System.out.println("----------------------------");
            printDict(((ConsLoPair) dictionary).rest);
        }
        else {
            return;
        }
    }

    public ILoBuddy dfsPath(Person person, Person searching) {
        Person target = depthFirstSearch(person);
        printDict(parentMapping.dict);
        // System.out.println( parentMapping.dict.getPair(searching).child.equals(parentMapping.dict.getPair(searching).parent));
        return tracePath(target, new MTLoBuddy());
    }
}
*/
