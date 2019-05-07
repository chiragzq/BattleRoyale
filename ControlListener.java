/**
 * Interface that should be implemented by a class that can handle controls
 */
public interface ControlListener {
    void wPressed();
    void aPressed();
    void sPressed();
    void dPressed();

    void wReleased();
    void aReleased();
    void sReleased();
    void dReleased();

    void click();

    void num1Pressed();
    void num2Pressed();
}