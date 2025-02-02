package software.ulpgc.kata4.io;

import software.ulpgc.kata4.commands.Command;
import software.ulpgc.kata4.model.Player;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class ImportCommand implements Command {

    private static final String CSV_PATH = new File("").getAbsolutePath() + "\\IE1.csv";

    public ImportCommand() {
    }

    @Override
    public void execute() {
        try {
            File input = getInputFile();
            File output = getOutputFile();
            doExecute(input, output);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private File getOutputFile() {
        return new File("inazumaeleven.db");
    }

    private File getInputFile() throws IOException {
        downloadCsvIfNeeded();
        return new File(CSV_PATH);
    }

    private void doExecute(File input, File output) throws Exception {
        try (PlayerReader reader = createPlayerReader(input);
             PlayerWriter writer = createPlayerWriter(output)) {
            Player player;
            while ((player = reader.read()) != null) {
                writer.write(player);
            }
        }
    }

    private DatabasePlayerWriter createPlayerWriter(File output) throws SQLException {
        return new DatabasePlayerWriter(deleteIfExists(output));
    }

    private static File deleteIfExists(File file) {
        if (file.exists()) file.delete();
        return file;
    }

    private FilePlayerReader createPlayerReader(File input) throws IOException {
        return new FilePlayerReader(input, new CsvPlayerDeserializer());
    }

    private void downloadCsvIfNeeded() throws IOException {
        File csvFile = new File(CSV_PATH);
        if (!csvFile.exists()) {
            String url = "https://github.com/MrWalo/Resources/raw/main/IE1.csv.gz";
            CsvDownloader.downloadCsv(url, CSV_PATH);
        }
    }
}
