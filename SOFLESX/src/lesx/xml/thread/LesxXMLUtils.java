package lesx.xml.thread;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.concurrent.WorkerStateEvent;
import javafx.util.Pair;
import lesx.gui.message.LesxMessage;
import lesx.property.properties.ELesxUseCase;
import lesx.property.properties.LesxComponent;
import lesx.ui.soflesx.LesxMain;

public class LesxXMLUtils {

  private final static Logger LOGGER = Logger.getLogger(LesxXMLUtils.class.getName());

  private static boolean resourceLoaded;
  private static boolean businessLoaded;

  public static void importXMLFileToLesxProperty(Runnable run, ELesxUseCase useCase) {
    final Consumer<Pair<Boolean, Map<Long, Map<Long, ? extends LesxComponent>>>> consumer = obs -> {
      if (obs.getKey()) {
        LesxMain.getInstance()
            .getDbProperty()
            .addDataMap(obs.getValue());
        run.run();
      }
      else {
        LesxXMLUtils.writeNewXML(available -> {
          if (available.getKey()) {
            LesxMain.getInstance()
                .getDbProperty()
                .addDataMap(available.getValue());
            run.run();
          }
          else {
            throw new RuntimeException(LesxMessage.getMessage("ERROR-XML_READER_DATA"));
          }
        }, useCase);
      }
    };
    LesxXMLRead read = new LesxXMLRead(useCase);
    read.start();
    read.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, obs -> consumer.accept(read.getValue()));
  }

  public static void importAllXMLFileToLesxProperty(Runnable run) {
    // TODO LOAD HERE ALL XML
    LesxXMLUtils.importXMLFileToLesxProperty(() -> {
      resourceLoaded = true;
      verifyLoadedXML(run);
    }, ELesxUseCase.UC_XML_RESOURCE);
    LesxXMLUtils.importXMLFileToLesxProperty(() -> {
      businessLoaded = true;
      verifyLoadedXML(run);
    }, ELesxUseCase.UC_XML_BUSINESS);
  }

  private static void verifyLoadedXML(Runnable run) {
    if (resourceLoaded && businessLoaded) {
      run.run();
    }
  }

  public static void writeNewXML(Consumer<Pair<Boolean, Map<Long, Map<Long, ? extends LesxComponent>>>> consumer, ELesxUseCase useCase) {
    LesxXMLWriteNewFile write = new LesxXMLWriteNewFile(useCase);
    write.addEventFilter(WorkerStateEvent.WORKER_STATE_SUCCEEDED, obs -> consumer.accept(write.getValue()));
    write.start();
  }

  /**
   * Reads file XML content
   *
   * @param xmlPath
   * @return the XML converted
   */
  public static String getXMLContentToString(String xmlPath) {
    FileReader fr = null;
    char[] buffer = new char[1024];
    StringBuffer fileContent = new StringBuffer();
    try {
      fr = new FileReader(xmlPath);
      while (fr.read(buffer) != -1) {
        fileContent.append(new String(buffer));
      }
    }
    catch (FileNotFoundException e) {
      LOGGER.log(Level.SEVERE, LesxMessage.getMessage("ERROR-XML_READER", xmlPath), e);
    }
    catch (IOException e) {
      LOGGER.log(Level.SEVERE, LesxMessage.getMessage("ERROR-XML_READER", xmlPath), e);
    }
    finally {
      if (fr != null) {
        try {
          fr.close();
        }
        catch (IOException e) {
          LOGGER.log(Level.SEVERE, LesxMessage.getMessage("ERROR-XML_READER", xmlPath), e);
        }
      }
    }
    return fileContent.toString();
  }

}
