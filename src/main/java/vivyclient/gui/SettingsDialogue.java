package vivyclient.gui;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import vivyclient.Client;
import vivyclient.data.ConnectionBroker;
import vivyclient.exception.UserInputException;
import vivyclient.util.DialogueUtil;
import vivyclient.util.Settings;

public class SettingsDialogue extends JDialog {
   private static final String NAME = "SettingsDialogue";
   private JLabel lDBHost;
   private JLabel lDBPort;
   private JLabel lDBName;
   private JLabel lDBUserName;
   private JLabel lDBUserPassword;
   private JTextField tDBHost;
   private JTextField tDBPort;
   private JTextField tDBName;
   private JTextField tDBUserName;
   private JPasswordField tDBUserPassword;
   private JLabel label;
   private JTextField tErrorLogPath;
   private JTextField tSQLLogPath;
   private JComboBox cLogSQLReads;
   private JComboBox cLogSQLWrites;
   private JComboBox cLookAndFeel;
   private JComboBox cLookAndFeelTheme;
   private JTextArea tSlogans;
   Boolean[] booleanOption;
   private JLabel jLabel1;
   private JButton bOkay;
   private JPanel settingsPanel;
   private JSeparator jSeparator1;
   private JButton bCancel;

   public SettingsDialogue(Frame parent, boolean modal) {
      super(parent, modal);
      this.booleanOption = new Boolean[]{Boolean.TRUE, Boolean.FALSE};
      this.initComponents();
      this.initialise();
      this.pack();
      this.setTitle("Settings");
      this.setSize(Settings.getWidth("SettingsDialogue"), Settings.getHeight("SettingsDialogue"));
      this.setLocation(Settings.getXPos("SettingsDialogue"), Settings.getYPos("SettingsDialogue"));
   }

   private void initialise() {
      this.lDBHost = new JLabel();
      this.lDBHost.setText("DB Host:");
      this.settingsPanel.add(this.lDBHost, this.getGBConstraints(0, 0, false));
      this.tDBHost = new JTextField();
      this.tDBHost.setText(Settings.getDBHostName());
      this.settingsPanel.add(this.tDBHost, this.getGBConstraints(1, 0, true));
      this.lDBPort = new JLabel();
      this.lDBPort.setText("DB Port:");
      this.settingsPanel.add(this.lDBPort, this.getGBConstraints(0, 1, false));
      this.tDBPort = new JTextField();
      this.tDBPort.setText(Settings.getDBServerPort());
      this.settingsPanel.add(this.tDBPort, this.getGBConstraints(1, 1, true));
      this.lDBName = new JLabel();
      this.lDBName.setText("DB Name:");
      this.settingsPanel.add(this.lDBName, this.getGBConstraints(0, 2, false));
      this.tDBName = new JTextField();
      this.tDBName.setText(Settings.getDBName());
      this.settingsPanel.add(this.tDBName, this.getGBConstraints(1, 2, true));
      this.lDBUserName = new JLabel();
      this.lDBUserName.setText("DB UserName:");
      this.settingsPanel.add(this.lDBUserName, this.getGBConstraints(0, 3, false));
      this.tDBUserName = new JTextField();
      this.tDBUserName.setText(Settings.getDBUserName());
      this.settingsPanel.add(this.tDBUserName, this.getGBConstraints(1, 3, true));
      this.lDBUserPassword = new JLabel();
      this.lDBUserPassword.setText("DB Password:");
      this.settingsPanel.add(this.lDBUserPassword, this.getGBConstraints(0, 4, false));
      this.tDBUserPassword = new JPasswordField();
      this.tDBUserPassword.setText(Settings.getDBUserPassword());
      this.settingsPanel.add(this.tDBUserPassword, this.getGBConstraints(1, 4, true));
      this.label = new JLabel();
      this.label.setText("Error Log:");
      this.settingsPanel.add(this.label, this.getGBConstraints(0, 5, false));
      this.tErrorLogPath = new JTextField();
      this.tErrorLogPath.setText(Settings.getErrorLogFilePath());
      this.settingsPanel.add(this.tErrorLogPath, this.getGBConstraints(1, 5, true));
      this.label = new JLabel();
      this.label.setText("SQL Log:");
      this.settingsPanel.add(this.label, this.getGBConstraints(0, 6, false));
      this.tSQLLogPath = new JTextField();
      this.tSQLLogPath.setText(Settings.getSqlLogFilePath());
      this.settingsPanel.add(this.tSQLLogPath, this.getGBConstraints(1, 6, true));
      this.label = new JLabel();
      this.label.setText("Log Reads:");
      this.settingsPanel.add(this.label, this.getGBConstraints(0, 7, false));
      this.cLogSQLReads = new JComboBox();
      this.cLogSQLReads.setModel(new DefaultComboBoxModel(this.booleanOption));
      this.cLogSQLReads.setSelectedItem(new Boolean(Settings.getLogSQLReads()));
      this.settingsPanel.add(this.cLogSQLReads, this.getGBConstraints(1, 7, true));
      this.label = new JLabel();
      this.label.setText("Log Writes:");
      this.settingsPanel.add(this.label, this.getGBConstraints(0, 8, false));
      this.cLogSQLWrites = new JComboBox();
      this.cLogSQLWrites.setModel(new DefaultComboBoxModel(this.booleanOption));
      this.cLogSQLWrites.setSelectedItem(new Boolean(Settings.getLogSQLWrites()));
      this.settingsPanel.add(this.cLogSQLWrites, this.getGBConstraints(1, 8, true));
      this.label = new JLabel();
      this.label.setText("Print Footers:");
      this.settingsPanel.add(this.label, this.getGBConstraints(0, 9, false));
      this.tSlogans = new JTextArea();
      Dimension textAreaSize = new Dimension(10, 40);
      this.tSlogans.setText(Settings.getPrintFooterTags());
      JScrollPane slogansScroll = new JScrollPane(this.tSlogans);
      slogansScroll.setMinimumSize(textAreaSize);
      slogansScroll.setPreferredSize(textAreaSize);
      this.settingsPanel.add(slogansScroll, this.getGBConstraints(1, 9, true));
   }

   private void setValues() throws UserInputException {
      Settings.setDBHostName(this.tDBHost.getText().trim());
      Settings.setDBServerPort(this.tDBPort.getText().trim());
      Settings.setDBName(this.tDBName.getText().trim());
      Settings.setDBUserName(this.tDBUserName.getText().trim());
      StringBuffer password = new StringBuffer();
      password.append(this.tDBUserPassword.getPassword());
      Settings.setDBUserPassword(password.toString());
      Settings.setErrorLogFilePath(this.tErrorLogPath.getText());
      Settings.setSqlLogFilePath(this.tSQLLogPath.getText());
      Settings.setLogSQLReads(((Boolean)this.cLogSQLReads.getSelectedItem()).booleanValue());
      Settings.setLogSQLWrites(((Boolean)this.cLogSQLWrites.getSelectedItem()).booleanValue());
      if(this.tSlogans.getText().trim().length() == 0) {
         throw new UserInputException("Enter Print Slogan", this.tSlogans);
      } else {
         Settings.setPrintFooterTags(this.tSlogans.getText());
         Settings.setUserHasSetup(true);
      }
   }

   private GridBagConstraints getGBConstraints(int gridx, int gridy, boolean wide) {
      GridBagConstraints gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.fill = 2;
      gridBagConstraints.insets = new Insets(0, 2, 4, 2);
      gridBagConstraints.anchor = 11;
      if(wide) {
         gridBagConstraints.weightx = 1.0D;
         gridBagConstraints.fill = 1;
      }

      gridBagConstraints.gridx = gridx;
      gridBagConstraints.gridy = gridy;
      return gridBagConstraints;
   }

   private void initComponents() {
      this.jLabel1 = new JLabel();
      this.jSeparator1 = new JSeparator();
      this.bOkay = new JButton();
      this.bCancel = new JButton();
      this.settingsPanel = new JPanel();
      this.jLabel1.setText("jLabel1");
      this.getContentPane().setLayout(new GridBagLayout());
      this.addWindowListener(new WindowAdapter() {
         public void windowClosing(WindowEvent evt) {
            SettingsDialogue.this.closeDialog(evt);
         }
      });
      GridBagConstraints gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 1;
      gridBagConstraints.gridwidth = 0;
      gridBagConstraints.fill = 2;
      gridBagConstraints.ipady = 2;
      gridBagConstraints.anchor = 15;
      gridBagConstraints.weightx = 1.0D;
      gridBagConstraints.weighty = 1.0D;
      gridBagConstraints.insets = new Insets(5, 0, 0, 0);
      this.getContentPane().add(this.jSeparator1, gridBagConstraints);
      this.bOkay.setText("Okay");
      this.bOkay.addMouseListener(new MouseAdapter() {
         public void mouseClicked(MouseEvent evt) {
            SettingsDialogue.this.okClicked(evt);
         }
      });
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 1;
      gridBagConstraints.gridy = 2;
      gridBagConstraints.anchor = 14;
      gridBagConstraints.insets = new Insets(5, 0, 5, 5);
      this.getContentPane().add(this.bOkay, gridBagConstraints);
      this.bCancel.setText("Cancel");
      this.bCancel.addMouseListener(new MouseAdapter() {
         public void mouseClicked(MouseEvent evt) {
            SettingsDialogue.this.cancelClicked(evt);
         }
      });
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 2;
      gridBagConstraints.anchor = 14;
      gridBagConstraints.weightx = 1.0D;
      gridBagConstraints.insets = new Insets(5, 5, 5, 5);
      this.getContentPane().add(this.bCancel, gridBagConstraints);
      this.settingsPanel.setLayout(new GridBagLayout());
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 0;
      gridBagConstraints.gridwidth = 0;
      gridBagConstraints.fill = 1;
      gridBagConstraints.weightx = 1.0D;
      gridBagConstraints.insets = new Insets(4, 4, 4, 4);
      this.getContentPane().add(this.settingsPanel, gridBagConstraints);
      this.pack();
   }

   private void cancelClicked(MouseEvent evt) {
      this.closeMe();
   }

   private void okClicked(MouseEvent evt) {
      try {
         this.setValues();
         ConnectionBroker.resetConnection();
         this.closeMe();
      } catch (UserInputException var3) {
         DialogueUtil.handleUserInputException(var3, "Settings cannot be saved", "Invalid Settings", Client.getMainFrame());
      }

   }

   private void closeDialog(WindowEvent evt) {
      this.closeMe();
   }

   private void closeMe() {
      Settings.setWidth("SettingsDialogue", (int)this.getSize().getWidth());
      Settings.setHeight("SettingsDialogue", (int)this.getSize().getHeight());
      Settings.setXPos("SettingsDialogue", this.getLocation().x);
      Settings.setYPos("SettingsDialogue", this.getLocation().y);
      this.setVisible(false);
      this.dispose();
   }
}
