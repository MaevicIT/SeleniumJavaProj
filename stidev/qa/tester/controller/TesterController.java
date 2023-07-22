package stidev.qa.tester.controller;

import stidev.qa.tester.model.Browsers;
import stidev.qa.tester.model.Tester;
import stidev.qa.tester.model.Tests;
import stidev.qa.tester.view.TesterView;

public class TesterController {

    private final Tester test;
    private final TesterView view;

    public TesterController(Tester test, TesterView view) {
        this.test = test;
        this.view = view;

        initializeUI();
        addBrowserActionListeners();
        addTestActionListeners();
        addRunTestActionListener();
    }

    private void initializeUI() {
        view.testerForm.getTestDesc().setText("Pick browser to test on: ");
        view.testerForm.getTestTypeDesc().setText("Choose a test type");
        view.testerForm.getLoginButton().setEnabled(false);
        view.testerForm.getSignoutButton().setEnabled(false);
        view.testerForm.getImagesButton().setEnabled(false);
        view.testerForm.getSiteNavButton().setEnabled(false);
        view.testerForm.getRunTestButton().setEnabled(false);
    }

    private void addBrowserActionListeners() {
        view.testerForm.getChromeButton().addActionListener(e -> setBrowser(Browsers.CHROME));
        view.testerForm.getSafariButton().addActionListener(e -> setBrowser(Browsers.SAFARI));
        view.testerForm.getFireFoxButton().addActionListener(e -> setBrowser(Browsers.FIREFOX));
        view.testerForm.getEdgeButton().addActionListener(e -> setBrowser(Browsers.EDGE));
    }

    private void setBrowser(Browsers browser) {
        test.setBrowser(browser);
        view.testerForm.getTestDesc().setText(test.getBrowserDesc() + " TESTER");
        System.out.println(test.getBrowserDesc());
        view.testerForm.getLoginButton().setEnabled(true);
        view.testerForm.getSignoutButton().setEnabled(true);
        view.testerForm.getImagesButton().setEnabled(true);
        view.testerForm.getSiteNavButton().setEnabled(true);
    }

    private void addTestActionListeners() {
        view.testerForm.getLoginButton().addActionListener(e -> setTest(Tests.LOGIN));
        view.testerForm.getSignoutButton().addActionListener(e -> setTest(Tests.SIGNOUT));
        view.testerForm.getImagesButton().addActionListener(e -> setTest(Tests.IMAGES));
        view.testerForm.getSiteNavButton().addActionListener(e -> setTest(Tests.SITE_NAV));
    }

    private void setTest(Tests testType) {
        test.setTest(testType);
        view.testerForm.getTestTypeDesc().setText("Testing the " + test.getTestDesc() + " feature");
        view.testerForm.getRunTestButton().setEnabled(true);
        System.out.println(test.getTestDesc());
    }

    public void addRunTestActionListener() {
        view.testerForm.getRunTestButton().addActionListener(e -> {
            test.tester();
        });
    }
}
