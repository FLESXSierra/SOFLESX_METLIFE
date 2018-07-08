package lesx.xml.thread;

import java.io.File;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.util.Pair;
import lesx.gui.message.LesxMessage;
import lesx.property.properties.ELesxProductType;
import lesx.property.properties.ELesxUseCase;
import lesx.property.properties.LesxComponent;
import lesx.utils.LesxPropertyUtils;
import lesx.utils.LesxString;
import lesx.xml.property.LesxBusinessXMLParser;
import lesx.xml.property.LesxListBusinessXMLParser;
import lesx.xml.property.LesxListResourceXMLParser;
import lesx.xml.property.LesxProductTypeXMLParser;
import lesx.xml.property.LesxResourceXMLParser;

public class LesxXMLWriteNewFile extends Service<Pair<Boolean, Map<Long, Map<Long, ? extends LesxComponent>>>> {

  private final static Logger LOGGER = Logger.getLogger(LesxXMLWriteNewFile.class.getName());

  private String path;
  private Boolean available;
  private ELesxUseCase useCase;

  public LesxXMLWriteNewFile(ELesxUseCase useCase) {
    this.useCase = useCase;
    switch (useCase) {
      case UC_XML_RESOURCE:
        path = LesxString.XML_RESOURCE_PATH;
        break;
      case UC_XML_BUSINESS:
        path = LesxString.XML_BUSINESS_PATH;
        break;
      default:
        new Throwable(new IllegalArgumentException(LesxMessage.getMessage("ERROR-USE_CASE_NOT_SUPPORTED", useCase)));
        break;
    }
  }

  @Override
  protected Task<Pair<Boolean, Map<Long, Map<Long, ? extends LesxComponent>>>> createTask() {
    return new Task<Pair<Boolean, Map<Long, Map<Long, ? extends LesxComponent>>>>() {

      @Override
      protected Pair<Boolean, Map<Long, Map<Long, ? extends LesxComponent>>> call() throws Exception {
        available = true;
        Map<Long, Map<Long, ? extends LesxComponent>> resultMap = new HashMap<>();
        try {
          //CREATING XML WITH ROOT NODE
          DocumentBuilderFactory dbfactory = DocumentBuilderFactory.newInstance();
          DocumentBuilder dBuilder = dbfactory.newDocumentBuilder();
          Document doc;
          doc = dBuilder.newDocument();
          TransformerFactory transformerFactory = TransformerFactory.newInstance();
          Transformer transformer = transformerFactory.newTransformer();
          DOMSource source = new DOMSource(doc);
          StreamResult result = new StreamResult(new File(path));
          transformer.transform(source, result);

          //ADDING THE "NEW" INFO (DEMO)
          resultMap = createDefaultMap();
          LOGGER.log(Level.INFO, LesxMessage.getMessage("INFO-XML_WRITER_COMPLETE"));
        }
        catch (ParserConfigurationException pce) {
          available = false;
          LOGGER.log(Level.SEVERE, LesxMessage.getMessage("ERROR-XML_WRITER", path), pce);
          pce.printStackTrace();
          return new Pair<Boolean, Map<Long, Map<Long, ? extends LesxComponent>>>(available, resultMap);
        }
        catch (TransformerException e) {
          available = false;
          LOGGER.log(Level.SEVERE, LesxMessage.getMessage("ERROR-XML_WRITER", path), e);
          e.printStackTrace();
          return new Pair<Boolean, Map<Long, Map<Long, ? extends LesxComponent>>>(available, resultMap);
        }
        catch (Exception ex) {
          available = false;
          LOGGER.log(Level.SEVERE, LesxMessage.getMessage("ERROR-XML_WRITER", path), ex);
          ex.printStackTrace();
          return new Pair<Boolean, Map<Long, Map<Long, ? extends LesxComponent>>>(available, resultMap);
        }
        return new Pair<Boolean, Map<Long, Map<Long, ? extends LesxComponent>>>(available, resultMap);
      }

    };
  }

  private Map<Long, Map<Long, ? extends LesxComponent>> createDefaultMap() throws Exception {
    JAXBContext jaxbContext;
    Marshaller jaxbMarshaller;
    Map<Long, Map<Long, ? extends LesxComponent>> resultMap = new HashMap<>();
    File file = new File(path);
    switch (useCase) {
      case UC_XML_RESOURCE:
        jaxbContext = JAXBContext.newInstance(LesxListResourceXMLParser.class);
        jaxbMarshaller = jaxbContext.createMarshaller();
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        LesxResourceXMLParser resourceDemo = new LesxResourceXMLParser();
        resourceDemo.setCc(0L);
        resourceDemo.setId(0L);
        resourceDemo.setName(LesxString.ATTR_XML_NAME);
        resourceDemo.setLocation(0L);
        resourceDemo.setSolicitud(0L);
        resourceDemo.setBirthday(LocalDate.now()
            .format(LesxPropertyUtils.FORMATTER)
            .toString());
        resourceDemo.setRegistration_date(LocalDate.now()
            .format(LesxPropertyUtils.FORMATTER)
            .toString());
        LesxListResourceXMLParser list = new LesxListResourceXMLParser();
        list.setResources(Arrays.asList(resourceDemo));
        jaxbMarshaller.marshal(list, file);
        resultMap = LesxPropertyUtils.converXMLPropertyIntoLesxProperty(list, ELesxUseCase.UC_XML_RESOURCE);
        break;
      case UC_XML_BUSINESS:
        jaxbContext = JAXBContext.newInstance(LesxListBusinessXMLParser.class);
        jaxbMarshaller = jaxbContext.createMarshaller();
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        LesxBusinessXMLParser businessDemo = new LesxBusinessXMLParser();
        businessDemo.setId(0L);
        businessDemo.setDate(LocalDate.now()
            .format(LesxPropertyUtils.FORMATTER)
            .toString());
        LesxProductTypeXMLParser productType = new LesxProductTypeXMLParser();
        productType.setTypeAP(ELesxProductType.AP_SALUD.getKey());
        productType.setPrimaAP(300000L);
        businessDemo.setProducto(productType);
        businessDemo.setResource_id(0L);
        LesxListBusinessXMLParser listBusiness = new LesxListBusinessXMLParser();
        listBusiness.setBusiness(Arrays.asList(businessDemo));
        jaxbMarshaller.marshal(listBusiness, file);
        resultMap = LesxPropertyUtils.converXMLPropertyIntoLesxProperty(listBusiness, ELesxUseCase.UC_XML_BUSINESS);
        break;
      default:
        new Throwable(new IllegalArgumentException(LesxMessage.getMessage("ERROR-USE_CASE_NOT_SUPPORTED", useCase)));
        break;
    }
    return resultMap;
  }

}
