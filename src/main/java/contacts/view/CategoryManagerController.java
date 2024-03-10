package contacts.view;

import contacts.entities.Category;
import contacts.service.CategoryService;
import contacts.utils.CategoryValueFactory;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

public class CategoryManagerController {
    @FXML
    private TableView<Category> categoryTable;
    @FXML
    private TableColumn<Category, String> categoryColumn;
    @FXML
    private GridPane formGrid;
    @FXML
    private TextField categoryNameField;

    private Category currentCategory;

    @FXML
    private void handleSaveButton() {
        currentCategory.setCategoryName(categoryNameField.getText());
        CategoryService.updateCategory(currentCategory);
        resetView();
    }

    @FXML
    private void handleDeleteButton() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete " + currentCategory.getCategoryName() + "?");
        alert.setHeaderText("Are you sure you want to delete the category " + currentCategory.getCategoryName() + "?");
        alert.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> deleteCurrentCategory());
    }

    private void deleteCurrentCategory() {
        int selectedIndex = categoryTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            CategoryService.deleteCategory(currentCategory);
            categoryTable.getItems().remove(selectedIndex);
            resetView();
        }
    }

    @FXML
    private void handleAddButton() {
        Category newCategory = new Category();
        newCategory.setCategoryName("New Category");
        CategoryService.addCategory(newCategory);
        categoryTable.getSelectionModel().select(newCategory);
    }

    @FXML
    private void initialize() {
        categoryColumn.setCellValueFactory(new CategoryValueFactory());
        populateList();
        categoryTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showCategoryDetails(newValue));
        resetView();
    }

    private void populateList() {
        categoryTable.setItems(CategoryService.getCategories());
        refreshList();
    }

    private void refreshList() {
        categoryTable.refresh();
        categoryTable.getSelectionModel().clearSelection();
    }

    private void resetView() {
        showCategoryDetails(null);
        refreshList();
        categoryTable.getSelectionModel().select(currentCategory);
    }

    private void showCategoryDetails(Category category) {
        if (category == null) {
            formGrid.setVisible(false);
        } else {
            formGrid.setVisible(true);
            currentCategory = category;
            categoryNameField.setText(currentCategory.getCategoryName());
        }
    }
}
