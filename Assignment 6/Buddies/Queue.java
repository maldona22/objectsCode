public class Queue {
    ILoBuddy queue;

    Queue(ILoBuddy queue) {
        this.queue = queue;
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }

    public Person peek() {
        if (queue instanceof ConsLoBuddy) {
            return ((ConsLoBuddy) queue).first;
        }
        else {
            throw new IllegalAccessError();
        }
    }

    public Queue enqueue(Person person) {
        return new Queue(queue.append(person));
    }

     public Queue enqueue(ILoBuddy people) {
        return new Queue(queue.append(people));
    }

    public Queue dequeue() {
        if (queue instanceof ConsLoBuddy) {
            return new Queue(((ConsLoBuddy) queue).rest);
        }
        else {
            throw new IllegalAccessError();
        }
    }
}
