import java.io.File;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

public class HotelManagement {

    // Limites
    static final int MAX_ROOMS = 200; // Número máximo de quartos
    static final int MAX_GUESTS = 500; // Número máximo de hóspedes
    static final int MAX_RESERVATIONS = 1000; //Número máximo de reservas

    // Ficheiros CSV
    static final String ROOMS_CSV = "quartos.csv"; // Ficheiro de memória local para os quartos
    static final String GUESTS_CSV = "hospedes.csv"; // Ficheiro de memória local para os hóspedes
    static final String RESERVATIONS_CSV = "reservas.csv"; //Ficheiro de memória local para as reservas

    static Scanner scanner = new Scanner(System.in);

    // Armazenamento e contadores
    static Room[] rooms = new Room[MAX_ROOMS]; // Array de criação e armazenamento dos quartos
    static int roomCount = 0; // Contador para número de quartos
    static int nextRoomId = 1; // Incrementador de ID de quartos

    static Guest[] guests = new Guest[MAX_GUESTS];
    static int guestCount = 0;
    static int nextGuestId = 1;

    static Reservation[] reservations = new Reservation[MAX_RESERVATIONS];
    static int reservationCount = 0;
    static int nextReservationId = 1;

    public static void main(String[] args) {

        // Carrega ficheiros de memória local
        loadRooms();
        loadGuests();
        loadReservations();

        int option;
        do { // Menu Principal
            System.out.println("\n===== Gestão Hoteleira =====");
            System.out.println("1 - Quartos");
            System.out.println("2 - Hóspedes");
            System.out.println("3 - Reservas");
            System.out.println("0 - Sair");

            option = readInt(); // Função que lida com exceções de input

            switch (option) {
                case 1 -> roomMenu();
                case 2 -> guestMenu();
                case 3 -> reservationMenu();
                case 0 -> System.out.println("Adeus!");
                default -> System.out.println("Opção Inválida.");
            }

        } while (option != 0);
    }


    static int readInt() { // Continua a pedir ao utilizador uma opção válida. Lida com exeções.
        while (true) {
            try { // vai ler a linha toda como uma string e se for opção válida converte e retorna a opção como int, caso contrário lança uma exeção que é "catched" e pede novo input ao utilizador.
                String line = scanner.nextLine();
                if (line.isBlank()) continue;
                return Integer.parseInt(line.trim());
            } catch (NumberFormatException e) {
                System.out.print("Número inválido. Por favor tente novamente.: ");
            }
        }
    }

    static String readLine() { // vai continuar a pedir input ao utilizador enquanto a linha estiver vazia ou só tiver espaços
        String s;
        do {
            s = scanner.nextLine().trim();
        } while (s.isEmpty());
        return s;
    }

    static LocalDate readDate(String msg) { // Garante que o formato da data introduzida é válida
        while (true) {
            System.out.print(msg);
            String raw = readLine();
            try {
                return LocalDate.parse(raw);
            } catch (DateTimeParseException e) {
                System.out.println("Data inválida. Use o formato - YYYY-MM-DD.");
            }
        }
    }


    static void roomMenu() {
        int op;
        do {
            System.out.println("\n--- Menu de Quartos ---");
            System.out.println("1 - Adicionar quarto");
            System.out.println("2 - Listar quarto");
            System.out.println("3 - Remover quarto");
            System.out.println("0 - Voltar atrás");

            op = readInt();

            switch (op) {
                case 1 -> addRoom();
                case 2 -> listRooms();
                case 3 -> removeRoom();
                case 0 -> {
                }
                default -> System.out.println("Opção inválida.");
            }
        } while (op != 0);
    }

    static void guestMenu() {
        int op;
        do {
            System.out.println("\n--- Menu de Hóspedes ---");
            System.out.println("1 - Adicionar hóspede");
            System.out.println("2 - Listar hóspedes");
            System.out.println("3 - Remover hóspede");
            System.out.println("0 - Voltar atrás");

            op = readInt();

            switch (op) {
                case 1 -> addGuest();
                case 2 -> listGuests();
                case 3 -> removeGuest();
                case 0 -> {
                }
                default -> System.out.println("Opção inválida.");
            }
        } while (op != 0);
    }

    static void reservationMenu() {
        int op;
        do {
            System.out.println("\n--- Menu de Reservas ---");
            System.out.println("1 - Criar Reserva");
            System.out.println("2 - Listar todas as Reservas");
            System.out.println("3 - Listar Reservas por quarto");
            System.out.println("4 - Listar Reservas por hóspede");
            System.out.println("5 - Cancelar Reserva");
            System.out.println("0 - Voltar atrás");

            op = readInt();

            switch (op) {
                case 1 -> createReservation();
                case 2 -> listAllReservations();
                case 3 -> listReservationsByRoom();
                case 4 -> listReservationsByGuest();
                case 5 -> cancelReservation();
                case 0 -> {
                }
                default -> System.out.println("Opção inválida.");
            }
        } while (op != 0);
    }

    static void addRoom() { // Impede que o número máximo de quartos seja excedido
        if (roomCount >= MAX_ROOMS) {
            System.out.println("Limite que quarto alcançado.");
            return;
        }

        int id = nextRoomId++;

        System.out.print("Número do quarto: ");
        int number = readInt();

        System.out.print("Capacidade: ");
        int capacity = readInt();

        if (capacity < 1 || capacity >= 6) {
            System.out.println("Deve introduzir uma capacidade entre 1 e 6.");
            return;
        }

        rooms[roomCount++] = new Room(id, number, capacity);
        saveRooms();

        System.out.println("Quarto adicionado com ID: " + id);
    }

    static void listRooms() {
        if (roomCount == 0) {
            System.out.println("Não há quartos.");
            return;
        }
        for (int i = 0; i < roomCount; i++) {
            System.out.println("ID do quarto: " + rooms[i].getId());
            System.out.println("Número do quarto: " + rooms[i].getNumber());
            System.out.println("Capacidade: " + rooms[i].getCapacity());

        }
    }

    static void removeRoom() { // Remove quarto através do ID
        System.out.print("ID do quarto a remover: ");
        int id = readInt();

        Room room = findRoomById(id); // Confirma que o quarto existe
        if (room == null) {
            System.out.println("Quarto não encontrado.");
            return;
        }

        // Impede a eliminação do quarto caso esteja reservado
        for (int i = 0; i < reservationCount; i++) {
            Reservation r = reservations[i];
            if (r.isActive() && r.getRoomId() == id) {
                System.out.println("O quarto não pode ser removido, pois tem reservas ativas!");
                return;
            }
        }

        deleteRoomById(id);
        saveRooms();
        System.out.println("Quarto removido.");
    }

    static Room findRoomById(int id) { // Procura e retorna quartos com id recebido. Se não encontrar retorna null
        for (int i = 0; i < roomCount; i++) {
            if (rooms[i].getId() == id) return rooms[i];
        }
        return null;
    }

    // Como um array tem tamanho fixo não é possível eliminar posições.
    // Desloca os elementos seguintes ao id que o utilizador pretende eliminar uma posição para trás.
    // Diminui o contador de quartos válidos
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


    static void addGuest() {
        // Verifica se o número máximo de hóspedes é excedido.
        if (guestCount >= MAX_GUESTS) {
            System.out.println("Número máximo de hóspedes atingido.");
            return;
        }

        int id = nextGuestId++;

        System.out.print("Nome: ");
        String name = readLine();

        System.out.print("Email: ");
        String email = readLine();

        System.out.print("Contacto: ");
        int contact = readInt();

        System.out.print("Tipo de Documento: ");
        String type = readLine();

        System.out.print("Número do Documento: ");
        int docNumber = readInt();

        guests[guestCount++] = new Guest(id, name, email, contact, type, docNumber);

        saveGuests(); // adiciona ao CSV
        System.out.println("Hóspede adicionado com ID: " + id);
    }

    static void listGuests() {
        if (guestCount == 0) {
            System.out.println("Não há hóspedes.");
            return;
        }
        for (int i = 0; i < guestCount; i++) {
            System.out.println(guests[i]);
        }
    }

    static void removeGuest() {
        System.out.print("Id do Hóspede a remover: ");
        int id = readInt();

        Guest g = findGuestById(id);
        if (g == null) {
            System.out.println("Hóspede não encontrado.");
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
                } catch (Exception ignore) {
                }
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

