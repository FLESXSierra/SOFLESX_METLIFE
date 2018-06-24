package lesx.utils;

import java.util.Optional;

import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.StageStyle;
import javafx.stage.Window;

/**
 * Creates an Alert, please be sure to call first the {@link #create()} method first.
 *
 * @author lesx
 */
public class LesxAlertBuilder {

  private Alert alert = new Alert(AlertType.NONE);

  /**
   * Creates a new Instance of the Alert
   *
   * @return LesxAlertBuilder
   */
  public static LesxAlertBuilder create() {
    return new LesxAlertBuilder();
  }

  /**
   * @param type sets the Alert Type of LesxAlertBuilder
   */
  public LesxAlertBuilder setType(AlertType type) {
    alert.setAlertType(type);
    return this;
  }

  /**
   * @param window sets the window
   */
  public LesxAlertBuilder setOwner(Window window) {
    alert.initOwner(window);
    return this;
  }

  /**
   * @param title sets the title
   */
  public LesxAlertBuilder setTitle(String title) {
    alert.setTitle(title);
    return this;
  }

  /**
   * @param header sets the header text
   */
  public LesxAlertBuilder setHeaderText(String header) {
    alert.setHeaderText(header);
    return this;
  }

  /**
   * @param content sets the content text
   */
  public LesxAlertBuilder setContentText(String content) {
    alert.setContentText(content);
    return this;
  }

  /**
   * @param buttons sets the buttons
   */
  public LesxAlertBuilder setButtons(ButtonType... buttons) {
    alert.getButtonTypes()
        .clear();
    alert.getButtonTypes()
        .addAll(buttons);
    return this;
  }

  /**
   * @param style sets the style
   */
  public LesxAlertBuilder setStyle(StageStyle style) {
    alert.initStyle(style);
    return this;
  }

  /**
   * @param graphic sets the Graphic
   */
  public LesxAlertBuilder setGraphic(Node graphic) {
    alert.setGraphic(graphic);
    return this;
  }

  public void show() {
    alert.show();
  }

  public Optional<ButtonType> showAndWait() {
    return alert.showAndWait();
  }

  public Alert getAlert() {
    return alert;
  }

}
