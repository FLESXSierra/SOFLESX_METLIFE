package lesx.ui.components.dialogs;

import java.io.File;
import java.util.List;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import lesx.gui.message.LesxMessage;
import lesx.scene.controller.LesxController;
import lesx.ui.soflesx.LesxMain;
import lesx.utils.LesxAlertBuilder;
import lesx.utils.LesxMisc;
import lesx.xml.thread.LesxXMLImportData;

public class LesxImportXMLController extends LesxController {

  @FXML
  Label header;
  @FXML
  Label loadText;
  @FXML
  ProgressIndicator progress;

  //Stage
  private Window window;

  public LesxImportXMLController() {
    setTitle(LesxMessage.getMessage("TEXT-TITLE_IMPORT_XML"));
  }

  @FXML
  public void initialize() {
    header.setText(LesxMessage.getMessage("TEXT-HEADER_IMPORTING_XML"));
    loadText.setText(LesxMessage.getMessage("TEXT-LOADING_WAITING"));
    final FileChooser fileChooser = new FileChooser();
    Platform.runLater(() -> {
      FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.xml)", "*.xml");
      fileChooser.setTitle("Open files");
      fileChooser.setSelectedExtensionFilter(extFilter);
      final List<File> files = fileChooser.showOpenMultipleDialog(window);
      if (!LesxMisc.isEmpty(files)) {
        LesxXMLImportData importTask = new LesxXMLImportData(files);
        loadText.textProperty()
            .bind(importTask.messageProperty());
        importTask.setOnFailed(obs -> result(false));
        importTask.setOnSucceeded(obs -> result(importTask.getValue()));
        Thread tested = new Thread(importTask);
        tested.setDaemon(true);
        tested.start();
      }
      else {
        closeWindow();
      }
    });
  }

  private void result(boolean result) {
    String title = result ? LesxMessage.getMessage("TITLE-SUCCESS_IMPORT_ALERT") : LesxMessage.getMessage("TITLE-FAILED_IMPORT_ALERT");
    String header = result ? LesxMessage.getMessage("TEXT-HEADER_IMPORT_SUCCESS_ALERT") : LesxMessage.getMessage("TEXT-HEADER_IMPORT_FAILED_ALERT");
    LesxAlertBuilder.create()
        .setTitle(title)
        .setContentText(header)
        .setOwner(window)
        .setButtons(ButtonType.OK)
        .showAndWait();
    if (result) {
      LesxMain.getInstance()
          .getDbProperty()
          .refresh();
    }
    closeWindow();
  }

  /**
   * @param window sets the window
   */
  public void setWindow(Window window) {
    this.window = window;
    setExitOperation((Stage) window);
  }

}
