package nl.mpi.kinnate.plugins.export;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import nl.mpi.arbil.plugin.PluginBugCatcher;
import nl.mpi.arbil.plugin.PluginDialogHandler;
import nl.mpi.arbil.plugin.PluginException;
import nl.mpi.arbil.plugin.PluginSessionStorage;
import nl.mpi.kinnate.entityindexer.CollectionExport;
import nl.mpi.kinnate.entityindexer.QueryException;

/**
 * Created on : Nov 9, 2012, 1:40:55 PM
 *
 * @author Peter Withers <peter.withers@mpi.nl>
 */
public class MigrationWizard {

    final PluginBugCatcher bugCatcher;
    final PluginDialogHandler dialogHandler;
    final PluginSessionStorage sessionStorage;

    public MigrationWizard(PluginBugCatcher bugCatcher, PluginDialogHandler dialogHandler, PluginSessionStorage sessionStorage) {
        this.bugCatcher = bugCatcher;
        this.dialogHandler = dialogHandler;
        this.sessionStorage = sessionStorage;
    }

    public File checkAndOfferMigration() {
        // look for an old version of the application directory
        File oldAppDir = new File(sessionStorage.getApplicationSettingsDirectory().getParentFile(), ".kinoath-1-0");
        File oldAppExportFile = new File(oldAppDir, "MigrationWizard.kinoath");
        // look for a new version of the application directory
        File newAppDir = sessionStorage.getProjectWorkingDirectory();
        // if the old exists and the new does not or is empty then offer migration 
        if (oldAppDir.exists() && (!newAppDir.exists() || newAppDir.list().length < 3)) {
            if (!oldAppExportFile.exists()) {
                // create export file
                createDatabase(oldAppDir, oldAppExportFile);
            }
            // return the export file
            return oldAppExportFile;
        } else {
            return null;
        }
    }

    private void createDatabase(final File importDirectory, final File exportFile) {
//        new Thread() {
//            @Override
//            public void run() {
        final CollectionExport entityCollection = new CollectionExport(bugCatcher, sessionStorage);
        try {
            final GedcomExport gedcomExport = new GedcomExport(entityCollection);
            gedcomExport.dropAndCreate(importDirectory, "*.kmdi");
//                    dialogHandler.append("Completed cteating temporary database\n");
            //                    resultsText.setText("Generating export contents.\n");
            final String generateExportResult = gedcomExport.generateExport(gedcomExport.getGedcomQuery());
//                    resultsText.setText("Creating export file: " + saveFile.toString() + "\n");
            FileWriter fileWriter = new FileWriter(exportFile);
            fileWriter.write(generateExportResult);
            fileWriter.close();
//                    resultsText.setText("Export file complete.\n");
            dialogHandler.addMessageDialogToQueue("Save Complete", "Save File");
        } catch (IOException exception) {
//                    resultsText.setText("Error Saving File.\n");
            dialogHandler.addMessageDialogToQueue(exception.getMessage(), "Error Saving File");
            bugCatcher.logException(new PluginException(exception.getMessage()));
        } catch (QueryException exception) {
//                    resultsText.setText("Error Creating Export.\n");
            dialogHandler.addMessageDialogToQueue(exception.getMessage(), "Error Creating Export");
            bugCatcher.logException(new PluginException(exception.getMessage()));
        }
//            }
//        }.start();
    }
}
