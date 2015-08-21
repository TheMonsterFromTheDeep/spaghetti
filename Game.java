package spaghetti;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import javax.sound.sampled.Clip;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Game extends JFrame {
    private class DrawData {
        int defaultWidth;
        int defaultHeight;
        int drawWidth;
        int drawHeight;
        int drawOffsetX;
        int drawOffsetY;
        double scaleFactorX;
        double scaleFactorY;
        
        public Graphics g;
        public BufferedImage buffer;
        
        public DrawData(int dw, int dh) {
            defaultWidth = dw;
            defaultHeight = dh;
            drawWidth = defaultWidth;
            drawHeight = defaultHeight;
            drawOffsetX = 0;
            drawOffsetY = 0;
            
            scaleFactorX = (double)defaultHeight / (double)defaultWidth;
            scaleFactorY = (double)defaultWidth / (double)defaultHeight;
            
            buffer = new BufferedImage(drawWidth, drawHeight, BufferedImage.TYPE_INT_ARGB);
            g = buffer.createGraphics();
        }
        
        public void resize(int windowWidth, int windowHeight) {
            if(windowWidth < (double) (windowHeight * scaleFactorY)) {
                drawWidth = windowWidth;
                drawHeight = (int) (windowWidth * scaleFactorX);
                drawOffsetX = 0;
                drawOffsetY = (int)Math.floor((windowHeight - drawHeight) / 2);
            }
            else {
                drawWidth = (int) (windowHeight * scaleFactorY);
                drawHeight = windowHeight;
                drawOffsetX = (int)Math.floor((windowWidth - drawWidth) / 2);
                drawOffsetY = 0;
            }
        }
    }
    
    private JPanel panel;
    
    private DrawData drawdata;
    
    private Timer t;
    
    private boolean lmbDown = false;
    private boolean rmbDown = false;
    private boolean mmbDown = false;
    
    private int tick = 0;
    
    private BufferedImage render() {
        System.err.println(drawdata.buffer.getWidth());
        BufferedImage tmp = Util.scaleImage(drawdata.buffer,drawdata.drawWidth,drawdata.drawHeight);
        System.err.println(tmp.getWidth());
        return tmp;
    }
    
    public Game(String title, int defaultWidth, int defaultHeight) {
        this.setTitle(title);
        
        drawdata = new DrawData(defaultWidth, defaultHeight);
        
        t = new Timer(10, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                draw(tick,drawdata.g);
                loop(tick);
                tick++;
            }
        });
        ComponentListener cl = new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent ce) {
                drawdata.resize(panel.getWidth(), panel.getHeight());
                onresize();
            }

            @Override
            public void componentMoved(ComponentEvent ce) {
                onmove();
            }

            @Override
            public void componentShown(ComponentEvent ce) {
                onshown();
            }

            @Override
            public void componentHidden(ComponentEvent ce) {
                onhidden();
            }
        };
        this.addComponentListener(cl);
        KeyListener kl = new KeyListener() {
            @Override
            public void keyTyped(KeyEvent ke) {
                onkeytype(ke);
            }

            @Override
            public void keyPressed(KeyEvent ke) {
                onkeydown(ke);
            }

            @Override
            public void keyReleased(KeyEvent ke) {
                onkeyup(ke);
            }
        };
        this.addKeyListener(kl);
        MouseListener ml = new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent me) {
            }

            @Override
            public void mousePressed(MouseEvent me) {
                if(me.getButton() == MouseEvent.BUTTON1) { lmbDown = true; }
                if(me.getButton() == MouseEvent.BUTTON2) { mmbDown = true; }
                if(me.getButton() == MouseEvent.BUTTON3) { rmbDown = true; }
            }

            @Override
            public void mouseReleased(MouseEvent me) {
                if(me.getButton() == MouseEvent.BUTTON1) { lmbDown = false; }
                if(me.getButton() == MouseEvent.BUTTON2) { mmbDown = false; }
                if(me.getButton() == MouseEvent.BUTTON3) { rmbDown = false; }
            }

            @Override
            public void mouseEntered(MouseEvent me) {
            }

            @Override
            public void mouseExited(MouseEvent me) {
            }
        };
        this.addMouseListener(ml);
        panel = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                g.setColor(Color.BLACK);
                g.fillRect(0, 0, this.getWidth(), this.getHeight());
                g.drawImage(render(), drawdata.drawOffsetX, drawdata.drawOffsetY, null);
                panel.repaint();
            }
        };
        panel.setPreferredSize(new Dimension(defaultWidth, defaultHeight));
        this.add(panel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        
        setup();
        
        this.setVisible(true);
    }
    
    public final void run() { t.start(); }
    
    protected final void setIcon(BufferedImage b) {
        this.setIconImage(b);
    }
    
    protected final void setIcon(String relativePath) {
        this.setIconImage(Util.loadImage(relativePath));
    }
    
    protected void setup() { };
    
    protected void draw(int tick, Graphics g) { }
    protected void loop(int tick) { }
    
    protected void onresize() { }
    protected void onmove() { }
    protected void onshown() { }
    protected void onhidden() { }
    
    protected void onkeydown(KeyEvent e) { }
    protected void onkeyup(KeyEvent e) { }
    protected void onkeytype(KeyEvent e) { }
    
    protected void onmousedown(MouseEvent e) { }
    protected void onmouseup(MouseEvent e) { }
    protected void onmouseclick(MouseEvent e) { }
    
    protected final Graphics graphics() { return drawdata.g; }
    protected final int tick() { return tick; }
    
    protected final boolean leftMouseDown() { return lmbDown; }
    protected final boolean middleMouseDown() { return mmbDown; }
    protected final boolean rightMouseDown() { return rmbDown; }
    
    protected final int mouseX() {
        int screenx = MouseInfo.getPointerInfo().getLocation().x - this.getLocationOnScreen().x - this.getInsets().left;
        return (int)((drawdata.defaultWidth * (screenx - drawdata.drawOffsetX)) / drawdata.drawWidth);

    }
    
    protected final int mouseY() {
        int screeny = MouseInfo.getPointerInfo().getLocation().y - this.getLocationOnScreen().y - this.getInsets().top;
        return (int)((drawdata.defaultHeight * (screeny - drawdata.drawOffsetY)) / drawdata.drawHeight);
    }
    
    protected final void forcePlayClip(Clip c) {
        c.setFramePosition(0);
        c.start();
    }
    
    protected final void playClip(Clip c) {
        if(!c.isRunning()) {
            c.setFramePosition(0);
            c.start();
        }
    }
}