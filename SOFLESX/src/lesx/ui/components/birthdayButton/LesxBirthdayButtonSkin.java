package lesx.ui.components.birthdayButton;

import com.sun.javafx.scene.control.skin.ButtonSkin;

import javafx.collections.ListChangeListener;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Tooltip;
import javafx.stage.StageStyle;
import lesx.icon.utils.LesxIcon;
import lesx.ui.soflesx.LesxMain;
import lesx.utils.LesxAlertBuilder;

public class LesxBirthdayButtonSkin extends ButtonSkin {

  private LesxBirthdayButton button;

  public LesxBirthdayButtonSkin(LesxBirthdayButton button) {
    super(button);
    this.button = button;
    button.setText("");
    button.getNames()
        .addListener((ListChangeListener<String>) obs -> rebuildButton());
    button.setOnAction(obs -> showAlert());
    rebuildButton();
  }

  private void showAlert() {
    LesxAlertBuilder.create()
        .setType(AlertType.CONFIRMATION)
        .setStyle(StageStyle.UTILITY)
        .setTitle("¡Cumpleaños!")
        .setHeaderText(buildNamesString())
        .setGraphic(LesxIcon.getImage(LesxIcon.PARTY))
        .setOwner(LesxMain.getInstance()
            .getStage())
        .setButtons(ButtonType.OK)
        .show();
  }

  private String buildNamesString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("¡Estamos de cumpleaños!\n");
    for (String name : button.getNames()) {
      stringBuilder.append(" - ");
      stringBuilder.append(name);
      stringBuilder.append("\n");
    }
    return stringBuilder.toString();
  }

  private void rebuildButton() {
    button.setGraphic(null);
    button.setTooltip(null);
    Tooltip tool = new Tooltip();
    tool.setAutoHide(true);
    if (!button.getNames()
        .isEmpty()) {
      button.setDisable(false);
      button.setGraphic(LesxIcon.getImage(LesxIcon.PRESENT));
      tool.setText(buildNamesString());
    }
    else {
      button.setDisable(true);
      button.setGraphic(LesxIcon.getImage(LesxIcon.CLOCK));
    }
    button.setTooltip(tool);
  }

}
