package vivyclient.print.test;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.Hashtable;
import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class LineBreakSample extends JApplet {
   private LineBreakMeasurer lineMeasurer;
   private int paragraphStart;
   private int paragraphEnd;
   private static final Hashtable map = new Hashtable();
   private static AttributedString vanGogh;

   public void init() {
      this.buildUI(this.getContentPane());
   }

   public void buildUI(Container container) {
      LineBreakSample.LineBreakPanel lineBreakPanel = new LineBreakSample.LineBreakPanel();
      container.add(lineBreakPanel, "Center");
   }

   public static void main(String[] args) {
      JFrame f = new JFrame("HitTestSample");
      f.addWindowListener(new WindowAdapter() {
         public void windowClosing(WindowEvent e) {
            System.exit(0);
         }
      });
      LineBreakSample controller = new LineBreakSample();
      controller.buildUI(f.getContentPane());
      f.setSize(new Dimension(400, 250));
      f.setVisible(true);
   }

   static {
      map.put(TextAttribute.SIZE, new Float(18.0D));
      vanGogh = new AttributedString("Many people believe that Vincent van Gogh painted his best works during the two-year period he spent in Provence. Here is where he painted The Starry Night--which some consider to be his greatest work of all. However, as his artistic brilliance reached new heights in Provence, his physical and mental health plummeted.  But since I care more about lookhowlongthiswordisImadeitmyself, let\'s forget this van Goph fool!!!", map);
   }

   class LineBreakPanel extends JPanel {
      public LineBreakPanel() {
         AttributedCharacterIterator paragraph = LineBreakSample.vanGogh.getIterator();
         LineBreakSample.this.paragraphStart = paragraph.getBeginIndex();
         LineBreakSample.this.paragraphEnd = paragraph.getEndIndex();
         LineBreakSample.this.lineMeasurer = new LineBreakMeasurer(paragraph, new FontRenderContext((AffineTransform)null, false, false));
      }

      public void paintComponent(Graphics g) {
         super.paintComponent(g);
         this.setBackground(Color.white);
         Graphics2D graphics2D = (Graphics2D)g;
         Dimension size = this.getSize();
         float formatWidth = (float)size.width;
         float drawPosY = 0.0F;
         LineBreakSample.this.lineMeasurer.setPosition(LineBreakSample.this.paragraphStart);

         while(LineBreakSample.this.lineMeasurer.getPosition() < LineBreakSample.this.paragraphEnd) {
            TextLayout layout = LineBreakSample.this.lineMeasurer.nextLayout(formatWidth);
            drawPosY += layout.getAscent();
            float drawPosX;
            if(layout.isLeftToRight()) {
               drawPosX = 0.0F;
            } else {
               drawPosX = formatWidth - layout.getAdvance();
            }

            layout.draw(graphics2D, drawPosX, drawPosY);
            drawPosY += layout.getDescent() + layout.getLeading();
         }

      }
   }
}
