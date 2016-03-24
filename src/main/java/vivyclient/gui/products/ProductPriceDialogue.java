package vivyclient.gui.products;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.math.BigDecimal;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;
import vivyclient.Client;
import vivyclient.data.TransactionContainer;
import vivyclient.exception.BusinessException;
import vivyclient.exception.UserInputException;
import vivyclient.gui.products.ProductMakeupPricePanel;
import vivyclient.model.ProductPrice;
import vivyclient.util.DialogueUtil;

public class ProductPriceDialogue extends JDialog {
   private ProductMakeupPricePanel parent;
   private ProductPrice productPrice;
   private boolean isNew;
   private JLabel lSiteName;
   private JTextField tQuantity;
   private JLabel lSpecialPrice;
   private JButton bOkay;
   private JCheckBox cSpecialPrice;
   private JTextField tUnitPrice;
   private JLabel jLabel3;
   private JLabel jLabel2;
   private JTextField tSiteName;

   public ProductPriceDialogue(ProductMakeupPricePanel parent, boolean modal, ProductPrice productPrice, boolean isNew) throws Exception {
      super(Client.getMainFrame(), modal);
      this.parent = parent;
      this.productPrice = productPrice;
      this.isNew = isNew;
      this.initComponents();
      this.setValues();
      Client mainFrame = Client.getMainFrame();
      double xPos = mainFrame.getLocation().getX() + (mainFrame.getSize().getWidth() - this.getSize().getWidth()) / 2.0D;
      double yPos = mainFrame.getLocation().getY() + (mainFrame.getSize().getHeight() - this.getSize().getHeight()) / 2.0D;
      this.setLocation((int)xPos, (int)yPos);
   }

   private void setValues() throws Exception {
      this.tSiteName.setText(this.productPrice.getSite().getName());
      if(this.isNew) {
         this.tQuantity.setText("");
         this.tUnitPrice.setText("");
         this.cSpecialPrice.setSelected(false);
      } else {
         this.tQuantity.setText(Integer.toString(this.productPrice.getQuantity()));
         this.tUnitPrice.setText(this.productPrice.getUnitPrice().toString());
         this.cSpecialPrice.setSelected(this.productPrice.getSpecialPrice());
      }

   }

   private void readInput(ProductPrice productPrice) throws Exception {
      String quantity = this.tQuantity.getText().trim();
      String price = this.tUnitPrice.getText().trim();
      boolean q = false;
      BigDecimal p = null;
      if(quantity.length() == 0) {
         throw new UserInputException("Enter Quantity", this.tQuantity);
      } else {
         int q1;
         try {
            q1 = Integer.parseInt(quantity);
         } catch (Exception var8) {
            throw new UserInputException("Invalid Quantity format: Integer required", this.tQuantity);
         }

         if(price.length() == 0) {
            throw new UserInputException("Enter Unit Price", this.tUnitPrice);
         } else {
            try {
               p = new BigDecimal(price);
            } catch (Exception var7) {
               throw new UserInputException("Invalid Unit Price format: Decimal required", this.tQuantity);
            }

            productPrice.setQuantity(q1);
            productPrice.setUnitPrice(p);
            productPrice.setSpecialPrice(this.cSpecialPrice.isSelected());
         }
      }
   }

   private void setInputToObject() throws Exception {
      if(this.isNew) {
         this.readInput(this.productPrice);
         ProductPrice.verifyProductPriceOkay(this.productPrice, true);
      } else {
         int originalQuantity = this.productPrice.getQuantity();
         BigDecimal originalPrice = this.productPrice.getUnitPrice();

         try {
            this.readInput(this.productPrice);
            ProductPrice.verifyProductPriceOkay(this.productPrice, false);
         } catch (Exception var4) {
            this.productPrice.setQuantity(originalQuantity);
            this.productPrice.setUnitPrice(originalPrice);
            throw var4;
         }
      }

   }

   private void initComponents() {
      this.lSiteName = new JLabel();
      this.tSiteName = new JTextField();
      this.jLabel2 = new JLabel();
      this.tQuantity = new JTextField();
      this.jLabel3 = new JLabel();
      this.tUnitPrice = new JTextField();
      this.bOkay = new JButton();
      this.lSpecialPrice = new JLabel();
      this.cSpecialPrice = new JCheckBox();
      this.getContentPane().setLayout(new GridBagLayout());
      this.setDefaultCloseOperation(2);
      this.setTitle("Price Details");
      this.setModal(true);
      this.addWindowListener(new WindowAdapter() {
         public void windowClosing(WindowEvent evt) {
            ProductPriceDialogue.this.closeDialog(evt);
         }
      });
      this.lSiteName.setText("Site:");
      GridBagConstraints gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.insets = new Insets(10, 5, 5, 5);
      this.getContentPane().add(this.lSiteName, gridBagConstraints);
      this.tSiteName.setText("jTextField1");
      this.tSiteName.setEnabled(false);
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridwidth = 0;
      gridBagConstraints.fill = 2;
      gridBagConstraints.ipadx = 100;
      gridBagConstraints.weightx = 1.0D;
      gridBagConstraints.insets = new Insets(10, 0, 5, 10);
      this.getContentPane().add(this.tSiteName, gridBagConstraints);
      this.jLabel2.setText("Quantity:");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 1;
      gridBagConstraints.insets = new Insets(5, 5, 5, 5);
      this.getContentPane().add(this.jLabel2, gridBagConstraints);
      this.tQuantity.setText("jTextField1");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 1;
      gridBagConstraints.gridy = 1;
      gridBagConstraints.gridwidth = 0;
      gridBagConstraints.fill = 2;
      gridBagConstraints.weightx = 1.0D;
      gridBagConstraints.insets = new Insets(5, 0, 5, 10);
      this.getContentPane().add(this.tQuantity, gridBagConstraints);
      this.jLabel3.setText("Unit Price:");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 2;
      gridBagConstraints.insets = new Insets(5, 5, 5, 5);
      this.getContentPane().add(this.jLabel3, gridBagConstraints);
      this.tUnitPrice.setText("jTextField2");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 1;
      gridBagConstraints.gridy = 2;
      gridBagConstraints.gridwidth = 0;
      gridBagConstraints.fill = 2;
      gridBagConstraints.weightx = 1.0D;
      gridBagConstraints.insets = new Insets(5, 0, 5, 10);
      this.getContentPane().add(this.tUnitPrice, gridBagConstraints);
      this.bOkay.setText("Okay");
      this.bOkay.addMouseListener(new MouseAdapter() {
         public void mouseClicked(MouseEvent evt) {
            ProductPriceDialogue.this.bOkayClicked(evt);
         }
      });
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 2;
      gridBagConstraints.gridy = 3;
      gridBagConstraints.gridheight = 2;
      gridBagConstraints.anchor = 12;
      gridBagConstraints.weightx = 1.0D;
      gridBagConstraints.weighty = 1.0D;
      gridBagConstraints.insets = new Insets(10, 0, 10, 10);
      this.getContentPane().add(this.bOkay, gridBagConstraints);
      this.lSpecialPrice.setText("On Special:");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 3;
      gridBagConstraints.insets = new Insets(5, 5, 5, 5);
      this.getContentPane().add(this.lSpecialPrice, gridBagConstraints);
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 1;
      gridBagConstraints.gridy = 3;
      gridBagConstraints.anchor = 17;
      gridBagConstraints.insets = new Insets(5, 0, 5, 10);
      this.getContentPane().add(this.cSpecialPrice, gridBagConstraints);
      this.pack();
   }

   private void bOkayClicked(MouseEvent evt) {
      try {
         this.setInputToObject();
         this.productPrice.save((TransactionContainer)null);
         if(this.isNew) {
            this.parent.newPriceAdded(this.productPrice);
         }

         this.closeDialog((WindowEvent)null);
      } catch (UserInputException var3) {
         DialogueUtil.handleUserInputException(var3, "Bad User Input:", "Error", super.getParent());
      } catch (BusinessException var4) {
         DialogueUtil.handleException(var4, "Product Price not allowed:", "Business Violation", false, super.getParent());
      } catch (Exception var5) {
         DialogueUtil.handleException(var5, "Error:", "Error", true, super.getParent());
      }

   }

   private void closeDialog(WindowEvent evt) {
      this.setVisible(false);
      this.dispose();
   }
}
