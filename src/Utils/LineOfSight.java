package Utils;

import Converters.pixelsCoordsConverter;
import Elements.Block;
import Geom.Point3D;

import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class LineOfSight {

    private pixelsCoordsConverter converter;

    private ArrayList<Block> blockArrayList;
    private ArrayList<Rectangle2D> blockRectangles;

    private Line2D line;
    private Rectangle2D block;


    /**
     * This constructor gets all the blocks, a source point and a target.
     * It creates a line between the 2 given points and check if they intersect with the blocks.
     *
     * @param blocks arraylist of blocks
     * @param width board width
     * @param height board height
     */
    public LineOfSight(ArrayList<Block> blocks , int width, int height) {
        this.blockArrayList = blocks;

        blockRectangles = new ArrayList<>();

        converter = new pixelsCoordsConverter( width, height, 35.20238, 35.21236, 32.10190, 32.10569);

        createRectangles();
    }

    /**
     * This function takes all the blocks and creates rectangles from them, these rectangles are
     * needed in order to check intersection.
     */
    private void createRectangles() {
        for(Block b: blockArrayList) {
            int [] LeftTop = converter.gps2Pixels(b.getMaxLeft());
            int[] minArr = converter.gps2Pixels(b.getMinLeft());
            int[] maxArr = converter.gps2Pixels(b.getMaxRight());

            int width = maxArr[0] - LeftTop[0];
            int height = minArr[1] - LeftTop[1];

            block = new Rectangle2D.Double(LeftTop[0], LeftTop[1], width, height);

            blockRectangles.add(block);
        }
    }

    /**
     * This function checks if a line intersects with one of the rectangles.
     *
     * @param source source point
     * @param target target point
     * @return true or false
     */
    public boolean checkIntersection(Point3D source, Point3D target) {
        line = new Line2D.Double(source.get_x(), source.get_y(), target.get_x(), target.get_y());
        for(Rectangle2D block : blockRectangles) {
            if(line.intersects(block)) return true;
        }
        return false;
    }

}
