package lesx.ui.mainpage.comisionmes;

import java.text.NumberFormat;
import java.util.Comparator;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.util.Callback;
import lesx.datamodel.LesxBusinessResourceDataModel;
import lesx.gui.message.LesxMessage;
import lesx.property.properties.ELesxListenerType;
import lesx.property.properties.ELesxMonth;
import lesx.property.properties.ELesxUseCase;
import lesx.property.properties.LesxReportMonthBusiness;
import lesx.scene.controller.LesxController;
import lesx.ui.components.LesxTableViewPane;
import lesx.ui.soflesx.LesxMain;

public class LesxComisionMesPaneController extends LesxController {

  @FXML
  LesxTableViewPane<LesxReportMonthBusiness> tablePane;

  private TableView<LesxReportMonthBusiness> table;
  private TableColumn<LesxReportMonthBusiness, String> month;
  private TableColumn<LesxReportMonthBusiness, String> comision;
  private ObservableList<LesxReportMonthBusiness> report = FXCollections.observableArrayList();

  private BooleanProperty showProgress = new SimpleBooleanProperty(this, "showProgress", false);
  private ObjectProperty<Integer> yearProperty = new SimpleObjectProperty<>(this, "yearProperty");
  private LesxBusinessResourceDataModel dataModel;
  Comparator<LesxReportMonthBusiness> comparator = Comparator.comparingInt(LesxReportMonthBusiness::getMonth);

  @FXML
  public void initialize() {
    showProgress.set(true);
    table = tablePane.getTable();
    tablePane.setUseCase(ELesxUseCase.UC_YEAR_ONLY);
    yearProperty.bind(tablePane.getToolBar()
        .yearProperty());
    yearProperty.addListener(obs -> loadDataInTable());
    setTitle(LesxMessage.getMessage("TEXT-TITLE_COMISION_MONTH"));
    dataModel = new LesxBusinessResourceDataModel(LesxMain.getInstance()
        .getDbProperty()
        .getBusinessResourceMap());
    updateCache = () -> updateDataFromCache();
    LesxMain.getInstance()
        .getDbProperty()
        .setListenerRB(ELesxListenerType.UPDATE, updateCache);
    initializeTable();
    loadDataInTable();
    showProgress.set(false);
  }

  @SuppressWarnings("unchecked")
  private void initializeTable() {
    month = new TableColumn<>(LesxMessage.getMessage("TEXT-COLUMN_NAME_MONTH"));
    month.setCellValueFactory(new Callback<CellDataFeatures<LesxReportMonthBusiness, String>, ObservableValue<String>>() {
      @Override
      public ObservableValue<String> call(CellDataFeatures<LesxReportMonthBusiness, String> data) {
        return new SimpleStringProperty(ELesxMonth.valueOf(data.getValue()
            .getMonth() - 1)
            .toString());
      }
    });
    comision = new TableColumn<>(LesxMessage.getMessage("TEXT-COLUMN_NAME_COMISION"));
    comision.setCellValueFactory(new Callback<CellDataFeatures<LesxReportMonthBusiness, String>, ObservableValue<String>>() {
      @Override
      public ObservableValue<String> call(CellDataFeatures<LesxReportMonthBusiness, String> data) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        return new SimpleStringProperty(formatter.format(data.getValue()
            .getComision()
            * data.getValue()
                .getPercent()));
      }
    });
    table.getColumns()
        .setAll(month, comision);
    table.setItems(report);
  }

  private void updateDataFromCache() {
    //Load Data Base
    dataModel.setMap(LesxMain.getInstance()
        .getDbProperty()
        .getBusinessResourceMap());
    loadDataInTable();
  }

  private void loadDataInTable() {
    report.setAll(dataModel.getMonthToMonthReport(yearProperty.get()));
    FXCollections.sort(report, comparator);
  }

  @Override
  public BooleanProperty showProgressProperty() {
    return showProgress;
  }

  @Override
  public void clearComponent() {
    LesxMain.getInstance()
        .getDbProperty()
        .removeListenerRB(ELesxListenerType.UPDATE, updateCache);
  }
}
