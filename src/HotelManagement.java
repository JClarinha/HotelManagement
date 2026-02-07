import java.io.File;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

public class HotelManagement {

    // ===== LIMITES =====
    static final int MAX_ROOMS = 200;
    static final int MAX_GUESTS = 500;
    static final int MAX_RESERVATIONS = 1000;

    // ===== FICHEIROS =====
    static final String ROOMS_CSV = "quartos.csv";
    static final String GUESTS_CSV = "hospedes.csv";
    static final String RESERVATIONS_CSV = "reservas.csv";

    static Scanner scanner = new Scanner(System.in);

    // ===== STORAGE (arrays + contadores) =====
    static Room[] rooms = new Room[MAX_ROOMS];
    static int roomCount = 0;
    static int nextRoomId = 1;

    static Guest[] guests = new Guest[MAX_GUESTS];
    static int guestCount = 0;
    static int nextGuestId = 1;

    static Reservation[] reservations = new Reservation[MAX_RESERVATIONS];
    static int reservationCount = 0;
    static int nextReservationId = 1;

    public static void main(String[] args) {

        // carregar ficheiros ao arrancar
        loadRooms();
        loadGuests();
        loadReservations();

        int option;
        do {
            System.out.println("\n===== HOTEL MANAGEMENT =====");
            System.out.println("1 - Rooms");
            System.out.println("2 - Guests");
            System.out.println("3 - Reservations");
            System.out.println("0 - Exit");

            option = readInt();

            switch (option) {
                case 1 -> roomMenu();
                case 2 -> guestMenu();
                case 3 -> reservationMenu();
                case 0 -> System.out.println("Goodbye!");
                default -> System.out.println("Invalid option.");
            }

        } while (option != 0);
    }

    // =========================
    // INPUT HELPERS
    // =========================
    static int readInt() {
        while (true) {
            try {
                String line = scanner.nextLine();
                if (line.isBlank()) continue;
                return Integer.parseInt(line.trim());
            } catch (NumberFormatException e) {
                System.out.print("Invalid number. Try again: ");
            }
        }
    }

    static String readLine() {
        String s;
        do {
            s = scanner.nextLine().trim();
        } while (s.isEmpty());
        return s;
    }

    static LocalDate readDate(String msg) {
        while (true) {
            System.out.print(msg);
            String raw = readLine();
            try {
                return LocalDate.parse(raw);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date. Use format YYYY-MM-DD.");
            }
        }
    }

    // =========================
    // MENUS
    // =========================
    static void roomMenu() {
        int op;
        do {
            System.out.println("\n--- ROOM MENU ---");
            System.out.println("1 - Add Room");
            System.out.println("2 - List Rooms");
            System.out.println("3 - Remove Room");
            System.out.println("0 - Back");

            op = readInt();

            switch (op) {
                case 1 -> addRoom();
                case 2 -> listRooms();
                case 3 -> removeRoom();
                case 0 -> { }
                default -> System.out.println("Invalid option.");
            }
        } while (op != 0);
    }

    static void guestMenu() {
        int op;
        do {
            System.out.println("\n--- GUEST MENU ---");
            System.out.println("1 - Add Guest");
            System.out.println("2 - List Guests");
            System.out.println("3 - Remove Guest");
            System.out.println("0 - Back");

            op = readInt();

            switch (op) {
                case 1 -> addGuest();
                case 2 -> listGuests();
                case 3 -> removeGuest();
                case 0 -> { }
                default -> System.out.println("Invalid option.");
            }
        } while (op != 0);
    }

    static void reservationMenu() {
        int op;
        do {
            System.out.println("\n--- RESERVATION MENU ---");
            System.out.println("1 - Create Reservation");
            System.out.println("2 - List All Reservations");
            System.out.println("3 - List Reservations By Room");
            System.out.println("4 - List Reservations By Guest");
            System.out.println("5 - Cancel Reservation");
            System.out.println("0 - Back");

            op = readInt();

            switch (op) {
                case 1 -> createReservation();
                case 2 -> listAllReservations();
                case 3 -> listReservationsByRoom();
                case 4 -> listReservationsByGuest();
                case 5 -> cancelReservation();
                case 0 -> { }
                default -> System.out.println("Invalid option.");
            }
        } while (op != 0);
    }

    // =========================
    // ROOMS
    // =========================
    static void addRoom() {
        if (roomCount >= MAX_ROOMS) {
            System.out.println("Room limit reached.");
            return;
        }

        int id = nextRoomId++;

        System.out.print("Room number: ");
        int number = readInt();

        System.out.print("Capacity: ");
        int capacity = readInt();

        if (capacity < 1) {
            System.out.println("Capacity must be >= 1.");
            return;
        }

        rooms[roomCount++] = new Room(id, number, capacity);
        saveRooms();

        System.out.println("Room added with ID: " + id);
    }

    static void listRooms() {
        if (roomCount == 0) {
            System.out.println("No rooms.");
            return;
        }
        for (int i = 0; i < roomCount; i++) {
            System.out.println(rooms[i]);
        }
    }

    static void removeRoom() {
        System.out.print("Room ID to remove: ");
        int id = readInt();

        Room room = findRoomById(id);
        if (room == null) {
            System.out.println("Room not found.");
            return;
        }

        // não remover se tiver reserva ativa
        for (int i = 0; i < reservationCount; i++) {
            Reservation r = reservations[i];
            if (r.isActive() && r.getRoomId() == id) {
                System.out.println("Cannot remove: room has active reservations.");
                return;
            }
        }

        deleteRoomById(id);
        saveRooms();
        System.out.println("Room removed.");
    }

    static Room findRoomById(int id) {
        for (int i = 0; i < roomCount; i++) {
            if (rooms[i].getId() == id) return rooms[i];
        }
        return null;
    }

    static void deleteRoomById(int id) {
        for (int i = 0; i < roomCount; i++) {
            if (rooms[i].getId() == id) {
                for (int j = i; j < roomCount - 1; j++) rooms[j] = rooms[j + 1];
                rooms[roomCount - 1] = null;
                roomCount--;
                return;
            }
        }
    }

    // =========================
    // GUESTS
    // =========================
    static void addGuest() {
        if (guestCount >= MAX_GUESTS) {
            System.out.println("Guest limit reached.");
            return;
        }

        int id = nextGuestId++;

        System.out.print("Name: ");
        String name = readLine();

        System.out.print("Email: ");
        String email = readLine();

        System.out.print("Contact: ");
        int contact = readInt();

        System.out.print("Document type: ");
        String type = readLine();

        System.out.print("Document number: ");
        int docNumber = readInt();

        guests[guestCount++] = new Guest(id, name, email, contact, type, docNumber);

        saveGuests();
        System.out.println("Guest added with ID: " + id);
    }

    static void listGuests() {
        if (guestCount == 0) {
            System.out.println("No guests.");
            return;
        }
        for (int i = 0; i < guestCount; i++) {
            System.out.println(guests[i]);
        }
    }

    static void removeGuest() {
        System.out.print("Guest ID to remove: ");
        int id = readInt();

        Guest g = findGuestById(id);
        if (g == null) {
            System.out.println("Guest not found.");
            return;
        }

        // não remover se tiver reserva ativa
        for (int i = 0; i < reservationCount; i++) {
            Reservation r = reservations[i];
            if (r.isActive() && r.getGuestId() == id) {
                System.out.println("Cannot remove: guest has active reservations.");
                return;
            }
        }

        deleteGuestById(id);
        saveGuests();
        System.out.println("Guest removed.");
    }

    static Guest findGuestById(int id) {
        for (int i = 0; i < guestCount; i++) {
            if (guests[i].getId() == id) return guests[i];
        }
        return null;
    }

    static void deleteGuestById(int id) {
        for (int i = 0; i < guestCount; i++) {
            if (guests[i].getId() == id) {
                for (int j = i; j < guestCount - 1; j++) guests[j] = guests[j + 1];
                guests[guestCount - 1] = null;
                guestCount--;
                return;
            }
        }
    }

    // =========================
    // RESERVATIONS
    // =========================
    static void createReservation() {
        if (roomCount == 0 || guestCount == 0) {
            System.out.println("You need at least 1 room and 1 guest first.");
            return;
        }
        if (reservationCount >= MAX_RESERVATIONS) {
            System.out.println("Reservation limit reached.");
            return;
        }

        int id = nextReservationId++;

        System.out.print("Room ID: ");
        int roomId = readInt();
        Room room = findRoomById(roomId);
        if (room == null) {
            System.out.println("Room not found.");
            return;
        }

        System.out.print("Guest ID: ");
        int guestId = readInt();
        Guest guest = findGuestById(guestId);
        if (guest == null) {
            System.out.println("Guest not found.");
            return;
        }

        System.out.print("Number of guests: ");
        int nGuests = readInt();
        if (nGuests < 1 || nGuests > room.getCapacity()) {
            System.out.println("Invalid number of guests (1.." + room.getCapacity() + ").");
            return;
        }

        LocalDate start = readDate("Start date (YYYY-MM-DD): ");
        LocalDate end = readDate("End date (YYYY-MM-DD): ");

        if (start.isAfter(end)) {
            System.out.println("Invalid dates: start > end.");
            return;
        }

        if (hasConflict(roomId, start, end, -1)) {
            System.out.println("Conflict: room already booked for those dates.");
            return;
        }

        reservations[reservationCount++] = new Reservation(id, roomId, guestId, nGuests, start, end);
        saveReservations();
        System.out.println("Reservation created with ID: " + id);
    }

    static void listAllReservations() {
        if (reservationCount == 0) {
            System.out.println("No reservations.");
            return;
        }
        for (int i = 0; i < reservationCount; i++) {
            System.out.println(reservations[i]);
        }
    }

    static void listReservationsByRoom() {
        System.out.print("Room ID: ");
        int roomId = readInt();

        LocalDate today = LocalDate.now();
        boolean found = false;

        for (int i = 0; i < reservationCount; i++) {
            Reservation r = reservations[i];
            if (r.isActive() && r.getRoomId() == roomId && !r.getEndDate().isBefore(today)) {
                System.out.println(r);
                found = true;
            }
        }

        if (!found) System.out.println("No present/future reservations for that room.");
    }

    static void listReservationsByGuest() {
        System.out.print("Guest ID: ");
        int guestId = readInt();

        LocalDate today = LocalDate.now();
        boolean found = false;

        for (int i = 0; i < reservationCount; i++) {
            Reservation r = reservations[i];
            if (r.isActive() && r.getGuestId() == guestId && !r.getEndDate().isBefore(today)) {
                System.out.println(r);
                found = true;
            }
        }

        if (!found) System.out.println("No present/future reservations for that guest.");
    }

    static void cancelReservation() {
        System.out.print("Reservation ID to cancel: ");
        int id = readInt();

        Reservation r = findReservationById(id);
        if (r == null) {
            System.out.println("Reservation not found.");
            return;
        }
        if (!r.isActive()) {
            System.out.println("Reservation already cancelled.");
            return;
        }

        r.cancel();
        saveReservations();
        System.out.println("Reservation cancelled.");
    }

    static Reservation findReservationById(int id) {
        for (int i = 0; i < reservationCount; i++) {
            if (reservations[i].getId() == id) return reservations[i];
        }
        return null;
    }

    static boolean hasConflict(int roomId, LocalDate start, LocalDate end, int ignoreReservationId) {
        for (int i = 0; i < reservationCount; i++) {
            Reservation r = reservations[i];
            if (!r.isActive()) continue;
            if (r.getRoomId() != roomId) continue;
            if (r.getId() == ignoreReservationId) continue;

            boolean overlap = !start.isAfter(r.getEndDate()) && !end.isBefore(r.getStartDate());
            if (overlap) return true;
        }
        return false;
    }

    // =========================
    // CSV HELPERS
    // =========================
    static String escapeCsv(String s) {
        if (s == null) return "";
        if (s.contains(",") || s.contains("\"")) {
            s = s.replace("\"", "\"\"");
            return "\"" + s + "\"";
        }
        return s;
    }

    static String[] splitCsvLine(String line) {
        ArrayList<String> out = new ArrayList<>();
        StringBuilder cur = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            if (c == '"') {
                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    cur.append('"');
                    i++;
                } else {
                    inQuotes = !inQuotes;
                }
            } else if (c == ',' && !inQuotes) {
                out.add(cur.toString());
                cur.setLength(0);
            } else {
                cur.append(c);
            }
        }
        out.add(cur.toString());
        return out.toArray(new String[0]);
    }

    // =========================
    // CSV ROOMS
    // =========================
    static void saveRooms() {
        try (PrintWriter pw = new PrintWriter(ROOMS_CSV)) {
            pw.println("id,number,capacity");
            for (int i = 0; i < roomCount; i++) {
                Room r = rooms[i];
                pw.printf("%d,%d,%d%n", r.getId(), r.getNumber(), r.getCapacity());
            }
        } catch (Exception e) {
            System.out.println("Error saving rooms: " + e.getMessage());
        }
    }

    static void loadRooms() {
        roomCount = 0;
        nextRoomId = 1;

        File f = new File(ROOMS_CSV);
        if (!f.exists()) return;

        try (Scanner fileScanner = new Scanner(f)) {
            if (fileScanner.hasNextLine()) fileScanner.nextLine(); // header

            while (fileScanner.hasNextLine() && roomCount < MAX_ROOMS) {
                String line = fileScanner.nextLine().trim();
                if (line.isEmpty()) continue;

                String[] p = splitCsvLine(line);
                if (p.length < 3) continue;

                int id = Integer.parseInt(p[0].trim());
                int number = Integer.parseInt(p[1].trim());
                int cap = Integer.parseInt(p[2].trim());

                rooms[roomCount++] = new Room(id, number, cap);
                if (id >= nextRoomId) nextRoomId = id + 1;
            }
        } catch (Exception e) {
            System.out.println("Error loading rooms: " + e.getMessage());
        }
    }

    // =========================
    // CSV GUESTS
    // =========================
    static void saveGuests() {
        try (PrintWriter pw = new PrintWriter(GUESTS_CSV)) {
            pw.println("id,name,email,contact,documentType,documentNumber");

            for (int i = 0; i < guestCount; i++) {
                Guest g = guests[i];
                pw.printf("%d,%s,%s,%d,%s,%d%n",
                        g.getId(),
                        escapeCsv(g.getName()),
                        escapeCsv(g.getEmail()),
                        g.getContact(),
                        escapeCsv(g.getTypeOfDocument()),
                        g.getDocumentNumber()
                );
            }
        } catch (Exception e) {
            System.out.println("Error saving guests: " + e.getMessage());
        }
    }

    static void loadGuests() {
        guestCount = 0;
        nextGuestId = 1;

        File f = new File(GUESTS_CSV);
        if (!f.exists()) return;

        try (Scanner fileScanner = new Scanner(f)) {
            if (fileScanner.hasNextLine()) fileScanner.nextLine(); // header

            while (fileScanner.hasNextLine() && guestCount < MAX_GUESTS) {
                String line = fileScanner.nextLine().trim();
                if (line.isEmpty()) continue;

                String[] parts = splitCsvLine(line);

                try {
                    if (parts.length >= 6) {
                        int id = Integer.parseInt(parts[0].trim());
                        String name = parts[1].trim();
                        String email = parts[2].trim();
                        int contact = Integer.parseInt(parts[3].trim());
                        String docType = parts[4].trim();
                        int docNumber = Integer.parseInt(parts[5].trim());

                        guests[guestCount++] = new Guest(id, name, email, contact, docType, docNumber);
                        if (id >= nextGuestId) nextGuestId = id + 1;
                    }
                } catch (Exception ignore) { }
            }
        } catch (Exception e) {
            System.out.println("Error loading guests: " + e.getMessage());
        }
    }

    // =========================
    // CSV RESERVATIONS
    // =========================
    static void saveReservations() {
        try (PrintWriter pw = new PrintWriter(RESERVATIONS_CSV)) {
            pw.println("id,roomId,guestId,numberOfGuests,startDate,endDate,active");

            for (int i = 0; i < reservationCount; i++) {
                Reservation r = reservations[i];
                pw.printf("%d,%d,%d,%d,%s,%s,%b%n",
                        r.getId(),
                        r.getRoomId(),
                        r.getGuestId(),
                        r.getNumberOfGuests(),
                        r.getStartDate().toString(),
                        r.getEndDate().toString(),
                        r.isActive()
                );
            }
        } catch (Exception e) {
            System.out.println("Error saving reservations: " + e.getMessage());
        }
    }

    static void loadReservations() {
        reservationCount = 0;
        nextReservationId = 1;

        File f = new File(RESERVATIONS_CSV);
        if (!f.exists()) return;

        try (Scanner fileScanner = new Scanner(f)) {
            if (fileScanner.hasNextLine()) fileScanner.nextLine(); // header

            while (fileScanner.hasNextLine() && reservationCount < MAX_RESERVATIONS) {
                String line = fileScanner.nextLine().trim();
                if (line.isEmpty()) continue;

                String[] p = splitCsvLine(line);
                if (p.length < 7) continue;

                int id = Integer.parseInt(p[0].trim());
                int roomId = Integer.parseInt(p[1].trim());
                int guestId = Integer.parseInt(p[2].trim());
                int nGuests = Integer.parseInt(p[3].trim());
                LocalDate start = LocalDate.parse(p[4].trim());
                LocalDate end = LocalDate.parse(p[5].trim());
                boolean active = Boolean.parseBoolean(p[6].trim());

                Reservation r = new Reservation(id, roomId, guestId, nGuests, start, end);
                if (!active) r.cancel();

                reservations[reservationCount++] = r;

                if (id >= nextReservationId) nextReservationId = id + 1;
            }
        } catch (Exception e) {
            System.out.println("Error loading reservations: " + e.getMessage());
        }
    }
}

