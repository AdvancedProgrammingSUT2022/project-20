package ir.ap.view;

public enum Menu {
    LOGIN(new LoginMenuView()),
    MAIN(new MainMenuView()),
    GAME(new GameMenuView()),
    PROFILE(new ProfileMenuView());

    private final AbstractMenuView menuView;

    Menu(AbstractMenuView menuView) {
        this.menuView = menuView;
    }

    public Menu runCommand() {
        return menuView.runCommand();
    }
}
