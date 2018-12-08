package com.dream.codec.tlv.utils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

/**
 * Reflection Utils
 *
 * @author DreamJM
 */
public class ReflectionUtils {

    /**
     * Get Classes by package name
     *
     * @param packageName package name
     * @return classes
     */
    public static List<Class<?>> getClasses(String packageName) {
        List<Class<?>> classes = new ArrayList<>();
        String packageDirName = packageName.replace('.', '/');
        Enumeration<URL> dirs;
        try {
            dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
            while (dirs.hasMoreElements()) {
                URL url = dirs.nextElement();
                String protocol = url.getProtocol();
                if ("file".equals(protocol)) {
                    String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
                    findAndAddClassesInPackageByFile(packageName, filePath, classes);
                } else if ("jar".equals(protocol)) {
                    JarFile jar = ((JarURLConnection) url.openConnection()).getJarFile();
                    Enumeration<JarEntry> entries = jar.entries();
                    while (entries.hasMoreElements()) {
                        JarEntry entry = entries.nextElement();
                        String name = entry.getName();
                        if (name.charAt(0) == '/') {
                            name = name.substring(1);
                        }
                        if (name.startsWith(packageDirName)) {
                            int idx = name.lastIndexOf('/');
                            if (idx != -1) {
                                packageName = name.substring(0, idx).replace('/', '.');
                                if (name.endsWith(".class") && !entry.isDirectory()) {
                                    String className = name.substring(packageName.length() + 1, name.length() - 1);
                                    try {
                                        classes.add(Class.forName(packageName + "." + className));
                                    } catch (ClassNotFoundException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return classes;
    }

    private static void findAndAddClassesInPackageByFile(String packageName, String packagePath, List<Class<?>> classes) {
        File dir = new File(packagePath);
        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }
        File[] dirFiles = dir.listFiles(file -> file.isDirectory() || file.getName().endsWith(".class"));
        if (dirFiles == null) {
            return;
        }
        for (File file : dirFiles) {
            if (file.isDirectory()) {
                findAndAddClassesInPackageByFile(packageName + "." + file.getName(), file.getAbsolutePath(), classes);
            } else {
                String className = file.getName().substring(0, file.getName().length() - 6);
                try {
                    classes.add(Class.forName(packageName + '.' + className));
                } catch (ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    /**
     * Get all fields of specific class
     *
     * @param clazz class
     * @return all fields
     */
    public static Field[] loadAllFields(Class<?> clazz) {
        List<Field> allFields = new ArrayList<>();
        for (Class searchType = clazz; Object.class != searchType && searchType != null; searchType = searchType.getSuperclass()) {
            Field[] fields = searchType.getDeclaredFields();
            allFields.addAll(Arrays.stream(fields).filter(field -> !Modifier.isStatic(field.getModifiers())).collect(Collectors.toList()));
        }
        return allFields.toArray(new Field[]{});
    }

    /**
     * Get specific field value from object
     *
     * @param field  field
     * @param object object
     * @return field value
     * @throws IllegalAccessException Reflection Exception
     */
    public static Object getField(Field field, Object object) throws IllegalAccessException {
        return field.get(object);
    }

    /**
     * Set Field value of specific object
     *
     * @param field  Field
     * @param object Object
     * @param value  Field Value
     * @throws IllegalAccessException Reflection Exception
     */
    public static void setField(Field field, Object object, Object value) throws IllegalAccessException {
        field.set(object, value);
    }

    /**
     * Parse Sub Type form List or Array
     *
     * @param field List or Array field
     * @return Sub Type of List or Array field
     */
    public static Class<?> parseSubType(Field field) {
        Class<?> type = field.getType();
        if (type.isArray()) {
            return type.getComponentType();
        } else if (List.class == type) {
            Type genericType = field.getGenericType();
            if (genericType instanceof ParameterizedType) {
                return (Class<?>) ((ParameterizedType) genericType).getActualTypeArguments()[0];
            }
        }
        return null;
    }

}
