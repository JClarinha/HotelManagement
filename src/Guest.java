public class Guest {
    private final int id;
    private final String name;
    private final String email;
    private final int contact;
    private final String typeOfDocument;
    private final int documentNumber;

    public Guest(int id, String name, String email, int contact, String typeOfDocument, int documentNumber) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.contact = contact;
        this.typeOfDocument = typeOfDocument;
        this.documentNumber = documentNumber;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public int getContact() { return contact; }
    public String getTypeOfDocument() { return typeOfDocument; }
    public int getDocumentNumber() { return documentNumber; }

    @Override
    public String toString() {
        return "Guest{id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", contact=" + contact +
                ", documentType='" + typeOfDocument + '\'' +
                ", documentNumber=" + documentNumber +
                '}';
    }
}
