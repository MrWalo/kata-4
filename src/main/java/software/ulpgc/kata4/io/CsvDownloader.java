package software.ulpgc.kata4.io;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class CsvDownloader {
    public static void downloadCsv(String imageURL, String route) throws IOException {
        Files.copy(new URL(imageURL).openStream(), Path.of(route), StandardCopyOption.REPLACE_EXISTING);
    }
}