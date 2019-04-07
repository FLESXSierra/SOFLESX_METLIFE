package lesx.ui.components.birthdayButton;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;

public class LesxBirthdayButton extends Button {

  private ObservableList<String> names = FXCollections.observableArrayList();

  public LesxBirthdayButton() {
    super();
    setId("birday-button");
  }

  public ObservableList<String> getNames() {
    return names;
  }

  public void setNames(List<String> names) {
    this.names.clear();
    this.names.addAll(names);
  }

  @Override
  protected LesxBirthdayButtonSkin createDefaultSkin() {
    return new LesxBirthdayButtonSkin(this);
  }

}
