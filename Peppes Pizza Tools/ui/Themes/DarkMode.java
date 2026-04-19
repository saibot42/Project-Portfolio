package ui.Themes;

import java.awt.Color;

public class DarkMode implements Theme {
    public Color background()        { return new Color(32, 32, 32); }
    public Color cardColor()         { return new Color(44, 44, 44); }
    public Color cardBorderColor()   { return new Color(75, 75, 75); }
    public Color subtleBorderColor() { return new Color(58, 58, 58); }
    public Color primaryTextColor()  { return new Color(240, 240, 240); }
    public Color mutedTextColor()    { return new Color(100, 100, 100); }
    public Color sidebarColor()      { return new Color(44, 44, 44); }
    public Color onTimeColor()       { return new Color(39, 90, 67); }
    public Color onTimeTextColor()   { return new Color(130, 200, 160); }
    public Color warningColor()      { return new Color(100, 72, 20); }
    public Color warningTextColor()  { return new Color(220, 175, 80); }
    public Color lateColor()         { return new Color(90, 30, 30); }
    public Color lateTextColor()     { return new Color(220, 100, 100); }
    public Color transparentColor()  { return new Color(0, 0, 0, 0); }
}
