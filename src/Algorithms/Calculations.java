package Algorithms;

import Elements.Block;
import Elements.Fruit;
import Elements.Game;
import Geom.Point3D;
import Graph.Graph;
import Graph.Node;
import Graph.Graph_Algo;

import Utils.GraphObject;
import Converters.pixelsCoordsConverter;
import Utils.LineOfSight;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 * This class represents the Algorithm to run the game Automatically.
 * The calculations needed for the Algorithm included:
 * Setting up points for each block edge, fruit, and player.
 * Checking paths from player to each fruit with consideration of the blocks.
 * Creating a graph in order to calculate the shortest path from player to fruit
 * using the dijkstra Algorithm.
 *
 * @author Itay Tuson and Sagi Oshri.
 */
public class Calculations {

    private pixelsCoordsConverter converter;

    private Game game;

    private GraphObject playerObject;
    private GraphObject fruitObject;
    private GraphObject targetFruit;

    private ArrayList<GraphObject> blocksArrayList;
    private ArrayList<GraphObject> fruitArrayList;
    private ArrayList<GraphObject> path;
    private ArrayList<GraphObject> finalPath;

    private ArrayList<ArrayList<GraphObject>> allPaths;

    private ArrayList<Double> pathsDistance;

    private ArrayList<String> shortestPath;
    private ArrayList<ArrayList<String>> allShortestPaths;

    private LineOfSight lineOfSight;
    private Graph graph;

    private static boolean fruitINIT;
    private static int target;

    /**
     * The constructor of this class takes the game the board width and height and initiates the
     * parameters needed.
     * Some of the parameters are: LineOfSight, creating the block Objects and the Converter.
     *
     * @param game
     * @param width
     * @param height
     */
    public Calculations(Game game, int width, int height) {

        GraphObject.resetID();

        this.game = game;
        this.lineOfSight = new LineOfSight(game.getBlockArrayList(), width, height);

        blocksArrayList = new ArrayList<>();
        converter = new pixelsCoordsConverter( width, height, 35.20238, 35.21236, 32.10190, 32.10569);

        createBlockObjects();

        fruitINIT = false;
        target = 0;
    }


    /**
     * This function initiates all the calculations needed to get the shortest path from player to fruit.
     */
    public void INIT(){
        //create a GraphObject for player.
        createPlayerObject();

        //create a GraphObject for each fruit.
        fruitArrayList = new ArrayList<>();
        for(Fruit f : game.getFruitArrayList()) {
            createFruitObject(f);
        }

        //create an ArrayList containing player, blocks and a given fruit, all as GraphObjects.
        allPaths = new ArrayList<>();
        for (GraphObject graphObject : fruitArrayList) {
            createPath(graphObject);
        }

        //Add neighbors to each GraphObject, create a Graph, and calculate path.
        pathsDistance = new ArrayList<>();
        allShortestPaths = new ArrayList<>();
        for (ArrayList<GraphObject> graphObjects : allPaths) {
            addNeighbors(graphObjects);
            Graph g = createGraph(graphObjects);
            createPath(g);
            clearNeighbors(graphObjects);
        }

        //Find the minimum path in all the paths.
        getMinPath();
    }


    /**
     * This function creates GraphObjects from Blocks.
     */
    private void createBlockObjects() {
        for (Block b : game.getBlockArrayList()) {
            int [] LeftTop = converter.gps2Pixels(b.getMaxLeft());
            LeftTop[0] = LeftTop[0]-2;
            LeftTop[1] = LeftTop[1]-2;
            Point3D leftTopPixels = new Point3D(LeftTop[0], LeftTop[1]);
            Point3D leftTopGPS = converter.toCoords(LeftTop[0], LeftTop[1]);
            GraphObject leftTopObject = new GraphObject(leftTopPixels, leftTopGPS);
            blocksArrayList.add(leftTopObject);

            int [] RightTop = converter.gps2Pixels(b.getMaxRight());
            RightTop[0] = RightTop[0]+2;
            RightTop[1] = RightTop[1]-2;
            Point3D rightTopPixels = new Point3D(RightTop[0], RightTop[1]);
            Point3D rightTopGPS = converter.toCoords(RightTop[0], RightTop[1]);
            GraphObject rightTopObject = new GraphObject(rightTopPixels, rightTopGPS);
            blocksArrayList.add(rightTopObject);

            int [] RightBottom = converter.gps2Pixels(b.getMinRight());
            RightBottom[0] = RightBottom[0]+2;
            RightBottom[1] = RightBottom[1]+2;
            Point3D rightBotPixels = new Point3D(RightBottom[0], RightBottom[1]);
            Point3D rightBotGPS = converter.toCoords(RightBottom[0], RightBottom[1]);
            GraphObject rightBotObject = new GraphObject(rightBotPixels, rightBotGPS);
            blocksArrayList.add(rightBotObject);

            int [] LeftBottom = converter.gps2Pixels(b.getMinLeft());
            LeftBottom[0] = LeftBottom[0]-2;
            LeftBottom[1] = LeftBottom[1]+2;
            Point3D leftBotPixels = new Point3D(LeftBottom[0], LeftBottom[1]);
            Point3D leftBotGPS = converter.toCoords(LeftBottom[0], LeftBottom[1]);
            GraphObject leftBotObject = new GraphObject(leftBotPixels, leftBotGPS);
            blocksArrayList.add(leftBotObject);
        }
    }

    /**
     * This function creates a GraphObject for the player.
     */
    private void createPlayerObject() {
        int [] playerPoint = converter.gps2Pixels(game.getPlayer().getPoint());
        Point3D pixelPoint = new Point3D(playerPoint[0], playerPoint[1]);
        playerObject = new GraphObject(pixelPoint, game.getPlayer().getPoint());
        playerObject.setID(0);
        GraphObject.decreaseID();
    }

    /**
     * This function creates a GraphObject for a given fruit.
     * @param f
     */
    private  void createFruitObject(Fruit f) {
        int [] fruitPoint = converter.gps2Pixels(f.getPoint());
        Point3D fruitPixels = new Point3D(fruitPoint[0], fruitPoint[1]);
        if(fruitINIT) GraphObject.decreaseID();
        fruitObject = new GraphObject(fruitPixels, f.getPoint());
        fruitArrayList.add(fruitObject);
        fruitINIT = true;
    }

    /**
     * This function creates an ArrayList of GraphObjects containing the player, block edges and a given fruit.
     * @param fruitObject
     */
    private void createPath(GraphObject fruitObject) {
        path = new ArrayList<>();
        path.add(playerObject);
        path.addAll(blocksArrayList);
        path.add(fruitObject);

        allPaths.add(path);
    }

    /**
     * This function takes the GraphObject of the player and edges and calculates each objects neighbors.
     * The calculation is made by using the BFS algorithm.
     * @param graphObjectArrayList
     */
    private void addNeighbors(ArrayList<GraphObject> graphObjectArrayList) {
        Queue<GraphObject> BFS = new LinkedList<>();

        int fruitIndex = graphObjectArrayList.size()-1;

        GraphObject player  = graphObjectArrayList.get(0);
        BFS.add(player);

        while(!BFS.isEmpty()) {
            GraphObject pollObject = BFS.poll();
            pollObject.setDone(true);

            for (GraphObject graphObject : graphObjectArrayList) {
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
        for(GraphObject GO : graphObjectArrayList) GO.setDone(false);
    }

    /**
     *This function creates a Graph for a given ArrayList of GraphObjects.
     * The Graph is created by creating nodes to each Object that has neighbors and then creating edges
     * between the nodes that have a straight path between them.
     *
     * @param graphObjectArrayList
     * @return
     */
    private Graph createGraph(ArrayList<GraphObject> graphObjectArrayList) {
        graph = new Graph();
        //int id = 0;
        for(int i = 0; i < graphObjectArrayList.size(); i++) {
            if(!graphObjectArrayList.get(i).getNeighbors().isEmpty()){
                Node node = new Node("" + i);
                node.set_id(i);
                graph.add(node);
            }
        }

        Node node = new Node("" + (graphObjectArrayList.size()-1));
        node.set_id(graphObjectArrayList.size()-1);
        graph.add(node);

        for (int i= 0; i < graphObjectArrayList.size(); i ++) {
            for (GraphObject neighbor : graphObjectArrayList.get(i).getNeighbors()) {
                graph.addEdge("" + graphObjectArrayList.get(i).getID(), "" + neighbor.getID(), graphObjectArrayList.get(i).getPointPixels().distance2D(neighbor.getPointPixels()));
            }
        }
        return graph;
    }

    /**
     * This function calculates the path in a given graph from player to fruit, with consideration to the blocks.
     * This method is used by using the dijkstra Graph Algorithm.
     * @param g
     */
    private void createPath(Graph g) {
        shortestPath = new ArrayList<>();
        target = path.size()-1;
        Graph_Algo.dijkstra(graph, "0");
        Node b = graph.getNodeByName("" + (target));
        pathsDistance.add(b.getDist());
        shortestPath = b.getPath();
        allShortestPaths.add(shortestPath);
    }

    /**
     * This function calculates the minimum path for a player from all the paths.
     */
    private void getMinPath() {
        int minIndex = 0;
        if(pathsDistance.isEmpty()) return;
<<<<<<< HEAD
=======
        //  if(pathsDistance.size()==1) return;
>>>>>>> Bugs_fix_and_statistics
        double min = pathsDistance.get(0);

        for (int i = 1; i < pathsDistance.size(); i ++) {
            if(pathsDistance.get(i) < min) {
                min = pathsDistance.get(i);
                minIndex = i;
            }
        }

        createFinalPath(allPaths.get(minIndex), allShortestPaths.get(minIndex), minIndex);
    }

    /**
     * This function creates the final path.
     * @param graphObjects
     * @param shortestPath
     * @param index
     */
    private void createFinalPath(ArrayList<GraphObject> graphObjects, ArrayList<String> shortestPath, int index) {
        finalPath = new ArrayList<>();
        for (String s : shortestPath) {
            for (GraphObject graphObject : graphObjects) {
                if(Integer.parseInt(s)== graphObject.getID()) {
                    finalPath.add(graphObject);
                    break;
                }
            }
        }
        finalPath.add(fruitArrayList.get(index));
        targetFruit = finalPath.get(finalPath.size()-1);
    }

    /**
     * This function clears the neighbors of each GraphObject after every iteration.
     * @param graphObjects
     */
    private void clearNeighbors(ArrayList<GraphObject> graphObjects) {
        for(GraphObject graphObject : graphObjects) {
            graphObject.clearNeighbors();
        }
    }

    public ArrayList<GraphObject> getFinalPath() { return finalPath; }

    public GraphObject getTargetFruit() { return targetFruit; }


    /**
     * Simple getters methods needed for the JUnit class.
     * @return ArrayList
     */
    public ArrayList<GraphObject> getPath() {
        return path;
    }

    public ArrayList<ArrayList<GraphObject>> getAllPaths() {
        return allPaths;
    }

    public ArrayList<Double> getPathsDistance() {
        return pathsDistance;
    }

    public ArrayList<String> getShortestPath() {
        return shortestPath;
    }

    public ArrayList<ArrayList<String>> getAllShortestPaths() {
        return allShortestPaths;
    }

}