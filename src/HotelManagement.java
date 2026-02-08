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
            System.out.println("2 - Listar todos os quartos");
            System.out.println("3 - Listar quartos livres (hoje)");
            System.out.println("4 - Listar quartos ocupados (hoje)");
            System.out.println("5 - Remover quarto");
            System.out.println("0 - Voltar atrás");

            op = readInt();

            switch (op) {
                case 1 -> addRoom();
                case 2 -> listRooms();
                case 3 -> listAvailableRoomsToday();
                case 4 -> listOccupiedRoomsToday();
                case 5 -> removeRoom();
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

        if (capacity < 1 || capacity > 6) {
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
            printRoom(rooms[i]);

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

    static boolean isRoomOccupiedToday(int roomId) {
        LocalDate today = LocalDate.now();

        for (int i = 0; i < reservationCount; i++) {
            Reservation r = reservations[i];
            if (r == null) continue;

            if (r.isActive() && r.getRoomId() == roomId) {
                boolean todayInside = !today.isBefore(r.getStartDate()) && !today.isAfter(r.getEndDate());
                if (todayInside) return true;
            }
        }
        return false;
    }

    static void listAvailableRoomsToday() {
        if (roomCount == 0) {
            System.out.println("Não há quartos.");
            return;
        }

        boolean found = false;

        for (int i = 0; i < roomCount; i++) {
            Room room = rooms[i];
            if (!isRoomOccupiedToday(room.getId())) {
                printRoom(room);
                found = true;
            }
        }

        if (!found) System.out.println("Não há quartos livres hoje.");
    }

    static void listOccupiedRoomsToday() {
        if (roomCount == 0) {
            System.out.println("Não há quartos.");
            return;
        }

        boolean found = false;

        for (int i = 0; i < roomCount; i++) {
            Room room = rooms[i];
            if (isRoomOccupiedToday(room.getId())) {
                printRoom(room);
                found = true;

            }
        }

        if (!found) System.out.println("Não há quartos ocupados hoje.");
    }

    static void printRoom(Room room) {
        System.out.println("");
        System.out.println("ID do quarto: " + room.getId());
        System.out.println("Número do quarto: " + room.getNumber());
        System.out.println("Capacidade: " + room.getCapacity());
        System.out.println("---------------------------");
    }

    static void printGuests(Guest guest) {
        System.out.println("");
        System.out.println("ID do hóspede: " + guest.getId());
        System.out.println("Nome do hóspede: " + guest.getName());
        System.out.println("Email do hóspede: " + guest.getEmail());
        System.out.println("Contacto do hóspede" + guest.getContact());
        System.out.println("Tipo de documento do hóspede: " +  guest.getTypeOfDocument());
        System.out.println("Número do documento do hóspede: " + guest.getDocumentNumber());
        System.out.println("---------------------------");
    }

    static void printReservations(Reservation reservations) {
        System.out.println("");
        System.out.println("ID da reserva: " + reservations.getId());
        System.out.println("Id do quarto: " + reservations.getRoomId());
        System.out.println("ID do hóspede: " + reservations.getGuestId());
        System.out.println("Data de check-in " + reservations.getStartDate());
        System.out.println("Data de check-out: " + reservations.getEndDate());
        System.out.println("---------------------------");
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
            printGuests(guests[i]);
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

        // Caso o hóspede tenha reserva ativa, não o remove
        for (int i = 0; i < reservationCount; i++) {
            Reservation r = reservations[i];
            if (r.isActive() && r.getGuestId() == id) {
                System.out.println("Impossível remover, hóspede com reserva ativa.");
                return;
            }
        }

        deleteGuestById(id);
        saveGuests();
        System.out.println("Hóspede removido.");
    }

    // Encontra o hóspede por id
    static Guest findGuestById(int id) {
        for (int i = 0; i < guestCount; i++) {
            if (guests[i].getId() == id) return guests[i];
        }
        return null;
    }

    // Remove o hóspede através do id
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


    static void createReservation() {
        if (roomCount == 0 || guestCount == 0) { // Garante que o utilizador introduz pelo menos um quarto e um hóspede
            System.out.println("Precisa de adicionar um quarto e um hóspede primeiro.");
            return;
        }
        if (reservationCount >= MAX_RESERVATIONS) { // Valida se ainda é possivel adicionar mais reservas
            System.out.println("Limite de reservas atingido.");
            return;
        }

        int id = nextReservationId++;

        System.out.print("ID do quarto: ");
        int roomId = readInt();
        Room room = findRoomById(roomId); // Verifica que o quarto existe
        if (room == null) {
            System.out.println("Quarto não encontrado.");
            return;
        }

        System.out.print("ID do hóspede: ");
        int guestId = readInt();
        Guest guest = findGuestById(guestId); // Verifica se o hóspede existe
        if (guest == null) {
            System.out.println("Hóspede não encontrado.");
            return;
        }

        System.out.print("Número de hóspedes: ");
        int nGuests = readInt();
        if (nGuests < 1 || nGuests > room.getCapacity()) { // Verifica se o número de hóspedes não excede a capacidade do quarto
            System.out.println("Número de hóspedes inválido (1.." + room.getCapacity() + ").");
            return;
        }

        LocalDate start = readDate("Check-in (YYYY-MM-DD): ");
        LocalDate end = readDate("Check-out (YYYY-MM-DD): ");

        if (start.isAfter(end)) { // Verifica se a data de chack-in é antes da data de check-out.
            System.out.println("Data inválida: início > fim.");
            return;
        }

        if (hasConflict(roomId, start, end, -1)) { // Impede double booking
            System.out.println("Conflito: quarto já reservado para as datas pretendeidas.");
            return;
        }

        reservations[reservationCount++] = new Reservation(id, roomId, guestId, nGuests, start, end);
        saveReservations(); // Guarda no CSV reservas
        System.out.println("Reserva criada com o ID: " + id);
    }

    static void listAllReservations() {
        if (reservationCount == 0) { // Verifica se há reservas
            System.out.println("Não há reservas.");
            return;
        }
        for (int i = 0; i < reservationCount; i++) { // Lista todas as reservas ativas
            printReservations(reservations[i]);
        }
    }

    static void listReservationsByRoom() {
        System.out.print("ID do quarto: ");
        int roomId = readInt();

        LocalDate today = LocalDate.now(); // Guarda a data atual
        boolean found = false;

        for (int i = 0; i < reservationCount; i++) { // Lista todos os quartos reservados na data atual
            Reservation r = reservations[i];
            if (r.isActive() && r.getRoomId() == roomId && !r.getEndDate().isBefore(today)) {
                printReservations(r);
                found = true;
            }
        }

        if (!found) System.out.println("Não há reservas para este quarto.");
    }

    static void listReservationsByGuest() { // Lista as reservas por hóspede.
        System.out.print("ID de hóspede: ");
        int guestId = readInt();

        LocalDate today = LocalDate.now();
        boolean found = false;

        for (int i = 0; i < reservationCount; i++) {
            Reservation r = reservations[i];
            if (r.isActive() && r.getGuestId() == guestId && !r.getEndDate().isBefore(today)) {
                printReservations(r);
                found = true;
            }
        }

        if (!found) System.out.println("Hóspede sem reservas.");
    }

    static void cancelReservation() { // Faz o cancelamento de uma reserva
        System.out.print("ID da reserva a cancelar: ");
        int id = readInt();

        Reservation r = findReservationById(id); // Verifica se a reserva existe
        if (r == null) {
            System.out.println("Reserva não encontrada.");
            return;
        }
        if (!r.isActive()) { // Verifica se a reserva ainda está ativa
            System.out.println("Reserva já cancelada.");
            return;
        }

        r.cancel();
        saveReservations();
        System.out.println("Reserva cancelada.");
    }

    static Reservation findReservationById(int id) { // Encontra a reserva através do ID
        for (int i = 0; i < reservationCount; i++) {
            if (reservations[i].getId() == id) return reservations[i];
        }
        return null;
    }

    static boolean hasConflict(int roomId, LocalDate start, LocalDate end, int ignoreReservationId) { // Verifica se há conflito de reservas
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

    // Garante que textos são guardados corretamente no formato CSV.
    // Se o texto tiver vírgulas ou aspas, envolve o texto entre aspas
    // e duplica aspas internas para evitar erros ao ler o ficheiro.
    static String escapeCsv(String s) {
        if (s == null) return "";
        if (s.contains(",") || s.contains("\"")) {
            s = s.replace("\"", "\"\"");
            return "\"" + s + "\"";
        }
        return s;
    }
    // Divide uma linha do CSV em colunas, respeitando aspas.
    // Permite ler corretamente campos que contenham vírgulas ou aspas.
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

    // Guarda todos os quartos no ficheiro CSV.
    // Cada linha representa um quarto.
    // Substitui totalmente o conteúdo do ficheiro.
    static void saveRooms() {
        try (PrintWriter pw = new PrintWriter(ROOMS_CSV)) {
            pw.println("id,number,capacity");
            for (int i = 0; i < roomCount; i++) {
                Room r = rooms[i];
                pw.printf("%d,%d,%d%n", r.getId(), r.getNumber(), r.getCapacity());
            }
        } catch (Exception e) {
            System.out.println("Erro ao guardar quartos: " + e.getMessage());
        }
    }
    // Carrega os quartos do ficheiro CSV para memória.
    // Reconstrói o array rooms e atualiza contadores e IDs.
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
            System.out.println("Erro ao carregar quartos: " + e.getMessage());
        }
    }

    // Guarda todos os hóspedes no ficheiro CSV.
    // Usa escapeCsv para evitar erros com vírgulas ou aspas.
    static void saveGuests() {
        try (PrintWriter pw = new PrintWriter(GUESTS_CSV)) {
            pw.println("id,name,email,contact,documentType,documentNumber");

            for (int i = 0; i < guestCount; i++) {
                Guest g = guests[i];
                pw.printf("%d,%s,%s,%d,%s,%d%n", g.getId(), escapeCsv(g.getName()), escapeCsv(g.getEmail()), g.getContact(), escapeCsv(g.getTypeOfDocument()), g.getDocumentNumber());
            }
        } catch (Exception e) {
            System.out.println("Erro ao guardar hóspedes: " + e.getMessage());
        }
    }

    // Carrega hóspedes do ficheiro CSV.
    // Converte cada linha num objeto Guest.
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
            System.out.println("Erro ao carregar hóspedes: " + e.getMessage());
        }
    }

    // Guarda reservas no CSV incluindo estado ativo/inativo.
    static void saveReservations() {
        try (PrintWriter pw = new PrintWriter(RESERVATIONS_CSV)) {
            pw.println("id,roomId,guestId,numberOfGuests,startDate,endDate,active");

            for (int i = 0; i < reservationCount; i++) {
                Reservation r = reservations[i];
                pw.printf("%d,%d,%d,%d,%s,%s,%b%n", r.getId(), r.getRoomId(), r.getGuestId(), r.getNumberOfGuests(), r.getStartDate().toString(), r.getEndDate().toString(), r.isActive());
            }
        } catch (Exception e) {
            System.out.println("Erro ao guardar reservas: " + e.getMessage());
        }
    }

    // Carrega reservas do CSV e restaura estado ativo/inativo.
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
            System.out.println("Erro ao carregar reservas: " + e.getMessage());
        }
    }
}

