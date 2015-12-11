package fettuccine.sprite;

import fettuccine.graphics.Graphic;

/**
 * The SpriteSystem manages Sprite graphical resources and collision maps.
 * @author TheMonsterFromTheDeep
 */
public class SpriteSystem {
    SpriteData[] data;
    
    public SpriteSystem(int dataCount) {
        dataCount = dataCount >= 0 ? dataCount : 0;
        data = new SpriteData[dataCount];
    }
    
    public void addData(Graphic g, CollisionMap cm, String name) {
        for(int i = 0; i < data.length; i++) {
            if(data[i] == null) {
                data[i] = new SpriteData(g, cm, name);
                return;
            }
        }
        
        SpriteData[] tmp = data;
        data = new SpriteData[data.length + 1];
        System.arraycopy(tmp,0,data,0,tmp.length);
        data[tmp.length] = new SpriteData(g, cm, name);
    }
    
    public void packData() {
        for(int i = 0; i < data.length; i++) {
            data[i].setId(i);
            if(data[i] == null) {
                SpriteData[] tmp = data;
                data = new SpriteData[i];
                System.arraycopy(tmp,0,data,0,data.length);
                return;
            }
        }
    }
    
    public Camera getCamera() {
        Camera cam = new Camera();
        cam.graphics = new Graphic[data.length];
        for(int i = 0; i < data.length; i++) {
            cam.graphics[i] = data[i].graphic;
        }
        return cam;
    }
    
    public CollisionMap getMapById(int id) {
        for(SpriteData d : data) {
            if(d.id == id) {
                return d.getMapInstance();
            }
        }
        return null;
    }
}