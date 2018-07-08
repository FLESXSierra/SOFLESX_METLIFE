package lesx.ui.components.dialogs;

import static lesx.property.properties.ELesxUseCase.ADD;
import static lesx.property.properties.ELesxUseCase.ADD_ONLY;
import static lesx.property.properties.ELesxUseCase.EDIT;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Window;
import lesx.datamodel.ILesxDataModel;
import lesx.gui.message.LesxMessage;
import lesx.property.properties.ELesxUseCase;
import lesx.property.properties.LesxComponent;
import lesx.property.properties.LesxProperty;
import lesx.scene.controller.LesxController;
import lesx.ui.components.LesxPropertySheet;
import lesx.utils.LesxAlertBuilder;
import lesx.utils.LesxButtonType;

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
  private ELesxUseCase useCase;
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

  public void init(ILesxDataModel<? extends LesxComponent> dataModel, ELesxUseCase useCase) {
    setTitle(LesxMessage.getMessage("TEXT-TITLE_COMPONENT_DIALOG_CREATE"));
    setDataModel(dataModel);
    setComponent(useCase);
    descriptionLabel.setText(getDescriptionText(useCase));
    defaultName.setText(getHeader(useCase));
    setIsCreate(useCase);
    propertySheet = new LesxPropertySheet(getPropertiesPane(), getEditorsPane(), getComponent(),
        (Long newKey, LesxProperty property) -> dataModel.isUniqueProperty(property, newKey, this.useCase));
    pendingChanges.bind(propertySheet.getPendingChangesProperty());
    installButtonsBinding(useCase);
    if (useCase == EDIT) {
      Platform.runLater(() -> setPendingChanges(false));
    }
  }

  /**
   * Sets isCreate
   *
   * @param useCase ELesxUseCase
   */
  protected void setIsCreate(ELesxUseCase useCase) {
    this.useCase = useCase;
  }

  private void applyChanges() {
    canClose = false;
    save();
    afterSaveProperty.set(!afterSaveProperty.get());
    canClose = true;
    reInitialize();
    Platform.runLater(() -> setPendingChanges(false));
  }

  private void save() {
    if (pendingChanges.get()) {
      boolean isValid = getPropertySheet().isValid();
      boolean duplicateId = isDuplicate(useCase);
      boolean showIssues = !isValid || duplicateId;
      if (showIssues) {
        StringBuilder issues = new StringBuilder(1024);
        StringBuilder header = new StringBuilder(1024);
        if (!isValid) {
          issues.append(LesxMessage.getMessage("TEXT-INVALID_PROPERTY_ISSUE"));
          issues.append("\n");
        }
        if (duplicateId) {
          issues.append(LesxMessage.getMessage("TEXT-DUPLICATE_ID_PROPERTY_ISSUE", getComponentName().toString()));
          issues.append("\n");
        }
        header.append(LesxMessage.getMessage("TEXT-ALERT_DIALOG_HEADER_ERROR"));
        header.append("\n");
        header.append(LesxMessage.getMessage("TEXT-ALERT_DIALOG_CONTEXT_ERROR"));
        LesxAlertBuilder.create()
            .setOwner(getWindow())
            .setType(AlertType.ERROR)
            .setTitle(LesxMessage.getMessage("TEXT-ALERT_DIALOG_TITLE_ERROR"))
            .setHeaderText(header.toString())
            .setContentText(issues.toString())
            .setButtons(ButtonType.OK)
            .showAndWait();
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
      String name = getComponentName();
      ButtonType result = LesxAlertBuilder.create()
          .setOwner(getWindow())
          .setType(AlertType.WARNING)
          .setTitle(LesxMessage.getMessage("TEXT-ALERT_DIALOG_TITLE_PENDING_SAVES"))
          .setHeaderText(LesxMessage.getMessage("TEXT-ALERT_DIALOG_HEADER_PENDING_SAVES"))
          .setContentText(LesxMessage.getMessage("TEXT-ALERT_DIALOG_CONTEXT_PENDING_SAVES", name))
          .setButtons(LesxButtonType.SAVE, LesxButtonType.DONT_SAVE, LesxButtonType.CANCEL)
          .showAndWait()
          .orElse(null);
      if (LesxButtonType.SAVE.equals(result)) {
        save();
      }
      else if (LesxButtonType.DONT_SAVE.equals(result)) {
        closeProperty.set(true);
      }
    }
    else {
      closeProperty.set(true);
    }
  }

  /**
   * Install the bindings of OK and Apply Buttons
   *
   * @param useCase ELesxUseCase
   */
  protected void installButtonsBinding(ELesxUseCase useCase) {
    ok.disableProperty()
        .bind(Bindings.or(Bindings.not(getPropertySheet().validProperty()), Bindings.not(getPendingChanges())));
    if (useCase == ADD_ONLY) {
      apply.setDisable(true);
    }
    else {
      apply.disableProperty()
          .bind(Bindings.or(Bindings.not(getPropertySheet().validProperty()), Bindings.not(getPendingChanges())));
    }
  }

  /**
   * Gets the text of descriptionLabel like "Panel de creación de nuevo objeto ... "
   *
   * @param useCase {@code true} if this dialog is use to create components
   * @return text
   */
  protected String getDescriptionText(ELesxUseCase useCase) {
    return useCase == EDIT ? LesxMessage.getMessage("TEXT-DESCRIPTION_LABEL_EDIT_COMPONENT") : LesxMessage.getMessage("TEXT-DESCRIPTION_LABEL_NEW_COMPONENT");
  }

  /**
   * Creates the header String
   *
   * @param useCase boolean
   * @return a string
   */
  protected String getHeader(ELesxUseCase useCase) {
    StringBuilder string;
    string = new StringBuilder(128);
    if (useCase == EDIT) {
      string.append(LesxMessage.getMessage("TEXT-HEADER_LABEL_COMPONENT_PANE", component.toString()));
    }
    else {
      string.append(LesxMessage.getMessage("TEXT-HEADER_LABEL_COMPONENT_PANE", "Nuevo"));
    }
    string.append(".");
    return string.toString();
  }

  /**
   * Creates a new component or edit a component
   *
   * @param useCase {@code true} if is required a new component, otherwise the data model will get the component
   */
  protected void setComponent(ELesxUseCase useCase) {
    component = useCase == ADD ? new LesxComponent() : (LesxComponent) dataModel.getComponentSelected();
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
    useCase = EDIT;
    init(dataModel, useCase);
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
   * @param useCase
   *
   * @return search for duplicates.
   */
  protected boolean isDuplicate(ELesxUseCase useCase) {
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
