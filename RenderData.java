package fettuccine;

public class RenderData {
    
    public int canvasWidth, canvasHeight;
    public int renderWidth, renderHeight;
    public int renderX, renderY;
    public double scaleX, scaleY;

    public RenderData(int dw, int dh) {
        renderWidth = canvasWidth = dw;
        renderHeight = canvasHeight = dh;
        renderX = 0;
        renderY = 0;
        
        scaleX = scaleY = 1;
    }

    public void recalculate(int windowWidth, int windowHeight) {
        double YXRatio = (double)canvasHeight / canvasWidth;
        double XYRatio = (double)canvasWidth / canvasHeight;
        
        if(windowWidth < (windowHeight * XYRatio)) {
            renderWidth = windowWidth;
            renderHeight = (int)(windowWidth * YXRatio);
            renderX = 0;
            renderY = (int)Math.floor((windowHeight - renderHeight) / 2);
        }
        else {
            renderWidth = (int)(windowHeight * XYRatio);
            renderHeight = windowHeight;
            renderX = (int)Math.floor((windowWidth - renderWidth) / 2);
            renderY = 0;
        }
        
        scaleX = (double)renderWidth / canvasWidth;
        scaleY = (double)renderHeight / canvasHeight;
    }
}