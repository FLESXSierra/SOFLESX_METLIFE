package lesx.xml.thread;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import javafx.concurrent.Task;
import lesx.gui.message.LesxMessage;
import lesx.property.properties.ELesxUseCase;
import lesx.property.properties.LesxBusiness;
import lesx.property.properties.LesxComponent;
import lesx.property.properties.LesxResource;
import lesx.utils.LesxMisc;
import lesx.utils.LesxPropertyUtils;
import lesx.utils.LesxString;
import lesx.xml.property.LesxListBusinessXMLParser;
import lesx.xml.property.LesxListResourceXMLParser;

public class LesxXMLImportData extends Task<Boolean> {

  private final static Logger LOGGER = Logger.getLogger(LesxXMLRead.class.getName());
  private Map<Long, ? extends LesxComponent> resultR = new HashMap<>();
  private Map<Long, ? extends LesxComponent> resultB = new HashMap<>();

  private List<File> files;

  public LesxXMLImportData(List<File> files) {
    this.files = files;
  }

  @Override
  protected Boolean call() throws Exception {
    if (LesxMisc.isEmpty(files)) {
      LOGGER.log(Level.SEVERE, "Empty Files");
      failed();
    }
    updateMessage(LesxMessage.getMessage("TEXT-LOADING_TITLE"));
    for (File file : files) {
      if (!readFile(file)) {
        failed();
        return false;
      }
    }
    updateMessage(LesxMessage.getMessage("TEXT-LOADING_SAVING"));
    if (!LesxMisc.isEmpty(resultR)) {
      if (!saveFile(resultR, ELesxUseCase.UC_XML_RESOURCE)) {
        failed();
        return false;
      }
    }
    if (!LesxMisc.isEmpty(resultB)) {
      if (!saveFile(resultB, ELesxUseCase.UC_XML_BUSINESS)) {
        failed();
        return false;
      }
    }
    updateMessage(LesxMessage.getMessage("TEXT-LOADING_SUCCESS"));
    return true;
  }

  private boolean saveFile(Map<Long, ? extends LesxComponent> map, ELesxUseCase useCase) {
    boolean success = false;
    File xmlFile = new File(useCase == ELesxUseCase.UC_XML_RESOURCE ? LesxString.XML_RESOURCE_PATH : LesxString.XML_BUSINESS_PATH);
    if (xmlFile.exists()) {
      JAXBContext context;
      Unmarshaller jaxbUnmarshaller;
      JAXBContext jaxbContext;
      Marshaller jaxbMarshaller;
      try {
        switch (useCase) {
          case UC_XML_RESOURCE:
            List<LesxResource> resources = map.values()
                .stream()
                .map(LesxResource.class::cast)
                .collect(Collectors.toList());
            context = JAXBContext.newInstance(LesxListResourceXMLParser.class);
            jaxbUnmarshaller = context.createUnmarshaller();
            //convert to desired object
            final LesxListResourceXMLParser propertiesResources = (LesxListResourceXMLParser) jaxbUnmarshaller.unmarshal(xmlFile);
            propertiesResources.setResources(LesxPropertyUtils.convertLesxCostumerIntoXMLParser(resources));
            //save data
            jaxbContext = JAXBContext.newInstance(LesxListResourceXMLParser.class);
            jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.marshal(propertiesResources, xmlFile);
            success = true;
            break;
          case UC_XML_BUSINESS:
            List<LesxBusiness> business = map.values()
                .stream()
                .map(LesxBusiness.class::cast)
                .collect(Collectors.toList());
            context = JAXBContext.newInstance(LesxListBusinessXMLParser.class);
            jaxbUnmarshaller = context.createUnmarshaller();
            //convert to desired object
            final LesxListBusinessXMLParser propertiesBusiness = (LesxListBusinessXMLParser) jaxbUnmarshaller.unmarshal(xmlFile);
            propertiesBusiness.setBusiness(LesxPropertyUtils.convertLesxBusinessIntoXMLParser(business));
            //save data
            jaxbContext = JAXBContext.newInstance(LesxListBusinessXMLParser.class);
            jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.marshal(propertiesBusiness, xmlFile);
            success = true;
            break;
          default:
            LOGGER.log(Level.SEVERE, "Use case not supported");
            return false;
        }
      }
      catch (Exception e) {
        LOGGER.log(Level.SEVERE, LesxMessage.getMessage("ERROR-SAVE_XML_DATA", useCase), e);
        return false;
      }
    }
    else {
      LOGGER.log(Level.SEVERE, "File Not Exist");
      return false;
    }
    return success;
  }

  private boolean readFile(File file) {
    if (file.exists()) {
      String name = file.getName();
      ELesxUseCase useCase;
      if (LesxString.XML_NAME_RESOURCE.equals(name)) {
        useCase = ELesxUseCase.UC_XML_RESOURCE;
      }
      else if (LesxString.XML_NAME_BUSINESS.equals(name)) {
        useCase = ELesxUseCase.UC_XML_BUSINESS;
      }
      else {
        LOGGER.log(
            Level.SEVERE,
            "Names of files not recognized, be sure it follows: Resource XML '" + LesxString.XML_NAME_RESOURCE + "' and Business XML '"
                + LesxString.XML_NAME_BUSINESS + "'");
        return false;
      }
      try {
        JAXBContext context;
        Unmarshaller jaxbUnmarshaller;
        switch (useCase) {
          case UC_XML_RESOURCE:
            context = JAXBContext.newInstance(LesxListResourceXMLParser.class);
            jaxbUnmarshaller = context.createUnmarshaller();
            //convert to desired object
            LesxListResourceXMLParser propertiesResource = (LesxListResourceXMLParser) jaxbUnmarshaller.unmarshal(file);
            resultR = LesxPropertyUtils.converXMLPropertyIntoLesxProperty(propertiesResource, useCase)
                .get(useCase.getKey());
            return !LesxMisc.isEmpty(resultR);
          case UC_XML_BUSINESS:
            context = JAXBContext.newInstance(LesxListBusinessXMLParser.class);
            jaxbUnmarshaller = context.createUnmarshaller();
            //convert to desired object
            LesxListBusinessXMLParser propertiesBusiness = (LesxListBusinessXMLParser) jaxbUnmarshaller.unmarshal(file);
            resultB = LesxPropertyUtils.converXMLPropertyIntoLesxProperty(propertiesBusiness, useCase)
                .get(useCase.getKey());
            return !LesxMisc.isEmpty(resultB);
          default:
            LOGGER.log(Level.SEVERE, "Use case not supported");
            return false;
        }
      }
      catch (Exception e) {
        LOGGER.log(Level.SEVERE, "Exception fired while importing", e);
        return false;
      }
    }
    else {
      LOGGER.log(Level.SEVERE, "File doesn't exist");
      return false;
    }
  }

  @Override
  protected void failed() {
    updateMessage(LesxMessage.getMessage("TEXT-LOADING_ERROR_TITLE"));
    super.failed();
  }

}
