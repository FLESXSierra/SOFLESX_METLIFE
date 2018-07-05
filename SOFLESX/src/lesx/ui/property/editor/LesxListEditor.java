package lesx.ui.property.editor;

import java.util.Collection;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.ComboBox;
import lesx.utils.LesxMisc;

public class LesxListEditor extends ComboBox<String> {

  private ObservableList<String> values = FXCollections.observableArrayList();
  private FilteredList<String> filteredValues;
  private BooleanProperty valid = new SimpleBooleanProperty(this, "valid", true);
  private StringProperty value = new SimpleStringProperty(this, "value");
  private boolean mandatory = true;
  private String text;

  public LesxListEditor() {
    setEditable(true);
    getEditor().textProperty()
        .addListener(obs -> valueChanged());
    filteredValues = new FilteredList<String>(values, p -> true);
    filterList();
    setItems(filteredValues);
    valid.addListener(obs -> changeStyle());
  }

  private void valueChanged() {
    text = getEditor().getText();
    filterList();
    if (isValid()) {
      value.set(text);
    }
    else {
      value.set(null);
    }
  }

  private void changeStyle() {
    getEditor().setStyle(isValid() ? "-fx-text-fill: -fx-text-inner-color;" : "-fx-text-fill: red;");
  }

  private void filterList() {
    validate();
    Platform.runLater(() -> {
      if (LesxMisc.isEmptyString(text)) {
        filteredValues.setPredicate(s -> true);
      }
      else {
        filteredValues.setPredicate(s -> s.toLowerCase()
            .startsWith(text.toLowerCase()));
      }
    });
  }

  protected void validate() {
    if (filteredValues.contains(text)) {
      valid.set(true);
    }
    else if (LesxMisc.equals(values.size(), filteredValues.size()) && LesxMisc.isEmptyString(getEditor().getText()) && !mandatory) {
      valid.set(true);
    }
    else {
      valid.set(false);
    }
  }

  public StringProperty valueListProperty() {
    return value;
  }

  public void setValid(boolean valid) {
    this.valid.set(valid);
  }

  public boolean isValid() {
    return valid.get();
  }

  public BooleanProperty validProperty() {
    return valid;
  }

  public String getStringValue() {
    return isValid() ? getValue() : "";
  }

  public void setListValues(Collection<String> values) {
    this.values.setAll(values);
  }

}
