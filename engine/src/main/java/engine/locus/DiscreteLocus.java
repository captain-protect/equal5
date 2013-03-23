package engine.locus;

import java.util.Arrays;

/**
 * User: Oleksiy Pylypenko
 * At: 2/8/13  1:11 PM
 */
public class DiscreteLocus implements PixelDrawable, RowDrawable {
    private final int [][]rows;
    private final RectRange range;

    public DiscreteLocus(int[][] rows) {
        this.rows = rows;

        int width = findWidth(rows);
        range = new RectRange(0, 0, width, rows.length);
    }

    private int findWidth(int[][] rows) {
        int width = 0;
        for (int []row : rows) {
            if (row.length > 0) {
                int val = row[row.length - 1];
                if (val + 1 > width) {
                    width = val + 1;
                }
            }
        }
        return width;
    }

    @Override
    public RectRange getSize() {
        return range;
    }

    public void draw(RectRange range, PixelDrawer drawer) {
        for (int y = range.getMinY(); y < range.getMinY() + range.getHeight(); y++) {
            if (!(0 <= y && y < rows.length)) {
                continue;
            }

            for (int x : rows[y]) {
                if (!(range.getMinX() <= x && x < range.getMinX() + range.getWidth())) {
                    continue;
                }
                drawer.put(x, y);
            }
        }

    }

    public void draw(RectRange range, RowDrawer drawer) {
        for (int y = range.getMinY(); y < range.getMinY() + range.getHeight(); y++) {
            if (!(0 <= y && y < rows.length)) {
                continue;
            }

            int pos1 = Arrays.binarySearch(rows[y], range.getMinX());
            if (pos1 < 0) {
                pos1 = -(pos1+1);
            }

            int pos2 = Arrays.binarySearch(rows[y], range.getMinX() + range.getWidth());
            if (pos2 < 0) {
                pos2 = -(pos2+1);
            }

            drawer.drawRow(y, pos1, pos2, rows[y]);
        }

    }

}
