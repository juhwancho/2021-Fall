import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class BackEnd extends ServerResourceAccessible {
    List<Post>PostDB=new LinkedList<>();
    static int maxid = 0;

    // Use getServerStorageDir() as a default directory
    // TODO sub-program 1 ~ 4 :
    BackEnd() {
        uploadPostDB();
        updatemaxid();
    }

    public boolean caseinsensitiveequal(String a, String b) {
        if (a.length() != b.length()) return false;
        return (a.toLowerCase().compareTo(b.toLowerCase()) == 0);
    }

    public boolean authcheck(String[] s) {
        String passwordadd = getServerStorageDir() + s[0];
        File file = new File(passwordadd + "/password.txt");
        try {
            Scanner fileinput = new Scanner(file);
            while (fileinput.hasNext()) {
                String temp = fileinput.nextLine();
                if (caseinsensitiveequal(s[1], temp))
                    return true;
            }
            return false;
        } catch (FileNotFoundException e) {
            return false;
        }


    }

    public boolean postserverupload(Post s, String username) {

        maxid++;
        try {
            PostDB.add(s);
            File postfile = new File(getServerStorageDir() + username + "/post/" + maxid + ".txt");
            postfile.createNewFile();
            FileWriter fileWriter = new FileWriter(postfile);
            s.setId(maxid);
            fileWriter.write(s.getDate());
            fileWriter.append("\n");
            fileWriter.append(s.getTitle());
            fileWriter.append("\n\n");
            String[] content = s.getContent().split("\n");
            for (int i = 0; i < content.length; i++) {
                fileWriter.append(content[i]);
                if (i < content.length - 1)
                    fileWriter.append("\n");
            }
            fileWriter.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }

    public void updatemaxid() {
        int max = 0;
        File datadir = new File(getServerStorageDir());
        File[] userList = datadir.listFiles();
        for (File user : userList) {
            File userpostpath = new File(user.getPath() + "/post");
            File[] userpost = userpostpath.listFiles();

            assert userpost != null;
            for (File post : userpost) {
                String name = post.getName().substring(0, post.getName().lastIndexOf('.'));
                if (Integer.parseInt(name) > max) {
                    max = Integer.parseInt(name);
                }
            }
        }
        maxid = max;
    }

    public List<Post> recommend(String userid, int N) {
        String passwordadd = getServerStorageDir() + userid;
        File file = new File(passwordadd + "/friend.txt");
        List<String> friendname = new LinkedList<>();
        List<Post> frinedpost = new LinkedList<>();
        try {
            Scanner scanner = new Scanner(file);

            while (scanner.hasNext()) {
                friendname.add(scanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        for (String name : friendname) {
            File friendpostpath = new File(getServerStorageDir() + name + "/post");
            File[] userpost = friendpostpath.listFiles();
            for (File s : userpost) {
                frinedpost.add(readpost(s));
            }

        }

        Collections.sort(frinedpost);
        List<Post> finalpost = new LinkedList<>();
        for (int i = 0; i < Math.min(N, frinedpost.size()); i++) {
            finalpost.add(frinedpost.get(i));
        }


        return finalpost;
    }
    public void uploadPostDB(){
        File datadir = new File(getServerStorageDir());
        File[] userList = datadir.listFiles();
        for (File user : userList) {
            File userpostpath = new File(user.getPath() + "/post");
            File[] userpost = userpostpath.listFiles();

            assert userpost != null;
            for (File post : userpost) {
                PostDB.add(readpost(post));
                }
            }
        }


     public List<Post> keywordSearch(String[]keyword){
         List<Post>lstone= new LinkedList<>();
        for(Post a: PostDB){
             a.occurrefresh();
         }
         for(Post a: PostDB){
             for(int i=1;i<keyword.length;i++){
                 if(a.containskeyword(keyword[i])!=0)
                     if(!lstone.contains(a))
                         lstone.add(a);
                     a.keywordoccurence+=a.containskeyword(keyword[i]);
             }
         }
         Mycomparator mycomparator=new Mycomparator();
         Collections.sort(lstone,mycomparator);

         List<Post> finalpost = new LinkedList<>();
         for (int i = 0; i < Math.min(10, lstone.size()); i++) {
             finalpost.add(lstone.get(i));
         }

         return finalpost;
     }

    public Post readpost(File s) {
        try {
            int id = Integer.parseInt(s.getName().substring(0, s.getName().lastIndexOf('.')));
            Scanner fileinput = new Scanner(s);
            LocalDateTime posttime = LocalDateTime.parse(fileinput.nextLine(), DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
            String title = fileinput.nextLine();
            fileinput.nextLine();
            String content = "";
            while (fileinput.hasNext()) {
                content=content.concat(fileinput.nextLine());
                content=content.concat("\n");
            }
            String finalcontent = content.substring(0, content.lastIndexOf("\n"));
            return new Post(id, posttime, title, finalcontent);

    }catch(
    FileNotFoundException e)

    {
        e.printStackTrace();
    }
         return null;
}

    }
    class Mycomparator implements Comparator<Post>{

        @Override
        public int compare(Post o1, Post o2) {
            if(o2.keywordoccurence!=o1.keywordoccurence)
                return o2.keywordoccurence-o1.keywordoccurence;
            else
                return o2.wordnum-o1.wordnum;
        }
    }
    // Create helper funtions to support FrontEnd class

