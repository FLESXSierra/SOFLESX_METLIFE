package lesx.ui.property.editor.skin;

import java.time.LocalDate;

import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import lesx.gui.message.LesxMessage;
import lesx.ui.property.editor.LesxYesNoDateEditor;
import lesx.utils.LesxMisc;
import lesx.utils.LesxPropertyUtils;

public class LesxYesNoDateEditorSkin extends SkinBase<LesxYesNoDateEditor> {

  private static final String INVALID_STYLE = "-fx-text-fill: red;";
  private static final String VALID_STYLE = "-fx-text-fill: -fx-text-inner-color;";

  private CheckBox check = new CheckBox();
  private DatePicker date = new DatePicker();
  private HBox wrapper = new HBox();
  private boolean ignoreCommit;

  public LesxYesNoDateEditorSkin(LesxYesNoDateEditor control) {
    super(control);
    if (control.getCancelled()
        .getValue() != null) {
      check.setSelected(control.getCancelled()
          .getValue());
    }
    if (!LesxMisc.isEmptyString(control.getCancelled()
        .getDate())) {
      date.setValue(LocalDate.parse(control.getCancelled()
          .getDate(), LesxPropertyUtils.FORMATTER));
    }
    else {
      date.setValue(LocalDate.now());
    }
    ignoreCommit = true;
    verifyCheck();
    ignoreCommit = false;
    check.setDisable(control.isReadOnly());
    date.setDisable(control.isReadOnly());
    date.valueProperty()
        .addListener(obs -> verifyValid());
    check.selectedProperty()
        .addListener(obs -> verifyCheck());
    HBox.setHgrow(date, Priority.ALWAYS);
    wrapper.setAlignment(Pos.CENTER_LEFT);
    wrapper.setSpacing(5);
    wrapper.getChildren()
        .addAll(check, date);
    getChildren().add(wrapper);
  }

  private void verifyCheck() {
    date.setVisible(check.isSelected());
    check.setText(check.isSelected() ? LesxMessage.getMessage("TEXT-YES_NO_DATE_CHECK_BOX_YES") : LesxMessage.getMessage("TEXT-YES_NO_DATE_CHECK_BOX_NO"));
    verifyValid();
  }

  private void verifyValid() {
    boolean isValid = true;
    if (check.isSelected() && date.getValue() == null) {
      isValid = false;
    }
    getSkinnable().setValid(isValid);
    check.setStyle(isValid ? VALID_STYLE : INVALID_STYLE);
    getSkinnable().getCancelled()
        .setDate(date.getValue() != null ? date.getValue()
            .format(LesxPropertyUtils.FORMATTER) : "");
    getSkinnable().getCancelled()
        .setValue(check.isSelected());
    if (!ignoreCommit) {
      getSkinnable().commit();
    }
  }

}
