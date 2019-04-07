package lesx.icon.utils;

import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lesx.gui.message.LesxMessage;

public class LesxIcon {

  private final static Logger LOGGER = Logger.getLogger(LesxIcon.class.getName());

  public static String HELP = "/images/help.png";
  public static String BAIT = "/images/bait.PNG";
  public static String PIRATE = "/images/pirate.png";
  public static String ADD = "/images/add.png";
  public static String CHART = "/images/chart.png";
  public static String CLOCK = "/images/clock.png";
  public static String DELETE = "/images/delete.png";
  public static String EDIT = "/images/edit.png";
  public static String ERASE = "/images/erase.png";
  public static String CHILDREN = "/images/children.png";
  public static String PEOPLE = "/images/people.png";
  public static String PRESENT = "/images/gift.png";
  public static String PARTY = "/images/party.png";
  public static String SELL = "/images/sell.png";
  public static String PRICE_ICON = "/images/editSell.png";
  public static String MONEY = "/images/money.png";
  public static String METLIFE = "/images/MetLife.png";

  public static ImageView getImage(String path) {
    ImageView imageView = null;
    try {
      final Image image = new Image(LesxIcon.class.getResourceAsStream(path));
      imageView = new ImageView(image);
    }
    catch (Exception e) {
      LOGGER.log(Level.SEVERE, LesxMessage.getMessage("ERROR-LOAD_IMAGE", path), e);
      e.printStackTrace();
    }
    return imageView;
  }

}
