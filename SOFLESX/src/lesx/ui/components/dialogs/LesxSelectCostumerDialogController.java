package lesx.ui.components.dialogs;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.util.Callback;
import lesx.datamodel.LesxResourcesDataModel;
import lesx.gui.message.LesxMessage;
import lesx.property.properties.ELesxUseCase;
import lesx.property.properties.LesxResource;
import lesx.scene.controller.LesxController;
import lesx.ui.components.LesxTableViewPane;

public class LesxSelectCostumerDialogController extends LesxController {

  @FXML
  Label header;
  @FXML
  LesxTableViewPane<LesxResource> costumerTable;
  @FXML
  Button add;
  @FXML
  Button cancel;

  // Table
  private TableView<LesxResource> tableCostumer;
  //Columns
  private TableColumn<LesxResource, String> nameColumn;
  private TableColumn<LesxResource, String> cc;
  // Data Models
  private LesxResourcesDataModel dataModelResources;
  //Data
  private BooleanProperty closeProperty = new SimpleBooleanProperty(this, "closeProperty");
  private BooleanProperty selectedProperty = new SimpleBooleanProperty(this, "selectedProperty");
  private List<Long> selectedCostumers = new ArrayList<>();
  private SelectionMode mode = SelectionMode.SINGLE;

  @FXML
  public void initialize() {
    setTitle(LesxMessage.getMessage("TEXT-TITLE_SELECT_COSTUMER_DIALOG"));
    costumerTable.setUseCase(ELesxUseCase.UC_READ_ONLY);
    tableCostumer = costumerTable.getTable();
    tableCostumer.getSelectionModel()
        .selectedItemProperty()
        .addListener(obs -> selectedCostumers());
    add.setText(LesxMessage.getMessage("TEXT-ADD_BUTTON"));
    cancel.setText(LesxMessage.getMessage("TEXT-BUTTON_CANCEL"));
  }

  private void selectedCostumers() {
    selectedCostumers.clear();
    if (tableCostumer.getSelectionModel()
        .getSelectedItems() != null) {
      selectedProperty.setValue(true);
      selectedCostumers.addAll(tableCostumer.getSelectionModel()
          .getSelectedItems()
          .stream()
          .filter(costumer -> costumer != null)
          .map(costumer -> costumer.getId())
          .collect(Collectors.toList()));
    }
    else {
      selectedProperty.setValue(false);
    }
  }

  public void init(LesxResourcesDataModel dataModelResources) {
    init(dataModelResources, null);
  }

  public void init(LesxResourcesDataModel dataModelResources, SelectionMode mode) {
    if (mode != null) {
      this.mode = mode;
    }
    this.dataModelResources = dataModelResources;
    cancel.setOnAction(obs -> close());
    add.setOnAction(obs -> saveCostumers());
    add.disableProperty()
        .bind(Bindings.not(selectedProperty));
    tableCostumer.getSelectionModel()
        .setSelectionMode(this.mode);
    fillDataCostumerTable();
  }

  @SuppressWarnings("unchecked")
  private void fillDataCostumerTable() {
    //Config. Columns.
    nameColumn = new TableColumn<>(LesxMessage.getMessage("TEXT-COLUMN_NAME_NAME"));
    nameColumn.setCellValueFactory(new Callback<CellDataFeatures<LesxResource, String>, ObservableValue<String>>() {
      @Override
      public ObservableValue<String> call(CellDataFeatures<LesxResource, String> data) {
        return new SimpleStringProperty(data.getValue()
            .getName());
      }
    });
    cc = new TableColumn<>(LesxMessage.getMessage("TEXT-COLUMN_NAME_CC"));
    cc.setCellValueFactory(new Callback<CellDataFeatures<LesxResource, String>, ObservableValue<String>>() {
      @Override
      public ObservableValue<String> call(CellDataFeatures<LesxResource, String> data) {
        return new SimpleStringProperty(data.getValue()
            .getCc()
            .toString());
      }
    });
    nameColumn.prefWidthProperty()
        .bind(tableCostumer.widthProperty()
            .divide(2));
    cc.prefWidthProperty()
        .bind(tableCostumer.widthProperty()
            .divide(2));
    tableCostumer.getColumns()
        .addAll(nameColumn, cc);
    tableCostumer.getItems()
        .addAll(dataModelResources.getResources());
  }

  /**
   * Saves the costumers selected.
   *
   * @return
   */
  private void saveCostumers() {
    if (mode == SelectionMode.SINGLE) {
      dataModelResources.setComponentSelected(dataModelResources.getResource(selectedCostumers)
          .get(0));
    }
    close();
  }

  /**
   * Closes the dialog.
   */
  private void close() {
    closeProperty.set(true);
  }

  public BooleanProperty closeProperty() {
    return closeProperty;
  }

}
