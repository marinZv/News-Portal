package rs.raf.demo.repositories;

import java.sql.*;
import java.util.Optional;

abstract public class MySqlAbstractRepository {

    public MySqlAbstractRepository(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    protected Connection newConnection() throws SQLException {
        return DriverManager.getConnection(
                "jdbc:mysql://" + this.getHost() + ":" + this.getPort() + "/" + this.getDatabaseName(), this.getUsername(), this.getPassword()
        );
    }

    protected String getHost(){
        return "localhost";
    }

    protected int getPort(){
        return 3306;
    }

    protected String getDatabaseName(){
        return "projectwebdb";
    }

    protected String getUsername() {
        return "root";
    }

    protected String getPassword() {
        return "ZvecanKosmet99";
    }

    protected void closeStatement(Statement statement) {
        try {
            Optional.of(statement).get().close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    protected void closeResultSet(ResultSet resultSet) {
        try {
            System.out.println("Ovde puca?: " +resultSet);
            Optional.of(resultSet).get().close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    protected void closeConnection(Connection connection) {
        try {
            Optional.of(connection).get().close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

}
