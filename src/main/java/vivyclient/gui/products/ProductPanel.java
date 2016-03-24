package vivyclient.gui.products;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Calendar;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import vivyclient.Client;
import vivyclient.data.TransactionContainer;
import vivyclient.exception.UserInputException;
import vivyclient.gui.ModifyPanel;
import vivyclient.gui.ModifyPanelListener;
import vivyclient.gui.common.PanelUtil;
import vivyclient.gui.products.ProductMakeupPricePanel;
import vivyclient.model.DurationType;
import vivyclient.model.FeaturedLevel;
import vivyclient.model.LinkType;
import vivyclient.model.Product;
import vivyclient.util.Settings;
import vivyclient.util.ViewUtil;

public class ProductPanel extends JPanel implements ModifyPanel {
   private ModifyPanelListener listener;
   private Product product;
   private boolean isNew;
   private boolean changed;
   private static final int MAX_NAME_LENGTH = 255;
   private static final int MAX_MANUFACTURER_LENGTH = 100;
   private static final int MAX_WARRANTY_LENGTH = 100;
   private static final int MAX_IMAGE_NAME_LENGTH = 50;
   private static final int MAX_LINK_URL_LENGTH = 200;
   private static final int MAX_PLATFORM_LENGTH = 255;
   private static final int MAX_MEDIA_LENGTH = 255;
   private JPanel checkboxPanel;
   private JPanel productAttributesPanel;
   private JComboBox cWarrantyDurationType;
   private JTextField tWarrantyDurationMultiplier;
   private JLabel lLinkUrl;
   private JComboBox cManufacturer;
   private JTextField tProductName;
   private JTextField tImageName;
   private JComboBox cMedia;
   private JLabel lManufacturer;
   private JPanel warrantyPanel;
   private JLabel lAcademic;
   private JComboBox cFeatured;
   private JCheckBox cAcademic;
   private JLabel lLastUpdate;
   private JTextField tProductId;
   private JScrollPane pDescription;
   private JLabel lProductName;
   private JLabel lWarranty;
   private JTextField tWarrantyComments;
   private JComboBox cFreightUnits;
   private JLabel lProductId;
   private JLabel lFreightUnits;
   private JLabel lImageName;
   private JComboBox cPlatform;
   private JPanel webPanel;
   private JEditorPane textDescription;
   private JComboBox cLinkType;
   private JLabel lPlatform;
   private JLabel lFeatured;
   private JSeparator jSeparator1;
   private JLabel lWarrantyComments;
   private JLabel lMedia;
   private JLabel lTitle;
   private JTextField tLinkUrl;
   private JPanel pSoftware;
   private JTextField tLastUpdate;

   public ProductPanel(Product product, ModifyPanelListener listener) throws Exception {
      this.product = product;
      this.listener = listener;
      this.initComponents();
      this.setValues();
      GridBagConstraints gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.fill = 1;
      gridBagConstraints.weightx = 1.0D;
      gridBagConstraints.weighty = 0.5D;
      gridBagConstraints.gridy = 1;
      this.add(new ProductMakeupPricePanel(product, this), gridBagConstraints);
   }

   private void setValues() throws Exception {
      this.isNew = !this.product.exists((TransactionContainer)null);
      if(this.isNew) {
         this.tProductId.setText("");
         this.lTitle.setText("New Product");
         this.cFreightUnits.setSelectedItem("");
      } else {
         this.tProductId.setText(this.product.getObjectId() + "");
         this.lTitle.setText("Editing " + this.product.toString());
         this.cFreightUnits.setSelectedItem(this.product.getFreightUnits() + "");
      }

      this.tProductId.setEnabled(this.isNew);
      this.tProductName.setText(this.product.getName());
      this.cAcademic.setSelected(this.product.getAcademic());
      this.tLastUpdate.setText(ViewUtil.calendarDisplay(this.product.getLastUpdate()));
      if(this.product.getDescription() != null) {
         this.textDescription.setText(this.product.getDescription());
      } else {
         this.textDescription.setText("");
      }

      ArrayList paymentDurationTypes = new ArrayList();
      paymentDurationTypes.add((Object)null);
      paymentDurationTypes.addAll(DurationType.cachedFindAll(new DurationType()));
      paymentDurationTypes.remove(DurationType.BILLING_MONTH_DURATION_TYPE);
      this.cWarrantyDurationType.setModel(new DefaultComboBoxModel(paymentDurationTypes.toArray()));
      this.cManufacturer.setModel(new DefaultComboBoxModel(Product.getDistinctList(this.product.getSubCategory().getCategory().getProductType(), "manufacturer")));
      this.cFreightUnits.setModel(new DefaultComboBoxModel(Product.getDistinctList(this.product.getSubCategory().getCategory().getProductType(), "freightUnits")));
      this.cPlatform.setModel(new DefaultComboBoxModel(Product.getDistinctList(this.product.getSubCategory().getCategory().getProductType(), "platform")));
      this.cMedia.setModel(new DefaultComboBoxModel(Product.getDistinctList(this.product.getSubCategory().getCategory().getProductType(), "media")));
      PanelUtil.populateComboList(this.cLinkType, new LinkType(), false);
      this.cManufacturer.setSelectedItem(this.product.getManufacturer());
      this.cFreightUnits.setSelectedItem(this.product.getFreightUnits() + "");
      this.cPlatform.setSelectedItem(this.product.getPlatform());
      this.cMedia.setSelectedItem(this.product.getMedia());
      PanelUtil.setComboSelection(this.cWarrantyDurationType, this.product.getWarrantyDurationType());
      this.tWarrantyDurationMultiplier.setText(ViewUtil.intDisplay(this.product.getWarrantyDurationMultiplier(), false));
      this.tWarrantyComments.setText(this.product.getWarrantyComments());
      PanelUtil.setComboSelection(this.cLinkType, this.product.getLinkType());
      this.tLinkUrl.setText(this.product.getLinkUrl());
      PanelUtil.populateComboList(this.cFeatured, new FeaturedLevel(), false);
      PanelUtil.setComboSelection(this.cFeatured, this.product.getFeaturedLevel());
      this.tImageName.setText(this.product.getImageName());
      this.changed = false;
   }

   public boolean exit() throws Exception {
      if(this.changed) {
         int i = JOptionPane.showConfirmDialog(Client.getMainFrame(), "Save Product before continuing?", "Save Changes", 1);
         if(i == 2) {
            return false;
         } else {
            if(i == 0) {
               this.save();
            }

            return true;
         }
      } else {
         return true;
      }
   }

   public void save() throws Exception {
      ActionEvent bug = new ActionEvent(this, 1001, (String)null);
      this.cManufacturer.actionPerformed(bug);
      this.cFreightUnits.actionPerformed(bug);
      this.cPlatform.actionPerformed(bug);
      this.cMedia.actionPerformed(bug);
      String id = this.tProductId.getText().trim();
      if(this.isNew) {
         if(id.length() == 0) {
            this.product.setObjectId(Settings.getNullInt());
         } else {
            try {
               this.product.setObjectId(Integer.parseInt(id));
            } catch (Exception var5) {
               throw new UserInputException("Invalid Product Id format: integer required", this.tProductId);
            }

            if(this.product.exists((TransactionContainer)null)) {
               throw new UserInputException("Invalid Product Id: this Id is already in use", this.tProductId);
            }
         }
      }

      this.product.setName(this.tProductName.getText());
      if(this.cManufacturer.getSelectedItem() != null) {
         this.product.setManufacturer(this.cManufacturer.getSelectedItem().toString());
      } else {
         this.product.setManufacturer((String)null);
      }

      this.product.setWarrantyDurationType((DurationType)PanelUtil.getValidatedComboItem(this.cWarrantyDurationType, "Warranty Duration Type", false));
      if(this.product.getWarrantyDurationType() != null) {
         this.product.setWarrantyDurationMultiplier(PanelUtil.getValidatedIntValue(this.tWarrantyDurationMultiplier, "Warranty Duration Multiplier", true, 1));
      } else {
         this.product.setWarrantyDurationMultiplier(-1);
      }

      this.product.setWarrantyComments(PanelUtil.getValidatedStringValue(this.tWarrantyComments, "Warranty Comments", false));
      if(this.cFreightUnits.getSelectedItem() != null && this.cFreightUnits.getSelectedItem().toString().trim().length() != 0) {
         try {
            this.product.setFreightUnits(Double.parseDouble(this.cFreightUnits.getSelectedItem().toString().trim()));
         } catch (Exception var4) {
            throw new UserInputException("Invalid Freight Units format: number required", this.cFreightUnits);
         }

         this.product.setAcademic(this.cAcademic.isSelected());
         if(this.cPlatform.getSelectedItem() != null) {
            if(this.cPlatform.getSelectedItem().toString().trim().length() > 255) {
               throw new UserInputException("Platform Name too long: expected length is 255", this.cPlatform);
            }

            this.product.setPlatform(this.cPlatform.getSelectedItem().toString().trim());
         } else {
            this.product.setPlatform((String)null);
         }

         if(this.cMedia.getSelectedItem() != null) {
            if(this.cMedia.getSelectedItem().toString().trim().length() > 255) {
               throw new UserInputException("Media Name too long: expected length is 255", this.cMedia);
            }

            this.product.setMedia(this.cMedia.getSelectedItem().toString().trim());
         } else {
            this.product.setMedia((String)null);
         }

         this.product.setLinkType((LinkType)PanelUtil.getValidatedComboItem(this.cLinkType, "Link Type", true));
         if(this.tLinkUrl.getText().trim().length() > 200) {
            throw new UserInputException("Link URL too long: expected length is 200", this.tLinkUrl);
         } else {
            this.product.setLinkUrl(this.tLinkUrl.getText().trim());
            this.product.setFeaturedLevel((FeaturedLevel)PanelUtil.getValidatedComboItem(this.cFeatured, "Featured Level", true));
            if(this.tImageName.getText().trim().length() > 50) {
               throw new UserInputException("Image Name too long: expected length is 50", this.tImageName);
            } else {
               this.product.setImageName(this.tImageName.getText().trim());
               this.product.setDescription(this.textDescription.getText());
               this.product.save((TransactionContainer)null);
               this.listener.saveCompleted(this.product, this.isNew);
               this.product.setLastUpdate(Calendar.getInstance());
               if(this.isNew) {
                  this.setValues();
               }

            }
         }
      } else {
         throw new UserInputException("Enter value for Freight Units", this.cFreightUnits);
      }
   }

   public Component getOuterFrame() {
      return Client.getMainFrame();
   }

   private void initComponents() {
      this.productAttributesPanel = new JPanel();
      this.lProductName = new JLabel();
      this.tProductName = new JTextField();
      this.lProductId = new JLabel();
      this.lManufacturer = new JLabel();
      this.cManufacturer = new JComboBox();
      this.lFreightUnits = new JLabel();
      this.cFreightUnits = new JComboBox();
      this.pSoftware = new JPanel();
      this.cPlatform = new JComboBox();
      this.lMedia = new JLabel();
      this.cMedia = new JComboBox();
      this.lPlatform = new JLabel();
      this.pDescription = new JScrollPane();
      this.textDescription = new JEditorPane();
      this.lTitle = new JLabel();
      this.checkboxPanel = new JPanel();
      this.cAcademic = new JCheckBox();
      this.lLastUpdate = new JLabel();
      this.tLastUpdate = new JTextField();
      this.lAcademic = new JLabel();
      this.tProductId = new JTextField();
      this.jSeparator1 = new JSeparator();
      this.warrantyPanel = new JPanel();
      this.lWarranty = new JLabel();
      this.cWarrantyDurationType = new JComboBox();
      this.tWarrantyDurationMultiplier = new JTextField();
      this.lWarrantyComments = new JLabel();
      this.tWarrantyComments = new JTextField();
      this.webPanel = new JPanel();
      this.lImageName = new JLabel();
      this.tImageName = new JTextField();
      this.lLinkUrl = new JLabel();
      this.tLinkUrl = new JTextField();
      this.cLinkType = new JComboBox();
      this.lFeatured = new JLabel();
      this.cFeatured = new JComboBox();
      this.setLayout(new GridBagLayout());
      this.setBorder(new EmptyBorder(new Insets(10, 10, 10, 10)));
      this.productAttributesPanel.setLayout(new GridBagLayout());
      this.lProductName.setText("Product Name:");
      GridBagConstraints gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 2;
      gridBagConstraints.insets = new Insets(0, 0, 2, 5);
      this.productAttributesPanel.add(this.lProductName, gridBagConstraints);
      this.tProductName.setText("jTextField1");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 1;
      gridBagConstraints.gridy = 2;
      gridBagConstraints.fill = 2;
      gridBagConstraints.weightx = 1.0D;
      gridBagConstraints.insets = new Insets(0, 0, 2, 0);
      this.productAttributesPanel.add(this.tProductName, gridBagConstraints);
      this.lProductId.setText("Product Id:");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 2;
      gridBagConstraints.gridy = 2;
      gridBagConstraints.insets = new Insets(0, 20, 2, 5);
      this.productAttributesPanel.add(this.lProductId, gridBagConstraints);
      this.lManufacturer.setText("Manufacturer:");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 3;
      gridBagConstraints.insets = new Insets(0, 0, 2, 5);
      this.productAttributesPanel.add(this.lManufacturer, gridBagConstraints);
      this.cManufacturer.setEditable(true);
      this.cManufacturer.setMinimumSize(new Dimension(120, 20));
      this.cManufacturer.setPreferredSize(new Dimension(120, 20));
      this.cManufacturer.setLightWeightPopupEnabled(false);
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 1;
      gridBagConstraints.gridy = 3;
      gridBagConstraints.fill = 2;
      gridBagConstraints.weightx = 1.0D;
      gridBagConstraints.insets = new Insets(0, 0, 2, 0);
      this.productAttributesPanel.add(this.cManufacturer, gridBagConstraints);
      this.lFreightUnits.setText("Freight Units:");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 4;
      gridBagConstraints.insets = new Insets(0, 0, 0, 6);
      this.productAttributesPanel.add(this.lFreightUnits, gridBagConstraints);
      this.cFreightUnits.setEditable(true);
      this.cFreightUnits.setMinimumSize(new Dimension(120, 20));
      this.cFreightUnits.setPreferredSize(new Dimension(120, 20));
      this.cFreightUnits.setLightWeightPopupEnabled(false);
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 1;
      gridBagConstraints.gridy = 4;
      gridBagConstraints.fill = 2;
      this.productAttributesPanel.add(this.cFreightUnits, gridBagConstraints);
      this.pSoftware.setLayout(new GridBagLayout());
      this.pSoftware.setBorder(new TitledBorder("Software"));
      this.cPlatform.setEditable(true);
      this.cPlatform.setMinimumSize(new Dimension(120, 20));
      this.cPlatform.setPreferredSize(new Dimension(120, 20));
      this.cPlatform.setLightWeightPopupEnabled(false);
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 1;
      gridBagConstraints.gridy = 0;
      gridBagConstraints.fill = 2;
      gridBagConstraints.weightx = 1.0D;
      this.pSoftware.add(this.cPlatform, gridBagConstraints);
      this.lMedia.setText("Media:");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 2;
      gridBagConstraints.gridy = 0;
      gridBagConstraints.insets = new Insets(0, 20, 0, 5);
      this.pSoftware.add(this.lMedia, gridBagConstraints);
      this.cMedia.setEditable(true);
      this.cMedia.setMinimumSize(new Dimension(120, 20));
      this.cMedia.setPreferredSize(new Dimension(120, 20));
      this.cMedia.setLightWeightPopupEnabled(false);
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 3;
      gridBagConstraints.gridy = 0;
      gridBagConstraints.fill = 2;
      gridBagConstraints.weightx = 1.0D;
      gridBagConstraints.insets = new Insets(0, 0, 0, 5);
      this.pSoftware.add(this.cMedia, gridBagConstraints);
      this.lPlatform.setText("Platform:");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 0;
      gridBagConstraints.insets = new Insets(0, 5, 0, 5);
      this.pSoftware.add(this.lPlatform, gridBagConstraints);
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 6;
      gridBagConstraints.gridwidth = 0;
      gridBagConstraints.fill = 2;
      gridBagConstraints.ipady = 9;
      this.productAttributesPanel.add(this.pSoftware, gridBagConstraints);
      this.pDescription.setBorder(new TitledBorder("Description"));
      this.pDescription.setHorizontalScrollBarPolicy(31);
      this.pDescription.setVerticalScrollBarPolicy(22);
      this.pDescription.setViewportView(this.textDescription);
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 8;
      gridBagConstraints.gridwidth = 0;
      gridBagConstraints.fill = 1;
      gridBagConstraints.ipady = 14;
      gridBagConstraints.weightx = 1.0D;
      gridBagConstraints.weighty = 1.0D;
      gridBagConstraints.insets = new Insets(0, 0, 5, 5);
      this.productAttributesPanel.add(this.pDescription, gridBagConstraints);
      this.lTitle.setFont(new Font("Arial", 1, 14));
      this.lTitle.setForeground(SystemColor.activeCaption);
      this.lTitle.setText("Product Name");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 0;
      gridBagConstraints.gridwidth = 0;
      gridBagConstraints.anchor = 17;
      gridBagConstraints.insets = new Insets(5, 5, 3, 5);
      this.productAttributesPanel.add(this.lTitle, gridBagConstraints);
      this.checkboxPanel.setLayout(new GridBagLayout());
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.fill = 2;
      gridBagConstraints.weightx = 1.0D;
      this.checkboxPanel.add(this.cAcademic, gridBagConstraints);
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 3;
      gridBagConstraints.gridy = 4;
      gridBagConstraints.fill = 1;
      gridBagConstraints.weightx = 1.0D;
      this.productAttributesPanel.add(this.checkboxPanel, gridBagConstraints);
      this.lLastUpdate.setText("Last Update:");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 2;
      gridBagConstraints.gridy = 3;
      gridBagConstraints.insets = new Insets(2, 20, 0, 5);
      this.productAttributesPanel.add(this.lLastUpdate, gridBagConstraints);
      this.tLastUpdate.setEditable(false);
      this.tLastUpdate.setEnabled(false);
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 3;
      gridBagConstraints.gridy = 3;
      gridBagConstraints.fill = 2;
      gridBagConstraints.weightx = 1.0D;
      gridBagConstraints.insets = new Insets(2, 0, 0, 0);
      this.productAttributesPanel.add(this.tLastUpdate, gridBagConstraints);
      this.lAcademic.setText("Academic:");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 2;
      gridBagConstraints.gridy = 4;
      gridBagConstraints.insets = new Insets(0, 20, 0, 5);
      this.productAttributesPanel.add(this.lAcademic, gridBagConstraints);
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 3;
      gridBagConstraints.gridy = 2;
      gridBagConstraints.fill = 2;
      gridBagConstraints.weightx = 1.0D;
      gridBagConstraints.insets = new Insets(0, 0, 2, 0);
      this.productAttributesPanel.add(this.tProductId, gridBagConstraints);
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 1;
      gridBagConstraints.gridwidth = 0;
      gridBagConstraints.fill = 2;
      gridBagConstraints.ipadx = 3;
      gridBagConstraints.insets = new Insets(0, 0, 10, 0);
      this.productAttributesPanel.add(this.jSeparator1, gridBagConstraints);
      this.warrantyPanel.setLayout(new GridBagLayout());
      this.warrantyPanel.setBorder(new TitledBorder("Warranty"));
      this.lWarranty.setText("Duration:");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 0;
      gridBagConstraints.insets = new Insets(0, 0, 0, 5);
      this.warrantyPanel.add(this.lWarranty, gridBagConstraints);
      this.cWarrantyDurationType.setMinimumSize(new Dimension(120, 20));
      this.cWarrantyDurationType.setPreferredSize(new Dimension(120, 20));
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 2;
      gridBagConstraints.gridy = 0;
      gridBagConstraints.fill = 2;
      gridBagConstraints.weightx = 0.5D;
      this.warrantyPanel.add(this.cWarrantyDurationType, gridBagConstraints);
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 1;
      gridBagConstraints.gridy = 0;
      gridBagConstraints.fill = 2;
      gridBagConstraints.weightx = 0.1D;
      gridBagConstraints.insets = new Insets(0, 0, 0, 2);
      this.warrantyPanel.add(this.tWarrantyDurationMultiplier, gridBagConstraints);
      this.lWarrantyComments.setText("Comments:");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 3;
      gridBagConstraints.gridy = 0;
      gridBagConstraints.insets = new Insets(0, 15, 0, 5);
      this.warrantyPanel.add(this.lWarrantyComments, gridBagConstraints);
      this.tWarrantyComments.setText("jTextField1");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 4;
      gridBagConstraints.gridy = 0;
      gridBagConstraints.fill = 2;
      gridBagConstraints.weightx = 1.0D;
      this.warrantyPanel.add(this.tWarrantyComments, gridBagConstraints);
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 5;
      gridBagConstraints.gridwidth = 0;
      gridBagConstraints.fill = 2;
      gridBagConstraints.weightx = 1.0D;
      gridBagConstraints.insets = new Insets(1, 0, 0, 0);
      this.productAttributesPanel.add(this.warrantyPanel, gridBagConstraints);
      this.webPanel.setLayout(new GridBagLayout());
      this.webPanel.setBorder(new TitledBorder("Web"));
      this.lImageName.setText("Image Name:");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 2;
      gridBagConstraints.gridy = 1;
      gridBagConstraints.insets = new Insets(2, 2, 0, 5);
      this.webPanel.add(this.lImageName, gridBagConstraints);
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 3;
      gridBagConstraints.gridy = 1;
      gridBagConstraints.fill = 2;
      gridBagConstraints.weightx = 1.0D;
      gridBagConstraints.insets = new Insets(2, 0, 0, 0);
      this.webPanel.add(this.tImageName, gridBagConstraints);
      this.lLinkUrl.setText("Link URL:");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 0;
      gridBagConstraints.insets = new Insets(2, 0, 0, 5);
      this.webPanel.add(this.lLinkUrl, gridBagConstraints);
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 2;
      gridBagConstraints.gridy = 0;
      gridBagConstraints.gridwidth = 0;
      gridBagConstraints.fill = 2;
      gridBagConstraints.weightx = 1.0D;
      gridBagConstraints.insets = new Insets(2, 3, 0, 0);
      this.webPanel.add(this.tLinkUrl, gridBagConstraints);
      this.cLinkType.setMinimumSize(new Dimension(31, 20));
      this.cLinkType.setPreferredSize(new Dimension(31, 20));
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 1;
      gridBagConstraints.gridy = 0;
      gridBagConstraints.fill = 2;
      gridBagConstraints.weightx = 0.4D;
      gridBagConstraints.insets = new Insets(2, 0, 0, 0);
      this.webPanel.add(this.cLinkType, gridBagConstraints);
      this.lFeatured.setText("Featured:");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 1;
      gridBagConstraints.insets = new Insets(2, 0, 0, 5);
      this.webPanel.add(this.lFeatured, gridBagConstraints);
      this.cFeatured.setMinimumSize(new Dimension(31, 20));
      this.cFeatured.setPreferredSize(new Dimension(31, 20));
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.fill = 2;
      this.webPanel.add(this.cFeatured, gridBagConstraints);
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 7;
      gridBagConstraints.gridwidth = 0;
      gridBagConstraints.fill = 2;
      gridBagConstraints.weightx = 1.0D;
      this.productAttributesPanel.add(this.webPanel, gridBagConstraints);
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.fill = 1;
      gridBagConstraints.weightx = 1.0D;
      gridBagConstraints.weighty = 0.5D;
      this.add(this.productAttributesPanel, gridBagConstraints);
   }
}
