package lesx.ui.components.dialogs;

import javafx.fxml.FXML;
import lesx.datamodel.ILesxDataModel;
import lesx.datamodel.LesxBusinessResourceDataModel;
import lesx.gui.message.LesxMessage;
import lesx.property.properties.ELesxPropertyKeys;
import lesx.property.properties.LesxBusiness;
import lesx.property.properties.LesxComponent;
import lesx.property.properties.LesxResourceBusiness;

public class LesxEditBusinessDialog extends LesxEditComponentDialog {

  private LesxBusinessResourceDataModel dataModel;
  private LesxResourceBusiness resourceBusiness;
  private LesxBusiness business;

  @FXML
  @Override
  public void initialize() {
    super.initialize();
  }

  @Override
  protected void reInitialize() {
    setIsCreate(false);
    dataModel.setComponentSelected(resourceBusiness);
    init(dataModel, false);
  }

  @Override
  public String getTitle() {
    return LesxMessage.getMessage("TEXT-TITLE_RESOURCE_DIALOG_CREATE");
  }

  @Override
  protected String getDescriptionText(boolean isCreate) {
    return isCreate ? LesxMessage.getMessage("TEXT-DESCRIPTION_LABEL_NEW_RESOURCE") : LesxMessage.getMessage("TEXT-DESCRIPTION_LABEL_EDIT_RESOURCE");
  }

  @Override
  protected String getHeader(boolean isCreate) {
    StringBuilder string;
    string = new StringBuilder(128);
    if (isCreate) {
      string.append(LesxMessage.getMessage("TEXT-HEADER_LABEL_RESOURCE_PANE", "Nuevo"));
    }
    else {
      string.append(LesxMessage.getMessage("TEXT-HEADER_LABEL_RESOURCE_PANE", resourceBusiness.getResource()
          .getName()));
    }
    string.append(".");
    return string.toString();
  }

  @Override
  protected void saveValues() {
    resourceBusiness = LesxResourceBusiness
        .of(resourceBusiness.getResource(), new LesxBusiness(((LesxBusiness) getPropertySheet().getComponent()).getPropertyValues()));
    dataModel.addResourceBusiness(resourceBusiness);
  }

  @Override
  protected void setDataModel(ILesxDataModel<? extends LesxComponent> newDataModel) {
    dataModel = (LesxBusinessResourceDataModel) newDataModel;
  }

  @Override
  protected boolean isDuplicate(boolean isCreate) {
    return dataModel.isDuplicate(resourceBusiness, isCreate);
  }

  @Override
  protected String getComponentName() {
    return resourceBusiness.getResource() == null ? "*No Name*" : resourceBusiness.getResource()
        .getName();
  }

  @Override
  protected void setComponent(boolean isCreate) {
    if (isCreate) {
      business = new LesxBusiness();
      business.setId(dataModel.createNewKeyForBusinessIdProperty());
      business.setKey(ELesxPropertyKeys.RESOURCE);
    }
    else {
      business = dataModel.getComponentSelected()
          .getBusiness()
          .clone();
    }
  }

  @Override
  protected LesxBusiness getComponent() {
    return business;
  }

}
