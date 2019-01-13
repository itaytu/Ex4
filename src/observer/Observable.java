package observer;

import java.util.ArrayList;

/**
 * This class defines an Observable, means an object that the Observer wants to subscribe for changes.
 */
public class Observable {

    private ArrayList<Observer> observers = new ArrayList<>();

    /**
     * Method that will update all the observers that subscribed, that the Object has changed.
     */
    protected void notifyObservers() {
        for (Observer o : observers) {
            o.update(this);
        }
    }

    /**
     * Add a specified Observer to the list.
     */
    public void addObserver(Observer o) {
        observers.add(o);
    }
}
