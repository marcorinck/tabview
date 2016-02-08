package my.package.components;

import org.apache.log4j.Logger;

import javax.faces.component.FacesComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;
import java.io.IOException;

@FacesComponent("my.package.components.Tab")
public class Tab extends UINamingContainer {

  public boolean isTabActive() {
    TabView tabView = (TabView) getCompositeComponentParent(this);
  
    return tabView.isTabActive(this);
  }

  public String getTabId() {
      return (String) getAttributes().get("id");
  }
  
  public String getTabLabel() {
    return (String) getAttributes().get("label");
  }
}
