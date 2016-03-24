package vivyclient.gui.products;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import vivyclient.Client;
import vivyclient.data.TransactionContainer;
import vivyclient.exception.BusinessException;
import vivyclient.exception.UserInputException;
import vivyclient.gui.common.PanelUtil;
import vivyclient.gui.products.ProductMakeupPricePanel;
import vivyclient.gui.products.ProductPickerDialogue;
import vivyclient.model.DurationType;
import vivyclient.model.Product;
import vivyclient.model.ProductMakeup;
import vivyclient.util.DialogueUtil;

public class ProductMakeupDialogue extends JDialog {
   private ProductMakeupPricePanel parent;
   private ProductMakeup productMakeup;
   private boolean isNew;
   private Product selectedPart;
   private JLabel lDuration;
   private JButton bOkay;
   private JLabel lSiteName;
   private JPanel warrantyPanel;
   private JLabel jLabel2;
   private JTextField tWarrantyMultiplier;
   private JTextField tWarrantyComments;
   private JTextField tPartName;
   private JLabel lComments;
   private JButton bSelectPart;
   private JPanel partPanel;
   private JComboBox cWarrantyDuration;
   private JTextField tQuantity;

   public ProductMakeupDialogue(ProductMakeupPricePanel parent, boolean modal, ProductMakeup productMakeup, boolean isNew) throws Exception {
      super(Client.getMainFrame(), modal);
      this.parent = parent;
      this.productMakeup = productMakeup;
      this.isNew = isNew;
      this.initComponents();
      ArrayList paymentDurationTypes = new ArrayList();
      paymentDurationTypes.add((Object)null);
      paymentDurationTypes.addAll(DurationType.cachedFindAll(new DurationType()));
      paymentDurationTypes.remove(DurationType.BILLING_MONTH_DURATION_TYPE);
      this.cWarrantyDuration.setModel(new DefaultComboBoxModel(paymentDurationTypes.toArray()));
      this.setValues();
      Client mainFrame = Client.getMainFrame();
      double xPos = mainFrame.getLocation().getX() + (mainFrame.getSize().getWidth() - this.getSize().getWidth()) / 2.0D;
      double yPos = mainFrame.getLocation().getY() + (mainFrame.getSize().getHeight() - this.getSize().getHeight()) / 2.0D;
      this.setLocation((int)xPos, (int)yPos);
   }

   private void setValues() throws Exception {
      if(this.isNew) {
         this.selectedPart = null;
         this.tPartName.setText("");
         this.tQuantity.setText("");
         this.tWarrantyMultiplier.setText("");
         this.tWarrantyComments.setText("");
      } else {
         this.selectedPart = this.productMakeup.getPart();
         this.tPartName.setText(this.selectedPart.toString());
         this.tQuantity.setText(Integer.toString(this.productMakeup.getQuantity()));
         PanelUtil.setComboSelection(this.cWarrantyDuration, this.productMakeup.getWarrantyDurationType());
         if(this.productMakeup.getWarrantyDurationType() != null) {
            this.tWarrantyMultiplier.setText(String.valueOf(this.productMakeup.getWarrantyDurationMultiplier()));
         } else {
            this.tWarrantyMultiplier.setText("");
         }

         this.tWarrantyComments.setText(this.productMakeup.getWarrantyComments());
      }

   }

   private void readInput(ProductMakeup productMakeup) throws Exception {
      String quantity = this.tQuantity.getText().trim();
      boolean q = false;
      if(this.selectedPart == null) {
         throw new UserInputException("Select Part", this.bSelectPart);
      } else if(quantity.length() == 0) {
         throw new UserInputException("Enter Quantity", this.tQuantity);
      } else {
         int q1;
         try {
            q1 = Integer.parseInt(quantity);
         } catch (Exception var5) {
            throw new UserInputException("Invalid Quantity format: Integer required", this.tQuantity);
         }

         productMakeup.setPart(this.selectedPart);
         productMakeup.setQuantity(q1);
         productMakeup.setWarrantyDurationType((DurationType)PanelUtil.getValidatedComboItem(this.cWarrantyDuration, "Warranty Duration Type", false));
         if(productMakeup.getWarrantyDurationType() != null) {
            productMakeup.setWarrantyDurationMultiplier(PanelUtil.getValidatedIntValue(this.tWarrantyMultiplier, "Warranty Duration Multiplier", true, 1));
            productMakeup.setWarrantyComments(this.tWarrantyComments.getText().trim());
         } else {
            productMakeup.setWarrantyDurationMultiplier(-1);
            productMakeup.setWarrantyComments((String)null);
         }

      }
   }

   private void setInputToObject() throws Exception {
      this.readInput(this.productMakeup);
   }

   private void initComponents() {
      this.lSiteName = new JLabel();
      this.jLabel2 = new JLabel();
      this.tQuantity = new JTextField();
      this.bOkay = new JButton();
      this.partPanel = new JPanel();
      this.tPartName = new JTextField();
      this.bSelectPart = new JButton();
      this.warrantyPanel = new JPanel();
      this.lDuration = new JLabel();
      this.cWarrantyDuration = new JComboBox();
      this.tWarrantyMultiplier = new JTextField();
      this.tWarrantyComments = new JTextField();
      this.lComments = new JLabel();
      this.getContentPane().setLayout(new GridBagLayout());
      this.setDefaultCloseOperation(2);
      this.setTitle("Part Details");
      this.setModal(true);
      this.addWindowListener(new WindowAdapter() {
         public void windowClosing(WindowEvent evt) {
            ProductMakeupDialogue.this.closeDialog(evt);
         }
      });
      this.lSiteName.setText("Part:");
      GridBagConstraints gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 0;
      gridBagConstraints.anchor = 13;
      gridBagConstraints.insets = new Insets(7, 5, 0, 2);
      this.getContentPane().add(this.lSiteName, gridBagConstraints);
      this.jLabel2.setText("Quantity:");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 1;
      gridBagConstraints.anchor = 13;
      gridBagConstraints.insets = new Insets(2, 5, 0, 2);
      this.getContentPane().add(this.jLabel2, gridBagConstraints);
      this.tQuantity.setText("jTextField1");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 1;
      gridBagConstraints.gridy = 1;
      gridBagConstraints.fill = 2;
      gridBagConstraints.weightx = 1.0D;
      gridBagConstraints.insets = new Insets(2, 0, 0, 5);
      this.getContentPane().add(this.tQuantity, gridBagConstraints);
      this.bOkay.setText("Okay");
      this.bOkay.addMouseListener(new MouseAdapter() {
         public void mouseClicked(MouseEvent evt) {
            ProductMakeupDialogue.this.bOkayClicked(evt);
         }
      });
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 1;
      gridBagConstraints.gridy = 3;
      gridBagConstraints.anchor = 12;
      gridBagConstraints.weighty = 1.0D;
      gridBagConstraints.insets = new Insets(2, 0, 5, 5);
      this.getContentPane().add(this.bOkay, gridBagConstraints);
      this.partPanel.setLayout(new GridBagLayout());
      this.tPartName.setEditable(false);
      this.tPartName.setText("jTextField1");
      this.tPartName.setEnabled(false);
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 0;
      gridBagConstraints.fill = 2;
      gridBagConstraints.ipadx = 100;
      gridBagConstraints.weightx = 1.0D;
      gridBagConstraints.insets = new Insets(2, 0, 0, 2);
      this.partPanel.add(this.tPartName, gridBagConstraints);
      this.bSelectPart.setText("...");
      this.bSelectPart.setToolTipText("Click to select Part");
      this.bSelectPart.setMargin(new Insets(2, 2, 2, 2));
      this.bSelectPart.setMaximumSize(new Dimension(35, 20));
      this.bSelectPart.setMinimumSize(new Dimension(35, 20));
      this.bSelectPart.setPreferredSize(new Dimension(35, 20));
      this.bSelectPart.addMouseListener(new MouseAdapter() {
         public void mouseClicked(MouseEvent evt) {
            ProductMakeupDialogue.this.bSelectPartClicked(evt);
         }
      });
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 1;
      gridBagConstraints.gridy = 0;
      gridBagConstraints.insets = new Insets(2, 0, 0, 0);
      this.partPanel.add(this.bSelectPart, gridBagConstraints);
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 1;
      gridBagConstraints.gridy = 0;
      gridBagConstraints.fill = 2;
      gridBagConstraints.insets = new Insets(7, 0, 0, 5);
      this.getContentPane().add(this.partPanel, gridBagConstraints);
      this.warrantyPanel.setLayout(new GridBagLayout());
      this.warrantyPanel.setBorder(new TitledBorder("Warranty"));
      this.lDuration.setText("Duration:");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 0;
      gridBagConstraints.anchor = 13;
      gridBagConstraints.insets = new Insets(2, 5, 0, 2);
      this.warrantyPanel.add(this.lDuration, gridBagConstraints);
      this.cWarrantyDuration.setMinimumSize(new Dimension(124, 20));
      this.cWarrantyDuration.setPreferredSize(new Dimension(124, 20));
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 2;
      gridBagConstraints.gridy = 0;
      gridBagConstraints.gridwidth = 0;
      gridBagConstraints.fill = 2;
      gridBagConstraints.weightx = 1.0D;
      gridBagConstraints.insets = new Insets(2, 0, 0, 5);
      this.warrantyPanel.add(this.cWarrantyDuration, gridBagConstraints);
      this.tWarrantyMultiplier.setText("jTextField1");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 1;
      gridBagConstraints.gridy = 0;
      gridBagConstraints.fill = 2;
      gridBagConstraints.weightx = 0.2D;
      gridBagConstraints.insets = new Insets(2, 0, 0, 2);
      this.warrantyPanel.add(this.tWarrantyMultiplier, gridBagConstraints);
      this.tWarrantyComments.setText("jTextField1");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 1;
      gridBagConstraints.gridy = 1;
      gridBagConstraints.gridwidth = 0;
      gridBagConstraints.fill = 2;
      gridBagConstraints.weightx = 1.0D;
      gridBagConstraints.insets = new Insets(2, 0, 0, 5);
      this.warrantyPanel.add(this.tWarrantyComments, gridBagConstraints);
      this.lComments.setText("Comments:");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 1;
      gridBagConstraints.anchor = 13;
      gridBagConstraints.insets = new Insets(0, 0, 0, 2);
      this.warrantyPanel.add(this.lComments, gridBagConstraints);
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 2;
      gridBagConstraints.gridwidth = 0;
      gridBagConstraints.fill = 2;
      gridBagConstraints.insets = new Insets(0, 5, 0, 5);
      this.getContentPane().add(this.warrantyPanel, gridBagConstraints);
      this.pack();
   }

   private void bSelectPartClicked(MouseEvent evt) {
      try {
         Product ex = ProductPickerDialogue.getUserSelectedProduct();
         if(ex != null) {
            this.selectedPart = ex;
            this.tPartName.setText(ex.getName());
         }
      } catch (Exception var3) {
         DialogueUtil.handleException(var3, "Error:", "Error", true, super.getParent());
      }

   }

   private void bOkayClicked(MouseEvent evt) {
      try {
         this.setInputToObject();
         this.productMakeup.save((TransactionContainer)null);
         if(this.isNew) {
            this.parent.newPartAdded(this.productMakeup);
         }

         this.closeDialog((WindowEvent)null);
      } catch (UserInputException var3) {
         DialogueUtil.handleUserInputException(var3, "Bad User Input:", "Error", super.getParent());
      } catch (BusinessException var4) {
         DialogueUtil.handleException(var4, "Product Part not allowed:", "Business Violation", false, super.getParent());
      } catch (Exception var5) {
         DialogueUtil.handleException(var5, "Error:", "Error", true, super.getParent());
      }

   }

   private void closeDialog(WindowEvent evt) {
      this.setVisible(false);
      this.dispose();
   }
}
