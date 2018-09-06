package lesx.ui.mainpage.capp;

import javafx.scene.control.TableCell;
import lesx.property.properties.LesxReportMonthBusiness;

public class LesxCAPPCellFactory extends TableCell<LesxReportMonthBusiness, String> {

  private static final String INVALID_STYLE = "-fx-background-color: red; -fx-text-fill: white;";
  private static final String VALID_STYLE = "-fx-background-color: green; -fx-text-fill: black;";

  @Override
  protected void updateItem(String item, boolean empty) {
    super.updateItem(item, empty);
    setText(item);
    setStyle(null);
    if (!empty) {
      LesxReportMonthBusiness report = getTableRow() != null ? ((LesxReportMonthBusiness) getTableRow().getItem()) : null;
      if (report != null) {
        if (report.getAchievedCAPP()
            .getFirst()) {
          setStyle(report.getAchievedCAPP()
              .getSecond() ? VALID_STYLE : INVALID_STYLE);
        }
      }
    }
  }

}
