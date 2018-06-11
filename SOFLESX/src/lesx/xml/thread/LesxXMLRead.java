package lesx.xml.thread;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBException;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.util.Pair;
import lesx.gui.message.LesxMessage;
import lesx.property.properties.ELesxUseCase;
import lesx.property.properties.LesxComponent;
import lesx.utils.LesxString;

public class LesxXMLRead extends Service<Pair<Boolean, Map<Long, Map<Long, ? extends LesxComponent>>>> {

  private final static Logger LOGGER = Logger.getLogger(LesxXMLRead.class.getName());

  private String path;
  private Boolean available;
  private ELesxUseCase useCase;

  public LesxXMLRead(ELesxUseCase useCase) {
    this.useCase = useCase;
    switch (useCase) {
      case UC_XML_COSTOMER:
        this.path = LesxString.XML_PATH;
        break;
      default:
        LOGGER.log(
            Level.SEVERE,
            LesxMessage.getMessage("ERROR-XML_USECASE_NOT_SUPPORTED", useCase),
            new Throwable(new IllegalArgumentException(LesxMessage.getMessage("ERROR-XML_USECASE_NOT_SUPPORTED", useCase))));
        break;
    }
  }

  @Override
  protected Task<Pair<Boolean, Map<Long, Map<Long, ? extends LesxComponent>>>> createTask() {
    return new Task<Pair<Boolean, Map<Long, Map<Long, ? extends LesxComponent>>>>() {

      @Override
      protected Pair<Boolean, Map<Long, Map<Long, ? extends LesxComponent>>> call() throws Exception, JAXBException {
        available = false;
        Map<Long, Map<Long, ? extends LesxComponent>> result = new HashMap<>();
        File xmlFile = new File(path);
        if (xmlFile.exists()) {
          try {
            available = true;
            //            JAXBContext context;
            //            Unmarshaller jaxbUnmarshaller;
            switch (useCase) {
              case UC_XML_COSTOMER:
                //                context = JAXBContext.newInstance(LesxListPropertiesXMLParser.class);
                //                jaxbUnmarshaller = context.createUnmarshaller();
                //                //convert to desired object
                //                LesxListPropertiesXMLParser properties = (LesxListPropertiesXMLParser) jaxbUnmarshaller.unmarshal(xmlFile);
                //                result.putAll(LesxPropertyUtils.converXMLPropertyIntoLesxProperty(properties, useCase));
                break;
              default:
                LOGGER.log(
                    Level.SEVERE,
                    LesxMessage.getMessage("ERROR-XML_USECASE_NOT_SUPPORTED", useCase),
                    new Throwable(new IllegalArgumentException(LesxMessage.getMessage("ERROR-XML_USECASE_NOT_SUPPORTED", useCase))));
                break;
            }
            LOGGER.log(Level.INFO, LesxMessage.getMessage("INFO-XML_READER_COMPLETE"));
          }
          catch (Exception e) {
            available = false;
            LOGGER.log(Level.SEVERE, LesxMessage.getMessage("ERROR-XML_READER", path), e);
            e.printStackTrace();
          }
        }
        return new Pair<Boolean, Map<Long, Map<Long, ? extends LesxComponent>>>(available, result);
      }
    };
  }

}
