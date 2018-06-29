package lesx.ui.components.dialogs;

import static lesx.property.properties.ELesxUseCase.EDIT;

import javafx.fxml.FXML;
import lesx.datamodel.ILesxDataModel;
import lesx.datamodel.LesxBusinessResourceDataModel;
import lesx.gui.message.LesxMessage;
import lesx.property.properties.ELesxPropertyKeys;
import lesx.property.properties.ELesxUseCase;
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
    setIsCreate(EDIT);
    init(dataModel, EDIT);
  }

  @Override
  public String getTitle() {
    return LesxMessage.getMessage("TEXT-TITLE_RESOURCE_DIALOG_CREATE");
  }

  @Override
  protected String getDescriptionText(ELesxUseCase isCreate) {
    return isCreate == EDIT ? LesxMessage.getMessage("TEXT-DESCRIPTION_LABEL_EDIT_RESOURCE") : LesxMessage.getMessage("TEXT-DESCRIPTION_LABEL_NEW_RESOURCE");
  }

  @Override
  protected String getHeader(ELesxUseCase isCreate) {
    StringBuilder string;
    string = new StringBuilder(128);
    if (isCreate == EDIT) {
      string.append(LesxMessage.getMessage("TEXT-HEADER_LABEL_RESOURCE_PANE", resourceBusiness.getResource()
          .getName()));
    }
    else {
      string.append(LesxMessage.getMessage("TEXT-HEADER_LABEL_RESOURCE_PANE", "Nuevo"));
    }
    string.append(".");
    return string.toString();
  }

  @Override
  protected void saveValues() {
    resourceBusiness = LesxResourceBusiness
        .of(resourceBusiness.getResource(), new LesxBusiness(((LesxBusiness) getPropertySheet().getComponent()).getPropertyValues()));
    dataModel.addResourceBusiness(resourceBusiness);
    dataModel.setComponentSelected(resourceBusiness);
  }

  @Override
  protected void setDataModel(ILesxDataModel<? extends LesxComponent> newDataModel) {
    dataModel = (LesxBusinessResourceDataModel) newDataModel;
  }

  @Override
  protected boolean isDuplicate(ELesxUseCase isCreate) {
    return dataModel.isDuplicate(resourceBusiness, isCreate);
  }

  @Override
  protected String getComponentName() {
    return (resourceBusiness == null || resourceBusiness.getResource() == null) ? "*No Name*" : resourceBusiness.getResource()
        .getName();
  }

  @Override
  protected void setComponent(ELesxUseCase isCreate) {
    if (isCreate == EDIT) {
      business = dataModel.getComponentSelected()
          .getBusiness()
          .clone();
      resourceBusiness = dataModel.getComponentSelected()
          .clone();
    }
    else {
      business = new LesxBusiness();
      business.setId(dataModel.createNewKeyForBusinessIdProperty());
      business.setKey(ELesxPropertyKeys.RESOURCE);
    }
  }

  @Override
  protected LesxBusiness getComponent() {
    return business;
  }

}
