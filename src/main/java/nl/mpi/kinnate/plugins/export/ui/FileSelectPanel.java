/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.mpi.kinnate.plugins.export.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import nl.mpi.flap.plugin.PluginDialogHandler;
import nl.mpi.flap.plugin.PluginSessionStorage;
import sun.awt.HorizBagLayout;

/**
 * @since Sep 18, 2014 6:12:00 AM (creation date)
 * @author Peter Withers <peter.withers@mpi.nl>
 */
public class FileSelectPanel extends JPanel {

    private File exportFile = null;

    public FileSelectPanel(final PluginDialogHandler dialogHandler, final PluginSessionStorage sessionStorage, final String pluginName) {
        this.setLayout(new BorderLayout());
        final JLabel fileLabel = new JLabel();
        final String lastFile = sessionStorage.loadString("nl.mpi.kinnate.plugins.export." + pluginName);
        if (lastFile != null) {
            exportFile = new File(lastFile);
            fileLabel.setText(exportFile.toString());
        }

        this.add(fileLabel, BorderLayout.CENTER);

        this.add(new JButton(new AbstractAction("Browse") {

            public void actionPerformed(ActionEvent e) {
                File[] saveFile = dialogHandler.showFileSelectBox("Select Export File", false, false, null, PluginDialogHandler.DialogueType.save, null);
                if (saveFile != null && saveFile.length > 0) {
                    exportFile = saveFile[0];
                    fileLabel.setText(exportFile.toString());
                    sessionStorage.saveString("nl.mpi.kinnate.plugins.export." + pluginName, exportFile.toString());
                }

            }
        }), BorderLayout.LINE_END);
    }

    public File getExportFile() {
        return exportFile;
    }
}
