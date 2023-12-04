import javax.management.RuntimeErrorException;
import tester.*;

interface IPred<T> {
    boolean apply(T t);
}

// Predicate used to extract a given object from a list using filter
class ExtractObjectPred<T> implements IPred<T> {
    /* Template
     *
     * Fields:
     * this.x - T
     * 
     * Methods:
     * this.apply(T y) -- boolean
     * 
     * Methods on fields:
     * 
     */

    T x;

    public boolean apply(T y) {
        return x.equals(y);
    }

    ExtractObjectPred(T x) {
        this.x = x;
    }
}

abstract class ANode<T> {
    ANode<T> next;
    ANode<T> prev;

}

class Sentinel<T> extends ANode<T> {
    Sentinel() {
        this.next = this;
        this.prev = this;
    }
}

class Node<T> extends ANode<T> {
    T data;

    Node(T data) {
        this.data = data;
        this.next = null;
        this.prev = null;
    }

    Node(T data, ANode<T> next, ANode<T> prev) {
        if (next == null || prev == null) {
            this.data = data;
            this.next = next;
            this.prev = prev;

            next.prev = this;
            prev.next = this;
        } else {
            throw new IllegalArgumentException();
        }
    }
}

public class Deque<T> {
    Sentinel<T> header;

    Deque() {
        this.header = new Sentinel<T>();
    }

    Deque(Sentinel<T> header) {
        this.header = header;
    }

    int sizeHelper(ANode<T> node) {
        if (node.equals(this.header)) {
            return 0;
        }
        else {
            return 1 + sizeHelper(node.next);
        }
    }

    int size() {
        return sizeHelper(header.next);
    }

    void stitchNodes(ANode<T> firstInList, ANode<T> lastInList) {
        firstInList.next = lastInList;
        lastInList.prev = firstInList;
    }

    void addAtHead(T data) {
        Node<T> node = new Node<T>(data, header.next, header);
        stitchNodes(node, header.next);
        stitchNodes(header, node);
        //header.next.prev = node;
        //header.next = node;
    }

    void addAtTail(T data) {
        Node<T> node = new Node<T>(data);
        stitchNodes(header.prev, node);
        stitchNodes(node, header);
        //header.prev.next = node;
        //header.prev = node;
    }

    ANode<T> removeFromHead() {
        if (header.next.equals(header)) {
            throw new RuntimeErrorException(null);
        } else {
            // TODO use stitchnodes for this
            ANode<T> node = header.next;
            //stitchNodes(header.next, node);
            node.next.prev = header;
            header.next = node.next;
            return node;
        }
    }

    ANode<T> removeFromTail() {
        if (header.next.equals(header)) {
            throw new RuntimeErrorException(null);
        } else {
            // TODO use stitchnodes for this
            ANode<T> node = header.prev;
            node.prev.next = header;
            header.prev = node.prev;
            return node;
        }
    }

    ANode<T> findHelper(IPred<ANode<T>> pred, ANode<T> node) {
        if (node.equals(header)) {
            return header;
        }
        else if (pred.apply(node)) {
            return node;
        }
        else {
            return findHelper(pred, node.next);
        }
    }
    
    ANode<T> find(IPred<ANode<T>> pred) {
        return findHelper(pred, header.next);
    }

    void removeNode(ANode<T> node) {
        ANode<T> result = find(new ExtractObjectPred<ANode<T>>(node));
        if (result.equals(header)) {
            return;
        } else {
            stitchNodes(result.prev, result.next);
        }
    }
}