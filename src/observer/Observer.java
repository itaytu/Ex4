package observer;

/**
 * Interface that defines the Observer. Will be implemented in the main application Controller.
 */
public class Observer {

    /**
     * Define the obects to subscribe for changes
     * @param o Observable to observe
     */
    public void observe(Observable o) {
        o.addObserver(this);
    }

    /**
     * The method that will fire when the Observable will change, will be implemented for the specific use of the Observer.
     * @param o The observable that changed
     */
    public void update(Observable o) {}
}
