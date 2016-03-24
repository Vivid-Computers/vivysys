package vivyclient.print.table;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import vivyclient.exception.AppRuntimeException;

public abstract class TableRegion {
   public static final String CENTER = "C";
   public static final String NORTH = "N";
   public static final String EAST = "E";
   public static final String SOUTH = "S";
   public static final String WEST = "W";
   private Font font;
   private Color foreColour;
   private Color backColour;
   private TableRegion parent;
   private String align;

   public TableRegion() {
   }

   public TableRegion(TableRegion parent) {
      this.parent = parent;
   }

   public abstract void initialise(Graphics2D var1);

   public Font getFont() {
      return this.font;
   }

   public void setFont(Font font) {
      this.font = font;
   }

   public Color getForeColour() {
      return this.foreColour;
   }

   public void setForeColour(Color foreColour) {
      this.foreColour = foreColour;
   }

   public Color getBackColour() {
      return this.backColour;
   }

   public void setBackColour(Color backColour) {
      this.backColour = backColour;
   }

   public TableRegion getParent() {
      return this.parent;
   }

   public void setParent(TableRegion parent) {
      this.parent = parent;
   }

   public Font getEffectiveFont() {
      if(this.font != null) {
         return this.font;
      } else if(this.parent != null) {
         return this.parent.getEffectiveFont();
      } else {
         throw new AppRuntimeException();
      }
   }

   public Color getEffectiveForeColour() {
      return this.foreColour != null?this.foreColour:(this.parent != null?this.parent.getEffectiveForeColour():null);
   }

   public Color getEffectiveBackColour() {
      return this.backColour != null?this.backColour:(this.parent != null?this.parent.getEffectiveBackColour():null);
   }

   public String getEffectiveAlign() {
      return this.align != null?this.align:(this.parent != null?this.parent.getEffectiveAlign():null);
   }

   public String getAlign() {
      return this.align;
   }

   public void setAlign(String align) {
      this.align = align;
   }

   protected Rectangle2D drawString(Graphics2D g, String string, double startX, double baseLineY, double width, String align) {
      Rectangle2D bounds = g.getFontMetrics().getStringBounds(string, g);
      float x = (float)startX;
      if("E".equals(align)) {
         x = (float)(startX + (width - bounds.getWidth()));
      } else if("C".equals(align)) {
         x = (float)(startX + (width - bounds.getWidth()) / 2.0D);
      }

      g.drawString(string, x, (float)baseLineY);
      return bounds;
   }

   protected double max(double d1, double d2) {
      return d1 >= d2?d1:d2;
   }
}
