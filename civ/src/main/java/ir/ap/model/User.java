package ir.ap.model;

import java.util.ArrayList;

public class User {
    private static ArrayList< User > users = new ArrayList<>();
    private String username ;
    private String password ;
    private String nickname ; 
    private int score ;
    private boolean isLogin ;

    public User(String username, String password, String nickname) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        score = 0;
        isLogin = false;
    }

    public User(String username, String password, String nickname, int score, boolean isLogin) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.score = score;
        this.isLogin = isLogin;
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

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean isLogin) {
        this.isLogin = isLogin;
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
