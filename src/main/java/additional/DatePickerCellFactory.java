package additional;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;
import javafx.scene.control.DatePicker;
import java.time.LocalDate;

public class DatePickerCellFactory<S, T> implements Callback<TableColumn<S, T>, TableCell<S, T>> {
    @Override
    public TableCell<S, T> call(TableColumn<S, T> column) {
        return new TableCell<S, T>() {
            private DatePicker datePicker;

            {
                datePicker = new DatePicker();
                datePicker.setEditable(true);

                // Commit the edit on Enter key or focus lost
                datePicker.setOnAction(event -> {
                    commitEdit((T) datePicker.getValue());
                });
            }

            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    if (item instanceof LocalDate) {
                        datePicker.setValue((LocalDate) item);
                        setGraphic(datePicker);
                    }
                }
            }
        };
    }
}
