package whist.interfaces;

public interface IObserver {

    //method to update the observer, used by subject
    public void update();

    //attach with subject to observe
    public void setSubject(IObservable subject);
}