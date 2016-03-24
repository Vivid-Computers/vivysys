package vivyclient.gui.sales;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.EventObject;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import vivyclient.Client;
import vivyclient.gui.sales.PriceForProductContainer;
import vivyclient.model.Product;
import vivyclient.model.ProductPrice;
import vivyclient.util.DialogueUtil;

public class SaleLinePriceEditor implements TableCellEditor, ActionListener {
   JComboBox comboBox = new JComboBox();
   protected static Border noFocusBorder = new EmptyBorder(1, 1, 1, 1);
   private Color unselectedForeground;
   private Color unselectedBackground;
   private ArrayList cellEditorListeners = new ArrayList();

   public SaleLinePriceEditor() {
      this.comboBox.setOpaque(true);
      this.comboBox.setBorder(noFocusBorder);
      this.comboBox.setLightWeightPopupEnabled(false);
      this.comboBox.setEditable(true);
      this.comboBox.addActionListener(this);
   }

   public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
      try {
         if(isSelected) {
            this.comboBox.setForeground(table.getSelectionForeground());
            this.comboBox.setBackground(table.getSelectionBackground());
         } else {
            this.comboBox.setForeground(this.unselectedForeground != null?this.unselectedForeground:table.getForeground());
            this.comboBox.setBackground(this.unselectedBackground != null?this.unselectedBackground:table.getBackground());
         }

         this.comboBox.setFont(table.getFont());
         this.comboBox.setBorder(noFocusBorder);
         if(value instanceof PriceForProductContainer) {
            Product e = ((PriceForProductContainer)value).getProduct();
            BigDecimal price = ((PriceForProductContainer)value).getUnitPrice();
            if(e != null) {
               ProductPrice[] prices = new ProductPrice[e.getProductPriceCount()];

               for(int i = 0; i < e.getProductPriceCount(); ++i) {
                  prices[i] = e.getProductPrice(i);
               }

               this.comboBox.setModel(new DefaultComboBoxModel(prices));
            }

            this.comboBox.setSelectedItem(price);
         } else {
            this.comboBox.setSelectedItem(value);
         }

         return this.comboBox;
      } catch (Exception var10) {
         DialogueUtil.handleException(var10, "An exception has occured while trying to set Unit Price", "ERROR", true, Client.getMainFrame());
         return new JLabel("ERROR!");
      }
   }

   public void addCellEditorListener(CellEditorListener l) {
      this.cellEditorListeners.add(l);
   }

   public void removeCellEditorListener(CellEditorListener l) {
      this.cellEditorListeners.remove(l);
   }

   private void notifyAboutStop() {
      for(int i = 0; i < this.cellEditorListeners.size(); ++i) {
         CellEditorListener l = (CellEditorListener)this.cellEditorListeners.get(i);
         l.editingStopped(new ChangeEvent(this.comboBox));
      }

   }

   private void notifyAboutCancel() {
      for(int i = 0; i < this.cellEditorListeners.size(); ++i) {
         CellEditorListener l = (CellEditorListener)this.cellEditorListeners.get(i);
         l.editingCanceled(new ChangeEvent(this.comboBox));
      }

   }

   public void cancelCellEditing() {
      this.notifyAboutCancel();
   }

   public Object getCellEditorValue() {
      return this.comboBox.getSelectedItem();
   }

   public boolean isCellEditable(EventObject anEvent) {
      return true;
   }

   public boolean shouldSelectCell(EventObject anEvent) {
      return true;
   }

   public boolean stopCellEditing() {
      this.notifyAboutStop();
      return true;
   }

   public void actionPerformed(ActionEvent e) {
      this.notifyAboutStop();
   }

   public static class UIResource extends DefaultTableCellRenderer implements javax.swing.plaf.UIResource {
   }
}
