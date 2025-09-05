/*
 *  Origami Solutions
 */
package com.origami.sgr.services.ejbs;

import com.origami.sgr.entities.PubGuiMenubar;
import com.origami.sgr.services.interfaces.MenuCacheLocal;
import com.origami.sgr.services.interfaces.MenuServiceLocal;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import javax.annotation.PostConstruct;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import com.origami.sgr.util.EntityBeanCopy;

/**
 *
 * @author fernando
 */
@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
public class MenuCache implements MenuCacheLocal {

    @EJB
    private MenuServiceLocal menuService;

    protected ConcurrentMap<String, PubGuiMenubar> menubarsMap;
    protected ConcurrentMap<String, String> lockerMap = new ConcurrentHashMap<>();

    /**
     *
     * @param identificador Object
     */
    @Override
    public void clearCache(String identificador) {
        menubarsMap.remove(identificador);
    }

    /**
     * devuelve el menuBar correspondiente, en caso de no estar cacheado lo
     * genera.
     *
     * @param identificador Object
     * @return Object
     */
    @Override
    public PubGuiMenubar getMenuBar(String identificador) {
        if (identificador == null) {
            return null;
        }

        PubGuiMenubar menubar1 = menubarsMap.get(identificador);
        // si no ha sido inicializado, hacerlo:
        if (menubar1 == null) {
            generarMenubar(identificador);
            return getMenuBar(identificador);
        }
        PubGuiMenubar menu = (PubGuiMenubar) EntityBeanCopy.clone(menubar1);
        return menu;
    }

    protected void generarMenubar(String identificador) {

        synchronized (getLockerObject(identificador)) {

            // comprobar si no se entró en espera y ya existe el identificador mapeado:
            PubGuiMenubar menubar1 = menubarsMap.get(identificador);
            if (menubar1 != null) {
                
            } else {
                this.loadMenuBar(identificador);
            }

        }
    }

    private void loadMenuBar(String identificador) {

        PubGuiMenubar menuBar1 = menuService.getMenuBarTree(identificador);
        
        // si se encontro menubar, realizar la clonacion
        if (menuBar1 != null) {
            PubGuiMenubar menubar2 = (PubGuiMenubar) EntityBeanCopy.clone(menuBar1);
            menubarsMap.putIfAbsent(identificador, menubar2);
        }
    }

    protected String getLockerObject(String identificador) {
        lockerMap.putIfAbsent(identificador, identificador);

        return lockerMap.get(identificador);
    }

    /**
     * Inicializa el map de menubars en vacio
     */
    protected void initMenubarsMap() {
        this.menubarsMap = new ConcurrentHashMap<>();
    }

    /**
     * Inicializa el singleton ejb
     */
    @PostConstruct
    protected void init() {
        this.initMenubarsMap();
    }

}
