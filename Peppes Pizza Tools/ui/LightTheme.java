package ui;

import java.awt.Color;

public class LightTheme implements Theme {
    public Color background()        { return new Color(245, 245, 245); }
    public Color cardColor()         { return new Color(255, 255, 255); }
    public Color cardBorderColor()   { return new Color(210, 210, 210); }
    public Color subtleBorderColor() { return new Color(225, 225, 225); }
    public Color primaryTextColor()  { return new Color(30, 30, 30); }
    public Color mutedTextColor()    { return new Color(140, 140, 140); }
    public Color sidebarColor()      { return new Color(235, 235, 235); }
    public Color onTimeColor()       { return new Color(225, 245, 238); }
    public Color onTimeTextColor()   { return new Color(15, 110, 86); }
    public Color warningColor()      { return new Color(250, 238, 218); }
    public Color warningTextColor()  { return new Color(133, 79, 11); }
    public Color lateColor()         { return new Color(252, 235, 235); }
    public Color lateTextColor()     { return new Color(163, 45, 45); }
    public Color transparentColor()  { return new Color(0, 0, 0, 0); }
}