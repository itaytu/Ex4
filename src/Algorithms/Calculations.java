package Algorithms;

import Coords.MyCoords;
import Elements.Block;
import Elements.Fruit;
import Elements.Game;

import Converters.pixelsCoordsConverter;
import Geom.Point3D;
import Graph.Graph;
import Utils.GraphObject;
import Utils.LineOfSight;
import Graph.Graph_Algo;
import Graph.Node;

import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Calculations {

    private pixelsCoordsConverter converter;
    private MyCoords coords = new MyCoords();

   // private ArrayList<int[]> pixelArray;
   // private ArrayList<Point3D> pointsGPS;

    private ArrayList<GraphObject> graphObjects;

    private ArrayList<String> shortestPath;
    private ArrayList<Double> pathsDistance;
    private ArrayList<GraphObject> fruitObjects;
    private ArrayList<GraphObject> finalPath;

    private ArrayList<ArrayList<String>> paths;

    private Game game;

    private LineOfSight lineOfSight;
    private Graph graph;

    private static boolean fruitINIT;

    public Calculations(Game game, int width, int height) {

        this.game = game;
        this.lineOfSight = new LineOfSight(game.getBlockArrayList(), width, height);
        this.graph = new Graph();

      //  pointsGPS = new ArrayList<>();
       // pixelArray = new ArrayList<>();
        graphObjects = new ArrayList<>();
        shortestPath = new ArrayList<>();
        pathsDistance = new ArrayList<>();
        finalPath = new ArrayList<>();
        paths = new ArrayList<>();
        fruitObjects = new ArrayList<>();

        converter = new pixelsCoordsConverter( width, height, 35.20238, 35.21236, 32.10190, 32.10569);
        fruitINIT = false;
    }

    public void INIT(){

        GraphObject.resetID();

        convertBlockAndPlayerToPixels();

        for(Fruit f : game.getFruitArrayList()) {

            //This function converts all the GPS points to pixels.
            convertFruitToPixels(f);

            //This function creates GraphObjects for all the points.
         //   createGraphObject();

            //This function checks for all the graphObjects their neighbors and adds them.
            addNeighbors();

            System.out.println(graphObjects.get(graphObjects.size()-1).getID());
            //This function creates the graph for the graphObjects
            createGraph();

            //Use the Algorithm to create path
            createPath();

            //Add the path to an ArrayList of paths.
            addPath();

            //This function resets all the Arrays.
            reset();
        }

        //This function sets the minimum path
        minPath();
      //  for(int i = 0; i < shortestPath.size(); i++)
       // System.out.println(shortestPath.get(i));

    }


    /**
     * This function converts the points of the player, block edges and closest fruit from GPS
     * coordinates to pixels.
     */

    private void convertBlockAndPlayerToPixels() {

        int [] playerPoint = converter.gps2Pixels(game.getPlayer().getPoint());
        Point3D pixelPoint = new Point3D(playerPoint[0], playerPoint[1]);
        GraphObject playerObject = new GraphObject(pixelPoint, game.getPlayer().getPoint());
        graphObjects.add(playerObject);

      // pixelArray.add(playerPoint);
      // pointsGPS.add(game.getPlayer().getPoint());

        if(!game.getBlockArrayList().isEmpty()) {
            for (Block b : game.getBlockArrayList()) {
                correctEdgePixels(b);
            }
        }

        //System.out.println("CLOSEST FRUIT: " + closestFruit.getPoint().get_x() + ", " + closestFruit.getPoint().get_y())
    }

    private void convertFruitToPixels(Fruit f) {
        int [] fruitPoint = converter.gps2Pixels(f.getPoint());
        Point3D fruitPixels = new Point3D(fruitPoint[0], fruitPoint[1]);
     //   GraphObject fruitObject = new GraphObject(fruitPixels, f.getPoint());
        if(!fruitINIT) {
            GraphObject fruitObject = new GraphObject(fruitPixels, f.getPoint());
            graphObjects.add(fruitObject);
            fruitObjects.add(fruitObject);
            fruitINIT = true;
        }
        else {
            graphObjects.remove(graphObjects.size()-1);
            GraphObject.decreaseID();
            GraphObject fruitObject = new GraphObject(fruitPixels, f.getPoint());
            graphObjects.add(fruitObject);
            fruitObjects.add(fruitObject);
        }

    }

    /**
     * This function adds pixels to the edges of every block so that the player
     * wont intersect with the edge itself.
     *
     * @param b
     */
    private void correctEdgePixels(Block b) {

        int [] LeftTop = converter.gps2Pixels(b.getMaxLeft());
        LeftTop[0] = LeftTop[0]-2;
        LeftTop[1] = LeftTop[1]-2;
        Point3D leftTopPixels = new Point3D(LeftTop[0], LeftTop[1]);
        Point3D leftTopGPS = converter.toCoords(LeftTop[0], LeftTop[1]);
        GraphObject leftTopObject = new GraphObject(leftTopPixels, leftTopGPS);
        graphObjects.add(leftTopObject);
       //pointsGPS.add(b.getMaxLeft());

        int [] RightTop = converter.gps2Pixels(b.getMaxRight());
        RightTop[0] = RightTop[0]+2;
        RightTop[1] = RightTop[1]-2;
        Point3D rightTopPixels = new Point3D(RightTop[0], RightTop[1]);
        Point3D rightTopGPS = converter.toCoords(RightTop[0], RightTop[1]);
        GraphObject rightTopObject = new GraphObject(rightTopPixels, rightTopGPS);
        graphObjects.add(rightTopObject);
       // pixelArray.add(RightTop);
       // pointsGPS.add(b.getMaxRight());

        int [] RightBottom = converter.gps2Pixels(b.getMinRight());
        RightBottom[0] = RightBottom[0]+2;
        RightBottom[1] = RightBottom[1]+2;
        Point3D rightBotPixels = new Point3D(RightBottom[0], RightBottom[1]);
        Point3D rightBotGPS = converter.toCoords(RightBottom[0], RightBottom[1]);
        GraphObject rightBotObject = new GraphObject(rightBotPixels, rightBotGPS);
        graphObjects.add(rightBotObject);
      //  pixelArray.add(RightBottom);
      //  pointsGPS.add(b.getMinRight());

        int [] LeftBottom = converter.gps2Pixels(b.getMinLeft());
        LeftBottom[0] = LeftBottom[0]-2;
        LeftBottom[1] = LeftBottom[1]+2;
        Point3D leftBotPixels = new Point3D(LeftBottom[0], LeftBottom[1]);
        Point3D leftBotGPS = converter.toCoords(LeftBottom[0], LeftBottom[1]);
        GraphObject leftBotObject = new GraphObject(leftBotPixels, leftBotGPS);
        graphObjects.add(leftBotObject);
     //   pixelArray.add(LeftBottom);
      //  pointsGPS.add(b.getMinLeft());
    }

    /**
     * This function takes all the points and creates graph objects for them, these objects will
     * be used when building the graph for the algorithm.
     */
/*    private void createGraphObject() {
        int index = 0;
        graphObjects = new ArrayList<>();
        for (int[] i: pixelArray) {
            Point3D p = new Point3D(i[0], i[1]);
            GraphObject graphObject = new GraphObject(p, pointsGPS.get(index));
            graphObjects.add(graphObject);
            index++;
        }
    }*/

    /**
     * This function checks for every two points except for the fruit if they can create a line
     * between them. The line can only be created if they have a line of sight - which means there
     * are no objects between them.
     */
    private void addNeighbors() {
        Queue<GraphObject> BFS = new LinkedList<>();

        int fruitIndex = graphObjects.size()-1;

        GraphObject player  = graphObjects.get(0);
        BFS.add(player);

        while(!BFS.isEmpty()) {
            GraphObject pollObject = BFS.poll();
            pollObject.setDone(true);

            for (GraphObject graphObject : graphObjects) {
                if(graphObject.isDone()) continue;
                else if(graphObject.getID() == pollObject.getID()) continue;
                else if(!lineOfSight.checkIntersection(pollObject.getPointPixels(), graphObject.getPointPixels())){
                    pollObject.getNeighbors().add(graphObject);
                    if(graphObject.getID()!= fruitIndex) {
                        BFS.add(graphObject);
                        graphObject.setDone(true);
                    }
                }
            }
        }

        for(GraphObject GO : graphObjects) GO.setDone(false);
    }

    /**
     * This function creates a graph from the GraphObject.
     */
    private void createGraph() {
        for(int i = 0; i < graphObjects.size(); i++) {
            Node node = new Node("" + i);
            graph.add(node);
        }

        for (GraphObject graphObject : graphObjects) {
            for (GraphObject neighbor : graphObject.getNeighbors()) {
                graph.addEdge("" + graphObject.getID(), "" + neighbor.getID(), graphObject.getPointPixels().distance2D(neighbor.getPointPixels()));
            }
        }
    }

    /**
     * This function calculates the path for the player by using the dijkstra Algorithm on the given graph.
     */
    private void createPath() {
        Graph_Algo.dijkstra(graph, "0");
        Node b = graph.getNodeByName("" + (graphObjects.size()-1));
        pathsDistance.add(b.getDist());
        shortestPath = b.getPath();
      //  shortestPath.add("" + graphObjects.get(graphObjects.size()-1).getID());

    }

    /**
     * This function adds the path calculated into the ArrayList of paths.
     */
    private void addPath() {
        paths.add(shortestPath);
    }

    /**
     * This function resets all the lists in order to calculate the next round.
     */
    private void reset() {
       // pointsGPS = new ArrayList<>();
       // pixelArray = new ArrayList<>();
        //graphObjects = new ArrayList<>();
        shortestPath = new ArrayList<>();
       // GraphObject.resetID();

        graph = new Graph();
    }

    /**
     * This function returns the shortest path from all the given paths.
     */
    private void minPath() {
        if(pathsDistance.isEmpty()) return;
        int minIndex = 0;
        double minDistance = pathsDistance.get(0);
        for(int i = 1; i < pathsDistance.size(); i++) {
            if(pathsDistance.get(i) < minDistance) {
                minDistance = pathsDistance.get(i);
                minIndex = i;
            }
        }

        shortestPath = paths.get(minIndex);
        for (int i = 0; i < shortestPath.size(); i++) {
            GraphObject tmp = returnThisObject(Integer.parseInt(shortestPath.get(i)));
            finalPath.add(tmp);
        }
        finalPath.add(fruitObjects.get(minIndex));
    }

/*    public void clearGraph() {
        graph.clear_meta_data();
    }*/

    private GraphObject returnThisObject(int ID) {
        for(GraphObject GO : graphObjects) {
            if(GO.getID() == ID) return GO;
        }
        return null;
    }

    public ArrayList<GraphObject> getGraphObjects() {
        return graphObjects;
    }

    public void setGraphObjects(ArrayList<GraphObject> graphObjects) {
        this.graphObjects = graphObjects;
    }


    public ArrayList<String> getShortestPath() {
        return shortestPath;
    }

    public void setShortestPath(ArrayList<String> shortestPath) {
        this.shortestPath = shortestPath;
    }


    public Graph getGraph() {
        return graph;
    }

    public void setGraph(Graph graph) {
        this.graph = graph;
    }

    public ArrayList<GraphObject> getFinalPath() {
        return finalPath;
    }
}
