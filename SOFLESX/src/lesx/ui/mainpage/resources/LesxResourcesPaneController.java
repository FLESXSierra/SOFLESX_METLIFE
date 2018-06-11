package lesx.ui.mainpage.resources;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import lesx.scene.controller.LesxController;

public class LesxResourcesPaneController extends LesxController {

  @FXML
  Pane treePane;
  @FXML
  Pane tablePane;

  private BooleanProperty showProgress = new SimpleBooleanProperty(this, "showProgress", false);

  @FXML
  public void initialize() {
    showProgress.set(true);

    showProgress.set(false);
  }

  @Override
  public BooleanProperty showProgressProperty() {
    return showProgress;
  }

  @Override
  protected boolean showAlert() {
    return false;
  }
}
