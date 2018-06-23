package lesx.icon.utils;

import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lesx.gui.message.LesxMessage;

public class LesxIcon {

  private final static Logger LOGGER = Logger.getLogger(LesxIcon.class.getName());

  public static String ADD = "/lesx/icon/add.png";
  public static String CHART = "/lesx/icon/chart.png";
  public static String CLOCK = "/lesx/icon/clock.png";
  public static String DELETE = "/lesx/icon/delete.png";
  public static String EDIT = "/lesx/icon/edit.png";
  public static String ERASE = "/lesx/icon/erase.png";
  public static String CHILDREN = "/lesx/icon/children.png";
  public static String PEOPLE = "/lesx/icon/people.png";
  public static String PRESENT = "/lesx/icon/gift.png";
  public static String PARTY = "/lesx/icon/party.png";

  public static ImageView getImage(String path) {
    ImageView imageView = null;
    try {
      final Image image = new Image(path);
      imageView = new ImageView(image);
    }
    catch (Exception e) {
      LOGGER.log(Level.SEVERE, LesxMessage.getMessage("ERROR-LOAD_IMAGE", path), e);
      e.printStackTrace();
    }
    return imageView;
  }

}
