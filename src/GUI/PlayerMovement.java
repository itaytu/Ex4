package GUI;

import Geom.Point3D;

public class PlayerMovement extends Thread {

    private Board board;
    private int numOfclicks = 0;

    public PlayerMovement(Board board) {
        this.board = board;
    }

    @Override
    public void run() {
        double[] azimut = new double[3];
        while(board.getPlay().isRuning()) {
            Point3D stepPoint = new Point3D(board.getMovementX(), board.getMovementY());

            if(numOfclicks+1 == board.getNumOfclicks()) {
                azimut = board.getCoords().azimuth_elevation_dist(board.getGame().getPlayer().getPoint(), stepPoint);
                numOfclicks++;
            }

            board.getPlay().rotate(azimut[0]);
            board.getGame().update(board.getPlay());
            board.updateGUI();

            try {
                sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        board.throwMessage();
    }
}
