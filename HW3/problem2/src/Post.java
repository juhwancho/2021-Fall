import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

class Post implements Comparable<Post> {
    private final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

    private int id;
    private final static int ID_NOT_INITIATED = -1;
    private LocalDateTime dateTime;
    private String title, content;
    public int keywordoccurence=0;
    public int wordnum=0;

    Post(String title, String content) {
        this(ID_NOT_INITIATED, LocalDateTime.now(), title, content);
    }

    Post(int id, LocalDateTime dateTime, String title, String content) {
        this.id = id;
        this.dateTime = dateTime;
        this.title = title;
        this.content = content.trim();
        wordnum=content.length();
    }

    String getSummary() {
        return String.format("id: %d, created at: %s, title: %s", id, getDate(), title);
    }

    @Override
    public String toString() {
        return String.format(
            "-----------------------------------\n" +
            "id: %d\n" +
            "created at: %s\n" +
            "title: %s\n" +
            "content: %s"
            , id, getDate(), title, content);
}

    int getId() {
        return id;
    }

    void setId(int id) {
        this.id = id;
    }

    String getDate() {
        return dateTime.format(formatter);
    }

    void setDateTime(LocalDateTime dateTime){
        this.dateTime = dateTime;
    }

    String getTitle() {
        return title;
    }

    String getContent() {
        return content;
    }
    int containskeyword(String keyword){
        String[]temp;
        String[]temp2;
        temp=title.split(keyword);
        temp2=content.split(keyword);
        return temp.length+temp2.length-2;
    }

    static LocalDateTime parseDateTimeString(String dateString, DateTimeFormatter dateTimeFormatter) {
        return LocalDateTime.parse(dateString,dateTimeFormatter);
    }

    @Override
    public int compareTo(Post o) {
        return -this.dateTime.compareTo(o.dateTime);
    }
    public void occurrefresh(){
        keywordoccurence=0;
    }

}

