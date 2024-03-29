package contacts.dao;

import contacts.entities.Category;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import static contacts.dao.DataSourceFactory.getConnection;

public class CategoryDao {

    /**
     * Get list of all categories in the database
     *
     * @return
     *          the list of all the categories
     */
    public List<Category> getAllCategories() {
        List<Category> allCategories = new LinkedList<>();
        String query = "SELECT * FROM category";
        try(Connection connection = getConnection())
        {
            try(PreparedStatement statement = connection.prepareStatement(query))
            {
                try (ResultSet resultSet = statement.executeQuery())
                {
                    while (resultSet.next())
                    {
                        Category category = new Category(resultSet.getInt("idcategory"),
                                resultSet.getString("name"));
                        allCategories.add(category);
                    }
                    return allCategories;
                }
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * Add a category in the database
     *
     * @param category
     *              the category that we want to add
     * @return
     *              the category that has been added in the database
     */
    public Category addCategory(Category category) {
        String query = "INSERT INTO category(name) VALUES(?)";
        try(Connection connection = getConnection())
        {
            try(PreparedStatement statement = connection.prepareStatement(query))
            {
                statement.setString(1, category.getCategoryName());

                statement.executeUpdate();
                try (ResultSet resultId = statement.getGeneratedKeys())
                {
                    if(resultId.next())
                    {
                        category.setId(resultId.getInt(1));
                        return category;
                    }
                }
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * Update a category in the database
     *
     * @param category
     *              the category we want to update
     */
    public void updateCategory(Category category) {
        String query = "UPDATE category "
                + "SET name=?"
                + "WHERE idcategory=?;";
        try(Connection connection = getConnection())
        {
            try(PreparedStatement statement = connection.prepareStatement(query))
            {
                statement.setInt(2, category.getId());
                statement.setString(1, category.getCategoryName());

                statement.executeUpdate();
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Delete a category
     *
     * @param id
     *          the id of the category we want to delete
     */
    public void deleteCategory(int id){
        String query = "DELETE FROM category WHERE idcategory=?";

        try (Connection connection = getConnection())
        {
            try(PreparedStatement statement = connection.prepareStatement(query))
            {
                statement.setInt(1, id);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
