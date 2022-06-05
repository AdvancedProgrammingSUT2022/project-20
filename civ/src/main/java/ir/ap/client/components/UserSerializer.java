package ir.ap.client.components;

import java.io.Serializable;

public class UserSerializer implements Serializable {
    private WritableObjectProperty<String> username;
    private WritableObjectProperty<String> nickname;
    private WritableObjectProperty<Integer> maxScore;

    private transient WritableObjectProperty<Boolean> accepted;

    public UserSerializer(String username) {
        this.username = new WritableObjectProperty<>(username);
    }

    public UserSerializer(String username, boolean accepted) {
        this(username);
        this.accepted = new WritableObjectProperty<Boolean>(accepted);
    }

    public UserSerializer(String username, String nickname, int maxScore) {
        this(username);
        this.nickname = new WritableObjectProperty<>(nickname);
        this.maxScore = new WritableObjectProperty<>(maxScore);
    }

    public UserSerializer(String username, String nickname, int maxScore, boolean accepted) {
        this(username, nickname, maxScore);
        this.accepted = new WritableObjectProperty<Boolean>(accepted);
    }

    public WritableObjectProperty<String> usernameProperty() {
        return username;
    }

    public WritableObjectProperty<String> nicknameProperty() {
        return nickname;
    }

    public WritableObjectProperty<Integer> maxScoreProperty() {
        return maxScore;
    }

    public WritableObjectProperty<Boolean> acceptedProperty() {
        return accepted;
    }

    public String getUsername() {
        return username.get();
    }

    public String getNickname() {
        return nickname.get();
    }

    public Integer getMaxScore() {
        return maxScore.get();
    }

    public Boolean getAccepted() {
        return accepted.get();
    }

}
