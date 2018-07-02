package lesx.ui.property.editor;

import java.util.Collection;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.ComboBox;
import lesx.property.properties.ELesxPropertyType;
import lesx.utils.LesxMisc;
import lesx.utils.LesxPropertyUtils;

public class LesxListEditor extends ComboBox<String> {

  private ObservableList<String> values = FXCollections.observableArrayList();
  private FilteredList<String> filteredValues;
  private BooleanProperty valid = new SimpleBooleanProperty(this, "valid", true);
  private ELesxPropertyType type;
  private ObjectProperty<Object> value = new SimpleObjectProperty<>(this, "value");
  private boolean mandatory = true;
  private String text;

  public LesxListEditor() {
    setEditable(true);
    type = ELesxPropertyType.TEXT;
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
      LesxPropertyUtils.copyValueToTypeProperty(type, text, value);
    }
    else {
      LesxPropertyUtils.copyValueToTypeProperty(type, text, value);
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

  public void setType(ELesxPropertyType type) {
    this.type = type;
  }

}
