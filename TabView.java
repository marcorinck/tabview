package my.package.components;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import javax.el.ELContext;
import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@FacesComponent("my.package.components.TabView")
public class TabView extends UINamingContainer {

  public void makeTabActive(Tab activeTab) {
    FacesContext facesContext = getFacesContext();
    ELContext elContext = facesContext.getELContext();
  
    //getAttributes().put("activeTab", activeTab); <-- this should call setter of property on managed bean, at least in my jsf installation it does not work

    //so we use this longer method instead     
    ValueExpression valueExpression = facesContext.getApplication().getExpressionFactory()
         .createValueExpression(elContext, "#{cc.attrs.activeTab}", String.class);
    valueExpression.setValue(elContext, activeTab.getTabId());
  
    MethodExpression onTabChange = (MethodExpression) getAttributes().get("onTabChange");
    if (onTabChange != null) {
      onTabChange.invoke(elContext, new Object[]{activeTab.getTabId()});
    }
  }

  @Override
  public void encodeBegin(FacesContext context) throws IOException {
    super.encodeBegin(context);
    ResponseWriter writer = context.getResponseWriter();

    writer.startElement("div", this);
    writer.writeAttribute("id", this.getClientId(), null);
  }

  @Override
  public void encodeEnd(FacesContext context) throws IOException {
    List<UIComponent> children = getChildren();
    ResponseWriter writer = context.getResponseWriter();

    writer.startElement("div", this);
    writer.writeAttribute("class", "tab-content", null);

    if (children != null) {
       for (UIComponent child : children) {
          if (child.isRendered() && !(child instanceof SidebarLinks)) {
             child.encodeAll(context);
          }
       }
    }

    writer.endElement("div");
    writer.endElement("div"); //wurde in encodeBegin() angefangen!

    super.encodeEnd(context);
  }

  public List<Tab> getTabs() {
    List<Tab> tabs = new ArrayList<>();
    List<UIComponent> children = getChildren();

    if (CollectionUtils.isNotEmpty(children)) {
      for (UIComponent child : children) {
         if (child.isRendered() && child instanceof Tab) {
            tabs.add((Tab) child);
         }
      }
    }

    return tabs;
  }

  public String getCurrentActiveTab() {
    return (String) getAttributes().get("activeTab");
  }

  public boolean isTabActive(Tab tab) {
    return StringUtils.equals(getCurrentActiveTab(), tab.getTabId());
  }
}
