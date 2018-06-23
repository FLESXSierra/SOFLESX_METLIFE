package lesx.ui.components.dialogs;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Window;
import lesx.datamodel.ILesxDataModel;
import lesx.gui.message.LesxMessage;
import lesx.property.properties.LesxComponent;
import lesx.property.properties.LesxProperty;
import lesx.scene.controller.LesxController;
import lesx.ui.components.LesxPropertySheet;

public class LesxEditComponentDialog extends LesxController {

  @FXML
  private Button ok;
  @FXML
  private Button apply;
  @FXML
  private Button cancel;

  @FXML
  private Label descriptionLabel;
  @FXML
  private Label defaultName;

  @FXML
  private Pane propertiesPane;
  @FXML
  private Pane editorsPane;

  // PropertySheet
  private LesxPropertySheet propertySheet;
  //properties
  private BooleanProperty closeProperty = new SimpleBooleanProperty(this, "closeProperty", false);
  private BooleanProperty pendingChanges = new SimpleBooleanProperty(this, "pendingChanges");
  private BooleanProperty afterSaveProperty = new SimpleBooleanProperty(this, "afterSaveProperty");
  //Data
  private ILesxDataModel<? extends LesxComponent> dataModel;
  private LesxComponent component;
  private boolean canClose = true;
  private boolean isCreate;
  //Stage
  private Window window;

  @FXML
  public void initialize() {
    ok.setText(LesxMessage.getMessage("TEXT-BUTTON_OK"));
    ok.setOnAction(obs -> save());
    apply.setText(LesxMessage.getMessage("TEXT-BUTTON_APPLY"));
    apply.setOnAction(obs -> applyChanges());
    cancel.setText(LesxMessage.getMessage("TEXT-BUTTON_CANCEL"));
    cancel.setOnAction(obs -> closeDialog());
  }

  public void init(ILesxDataModel<? extends LesxComponent> dataModel, boolean isCreate) {
    setTitle(LesxMessage.getMessage("TEXT-TITLE_COMPONENT_DIALOG_CREATE"));
    setDataModel(dataModel);
    setComponent(isCreate);
    descriptionLabel.setText(getDescriptionText(isCreate));
    defaultName.setText(getHeader(isCreate));
    setIsCreate(isCreate);
    propertySheet = new LesxPropertySheet(getPropertiesPane(), getEditorsPane(), getComponent(),
        (Long newKey, LesxProperty property) -> dataModel.isUniqueProperty(property, newKey, this.isCreate));
    pendingChanges.bind(propertySheet.getPendingChangesProperty());
    installButtonsBinding();
    if (!isCreate) {
      Platform.runLater(() -> setPendingChanges(false));
    }
  }

  /**
   * Sets isCreate
   *
   * @param isCreate
   */
  protected void setIsCreate(boolean isCreate) {
    this.isCreate = isCreate;
  }

  private void applyChanges() {
    canClose = false;
    save();
    afterSaveProperty.set(!afterSaveProperty.get());
    canClose = true;
    reInitialize();
    getPropertySheet().setPendingChanges(false);
  }

  private void save() {
    if (pendingChanges.get()) {
      boolean isValid = getPropertySheet().isValid();
      boolean duplicateId = isDuplicate(isCreate);
      boolean showIssues = !isValid || duplicateId;
      if (showIssues) {
        StringBuilder issues = new StringBuilder(1024);
        StringBuilder header = new StringBuilder(1024);
        if (!isValid) {
          issues.append(LesxMessage.getMessage("TEXT-INVALID_PROPERTY_ISSUE"));
          issues.append("\n");
        }
        if (!duplicateId) {
          issues.append(LesxMessage.getMessage("TEXT-DUPLICATE_ID_PROPERTY_ISSUE", getComponentName().toString()));
          issues.append("\n");
        }
        header.append(LesxMessage.getMessage("TEXT-ALERT_DIALOG_HEADER_ERROR"));
        header.append("\n");
        header.append(LesxMessage.getMessage("TEXT-ALERT_DIALOG_CONTEXT_ERROR"));
        Alert alert = new Alert(AlertType.ERROR);
        alert.initOwner(getWindow());
        alert.setTitle(LesxMessage.getMessage("TEXT-ALERT_DIALOG_TITLE_ERROR"));
        alert.setHeaderText(header.toString());
        alert.setContentText(issues.toString());
        alert.getButtonTypes()
            .clear();
        alert.getButtonTypes()
            .addAll(ButtonType.OK);
        alert.showAndWait();
      }
      else {
        saveValues();
        if (canClose) {
          closeProperty.set(true);
        }
      }
    }
  }

  private void closeDialog() {
    if (pendingChanges.get()) {
      ButtonType save = new ButtonType(LesxMessage.getMessage("TEXT-SAVE_BUTTON"));
      ButtonType dontSave = new ButtonType(LesxMessage.getMessage("TEXT-NO_SAVE_BUTTON"));
      ButtonType cancel = new ButtonType(LesxMessage.getMessage("TEXT-CANCEL_BUTTON"));
      String name = getComponentName();
      Alert alert = new Alert(AlertType.WARNING);
      alert.initOwner(getWindow());
      alert.setTitle(LesxMessage.getMessage("TEXT-ALERT_DIALOG_TITLE_PENDING_SAVES"));
      alert.setHeaderText(LesxMessage.getMessage("TEXT-ALERT_DIALOG_HEADER_PENDING_SAVES"));
      alert.setContentText(LesxMessage.getMessage("TEXT-ALERT_DIALOG_CONTEXT_PENDING_SAVES", name));
      alert.getButtonTypes()
          .clear();
      alert.getButtonTypes()
          .addAll(save, dontSave, cancel);
      ButtonType result = alert.showAndWait()
          .orElse(null);
      if (save.equals(result)) {
        save();
      }
      else if (dontSave.equals(result)) {
        closeProperty.set(true);
      }
    }
    else {
      closeProperty.set(true);
    }
  }

  /**
   * Install the bindings of OK and Apply Buttons
   */
  protected void installButtonsBinding() {
    ok.disableProperty()
        .bind(Bindings.or(Bindings.not(getPropertySheet().validProperty()), Bindings.not(getPendingChanges())));
    apply.disableProperty()
        .bind(Bindings.or(Bindings.not(getPropertySheet().validProperty()), Bindings.not(getPendingChanges())));
  }

  /**
   * Gets the text of descriptionLabel like "Panel de creación de nuevo objeto ... "
   *
   * @param isCreate {@code true} if this dialog is use to create components
   * @return text
   */
  protected String getDescriptionText(boolean isCreate) {
    return isCreate ? LesxMessage.getMessage("TEXT-DESCRIPTION_LABEL_NEW_COMPONENT") : LesxMessage.getMessage("TEXT-DESCRIPTION_LABEL_EDIT_COMPONENT");
  }

  /**
   * Creates the header String
   *
   * @param isCreate boolean
   * @return a string
   */
  protected String getHeader(boolean isCreate) {
    StringBuilder string;
    string = new StringBuilder(128);
    if (isCreate) {
      string.append(LesxMessage.getMessage("TEXT-HEADER_LABEL_COMPONENT_PANE", "Nuevo"));
    }
    else {
      string.append(LesxMessage.getMessage("TEXT-HEADER_LABEL_COMPONENT_PANE", component.toString()));
    }
    string.append(".");
    return string.toString();
  }

  /**
   * Creates a new component or edit a component
   *
   * @param isCreate {@code true} if is required a new component, otherwise the data model will get the component
   */
  protected void setComponent(boolean isCreate) {
    component = isCreate ? new LesxComponent() : (LesxComponent) dataModel.getComponentSelected();
  }

  /**
   * Override this method in order to search for the right name.
   *
   * @return Component name.
   */
  protected String getComponentName() {
    return (component == null) ? "*No Name*" : component.toString();
  }

  /**
   * Override this method in order to save the values into the data model
   */
  protected void saveValues() {
    dataModel.persist(() -> {
      // Nothing
    });
  }

  /**
   * ReInitialize the dialog
   */
  protected void reInitialize() {
    isCreate = false;
    init(dataModel, isCreate);
  }

  /**
   * Sets Data Model
   *
   * @param newDataModel
   */
  protected void setDataModel(ILesxDataModel<? extends LesxComponent> newDataModel) {
    dataModel = newDataModel;
  }

  /**
   * Override this method in order to search for the right component duplicate
   *
   * @param isCreate
   *
   * @return search for duplicates.
   */
  protected boolean isDuplicate(boolean isCreate) {
    return false;
  }

  /**
   * @return the closeProperty
   */
  public BooleanProperty closeProperty() {
    return closeProperty;
  }

  /**
   * @return the afterSaveProperty
   */
  public BooleanProperty afterSaveProperty() {
    return afterSaveProperty;
  }

  /**
   * @return the propertySheet
   */
  protected LesxPropertySheet getPropertySheet() {
    return propertySheet;
  }

  /**
   * @return the pendingChanges
   */
  protected BooleanProperty getPendingChanges() {
    return pendingChanges;
  }

  /**
   * @return the descriptionLabel
   */
  protected Label getDescriptionLabel() {
    return descriptionLabel;
  }

  /**
   * @return the defaultName
   */
  protected Label getDefaultName() {
    return defaultName;
  }

  /**
   * @return the propertiesPane
   */
  protected Pane getPropertiesPane() {
    return propertiesPane;
  }

  /**
   * @return the editorsPane
   */
  protected Pane getEditorsPane() {
    return editorsPane;
  }

  /**
   * @return the component
   */
  protected LesxComponent getComponent() {
    return component;
  }

  protected void setPendingChanges(boolean pending) {
    getPropertySheet().setPendingChanges(false);
  }

  /**
   * @return the Window
   */
  private Window getWindow() {
    return window;
  }

  /**
   * @param window sets the window
   */
  public void setWindow(Window window) {
    this.window = window;
  }

  @Override
  protected boolean showAlert() {
    return false;
  }

}
