package vivyclient.print;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.border.MatteBorder;
import vivyclient.Client;
import vivyclient.util.DialogueUtil;

public class PrintPreview extends JInternalFrame {
   protected int m_wPage;
   protected int m_hPage;
   protected int m_scale;
   protected Printable m_target;
   protected JComboBox m_cbScale;
   protected PrintPreview.PreviewContainer m_preview;

   public PrintPreview(Printable target, PageFormat pageFormat) {
      this(target, "Print Preview", pageFormat);
   }

   public PrintPreview(Printable target, String title, PageFormat pageFormat) {
      super(title);
      this.setSize(600, 400);
      this.m_target = target;
      JToolBar tb = new JToolBar();
      JButton bt = new JButton("Print");
      bt.setMnemonic('p');
      ActionListener lst = new ActionListener() {
         public void actionPerformed(ActionEvent e) {
         }
      };
      bt.addActionListener(lst);
      bt.setAlignmentY(0.5F);
      bt.setMargin(new Insets(2, 6, 2, 6));
      tb.add(bt);
      bt = new JButton("Close");
      bt.setMnemonic('c');
      lst = new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            PrintPreview.this.dispose();
         }
      };
      bt.addActionListener(lst);
      bt.setAlignmentY(0.5F);
      bt.setMargin(new Insets(2, 6, 2, 6));
      tb.add(bt);
      String[] scales = new String[]{"10 %", "25 %", "50 %", "100 %"};
      this.m_cbScale = new JComboBox(scales);
      this.m_cbScale.setSelectedIndex(3);
      lst = new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            Thread runner = new Thread() {
               public void run() {
                  String str = PrintPreview.this.m_cbScale.getSelectedItem().toString();
                  if(str.endsWith("%")) {
                     str = str.substring(0, str.length() - 1);
                  }

                  str = str.trim();

                  try {
                     PrintPreview.this.m_scale = Integer.parseInt(str);
                  } catch (NumberFormatException var7) {
                     return;
                  }

                  int w = PrintPreview.this.m_wPage * PrintPreview.this.m_scale / 100;
                  int h = PrintPreview.this.m_hPage * PrintPreview.this.m_scale / 100;
                  Component[] comps = PrintPreview.this.m_preview.getComponents();

                  for(int k = 0; k < comps.length; ++k) {
                     if(comps[k] instanceof PrintPreview.PagePreview) {
                        PrintPreview.PagePreview pp = (PrintPreview.PagePreview)comps[k];
                        pp.setScaledSize(w, h);
                     }
                  }

                  PrintPreview.this.m_preview.doLayout();
                  PrintPreview.this.m_preview.getParent().getParent().validate();
               }
            };
            runner.start();
         }
      };
      this.m_cbScale.addActionListener(lst);
      this.m_cbScale.setMaximumSize(new Dimension(60, 23));
      this.m_cbScale.setEditable(true);
      tb.addSeparator();
      tb.add(this.m_cbScale);
      this.getContentPane().add(tb, "North");
      this.m_preview = new PrintPreview.PreviewContainer();
      this.createComponents(pageFormat);
      JScrollPane ps = new JScrollPane(this.m_preview);
      this.getContentPane().add(ps, "Center");
      this.setDefaultCloseOperation(2);
   }

   protected void createComponents(PageFormat pageFormat) {
      try {
         if(pageFormat.getHeight() == 0.0D || pageFormat.getWidth() == 0.0D) {
            System.err.println("Unable to determine default page size");
            return;
         }

         this.m_wPage = (int)pageFormat.getWidth();
         this.m_hPage = (int)pageFormat.getHeight();
         this.m_scale = 100;
         int e = this.m_wPage * this.m_scale / 100;
         int h = this.m_hPage * this.m_scale / 100;
         int pageIndex = 0;

         while(true) {
            BufferedImage img = new BufferedImage(this.m_wPage, this.m_hPage, 1);
            Graphics g = img.getGraphics();
            g.setColor(Color.white);
            g.fillRect(0, 0, this.m_wPage, this.m_hPage);
            if(this.m_target.print(g, pageFormat, pageIndex) != 0) {
               this.repaint();
               break;
            }

            PrintPreview.PagePreview pp = new PrintPreview.PagePreview(e, h, img);
            this.m_preview.add(pp);
            ++pageIndex;
         }
      } catch (PrinterException var8) {
         DialogueUtil.handleException(var8, "An Exception occured while preparing the Print Preview", "Error", true, Client.getMainFrame());
      }

   }

   class PagePreview extends JPanel {
      protected int m_w;
      protected int m_h;
      protected Image m_source;
      protected Image m_img;

      public PagePreview(int w, int h, Image source) {
         this.m_w = w;
         this.m_h = h;
         this.m_source = source;
         this.m_img = this.m_source.getScaledInstance(this.m_w, this.m_h, 4);
         this.m_img.flush();
         this.setBackground(Color.white);
         this.setBorder(new MatteBorder(1, 1, 2, 2, Color.black));
      }

      public void setScaledSize(int w, int h) {
         this.m_w = w;
         this.m_h = h;
         this.m_img = this.m_source.getScaledInstance(this.m_w, this.m_h, 4);
         this.repaint();
      }

      public Dimension getPreferredSize() {
         Insets ins = this.getInsets();
         return new Dimension(this.m_w + ins.left + ins.right, this.m_h + ins.top + ins.bottom);
      }

      public Dimension getMaximumSize() {
         return this.getPreferredSize();
      }

      public Dimension getMinimumSize() {
         return this.getPreferredSize();
      }

      public void paint(Graphics g) {
         g.setColor(this.getBackground());
         g.fillRect(0, 0, this.getWidth(), this.getHeight());
         g.drawImage(this.m_img, 0, 0, this);
         this.paintBorder(g);
      }
   }

   class PreviewContainer extends JPanel {
      protected int H_GAP = 16;
      protected int V_GAP = 10;

      public Dimension getPreferredSize() {
         int n = this.getComponentCount();
         if(n == 0) {
            return new Dimension(this.H_GAP, this.V_GAP);
         } else {
            Component comp = this.getComponent(0);
            Dimension dc = comp.getPreferredSize();
            int w = dc.width;
            int h = dc.height;
            Dimension dp = this.getParent().getSize();
            int nCol = Math.max((dp.width - this.H_GAP) / (w + this.H_GAP), 1);
            int nRow = n / nCol;
            if(nRow * nCol < n) {
               ++nRow;
            }

            int ww = nCol * (w + this.H_GAP) + this.H_GAP;
            int hh = nRow * (h + this.V_GAP) + this.V_GAP;
            Insets ins = this.getInsets();
            return new Dimension(ww + ins.left + ins.right, hh + ins.top + ins.bottom);
         }
      }

      public Dimension getMaximumSize() {
         return this.getPreferredSize();
      }

      public Dimension getMinimumSize() {
         return this.getPreferredSize();
      }

      public void doLayout() {
         Insets ins = this.getInsets();
         int x = ins.left + this.H_GAP;
         int y = ins.top + this.V_GAP;
         int n = this.getComponentCount();
         if(n != 0) {
            Component comp = this.getComponent(0);
            Dimension dc = comp.getPreferredSize();
            int w = dc.width;
            int h = dc.height;
            Dimension dp = this.getParent().getSize();
            int nCol = Math.max((dp.width - this.H_GAP) / (w + this.H_GAP), 1);
            int nRow = n / nCol;
            if(nRow * nCol < n) {
               ++nRow;
            }

            int index = 0;

            for(int k = 0; k < nRow; ++k) {
               for(int m = 0; m < nCol; ++m) {
                  if(index >= n) {
                     return;
                  }

                  comp = this.getComponent(index++);
                  comp.setBounds(x, y, w, h);
                  x += w + this.H_GAP;
               }

               y += h + this.V_GAP;
               x = ins.left + this.H_GAP;
            }

         }
      }
   }
}
