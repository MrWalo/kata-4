package software.ulpgc.kata4.io;

import software.ulpgc.kata4.model.Player;

import java.io.IOException;

public interface PlayerWriter extends AutoCloseable{
    void write(Player player) throws IOException;
}
