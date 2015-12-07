package fettuccine;

import fettuccine.graphics.RenderData;
import fettuccine.graphics.Renderer;
import fettuccine.control.KeyControl;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Game extends JFrame implements ActionListener {   
    private volatile boolean fill;
    
    private KeyControl[] keyControls;
    
    protected Renderer renderer;
    private RenderData renderData;
    
    private JPanel panel;
    
    private final Timer timer;
    
    private boolean lmbDown = false;
    private boolean rmbDown = false;
    private boolean mmbDown = false;
    
    private long tick;
    
    public Game(String title, int defaultWidth, int defaultHeight, double scale) {
        super(title);
        
        renderData = new RenderData(defaultWidth, defaultHeight);
        renderData.recalculate((int)(defaultWidth * scale), (int)(defaultHeight * scale));
        renderer = new Renderer(defaultWidth, defaultHeight);
        
        tick = 0;
        
        timer = new Timer(10, this);
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent ce) {
                renderData.recalculate(panel.getWidth(), panel.getHeight());
                fill = true;
                panel.repaint();
            }
        });
        
        keyControls = new KeyControl[0];
        
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent ke) {
                for(KeyControl kc : keyControls) {
                    kc.update(ke.getKeyCode(), true);
                }
            }

            @Override
            public void keyReleased(KeyEvent ke) {
                for(KeyControl kc : keyControls) {
                    kc.update(ke.getKeyCode(), false);
                }
            }
        });
        this.addMouseListener(new MouseAdapter() {
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
        });
        
        fill = true;
               
        panel = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                if(fill) {
                    g.setColor(Color.BLACK);
                    g.fillRect(0, 0, this.getWidth(), this.getHeight());
                    
                    fill = false;
                }
                renderer.renderTo(g, renderData);
            }
        };
        panel.setPreferredSize(new Dimension(renderData.renderWidth, renderData.renderHeight));
        this.add(panel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        
        panel.setBackground(new Color(0));
        
        this.setVisible(true);
    }
    
    public Game(String title, int defaultWidth, int defaultHeight) {
        this(title, defaultWidth, defaultHeight, 1);
    }
    
    public Game(int defaultWidth, int defaultHeight, double scale) {
        this("[Unnamed Game]", defaultWidth, defaultHeight, scale);
    }
    
    public Game(int defaultWidth, int defaultHeight) {
        this(defaultWidth, defaultHeight, 1);
    }
    
    public Game(String title) {
        this(title, 480, 360);
    }
    
    public Game() {
        this(480, 360);
    }
    
    @Override
    public void actionPerformed(ActionEvent ae) {
        ++tick;
        loop(tick);
        panel.repaint(renderData.renderX, renderData.renderY, renderData.renderWidth, renderData.renderHeight);
    }
    
    public final void run() {
        setup();
        timer.start(); 
    }
    
    protected void setup() { };
    
    protected void loop(long tick) { }
    
    protected final boolean isLMBDown() { return lmbDown; }
    protected final boolean isMMBDown() { return mmbDown; }
    protected final boolean isRMBDown() { return rmbDown; }
    
    protected final int getMouseX() {
        int screenx = MouseInfo.getPointerInfo().getLocation().x - this.getLocationOnScreen().x - this.getInsets().left;
        return (int)((1 / renderData.scaleX) * (screenx - renderData.renderX));
    }
    
    protected final int getMouseY() {
        int screeny = MouseInfo.getPointerInfo().getLocation().y - this.getLocationOnScreen().y - this.getInsets().top;
        return (int)((1 / renderData.scaleY) * (screeny - renderData.renderY));
    }
    
    protected final void watchControl(KeyControl kc) {
        KeyControl[] tmp = keyControls;
        keyControls = new KeyControl[keyControls.length + 1];
        System.arraycopy(tmp,0,keyControls,0,tmp.length);
        keyControls[tmp.length] = kc;
    }
}