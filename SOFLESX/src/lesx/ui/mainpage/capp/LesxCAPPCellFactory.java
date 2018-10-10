package lesx.ui.mainpage.capp;

import javafx.scene.control.TableCell;
import lesx.property.properties.LesxReportMonthBusiness;

public class LesxCAPPCellFactory extends TableCell<LesxReportMonthBusiness, String> {

  private static final String NONE_STYLE = null;
  private static final String FIRST_STYLE = "-fx-background-color: #8c8cff; -fx-text-fill: black";
  private static final String SECOND_STYLE = "-fx-background-color: #3535ff; -fx-text-fill: black";
  private static final String THIRD_STYLE = "-fx-background-color: blue; -fx-text-fill: black;";

  @Override
  protected void updateItem(String item, boolean empty) {
    super.updateItem(item, empty);
    setText(item);
    setStyle(null);
    if (!empty) {
      LesxReportMonthBusiness report = getTableRow() != null ? ((LesxReportMonthBusiness) getTableRow().getItem()) : null;
      if (report != null) {
        if (report.getCurrentCAPP() >= 15) {
          setStyle(THIRD_STYLE);
        }
        else if (report.getCurrentCAPP() >= 9) {
          setStyle(SECOND_STYLE);
        }
        else if (report.getCurrentCAPP() >= 5) {
          setStyle(FIRST_STYLE);
        }
        else {
          setStyle(NONE_STYLE);
        }
      }
    }
  }

}
