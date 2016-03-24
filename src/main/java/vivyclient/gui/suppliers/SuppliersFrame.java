package vivyclient.gui.suppliers;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import javax.swing.JInternalFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import javax.swing.event.TreeSelectionEvent;
import vivyclient.gui.common.ModelCollectionPanel;
import vivyclient.gui.common.ModelDisplayContainer;
import vivyclient.gui.suppliers.SuppliersPanel;
import vivyclient.model.BaseModel;
import vivyclient.util.Settings;

public class SuppliersFrame extends JInternalFrame implements ModelDisplayContainer {
   private static final String NAME = "SupplierFrame";
   private String displayType;
   private ModelCollectionPanel modelCollectionPanel;
   private JMenuBar frameMenu;

   public SuppliersFrame(String displayType) throws Exception {
      this.displayType = displayType;
      this.initComponents();
      this.modelCollectionPanel = new SuppliersPanel(displayType, this);
      this.getContentPane().add((JPanel)this.modelCollectionPanel);
      this.setSize(new Dimension(Settings.getWidth("SupplierFrame"), Settings.getHeight("SupplierFrame")));
      this.setLocation(Settings.getXPos("SupplierFrame"), Settings.getYPos("SupplierFrame"));
      this.setVisible(true);
   }

   private void initComponents() {
      this.frameMenu = new JMenuBar();
      this.setClosable(true);
      this.setIconifiable(true);
      this.setMaximizable(true);
      this.setResizable(true);
      this.setTitle("Suppliers");
      this.addInternalFrameListener(new InternalFrameListener() {
         public void internalFrameOpened(InternalFrameEvent evt) {
         }

         public void internalFrameClosing(InternalFrameEvent evt) {
            SuppliersFrame.this.frameClosing(evt);
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
      this.setJMenuBar(this.frameMenu);
      this.pack();
   }

   private void selectedCustomerChange(TreeSelectionEvent evt) {
   }

   private void frameClosing(InternalFrameEvent evt) {
      Settings.setWidth("SupplierFrame", (int)this.getSize().getWidth());
      Settings.setHeight("SupplierFrame", (int)this.getSize().getHeight());
      Settings.setXPos("SupplierFrame", this.getLocation().x);
      Settings.setYPos("SupplierFrame", this.getLocation().y);
      Settings.setMaximized("SupplierFrame", this.isMaximum());
      this.modelCollectionPanel.closing();
      this.setVisible(false);
      this.dispose();
   }

   public void resetJMenuBar() {
      this.frameMenu.removeAll();
      JPanel filler = new JPanel();
      filler.setMinimumSize(new Dimension(1, 22));
      filler.setMaximumSize(new Dimension(1, 22));
      filler.setPreferredSize(new Dimension(1, 22));
      this.frameMenu.add(filler);
      this.frameMenu.repaint();
   }

   public void addToJMenuBar(Component component) {
      this.frameMenu.add(component);
      this.frameMenu.validate();
      this.frameMenu.repaint();
   }

   public void setContainerCursor(Cursor cursor) {
      this.setCursor(cursor);
   }

   public void modelSelectionChange(BaseModel model) {
   }
}
