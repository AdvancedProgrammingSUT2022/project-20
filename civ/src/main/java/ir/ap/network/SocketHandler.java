package ir.ap.network;

import com.google.gson.JsonObject;
import ir.ap.controller.GameController;
import ir.ap.controller.JsonResponsor;
import ir.ap.controller.UserController;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.Socket;

public class SocketHandler extends Thread implements JsonResponsor {

    private final Socket socket;

    private boolean terminateFlag;

    private final GameController gameController; // TODO: Map many players to single game controller
    private final UserController userController;

    public SocketHandler(Socket socket) {
        this.socket = socket;
        this.terminateFlag = false;
        this.gameController = null;
        this.userController = new UserController(true);
    }

    private JsonObject process(Request request) {
        Class<?>[] paramTypes = new Class<?>[request.getParams().size()];
        int counter = 0;
        for (Object value : request.getParams()) {
            if (value.getClass().isArray()) {
                paramTypes[counter++] = String[].class;
            } else if (value.getClass().equals(String.class)) {
                paramTypes[counter++] = String.class;
            } else if (value.getClass().equals(Integer.class)) {
                paramTypes[counter++] = Integer.class;
            } else if (value.getClass().equals(Boolean.class)) {
                paramTypes[counter++] = Boolean.class;
            } else {
                return messageToJsonObj("Couldn't process param types", false);
            }
        }
        Method method;
        boolean controllerIsGame = true;
        try {
            method = GameController.class.getMethod(request.getMethodName(), paramTypes);
        } catch (Exception e) {
            try {
                method = UserController.class.getMethod(request.getMethodName(), paramTypes);
                controllerIsGame = false;
            } catch (Exception ex) {
                return messageToJsonObj("No such method", false);
            }
        }
        Object[] params = request.getParams().toArray(new Object[0]);
        try {
            Object controller = controllerIsGame ? gameController : userController;
            return (JsonObject) method.invoke(controller, params);
        } catch (Exception e) {
            return messageToJsonObj("Invalid parameters", false);
        }
    }

    @Override
    public void run() {
        DataInputStream inputStream;
        DataOutputStream outputStream;
        try {
            inputStream = new DataInputStream(socket.getInputStream());
            outputStream = new DataOutputStream(socket.getOutputStream());
        } catch (Exception e) {
            System.out.println("Unable to run socket handler");
            try {
                terminate();
            } catch (Exception ex) {
                System.out.println("Unable to terminate socket handler");
            }
            return;
        }
        while (!terminateFlag) {
            try {
                String request = inputStream.readUTF();
                Request requestObj = GSON.fromJson(request, Request.class);
                JsonObject responseJson = process(requestObj);
                String response = GSON.toJson(responseJson);
                outputStream.writeUTF(response);
            } catch (Exception e) {
                System.out.println("Connection closed on socket " + socket);
                try {
                    terminate();
                } catch (Exception ex) {
                    System.out.println("Unable to terminate socket handler");
                }
                return;
            }
        }
    }

    public synchronized void terminate() throws IOException {
        this.terminateFlag = true;
        if (socket != null && !socket.isClosed())
            socket.close();
    }
}
