public class IOData {
    public void setIndex(int index) {
        this.index = index;
    }

    private int index;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    private int count;

    public IOData(int index, int count) {
        this.index = index;
        this.count = count;
    }

    public int getIndex() {
        return index;
    }

    public String toString() {
        return index + " " + count;
    }

    public void addCount(int count) {
        this.count += count;
    }
}
