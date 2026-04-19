package ui;

import java.awt.Color;

public class LightMode implements Theme {
    public Color background()         { return new Color(245, 245, 245); }
    public Color cardColor()          { return new Color(255, 255, 255); }
    public Color cardBorderColor()    { return new Color(210, 210, 210); }
    public Color subtleBorderColor()  { return new Color(230, 230, 230); }
    public Color primaryTextColor()   { return new Color(33, 33, 33); }
    public Color mutedTextColor()     { return new Color(115, 115, 115); }
    public Color sidebarColor()       { return new Color(238, 238, 238); }
    
    // Status colors: Backgrounds are lighter (pastel), Text is darker for legibility
    public Color onTimeColor()        { return new Color(215, 240, 225); }
    public Color onTimeTextColor()    { return new Color(25, 80, 50); }
    
    public Color warningColor()       { return new Color(255, 240, 200); }
    public Color warningTextColor()   { return new Color(110, 80, 10); }
    
    public Color lateColor()          { return new Color(255, 215, 215); }
    public Color lateTextColor()      { return new Color(150, 30, 30); }
    
    public Color transparentColor()   { return new Color(0, 0, 0, 0); }
}