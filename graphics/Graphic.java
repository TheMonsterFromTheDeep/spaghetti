package fettuccine.graphics;

/**
 * A Graphic object represents graphical data of some type that can be drawn to
 * a Renderer. Graphics objects can represent image data or some other simpler data,
 * such as a colored rectangular shape.<br /><br />
 * 
 * A Graphic subclass must implement the render() method, which is how the Graphic object
 * is rendered.
 * @author TheMonsterFromTheDeep
 */
public interface Graphic {
    /**
     * Renders the graphical data represented by this Graphic object
     * to the specified Renderer.<br /><br />
     * 
     * This will, of course, apply any transformations that the Renderer
     * object contains.
     * @param r The Renderer to render the Graphic with.
     * @param x The x position to render the Graphic at.
     * @param y The y position to render the Graphic at.
     */
    void render(Renderer r, float x, float y);
    
    /** 
     * Returns a value representing the width of the Graphic. This should be equal to the width of its bounds, or similar.
     * @return The width of the Graphic's bounds.
     */
    int getWidth();
    /** 
     * Returns a value representing the height of the Graphic. This should be equal to the height of its bounds, or similar.
     * @return The height of the Graphic's bounds.
     */
    int getHeight();
}