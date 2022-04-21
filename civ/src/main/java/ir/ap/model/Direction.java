package ir.ap.model;

public enum Direction {
    UP_RIGHT(0),
    RIGHT(1),
    DOWN_RIGHT(2),
    DOWN_LEFT(3),
    LEFT(4),
    UP_LEFT(5);

    private final int id;

    Direction(int id) {
        this.id = id;
    }

    public static Direction getDirectionById(int id) {
        switch (id) {
            case 0:
                return UP_RIGHT;
            case 1:
                return RIGHT;
            case 2:
                return DOWN_RIGHT;
            case 3:
                return DOWN_LEFT;
            case 4:
                return LEFT;
            case 5:
                return UP_LEFT;
            default:
                return null;
        }
    }

    public int getId() {
        return id;
    }
}
