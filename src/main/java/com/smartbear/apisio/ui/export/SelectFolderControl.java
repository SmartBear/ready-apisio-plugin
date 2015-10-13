package com.smartbear.apisio.ui.export;

import com.eviware.soapui.support.UISupport;
import com.smartbear.apisio.Strings;
import com.smartbear.apisio.ui.validation.FieldValidator;
import com.smartbear.apisio.ui.validation.ValidationError;
import net.miginfocom.swing.MigLayout;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public final class SelectFolderControl {
    private final JPanel container = new JPanel();
    private final MigLayout layout = new MigLayout("wrap 3", "[][grow, fill][]");
    private final FieldValidator fieldValidator = new FieldValidator();

    private final JLabel title      = new JLabel(Strings.SelectFolderControl.TITLE);
    private final JTextField folder = new JTextField();
    private final JButton browse    = new JButton("Browse...");

    public SelectFolderControl() {
        container.setLayout(layout);
        Font oldFont = title.getFont();
        Font newFont = new Font(oldFont.getName(), Font.BOLD, oldFont.getSize() + 2);
        title.setFont(newFont);
        container.add(title, "span 3, gapbottom 20");
        container.add(new JLabel(Strings.SelectFolderControl.FOLDER));
        container.add(folder);
        container.add(browse);
        browse.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                    folder.setText(chooser.getSelectedFile().getAbsolutePath());
                }

            }
        });

        folder.setToolTipText(Strings.SelectFolderControl.FOLDER_HINT);
    }

    public Component getComponent() {
        return container;
    }

    public boolean validate() {
        ValidationError error = fieldValidator.validateFolderName(folder, "Folder");

        if (error != null) {
            UISupport.showErrorMessage(error.message);
            error.field.requestFocus();
        }

        return error == null;
    }

    public String getFolderName() {
        return folder.getText();
    }
}
