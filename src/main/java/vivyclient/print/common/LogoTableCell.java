package vivyclient.print.common;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;
import java.io.IOException;
import java.io.InputStream;

import vivyclient.print.table.AbstractTableCell;
import vivyclient.print.table.TableCell;
import vivyclient.print.table.TableColumn;
import vivyclient.print.table.TableRow;

import javax.imageio.ImageIO;

public class LogoTableCell extends AbstractTableCell implements TableCell {

   private Image logo;

   public LogoTableCell(TableRow row, TableColumn startColumn, int xSpan, double preferredWidth) {
      super(row, startColumn, xSpan);
      this.setPreferredWidth(preferredWidth);
      String path = "/vivyclient/gui/images/vivid-logo.png";
      InputStream stream = LogoTableCell.class.getResourceAsStream(path);
      if (stream == null) {
         throw new RuntimeException("Could not find " + path);
      }
      try {
         logo = ImageIO.read(stream);
      } catch (IOException e) {
         throw new RuntimeException("Error loading image", e);
      }
   }

   public void initialise(Graphics2D g) {

   }

   public Rectangle2D print(Graphics2D g, double startY, boolean hidden) {
      double startX = this.getStartColumn().getX();
      g.drawImage(logo, (int)startX, (int)startY, null);
      double width = logo.getWidth(null);
      double height = logo.getHeight(null);

      return new Double(startX, startY, width, height);
   }
}
