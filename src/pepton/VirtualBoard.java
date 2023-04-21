package pepton;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @apiNote 0: 1st player</p>1: 2nd player</p>2: empty disc
 */
public class VirtualBoard {
    public final int width, height;

    private final int[][] map;

    public VirtualBoard(int width, int height) {
        this.width = width;
        this.height = height;
        map = new int[width][height];
        for (int x = 0; x < width; x++) {
            Arrays.fill(map[x], 2);
        }
    }

    private VirtualBoard(int width, int height, int[][] map) {
        this.width = width;
        this.height = height;
        this.map = map.clone();
    }

    public int getState(int x, int y) {
        return map[x][y];
    }

    public int getAmountOf(int type) {
        int amount = 0;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                amount += map[x][y] == type ? 1 : 0;
            }
        }
        return amount;
    }

    public void setStartState() {
        for (int x = 0; x < width; x++) {
            Arrays.fill(map[x], 2);
        }
        int leftCenterX = width / 2 - 1;
        int bottomCenterY = height / 2 - 1;
        map[leftCenterX][bottomCenterY] = 0;
        map[leftCenterX + 1][bottomCenterY] = 1;
        map[leftCenterX][bottomCenterY + 1] = 1;
        map[leftCenterX + 1][bottomCenterY + 1] = 0;
    }

    public ArrayList<Vec2i> settablePositions(boolean side) {
        ArrayList<Vec2i> list = new ArrayList<>();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (checkTurnableDiscs(x, y, side, true).size() >= 1)
                    list.add(new Vec2i(x, y));
            }
        }

        return list;
    }

    /**
     * @param onlyOne searching ends with the first one
     * @return lists of turnable positions if you place a disc at (x,y)
     */
    public ArrayList<Vec2i> checkTurnableDiscs(int x, int y, boolean side, boolean onlyOne) {
        ArrayList<Vec2i> list = new ArrayList<>();
        Vec2i[] directions = new Vec2i[]{
                new Vec2i(1, 0), new Vec2i(1, -1), new Vec2i(0, -1), new Vec2i(-1, -1),
                new Vec2i(-1, 0), new Vec2i(-1, 1), new Vec2i(0, 1), new Vec2i(1, 1)};

        for (Vec2i vec : directions) {
            list.addAll(checkDirectionDiscs(x, y, vec, side, onlyOne));
            if (onlyOne && list.size() >= 1)
                return list;
        }
        return list;
    }

    private ArrayList<Vec2i> checkDirectionDiscs(int x, int y, Vec2i normal, boolean side, boolean onlyOne) {
        ArrayList<Vec2i> list = new ArrayList<>();
        if(getSafeState(x, y) != 2)
            return list; // return empty list as the position already has a disc or out of range.
        int amount = 0;
        int currentX = x, currentY = y;
        while (true) {
            currentX += normal.x();
            currentY += normal.y();
            int state = getSafeState(currentX, currentY);
            if (state >= 2 || state < 0) {
                amount = Integer.MIN_VALUE;
                break;
            } else if (Comparator.toBoolean(state) == side) {
                break;
            } else { // (boolean)state != type
                amount++;
            }
        }
        for (int i = 1; i <= amount; i++) {
            list.add(new Vec2i(x + normal.x() * i, y + normal.y() * i));
            if (onlyOne)
                return list;
        }
        return list;
    }

    public ArrayList<Vec2i> checkTurnableDiscs(int x, int y, boolean side) {
        return checkTurnableDiscs(x, y, side, false);
    }

    public VirtualBoard copy() {
        return new VirtualBoard(width, height, map);
    }

    /**
     * @return 3: out of range
     */
    public int getSafeState(int x, int y) {
        if (outOfRange(x, y))
            return 3;
        else
            return map[x][y];
    }

    public boolean outOfRange(int x, int y) {
        return x < 0 || width <= x || y < 0 || height <= y;
    }

    public void setStateAndUpdate(int x, int y, boolean side) {
        ArrayList<Vec2i> discs = checkTurnableDiscs(x, y, side);
        map[x][y] = Comparator.toInt(side);
        for (Vec2i disc : discs)
            map[disc.x()][disc.y()] = Comparator.toInt(side);
    }
}
