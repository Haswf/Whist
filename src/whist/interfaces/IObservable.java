package whist.interfaces;

public interface IObservable {
    //methods to register and unregister observers
    public void register(IObserver obj);
    public void unregister(IObserver obj);

    //method to notify observers of change
    public void notifyObservers();

    //method to get updates from subject
    public Object getUpdate(IObserver observer);
}
