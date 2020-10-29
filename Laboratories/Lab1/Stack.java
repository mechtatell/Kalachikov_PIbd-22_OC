package Lab1;

public class Stack {

    private final Object[] massive;
    private int size;

    public boolean push(Object element) {
        if (size < massive.length) {
            massive[size] = element;
            size++;
            return true;
        } else {
            return false;
        }
    }

    public Object pop() {
        if (!isEmpty()) {
            size--;
            return massive[size];
        } else {
            return null;
        }
    }

    public Object peek() {
        if (!isEmpty()) {
            return massive[size - 1];
        } else {
            return null;
        }
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void clear() {
        while (!isEmpty()) {
            pop();
        }
    }

    public Stack(int count) {
        massive = new Object[count];
        size = 0;
    }
}
