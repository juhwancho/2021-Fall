
import java.util.*;
import java.util.Comparator;

class MovieRating  {
    public int ratings;
    public String title;

    MovieRating(int ratings,String title){
        this.ratings=ratings;
        this.title=title;
    }



}
class Rating implements Comparable<Rating>{
    private int ratings;
    private String username;
    Rating(String username, int ratings){
        this.username=username;
        this.ratings=ratings;
    }

    public void  changeRating(int ratings){
        this.ratings=ratings;
    }
    public String getUsername(){
        return this.username;
    }
    public int getrating(){
        return this.ratings;
    }


    @Override
    public int compareTo(Rating o) {
        return this.getrating()-o.getrating();
    }
}

public class MovieApp {
    Set<String> Username= new HashSet<>();
    Set<User> Userlist= new HashSet<>();
    HashMap<String, Movie > MovieList=new HashMap<>();
    static HashMap<String,List<Rating>>  RatingDB=new HashMap<>();

    static public double getMedianrating( String title){

        if(!RatingDB.get(title).isEmpty()) {
            List<Rating> ratelist = RatingDB.get(title);
            Collections.sort(ratelist);
            if (ratelist.size() % 2 == 0) {
                int x = ratelist.size() / 2;

                double y=((double)(ratelist.get(x-1).getrating()+ratelist.get(x).getrating()))/2.0;

                return y;
            }
            double  y= (double)(ratelist.get((ratelist.size()-1)/2).getrating());
            return y;
        }

        else
            return 0;
    }
    public boolean addMovie(String title, String[] tags) {
        if (MovieList.containsKey(title)) {
        }
        else {
            if(tags.length!=0)
            MovieList.put(title, new Movie(title, tags));
            return true;
        }
        return false;
    }

    public boolean addUser(String name) {
        if(!Username.contains(name)) {
            Username.add(name);
            Userlist.add(new User(name));
            return true;
        }
        return false;
    }

    public Movie findMovie(String title) {
        // TODO sub-problem 1
        if(MovieList.containsKey(title))
        return MovieList.get(title);
        else return null;
    }

    public User findUser(String username) {
        // TODO sub-problem 1
        for(User s : Userlist){
            if (s.toString().equals(username))
            return s;
        }
        return null;
    }

    public List<Movie> findMoviesWithTags(String[] tags) {

        // TODO sub-problem 2
        LinkedList<Movie> foundmovies = new LinkedList<>();
        for (String s : MovieList.keySet()){
            if (MovieList.get(s).tagmatch(tags))
                foundmovies.add(MovieList.get(s));}

        Collections.sort(foundmovies);
        return foundmovies;

    }

    public boolean rateMovie(User user, String title, int rating) {
        int ratingadded=0;
        // TODO sub-problem 3
        if ((title==null)||(!MovieList.containsKey(title))||((rating)<1||rating>5))
            return false;
        else
            if(RatingDB.containsKey(title)){
                for(Rating s:  RatingDB.get(title)){
                    if(s.getUsername().equals(user.toString()))
                    { s.changeRating(rating);
                    return true;}
                }
                RatingDB.get(title).add(new Rating(user.toString(),rating));
                return true;
            }
        LinkedList<Rating>movierating =new LinkedList<>();
            movierating.add(new Rating(user.toString(),rating));
            RatingDB.put(title,movierating);
            return true;

    }

    public int getUserRating(User user, String title) {

        if ((title==null)||(user==null)||!(Userlist.contains(user))||!(MovieList.keySet().contains(title)))
        return -1;
        else {
            for(Rating s:RatingDB.get(title)){
                if(s.getUsername().equals(user.toString())){
                    return s.getrating();
                }
            };
            return 0;
        }
    }
    HashMap<String, HashMap<String,Integer>>SearchHistory=new HashMap<>();
    public List<Movie> findUserMoviesWithTags(User user, String[] tags) {
        // TODO sub-problem 4
        if ((user == null) || !(Userlist.contains(user)))
            return new LinkedList<>();
        else
            for(Movie s:findMoviesWithTags(tags)){
                if(SearchHistory.get(user.toString())==null) {
                SearchHistory.put(user.toString(),new HashMap<String,Integer>());
                SearchHistory.get(user.toString()).put(s.toString(),1);
                }
                else
                if(SearchHistory.get(user.toString()).containsKey(s.toString()))
                SearchHistory.get(user.toString()).replace(s.toString(),SearchHistory.get(user.toString()).get(s.toString())+1);
                SearchHistory.get(user.toString()).putIfAbsent(s.toString(),1);

            };
            return findMoviesWithTags(tags);

    }

    public List<Movie> recommend(User user) {
         HashMap<String, Integer>usrschHist=SearchHistory.get(user.toString());

         List<Movie> sortedMovie=new LinkedList<>();
         List<MovieRating>sorttemp=new LinkedList<>();
         if((user==null)||!(Userlist.contains(user)))
             return new LinkedList<>();

         for(String s:usrschHist.keySet()) {
            sorttemp.add(new MovieRating(usrschHist.get(s),s));
        }

       
         MyComparator s=new MyComparator();
         MyComparator2 s2=new MyComparator2();


         Collections.sort(sorttemp,s2);

         for(MovieRating t :sorttemp){
             sortedMovie.add(MovieList.get(t.title));
         }
        if(sortedMovie.size()>3){
            return sortedMovie.subList(0,2);
        }
        else return sortedMovie;
    }
}

class MyComparator implements Comparator<MovieRating> {

    public int compare(MovieRating o1, MovieRating o2) {
        return -(int) (2*(MovieApp.getMedianrating(o1.title)-MovieApp.getMedianrating(o2.title)));
    }



}

 class MyComparator2 implements Comparator<MovieRating> {


    public int compare(MovieRating o1, MovieRating o2) {
        MyComparator s=new MyComparator();
        if(o1.ratings!=o2.ratings){
            return o1.ratings-o2.ratings;
        }
        else if(s.compare(o1,o2)!=0){
            return s.compare(o1,o2);
        }
        else return o1.title.compareTo(o2.title);
    }
}

