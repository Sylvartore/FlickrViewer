package ass2.bcit.ca;

public class Item {
    String title;
    String media;
    String date_taken;
    String author;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setMedia(String media) {
        this.media = media;
    }

    public void setDate_taken(String date_taken) {
        this.date_taken = date_taken;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public String getMedia() {
        return media;
    }

    public String getDate_taken() {
        return date_taken;
    }

    public String getAuthor() {
        return author;
    }

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
