package ir.ap.client.components;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;

import java.io.Serializable;

public class UserSerializer implements Serializable {
    private StringProperty username;
    private StringProperty nickname;
    private IntegerProperty maxScore;
    
}
