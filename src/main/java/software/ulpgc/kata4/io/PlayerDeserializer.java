package software.ulpgc.kata4.io;

import software.ulpgc.kata4.model.Player;

public interface PlayerDeserializer {
    Player deserialize(String line);
}
