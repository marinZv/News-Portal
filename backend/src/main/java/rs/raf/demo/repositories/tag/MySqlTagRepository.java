package rs.raf.demo.repositories.tag;

import rs.raf.demo.entities.Tag;
import rs.raf.demo.repositories.MySqlAbstractRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MySqlTagRepository extends MySqlAbstractRepository implements TagRepository {
    @Override
    public List<Tag> allTags() {
        List<Tag> tags = new ArrayList<>();

        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try{
            connection = this.newConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM tag");

            while(resultSet.next()){
                Integer id = resultSet.getInt("id");
                String tagName = resultSet.getString("tag_name");
                Tag tag = new Tag(id, tagName);

                tags.add(tag);
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            this.closeStatement(statement);
            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }

        return tags;
    }

    @Override
    public Tag addTag(Tag tag) {

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        ResultSet checkingResultSet = null;

        try {
            connection = this.newConnection();
            preparedStatement = connection.prepareStatement("SELECT * FROM tag WHERE tag_name = ?");
            preparedStatement.setString(1,tag.getTagName());

            checkingResultSet = preparedStatement.executeQuery();

            String[] generatedColumns = {"id"};
            if(!checkingResultSet.next()){
                preparedStatement = connection.prepareStatement("INSERT INTO tag (tag_name) VALUES (?)", generatedColumns);
                preparedStatement.setString(1, tag.getTagName());
                preparedStatement.executeUpdate();

                resultSet = preparedStatement.getGeneratedKeys();
                if(resultSet.next()){
                    tag.setId(resultSet.getInt(1));
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            this.closeStatement(preparedStatement);
            this.closeResultSet(resultSet);
            this.closeResultSet(checkingResultSet);
            this.closeConnection(connection);
        }

        return tag;
    }

    @Override
    public Tag findTagByName(String tagName) {

        Tag tag = null;

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try{

            connection = this.newConnection();
            preparedStatement = connection.prepareStatement("SELECT * FROM tag WHERE tag.tag_name = ?");
            preparedStatement.setString(1, tagName);

            resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                Integer id = resultSet.getInt("id");
                tag = new Tag(id, tagName);
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            this.closeStatement(preparedStatement);
            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }


        return tag;
    }

    @Override
    public Tag findTagById(Integer id) {
        Tag tag = null;

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try{
            connection = this.newConnection();
            preparedStatement = connection.prepareStatement("SELECT * FROM tag where tag.id = ?");
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                String tagName = resultSet.getString("tag_name");
                tag = new Tag(id, tagName);
            }


        }catch (Exception e){

        }finally {
            this.closeStatement(preparedStatement);
            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }

        return tag;
    }

    @Override
    public void deleteTag(Integer id) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try{
            connection = this.newConnection();
            preparedStatement = connection.prepareStatement("DELETE FROM tag WHERE id = ?");
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            this.closeStatement(preparedStatement);
            this.closeConnection(connection);
        }

    }
}
