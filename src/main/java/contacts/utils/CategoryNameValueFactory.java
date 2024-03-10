package contacts.utils;

import contacts.entities.Category;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class CategoryNameValueFactory
        implements Callback<ListView<Category>, ListCell<Category>> {

    @Override
    public ListCell<Category> call(ListView<Category> param) {
        return new ListCell<Category>() {
            @Override
            protected void updateItem(Category item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null || empty) {
                    setText(null);
                } else {
                    setText(item.getCategoryName());
                }
            }
        };
    }
}
