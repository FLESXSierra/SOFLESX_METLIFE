package lesx.xml.thread;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
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
import lesx.property.properties.ELesxUseCase;
import lesx.property.properties.LesxComponent;
import lesx.utils.LesxPropertyUtils;
import lesx.utils.LesxString;
import lesx.xml.property.LesxBusinessXMLParser;
import lesx.xml.property.LesxListBusinessXMLParser;
import lesx.xml.property.LesxListResourceXMLParser;
import lesx.xml.property.LesxResourceXMLParser;

public class LesxXMLWriteNewFile extends Service<Pair<Boolean, Map<Long, Map<Long, ? extends LesxComponent>>>> {

  private final static Logger LOGGER = Logger.getLogger(LesxXMLWriteNewFile.class.getName());

  private String path;
  private Boolean available;
  private ELesxUseCase useCase;
  final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(LesxMessage.getMessage("DATE-FORMATTER_PERIOD_DATE_FORMAT"), Locale.ENGLISH);

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
        LesxResourceXMLParser costumerDemo = new LesxResourceXMLParser();
        costumerDemo.setCc(0L);
        costumerDemo.setId(0L);
        costumerDemo.setName(LesxString.ATTR_XML_NAME);
        costumerDemo.setLocation(0L);
        costumerDemo.setBirthday(LocalDate.now()
            .format(formatter)
            .toString());
        costumerDemo.setBusiness_id(0L);
        costumerDemo.setRegistration_date(LocalDate.now()
            .format(formatter)
            .toString());
        LesxListResourceXMLParser list = new LesxListResourceXMLParser();
        list.setResources(Arrays.asList(costumerDemo));
        jaxbMarshaller.marshal(list, file);
        resultMap = LesxPropertyUtils.converXMLPropertyIntoLesxProperty(list, ELesxUseCase.UC_XML_RESOURCE);
        break;
      case UC_XML_BUSINESS:
        jaxbContext = JAXBContext.newInstance(LesxListBusinessXMLParser.class);
        jaxbMarshaller = jaxbContext.createMarshaller();
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        LesxBusinessXMLParser businessDemo = new LesxBusinessXMLParser();
        businessDemo.setId(0L);
        businessDemo.setPrima(200000L);
        businessDemo.setNbs(12 * businessDemo.getPrima());
        businessDemo.setProducto(0);
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
