class Pair {
    Person child;
    Person parent;

    Pair(Person child, Person parent) {
        this.child = child;
        this.parent = parent;
    }
}

interface ILoPair {
    Pair getPair(Person child);

    boolean contains(Person child);

    boolean isEmpty();

    ILoPair replace(Person child, Person parent);
}

class ConsLoPair implements ILoPair {
    Pair first;
    ILoPair rest;

    ConsLoPair(Pair first, ILoPair rest) {
        this.first = first;
        this.rest = rest;
    }

    public Pair getPair(Person child) {
        if (first.child.equals(child)) {
            return first;
        } else {
            return rest.getPair(child);
        }
    }

    public boolean contains(Person child) {
        return this.first.child.equals(child) || this.rest.contains(child);
    }

    public boolean isEmpty() {
        return false;
    }

    public ILoPair replace(Person child, Person parent) {
        if (this.first.child.equals(child)) {
            return new ConsLoPair(new Pair(child, parent), rest.replace(child, parent));
        }
        else {
            return new ConsLoPair(first, rest.replace(child, parent));
        }
    }
}

class MtLoPair implements ILoPair {
    public Pair getPair(Person child) {
        return null;
    }

    public boolean contains(Person child) {
        return false;
    }

    public boolean isEmpty() {
        return true;
    }

    public ILoPair replace(Person child, Person parent) {
        return new MtLoPair();
    }
}

public class Dictionary {
    ILoPair dict;

    class MtLoStack implements ILoStack {
        public ILoDouble map(ConvertListToWeights func) {
            return new MtLoDouble();
        }
    }

    public void add(ILoBuddy people, Person parent) {
        people.addToDict(this, parent);
    }
    
    public void addCons(ConsLoBuddy people, Person parent) {
        if (dict.isEmpty()) {
            dict = people.map(new PersonToPair(parent));
        } else if (dict.contains(people.first)) {
            if (people.first.equals(parent)) {
                dict = dict.replace(people.first, null);
                add(people.rest, parent);
            } else {
                dict = dict.replace(people.first, parent);
                add(people.rest, parent);
            }
        } else {
            if (people.first.equals(parent)) {
                dict = new ConsLoPair(new Pair(people.first, null), dict);
                add(people.rest, parent);
            } else {
                dict = new ConsLoPair(new Pair(people.first, parent), dict);
                add(people.rest, parent);
            }
        }
    }
    
    public void addMt(MTLoBuddy people, Person parent) {
        return;
    }

    public Person getParent(Person child) {
        Pair result = dict.getPair(child);
        if (result == null) {
            return null;
        }
        else {
            return result.parent;
        }
    }
}
