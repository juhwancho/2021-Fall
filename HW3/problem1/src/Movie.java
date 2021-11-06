
import java.util.HashMap;
import java.util.Map;

public class Movie implements Comparable<Movie>{
    private String title;
    private String[]tag;

    public Movie(String title){
        this.title=title;
    }
    public Movie(String title, String[] tag) {
        this.title = title;
        this.tag=tag;
    }
    @Override
    public String toString() {
        return title;

    }
    public boolean tagmatch(String[] tag){
        int tagcontain=0;
        for(String i :tag){
            for(String j:this.tag){
            if(i.equals(j))
                tagcontain++;
            }
        }
        if(tagcontain==tag.length)
            return true;
        else return false;
    }

    @Override
    public int compareTo(Movie o) {

        return -this.title.compareTo(o.toString());
    }
}
