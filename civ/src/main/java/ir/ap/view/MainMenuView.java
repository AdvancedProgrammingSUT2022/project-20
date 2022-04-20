package ir.ap.view;

import java.util.regex.Matcher;

public class MainMenuView extends AbstractMenuView {
    
    public enum Command {
        ENTER_MENU("menu enter (?<menuName>\\w+)"),
        EXIT_MENU("menu exit"),
        SHOW_MENU("menu show-current");

        private final String regex;

        Command(String regex) {
            this.regex = regex;
        }

        @Override
        public String toString() {
            return "\\s*" + regex.replace(" ", "\\s+") + "\\s*";
        }
    }

    public enum Validator {
        ;

        private final String regex;

        Validator(String regex) {
            this.regex = regex;
        }

        @Override
        public String toString() {
            return regex;
        }
    }

    public enum Message {
        MENU_NAVIGATION_IMPOSSIBLE("menu navigation is not possible");

        private final String msg;

        Message(String msg) {
            this.msg = msg;
        }

        @Override
        public String toString() {
            return msg;
        }
    }

    public Enum<?>[] getCommands() {
        return Command.values();
    }

    public Menu enterMenu(Matcher matcher) {
        Menu nextMenu = Menu.getMenuByName(matcher.group("menuName"));
        if (nextMenu == Menu.LOGIN) {
            return responseAndGo(null, Menu.LOGIN);
        } else if (nextMenu == Menu.PROFILE) {
            return responseAndGo(null, Menu.PROFILE);
        } else {
            return responseAndGo(Message.MENU_NAVIGATION_IMPOSSIBLE, Menu.MAIN);
        }
    }

    public Menu exitMenu(Matcher matcher) {
        return responseAndGo(null, Menu.LOGIN);
    }

    public Menu showMenu(Matcher matcher) {
        return responseAndGo("Main Menu", Menu.MAIN);
    }
}
