package stidev.qa.tester.view;

import javax.swing.*;

public class TesterForm {
    public JTextField getTestURLTextField() {
        return testURLTextField;
    }

    public JButton getRunTestButton() {
        return runTestButton;
    }

    public JPanel getTestPanel() {
        return testPanel;
    }

    public JLabel getTestDesc() {
        return testDesc;
    }

    public JButton getChromeButton() {
        return chromeButton;
    }

    public JButton getSafariButton() {
        return safariButton;
    }

    public JButton getFireFoxButton() {
        return fireFoxButton;
    }

    public JButton getEdgeButton() {
        return edgeButton;
    }

    public JLabel getTestTypeDesc() {
        return testTypeDesc;
    }

    public JButton getLoginButton() {
        return loginButton;
    }

    public JButton getSignoutButton() {
        return signoutButton;
    }

    public JButton getSiteNavButton() {
        return siteNavButton;
    }

    public JButton getImagesButton() {
        return imagesButton;
    }

    private JPanel testPanel;
    private JLabel testDesc;
    private JButton chromeButton;
    private JButton safariButton;
    private JButton fireFoxButton;
    private JButton edgeButton;
    private JLabel testTypeDesc;
    private JButton loginButton;
    private JButton signoutButton;
    private JButton siteNavButton;
    private JButton imagesButton;
    private JButton runTestButton;
    private JTextField testURLTextField;
}
