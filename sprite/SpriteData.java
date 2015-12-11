package fettuccine.sprite;

import fettuccine.graphics.Graphic;

/**
 * A SpriteData contains a linked reference between a Graphic and Collision Map. It also
 * includes the name of a Sprite.
 * @author TheMonsterFromTheDeep
 */
public class SpriteData {
    Graphic graphic;
    CollisionMap map;
    
    String name;
    int id;
    
    public SpriteData(Graphic g, CollisionMap cm, String name) {
        this.graphic = g;
        this.map = cm;
        
        map.setAnchor(-((float)graphic.getWidth() / 2), -((float)graphic.getHeight() / 2));
        
        this.name = name;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public CollisionMap getMapInstance() {
        return map.createInstance();
    }
}