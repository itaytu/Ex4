package Algorithms;

import Elements.Block;
import Elements.Game;
import Geom.Point3D;

import java.util.ArrayList;

public class validEdges {

    private ArrayList<Block> blockArrayList;
    private ArrayList<Point3D> validEdges;

    public validEdges(ArrayList<Block> blockArrayList) {
        this.blockArrayList = blockArrayList;
        validEdges = new ArrayList<>();
        addValidEdges();
    }


    /**
     * This function runs on all the blocks and checks their edges to find out if there
     * are intersecting lines.
     */

    private void addValidEdges() {
        for(int i = 0; i < blockArrayList.size() - 1; i++) {
            Block temp = blockArrayList.get(i);
            for(int j = 0; j < blockArrayList.size(); j++) {
                if(i!=j) {
                    for (int t = 0; t < temp.getPoints().size(); t++) {
                        if (!isIn(temp.getPoints().get(t), blockArrayList.get(j)))
                            validEdges.add(temp.getPoints().get(t));
                    }
                }
            }
        }
    }

    /**
     * This function checks if a given point is inside a rectangle
     * @param point point to check
     * @param block block to check inside
     * @return boolean true or false
     */
    private boolean isIn(Point3D point, Block block) {
        double x = point.get_x();
        double y = point.get_y();

        if((block.getMinLeft().get_x()<= x) && (x<= block.getMaxRight().get_x()) &&
                (y<=block.getMaxRight().get_y()) && (block.getMinLeft().get_y()<= y))
            return true;

        return false;
    }

    public ArrayList<Point3D> getValidEdges() {
        return validEdges;
    }

}
