package com.jacotest.jacocotest.controller;




import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.Code;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class JarCodeDiff {

    public static void main(String[] args) throws IOException {
        String oldJarPath = "D:\\workspace\\codediff-test\\src\\main\\java\\com\\jacotest\\lib\\jacocotest-0.0.1-base-SNAPSHOT.jar";
        String newJarPath = "D:\\workspace\\codediff-test\\src\\main\\java\\com\\jacotest\\lib\\jacocotest-0.0.1-SNAPSHOT.jar";

        Map<String, JavaClass> oldClasses = getClassMap(oldJarPath);
        Map<String, JavaClass> newClasses = getClassMap(newJarPath);

        for (String className : oldClasses.keySet()) {
            if (newClasses.containsKey(className)) {
                compareClasses(oldClasses.get(className), newClasses.get(className));
            } else {
                System.out.println("Class removed: " + className);
            }
        }

        for (String className : newClasses.keySet()) {
            if (!oldClasses.containsKey(className)) {
                System.out.println("Class added: " + className);
            }
        }
    }

    private static Map<String, JavaClass> getClassMap(String jarPath) throws IOException {
        Map<String, JavaClass> classMap = new HashMap<>();
        try (JarFile jarFile = new JarFile(jarPath)) {
            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                if (entry.getName().endsWith(".class")) {
                    ClassParser parser = new ClassParser(jarPath, entry.getName());
                    JavaClass javaClass = parser.parse();
                    classMap.put(javaClass.getClassName(), javaClass);
                }
            }
        }
        return classMap;
    }

    private static void compareClasses(JavaClass oldClass, JavaClass newClass) {
        if (!oldClass.getSuperclassName().equals(newClass.getSuperclassName())) {
            System.out.println("Superclass changed for class: " + oldClass.getClassName());
        }

        Method[] oldMethods = oldClass.getMethods();
        Method[] newMethods = newClass.getMethods();

        for (int i = 0; i < newMethods.length; i++) {
            Code code = newMethods[i].getCode();
            System.out.println("newCode.toString() = " + code.toString());

        }
        for (int i = 0; i < oldMethods.length; i++) {
            Code code = oldMethods[i].getCode();
            System.out.println("oldCode.toString() = " + code.toString());


        }
        // Additional detailed comparison can be added here (method signature, bytecode, etc.)
    }
}


