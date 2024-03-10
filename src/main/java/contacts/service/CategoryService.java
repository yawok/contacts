package contacts.service;

import contacts.dao.CategoryDao;
import contacts.entities.Category;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class CategoryService {

    private final ObservableList<Category> categories;

    private static final CategoryDao categoryDao = new CategoryDao();

    private CategoryService() {
        categories = FXCollections.observableArrayList();
        List<Category> listCategory = categoryDao.getAllCategories();
        categories.addAll(listCategory);
    }

    public static ObservableList<Category> getCategories() {
        return CategoryServiceHolder.INSTANCE.categories;
    }

    public static Category getByName(String name) {
        return getCategories().stream().filter(x -> name.equals(x.getCategoryName())).findFirst().orElse(null);
    }

    public static void addCategory(Category category) {
        CategoryServiceHolder.INSTANCE.categories.add(category);
        categoryDao.addCategory(category);
    }

    public static void updateCategory(Category category) {
        categoryDao.updateCategory(category);
    }

    public static void deleteCategory(Category category) {
        categoryDao.deleteCategory(category.getId());
    }

    private static class CategoryServiceHolder {
        private static final contacts.service.CategoryService INSTANCE = new contacts.service.CategoryService();
    }
}
