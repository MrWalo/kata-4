package software.ulpgc.kata4.io;

import software.ulpgc.kata4.model.Player;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.List;

public class DatabasePlayerWriter implements PlayerWriter,AutoCloseable{
    private final Connection connection;
    private final PreparedStatement insertStatement;

    private final static String CreateTableStatement = """
            CREATE TABLE IF NOT EXISTS players (
                name TEXT PRIMARY KEY,
                team TEXT NOT NULL,
                position TEXT NOT NULL,
                element TEXT NOT NULL
            )
            """;
    private final static String InsertRecordStatement = """
            INSERT INTO players (name,team,position,element)
            VALUES (?,?,?,?)
            """;

    public DatabasePlayerWriter(File file) throws SQLException {
        this(connectionFor(file));
    }

    public DatabasePlayerWriter(String connection) throws SQLException{
        this.connection = DriverManager.getConnection(connection);
        this.connection.setAutoCommit(false);
        this.insertStatement = initDatabase(this.connection);
    }

    private PreparedStatement initDatabase(Connection connection) throws  SQLException{
        Statement statement = connection.createStatement();
        statement.execute(CreateTableStatement);
        return connection.prepareStatement(InsertRecordStatement);
    }

    private static String connectionFor(File file){return "jdbc:sqlite:"+file.getAbsolutePath();}

    @Override
    public void write(Player player) throws IOException {
        try{
            updateInsertStatementWith(player);
            insertStatement.execute();

        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private void updateInsertStatementWith(Player player) throws SQLException{
        for (Parameter parameter : toParameters(player))
            updateInsertStatementWith(parameter);
    }

    private void updateInsertStatementWith(Parameter parameter) throws SQLException{
        if(isNull(parameter.value))
            insertStatement.setNull(parameter.id, parameter.type);
        else
            insertStatement.setObject(parameter.id, parameter.value);
    }

    private boolean isNull(Object value){return value instanceof Integer && (Integer) value == -1;}

    private List<Parameter> toParameters(Player player){
        return List.of(
                new Parameter(1, player.name(), Types.LONGNVARCHAR),
                new Parameter(2, player.team(), Types.LONGNVARCHAR),
                new Parameter(3, player.position(), Types.LONGNVARCHAR),
                new Parameter(4, player.element(), Types.LONGNVARCHAR)
        );
    }

    private record Parameter(int id, Object value, int type){}

    @Override
    public void close() throws Exception {
        this.connection.commit();
    }
}
