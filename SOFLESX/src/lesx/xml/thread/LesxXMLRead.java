package lesx.xml.thread;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.util.Pair;
import lesx.gui.message.LesxMessage;
import lesx.property.properties.ELesxUseCase;
import lesx.property.properties.LesxComponent;
import lesx.utils.LesxPropertyUtils;
import lesx.utils.LesxString;
import lesx.xml.property.LesxListBusinessXMLParser;
import lesx.xml.property.LesxListResourceXMLParser;

public class LesxXMLRead extends Service<Pair<Boolean, Map<Long, Map<Long, ? extends LesxComponent>>>> {

  private final static Logger LOGGER = Logger.getLogger(LesxXMLRead.class.getName());

  private String name;
  private Boolean available;
  private ELesxUseCase useCase;

  public LesxXMLRead(ELesxUseCase useCase) {
    this.useCase = useCase;
    switch (useCase) {
      case UC_XML_RESOURCE:
        this.name = LesxString.XML_RESOURCE_PATH;
        break;
      case UC_XML_BUSINESS:
        this.name = LesxString.XML_BUSINESS_PATH;
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
        File xmlFile = new File(name);
        if (xmlFile != null) {
          try {
            available = true;
            // PLEASE BE SURE TO MODIFY ALSO IMPORT ACTION
            JAXBContext context;
            Unmarshaller jaxbUnmarshaller;
            switch (useCase) {
              case UC_XML_RESOURCE:
                context = JAXBContext.newInstance(LesxListResourceXMLParser.class);
                jaxbUnmarshaller = context.createUnmarshaller();
                //convert to desired object
                LesxListResourceXMLParser propertiesResource = (LesxListResourceXMLParser) jaxbUnmarshaller.unmarshal(xmlFile);
                result.putAll(LesxPropertyUtils.converXMLPropertyIntoLesxProperty(propertiesResource, useCase));
                break;
              case UC_XML_BUSINESS:
                context = JAXBContext.newInstance(LesxListBusinessXMLParser.class);
                jaxbUnmarshaller = context.createUnmarshaller();
                //convert to desired object
                LesxListBusinessXMLParser propertiesBusiness = (LesxListBusinessXMLParser) jaxbUnmarshaller.unmarshal(xmlFile);
                result.putAll(LesxPropertyUtils.converXMLPropertyIntoLesxProperty(propertiesBusiness, useCase));
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
            available = true;
            LOGGER.log(Level.SEVERE, LesxMessage.getMessage("ERROR-XML_READER", name), e);
            e.printStackTrace();
          }
        }
        return new Pair<Boolean, Map<Long, Map<Long, ? extends LesxComponent>>>(available, result);
      }
    };
  }

}
