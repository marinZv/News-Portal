package rs.raf.demo.repositories.category;

import rs.raf.demo.entities.Category;
import rs.raf.demo.repositories.MySqlAbstractRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MySqlCategoryRepository extends MySqlAbstractRepository implements CategoryRepository {
    @Override
    public List<Category> allCategories() {
        List<Category> categories = new ArrayList<>();

        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try{
            connection = this.newConnection();
            statement = connection.createStatement();

            resultSet = statement.executeQuery("SELECT * FROM categories");
            while(resultSet.next()){
                String name = resultSet.getString("name");
                String description = resultSet.getString("description");
                Integer id = resultSet.getInt("id");
                Category category = new Category(id, name, description);
                categories.add(category);
            }


        }catch (Exception e){
            e.printStackTrace();
        }finally {
            this.closeStatement(statement);
            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }


        return categories;
    }

    @Override
    public Category findCategoryByName(String name) {
        Category category = null;

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try{
            connection = this.newConnection();

            preparedStatement = connection.prepareStatement("SELECT * FROM categories WHERE categories.name = ?");
            preparedStatement.setString(1, name);
            resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                Integer id = resultSet.getInt("id");
                String description = resultSet.getString("description");
                category = new Category(id, name, description);
            }

        }catch (Exception e){

        }finally {
            this.closeStatement(preparedStatement);
            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }

        return category;
    }

    @Override
    public Category addCategory(Category category) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try{
            connection = this.newConnection();

            preparedStatement = connection.prepareStatement("SELECT * FROM categories WHERE categories.name = ?");
            preparedStatement.setString(1, category.getName());
            resultSet = preparedStatement.executeQuery();

            String[] generatedColumns = {"id"};
            if(!resultSet.next()){
                preparedStatement = connection.prepareStatement("INSERT INTO categories (name, description) VALUES (?, ?)", generatedColumns);
                preparedStatement.setString(1, category.getName());
                preparedStatement.setString(2, category.getDescription());
                preparedStatement.executeUpdate();

                resultSet = preparedStatement.getGeneratedKeys();
                if(resultSet.next()){
                    category.setId(resultSet.getInt(1));
                }

            }


        }catch (Exception e){
            e.printStackTrace();
        }finally {
            this.closeStatement(preparedStatement);
            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }


        return category;
    }

    @Override
    public void deleteCategory(String name) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = this.newConnection();

            preparedStatement = connection.prepareStatement("DELETE FROM categories WHERE name = ?");
            preparedStatement.setString(1, name);
            preparedStatement.executeUpdate();

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            this.closeStatement(preparedStatement);
            this.closeConnection(connection);
        }
    }

    @Override
    public Category updateCategory(Category category, String name) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try{
            connection = this.newConnection();

            if(!name.equals(category.getName())){
                preparedStatement = connection.prepareStatement("SELECT * FROM categories WHERE name = ?");
                preparedStatement.setString(1, category.getName());
                resultSet = preparedStatement.executeQuery();
            }

            if(resultSet == null || !resultSet.next() || name.equals(category.getName())){
                preparedStatement = connection.prepareStatement("UPDATE categories SET categories.name = ?, categories.description = ? WHERE categories.name = ?");
                preparedStatement.setString(1, category.getName());
                preparedStatement.setString(2, category.getDescription());
                preparedStatement.setString(3, name);

                preparedStatement.executeUpdate();
            }


        }catch (Exception e){
            e.printStackTrace();
        }finally {
            this.closeStatement(preparedStatement);
            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }

        return category;
    }
}
