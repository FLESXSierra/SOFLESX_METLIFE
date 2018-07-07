package lesx.ui.property.editor;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.DatePicker;
import javafx.util.StringConverter;
import lesx.property.properties.LesxProperty;
import lesx.utils.LesxMisc;
import lesx.utils.LesxPropertyUtils;

public class LesxPeriodDatePicker extends DatePicker {

  private BooleanProperty valid = new SimpleBooleanProperty(this, "valid", true);
  private StringProperty period = new SimpleStringProperty(this, "period", "");
  private boolean mandatory;

  public LesxPeriodDatePicker(LesxProperty fxProperty) {
    setConverter(new PeriodDateConverter());
    if (fxProperty.getValue() != null) {
      try {
        LocalDate date = LocalDate.parse(fxProperty.getValue()
            .toString(), LesxPropertyUtils.FORMATTER);
        date.format(LesxPropertyUtils.FORMATTER);
        setValue(date);
        valueChanged();
      }
      catch (DateTimeParseException e) {
        period.set("");
      }
    }
    valid.bindBidirectional(fxProperty.validProperty());
    valid.addListener(obs -> changeStyle());
    mandatory = fxProperty.isMandatory();
    if (fxProperty.isReadOnly()) {
      setEditable(false);
    }
    valueProperty().addListener(obs -> valueChanged());
    fxProperty.getPropertyValue()
        .bind(period);
  }

  private void valueChanged() {
    if (!LesxMisc.isEmptyString(getValue().toString())) {
      try {
        period.set(getValue().format(LesxPropertyUtils.FORMATTER)
            .toString());
        valid.set(true);
      }
      catch (DateTimeParseException e) {
        valid.set(false);
        period.set("");
      }
    }
    else if (mandatory) {
      valid.set(false);
    }
  }

  private void changeStyle() {
    getEditor().setStyle(valid.get() ? "-fx-text-fill: -fx-text-inner-color;" : "-fx-text-fill: red;");
  }

  /**
   * Class to convert from dd/MM/yyyy to dd.MM.yyyy
   *
   * @author led_s
   *
   */
  private class PeriodDateConverter extends StringConverter<LocalDate> {
    @Override
    public LocalDate fromString(String date) {
      return LocalDate.parse(date, LesxPropertyUtils.FORMATTER);
    }

    @Override
    public String toString(LocalDate date) {
      if (date == null) {
        return "";
      }
      return date.format(LesxPropertyUtils.FORMATTER);
    }
  }

  public BooleanProperty validProperty() {
    return valid;
  }

}
