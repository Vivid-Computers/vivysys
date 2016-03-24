package vivyclient.gui.sales;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.EventObject;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import vivyclient.Client;
import vivyclient.data.TransactionContainer;
import vivyclient.exception.ModelNotFoundException;
import vivyclient.exception.UserInputException;
import vivyclient.gui.products.ProductPickerDialogue;
import vivyclient.model.Product;
import vivyclient.util.DialogueUtil;

public class SaleLineProductEditor extends JPanel implements TableCellEditor {
   Product product;
   int originalProductNumber;
   private JTextField tProductName;
   private JButton bSelectProduct;
   protected static Border noFocusBorder = new EmptyBorder(1, 1, 1, 1);
   private Color unselectedForeground;
   private Color unselectedBackground;
   private ArrayList cellEditorListeners = new ArrayList();

   public SaleLineProductEditor() {
      this.setOpaque(true);
      this.setBorder(noFocusBorder);
      this.initComponents();
   }

   private void initComponents() {
      this.tProductName = new JTextField();
      this.bSelectProduct = new JButton();
      this.setLayout(new GridBagLayout());
      this.tProductName.addFocusListener(new FocusAdapter() {
         public void focusGained(FocusEvent evt) {
            SaleLineProductEditor.this.tProductNameFocusGained(evt);
         }
      });
      GridBagConstraints gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.fill = 1;
      gridBagConstraints.weightx = 1.0D;
      gridBagConstraints.weighty = 1.0D;
      this.add(this.tProductName, gridBagConstraints);
      this.bSelectProduct.setText("...");
      this.bSelectProduct.setToolTipText("Click to select Product");
      this.bSelectProduct.setAlignmentY(0.8F);
      this.bSelectProduct.setMargin(new Insets(2, 2, 2, 2));
      this.bSelectProduct.setMaximumSize(new Dimension(20, 20));
      this.bSelectProduct.setMinimumSize(new Dimension(20, 20));
      this.bSelectProduct.setPreferredSize(new Dimension(20, 20));
      this.bSelectProduct.setVerticalAlignment(3);
      this.bSelectProduct.setVerticalTextPosition(3);
      this.bSelectProduct.addMouseListener(new MouseAdapter() {
         public void mouseClicked(MouseEvent evt) {
            SaleLineProductEditor.this.bSelectProductClicked(evt);
         }
      });
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.fill = 3;
      gridBagConstraints.weighty = 1.0D;
      this.add(this.bSelectProduct, gridBagConstraints);
   }

   private void tProductNameFocusGained(FocusEvent evt) {
      if(this.product == null) {
         this.originalProductNumber = -1;
         this.tProductName.setText("");
      } else {
         this.originalProductNumber = this.product.getObjectId();
         this.tProductName.setText(String.valueOf(this.originalProductNumber));
      }

   }

   private void bSelectProductClicked(MouseEvent evt) {
      try {
         Product ex = ProductPickerDialogue.getUserSelectedProduct();
         if(ex != null) {
            this.setValue(ex);
            this.notifyAboutStop();
         } else {
            this.notifyAboutCancel();
         }
      } catch (Exception var3) {
         DialogueUtil.handleException(var3, "Error:", "Error", true, Client.getMainFrame());
      }

   }

   public void setForeground(Color c) {
      super.setForeground(c);
      this.unselectedForeground = c;
   }

   public void setBackground(Color c) {
      super.setBackground(c);
      this.unselectedBackground = c;
   }

   public void updateUI() {
      super.updateUI();
      this.setForeground((Color)null);
      this.setBackground((Color)null);
   }

   public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
      if(isSelected) {
         super.setForeground(table.getSelectionForeground());
         super.setBackground(table.getSelectionBackground());
      } else {
         super.setForeground(this.unselectedForeground != null?this.unselectedForeground:table.getForeground());
         super.setBackground(this.unselectedBackground != null?this.unselectedBackground:table.getBackground());
      }

      this.setFont(table.getFont());
      this.setBorder(noFocusBorder);
      this.setValue(value);
      this.originalProductNumber = -2;
      return this;
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
         l.editingStopped(new ChangeEvent(this));
      }

   }

   private void notifyAboutCancel() {
      for(int i = 0; i < this.cellEditorListeners.size(); ++i) {
         CellEditorListener l = (CellEditorListener)this.cellEditorListeners.get(i);
         l.editingCanceled(new ChangeEvent(this));
      }

   }

   public void cancelCellEditing() {
      System.out.println("[SaleLineProductEditor] cancelCellEditing");
      this.notifyAboutCancel();
   }

   public Object getCellEditorValue() {
      return this.product;
   }

   public boolean isCellEditable(EventObject anEvent) {
      return true;
   }

   public boolean shouldSelectCell(EventObject anEvent) {
      return true;
   }

   public boolean stopCellEditing() {
      if(this.originalProductNumber != -2) {
         String currentText = this.tProductName.getText().trim();
         if(currentText.length() == 0) {
            this.setValue((Object)null);
         } else {
            try {
               int e = Integer.parseInt(currentText);
               if(e != this.originalProductNumber) {
                  Product newProduct = new Product(e);
                  newProduct.populate((TransactionContainer)null);
                  this.setValue(newProduct);
               }
            } catch (NumberFormatException var4) {
               DialogueUtil.handleUserInputException(new UserInputException("Integer Expected", (JComponent)null), "Invalid Input", "Invalid Product Id", Client.getMainFrame());
               return false;
            } catch (ModelNotFoundException var5) {
               DialogueUtil.handleUserInputException(new UserInputException("Product Id " + var5.getModel().getObjectId() + " is invalid", (JComponent)null), "Invalid input", "Invalid Product Id", Client.getMainFrame());
               return false;
            } catch (Exception var6) {
               DialogueUtil.handleException(var6, "An error was encountered finding Product", "ERROR", true, Client.getMainFrame());
               return false;
            }
         }
      }

      this.notifyAboutStop();
      return true;
   }

   public boolean isOpaque() {
      Color back = this.getBackground();
      Container p = this.getParent();
      if(p != null) {
         p = p.getParent();
      }

      boolean colorMatch = back != null && p != null && back.equals(p.getBackground()) && p.isOpaque();
      return !colorMatch && super.isOpaque();
   }

   protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
      if(propertyName == "text") {
         super.firePropertyChange(propertyName, oldValue, newValue);
      }

   }

   public void firePropertyChange(String propertyName, boolean oldValue, boolean newValue) {
   }

   protected void setValue(Object value) {
      if(value instanceof Product) {
         this.product = (Product)value;
      } else {
         this.product = null;
      }

      this.tProductName.setText(value == null?"<no product>":value.toString());
   }

   public static class UIResource extends DefaultTableCellRenderer implements javax.swing.plaf.UIResource {
   }
}
