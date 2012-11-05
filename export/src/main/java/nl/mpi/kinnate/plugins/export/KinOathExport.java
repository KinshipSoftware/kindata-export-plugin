package nl.mpi.kinnate.plugins.export;

import javax.swing.JScrollPane;
import nl.mpi.arbil.plugin.KinOathPanelPlugin;
import nl.mpi.arbil.plugin.PluginBugCatcher;
import nl.mpi.arbil.plugin.PluginDialogHandler;
import nl.mpi.arbil.plugin.PluginException;
import nl.mpi.arbil.plugin.PluginSessionStorage;
import nl.mpi.kinnate.plugin.AbstractBasePlugin;
import nl.mpi.kinnate.plugins.export.ui.KinOathExportPanel;

/**
 * Document : KinOathExport Created on : Aug 13, 2012, 6:35:12 PM
 *
 * @author Peter Withers
 */
public class KinOathExport extends AbstractBasePlugin implements KinOathPanelPlugin {

    public KinOathExport() throws PluginException {
        super("KinOath Single File Export", "Creates a single KinOath file which contains all fields and relations of all entities via a temp database.", "nl.mpi.kinnate.plugins.export");
    }

    public JScrollPane getUiPanel(PluginDialogHandler dialogHandler, PluginSessionStorage sessionStorage, PluginBugCatcher bugCatcher) throws PluginException {
        final KinOathExportPanel exportPanel = new KinOathExportPanel(dialogHandler, sessionStorage, bugCatcher);
        return new JScrollPane(exportPanel);
    }
}
