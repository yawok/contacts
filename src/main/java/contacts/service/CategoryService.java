package contacts.service;

import contacts.dao.CategoryDao;
import contacts.entities.Category;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class CategoryService {

    private final ObservableList<Category> categories;

    private static final CategoryDao categoryDao = new CategoryDao();

    /**
     * Call the Dao to get all the categories in the database
     * and initialize the list of categories
     */
    private CategoryService() {
        categories = FXCollections.observableArrayList();
        List<Category> listCategory = categoryDao.getAllCategories();
        categories.addAll(listCategory);
    }

    /**
     * Get all the categories in the database
     * @return
     *          a list of all the categories
     */
    public static ObservableList<Category> getCategories() {
        return CategoryServiceHolder.INSTANCE.categories;
    }

    /**
     * Get a Category object by its name
     * @param name
     *          the name of the category we want
     * @return
     *          the category corresponding
     */
    public static Category getByName(String name) {
        return getCategories().stream().filter(x -> name.equals(x.getCategoryName())).findFirst().orElse(null);
    }

    /**
     * Call the Dao to add a category
     *
     * @param category
     *              the category we want to add
     */
    public static void addCategory(Category category) {
        CategoryServiceHolder.INSTANCE.categories.add(category);
        categoryDao.addCategory(category);
    }

    /**
     * Call the Dao to update a category
     *
     * @param category
     *              the category we want to update
     */
    public static void updateCategory(Category category) {
        categoryDao.updateCategory(category);
    }

    /**
     * Call the Dao to delete a category
     *
     * @param category
     *              the Category we want to delete
     */
    public static void deleteCategory(Category category) {
        categoryDao.deleteCategory(category.getId());
    }

    private static class CategoryServiceHolder {
        private static final contacts.service.CategoryService INSTANCE = new contacts.service.CategoryService();
    }
}
