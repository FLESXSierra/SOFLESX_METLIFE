package lesx.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.beans.property.ObjectProperty;
import lesx.gui.message.LesxMessage;
import lesx.property.properties.ELesxPropertyKeys;
import lesx.property.properties.ELesxPropertyType;
import lesx.property.properties.ELesxUseCase;
import lesx.property.properties.LesxBusiness;
import lesx.property.properties.LesxComponent;
import lesx.property.properties.LesxResource;
import lesx.xml.property.LesxBusinessXMLParser;
import lesx.xml.property.LesxListBusinessXMLParser;
import lesx.xml.property.LesxListResourceXMLParser;
import lesx.xml.property.LesxResourceXMLParser;

public class LesxPropertyUtils {

  private final static Logger LOGGER = Logger.getLogger(LesxPropertyUtils.class.getName());

  /**
   * Returns a Map containing all the information of the properties on the given LesxListPropertiesXMLParser
   *
   * @param list LesxListPropertiesXMLParser
   * @param useCase Needed to add the key into the map
   * @return Map < Long, Map < Long, ? extends LesxProperty > > not null.
   */
  public static <T> Map<Long, Map<Long, ? extends LesxComponent>> converXMLPropertyIntoLesxProperty(T list, ELesxUseCase useCase) {
    Map<Long, Map<Long, ? extends LesxComponent>> result = new HashMap<>();
    switch (useCase) {
      case UC_XML_RESOURCE:
        LesxListResourceXMLParser castList = (LesxListResourceXMLParser) list;
        if (list != null && !LesxMisc.isEmpty(castList.getResources())) {
          LesxResource resource;
          Map<Long, LesxComponent> mapResource = new HashMap<>();
          //Adding the costumer Property
          for (LesxResourceXMLParser resourceXML : castList.getResources()) {
            resource = new LesxResource(resourceXML);
            mapResource.put(resource.getId(), resource);
          }
          result.put(ELesxPropertyKeys.RESOURCE.getValue(), mapResource);
        }
        break;
      case UC_XML_BUSINESS:
        LesxListBusinessXMLParser castListBusiness = (LesxListBusinessXMLParser) list;
        if (list != null && !LesxMisc.isEmpty(castListBusiness.getBusiness())) {
          LesxBusiness business;
          Map<Long, LesxComponent> mapBusiness = new HashMap<>();
          //Adding the costumer Property
          for (LesxBusinessXMLParser businessXML : castListBusiness.getBusiness()) {
            business = new LesxBusiness(businessXML);
            mapBusiness.put(business.getId(), business);
          }
          result.put(ELesxPropertyKeys.BUSINESS.getValue(), mapBusiness);
        }
        break;
      default:
        break;
    }
    return result;

  }

  public static void copyValueToTypeProperty(ELesxPropertyType type, Object value, ObjectProperty<Object> valueProperty) {
    try {
      switch (type) {
        case INTEGER:
          if (value != null) {
            valueProperty.set(Integer.valueOf(value.toString()));
          }
          else {
            valueProperty.set(null);
          }
          break;

        case LOCATION:
        case LONG:
          if (!LesxMisc.isEmpty(value)) {
            if (!value.toString()
                .endsWith(".")) {
              valueProperty.set(Long.valueOf(value.toString()));//TODO Issue with '.' not Committing value
            }
          }
          else {
            valueProperty.set(null);
          }
          break;

        case TEXT:
          if (value != null) {
            valueProperty.set(value.toString());
          }
          else {
            valueProperty.set(null);
          }
          break;

        default:
          break;
      }
    }
    catch (NumberFormatException e) {
      LOGGER.log(Level.SEVERE, LesxMessage.getMessage("ERROR-PROPERTY_UTILS_PARSING", type, valueProperty.get()));
      e.printStackTrace();
    }
  }

  public static List<LesxResourceXMLParser> convertLesxCostumerIntoXMLParser(List<LesxResource> resources) {
    List<LesxResourceXMLParser> properties = new ArrayList<>();
    for (LesxResource costumer : resources) {
      properties.add(new LesxResourceXMLParser(costumer));
    }
    return properties;
  }

  public static List<LesxBusinessXMLParser> convertLesxBusinessIntoXMLParser(List<LesxBusiness> business) {
    List<LesxBusinessXMLParser> properties = new ArrayList<>();
    for (LesxBusiness costumer : business) {
      properties.add(new LesxBusinessXMLParser(costumer));
    }
    return properties;
  }

}
