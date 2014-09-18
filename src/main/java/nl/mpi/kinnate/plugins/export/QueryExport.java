package nl.mpi.kinnate.plugins.export;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import nl.mpi.flap.kinnate.entityindexer.QueryException;
import nl.mpi.flap.module.AbstractBaseModule;
import nl.mpi.flap.plugin.KinOathPanelPlugin;
import nl.mpi.flap.plugin.PluginBugCatcher;
import nl.mpi.flap.plugin.PluginDialogHandler;
import nl.mpi.flap.plugin.PluginException;
import nl.mpi.flap.plugin.PluginSessionStorage;
import nl.mpi.kinnate.entityindexer.CollectionExport;
import nl.mpi.kinnate.plugins.export.ui.FileSelectPanel;

/**
 * Document : QueryExport Created on : Sept 17, 2014, 21:53 PM
 *
 * @author Peter Withers
 */
public class QueryExport extends AbstractBaseModule implements KinOathPanelPlugin {

    public QueryExport() throws PluginException {
        super("XQuery export", "Allows custom XQuery Exports.", "nl.mpi.kinnate.plugins.export");
    }

    public JScrollPane getUiPanel(PluginDialogHandler dialogHandler, PluginSessionStorage sessionStorage, PluginBugCatcher bugCatcher) throws PluginException {
        final CollectionExport entityCollection = new CollectionExport(bugCatcher, sessionStorage);
        final FileSelectPanel fileSelectPanel = new FileSelectPanel(dialogHandler, sessionStorage, "QueryExport");
        final JPanel pluginPanel = new JPanel(new BorderLayout());
        final JEditorPane jEditorPane = new JEditorPane("text/plain", "db:list()");
        pluginPanel.add(new JScrollPane(jEditorPane), BorderLayout.CENTER);
        final JLabel errorLabel = new JLabel();
        pluginPanel.add(new JButton(new AbstractAction("run") {

            public void actionPerformed(ActionEvent e) {
                errorLabel.setText("running...");
                try {
                    System.out.println("fileSelectPanel:" + fileSelectPanel.getExportFile().toString());
                    final String exportQueryResult = entityCollection.performExportQuery(jEditorPane.getText());
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
