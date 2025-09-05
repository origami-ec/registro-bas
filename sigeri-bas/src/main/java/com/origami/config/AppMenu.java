/*
 *  Origami Solutions
 */
package com.origami.config;

import com.origami.session.UserSession;
import com.origami.sgr.entities.PubGuiMenu;
import com.origami.sgr.entities.PubGuiMenubar;
import com.origami.sgr.services.interfaces.MenuCacheLocal;
import com.origami.sgr.services.interfaces.MenuServiceLocal;
import com.origami.sgr.util.JsfUti;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;

/**
 *
 * @author fernando
 */
@Named
@RequestScoped
public class AppMenu {

    @EJB
    protected MenuServiceLocal menuService;
    @EJB
    protected MenuCacheLocal menuCache;

    @Inject
    protected UserSession userSession;

    private MenuModel model;

    @PostConstruct
    public void initView() {
        if (!PrimeFaces.current().isAjaxRequest()) {
            if (model == null) {
                model = new DefaultMenuModel();
            }
            llenarMenu(getMenuWorkflow());
        }
    }

    public void llenarMenu(List<PubGuiMenu> menus) {
        int count = 0;
        model = new DefaultMenuModel();
//        System.out.println("Menus encontrados: " + menus.size());
        for (PubGuiMenu menu : menus) { 
            DefaultSubMenu subMenu = DefaultSubMenu.builder().label(menu.getNombre()).icon(menu.getIcono()).build();
            //DefaultSubMenu subMenu = new DefaultSubMenu(menu.getNombre());
            subMenu.setId("menu_" + count);
            subMenu.setStyleClass("layout-menubar-container");
            
            if (tieneSubmenus(menu)) {
                for (PubGuiMenu itemMenu : menu.getMenusHijos_byNumPosicion()) {
                    DefaultMenuItem item
                            = DefaultMenuItem.builder()
                                    .value(itemMenu.getNombre())
                                    .icon(itemMenu.getIcono())
                                    .url(menuUrl(itemMenu))
                                    .build();
                    item.setIncludeViewParams(false);
                    item.setOnclick("customfunction([{name:'name_', value:'" + itemMenu.getNombre() + "'}, {name:'url_', value:'" + menuUrl(itemMenu) + "'}]); customMenu(event);");
                    subMenu.getElements().add(item);

                }
                model.getElements().add(subMenu);
            }
            count++;
        }
    }

    public void onClic(ActionEvent x) {
        String value = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("name_");
        userSession.setTopPage(value);
        userSession.setTitlePage(value);
    }

    public List<PubGuiMenu> getMenuWorkflow() {
        PubGuiMenubar menubar = menuService.genMenuStruct("workflow.main", userSession);
        return menubar.getMenuListSoyMenubar_byOrden();
    }

    public Boolean tieneSubmenus(PubGuiMenu menu) {
        if (menu.getMenusHijos_byNumPosicion() != null
                && menu.getMenusHijos_byNumPosicion().size() > 0) {
            return true;
        }
        return false;
    }

    public void clearMenuWorkflow() {
        menuCache.clearCache("workflow.main");
    }

    public String menuUrl(PubGuiMenu menu) {

        if (menu.getHrefUrl() != null && !menu.getHrefUrl().equals("") && !menu.getHrefUrl().equals("#")) {
            if (menu.getHrefUrl().startsWith("http://")) {
                return menu.getHrefUrl();
            }
            if (menu.getHrefUrl().startsWith("/")) {
                return JsfUti.getHostContextUrl() + menu.getHrefUrl().substring(1);
            } else {
                return JsfUti.getHostContextUrl() + menu.getHrefUrl();
            }

        }
        return "#";
    }

    /**
     * Creates a new instance of AppMenu
     */
    public AppMenu() {
    }

    public UserSession getUserSession() {
        return userSession;
    }

    public void setUserSession(UserSession userSession) {
        this.userSession = userSession;
    }

    public MenuModel getModel() {
        return model;
    }

    public void setModel(MenuModel model) {
        this.model = model;
    }

}
