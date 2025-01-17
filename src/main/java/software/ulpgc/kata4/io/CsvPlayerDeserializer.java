package software.ulpgc.kata4.io;

import software.ulpgc.kata4.model.Player;

public class CsvPlayerDeserializer implements PlayerDeserializer {
    private static final int NAME = 0;
    private static final int TEAM = 1;
    private static final int POSITION = 2;
    private static final int ELEMENT = 3;

    @Override
    public Player deserialize(String line) {
        return read(line.split(","));
    }

    private Player read(String[] fields){
        return new Player(fields[NAME], fields[TEAM], fields[POSITION], fields[ELEMENT] );
    }


}
