/*
 *  Origami Solutions
 */

package com.origami.sgr.services.interfaces;
import com.origami.sgr.entities.PubGuiMenubar;
import javax.ejb.Local;

/**
 *
 * @author fernando
 */
@Local
public interface MenuCacheLocal {

    public PubGuiMenubar getMenuBar(String identificador);

    public void clearCache(String identificador);
    
}
