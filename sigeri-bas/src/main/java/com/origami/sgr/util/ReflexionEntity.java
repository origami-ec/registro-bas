/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.util;

import com.origami.sgr.bpm.models.ListCollectionsReff;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import org.hibernate.collection.internal.PersistentBag;

/**
 *
 * @author Angel Navarro
 */
public class ReflexionEntity implements Serializable {

    /**
     *
     * @param <T> Object
     * @param entity Nombre de La entity
     * @return Si la entity se encuentra en el paquete
     * "com.origami.sgm.entities" retorna la clase requerida.
     */
    public static <T> Class entityClass(String entity) {
        try {
            return Class.forName("com.origami.rpp.entities." + entity);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ReflexionEntity.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Busca la clave primaria de la Clase lo busca por la anotación {@link Id}
     *
     * @param entry Clase a obtener a el id
     * @return Valor del campo como un {@link String}
     */
    public static String getIdEntity(Object entry) {
        Object entity = null;
        if (entry instanceof PersistentBag) {
            PersistentBag bag = (PersistentBag) entry;
            if (bag.size() > 0) {
                entity = bag.get(0);
            }
        } else {
            entity = entry;
        }

        if (entity != null) {
            String packages = entity.getClass().getPackage().getName().concat(".");
            String name = entity.getClass().getSimpleName();
            Field fiel[] = entity.getClass().getDeclaredFields();
            for (Field f : fiel) {
                if (isAnnotationType(f, Id.class)) {
                    try {
                        f.setAccessible(true);
                        Object value = f.get(entity);
                        if (value != null) {
                            return packages.concat(name).concat(":").concat(value.toString()).concat(":")
                                    .concat(value.getClass().getName());
                        } else {
                            return null;
                        }
                    } catch (IllegalArgumentException | IllegalAccessException ex) {
                        Logger.getLogger(ReflexionEntity.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        return null;
    }

    public static Object instanceConsString(String type, String value) {
        Object obj = null;
        try {
            Class clazz = Class.forName(type);

            for (java.lang.reflect.Constructor con : clazz.getConstructors()) {
                for (Class parameterType : con.getParameterTypes()) {
                    if (parameterType.equals(String.class)) {
                        if (con.getParameterTypes().length == 1) {
                            obj = con.newInstance(value);
                            break;
                        }
                    }
                }
            }
        } catch (ClassNotFoundException | IllegalAccessException | IllegalArgumentException | InstantiationException
                | SecurityException | InvocationTargetException e) {
            return null;
        }
        return obj;
    }

    /**
     * Busca la clave primaria de la Clase lo busca por la anotación {@link Id}
     *
     * Obtiene el valor del id de la {@code entity}
     *
     * @param entity Clase a obtener a el id
     * @return Valor del campo
     */
    public static Object getIdFromEntity(Object entity) {
        if (entity != null) {
            Field fiel[] = entity.getClass().getDeclaredFields();
            for (Field f : fiel) {
                if (isAnnotationType(f, Id.class)) {
                    try {
                        f.setAccessible(true);
                        Object value = f.get(entity);
                        if (value != null) {
                            return f.get(entity);
                        } else {
                            return null;
                        }
                    } catch (IllegalArgumentException | IllegalAccessException ex) {
                        Logger.getLogger(ReflexionEntity.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        return null;
    }

    /**
     * Compara le annotation de tipo string.
     *
     * @param field Campo a comparar la annotation.
     * @param annotation Annotation ejemplo: "@javax.persistence.Id()"
     * @return true si esta presente la annotation caso contrario false.
     */
    public static Boolean isAnnotationType(Field field, String annotation) {
        Annotation anotacion[] = field.getAnnotations();
        for (Annotation anot : anotacion) {
            if (anot.toString().equals(annotation)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Compara si existe la annotation de tipo class.
     *
     * @param field campoAsoc a verificar la annotation.
     * @param clazz Id.class
     * @return true si esta presente la annotation caso contrario false.
     */
    public static Boolean isAnnotationType(Field field, Class clazz) {
        return field.isAnnotationPresent(clazz);
    }

    public static Boolean isAnnotationPresent(Object entity, Class clazz) {
        return entity.getClass().isAnnotationPresent(clazz);
    }

    public static Boolean isAnnotationTypeObject(Object entity, String annotation) {
        if (entity != null) {
            Field fiel[] = entity.getClass().getDeclaredFields();
            for (Field f : fiel) {
                return isAnnotationType(f, annotation);
            }
        }
        return false;
    }

    /**
     *
     * @param entity Object
     * @param annotation Object
     * @return Object
     */
    public static Boolean isAnnotationTypeObject(Object entity, Class annotation) {
        if (entity != null) {
            Field fiel[] = entity.getClass().getDeclaredFields();
            for (Field f : fiel) {
                return isAnnotationType(f, annotation);
            }
        }
        return false;
    }

    public static String isAnnotationTypeObjects(Object entity, String annotation) {
        if (entity != null) {
            Field fiel[] = entity.getClass().getDeclaredFields();
            for (Field f : fiel) {
                if (isAnnotationType(f, annotation)) {
                    return f.getName();
                }
            }
        }
        return null;
    }

    public static Boolean isAnnotationType(Field field, Annotation anotacion) {
        Annotation anotacions[] = field.getAnnotations();
        for (Annotation anot : anotacions) {
//            if (anot instanceof Id) {
            if (anot == anotacion) {
                return true;
            }
        }
        return false;
    }

    public static Object emptyInstance(String type) {
        Object obj = null;
        try {
            Class clazz = Class.forName(type);

            for (java.lang.reflect.Constructor con : clazz.getConstructors()) {
                if (con.getParameterTypes().length == 0) {
                    obj = con.newInstance();
                    break;
                }
            }
        } catch (ClassNotFoundException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            return null;
        }
        return obj;
    }

    /**
     * Obtiene todos los campo del objecto y busca los que sean del tipo
     * {@code aClass}, si el campo tiene una lista que no este vacia lo agrega a
     * una lista de tipo {@link ListCollectionsReff}, ademas obtiene el nombre
     * del campo que esta relacionado con CatEnte
     *
     * @param e Objecto para obtener los Campos.
     * @param aClass Clase de tipo de elemento que se desea obtener los campos.
     * ejempo: {@link Collection}, {@link List}
     * @return Lista de {@link ListCollectionsReff}
     */
    public static List<ListCollectionsReff> getCampos(Object e, Class<?> aClass) {
        Field[] campos = e.getClass().getDeclaredFields();
        List<ListCollectionsReff> list = new ArrayList<>();
        for (Field campo : campos) {
            if (equalsFieldType(campo, aClass)) {
                try {
                    campo.setAccessible(true);
                    List<?> l = (List<?>) campo.get(e);
                    if (Utils.isNotEmpty(l)) {
                        ListCollectionsReff model = new ListCollectionsReff();
                        OneToMany asociacion = (OneToMany) getAnnotation(campo, OneToMany.class);
                        model.setCampoAsociado(asociacion.mappedBy());
                        model.setElementos(l);
                        list.add(model);
                    }

                } catch (IllegalArgumentException | IllegalAccessException ex) {
                    Logger.getLogger(ReflexionEntity.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return list;
    }

    public static List<?> getCamposByType(Object e, Class<?> aClass) {
        Field[] campos = e.getClass().getDeclaredFields();
        List<Object> list = new ArrayList<>();
        for (Field campo : campos) {
            if (equalsFieldType(campo, aClass)) {
                try {
                    campo.setAccessible(true);
                    Object l = campo.get(e);
                    if (l != null) {
                        list.add(l);
                    }
                } catch (IllegalArgumentException | IllegalAccessException ex) {
                    Logger.getLogger(ReflexionEntity.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return list;
    }

    public static <T> T getCampo(T e, String campo) {
        try {
            Field campoAsoc = e.getClass().getDeclaredField(campo);
            campoAsoc.setAccessible(true);
            Object l = campoAsoc.get(e);
            if (l != null) {
                return (T) l;
            }
        } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException ex) {
            Logger.getLogger(ReflexionEntity.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Buscar en {@code e} el campo que se le pasa como parametro {@code campo}
     * y se setea el valor {@code value},
     *
     * @param e Entity.
     * @param campo Campo al que de se va realizar El cambio del valor
     * @param value Valor que se asignara al campo.
     * @return true cuando el cambio se realizo correctamente caso contrario
     * false.
     */
    public static Boolean setCampo(Object e, String campo, Object value) {
        try {
            Field campoAsoc = e.getClass().getDeclaredField(campo);
            campoAsoc.setAccessible(true);
            if (value == null) {
                campoAsoc.set(e, value);
            } else {
                if (campoAsoc.getType() == value.getClass()) {
                    campoAsoc.set(e, value);
                } else {
                    campoAsoc.set(e, ReflexionEntity.getIdFromEntity(value));
                }
            }
            return true;
        } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException ex) {
            Logger.getLogger(ReflexionEntity.class.getName()).log(Level.SEVERE, e.getClass().getName(), ex);
        }
        return false;
    }

    public static Boolean equalsFieldType(Field field, Class typeClass) {
        return field.getType().equals(typeClass);
    }

    public static Class classForName(String name) throws ClassNotFoundException {
        try {
            ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
            if (contextClassLoader != null) {
                return contextClassLoader.loadClass(name);
            }
        } catch (Throwable ignore) {
        }
        return Class.forName(name);
    }

    /**
     * Obtiene la Annotation que se le pasa como parametro.
     *
     * @param object Entity a obtener la Annotation.
     * @param aClass Clase de la Annotation
     * @return Annotation
     */
    public static Annotation getAnnotation(Object object, Class aClass) {
        if (object.getClass().isAnnotationPresent(aClass)) {
            Annotation annotation = object.getClass().getAnnotation(aClass);
            return annotation;
        }
        return null;
    }

    /**
     * Obtiene la Annotation del campoAsoc que se le pasa como parametro
     *
     * @param object Object
     * @param aClass Object
     * @return Object
     */
    public static Annotation getAnnotation(Field object, Class aClass) {
        if (object.isAnnotationPresent(aClass)) {
            Annotation annotation2 = object.getAnnotation(aClass);
            Annotation annotation = annotation2;
            return annotation;
        }
        return null;
    }

    public static List<Class<?>> getPagesForPackage(final String packagename) throws ClassNotFoundException {
        // This will hold a list of directories matching the packagename. There may be more than one if a package is split over multiple jars/paths
        ArrayList<File> directories = new ArrayList<>();
        try {
            ClassLoader cld = Thread.currentThread().getContextClassLoader();
            if (cld == null) {
                throw new ClassNotFoundException("Can't get class loader.");
            }
            String path = packagename.replace('.', '/');
            // Ask for all resources for the path
            Enumeration<URL> resources = cld.getResources(path);
            while (resources.hasMoreElements()) {
                directories.add(new File(URLDecoder.decode(resources.nextElement().getPath(), "UTF-8")));
            }
        } catch (NullPointerException x) {
            throw new ClassNotFoundException(packagename + " does not appear to be a valid package (Null pointer exception)");
        } catch (UnsupportedEncodingException encex) {
            throw new ClassNotFoundException(packagename + " does not appear to be a valid package (Unsupported encoding)");
        } catch (IOException ioex) {
            throw new ClassNotFoundException("IOException was thrown when trying to get all resources for " + packagename);
        }

        ArrayList<Class<?>> classes = new ArrayList<Class<?>>();
        // For every directory identified capture all the .class files
        for (int i = 0; i < directories.size(); i++) {
            if (((File) directories.get(i)).exists()) {
                // Get the list of the files contained in the package
                String[] files = ((File) directories.get(i)).list();
                for (int j = 0; j < files.length; j++) {
                    // we are only interested in .class files
                    if (files[j].endsWith(".xhtml")) {
                        // removes the .class extension
                        classes.add(Class.forName(packagename + '.' + files[j].substring(0, files[j].length() - 6)));
                    }
                }
            } else {
                throw new ClassNotFoundException(packagename + " (" + ((File) directories.get(i)).getPath() + ") does not appear to be a valid package");
            }
        }
        return classes;
    }

    public static String getNameIdEntity(Class entity) {
        if (entity != null) {
            Field fiel[] = entity.getDeclaredFields();
            for (Field f : fiel) {
                if (isAnnotationType(f, Id.class)) {
                    try {
                        return f.getName();
                    } catch (IllegalArgumentException ex) {
                        Logger.getLogger(ReflexionEntity.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        return null;
    }

    public static Object instanceConsString(Class type, String value) {
        Object obj = null;
        try {
            Class clazz = type;

            for (java.lang.reflect.Constructor con : clazz.getConstructors()) {
                for (Class parameterType : con.getParameterTypes()) {
                    if (parameterType.equals(String.class)) {
                        if (con.getParameterTypes().length == 1) {
                            obj = con.newInstance(value);
                            break;
                        }
                    }
                }
            }
        } catch (IllegalAccessException | IllegalArgumentException | InstantiationException | SecurityException | InvocationTargetException e) {
            return null;
        }
        return obj;
    }

    public static Object getTypeObject(Class clazz, String nameField) {
        try {
            Field temp = null;
            Object ob = null;
            if (nameField.contains(".")) {
                String[] split = nameField.split("\\.");
                for (String sp : split) {
                    if (clazz == null) {
                        System.out.println(nameField + " >> " + sp);
                        return null;
                    }
                    temp = clazz.getDeclaredField(sp);
                    if (temp.getType().equals(Collection.class)) {
                        ParameterizedType ct = (ParameterizedType) temp.getGenericType();
                        clazz = (Class) ct.getActualTypeArguments()[0];
                    } else {
                        clazz = temp.getType();
                    }
                }
                return clazz;
            } else {
                return clazz.getDeclaredField(nameField).getGenericType();
            }
        } catch (NoSuchFieldException | SecurityException e) {
            //Logger.getLogger(ReflexionEntity.class.getName()).log(Level.SEVERE, null, e);
            System.out.println("Warning: Field not found > " + nameField + " into class > " + clazz.getName());
        }
        return null;
    }
}
