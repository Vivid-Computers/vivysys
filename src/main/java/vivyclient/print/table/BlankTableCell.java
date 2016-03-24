package vivyclient.print.table;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;
import vivyclient.print.table.AbstractTableCell;
import vivyclient.print.table.TableCell;
import vivyclient.print.table.TableColumn;
import vivyclient.print.table.TableRow;

public class BlankTableCell extends AbstractTableCell implements TableCell {
   private double height;
   private Color fillColour;

   public BlankTableCell(TableRow row, TableColumn startColumn, int xSpan, double preferredWidth, double height, Color fillColour) {
      super(row, startColumn, xSpan);
      this.setPreferredWidth(preferredWidth);
      this.height = height;
      this.fillColour = fillColour;
   }

   public void initialise(Graphics2D g) {
   }

   public Rectangle2D print(Graphics2D g, double startY, boolean hidden) {
      double startX = this.getStartColumn().getX();
      double width = this.getWidth();
      if(!hidden && this.fillColour != null) {
         g.setColor(this.fillColour);
         g.fillRect((int)startX, (int)startY, (int)width, (int)this.height);
      }

      return new Double(startX, startY, width, this.height);
   }
}
