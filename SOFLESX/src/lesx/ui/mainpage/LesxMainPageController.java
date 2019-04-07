package lesx.ui.mainpage;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Accordion;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import lesx.datamodel.LesxBusinessResourceDataModel;
import lesx.datamodel.LesxResourcesDataModel;
import lesx.gui.message.LesxMessage;
import lesx.icon.utils.LesxIcon;
import lesx.property.properties.ELesxListenerType;
import lesx.property.properties.ELesxUseCase;
import lesx.property.properties.LesxResourceBusiness;
import lesx.scene.controller.LesxController;
import lesx.scene.controller.LesxSceneController;
import lesx.ui.components.birthdayButton.LesxBirthdayButton;
import lesx.ui.soflesx.LesxMain;
import lesx.utils.LesxAlertBuilder;
import lesx.utils.LesxButtonType;
import lesx.utils.LesxString;

public class LesxMainPageController extends LesxController {

  @FXML
  Accordion accordion;
  @FXML
  TitledPane fileMenu;
  @FXML
  TitledPane bussiness;
  @FXML
  TitledPane comisions;
  @FXML
  TitledPane help;
  @FXML
  ImageView image;
  @FXML
  Button importXML;
  @FXML
  Button exportXML;
  @FXML
  Button saveFlesx;
  @FXML
  Button main;
  @FXML
  Button newSell;
  @FXML
  Button resourcesItem;
  @FXML
  Button year;
  @FXML
  Button capp;
  @FXML
  Button cMonth;
  @FXML
  Button cYear2;
  @FXML
  Button reportBug;
  @FXML
  Button walkThrough;
  @FXML
  LesxBirthdayButton bButton;
  @FXML
  AnchorPane mainPane;
  @FXML
  HBox progressBox;
  @FXML
  Label loading;

  private final static Logger LOGGER = Logger.getLogger(LesxMainPageController.class.getName());

  private BooleanProperty showProgress = new SimpleBooleanProperty(this, "showProgress", false);

  public LesxMainPageController() {
    //Nothing
  }

  @FXML
  public void initialize() {
    //Sets all texts
    LesxSwitcherPane.setMainController(this);
    setTitle(LesxMessage.getMessage("TEXT-TITLE_MAINPAGE"));
    loading.setText(LesxMessage.getMessage("TEXT-LOADING_TITLE"));
    importXML.setText(LesxMessage.getMessage("TEXT-MENUITEM_IMPORT_SAVE"));
    exportXML.setText(LesxMessage.getMessage("TEXT-MENUITEM_EXPORT_SAVE"));
    saveFlesx.setText(LesxMessage.getMessage("TEXT-MENUITEM_FILE_SAVE"));
    fileMenu.setText(LesxMessage.getMessage("TEXT-MENUBAR_FILE"));
    resourcesItem.setText(LesxMessage.getMessage("TEXT-MENUITEM_EDIT_RESOURCES"));
    main.setText(LesxMessage.getMessage("TEXT-MENUITEM_MAIN"));
    newSell.setText(LesxMessage.getMessage("TEXT-MENUITEM_NEW_SELL"));
    year.setText(LesxMessage.getMessage("TEXT-MENUITEM_YEAR"));
    bussiness.setText(LesxMessage.getMessage("TEXT-MENUBAR_BUSSINESS"));
    capp.setText(LesxMessage.getMessage("TEXT-MENUITEM_CAPP"));
    cMonth.setText(LesxMessage.getMessage("TEXT-MENUITEM_MONTH"));
    cYear2.setText(LesxMessage.getMessage("TEXT-MENUITEM_YEAR_2"));
    comisions.setText(LesxMessage.getMessage("TEXT-MENUBAR_COMISIONS"));
    help.setText(LesxMessage.getMessage("TEXT-MENUBAR_HELP"));
    reportBug.setText(LesxMessage.getMessage("TEXT-MENUITEM_REPORT_BUG"));
    walkThrough.setText(LesxMessage.getMessage("TEXT-MENUITEM_WALK_THROUGH"));
    image.setImage(LesxIcon.getImage(LesxIcon.METLIFE)
        .getImage());
    accordion.setExpandedPane(bussiness);
  }

  @Override
  public void init() {
    LesxSwitcherPane.loadPane(LesxSwitcherPane.MAIN);
    setUpMenuButtons();
    progressBox.visibleProperty()
        .bind(showProgress);
  }

  /**
   * Adds Listeners and and set to read only if needed.
   */
  private void setUpMenuButtons() {
    cYear2.setDisable(true);
    walkThrough.setDisable(true);
    importXML.setOnAction(obs -> importXML());
    exportXML.setOnAction(obs -> exportXML());
    main.setOnAction(obs -> LesxSwitcherPane.loadPane(LesxSwitcherPane.MAIN));
    resourcesItem.setOnAction(obs -> LesxSwitcherPane.loadPane(LesxSwitcherPane.CLIENTES));
    newSell.setOnAction(obs -> addNewSell());
    saveFlesx.setOnAction(obs -> saveXMLAction());
    year.setOnAction(obs -> showYearDialog());
    reportBug.setOnAction(obs -> showHelpDialog());
    cMonth.setOnAction(obs -> LesxSwitcherPane.loadPane(LesxSwitcherPane.COMISION_MES));
    capp.setOnAction(obs -> LesxSwitcherPane.loadPane(LesxSwitcherPane.CAPP));
    LesxMain.getInstance()
        .getDbProperty()
        .setListener(ELesxListenerType.UPDATE, () -> updateBirthDayButtonNames());
    updateBirthDayButtonNames();
  }

  private void showHelpDialog() {
    StringBuilder content = new StringBuilder();
    content.append(LesxMessage.getMessage("TEXT-VERSION"));
    content.append("\n");
    content.append(LesxMessage.getMessage("TEXT-ALERT_HEADER_REPORT_BUG"));
    LesxAlertBuilder.create()
        .setOwner(mainPane.getScene()
            .getWindow())
        .setTitle(LesxMessage.getMessage("TEXT-ALERT_TITLE_REPORT_BUG"))
        .setContentText(content.toString())
        .setGraphic(LesxIcon.getImage(LesxIcon.HELP))
        .setButtons(ButtonType.OK)
        .showAndWait();
  }

  private void showYearDialog() {
    LesxSceneController.showYearDialog(this, new LesxBusinessResourceDataModel(LesxMain.getInstance()
        .getDbProperty()
        .getBusinessResourceMap()));
  }

  private void updateBirthDayButtonNames() {
    bButton.setNames(LesxMain.getInstance()
        .getDbProperty()
        .getBirthdayNames());
  }

  private void addNewSell() {
    LesxResourcesDataModel dataModelResource = new LesxResourcesDataModel();
    LesxBusinessResourceDataModel dataModel = new LesxBusinessResourceDataModel();
    //Load Data Base
    dataModelResource.setMap(LesxMain.getInstance()
        .getDbProperty()
        .getResourceMap());
    dataModel.setMap(LesxMain.getInstance()
        .getDbProperty()
        .getBusinessResourceMap());
    ButtonType result = LesxAlertBuilder.create()
        .setType(AlertType.CONFIRMATION)
        .setTitle(LesxMessage.getMessage("TEXT-ALERT_TITLE_NEW_SALE"))
        .setHeaderText(LesxMessage.getMessage("TEXT-ALERT_HEADER_NEW_SALE"))
        .setOwner(LesxMain.getInstance()
            .getStage())
        .setGraphic(LesxIcon.getImage(LesxIcon.MONEY))
        .setButtons(LesxButtonType.NEW_RESOURCE, LesxButtonType.NO_NEW_RESOURCE, LesxButtonType.CANCEL)
        .showAndWait()
        .orElse(null);
    if (LesxButtonType.NEW_RESOURCE.equals(result)) {
      LesxSceneController.showResourceEditDialog(this, ELesxUseCase.ADD_ONLY, dataModelResource, () -> {
        if (dataModelResource.getComponentSelected() != null) {
          dataModel.setComponentSelected(LesxResourceBusiness.of(dataModelResource.getComponentSelected(), null));
          LesxSceneController.showBusinessEditDialog(this, ELesxUseCase.ADD, dataModel, () -> {
            //Triggers Listener to update
          });
        }
      });
    }
    else if (LesxButtonType.NO_NEW_RESOURCE.equals(result)) {
      LesxSceneController.showSelectResourceDialog(this, dataModelResource, () -> {
        if (dataModelResource.getComponentSelected() != null) {
          dataModel.setComponentSelected(LesxResourceBusiness.of(dataModelResource.getComponentSelected(), null));
          LesxSceneController.showBusinessEditDialog(this, ELesxUseCase.ADD, dataModel, () -> {
            //Triggers Listener to update
          });
        }
      });
    }
  }

  /**
   * Exports the XML stored in the SOFLESX
   */
  private void exportXML() {
    final FileChooser fileChooser = new FileChooser();
    FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.xml)", "*.xml");
    fileChooser.setTitle("Save file");
    fileChooser.setSelectedExtensionFilter(extFilter);
    fileChooser.setInitialFileName(LesxString.XML_NAME_RESOURCE);
    final File dest = fileChooser.showSaveDialog(LesxMain.getInstance()
        .getStage());
    if (dest != null) {
      try {
        String path = dest.getPath()
            .replaceAll(dest.getName(), LesxString.XML_NAME_RESOURCE);
        File destination = new File(path);
        final File costumer = new File(LesxString.XML_RESOURCE_PATH);
        Files.copy(costumer.toPath(), destination.toPath());

        path = dest.getPath()
            .replaceAll(dest.getName(), LesxString.XML_NAME_BUSINESS);
        destination = new File(path);
        final File business = new File(LesxString.XML_BUSINESS_PATH);
        Files.copy(business.toPath(), destination.toPath());
      }
      catch (FileAlreadyExistsException e) {
        LOGGER.log(Level.WARNING, LesxMessage.getMessage("WARNING-FILES_ALREADY_EXIST"), e);
        LesxAlertBuilder.create()
            .setType(AlertType.WARNING)
            .setHeaderText(LesxMessage.getMessage("WARNING-FILES_ALREADY_EXIST"))
            .show();
      }
      catch (IOException ex) {
        LOGGER.log(Level.SEVERE, LesxMessage.getMessage("ERROR-EXPORT_XMLS"), ex);
        ex.printStackTrace();
      }
    }
  }

  /**
   * Import XML Files
   */
  private void importXML() {
    LesxSceneController.showImportDialog(this);
  }

  public void setMainPane(Node node) {
    AnchorPane.setBottomAnchor(node, 0.0);
    AnchorPane.setLeftAnchor(node, 0.0);
    AnchorPane.setRightAnchor(node, 0.0);
    AnchorPane.setTopAnchor(node, 0.0);
    mainPane.getChildren()
        .setAll(node);
  }

  private void saveXMLAction() {
    if (pendingChangesProperty().get()) {
      save(() -> {
        pendingChangesProperty().set(false);
        progressBox.setVisible(false);
        progressBox.visibleProperty()
            .bind(showProgress);
      });
    }
  }

  private void save(Runnable postSave) {
    progressBox.visibleProperty()
        .unbind();
    progressBox.setVisible(true);
    LesxMain.getInstance()
        .getDbProperty()
        .persist(() -> postSave.run());
  }

  @Override
  protected boolean consumeEvent() {
    return true;
  }

  @Override
  protected void onCloseWindow() {
    //    if (pendingChangesProperty().get()) {
    // Temporally changes on IOs is not saving
    save(() -> {
      LesxMain.getInstance()
          .getDbProperty()
          .clear();
      closeWindow();
      Platform.runLater(() -> Platform.runLater(() -> Platform.runLater(() -> Platform.runLater(() -> {
        System.exit(0);
      }))));
    });
    //  }
    //    else {
    //      closeWindow();
    //      Platform.runLater(() -> Platform.runLater(() -> Platform.runLater(() -> {
    //        System.exit(0);
    //      })));
    //    }
  }

  @Override
  public BooleanProperty showProgressProperty() {
    return showProgress;
  }

}
