package lesx.scene.controller;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class LesxController {
  private String title;
  private Stage stage;
  private boolean consumeEvent;
  private boolean forceClose;
  private BooleanProperty showProgress = new SimpleBooleanProperty(this, "showProgress", false);
  private BooleanProperty pendingChanges = new SimpleBooleanProperty(this, "pendingChanges", false);

  public LesxController() {
    //Nothing
  }

  public LesxController(Stage stage) {
    setExitOperation(stage);
  }

  public BooleanProperty showProgressProperty() {
    return showProgress;
  }

  public BooleanProperty pendingChangesProperty() {
    return pendingChanges;
  }

  protected void init() {
    //Nothing
  }

  public String getTitle() {
    return title;
  }

  protected void setTitle(String title) {
    this.title = title;
  }

  /**
   * Method called on Exit Operation
   */
  protected void onCloseWindow() {
    //Nothing
  }

  /**
   * Force the window to be close
   */
  public void closeWindow() {
    forceClose = true;
    stage.close();
  }

  /**
   * Consumes the close event from stage and close the window manually by using {@link LesxController#closeWindow()}
   *
   * @return showAlert
   */
  protected boolean consumeEvent() {
    return consumeEvent;
  }

  /**
   * Overrides -- Clears the component
   */
  public void clearComponent() {
    // Nothing
  }

  /**
   * Sets Exit handler operation
   */
  public void setExitOperation(Stage primaryStage) {
    stage = primaryStage;
    stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
      @Override
      public void handle(WindowEvent event) {
        if (!forceClose) {
          if (consumeEvent()) {
            event.consume();
          }
          onCloseWindow();
        }
      }
    });
  }

}
