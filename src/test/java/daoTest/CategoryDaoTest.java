package daoTest;

import contacts.dao.CategoryDao;
import contacts.entities.Category;
import org.junit.Before;
import org.junit.Test;

import java.sql.*;
import java.util.List;

import static contacts.dao.DataSourceFactory.getConnection;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;

public class CategoryDaoTest {
    CategoryDao categoryDao = new CategoryDao();

    @Before
    public void initDatabase() {
        try(Connection connection = getConnection()) {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS category (" +
                    "    idcategory INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                    "    name VARCHAR(45) NOT NULL);");
            stmt.executeUpdate("DELETE FROM category");
            stmt.executeUpdate("INSERT INTO category(idcategory, name) VALUES(1, 'Friend')");
            stmt.executeUpdate("INSERT INTO category(idcategory, name) VALUES(2, 'Family')");
            stmt.executeUpdate("INSERT INTO category(idcategory, name) VALUES(3, 'Work Acquaintance')");
            stmt.executeUpdate("INSERT INTO category(idcategory, name) VALUES(4, 'Other')");
            stmt.close();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void shouldListAllCategories() {
        //GIVEN

        //WHEN
        List<Category> categoryList = categoryDao.getAllCategories();

        //THEN
        assertThat(categoryList).hasSize(4);
        assertThat(categoryList).extracting(Category::getId, Category::getCategoryName)
                .containsOnly(tuple(1, "Friend"),
                        tuple(2, "Family"),
                        tuple(3, "Work Acquaintance"),
                        tuple(4, "Other"));
    }

    @Test
    public void shouldAddCategory() {
        //GIVEN
        Category categoryToAdd = new Category(1, "New Category");

        //WHEN
        Category resultCategory = categoryDao.addCategory(categoryToAdd);

        //THEN
        assertThat(resultCategory).isNotNull();
        assertThat(resultCategory.getId()).isNotNull();
        assertThat(resultCategory.getCategoryName()).isEqualTo("New Category");
    }

    @Test
    public void shouldUpdateCategory() {
        //GIVEN
        Category categoryToUpdate = new Category(1, "Updated Category");

        //WHEN
        categoryDao.updateCategory(categoryToUpdate);

        //THEN
        try(Connection connection = getConnection()) {
            try(PreparedStatement statement = connection.prepareStatement("SELECT * FROM category WHERE idcategory=?"))
            {
                statement.setInt(1, 1);
                try(ResultSet resultSet = statement.executeQuery())
                {
                    assertThat(resultSet.next()).isTrue();
                    assertThat(resultSet.getInt("idcategory")).isNotNull();
                    assertThat(resultSet.getString("name")).isEqualTo("Updated Category");
                    assertThat(resultSet.next()).isFalse();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void shouldNotUpdateUnknownCategory() {
        //GIVEN
        Category categoryToUpdate = new Category(5, "Unknown Category");

        //WHEN
        categoryDao.updateCategory(categoryToUpdate);

        //THEN
        try(Connection connection = getConnection()) {
            try(PreparedStatement statement = connection.prepareStatement("SELECT * FROM category WHERE idcategory=?"))
            {
                statement.setInt(1, 5);
                try(ResultSet resultSet = statement.executeQuery())
                {
                    assertThat(resultSet.next()).isFalse();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void shouldDeleteCategory() {
        //GIVEN
        int idCategory = 1;

        //WHEN
        categoryDao.deleteCategory(idCategory);

        //THEN
        try(Connection connection = getConnection()) {
            try(PreparedStatement statement = connection.prepareStatement("SELECT * FROM category WHERE idcategory=?"))
            {
                statement.setInt(1, 1);
                try(ResultSet resultSet = statement.executeQuery())
                {
                    assertThat(resultSet.next()).isFalse();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
