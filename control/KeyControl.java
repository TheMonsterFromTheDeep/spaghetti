package fettuccine.control;

/**
 * A KeyControl object contains a state which represents whether a certain control is active
 * or not. This control is going to be a key that the user is pressing. The KeyControl can
 * have its key object changed.
 * @author TheMonsterFromTheDeep
 */
public final class KeyControl {
    /** 
     * Stores the integer value representing the key that the control is tracking.
     * This is a value inside the KeyEvent class.
     */
    int keyCode;
    /** Stores the status of the control. If true, the key is pressed; otherwise, the key is not pressed. */
    public boolean status;
    
    /**
     * Creates a KeyControl based on the specified key code. It is the default key code
     * that they KeyControl will watch.
     * @param keyCode The key code for the KeyControl to watch.
     */
    public KeyControl(int keyCode) {
        this.keyCode = keyCode;
        status = false;
    }
    
    /**
     * Updates the KeyControl based on a specific integer key code. If the key code
     * matches the key code of the KeyControl, it will set its state to the specified
     * status. Otherwise, it will ignore the specified status and maintain its old
     * status.
     * @param keyCode The key code to check against.
     * @param newStatus The new status to update the KeyControl to if its code matches.
     */
    public void update(int keyCode, boolean newStatus) {
        if(this.keyCode == keyCode) {
            this.status = newStatus;
        }
    }
    
    /**
     * Updates the KeyControl to a new status. This will simply update the KeyControl
     * to whatever the specified status is, regardless of anything else.
     * @param newStatus The new status of the KeyControl.
     */
    public void update(boolean newStatus) {
        this.status = newStatus;
    }
    
    /**
     * Changes the KeyControl to track a different key. It will set its status
     * to false as it does not yet know the state of the new key.
     * @param keyCode The new key code to track.
     */
    public void track(int keyCode) {
        this.keyCode = keyCode;
        status = false;
    }
}