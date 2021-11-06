import java.util.*;
import java.time.LocalDateTime;

public class FrontEnd {
    private UserInterface ui;
    private BackEnd backend;
    private User user;


    public FrontEnd(UserInterface ui, BackEnd backend) {
        this.ui = ui;
        this.backend = backend;
    }
    
    public boolean auth(String authInfo){
        // TODO sub-problem 1
        String[] info = authInfo.split("\n");
        String id= info[0];
        String password=info[1];
        if((id=="")||(password==""))
        return false;
        if(backend.authcheck(info)) {
            this.user = new User(info[0], info[1]);
            return true;
        }
        else return false;


    }

    public void post(Pair<String, String> titleContentPair) {
         Post upload=new Post(titleContentPair.key,titleContentPair.value);
         backend.postserverupload(upload,user.id);
        // TODO sub-problem 2
    }
    
    public void recommend(int N){

        // TODO sub-problem 3
        List<Post> recommendedpost= backend.recommend(user.id, N);
        for(int i=0;i<recommendedpost.size();i++){
            ui.println(recommendedpost.get(i).toString());
        }
    }

    public void search(String command) {
        String [] keywords=command.split(" ");
        List<Post> searchedpost= backend.keywordSearch(keywords);
        for(int i=0;i<searchedpost.size();i++){
            ui.println(searchedpost.get(i).getSummary());
        }

        // TODO sub-problem 4
    }
    
    User getUser(){
        return user;
    }
}
