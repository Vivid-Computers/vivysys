package vivyclient.print.common;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;
import vivyclient.print.table.AbstractTableCell;
import vivyclient.print.table.TableCell;
import vivyclient.print.table.TableColumn;
import vivyclient.print.table.TableRow;

public class LogoTableCell extends AbstractTableCell implements TableCell {
   private double height;
   private Color fillColour;
   private Polygon logo;

   public LogoTableCell(TableRow row, TableColumn startColumn, int xSpan, double preferredWidth, Color fillColour) {
      super(row, startColumn, xSpan);
      this.setPreferredWidth(preferredWidth);
      this.fillColour = fillColour;
      this.logo = new Polygon();
      this.logo.addPoint(0, 60);
      this.logo.addPoint(28, 0);
      this.logo.addPoint(47, 43);
      this.logo.addPoint(78, 11);
      this.logo.addPoint(88, 60);
   }

   public void initialise(Graphics2D g) {
   }

   public Rectangle2D print(Graphics2D g, double startY, boolean hidden) {
      double startX = this.getStartColumn().getX();
      double defaultWidth = this.logo.getBounds().getWidth();
      double defaultHeight = this.logo.getBounds().getHeight();
      double width = this.getWidth();
      double ratio = width / defaultWidth;
      double height = defaultHeight * ratio;
      if(!hidden && this.fillColour != null) {
         AffineTransform originalTransform = g.getTransform();
         AffineTransform transform = new AffineTransform();
         transform.translate(startX, startY);
         transform.scale(ratio, ratio);
         g.transform(transform);
         g.setColor(this.fillColour);
         g.fillPolygon(this.logo);
         g.setTransform(originalTransform);
      }

      return new Double(startX, startY, width, height);
   }
}
