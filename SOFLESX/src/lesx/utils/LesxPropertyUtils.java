package lesx.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.beans.property.ObjectProperty;
import lesx.gui.message.LesxMessage;
import lesx.property.properties.ELesxPropertyType;
import lesx.property.properties.ELesxUseCase;
import lesx.property.properties.LesxComponent;

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
      case UC_XML_COSTOMER:
        //        LesxListPropertiesXMLParser castList = (LesxListPropertiesXMLParser) list;
        //        if (list != null && !LesxMisc.isEmpty(castList.getCostumer())) {
        //          LesxCostumer costumer;
        //          Map<Long, LesxComponent> mapCostumer = new HashMap<>();
        //          //Adding the costumer Property
        //          for (LesxCostumerXMLParser costumerXML : castList.getCostumer()) {
        //            costumer = new LesxCostumer(costumerXML);
        //            //No a la key con location, ID!
        //            mapCostumer.put(costumer.getId(), costumer);
        //          }
        //          result.put(ELesxPropertyKeys.COSTUMER.getValue(), mapCostumer);
        //        }
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

  //  public static List<LesxCostumerXMLParser> convertLesxCostumerIntoXMLParser(List<LesxCostumer> costumers) {
  //    List<LesxCostumerXMLParser> properties = new ArrayList<>();
  //    for (LesxCostumer costumer : costumers) {
  //      properties.add(new LesxCostumerXMLParser(costumer));
  //    }
  //    return null;
  //  }

}
