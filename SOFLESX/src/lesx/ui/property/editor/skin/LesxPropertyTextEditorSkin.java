package lesx.ui.property.editor.skin;

import com.sun.javafx.scene.control.skin.TextFieldSkin;

import javafx.beans.property.ObjectProperty;
import lesx.property.properties.ELesxPropertyType;
import lesx.ui.property.editor.LesxTextEditor;
import lesx.utils.LesxMisc;
import lesx.utils.LesxPropertyUtils;

public class LesxPropertyTextEditorSkin extends TextFieldSkin {

  private static final String INVALID_STYLE = "-fx-text-fill: red;";
  private static final String VALID_STYLE = "-fx-text-fill: -fx-text-inner-color;";

  private ELesxPropertyType type;
  private ObjectProperty<Object> value;

  public LesxPropertyTextEditorSkin(LesxTextEditor editor) {
    super(editor);
    editor.validProperty()
        .addListener(obs -> changedValid(editor.isValid()));
    editor.setDisable(editor.isReadOnly());
    value = editor.valueProperty();
    type = editor.getType();
    editor.textProperty()
        .addListener(obs -> textChanged(editor.getText()));
    initialize();
  }

  private void initialize() {
    if (!LesxMisc.isEmpty(value.getValue())) {
      getSkinnable().setText(value.getValue()
          .toString());
    }
  }

  private void textChanged(String text) {
    if (!String.valueOf(text)
        .equals(String.valueOf(value.get()))) {
      LesxPropertyUtils.copyValueToTypeProperty(type, text, value);
    }
  }

  private void changedValid(boolean valid) {
    if (valid) {
      getSkinnable().setStyle(VALID_STYLE);
    }
    else {
      getSkinnable().setStyle(INVALID_STYLE);
    }
  }

}
