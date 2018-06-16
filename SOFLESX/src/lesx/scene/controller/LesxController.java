package lesx.scene.controller;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import lesx.gui.message.LesxMessage;

public class LesxController {
  private String title;
  private Stage stage;
  private boolean showAlert = true;
  private BooleanProperty showProgress = new SimpleBooleanProperty(this, "showProgress", false);

  public LesxController() {
    //Nothing
  }

  public LesxController(Stage stage) {
    setExitOperation(stage);
  }

  public BooleanProperty showProgressProperty() {
    return showProgress;
  }

  protected void init() {
    //Nothing
  }

  protected String getTitle() {
    return title;
  }

  protected void setTitle(String title) {
    this.title = title;
  }

  /**
   * Method called on Exit Operation
   */
  protected void onCloseWindow() {
    closeWindow();
  }

  /**
   * Execute Platform.exit(); and System.exit(0);
   */
  protected void closeWindow() {
    //Nothing
  }

  /**
   * Shows the Verification Alert on close Stage.
   *
   * @return showAlert
   */
  protected boolean showAlert() {
    return showAlert;
  }

  /**
   * Sets Exit handler operation
   */
  public void setExitOperation(Stage primaryStage) {
    stage = primaryStage;
    stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
      @Override
      public void handle(WindowEvent event) {
        onCloseWindow();
        if (showAlert) {
          Alert alert = new Alert(AlertType.CONFIRMATION);
          alert.setTitle(LesxMessage.getMessage("TEXT-ALERT_TITLE_ON_EXIT_STAGE"));
          alert.setHeaderText(LesxMessage.getMessage("TEXT-ALERT_HEADER_ON_EXIT_STAGE"));
          alert.initOwner(primaryStage);
          ButtonType result = alert.showAndWait()
              .orElse(null);
          if (result == ButtonType.CANCEL) {
            event.consume();
          }
        }
      }
    });
  }

}
