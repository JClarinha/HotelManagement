import java.time.LocalDate;

public class Reservation {
    private final int id;
    private final int roomId;
    private final int guestId;
    private final int numberOfGuests;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private boolean active;

    public Reservation(int id, int roomId, int guestId, int numberOfGuests, LocalDate startDate, LocalDate endDate) {
        this.id = id;
        this.roomId = roomId;
        this.guestId = guestId;
        this.numberOfGuests = numberOfGuests;
        this.startDate = startDate;
        this.endDate = endDate;
        this.active = true;
    }

    public int getId() { return id; }
    public int getRoomId() { return roomId; }
    public int getGuestId() { return guestId; }
    public int getNumberOfGuests() { return numberOfGuests; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public boolean isActive() { return active; }

    public void cancel() { this.active = false; }

    @Override
    public String toString() {
        return "Reservation{id=" + id +
                ", roomId=" + roomId +
                ", guestId=" + guestId +
                ", guests=" + numberOfGuests +
                ", start=" + startDate +
                ", end=" + endDate +
                ", active=" + active +
                '}';
    }
}
