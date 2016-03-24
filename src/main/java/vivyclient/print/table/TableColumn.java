package vivyclient.print.table;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.LineMetrics;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;
import java.util.List;
import vivyclient.print.table.PrintableTable;
import vivyclient.print.table.TableCell;
import vivyclient.print.table.TableRegion;

public class TableColumn extends TableRegion {
   private String title;
   private PrintableTable table;
   private double preferredWidth;
   private int index;
   private boolean breakable;
   private double width;
   private Font titleFont;
   private Color titleForeColour;
   private String titleAlign;
   private double fillMultiplier;
   private double titleWidth;

   public TableColumn(double fillMultiplier, String title, Font titleFont, Color titleForeColour, String titleAlign, PrintableTable table) {
      super(table);
      this.fillMultiplier = fillMultiplier;
      this.table = table;
      this.title = title;
      this.titleFont = titleFont;
      this.titleForeColour = titleForeColour;
      this.titleAlign = titleAlign;
      table.addColumn(this);
   }

   public TableColumn(double fillMultiplier, PrintableTable table) {
      this(fillMultiplier, (String)null, (Font)null, (Color)null, (String)null, table);
   }

   public void initialise(Graphics2D g) {
      g.setFont(this.titleFont);
      if(this.title != null) {
         this.setTitleWidth(g.getFontMetrics().getStringBounds(this.title, g).getWidth());
      } else {
         this.titleWidth = 0.0D;
      }

      this.preferredWidth = this.titleWidth;
      List cells = this.table.getCellsForColumn(this);

      for(int i = 0; i < cells.size(); ++i) {
         TableCell cell = (TableCell)cells.get(i);
         cell.initialise(g);
         if(cell.getXSpan() == 1 && cell.getPreferredWidth() > this.preferredWidth) {
            this.preferredWidth = cell.getPreferredWidth();
         }
      }

   }

   public Rectangle2D printTitle(Graphics2D g, double startY, boolean hidden) {
      g.setFont(this.titleFont);
      g.setColor(this.titleForeColour);
      double height = 0.0D;
      if(this.title != null) {
         LineMetrics lm = g.getFontMetrics().getLineMetrics(this.title, g);
         if(!hidden) {
            this.drawString(g, this.title, this.getX(), startY + (double)lm.getAscent(), this.getWidth(), this.titleAlign);
         }

         height = (double)lm.getHeight();
      }

      return new Double(this.getX(), startY, this.getWidth(), height);
   }

   public double getX() {
      return this.table.getX(this);
   }

   public String getTitle() {
      return this.title;
   }

   public void setTitle(String title) {
      this.title = title;
   }

   public PrintableTable getTable() {
      return this.table;
   }

   public void setTable(PrintableTable table) {
      this.table = table;
   }

   public double getPreferredWidth() {
      return this.preferredWidth;
   }

   public void setPreferredWidth(double preferredWidth) {
      this.preferredWidth = preferredWidth;
   }

   public int getIndex() {
      return this.index;
   }

   public void setIndex(int index) {
      this.index = index;
   }

   public boolean isBreakable() {
      return this.breakable;
   }

   public void setBreakable(boolean breakable) {
      this.breakable = breakable;
   }

   public double getWidth() {
      return this.width;
   }

   public void setWidth(double width) {
      this.width = width;
   }

   public Font getTitleFont() {
      return this.titleFont;
   }

   public void setTitleFont(Font titleFont) {
      this.titleFont = titleFont;
   }

   public Color getTitleForeColour() {
      return this.titleForeColour;
   }

   public void setTitleForeColour(Color titleForeColour) {
      this.titleForeColour = titleForeColour;
   }

   public String getTitleAlign() {
      return this.titleAlign;
   }

   public void setTitleAlign(String titleAlign) {
      this.titleAlign = titleAlign;
   }

   public double getFillMultiplier() {
      return this.fillMultiplier;
   }

   public void setFillMultiplier(double fillMultiplier) {
      this.fillMultiplier = fillMultiplier;
   }

   public double getTitleWidth() {
      return this.titleWidth;
   }

   public void setTitleWidth(double titleWidth) {
      this.titleWidth = titleWidth;
   }
}
