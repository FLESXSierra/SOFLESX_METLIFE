package lesx.ui.components;

import static lesx.property.properties.ELesxActions.ACTIONS_ADD;
import static lesx.property.properties.ELesxActions.ACTIONS_ADD_SELL;
import static lesx.property.properties.ELesxActions.ACTIONS_CHILDREN;
import static lesx.property.properties.ELesxActions.ACTIONS_DELETE;
import static lesx.property.properties.ELesxActions.ACTIONS_DESELECT;
import static lesx.property.properties.ELesxActions.ACTIONS_EDIT;
import static lesx.property.properties.ELesxActions.ACTIONS_EDIT_SELL;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Separator;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import lesx.gui.message.LesxMessage;
import lesx.icon.utils.LesxIcon;
import lesx.property.properties.ELesxActions;
import lesx.property.properties.ELesxUseCase;
import lesx.utils.LesxMisc;

public class LesxToolBar extends ToolBar {

  private Button deselect;
  private Button delete;
  private Button add;
  private Button edit;
  private Button sell;
  private Button editSell;
  private ToggleButton children;
  private ComboBox<Integer> year;

  private Map<ELesxActions, EventHandler<ActionEvent>> actions;
  private EventHandler<ActionEvent> deselectAction;
  private EventHandler<ActionEvent> editAction;
  private EventHandler<ActionEvent> deleteAction;
  private EventHandler<ActionEvent> addAction;
  private EventHandler<ActionEvent> childrenAction;
  private EventHandler<ActionEvent> sellAction;
  private EventHandler<ActionEvent> editSellAction;

  private enum buttonType {
    DESELECT,
    EDIT,
    DELETE,
    ADD,
    CHILDREN,
    SELL,
    EDIT_SELL
  };

  private BooleanProperty selectedItem = new SimpleBooleanProperty(this, "selectedItem", false);
  private BooleanProperty selectedFilterTable = new SimpleBooleanProperty(this, "selectedFilterTable", true);

  private List<? extends ButtonBase> buttons;

  public LesxToolBar(ELesxUseCase useCase) {
    actions = new HashMap<>();
    deselect = new Button();
    deselect.setGraphic(LesxIcon.getImage(LesxIcon.ERASE));
    deselect.setText(null);
    deselect.setTooltip(generateToolTip(buttonType.DESELECT));
    deselect.disableProperty()
        .bind(Bindings.not(selectedItem));
    buildButtons(useCase);
  }

  private Tooltip generateToolTip(buttonType type) {
    Tooltip tool = new Tooltip();
    StringBuilder text = new StringBuilder();
    switch (type) {
      case DESELECT:
        text.append(LesxMessage.getMessage("TEXT-DESELECT_BUTTON"));
        break;
      case ADD:
        text.append(LesxMessage.getMessage("TEXT-ADD_BUTTON"));
        break;
      case CHILDREN:
        text.append(LesxMessage.getMessage("TEXT-SELECT_CHILDREN_BUTTON"));
        text.append("\n");
        text.append(LesxMessage.getMessage("TEXT-TOOLTIP_SELECT_CHILDREN"));
        break;
      case DELETE:
        text.append(LesxMessage.getMessage("TEXT-DELETE_BUTTON"));
        break;
      case EDIT:
        text.append(LesxMessage.getMessage("TEXT-EDIT_BUTTON"));
        break;
      case SELL:
        text.append(LesxMessage.getMessage("TEXT-SELL_BUTTON"));
        break;
      case EDIT_SELL:
        text.append(LesxMessage.getMessage("TEXT-EDIT_SELL_BUTTON"));
        break;
      default:
        break;
    }
    tool.setText(text.toString());
    tool.setWrapText(true);
    tool.setMaxWidth(400);
    return tool;
  }

  private void addButtonsToToolBar() {
    if (!LesxMisc.isEmpty(buttons)) {
      for (Object button : buttons) {
        Separator sep = new Separator();
        sep.setOrientation(Orientation.VERTICAL);
        getItems().add((ButtonBase) button);
        getItems().add(sep);
      }
      if (year != null) {
        getItems().add(year);
      }
    }
  }

  /**
   * Gets the map of Actions with the respective handler that will run on a CLICKED event.
   *
   * @return a Map
   */
  public Map<ELesxActions, EventHandler<ActionEvent>> getActions() {
    return actions;
  }

  /**
   * Install the EventHandler contained in the getActions() Map, is important to notice that null events will not be
   * added for obvious reason (NPE), please be sure that event handlers are assigned and this method is called to expect
   * this buttons do something with your App.
   */
  public void installHandlers() {
    for (Entry<ELesxActions, EventHandler<ActionEvent>> entry : actions.entrySet()) {
      if (entry.getValue() == null) {
        continue;
      }
      removeAction(entry.getKey());
      switch (entry.getKey()) {
        case ACTIONS_ADD:
          add.setOnAction(entry.getValue());
          break;
        case ACTIONS_CHILDREN:
          children.setOnAction(entry.getValue());
          break;
        case ACTIONS_DELETE:
          delete.setOnAction(entry.getValue());
          break;
        case ACTIONS_DESELECT:
          deselect.setOnAction(entry.getValue());
          break;
        case ACTIONS_EDIT:
          edit.setOnAction(entry.getValue());
          break;
        case ACTIONS_ADD_SELL:
          sell.setOnAction(entry.getValue());
          break;
        case ACTIONS_EDIT_SELL:
          editSell.setOnAction(entry.getValue());
          break;
      }
    }
  }

  public BooleanProperty getSelectedItemProperty() {
    return selectedItem;
  }

  public void setUseCase(ELesxUseCase useCase) {
    buildButtons(useCase);
  }

  private void buildButtons(ELesxUseCase useCase) {
    getItems().clear();
    year = null;
    actions.clear();
    actions.put(ACTIONS_DESELECT, deselectAction);
    switch (useCase) {
      case UC_TREE:
        buildChildrenButton();
        buttons = Arrays.asList(deselect, children);
        actions.put(ACTIONS_CHILDREN, childrenAction);
        break;
      case UC_TREE_MODIFY:
      case UC_TABLE:
        buildDeleteButton();
        buildAddButton();
        buildEditButton();
        buttons = Arrays.asList(deselect, add, edit, delete);
        actions.put(ACTIONS_DELETE, deleteAction);
        actions.put(ACTIONS_ADD, addAction);
        actions.put(ACTIONS_EDIT, editAction);
        break;
      case UC_READ_ONLY:
        buttons = Arrays.asList(deselect);
        break;
      case UC_ADD_REMOVE_ONLY:
        buildDeleteButton();
        buildAddButton();
        buttons = Arrays.asList(deselect, add, delete);
        actions.put(ACTIONS_DELETE, deleteAction);
        actions.put(ACTIONS_ADD, addAction);
        break;
      case UC_DELETE_ONLY:
        buildDeleteButton();
        buttons = Arrays.asList(deselect, delete);
        actions.put(ACTIONS_DELETE, deleteAction);
        break;
      case UC_RESOURCES:
        buildDeleteButton();
        buildAddButton();
        buildEditButton();
        buttons = Arrays.asList(deselect, add, edit, delete);
        actions.put(ACTIONS_DELETE, deleteAction);
        actions.put(ACTIONS_ADD, addAction);
        actions.put(ACTIONS_EDIT, editAction);
        break;
      case UC_TREE_TABLE_INICIO:
        year = new ComboBox<>();
        year.setMaxWidth(100);
        year.getItems()
            .addAll(generateYears());
        year.setValue(LocalDate.now()
            .getYear());
        buildDeleteButton();
        buildSellButton();
        buildEditSellButton();
        buttons = Arrays.asList(deselect, sell, editSell, delete);
        actions.put(ACTIONS_DELETE, deleteAction);
        actions.put(ACTIONS_ADD_SELL, sellAction);
        actions.put(ACTIONS_EDIT_SELL, editSellAction);
        break;
      default:
        break;
    }
    addButtonsToToolBar();
  }

  private List<Integer> generateYears() {
    Integer yearNow = LocalDate.now()
        .getYear();
    List<Integer> years = new ArrayList<>();
    for (int year = (yearNow - 5); year <= (yearNow + 5); year++) {
      years.add(year);
    }
    return years;
  }

  public BooleanProperty selectedFilterTableProperty() {
    return selectedFilterTable;
  }

  /**
   * Removes the Event Handler from the given action
   *
   * @param action ELesxActions
   */
  public void removeAction(ELesxActions action) {
    switch (action) {
      case ACTIONS_ADD:
        add.setOnAction(null);
        break;
      case ACTIONS_CHILDREN:
        children.setOnAction(null);
        break;
      case ACTIONS_DELETE:
        delete.setOnAction(null);
        break;
      case ACTIONS_DESELECT:
        deselect.setOnAction(null);
        break;
      case ACTIONS_EDIT:
        edit.setOnAction(null);
        break;
      case ACTIONS_ADD_SELL:
        sell.setOnAction(null);
        break;
      case ACTIONS_EDIT_SELL:
        editSell.setOnAction(null);
        break;
    }
  }

  public ObjectProperty<Integer> yearProperty() {
    return year.valueProperty();
  }

  private void buildAddButton() {
    add = new Button();
    add.setText(null);
    add.setGraphic(LesxIcon.getImage(LesxIcon.ADD));
    add.setTooltip(generateToolTip(buttonType.ADD));
    add.disableProperty()
        .bind(Bindings.not(selectedFilterTable));
  }

  private void buildChildrenButton() {
    children = new ToggleButton();
    children.setText(null);
    children.setGraphic(LesxIcon.getImage(LesxIcon.CHILDREN));
    children.setTooltip(generateToolTip(buttonType.CHILDREN));
    children.setSelected(true);
  }

  private void buildDeleteButton() {
    delete = new Button();
    delete.setText(null);
    delete.setGraphic(LesxIcon.getImage(LesxIcon.DELETE));
    delete.setTooltip(generateToolTip(buttonType.DELETE));
    delete.disableProperty()
        .bind(Bindings.not(selectedItem));
  }

  private void buildEditButton() {
    edit = new Button();
    edit.setText(null);
    edit.setGraphic(LesxIcon.getImage(LesxIcon.EDIT));
    edit.setTooltip(generateToolTip(buttonType.EDIT));
    edit.disableProperty()
        .bind(Bindings.not(selectedItem));
  }

  private void buildSellButton() {
    sell = new Button();
    sell.setText(null);
    sell.setTooltip(generateToolTip(buttonType.SELL));
    sell.setGraphic(LesxIcon.getImage(LesxIcon.SELL));
  }

  private void buildEditSellButton() {
    editSell = new Button();
    editSell.setText(null);
    editSell.setTooltip(generateToolTip(buttonType.EDIT_SELL));
    editSell.setGraphic(LesxIcon.getImage(LesxIcon.EDIT_SELL));
    editSell.disableProperty()
        .bind(Bindings.not(selectedItem));
  }

}
