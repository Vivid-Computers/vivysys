package vivyclient.gui.sales;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import javax.swing.JInternalFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import javax.swing.event.TreeSelectionEvent;
import vivyclient.gui.sales.DispatchesDisplayContainer;
import vivyclient.gui.sales.DispatchesPanel;
import vivyclient.model.Dispatch;
import vivyclient.util.Settings;

public class DispatchFrame extends JInternalFrame implements DispatchesDisplayContainer {
   private static final String NAME = "DispatchFrame";
   private String displayType;
   private DispatchesPanel dispatchesPanel;
   private JMenuBar dispatchMenu;

   public DispatchFrame(String displayType) throws Exception {
      this.displayType = displayType;
      this.initComponents();
      this.dispatchesPanel = new DispatchesPanel(displayType, this);
      this.getContentPane().add(this.dispatchesPanel);
      this.setSize(new Dimension(Settings.getWidth("DispatchFrame"), Settings.getHeight("DispatchFrame")));
      this.setLocation(Settings.getXPos("DispatchFrame"), Settings.getYPos("DispatchFrame"));
      this.setVisible(true);
   }

   private void initComponents() {
      this.dispatchMenu = new JMenuBar();
      this.setClosable(true);
      this.setIconifiable(true);
      this.setMaximizable(true);
      this.setResizable(true);
      this.setTitle("Dispatches");
      this.addInternalFrameListener(new InternalFrameListener() {
         public void internalFrameOpened(InternalFrameEvent evt) {
         }

         public void internalFrameClosing(InternalFrameEvent evt) {
            DispatchFrame.this.frameClosing(evt);
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
      this.setJMenuBar(this.dispatchMenu);
      this.pack();
   }

   private void selectedCustomerChange(TreeSelectionEvent evt) {
   }

   private void frameClosing(InternalFrameEvent evt) {
      Settings.setWidth("DispatchFrame", (int)this.getSize().getWidth());
      Settings.setHeight("DispatchFrame", (int)this.getSize().getHeight());
      Settings.setXPos("DispatchFrame", this.getLocation().x);
      Settings.setYPos("DispatchFrame", this.getLocation().y);
      Settings.setMaximized("DispatchFrame", this.isMaximum());
      this.dispatchesPanel.closing();
      this.setVisible(false);
      this.dispose();
   }

   public void dispatchSelectionChange(Dispatch dispatch) {
   }

   public void resetJMenuBar() {
      this.dispatchMenu.removeAll();
      JPanel filler = new JPanel();
      filler.setMinimumSize(new Dimension(1, 20));
      filler.setMaximumSize(new Dimension(1, 20));
      filler.setPreferredSize(new Dimension(1, 20));
      this.dispatchMenu.add(filler);
      this.dispatchMenu.repaint();
   }

   public void addToJMenuBar(Component component) {
      this.dispatchMenu.add(component);
      this.dispatchMenu.validate();
      this.dispatchMenu.repaint();
   }

   public void setContainerCursor(Cursor cursor) {
      this.setCursor(cursor);
   }
}
