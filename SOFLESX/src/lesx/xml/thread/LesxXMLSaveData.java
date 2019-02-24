package lesx.xml.thread;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import javafx.concurrent.Task;
import lesx.gui.message.LesxMessage;
import lesx.property.properties.ELesxUseCase;
import lesx.property.properties.LesxBusiness;
import lesx.property.properties.LesxComponent;
import lesx.property.properties.LesxResource;
import lesx.utils.LesxPropertyUtils;
import lesx.utils.LesxString;
import lesx.xml.property.LesxListBusinessXMLParser;
import lesx.xml.property.LesxListResourceXMLParser;

public class LesxXMLSaveData {

  private final static Logger LOGGER = Logger.getLogger(LesxXMLSaveData.class.getName());

  private List<LesxResource> resources = new ArrayList<>();
  private List<LesxBusiness> business = new ArrayList<>();
  private String path = "";
  private ExecutorService service = Executors.newSingleThreadExecutor(obs -> {
    Thread t = new Thread(obs);
    t.setDaemon(true);
    return t;
  });

  public void submit(Collection<? extends LesxComponent> map, ELesxUseCase useCase, Runnable onSucceed) {
    configureMaps(map, useCase);
    service.submit(createTask(onSucceed, useCase));
  }

  private void configureMaps(Collection<? extends LesxComponent> map, ELesxUseCase useCase) {
    LOGGER.log(Level.INFO, "Configuring maps for '" + useCase + "' use case");
    try {
      switch (useCase) {
        case UC_XML_RESOURCE:
          resources.clear();
          resources.addAll(map.stream()
              .map(LesxResource.class::cast)
              .collect(Collectors.toList()));
          path = LesxString.XML_RESOURCE_PATH;
          break;
        case UC_XML_BUSINESS:
          business.clear();
          business.addAll(map.stream()
              .map(LesxBusiness.class::cast)
              .collect(Collectors.toList()));
          path = LesxString.XML_BUSINESS_PATH;
          break;
        default:
          new Throwable(new IllegalArgumentException("Use case not supported"));
          break;
      }
      LOGGER.log(Level.INFO, "Configured maps for '" + useCase + "' use case");
    }
    catch (ClassCastException ce) {
      LOGGER.log(Level.SEVERE, LesxMessage.getMessage("ERROR-CAST_XML_DATA_SAVE", map.getClass(), useCase), ce);
    }
    catch (Exception e) {
      LOGGER.log(Level.SEVERE, LesxMessage.getMessage("ERROR-SAVE_XML_DATA", useCase), e);
      e.printStackTrace();
    }

  }

  private Task<Boolean> createTask(Runnable onSucceed, ELesxUseCase useCase) {
    return new Task<Boolean>() {
      @Override
      protected Boolean call() throws JAXBException, IOException {
        LOGGER.log(Level.INFO, "Saving maps for '" + useCase + "' use case");
        boolean success = false;
        File xmlFile = new File(path);
        LOGGER.log(Level.INFO, "Searching for : '" + path + "' path file");
        if (xmlFile.exists()) {
          JAXBContext context;
          Unmarshaller jaxbUnmarshaller;
          JAXBContext jaxbContext;
          Marshaller jaxbMarshaller;
          LOGGER.log(Level.INFO, "Configuring unmarshaller for '" + useCase + "' use case");
          try {
            switch (useCase) {
              case UC_XML_RESOURCE:
                context = JAXBContext.newInstance(LesxListResourceXMLParser.class);
                jaxbUnmarshaller = context.createUnmarshaller();
                //convert to desired object
                final LesxListResourceXMLParser propertiesResources = (LesxListResourceXMLParser) jaxbUnmarshaller.unmarshal(xmlFile);
                propertiesResources.setResources(LesxPropertyUtils.convertLesxCostumerIntoXMLParser(resources));
                //save data
                jaxbContext = JAXBContext.newInstance(LesxListResourceXMLParser.class);
                jaxbMarshaller = jaxbContext.createMarshaller();
                jaxbMarshaller.marshal(propertiesResources, xmlFile);
                LOGGER.log(Level.INFO, "Marshalled LesxListResourceXMLParser Instance");
                success = true;
                break;
              case UC_XML_BUSINESS:
                context = JAXBContext.newInstance(LesxListBusinessXMLParser.class);
                jaxbUnmarshaller = context.createUnmarshaller();
                //convert to desired object
                final LesxListBusinessXMLParser propertiesBusiness = (LesxListBusinessXMLParser) jaxbUnmarshaller.unmarshal(xmlFile);
                propertiesBusiness.setBusiness(LesxPropertyUtils.convertLesxBusinessIntoXMLParser(business));
                //save data
                jaxbContext = JAXBContext.newInstance(LesxListBusinessXMLParser.class);
                jaxbMarshaller = jaxbContext.createMarshaller();
                jaxbMarshaller.marshal(propertiesBusiness, xmlFile);
                LOGGER.log(Level.INFO, "Marshalled LesxListBusinessXMLParser Instance");
                success = true;
                break;
              default:
                break;
            }
          }
          catch (Exception e) {
            LOGGER.log(Level.SEVERE, LesxMessage.getMessage("ERROR-SAVE_XML_DATA", useCase), e);
            e.printStackTrace();
          }
        }
        return success;
      }

      @Override
      protected void succeeded() {
        super.succeeded();
        LOGGER.log(Level.INFO, "Saved maps for '" + useCase + "' use case");
        if (onSucceed != null) {
          onSucceed.run();
        }
      }
    };
  }

  public void shutDown() {
    LOGGER.log(Level.INFO, "Shutting down Executor Service");
    service.shutdownNow();
  }

}
