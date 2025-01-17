package software.ulpgc.kata4.io;

import software.ulpgc.kata4.model.Player;

import java.io.*;
import java.util.zip.GZIPInputStream;

public class FilePlayerReader implements PlayerReader, AutoCloseable{

    private final PlayerDeserializer deserializer;
    private final BufferedReader reader;

    public FilePlayerReader(File file, PlayerDeserializer deserializer) throws IOException {
        this.deserializer = deserializer;
        this.reader = new BufferedReader(
                new InputStreamReader(
                        new GZIPInputStream(
                                new FileInputStream(file))));
        this.reader.readLine();
    }

    @Override
    public Player read() throws IOException {
        return deserialize(reader.readLine());
    }

    @Override
    public void close() throws Exception {
        this.reader.close();
    }

    private Player deserialize(String line){
        return line != null?
                deserializer.deserialize(line):
                null;
    }
}
