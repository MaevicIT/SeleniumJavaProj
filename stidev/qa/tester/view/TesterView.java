package stidev.qa.tester.view;

import javax.swing.*;
import java.awt.*;

public class TesterView extends JFrame {

    public final TesterForm testerForm;

    public TesterView() {
        this.testerForm = new TesterForm();
        JPanel content = testerForm.getTestPanel();
        this.setPreferredSize(new Dimension(500, 300));
        this.setContentPane(content);
        this.initFrame();

        this.setTitle("QA Tester - Automated Tasks");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void initFrame() {
        // Ensure GUI creation and manipulation happen on the Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            this.pack();
            this.setVisible(true);
        });
    }
}

