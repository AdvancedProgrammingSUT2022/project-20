package ir.ap.client.components;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;

import java.io.Serializable;

public class UserSerializer implements Serializable {
    private StringProperty username;
    private StringProperty nickname;
    private IntegerProperty maxScore;
    public StringProperty getUsername() {
        return username;
    }
    public void setUsername(StringProperty username) {
        this.username = username;
    }
    public StringProperty getNickname() {
        return nickname;
    }
    public void setNickname(StringProperty nickname) {
        this.nickname = nickname;
    }
    public IntegerProperty getMaxScore() {
        return maxScore;
    }
    public void setMaxScore(IntegerProperty maxScore) {
        this.maxScore = maxScore;
    }
    
    

}
