package vivyclient;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Properties;
import java.util.jar.Manifest;
import javax.swing.Box;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import vivyclient.error.ErrorWriter;
import vivyclient.error.SQLLog;
import vivyclient.gui.SettingsDialogue;
import vivyclient.gui.customers.CustomerFrame;
import vivyclient.gui.products.ProductFrame;
import vivyclient.gui.sales.DispatchFrame;
import vivyclient.gui.sales.SaleFrame;
import vivyclient.gui.suppliers.SuppliersFrame;
import vivyclient.menu.ReportsMenu;
import vivyclient.util.DialogueUtil;
import vivyclient.util.Settings;

public class Client extends JFrame {
    private JDesktopPane desktopPane;
    private JToolBar toolBar;
    private JLabel infoLabel;
    private Timer currentTimer;
    private static final String NAME = "ClientFrame";
    public static final String VERSION_NUMBER = "3.0.3b";
    private static Client mainFrame;

    public static void main(String[] args) {
        mainFrame = new Client();
    }

    public Client() {
        this.changeLookAndFeel();
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {
                Client.this.exitForm(evt);
            }
        });
        this.setTitle("VividCLIENT Version " + getVersion());
        this.desktopPane = new JDesktopPane();
        this.getContentPane().add(this.desktopPane, "Center");
        this.toolBar = new JToolBar();
        this.getContentPane().add(this.toolBar, "North");
        this.createMenu();
        this.setExtendedState(Settings.getState("ClientFrame"));
        this.setSize(new Dimension(Settings.getWidth("ClientFrame"), Settings.getHeight("ClientFrame")));
        this.setLocation(Settings.getXPos("ClientFrame"), Settings.getYPos("ClientFrame"));
        this.setVisible(true);
        if (!Settings.getUserHasSetup()) {
            this.showSettings();
        }

        SQLLog.initialise();
        ErrorWriter.initialise();
        this.showMessage("Welcome to Vivysys....");
    }

    private static String getVersion() {
        Properties properties = new Properties();
        InputStream is = Client.class.getResourceAsStream("/META-INF/maven/nz.net.vivid/vivysys/pom.properties");
        try {
            properties.load(is);
            return properties.getProperty("version", "local");
        } catch (Exception e) {
            return "local";
        }
    }

    private void createMenu() {
        this.setJMenuBar(new JMenuBar());
        JMenu viewMenu = new JMenu("View");
        JMenuItem viewProducts = new JMenuItem("Products");
        viewProducts.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                Client.this.showProducts();
            }
        });
        viewMenu.add(viewProducts);
        JMenu salesSubMenu = new JMenu("Sales");
        JMenuItem viewSalesByCustomer = new JMenuItem("By Customer");
        viewSalesByCustomer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                Client.this.showSales("displayByCustomer");
            }
        });
        salesSubMenu.add(viewSalesByCustomer);
        JMenuItem viewSalesByStatus = new JMenuItem("By Status");
        viewSalesByStatus.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                Client.this.showSales("displayByStatus");
            }
        });
        salesSubMenu.add(viewSalesByStatus);
        JMenuItem viewSalesUnsorted = new JMenuItem("Unsorted");
        viewSalesUnsorted.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                Client.this.showSales("displayUnsorted");
            }
        });
        salesSubMenu.add(viewSalesUnsorted);
        viewMenu.add(salesSubMenu);
        JMenuItem viewCustomers = new JMenuItem("Customers");
        viewCustomers.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                Client.this.showCustomers();
            }
        });
        viewMenu.add(viewCustomers);
        viewMenu.add(this.getDispatchesMenu());
        viewMenu.add(this.getSuppliersMenu());
        JMenu toolsMenu = new JMenu("Tools");
        JMenuItem viewSettings = new JMenuItem("Settings");
        viewSettings.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                Client.this.showSettings();
            }
        });
        toolsMenu.add(viewSettings);
        this.getJMenuBar().add(viewMenu);
        this.getJMenuBar().add(new ReportsMenu());
        this.getJMenuBar().add(toolsMenu);
        this.getJMenuBar().add(Box.createHorizontalGlue());
        this.infoLabel = new JLabel();
        this.infoLabel.setForeground(Color.DARK_GRAY);
        this.getJMenuBar().add(this.infoLabel);
        this.repaint();
    }

    private void showProducts() {
        this.setCursor(new Cursor(3));
        ProductFrame productFrame = new ProductFrame(this);
        this.desktopPane.add(productFrame);

        try {
            productFrame.setSelected(true);
        } catch (PropertyVetoException var3) {
            productFrame.toFront();
        }

        this.setCursor(new Cursor(0));
    }

    private void showCustomers() {
        try {
            this.setCursor(new Cursor(3));
            CustomerFrame e = new CustomerFrame("displayUnsorted");
            this.desktopPane.add(e);

            try {
                e.setSelected(true);
            } catch (PropertyVetoException var7) {
                e.toFront();
            }
        } catch (Exception var8) {
            DialogueUtil.handleException(var8, "Error Displaying Customers", "ERROR", true, this);
        } finally {
            this.setCursor(new Cursor(0));
        }

    }

    private void showSettings() {
        this.setCursor(new Cursor(3));
        (new SettingsDialogue(this, true)).setVisible(true);
        this.setCursor(new Cursor(0));
    }

    private void showSales(String displayType) {
        this.setCursor(new Cursor(3));
        SaleFrame saleFrame = new SaleFrame(displayType);
        this.desktopPane.add(saleFrame);

        try {
            saleFrame.setSelected(true);
        } catch (PropertyVetoException var4) {
            saleFrame.toFront();
        }

        this.setCursor(new Cursor(0));
    }

    private void showDispatches(String displayType) {
        try {
            this.setCursor(new Cursor(3));
            DispatchFrame e = new DispatchFrame(displayType);
            this.desktopPane.add(e);

            try {
                e.setSelected(true);
            } catch (PropertyVetoException var8) {
                e.toFront();
            }
        } catch (Exception var9) {
            DialogueUtil.handleException(var9, "Error Displaying Dispatches", "ERROR", true, this);
        } finally {
            this.setCursor(new Cursor(0));
        }

    }

    private void showSuppliers(String displayType) {
        try {
            this.setCursor(new Cursor(3));
            SuppliersFrame e = new SuppliersFrame(displayType);
            this.desktopPane.add(e);

            try {
                e.setSelected(true);
            } catch (PropertyVetoException var8) {
                e.toFront();
            }
        } catch (Exception var9) {
            DialogueUtil.handleException(var9, "Error Displaying Suppliers", "ERROR", true, this);
        } finally {
            this.setCursor(new Cursor(0));
        }

    }

    private void exitForm(WindowEvent evt) {
        this.setCursor(new Cursor(3));
        Settings.setWidth("ClientFrame", (int) this.getSize().getWidth());
        Settings.setHeight("ClientFrame", (int) this.getSize().getHeight());
        Settings.setXPos("ClientFrame", this.getLocation().x);
        Settings.setYPos("ClientFrame", this.getLocation().y);
        Settings.setState("ClientFrame", this.getExtendedState());
        System.exit(0);
    }

    public static Client getMainFrame() {
        return mainFrame;
    }

    public static void addInternalFrame(JInternalFrame frame) {
        mainFrame.desktopPane.add(frame);
        frame.setVisible(true);

        try {
            frame.setSelected(true);
        } catch (PropertyVetoException var2) {
            frame.toFront();
        }

    }

    private void showMessage(String message) {
        if (this.currentTimer != null) {
            this.currentTimer.stop();
        }

        this.infoLabel.setText(message + "                ");
        this.getJMenuBar().repaint();
        this.currentTimer = new Timer((int) Settings.getInfoMessageDisplayPeriod(), new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                Client.this.infoLabel.setText("");
                Client.this.getJMenuBar().repaint();
            }
        });
        this.currentTimer.setRepeats(false);
        this.currentTimer.start();
    }

    public static void showInfoMessage(String message) {
        mainFrame.showMessage(message);
    }

    private JMenu getDispatchesMenu() {
        JMenu dispatchesSubMenu = new JMenu("Dispatches");
        JMenuItem viewBySale = new JMenuItem("By Sale");
        viewBySale.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                Client.this.showDispatches("displayBySale");
            }
        });
        dispatchesSubMenu.add(viewBySale);
        JMenuItem viewUnsorted = new JMenuItem("Unsorted");
        viewUnsorted.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                Client.this.showDispatches("displayUnsorted");
            }
        });
        dispatchesSubMenu.add(viewUnsorted);
        return dispatchesSubMenu;
    }

    private JMenu getSuppliersMenu() {
        JMenu dispatchesSubMenu = new JMenu("Suppliers");
        JMenuItem viewBySale = new JMenuItem("By Name");
        viewBySale.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                Client.this.showSuppliers("displayUnsorted");
            }
        });
        dispatchesSubMenu.add(viewBySale);
        return dispatchesSubMenu;
    }

    public static void switchLookAndFeel() {
        mainFrame.changeLookAndFeel();
    }

    private void changeLookAndFeel() {
        try {
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception var2) {
            var2.printStackTrace();
        }

    }
}
