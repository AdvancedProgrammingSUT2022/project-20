package ir.ap.model;

import java.util.ArrayList;

public class User {
    private static ArrayList< User > users = new ArrayList<>();
    private String username ;
    private String nickname ; 
    private String password ;
    private int score ;
    private int LastWin_s;
    private int LastWin_m;
    private int LastWin_h;
    private boolean isLogin ;
    private int id ;
    
    public User(String username, String nickname, String password) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        score = 0;
        isLogin = false;
    }
    
    public User(String username, String nickname, String password, int score, boolean isLogin) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.score = score;
        this.isLogin = isLogin;
    }

    public static void resetUsers() {
        users = new ArrayList<>();
    }
    
    public static ArrayList<User> getUsers() {
        return users;
    }
    
    public String getUsername() {
        return username;
    }
    
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getNickname() {
        return nickname;
    }
    
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    
    public int getScore() {
        return score;
    }
    
    public void setScore(int score) {
        this.score = score;
    }

    public int getLastWin_s() {
        return LastWin_s;
    }

    public void setLastWin_s(int lastWin_s) {
        LastWin_s = lastWin_s;
    }

    public int getLastWin_m() {
        return LastWin_m;
    }

    public void setLastWin_m(int lastWin_m) {
        LastWin_m = lastWin_m;
    }

    public int getLastWin_h() {
        return LastWin_h;
    }

    public void setLastWin_h(int lastWin_h) {
        LastWin_h = lastWin_h;
    }

    public boolean isLogin() {
        return isLogin;
    }
    
    public void setLogin(boolean isLogin) {
        this.isLogin = isLogin;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals( Object user ) {
        if( user != null && user instanceof User && ((User)user).getUsername() != null && this.username != null && ((User)user).getUsername().equals( this.username ) )return true ;
        else return false ;
    }
    
    public static boolean hasUser( User user ){
        for(int i = 0 ; i < users.size() ; i ++){
            if( users.get( i ).equals( user ) )return true ;
        }
        return false ;
    }
    
    public static boolean hasUser( String username ){
        for(int i = 0 ; i < users.size() ; i ++){
            if( users.get( i ).getUsername().equals( username ) )return true;
        }
        return false;
    }
    
    public static boolean usernameExists(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username))
            return true;
        }
        return false;
    }
    
    public static boolean nicknameExists(String nickname) {
        for (User user : users) {
            if (user.getNickname().equals(nickname))
            return true;
        }
        return false;
    }
    
    public static User getUser( String username ){
        for(int i = 0 ; i < users.size() ; i ++){
            if( users.get( i ).getUsername().equals( username ) )return users.get( i );
        }
        return null;
    }
    
    public boolean checkPassword( String password ){
        if( this.password.equals( password ) )return true;
        else return false;
    }
    
    public static boolean addUser( User user ){
        if( hasUser( user ) == true )return false;
        users.add( user ) ;
        return true;
    }

}
