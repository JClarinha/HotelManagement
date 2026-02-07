public class Room {
    private final int id;
    private final int number;
    private final int capacity;

    public Room(int id, int number, int capacity) {
        this.id = id;
        this.number = number;
        this.capacity = capacity;
    }

    public int getId() { return id; }
    public int getNumber() { return number; }
    public int getCapacity() { return capacity; }

    @Override
    public String toString() {
        return "Room{id=" + id + ", number=" + number + ", capacity=" + capacity + "}";
    }
}
