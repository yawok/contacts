package contacts.utils;

import contacts.entities.Category;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

public class CategoryValueFactory implements Callback<TableColumn.CellDataFeatures<Category, String>, ObservableValue<String>> {

    @Override
    public ObservableValue<String> call(TableColumn.CellDataFeatures<Category, String> cellData) {
        return new SimpleStringProperty(cellData.getValue().getCategoryName());
    }
}
