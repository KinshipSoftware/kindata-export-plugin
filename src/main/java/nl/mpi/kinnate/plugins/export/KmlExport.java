package nl.mpi.kinnate.plugins.export;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import nl.mpi.flap.kinnate.entityindexer.QueryException;
import nl.mpi.flap.module.AbstractBaseModule;
import nl.mpi.flap.plugin.KinOathPanelPlugin;
import nl.mpi.flap.plugin.PluginBugCatcher;
import nl.mpi.flap.plugin.PluginDialogHandler;
import nl.mpi.flap.plugin.PluginException;
import nl.mpi.flap.plugin.PluginSessionStorage;
import nl.mpi.kinnate.entityindexer.CollectionExport;
import nl.mpi.kinnate.plugins.export.ui.FileSelectPanel;
import org.basex.core.BaseXException;

/**
 * Document : KmlExport Created on : Sep 20, 2014, 20:33
 *
 * @author Peter Withers
 */
public class KmlExport extends AbstractBaseModule implements KinOathPanelPlugin {

    private String exportProject;

    public KmlExport() throws PluginException {
        super("KML file export", "Exports KML files.", "nl.mpi.kinnate.plugins.export");
    }

    public JScrollPane getUiPanel(PluginDialogHandler dialogHandler, final PluginSessionStorage sessionStorage, PluginBugCatcher bugCatcher) throws PluginException {
        final CollectionExport entityCollection = new CollectionExport(bugCatcher, sessionStorage);
        final FileSelectPanel fileSelectPanel = new FileSelectPanel(dialogHandler, sessionStorage, "KmlExport");
        exportProject = sessionStorage.loadString("nl.mpi.kinnate.plugins.export.KmlExport.exportProject");
        final JPanel pluginPanel = new JPanel(new BorderLayout());
        final JTextArea textPane = new JTextArea("This plugin will export all entities that have lat, lon fields into a KML file for viewing/processing in mapping applications.\n"
                + "To use, select the project then browse for the file into which you wish to export your data and click \"run\".");
        try {
            final String[] databaseList = entityCollection.listDatabases();
            final JComboBox projectSelectBox = new JComboBox(databaseList);
            projectSelectBox.setSelectedItem(exportProject);
            projectSelectBox.setAction(new AbstractAction() {

                public void actionPerformed(ActionEvent e) {
                    exportProject = projectSelectBox.getSelectedItem().toString();
                    sessionStorage.saveString("nl.mpi.kinnate.plugins.export.KmlExport.exportProject", exportProject);
                }
            });
            pluginPanel.add(projectSelectBox, BorderLayout.LINE_START);
        } catch (BaseXException baseXException) {
            textPane.setText(baseXException.getMessage());
        }
        pluginPanel.add(textPane, BorderLayout.CENTER);
        final JLabel errorLabel = new JLabel();
        pluginPanel.add(new JButton(new AbstractAction("run") {

            public void actionPerformed(ActionEvent e) {
                errorLabel.setText("running...");
                try {
                    System.out.println("fileSelectPanel:" + fileSelectPanel.getExportFile().toString());
                    final String exportQueryResult = entityCollection.performExportQuery("db:list()"); // todo: add the actual query here
                    System.out.println(exportQueryResult);
                    FileWriter fileWriter = new FileWriter(fileSelectPanel.getExportFile());
                    fileWriter.write(exportQueryResult);
                    fileWriter.close();
                    errorLabel.setText("Export file complete.\n");
                } catch (QueryException exception) {
                    errorLabel.setText(exception.getMessage());
                } catch (IOException exception) {
                    errorLabel.setText(exception.getMessage());
                }
            }
        }), BorderLayout.LINE_END);
        pluginPanel.add(fileSelectPanel, BorderLayout.PAGE_START);
        pluginPanel.add(errorLabel, BorderLayout.PAGE_END);
        return new JScrollPane(pluginPanel);
    }
}
