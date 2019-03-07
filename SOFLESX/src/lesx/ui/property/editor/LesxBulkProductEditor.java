package lesx.ui.property.editor;

import lesx.property.properties.LesxProperty;

/**
 * Editor created to supply the need of BULK CREATION/EDITION of the LesxProperty. Supported Actions:
 * <ul>
 * <i> BULK_CREATE </i>
 * </ul>
 *
 * @author led_s
 *
 */
public class LesxBulkProductEditor extends LesxProductEditor {

  /**
   * Generic constructor for BULK Product Editor
   * 
   * @param fxProperty needed to build the initial properties and bindings of the validations
   */
  public LesxBulkProductEditor(LesxProperty fxProperty) {
    super(fxProperty);
  }

}
