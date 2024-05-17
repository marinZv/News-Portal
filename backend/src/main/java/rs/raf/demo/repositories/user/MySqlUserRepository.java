package rs.raf.demo.repositories.user;

import org.apache.commons.codec.digest.DigestUtils;
import rs.raf.demo.entities.User;
import rs.raf.demo.repositories.MySqlAbstractRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySqlUserRepository extends MySqlAbstractRepository implements UserRepository {

    @Override
    public User findUser(String email) {

        User user = null;

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = this.newConnection();

            preparedStatement = connection.prepareStatement("SELECT * FROM users WHERE email = ?");
            preparedStatement.setString(1, email);
            resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                Integer id = resultSet.getInt("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String password = resultSet.getString("password");
                Integer status = resultSet.getInt("status");
                Integer role = resultSet.getInt("role");

                user = new User( id,  firstName,  lastName,  email,  password,  role,  status);
            }

        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            this.closeStatement(preparedStatement);
            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }

        return user;
    }

    @Override
    public User addUser(User user) {

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try{
            connection = this.newConnection();

            preparedStatement = connection.prepareStatement("SELECT * FROM users WHERE email = ?");
            preparedStatement.setString(1, user.getEmail());
            resultSet = preparedStatement.executeQuery();


            String[] generatedColumns = {"id"};
            if(!resultSet.next()){
                preparedStatement = connection.prepareStatement("INSERT INTO users (first_name, last_name, email, password, role, status) VALUES (?,?,?,?,?,?)", generatedColumns);

                preparedStatement.setString(1, user.getFirstName());
                preparedStatement.setString(2, user.getLastName());
                preparedStatement.setString(3, user.getEmail());
                preparedStatement.setString(4, DigestUtils.sha256Hex(user.getPassword()));
                preparedStatement.setInt(5, user.getRole());
                preparedStatement.setInt(6, user.getStatus());

                preparedStatement.executeUpdate();

                resultSet = preparedStatement.getGeneratedKeys();

                System.out.println(resultSet.getInt(1));

                if(resultSet.next()){
                    user.setId(resultSet.getInt(1));
                }

            }



        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            this.closeResultSet(resultSet);
            this.closeStatement(preparedStatement);
            this.closeConnection(connection);
        }

        return user;
    }

    @Override
    public List<User> allUsers() {
        List<User> users = new ArrayList<>();

        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;


        try{
            connection = this.newConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM users");

            while(resultSet.next()){
                User user = new User(resultSet.getInt("id"), resultSet.getString("first_name"), resultSet.getString("last_name"),
                        resultSet.getString("email"), resultSet.getString("password"));

                user.setStatus(resultSet.getInt("status"));
                user.setRole(resultSet.getInt("role"));

                users.add(user);

                System.out.println(user.getId());
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            this.closeStatement(statement);
            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }

        return users;
    }

    @Override
    public void deleteUser(String email) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try{
            connection = this.newConnection();
            preparedStatement = connection.prepareStatement("DELETE FROM users WHERE email = ?");
            preparedStatement.setString(1, email);
            preparedStatement.executeUpdate();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            this.closeStatement(preparedStatement);
            this.closeConnection(connection);
        }
    }

    @Override
    public User updateUser(User user, String email) {

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try{
            connection = this.newConnection();

            if(!user.getEmail().equals(email)){
                preparedStatement = connection.prepareStatement("SELECT * users WHERE email = ?");
                preparedStatement.setString(1, user.getEmail());
                resultSet = preparedStatement.executeQuery();
            }

            if(resultSet == null || !resultSet.next() || email.equals(user.getEmail())){
                preparedStatement = connection.prepareStatement("UPDATE users SET users.first_name = ?, users.last_name = ?, users.email = ?, users.role = ? WHERE users.email = ?");

                preparedStatement.setString(1,user.getFirstName());
                preparedStatement.setString(2, user.getLastName());
                preparedStatement.setString(3, user.getEmail());
                preparedStatement.setInt(4, user.getRole());
                preparedStatement.setString(5, email);

                preparedStatement.executeUpdate();
            }

        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            closeStatement(preparedStatement);
            closeResultSet(resultSet);
            closeConnection(connection);
        }

        return user;
    }

    @Override
    public void changeUserActivity(String email) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = this.newConnection();

            preparedStatement = connection.prepareStatement("SELECT * FROM users WHERE email = ?");
            preparedStatement.setString(1, email);
            resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                int status = resultSet.getInt("status");

                if(status == 0){
                    status = 1;
                }else{
                    status = 0;
                }

                preparedStatement = connection.prepareStatement("UPDATE users SET status = ? WHERE email = ?");
                preparedStatement.setInt(1, status);
                preparedStatement.setString(2, email);
                preparedStatement.executeUpdate();
            }

        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            this.closeStatement(preparedStatement);
            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }
    }
}
