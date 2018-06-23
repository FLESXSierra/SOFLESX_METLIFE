package lesx.ui.components.dialogs;

import javafx.fxml.FXML;
import lesx.datamodel.ILesxDataModel;
import lesx.datamodel.LesxResourcesDataModel;
import lesx.gui.message.LesxMessage;
import lesx.property.properties.ELesxPropertyKeys;
import lesx.property.properties.LesxComponent;
import lesx.property.properties.LesxResource;

public class LesxEditResourceDialog extends LesxEditComponentDialog {

  private LesxResourcesDataModel dataModel;
  private LesxResource resource;

  @FXML
  @Override
  public void initialize() {
    super.initialize();
  }

  @Override
  protected void reInitialize() {
    setIsCreate(false);
    dataModel.setResourceSelected(resource);
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
      string.append(LesxMessage.getMessage("TEXT-HEADER_LABEL_RESOURCE_PANE", resource.getName()));
    }
    string.append(".");
    return string.toString();
  }

  @Override
  protected void saveValues() {
    resource = new LesxResource(((LesxResource) getPropertySheet().getComponent()).getPropertyValues());
    dataModel.addResource(resource);
  }

  @Override
  protected void setDataModel(ILesxDataModel<? extends LesxComponent> newDataModel) {
    dataModel = (LesxResourcesDataModel) newDataModel;
  }

  @Override
  protected boolean isDuplicate(boolean isCreate) {
    return dataModel.isDuplicate(resource, isCreate);
  }

  @Override
  protected String getComponentName() {
    return resource.getName() == null ? "*No Name*" : resource.getName();
  }

  @Override
  protected void setComponent(boolean isCreate) {
    if (isCreate) {
      resource = new LesxResource();
      resource.setId(dataModel.createNewKeyForIdProperty());
      resource.setKey(ELesxPropertyKeys.RESOURCE);
    }
    else {
      resource = dataModel.getComponentSelected();
    }
  }

  @Override
  protected LesxResource getComponent() {
    return resource;
  }

}
