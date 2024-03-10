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

    /**
     * When we click on the "Save" button
     * we call the service to update the category
     */
    @FXML
    private void handleSaveButton() {
        currentCategory.setCategoryName(categoryNameField.getText());
        CategoryService.updateCategory(currentCategory);
        resetView();
    }

    /**
     * When we click on the "Delete" button
     * we show an alert to be sure that we didn't click by error
     */
    @FXML
    private void handleDeleteButton() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete " + currentCategory.getCategoryName() + "?");
        alert.setHeaderText("Are you sure you want to delete the category " + currentCategory.getCategoryName() + "?");
        alert.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> deleteCurrentCategory());
    }

    /**
     * We call the service to delete the category in the database
     */
    private void deleteCurrentCategory() {
        int selectedIndex = categoryTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            CategoryService.deleteCategory(currentCategory);
            categoryTable.getItems().remove(selectedIndex);
            resetView();
        }
    }

    /**
     * When we click on the "Add a category" button
     * we call the service to add the category in the database
     */
    @FXML
    private void handleAddButton() {
        Category newCategory = new Category();
        newCategory.setCategoryName("New Category");
        CategoryService.addCategory(newCategory);
        categoryTable.getSelectionModel().select(newCategory);
    }

    /**
     * When we arrive on the page we fill the list with categories
     * and we add the listener to show the category details when we select a category in the list
     */
    @FXML
    private void initialize() {
        categoryColumn.setCellValueFactory(new CategoryValueFactory());
        populateList();
        categoryTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showCategoryDetails(newValue));
        resetView();
    }

    /**
     * Call the service to get the list of categories and show theme in the list
     */
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

    /**
     * When we clicked on a category in the list we will show the details on the right of the page
     * @param category
     *               the category that we have selected
     */
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
