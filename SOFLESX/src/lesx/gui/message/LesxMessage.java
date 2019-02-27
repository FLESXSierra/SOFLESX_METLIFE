package lesx.gui.message;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import lesx.utils.LesxMisc;
import lesx.utils.LesxString;

public class LesxMessage {
  private final static Logger LOGGER = Logger.getLogger(LesxMessage.class.getName());

  public static String getMessage(String key) {
    Properties properties = new Properties();
    try {
      properties.load(getFile());
      Object value = properties.get(key);
      if (!LesxMisc.isEmpty(value)) {
        return String.valueOf(value);
      }
      LOGGER.warning(LesxMessage.getMessage("LOGGER-WARNING_NO_KEY", key));
      return "";
    }
    catch (FileNotFoundException e) {
      LOGGER.log(Level.SEVERE, "No se encuentra el archivo 'LesxMessage', por favor comuniquese con el admin.", e);
      e.printStackTrace();
      return "";
    }
    catch (IOException e) {
      LOGGER.log(Level.SEVERE, "Por favor comuniquese con el admin.", e);
      e.printStackTrace();
      return "";
    }
  }

  public static String getMessage(String key, Object... args) {
    String value = getMessage(key);
    if (args == null) {
      LOGGER.warning("argument is null on key: '" + key + "'");
      return value;
    }
    for (int i = 0; i < args.length; i++) {
      if (args[i] != null) {
        String sequence = "{" + String.valueOf(i) + "}";
        if (value.contains(sequence)) {
          value = value.replace(sequence, String.valueOf(args[i]));
        }
        else {
          LOGGER.warning("key '" + key + "' does not contain more arguments to replace");
        }
      }
      else {
        LOGGER.warning("argument is null on key: " + key);
      }
    }
    return value;
  }

  /**
   * Loads the file
   *
   * @param fileName File name
   * @return InputStream
   */
  public static InputStream getFile() {
    try {
      InputStream stream = LesxMessage.class.getResourceAsStream(LesxString.NAME_MESSAGE_PROPERTIES_FILE);
      if (stream == null) {
        throw new IllegalArgumentException("Cannot find file " + LesxString.NAME_MESSAGE_PROPERTIES_FILE);
      }
      return stream;
    }
    catch (Exception e) {
      e.printStackTrace();
      System.exit(1);
    }
    return null;
  }

}
