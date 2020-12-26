
/*18D8103015H Kento Watanabe*/
import java.awt.*;
import java.util.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Scanner;
import java.util.Random;

class Stone {
    public final static int black = 1;
    public final static int white = 2;
    private int obverse;

    Stone() {
        obverse = 0;
    }

    void setObverse(int color) {
        if (color == black || color == white)
            obverse = color;
        else
            System.out.println("黒か白でなければいけません");
    }

    void paint(Graphics g, Point p, int rad) {
        if (obverse == black) {
            g.setColor(Color.black);
            g.fillOval((int) p.getX(), (int) p.getY(), rad * 2, rad * 2);
        } else if (obverse == white) {
            g.setColor(Color.white);
            g.fillOval((int) p.getX(), (int) p.getY(), rad * 2, rad * 2);
        }
    }

    int getObverse() {
        return obverse;
    }

    void doReverse() {
        int tmp;
        if (obverse == 1) {
            obverse = 2;
        } else if (obverse == 2) {
            obverse = 1;
        }
    }
}

class Board {
    private Stone[][] board = new Stone[8][8];
    public int num_grid_black;
    public int num_gird_white;
    private Point[] direction = new Point[8];
    public int[][] eval_black = new int[8][8];
    public int[][] eval_white = new int[8][8];

    Board() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = new Stone();
            }
        }
        board[3][3].setObverse(1);
        board[3][4].setObverse(2);
        board[4][3].setObverse(2);
        board[4][4].setObverse(1);
        direction[0] = new Point(1, 0);
        direction[1] = new Point(1, 1);
        direction[2] = new Point(0, 1);
        direction[3] = new Point(-1, 1);
        direction[4] = new Point(-1, 0);
        direction[5] = new Point(-1, -1);
        direction[6] = new Point(0, -1);
        direction[7] = new Point(1, -1);
    }

    void paint(Graphics g, int unit_size) {
        g.setColor(Color.black);
        g.fillRect(0, 0, unit_size * 10, unit_size * 10);

        g.setColor(new Color(0, 85, 0));
        g.fillRect(unit_size, unit_size, unit_size * 8, unit_size * 8);

        g.setColor(Color.black);
        for (int i = 0; i < 9; i++) {
            g.drawLine(unit_size, unit_size * (i + 1), unit_size * 9, unit_size * (i + 1));
        }

        for (int i = 0; i < 9; i++) {
            g.drawLine(unit_size * (i + 1), unit_size, unit_size * (i + 1), unit_size * 9);
        }

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                g.fillRect((unit_size * 3 + unit_size * 4 * i) - 5, (unit_size * 3 + unit_size * 4 * j) - 5, 10, 10);
            }
        }

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Point p = new Point();
                p.setLocation((int) (unit_size * (i + 1) + unit_size * 0.1),
                        (int) (unit_size * (j + 1) + unit_size * 0.1));
                board[i][j].paint(g, p, (int) (unit_size * 0.4));
            }
        }
    }

    boolean isOnBoard(int x, int y) {
        if (x < 0 || 7 < x || y < 0 || 7 < y) {
            return false;
        } else {
            return true;
        }
    }

    void setStone(int x, int y, int s) {
        board[x][y].setObverse(s);
    }

    void evalulateBoard() {
        num_gird_white = 0;
        num_grid_black = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                eval_black[i][j] = countReverseStone(i, j, 1);
                if (eval_black[i][j] > 0)
                    num_grid_black++;
                eval_white[i][j] = countReverseStone(i, j, 2);
                if (eval_white[i][j] > 0)
                    num_gird_white++;
            }
        }
    }

    int countStone(int s) {
        int cnt = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j].getObverse() == s) {
                    cnt++;
                }
            }
        }
        return cnt;
    }

    ArrayList<Integer> getLine(int x, int y, Point d) {
        ArrayList<Integer> line = new ArrayList<Integer>();
        int cx = x + d.x;
        int cy = y + d.y;
        while (isOnBoard(cx, cy) && board[cx][cy].getObverse() != 0) {
            line.add(board[cx][cy].getObverse());
            cx += d.x;
            cy += d.y;
        }
        return line;
    }

    int countReverseStone(int x, int y, int s) {
        if (board[x][y].getObverse() != 0)
            return -1;
        int cnt = 0;
        for (int d = 0; d < 8; d++) {
            ArrayList<Integer> line = new ArrayList<Integer>();
            line = getLine(x, y, direction[d]);
            int n = 0;
            while (n < line.size() && line.get(n) != s)
                n++;
            if (1 <= n && n < line.size())
                cnt += n;
        }
        return cnt;
    }

    // void printBoard() {
    // System.out.println("Board:");
    // for (int i = 0; i < 8; i++) {
    // for (int j = 0; j < 8; j++) {
    // System.out.printf("%2d ", board[j][i].getObverse());
    // }
    // System.out.println("");
    // }
    // }

    // void printEval() {
    // System.out.println("Black(1):");
    // for (int i = 0; i < 8; i++) {
    // for (int j = 0; j < 8; j++) {
    // System.out.printf("%2d ", eval_black[j][i]);
    // }
    // System.out.println("");
    // }
    // System.out.println("White(2):");
    // for (int i = 0; i < 8; i++) {
    // for (int j = 0; j < 8; j++) {
    // System.out.printf("%2d ", eval_white[j][i]);
    // }
    // System.out.println("");
    // }
    // }

    void setStoneAndReverse(int x, int y, int s) {
        setStone(x, y, s);
        for (int d = 0; d < 8; d++) {
            int cx = x + direction[d].x;
            int cy = y + direction[d].y;
            while (isOnBoard(cx, cy) && board[cx][cy].getObverse() != board[x][y].getObverse()
                    && board[cx][cy].getObverse() != 0) {
                cx = cx + direction[d].x;
                cy = cy + direction[d].y;
                if (isOnBoard(cx, cy) && board[cx][cy].getObverse() == board[x][y].getObverse()) {
                    cx = cx - direction[d].x;
                    cy = cy - direction[d].y;
                    while (board[cx][cy].getObverse() != board[x][y].getObverse()) {
                        board[cx][cy].doReverse();
                        cx = cx - direction[d].x;
                        cy = cy - direction[d].y;
                    }
                }
            }
        }
    }
}

class Player {
    public final static int type_human = 0;
    public final static int type_computer = 1;
    private int color;
    private int type;

    Player(int c, int t) {
        if (c == Stone.black || c == Stone.white)
            color = c;
        else {
            System.out.println("プレイヤーの色は黒か白でなければなりません:" + t);
            System.exit(0);
        }
        if (t == type_human || t == type_computer)
            type = t;
        else {
            System.out.println("プレイヤーは人間かコンピュータでなければいけません:" + t);
            System.exit(0);
        }
    }

    int getColor() {
        return color;
    }

    int getType() {
        return type;
    }

    Point tactics_0(Board bd) {
        // 左上優先
        if (color == Stone.black) {
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (bd.eval_black[i][j] > 0) {
                        return (new Point(i, j));
                    }
                }
            }
        } else if (color == Stone.white) {
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (bd.eval_white[i][j] > 0) {
                        return (new Point(i, j));
                    }
                }
            }
        }
        return (new Point(-1, -1));
    }

    Point tactics_1(Board bd) {
        // ランダムに配置
        while (true) {
            Random r1 = new Random();
            Random r2 = new Random();
            int i = r1.nextInt(8);
            int j = r2.nextInt(8);
            if (color == Stone.black) {
                if (bd.num_grid_black == 0)
                    return (new Point(-1, -1));
                if (bd.eval_black[i][j] > 0) {
                    return (new Point(i, j));
                }
            } else if (color == Stone.white) {
                if (bd.num_gird_white == 0)
                    return (new Point(-1, -1));
                if (bd.eval_white[i][j] > 0) {
                    return (new Point(i, j));
                }
            }
        }
    }

    Point tactics_2(Board bd) {
        // ひっくりかせる相手の石が最も多いマス目を選ぶ
        int max = 0;
        int imax = 0;
        int jmax = 0;
        if (color == Stone.black) {
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (bd.eval_black[i][j] > max) {
                        max = bd.eval_black[i][j];
                        imax = i;
                        jmax = j;
                    }
                }
            }
        } else if (color == Stone.white) {
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (bd.eval_white[i][j] > max) {
                        max = bd.eval_white[i][j];
                        imax = i;
                        jmax = j;
                    }
                }
            }
        }
        if (max > 0)
            return (new Point(imax, jmax));
        else
            return (new Point(-1, -1));
    }

    Point tactics_3(Board bd) {
        // ひっくりかえせる相手の石の数と盤面の特性（角、端が有利）を考慮してマス目を選ぶ
        // 優先順位 石の数→角→端
        int max = 0;
        int imax = 0;
        int jmax = 0;
        if (color == Stone.black) {
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (bd.eval_black[i][j] > max) {
                        max = bd.eval_black[i][j];
                        imax = i;
                        jmax = j;
                    } else if (bd.eval_black[i][j] == max
                            && ((i == 0 && j == 0) || (i == 0 && j == 7) || (i == 7 && j == 0) || (i == 7 && j == 7))) {
                        max = bd.eval_black[i][j];
                        imax = i;
                        jmax = j;
                    } else if (bd.eval_black[i][j] == max && (i == 0 || i == 7 || j == 0 || j == 7)) {
                        max = bd.eval_black[i][j];
                        imax = i;
                        jmax = j;
                    }
                }
            }
        } else if (color == Stone.white) {
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (bd.eval_white[i][j] > max) {
                        max = bd.eval_white[i][j];
                        imax = i;
                        jmax = j;
                    } else if (bd.eval_black[i][j] == max
                            && ((i == 0 && j == 0) || (i == 0 && j == 7) || (i == 7 && j == 0) || (i == 7 && j == 7))) {
                        max = bd.eval_black[i][j];
                        imax = i;
                        jmax = j;
                    } else if (bd.eval_black[i][j] == max && (i == 0 || i == 7 || j == 0 || j == 7)) {
                        max = bd.eval_black[i][j];
                        imax = i;
                        jmax = j;
                    }
                }
            }
        }
        if (max > 0)
            return (new Point(imax, jmax));
        else
            return (new Point(-1, -1));
    }

    Point tactics_4(Board bd) {
        // ひっくりかえせる相手の石の数と盤面の特性（角、端が有利）を考慮してマス目を選ぶ
        // 優先順位 角→端→石の数
        int max = 0;
        int imax = 0;
        int jmax = 0;
        if (color == Stone.black) {
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (bd.eval_black[i][j] > max
                            && ((i == 0 && j == 0) || (i == 0 && j == 7) || (i == 7 && j == 0) || (i == 7 && j == 7))) {
                        max = bd.eval_black[i][j];
                        imax = i;
                        jmax = j;
                    } else if (bd.eval_black[i][j] > max && (i == 0 || i == 7 || j == 0 || j == 7)) {
                        max = bd.eval_black[i][j];
                        imax = i;
                        jmax = j;
                    } else if (bd.eval_black[i][j] >= max) {
                        max = bd.eval_black[i][j];
                        imax = i;
                        jmax = j;
                    }
                }
            }
        } else if (color == Stone.white) {
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (bd.eval_white[i][j] > max
                            && ((i == 0 && j == 0) || (i == 0 && j == 7) || (i == 7 && j == 0) || (i == 7 && j == 7))) {
                        max = bd.eval_white[i][j];
                        imax = i;
                        jmax = j;
                    } else if (bd.eval_white[i][j] > max && (i == 0 || i == 7 || j == 0 || j == 7)) {
                        max = bd.eval_white[i][j];
                        imax = i;
                        jmax = j;
                    } else if (bd.eval_white[i][j] >= max) {
                        max = bd.eval_white[i][j];
                        imax = i;
                        jmax = j;
                    }
                }
            }
        }
        if (max > 0)
            return (new Point(imax, jmax));
        else
            return (new Point(-1, -1));
    }

    Point nextMove(Board bd, Point p, int n) {
        if (type == type_human)
            return p;
        else if (type == type_computer)
            if (n == 0)
                return tactics_0(bd);
            else if (n == 1)
                return tactics_1(bd);
            else if (n == 2)
                return tactics_2(bd);
            else if (n == 3)
                return tactics_3(bd);
            else if (n == 4)
                return tactics_4(bd);
        return (new Point(-1, -1));
    }
}

public class Reversi extends JPanel {
    public final static int UNIT_SIZE = 80;
    private Board board = new Board();
    private int turn;
    private Player[] player = new Player[2];
    private static int[] n = new int[2];

    public Reversi() {
        setPreferredSize(new Dimension(UNIT_SIZE * 10, UNIT_SIZE * 10));
        addMouseListener(new MouseProc());
        player[0] = new Player(Stone.black, Player.type_computer);
        player[1] = new Player(Stone.white, Player.type_computer);
        for (int i = 0; i < 2; i++) {
            if (player[i].getType() == 1) {
                Scanner sc = new Scanner(System.in);
                System.out.print("戦略(1,2,3,4):");
                n[i] = sc.nextInt();
            }
        }
        turn = Stone.black;
        board.evalulateBoard();
    }

    public void paintComponent(Graphics g) {
        String msg1 = "";
        board.paint(g, UNIT_SIZE);
        g.setColor(Color.white);
        if (turn == Stone.black)
            msg1 = "黒の番です";
        else
            msg1 = "白の番です";
        if (player[turn - 1].getType() == Player.type_computer)
            msg1 += "(考えています）";
        String msg2 = "[黒:" + board.countStone(Stone.black) + ",白" + board.countStone(Stone.white) + "]";
        g.drawString(msg1, UNIT_SIZE / 2, UNIT_SIZE / 2);
        g.drawString(msg2, UNIT_SIZE / 2, 19 * UNIT_SIZE / 2);
    }

    public static void main(String[] args) {
        JFrame f = new JFrame();
        f.getContentPane().setLayout(new FlowLayout());
        f.getContentPane().add(new Reversi());
        f.pack();
        f.setResizable(false);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
    }

    void EndMessageDialog() {
        int black = board.countStone(Stone.black);
        int white = board.countStone(Stone.white);
        String str = "[黒:" + black + ", 白:" + white + " ] で";
        if (black > white)
            str += "黒の勝ち";
        else if (black < white)
            str += "白の勝ち";
        else
            str += "引き分け";
        JOptionPane.showMessageDialog(this, str, "ゲーム終了", JOptionPane.INFORMATION_MESSAGE);
        System.exit(0);
    }

    void MessageDialog(String str) {
        JOptionPane.showMessageDialog(this, str, "情報", JOptionPane.INFORMATION_MESSAGE);
    }

    void changeTurn() {
        if (turn == Stone.black)
            turn = Stone.white;
        else if (turn == Stone.white)
            turn = Stone.black;
    }

    class MouseProc extends MouseAdapter {
        public void mouseClicked(MouseEvent me) {
            Point point = me.getPoint();
            int btn = me.getButton();
            Point gp = new Point();
            gp.x = point.x / UNIT_SIZE - 1;
            gp.y = point.y / UNIT_SIZE - 1;
            if (!board.isOnBoard(gp.x, gp.y))
                return;
            removeMouseListener(this);
            if (player[turn - 1].getType() == Player.type_human) {
                if ((player[turn - 1].getColor() == Stone.black && board.num_grid_black == 0)
                        || (player[turn - 1].getColor() == Stone.white && board.num_gird_white == 0)) {
                    MessageDialog("あなたはパスです");
                    changeTurn();
                    repaint();
                    // board.printBoard();
                    board.evalulateBoard();
                    // board.printEval();
                } else if ((player[turn - 1].getColor() == Stone.black && board.eval_black[gp.x][gp.y] > 0)
                        || (player[turn - 1].getColor() == Stone.white && board.eval_white[gp.x][gp.y] > 0)) {
                    Point nm = player[turn - 1].nextMove(board, gp, n[turn - 1]);
                    board.setStoneAndReverse(nm.x, nm.y, player[turn - 1].getColor());
                    changeTurn();
                    repaint();
                    // board.printBoard();
                    board.evalulateBoard();
                    // board.printEval();
                    if (board.num_grid_black == 0 && board.num_gird_white == 0)
                        EndMessageDialog();
                }
                if (player[turn - 1].getType() == Player.type_human)
                    addMouseListener(this);
            }
            if (player[turn - 1].getType() == Player.type_computer) {
                Thread th = new TacticsThread();
                th.start();
            }
        }

        class TacticsThread extends Thread {
            public void run() {
                try {
                    Thread.sleep(1);
                    Point nm = player[turn - 1].nextMove(board, new Point(-1, -1), n[turn - 1]);
                    if (nm.x == -1 && nm.y == -1) {
                        MessageDialog("相手はパスです");
                    } else {
                        board.setStoneAndReverse(nm.x, nm.y, player[turn - 1].getColor());
                    }
                    changeTurn();
                    repaint();
                    // board.printBoard();
                    board.evalulateBoard();
                    // board.printEval();
                    addMouseListener(new MouseProc());
                    if (board.num_grid_black == 0 && board.num_gird_white == 0) {
                        EndMessageDialog();
                    }
                    if (player[turn - 1].getType() == Player.type_computer) {
                        Thread th = new TacticsThread();
                        th.start();
                    }
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
            }
        }
    }
}