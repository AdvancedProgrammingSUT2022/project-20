package ir.ap.network;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import ir.ap.controller.GameController;
import ir.ap.controller.JsonResponsor;
import ir.ap.controller.UserController;
import ir.ap.model.User;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.Socket;

public class SocketHandler extends Thread implements JsonResponsor {

    private final Socket socket;

    private boolean terminateFlag;

    private GameController gameController;
    private final UserController userController;

    private User user;

    public SocketHandler(Socket socket) {
        this.socket = socket;
        this.terminateFlag = false;
        this.gameController = null;
        this.userController = new UserController();
    }

    private JsonObject process(Request request) {
        if ("newGame".equals(request.getMethodName())) {
            return newGame(request.getParams().toArray(new String[0]));
        } else if ("login".equals(request.getMethodName())) {
            return login(request.getParams().get(0).toString(), request.getParams().get(1).toString());
        } else if ("continueGame".equals(request.getMethodName())) {
            return continueGame(request.getParams().get(0).toString());
        }
        Class<?>[] paramTypes = new Class<?>[request.getParams().size()];
        int counter = 0;
        for (Object value : request.getParams()) {
            if (value.getClass().equals(String.class)) {
                paramTypes[counter++] = String.class;
            } else if (value.getClass().equals(Double.class)) {
                paramTypes[counter++] = int.class;
            } else if (value.getClass().equals(Boolean.class)) {
                paramTypes[counter++] = boolean.class;
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
        for (int i = 0; i < params.length; i++) {
            if (params[i] instanceof Double)
                params[i] = ((Double) params[i]).intValue();
        }
        try {
            Object controller = controllerIsGame ? gameController : userController;
            if (params.length > 0)
                return (JsonObject) method.invoke(controller, params);
            else
                return (JsonObject) method.invoke(controller);
        } catch (Exception e) {
            return messageToJsonObj("Invalid parameters", false);
        }
    }

    public JsonObject login(String username, String password) {
        return userController.login(username, password, this);
    }

    public JsonObject newGame(String... players) {
        gameController = new GameController();
        JsonObject result = gameController.newGame(players);
        if (!isOk(result)) {
            gameController = null;
            return result;
        }
        for (JsonElement userJson : gameController.getAllUsersInGame().get("users").getAsJsonArray()) {
            User user = User.getUser(userJson.getAsJsonObject().get("username").getAsString());
            if (user.getSocketHandler() != null)
                user.getSocketHandler().setGameController(gameController);
            else
                return messageToJsonObj("Users should be login", false);
        }
        return result;
    }

    public JsonObject continueGame(String username) {
        return setOk(new JsonObject(), gameController != null);
    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    public GameController getGameController() {
        return gameController;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
        System.out.println("Server accepted socket " + socket);
        while (!terminateFlag) {
            try {
                String request = inputStream.readUTF();
                Request requestObj = GSON.fromJson(request, Request.class);
                JsonObject responseJson = process(requestObj);
                String response = GSON.toJson(responseJson);
                outputStream.writeUTF(response);
            } catch (Exception e) {
//                e.printStackTrace();
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
