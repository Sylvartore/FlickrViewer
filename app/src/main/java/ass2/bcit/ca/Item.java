package ass2.bcit.ca;

public class Item {
    String title;
    String media;
    String date_taken;
    String author;

    @Override
    public String toString() {
        return "Item{" +
                "title='" + title + '\'' +
                ", media=" + media +
                ", date_taken='" + date_taken + '\'' +
                ", author='" + author + '\'' +
                '}';
    }
}
