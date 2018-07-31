package lesx.logger;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import lesx.gui.message.LesxMessage;
import lesx.utils.LesxMisc;

public class LesxLogger extends Handler {

  @Override
  public void publish(LogRecord record) {
    try {
      StringBuilder log = new StringBuilder();
      LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(record.getMillis()), ZoneId.systemDefault());
      String dateFormatted = dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
      //WELCOME!
      if (record.getLevel() == Level.INFO && "Initializing".equals(record.getMessage())) {
        log.append("  ******************************************************\n");
        log.append("   *                    WELCOME                       *\n");
        log.append("   *                  LESX SOFTWARE                   *\n");
        log.append("   *                     SOFLEX                       *\n");
        log.append("  ******************************************************\n");
        log.append(dateFormatted + " : Initializing");
        System.out.println(log.toString());
        return;
      }
      if (record.getLevel() == Level.SEVERE) {
        log.append(LesxMessage.getMessage("LOGGER-ERROR_MESSAGE", dateFormatted));
      }
      else if (record.getLevel() == Level.WARNING) {
        log.append(LesxMessage.getMessage("LOGGER-WARNING_MESSAGE", dateFormatted));
      }
      else {
        log.append(LesxMessage.getMessage("LOGGER-LOG_MESSAGE", dateFormatted));
      }
      log.append(record.getLoggerName() + " : ");
      log.append(record.getMessage());
      if (record.getThrown() != null && !LesxMisc.isEmptyString(record.getThrown()
          .getMessage())) {
        log.append("\n");
        log.append(record.getThrown()
            .getMessage());
      }
      System.out.println(log.toString());
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void flush() {
    //Nothing
  }

  @Override
  public void close() throws SecurityException {
    //Nothing
  }

}
