package vivyclient.print;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import vivyclient.Client;
import vivyclient.print.BasePrinter;
import vivyclient.util.DialogueUtil;

public class PreviewPage extends Component {
   private BasePrinter printable;
   private PageFormat pageFormat;
   private Dimension size;
   private double zoom = 1.0D;

   public PreviewPage(BasePrinter printable, PageFormat pageFormat) {
      this.printable = printable;
      if(pageFormat == null) {
         pageFormat = new PageFormat();
         Paper paper = new Paper();
         double multiplier = 2.834645669291339D;
         double a4Width = 210.0D * multiplier;
         double a4Height = 297.0D * multiplier;
         double xMargin = 15.0D * multiplier;
         double yMargin = 15.0D * multiplier;
         paper.setSize(a4Width, a4Height);
         paper.setImageableArea(xMargin, yMargin, a4Width - 2.0D * xMargin, a4Height - 2.0D * yMargin);
         pageFormat.setPaper(paper);
      }

      this.pageFormat = pageFormat;
      this.zoom = 1.0D;
      this.size = new Dimension((int)(pageFormat.getWidth() * this.zoom), (int)(pageFormat.getHeight() * this.zoom));
   }

   public void paint(Graphics g) {
      try {
         Graphics2D e = (Graphics2D)g;
         AffineTransform original = e.getTransform();
         AffineTransform scaler = new AffineTransform();
         scaler.scale(this.zoom, this.zoom);
         e.transform(scaler);
         g.setColor(Color.WHITE);
         g.fillRect(0, 0, (int)this.size.getWidth(), (int)this.size.getHeight());
         g.setColor(Color.LIGHT_GRAY);
         g.drawRect((int)this.pageFormat.getImageableX() - 1, (int)this.pageFormat.getImageableY() - 1, (int)this.pageFormat.getImageableWidth() + 1, (int)this.pageFormat.getImageableHeight() + 1);
         this.printable.print(g, this.pageFormat, 0);
         e.setTransform(original);
      } catch (Exception var5) {
         DialogueUtil.handleException(var5, "Error rendering print job", "Error", true, Client.getMainFrame());
         this.setVisible(false);
      }

   }

   public Dimension getMinimumSize() {
      return this.size;
   }

   public Dimension getPreferredSize() {
      return this.size;
   }
}
