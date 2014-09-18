package nl.mpi.kinnate.plugins.export;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import nl.mpi.flap.kinnate.entityindexer.QueryException;
import nl.mpi.flap.module.AbstractBaseModule;
import nl.mpi.flap.plugin.KinOathPanelPlugin;
import nl.mpi.flap.plugin.PluginBugCatcher;
import nl.mpi.flap.plugin.PluginDialogHandler;
import nl.mpi.flap.plugin.PluginException;
import nl.mpi.flap.plugin.PluginSessionStorage;
import nl.mpi.kinnate.entityindexer.CollectionExport;
import org.basex.core.BaseXException;

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
        final JPanel pluginPanel = new JPanel();
        final JEditorPane jEditorPane = new JEditorPane("text/plain", "db:list()");
        pluginPanel.add(jEditorPane);
        final JLabel errorLabel = new JLabel();
        pluginPanel.add(new JButton(new AbstractAction("run") {

            public void actionPerformed(ActionEvent e) {
                errorLabel.setText("");
                try {
                    System.out.println(entityCollection.performExportQuery(jEditorPane.getText()));
                } catch (QueryException exception) {
                    errorLabel.setText(exception.getMessage());
                }
            }
        }));
        pluginPanel.add(errorLabel);
        return new JScrollPane(pluginPanel);
    }
}
