package vivyclient.print.table;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.Hashtable;
import vivyclient.print.table.TableCell;
import vivyclient.print.table.TableColumn;
import vivyclient.print.table.TableRegion;
import vivyclient.print.table.TableRow;

public class TextLayoutUsingTableCell extends TableRegion implements TableCell {
   private int xSpan;
   private TableRow row;
   private TableColumn startColumn;
   private double preferredWidth;
   private String printString;
   private LineBreakMeasurer lineMeasurer;
   private int textEnd;
   private Font lastPrintFont = null;
   private FontRenderContext lastFontRenderContext = null;
   private double lastPrintHeight = -1.0D;
   private double lastPrintWidth = -1.0D;

   public TextLayoutUsingTableCell(Object value, TableRow row, TableColumn startColumn, int xSpan, Font font, Color foreColour) {
      super((TableRegion)row);
      this.row = row;
      this.startColumn = startColumn;
      this.xSpan = xSpan;
      this.setFont(font);
      this.setForeColour(foreColour);
      row.addCell(this);
      if(value != null && value.toString().trim().length() != 0) {
         this.printString = value.toString();
      } else {
         this.printString = null;
      }

   }

   public void initialise(Graphics2D g) {
      if(this.printString != null) {
         Hashtable map = new Hashtable();
         map.put(TextAttribute.FONT, this.getEffectiveFont());
         AttributedCharacterIterator text = (new AttributedString(this.printString, map)).getIterator();
         this.textEnd = text.getEndIndex();
         this.lineMeasurer = new LineBreakMeasurer(text, g.getFontRenderContext());
         this.preferredWidth = this.print(g, 0.0F, 0.0F, Float.MAX_VALUE, true).getWidth() + 1.0D;
      } else {
         this.preferredWidth = 0.0D;
      }

   }

   public Rectangle2D print(Graphics2D g, double startY, boolean hidden) {
      return this.print(g, (float)this.startColumn.getX(), (float)startY, (float)this.getWidth(), hidden);
   }

   public Rectangle2D print(Graphics2D g, float startX, float startY, float maxWidth, boolean hidden) {
      double actualWidth = 0.0D;
      float drawPosY = startY;
      String textAlign = this.getEffectiveAlign();
      if(this.printString != null) {
         g.setColor(this.getEffectiveForeColour());
         this.lineMeasurer.setPosition(0);

         TextLayout layout;
         for(; this.lineMeasurer.getPosition() < this.textEnd; drawPosY += layout.getDescent() + layout.getLeading()) {
            layout = this.lineMeasurer.nextLayout(maxWidth);
            drawPosY += layout.getAscent();
            float lineWidth = layout.getAdvance();
            if((double)lineWidth > actualWidth) {
               actualWidth = (double)lineWidth;
            }

            if(!hidden) {
               float drawPosX = startX;
               if("E".equals(textAlign)) {
                  drawPosX = startX + (maxWidth - lineWidth);
               } else if("C".equals(textAlign)) {
                  drawPosX = startX + (maxWidth - lineWidth) / 2.0F;
               }

               layout.draw(g, drawPosX, drawPosY);
            }
         }
      }

      return new Double((double)startX, (double)startY, actualWidth, (double)(drawPosY - startY));
   }

   public String getEffectiveAlign() {
      return this.getAlign() != null?this.getAlign():(this.startColumn.getAlign() != null?this.startColumn.getAlign():super.getEffectiveAlign());
   }

   public double getWidth() {
      return this.xSpan == 1?this.startColumn.getWidth():this.startColumn.getTable().getSpanWidth(this.startColumn, this.xSpan);
   }

   public int getXSpan() {
      return this.xSpan;
   }

   public void setXSpan(int xSpan) {
      this.xSpan = xSpan;
   }

   public TableRow getRow() {
      return this.row;
   }

   public void setRow(TableRow row) {
      this.row = row;
   }

   public TableColumn getStartColumn() {
      return this.startColumn;
   }

   public void setStartColumn(TableColumn startColumn) {
      this.startColumn = startColumn;
   }

   public double getPreferredWidth() {
      return this.preferredWidth;
   }
}
