package software.ulpgc.kata4.io;

import software.ulpgc.kata4.model.Player;

import java.io.IOException;

public interface PlayerReader extends AutoCloseable {
    Player read() throws IOException;
}
