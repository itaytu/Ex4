package GUI;

import Geom.Point3D;

public class PlayerMovement extends Thread {

    private Board board;

    public PlayerMovement(Board board) {
        this.board = board;
    }

    @Override
    public void run() {
        while((!board.getGame().getFruitArrayList().isEmpty()) && (!MaxTimeReached())) {
            Point3D stepPoint = new Point3D(board.getMovementX(), board.getMovementY());
            double[] azimut = board.getCoords().azimuth_elevation_dist(board.getGame().getPlayer().getPoint(), stepPoint);
            board.getPlay().rotate(azimut[0]);
            board.getGame().update(board.getPlay());

            board.updateGUI();

            try {
                sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean MaxTimeReached() {

        int startIndex =  board.getPlay().getStatistics().lastIndexOf("Time left:") + 10;
        int endIndex = board.getPlay().getStatistics().indexOf(", kill");
        String Timeleft = board.getPlay().getStatistics().substring(startIndex, endIndex);
        double time = Double.parseDouble(Timeleft);

        if(time <= 0) return true;
        return false;
    }

}
