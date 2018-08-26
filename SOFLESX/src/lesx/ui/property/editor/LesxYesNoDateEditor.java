package lesx.ui.property.editor;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import lesx.property.properties.LesxCancelledBusiness;
import lesx.property.properties.LesxProperty;
import lesx.ui.property.editor.skin.LesxYesNoDateEditorSkin;

public class LesxYesNoDateEditor extends Control {

  private LesxCancelledBusiness cancelled = new LesxCancelledBusiness();
  private LesxProperty property;
  private BooleanProperty valid = new SimpleBooleanProperty(this, "valid", true);
  private boolean isReadOnly;

  public LesxYesNoDateEditor(LesxProperty fxProperty) {
    property = fxProperty;
    if (fxProperty.getValue() != null) {
      cancelled.setDate(((LesxCancelledBusiness) fxProperty.getValue()).getDate());
      cancelled.setValue(((LesxCancelledBusiness) fxProperty.getValue()).getValue());
    }
    isReadOnly = fxProperty.isReadOnly();
    valid.bindBidirectional(fxProperty.validProperty());
  }

  public BooleanProperty validProperty() {
    return valid;
  }

  public void setValid(boolean valid) {
    this.valid.set(valid);
  }

  public boolean isValid() {
    return valid.get();
  }

  public boolean isReadOnly() {
    return isReadOnly;
  }

  public LesxCancelledBusiness getCancelled() {
    return cancelled;
  }

  @Override
  protected Skin<?> createDefaultSkin() {
    return new LesxYesNoDateEditorSkin(this);
  }

  public void commit() {
    Platform.runLater(() -> property.setValue(cancelled.clone()));
  }

}
