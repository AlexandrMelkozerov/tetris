import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

public class TetrisGame extends JFrame {
    private static final int BOARD_WIDTH = 15;
    private static final int BOARD_HEIGHT = 25;
    private static final int BLOCK_SIZE = 30;

    private Timer timer;
    private boolean isGameOver = false;

    private ArrayList<Point> currentPiece;
    private int currentPieceType;
    private int currentX = 0;
    private int currentY = 0;

    private int[][] board;
    private BufferedImage bufferImage;

    public TetrisGame() {
        setTitle("Tetris");
        setSize(BOARD_WIDTH * BLOCK_SIZE, BOARD_HEIGHT * BLOCK_SIZE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        board = new int[BOARD_HEIGHT][BOARD_WIDTH];

        addKeyListener(new TetrisKeyListener());
        setFocusable(true);

        initializeGame();

        timer = new Timer(500, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!isGameOver) {
                    moveDown();
                }
            }
        });
        timer.start();
    }

    private void initializeGame() {
        currentPiece = new ArrayList<>();
        spawnPiece();
    }

    private void spawnPiece() {
        Random random = new Random();
        currentPieceType = random.nextInt(7);

        currentPiece.clear();

        for (Point point : TetrisPieces.PIECES[currentPieceType]) {
            currentPiece.add(new Point(point.x, point.y));
        }

        currentX = BOARD_WIDTH / 2 - 1;
        currentY = 0;

        if (!isValidMove()) {
            isGameOver = true;
            timer.stop();
        }
    }

    private boolean isValidMove() {
        for (Point point : currentPiece) {
            int x = currentX + point.x;
            int y = currentY + point.y;

            if (x < 0 || x >= BOARD_WIDTH || y < 0 || y >= BOARD_HEIGHT || board[y][x] != 0) {
                return false;
            }
        }

        return true;
    }

    private void moveDown() {
        currentY++;

        if (!isValidMove()) {
            currentY--;
            placePieceOnBoard();
            clearLines();
            spawnPiece();
        }

        repaint();
    }

    private void moveLeft() {
        currentX--;

        if (!isValidMove()) {
            currentX++;
        }

        repaint();
    }

    private void moveRight() {
        currentX++;

        if (!isValidMove()) {
            currentX--;
        }

        repaint();
    }

    private void rotate() {
        ArrayList<Point> tempPiece = new ArrayList<>();

        for (Point point : currentPiece) {
            tempPiece.add(new Point(-point.y, point.x));
        }

        currentPiece.clear();
        currentPiece.addAll(tempPiece);

        if (!isValidMove()) {
            tempPiece.clear();
            tempPiece.addAll(currentPiece);
            currentPiece.clear();
            currentPiece.addAll(currentPiece);
        }

        repaint();
    }

    private void placePieceOnBoard() {
        for (Point point : currentPiece) {
            int x = currentX + point.x;
            int y = currentY + point.y;
            board[y][x] = currentPieceType + 1;
        }
    }

    private void clearLines() {
        for (int i = BOARD_HEIGHT - 1; i >= 0; i--) {
            boolean isLineFull = true;

            for (int j = 0; j < BOARD_WIDTH; j++) {
                if (board[i][j] == 0) {
                    isLineFull = false;
                    break;
                }
            }

            if (isLineFull) {
                for (int k = i; k > 0; k--) {
                    System.arraycopy(board[k - 1], 0, board[k], 0, BOARD_WIDTH);
                }
            }
        }
    }

    private void drawPiece(Graphics g) {
        for (Point point : currentPiece) {
            int x = (currentX + point.x) * BLOCK_SIZE;
            int y = (currentY + point.y) * BLOCK_SIZE;

            g.setColor(TetrisPieces.PIECE_COLORS[currentPieceType]);
            g.fillRect(x, y, BLOCK_SIZE, BLOCK_SIZE);
            g.setColor(Color.BLACK);
            g.drawRect(x, y, BLOCK_SIZE, BLOCK_SIZE);
        }
    }

    private void drawBoard(Graphics g) {
        for (int i = 0; i < BOARD_HEIGHT; i++) {
            for (int j = 0; j < BOARD_WIDTH; j++) {
                if (board[i][j] != 0) {
                    g.setColor(TetrisPieces.PIECE_COLORS[board[i][j] - 1]);
                    g.fillRect(j * BLOCK_SIZE, i * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
                    g.setColor(Color.BLACK);
                    g.drawRect(j * BLOCK_SIZE, i * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
                }
            }
        }
    }

    @Override
    public void paint(Graphics g) {
        if (bufferImage == null) {
            bufferImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        }

        Graphics bufferGraphics = bufferImage.getGraphics();

        // Очистка буфера
        bufferGraphics.clearRect(0, 0, getWidth(), getHeight());

        // Отрисовка на буфере
        drawPiece(bufferGraphics);
        drawBoard(bufferGraphics);

        // Копирование буфера на экран
        g.drawImage(bufferImage, 0, 0, this);

        if (isGameOver) {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.drawString("Game Over", BOARD_WIDTH * BLOCK_SIZE / 4, BOARD_HEIGHT * BLOCK_SIZE / 2);
        }
    }

    private class TetrisKeyListener implements KeyListener {
        public void keyPressed(KeyEvent e) {
            if (!isGameOver) {
                if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    moveDown();
                } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    moveLeft();
                } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    moveRight();
                } else if (e.getKeyCode() == KeyEvent.VK_UP) {
                    rotate();
                }
            }
        }

        public void keyTyped(KeyEvent e) {}

        public void keyReleased(KeyEvent e) {}
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TetrisGame tetrisGame = new TetrisGame();
            tetrisGame.setVisible(true);
        });
    }
}