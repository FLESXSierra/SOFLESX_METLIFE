package lesx.ui.security;

import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.image.ImageView;
import lesx.icon.utils.LesxIcon;
import lesx.scene.controller.LesxController;
import lesx.utils.LesxAlertBuilder;

public class LesxBaitController extends LesxController {

  @FXML
  ImageView bait;

  @FXML
  public void initialize() {
    bait.setImage(LesxIcon.getImage(LesxIcon.BAIT)
        .getImage());
  }

  @FXML
  public void onMouseClickBait() {
    LesxAlertBuilder.create()
        .setTitle("Look at that Booty!")
        .setGraphic(LesxIcon.getImage(LesxIcon.PIRATE))
        .setContentText("Yaaargg! I'm Jack Sparrow!!")
        .setButtons(ButtonType.NO)
        .showAndWait();
    closeWindow();
  }

  @Override
  public String getTitle() {
    return "FLESX.";
  }

}
