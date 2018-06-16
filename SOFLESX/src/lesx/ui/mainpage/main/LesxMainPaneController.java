package lesx.ui.mainpage.main;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import lesx.gui.message.LesxMessage;
import lesx.scene.controller.LesxController;
import lesx.ui.components.LesxTreeTableViewPane;

public class LesxMainPaneController extends LesxController {

  @FXML
  Label yearLabel;
  @FXML
  ComboBox<Integer> yearCombo;
  @FXML
  LesxTreeTableViewPane<String> mainPane;

  private BooleanProperty showProgress = new SimpleBooleanProperty(this, "showProgress", false);

  public LesxMainPaneController() {
    showProgress.set(true);
    setTitle(LesxMessage.getMessage("TEXT-TITLE_MAINPAGE_MAIN"));
    showProgress.set(false);
  }

}
