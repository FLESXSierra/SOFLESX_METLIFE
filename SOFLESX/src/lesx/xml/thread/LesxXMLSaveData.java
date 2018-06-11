package lesx.xml.thread;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBException;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import lesx.gui.message.LesxMessage;
import lesx.property.properties.ELesxUseCase;
import lesx.property.properties.LesxComponent;
import lesx.utils.LesxString;

public class LesxXMLSaveData extends Service<Boolean> {

  private final static Logger LOGGER = Logger.getLogger(LesxXMLSaveData.class.getName());

  private List<LesxComponent> test = new ArrayList<>();
  private String path = "";
  private ELesxUseCase useCase;

  public LesxXMLSaveData(Collection<? extends LesxComponent> costumer, ELesxUseCase useCase) {
    this.useCase = useCase;
    try {
      switch (useCase) {
        case UC_XML_COSTOMER:
          test.clear();
          test.addAll(costumer.stream()
              .map(LesxComponent.class::cast)
              .collect(Collectors.toList()));
          path = LesxString.XML_PATH;
          break;
        default:
          new Throwable(new IllegalArgumentException("Use case not supported"));
          break;
      }
    }
    catch (ClassCastException ce) {
      LOGGER.log(Level.SEVERE, LesxMessage.getMessage("ERROR-CAST_XML_DATA_SAVE", costumer.getClass(), useCase), ce);
    }
    catch (Exception e) {
      LOGGER.log(Level.SEVERE, LesxMessage.getMessage("ERROR-SAVE_XML_DATA", useCase), e);
      e.printStackTrace();
    }

  }

  @Override
  protected Task<Boolean> createTask() {
    return new Task<Boolean>() {
      @Override
      protected Boolean call() throws JAXBException, IOException {
        boolean success = false;
        File xmlFile = new File(path);
        if (xmlFile.exists()) {
          //          JAXBContext context;
          //          Unmarshaller jaxbUnmarshaller;
          //          JAXBContext jaxbContext;
          //          Marshaller jaxbMarshaller;
          try {
            switch (useCase) {
              case UC_XML_COSTOMER:
                //                context = JAXBContext.newInstance(LesxListPropertiesXMLParser.class);
                //                jaxbUnmarshaller = context.createUnmarshaller();
                //                //convert to desired object
                //                final LesxListPropertiesXMLParser properties = (LesxListPropertiesXMLParser) jaxbUnmarshaller.unmarshal(xmlFile);
                //                properties.setCostumer(LesxPropertyUtils.convertLesxCostumerIntoXMLParser(test));
                //                //save data
                //                jaxbContext = JAXBContext.newInstance(LesxListPropertiesXMLParser.class);
                //                jaxbMarshaller = jaxbContext.createMarshaller();
                //                jaxbMarshaller.marshal(properties, xmlFile);
                //                success = true;
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

    };
  }

}
