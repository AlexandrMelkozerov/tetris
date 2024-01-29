import java.awt.*;

public class TetrisPieces {
    static final Point[][] PIECES = {
            {new Point(0, 0), new Point(1, 0), new Point(2, 0), new Point(3, 0)},   // I
            {new Point(0, 0), new Point(1, 0), new Point(0, 1), new Point(1, 1)},   // O
            {new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(2, 1)},   // L
            {new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(2, 1)},   // J
            {new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(2, 1)},   // T
            {new Point(1, 0), new Point(2, 0), new Point(0, 1), new Point(1, 1)},   // S
            {new Point(0, 0), new Point(1, 0), new Point(1, 1), new Point(2, 1)}    // Z
    };

    static final Color[] PIECE_COLORS = {
            Color.CYAN, Color.YELLOW, Color.ORANGE, Color.BLUE, Color.MAGENTA, Color.GREEN, Color.RED
    };
}