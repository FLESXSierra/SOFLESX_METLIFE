package lesx.ui.components;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import lesx.gui.message.LesxMessage;
import lesx.property.properties.ELesxActions;
import lesx.property.properties.ELesxUseCase;
import lesx.property.properties.LesxResourceBusiness;
import lesx.utils.LesxAlertBuilder;

public class LesxTreeTableViewPane<T> extends VBox {

  private LesxToolBar toolBar;
  private TreeTableView<T> table;
  private BooleanProperty selectedItemTable = new SimpleBooleanProperty(this, "selectedItemTable");
  private Consumer<ELesxUseCase> addNewItem;
  private Runnable onDeleteItem;
  private ELesxUseCase useCase;

  public LesxTreeTableViewPane() {
    initialize();
    toolBar = new LesxToolBar(useCase);
    toolBar.setPrefHeight(40);
    toolBar.setMinHeight(Region.USE_PREF_SIZE);
    table = new TreeTableView<>();
    table.setColumnResizePolicy(TreeTableView.CONSTRAINED_RESIZE_POLICY);
    table.getSelectionModel()
        .selectedItemProperty()
        .addListener(obs -> selectedItemTable());
    installToolBarActions();
    getChildren().addAll(toolBar, table);
    VBox.setVgrow(table, Priority.ALWAYS);
  }

  private void initialize() {
    useCase = ELesxUseCase.UC_TREE_TABLE_INICIO;
    addNewItem = (isCreate) -> {
      //Nothing
    };
    onDeleteItem = () -> {
      //Nothing
    };
  }

  private void installToolBarActions() {
    Map<ELesxActions, EventHandler<ActionEvent>> mapActions;
    mapActions = toolBar.getActions();
    List<ELesxActions> actions = new ArrayList<>();
    actions.addAll(mapActions.keySet());
    for (ELesxActions action : actions) {
      if (mapActions.get(action) != null) {
        toolBar.removeAction(action);
      }
      mapActions.remove(action);
      EventHandler<ActionEvent> actionHandler;
      switch (action) {
        case ACTIONS_ADD_SELL:
        case ACTIONS_ADD:
          actionHandler = s -> addNewItem.accept(ELesxUseCase.ADD);
          break;
        case ACTIONS_DELETE:
          actionHandler = s -> deleteItem();
          break;
        case ACTIONS_DESELECT:
          actionHandler = s -> table.getSelectionModel()
              .clearSelection();
          break;
        case ACTIONS_EDIT_SELL:
        case ACTIONS_EDIT:
          actionHandler = s -> addNewItem.accept(ELesxUseCase.EDIT);
          break;
        default:
          actionHandler = null;
          break;
      }
      mapActions.put(action, actionHandler);
    }
    toolBar.installHandlers();
    toolBar.getSelectedItemProperty()
        .bind(selectedItemTable);
  }

  private void deleteItem() {
    if (selectedItemTable.get()) {
      List<TreeItem<T>> temp = new ArrayList<>(table.getSelectionModel()
          .getSelectedItems());
      if (temp != null && !temp.isEmpty()) {
        Alert alert = LesxAlertBuilder.create()
            .setType(AlertType.CONFIRMATION)
            .setTitle(LesxMessage.getMessage("TEXT-ALERT_CONFIRMATION_TITLE"))
            .getAlert();
        if (useCase == ELesxUseCase.UC_ADD_REMOVE_ONLY || useCase == ELesxUseCase.UC_DELETE_ONLY || temp.size() > 1) {
          alert.setHeaderText(LesxMessage.getMessage("TEXT-ALERT_CONFIRMATION_DELETE_OBJECTS_HEADER", table.getSelectionModel()
              .getSelectedItems()
              .size()));
        }
        else {
          alert.setHeaderText(LesxMessage.getMessage("TEXT-ALERT_CONFIRMATION_DELETE_HEADER", temp.get(0)
              .getValue()
              .toString()));
        }
        ButtonType result = alert.showAndWait()
            .orElse(null);
        if (ButtonType.OK.equals(result)) {
          onDeleteItem.run();
          for (TreeItem<T> item : temp) {
            if (item.getParent() != null) {
              item.getParent()
                  .getChildren()
                  .remove(item);
            }
          }
          table.getSelectionModel()
              .clearSelection();
        }
      }
    }
  }

  public LesxToolBar getToolBar() {
    return toolBar;
  }

  public TreeTableView<T> getTable() {
    return table;
  }

  private void selectedItemTable() {
    selectedItemTable.set(table.getSelectionModel()
        .getSelectedItem() != null
        && table.getSelectionModel()
            .getSelectedItem()
            .getParent() != null
        && table.getSelectionModel()
            .getSelectedItem()
            .getParent()
            .getValue() != null);
  }

  public void setOnAddNewItem(Consumer<ELesxUseCase> consumer) {
    addNewItem = consumer;
    installToolBarActions();
  }

  public void setUseCase(ELesxUseCase useCase) {
    this.useCase = useCase;
    toolBar.setUseCase(useCase);
  }

  public void setOnDelete(Runnable runnable) {
    onDeleteItem = runnable;
    installToolBarActions();
  }

  public ObjectProperty<Integer> yearProperty() {
    return toolBar.yearProperty();
  }

  public boolean isSelectedResourceBusinessItem() {
    return selectedItemTable.get();
  }

  public void selectItem(LesxResourceBusiness item) {
    TreeItem<T> tree = searchForItem(table.getRoot(), item);
    if (tree != null) {
      tree.getParent()
          .setExpanded(true);
    }
    table.getSelectionModel()
        .select(tree);
  }

  private TreeItem<T> searchForItem(TreeItem<T> treeItem, LesxResourceBusiness item) {
    if (treeItem == null || (treeItem.getValue() != null && treeItem.getValue()
        .equals(item))) {
      return treeItem;
    }
    TreeItem<T> result = null;
    for (TreeItem<T> value : treeItem.getChildren()) {
      result = searchForItem(value, item);
      if (result != null && result.getValue() != null) {
        break;
      }
    }
    return result;
  }

}
