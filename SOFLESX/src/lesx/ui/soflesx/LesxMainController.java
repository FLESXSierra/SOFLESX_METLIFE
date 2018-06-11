package lesx.ui.soflesx;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import lesx.gui.message.LesxMessage;
import lesx.scene.controller.LesxController;
import lesx.utils.LesxString;

public class LesxMainController extends LesxController {

  @FXML
  Label header;
  @FXML
  Label loadText;
  @FXML
  ProgressIndicator progress;

  public LesxMainController() {
    //Nothing here
  }

  @FXML
  public void initialize() {
    setTitle(LesxMessage.getMessage("TEXT-TITLE_READING_XML"));
    header.setText(LesxMessage.getMessage("TEXT-HEADER_READING_XML"));
    loadText.setText(LesxMessage.getMessage("TEXT-LOADING_TITLE"));
  }

  @Override
  protected void init() {
    // TODO LOAD HERE ALL XML
    LesxMain.getInstance()
        .activateScene(LesxString.MAINPAGE_PATH);
  }

}
