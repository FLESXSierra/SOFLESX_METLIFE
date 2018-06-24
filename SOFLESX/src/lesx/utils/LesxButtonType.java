package lesx.utils;

import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import lesx.gui.message.LesxMessage;

public class LesxButtonType {

  public static final ButtonType SAVE = new ButtonType(LesxMessage.getMessage("TEXT-SAVE_BUTTON"), ButtonData.YES);
  public static final ButtonType DONT_SAVE = new ButtonType(LesxMessage.getMessage("TEXT-NO_SAVE_BUTTON"), ButtonData.NO);
  public static final ButtonType CANCEL = new ButtonType(LesxMessage.getMessage("TEXT-CANCEL_BUTTON"), ButtonData.CANCEL_CLOSE);
  public static final ButtonType NEW_RESOURCE = new ButtonType(LesxMessage.getMessage("TEXT-NEW_SELL_BUTTON"), ButtonData.OK_DONE);
  public static final ButtonType NO_NEW_RESOURCE = new ButtonType(LesxMessage.getMessage("TEXT-EXISTENT_SELL_BUTTON"), ButtonData.OK_DONE);

}
