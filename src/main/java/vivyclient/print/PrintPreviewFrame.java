package vivyclient.print;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import vivyclient.util.Settings;

public class PrintPreviewFrame extends JInternalFrame {
   private static final String NAME = "PrintPreviewFrame";
   private JScrollPane displayScrollPane;
   private JPanel displayPanel;

   public PrintPreviewFrame() throws Exception {
      this.initComponents();
      this.setSize(new Dimension(Settings.getWidth("PrintPreviewFrame"), Settings.getHeight("PrintPreviewFrame")));
      this.setLocation(Settings.getXPos("PrintPreviewFrame"), Settings.getYPos("PrintPreviewFrame"));
      this.setVisible(true);
   }

   public void addComponent(Component component) {
      GridBagConstraints gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.anchor = 11;
      gridBagConstraints.weightx = 1.0D;
      this.displayPanel.add(component, gridBagConstraints);
      this.repaint();
   }

   private void initComponents() {
      this.displayScrollPane = new JScrollPane();
      this.displayPanel = new JPanel();
      this.setClosable(true);
      this.setIconifiable(true);
      this.setMaximizable(true);
      this.setResizable(true);
      this.setTitle("Print Preview");
      this.addInternalFrameListener(new InternalFrameListener() {
         public void internalFrameOpened(InternalFrameEvent evt) {
         }

         public void internalFrameClosing(InternalFrameEvent evt) {
            PrintPreviewFrame.this.formInternalFrameClosing(evt);
         }

         public void internalFrameClosed(InternalFrameEvent evt) {
         }

         public void internalFrameIconified(InternalFrameEvent evt) {
         }

         public void internalFrameDeiconified(InternalFrameEvent evt) {
         }

         public void internalFrameActivated(InternalFrameEvent evt) {
         }

         public void internalFrameDeactivated(InternalFrameEvent evt) {
         }
      });
      this.displayPanel.setLayout(new GridBagLayout());
      this.displayScrollPane.setViewportView(this.displayPanel);
      this.getContentPane().add(this.displayScrollPane, "Center");
      this.pack();
   }

   private void formInternalFrameClosing(InternalFrameEvent evt) {
      Settings.setWidth("PrintPreviewFrame", (int)this.getSize().getWidth());
      Settings.setHeight("PrintPreviewFrame", (int)this.getSize().getHeight());
      Settings.setXPos("PrintPreviewFrame", this.getLocation().x);
      Settings.setYPos("PrintPreviewFrame", this.getLocation().y);
      Settings.setMaximized("PrintPreviewFrame", this.isMaximum());
      this.setVisible(false);
      this.dispose();
   }
}
