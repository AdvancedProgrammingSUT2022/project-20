package ir.ap.view;

import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import ir.ap.controller.GameController;
import ir.ap.controller.UserController;
import ir.ap.model.User;

public abstract class AbstractMenuView {
    protected static final Scanner SCANNER = new Scanner(System.in);
    protected static final UserController USER_CONTROLLER = new UserController();
    protected static final GameController GAME_CONTROLLER = new GameController();

    protected static User user;
    protected static Menu currentMenu = Menu.LOGIN;

    public static String constantCaseToCamelCase(String constCaseStr) {
        String res = Arrays.stream(constCaseStr.split("_"))
                .map(t -> t.substring(0, 1).toUpperCase() + t.substring(1).toLowerCase())
                .collect(Collectors.joining(""));
        return res.substring(0, 1).toLowerCase() + res.substring(1);
    }

    public static Matcher getCommandMatcher(String regex, String input) {
        Matcher res = Pattern.compile(regex).matcher(input);
        return (res.matches() ? res : null);
    }

    public static void run() {
        currentMenu = Menu.LOGIN;
        while ((currentMenu = currentMenu.runCommand()) != null) {
        }
    }

    public Menu runCommand() {
        if (!SCANNER.hasNextLine())
            return null;
        String input = SCANNER.nextLine();
        Matcher matcher;
        for (Enum<?> command : getCommands()) {
            matcher = getCommandMatcher(command.toString(), input);
            if (matcher != null) {
                String methodName = constantCaseToCamelCase(command.name());
                try {
                    return (Menu) this.getClass().getMethod(methodName,
                            Matcher.class)
                            .invoke(this, matcher);
                } catch (Exception ex) {
                    ex.getCause().printStackTrace();
                    throw new RuntimeException();
                }
            }
        }
        System.out.println("invalid command");
        return currentMenu;
    }

    public abstract Enum<?>[] getCommands();

    public abstract void enterMenu(Matcher matcher);

    public abstract void exitMenu(Matcher matcher);

    public abstract void showMenu(Matcher matcher);
}
