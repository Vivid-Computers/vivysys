package vivyclient.print.table;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.font.LineMetrics;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import vivyclient.exception.AppRuntimeException;
import vivyclient.print.PrintUtil;
import vivyclient.print.table.AbstractTableCell;
import vivyclient.print.table.TableCell;
import vivyclient.print.table.TableColumn;
import vivyclient.print.table.TableRow;

public class TextTableCell extends AbstractTableCell implements TableCell {
   private List tokenizedWords;
   private String printString;

   public TextTableCell(Object value, TableRow row, TableColumn startColumn, int xSpan, Font font, Color foreColour) {
      super(row, startColumn, xSpan);
      this.setFont(font);
      this.setForeColour(foreColour);
      if(value != null && value.toString().trim().length() != 0) {
         StringTokenizer tokenizer = new StringTokenizer(value.toString(), " \t\n\r\f", true);
         StringBuffer actualPrintString = new StringBuffer();
         this.tokenizedWords = new ArrayList();

         while(tokenizer.hasMoreTokens()) {
            String word = tokenizer.nextToken();
            if(word.trim().length() == 0) {
               this.tokenizedWords.add(" ");
               actualPrintString.append(" ");
            } else {
               this.tokenizedWords.add(word);
               actualPrintString.append(word);
            }
         }

         this.printString = actualPrintString.toString();
      } else {
         this.tokenizedWords = null;
         this.printString = null;
      }

   }

   public void initialise(Graphics2D g) {
      if(this.tokenizedWords == null) {
         this.setPreferredWidth(0.0D);
      } else {
         g.setFont(this.getEffectiveFont());
         this.setPreferredWidth(g.getFontMetrics().getStringBounds(this.printString, g).getWidth());
      }

   }

   public Rectangle2D print(Graphics2D g, double startY, boolean hidden) {
      double maxWidth = this.getWidth();
      double height = 0.0D;
      double startX = this.getStartColumn().getX();
      if(this.tokenizedWords != null) {
         g.setFont(this.getEffectiveFont());
         g.setColor(this.getEffectiveForeColour());
         LineMetrics lm = g.getFontMetrics().getLineMetrics(this.printString, g);
         if(this.getPreferredWidth() <= maxWidth) {
            height = (double)lm.getHeight();
            if(!hidden) {
               this.drawString(g, this.printString, startX, startY + (double)lm.getAscent(), this.getWidth(), this.getEffectiveAlign());
            }
         } else {
            ArrayList words = new ArrayList(this.tokenizedWords);
            double currentY = startY;
            FontMetrics fm = g.getFontMetrics();
            StringBuffer currentLine = new StringBuffer();
            int index = 0;

            while(true) {
               while(index < words.size()) {
                  String word = (String)words.get(index);
                  double currentWidth = fm.getStringBounds(currentLine.toString() + word, g).getWidth();
                  if(currentWidth > maxWidth) {
                     if(currentLine.length() == 0) {
                        String largestWord = PrintUtil.getLongestFittingString(word, true, g, maxWidth);
                        if(largestWord.length() == 0) {
                           throw new AppRuntimeException();
                        }

                        words.set(index, word.substring(largestWord.length()));
                        currentLine.append(largestWord);
                     }

                     if(!hidden) {
                        this.drawString(g, currentLine.toString(), startX, currentY + (double)lm.getAscent(), this.getWidth(), this.getEffectiveAlign());
                     }

                     currentLine = new StringBuffer();
                     currentY += (double)lm.getHeight();
                  } else {
                     if(currentLine.length() != 0 || word.trim().length() != 0) {
                        currentLine.append(word);
                     }

                     ++index;
                     if(index == words.size()) {
                        if(!hidden) {
                           this.drawString(g, currentLine.toString(), startX, currentY + (double)lm.getAscent(), this.getWidth(), this.getEffectiveAlign());
                        }

                        currentLine = new StringBuffer();
                        currentY += (double)lm.getHeight();
                     }
                  }
               }

               height = currentY - startY;
               break;
            }
         }
      }

      return new Double(startX, startY, maxWidth, height);
   }
}
