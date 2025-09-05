/*
 *  Origami Solutions
 */

package com.origami.sgr.services.interfaces;
import com.origami.session.WfUserSession;
import com.origami.sgr.entities.AclRol;
import com.origami.sgr.entities.GeDepartamento;
import com.origami.sgr.entities.PubGuiMenu;
import com.origami.sgr.entities.PubGuiMenuRol;
import com.origami.sgr.entities.PubGuiMenuTipoAcceso;
import com.origami.sgr.entities.PubGuiMenubar;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author fernando
 */
@Local
public interface MenuServiceLocal {

    public List<PubGuiMenu> getMenusOrdenados(PubGuiMenu menuPadre);

    public List<PubGuiMenu> getMenusOrdenados(PubGuiMenubar menuBar);

    public PubGuiMenubar getMenuBarTree(String menubarIdent);

    public PubGuiMenubar getMenuBar(String menubarIdent);

    public PubGuiMenu getMenu(Integer idMenu);

    public void menu_init(PubGuiMenu menu);

    public PubGuiMenu menu_associate(PubGuiMenu menu);

    public PubGuiMenubar genMenuStruct(String menubarIdent, WfUserSession userSession);

    //***************** Metodos para mantenimiento de menu *******************//
    public PubGuiMenu guardar(PubGuiMenu menu);

    public List<PubGuiMenubar> getMenuBarList();

    public List<PubGuiMenuTipoAcceso> getMenuAccesoList();

    public List<PubGuiMenuRol> getAccesosMenuRol(PubGuiMenu menu);

    public AclRol getRol(Long idRol);
    
    public String getRolName(Long idRol);

    public List<GeDepartamento> getDepts();

    public List<AclRol> getRolesDepartamento(Long id);

    public Boolean borrarMenu(PubGuiMenu menu);

    public Boolean borrarMenu(PubGuiMenuRol menuRol);

    public PubGuiMenuRol guardarPubGuiMenuRol(PubGuiMenuRol PermisoMenu);
    
}
