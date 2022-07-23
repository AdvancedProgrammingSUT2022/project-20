package ir.ap.network;

import java.util.ArrayList;

public class Request {
    private final String methodName;
    private final ArrayList<Object> params;

    public Request(String methodName) {
        this.methodName = methodName;
        this.params = new ArrayList<>();
    }

    public String getMethodName() {
        return methodName;
    }

    public void addParam(Object value) {
        params.add(value);
    }

    public ArrayList<Object> getParams() {
        return params;
    }
}
