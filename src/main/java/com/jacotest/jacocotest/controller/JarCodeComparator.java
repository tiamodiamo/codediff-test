package com.jacotest.jacocotest.controller;


import org.apache.bcel.classfile.*;
import org.apache.bcel.generic.ClassGen;
import org.apache.bcel.util.ClassPath;
import org.apache.bcel.util.SyntheticRepository;

import java.io.IOException;
import java.util.*;

public class JarCodeComparator {

    public static void main(String[] args) throws IOException {
        String oldJarPath = "D:\\workspace\\codediff-test\\src\\main\\java\\com\\jacotest\\lib\\jacocotest-0.0.1-base-SNAPSHOT.jar";
        String newJarPath = "D:\\workspace\\codediff-test\\src\\main\\java\\com\\jacotest\\lib\\jacocotest-0.0.1-SNAPSHOT.jar";

        List<JarDifference> differences = compareJars(oldJarPath, newJarPath);

        String json = convertDifferencesToJson(differences);

        System.out.println(json);
    }

    public static List<JarDifference> compareJars(String oldJarPath, String newJarPath) throws IOException {
        Map<String, JavaClass> oldClasses = getClassMap(oldJarPath);
        Map<String, JavaClass> newClasses = getClassMap(newJarPath);

        List<JarDifference> differences = new ArrayList<>();

        for (String className : oldClasses.keySet()) {
            JarDifference jarDifference = new JarDifference();
            jarDifference.setModuleName(className);

            if (newClasses.containsKey(className)) {
                JavaClass oldClass = oldClasses.get(className);
                JavaClass newClass = newClasses.get(className);

                List<MethodDifference> methodDifferences = compareMethods(oldClass.getMethods(), newClass.getMethods());
                jarDifference.setMethodDifferences(methodDifferences);

                List<CodeDifference> codeDifferences = compareCodes(oldClass, newClass);
                jarDifference.setCodeDifferences(codeDifferences);

                if (!methodDifferences.isEmpty() || !codeDifferences.isEmpty()) {
                    jarDifference.setType("MODIFY");
                }
            } else {
                jarDifference.setType("DELETE");
            }

            differences.add(jarDifference);
        }

        for (String className : newClasses.keySet()) {
            if (!oldClasses.containsKey(className)) {
                JarDifference jarDifference = new JarDifference();
                jarDifference.setModuleName(className);
                jarDifference.setType("ADD");
                differences.add(jarDifference);
            }
        }

        return differences;
    }

    private static Map<String, JavaClass> getClassMap(String jarPath) throws IOException {
        Map<String, JavaClass> classMap = new HashMap<>();
        ClassParser parser;
        try (JavaClassReader jarReader = new JavaClassReader(jarPath)) {
            while ((parser = jarReader.nextClass()) != null) {
                JavaClass javaClass = parser.parse();
                classMap.put(javaClass.getClassName(), javaClass);
            }
        }
        return classMap;
    }

    private static List<MethodDifference> compareMethods(Method[] oldMethods, Method[] newMethods) {
        List<MethodDifference> differences = new ArrayList<>();
        // 实现比较方法的逻辑，判断新增、删除、修改的方法，并添加到 differences 中
        return differences;
    }

    private static List<CodeDifference> compareCodes(JavaClass oldClass, JavaClass newClass) {
        List<CodeDifference> differences = new ArrayList<>();
        // 实现比较代码的逻辑，判断新增、删除、修改的代码行，并添加到 differences 中
        return differences;
    }

    private static String convertDifferencesToJson(List<JarDifference> differences) {
        StringBuilder jsonBuilder = new StringBuilder("[");
        for (JarDifference difference : differences) {
            jsonBuilder.append("{\"moduleName\":\"").append(difference.getModuleName()).append("\",\"type\":\"").append(difference.getType()).append("\",\"methodInfos\":[");
            for (MethodDifference methodDifference : difference.getMethodDifferences()) {
                jsonBuilder.append("{\"methodName\":\"").append(methodDifference.getMethodName()).append("\",\"parameters\":").append(methodDifference.getParameters()).append(",\"startLine\":").append(methodDifference.getStartLine()).append(",\"endLine\":").append(methodDifference.getEndLine()).append("},");
            }
            if (!difference.getMethodDifferences().isEmpty()) {
                jsonBuilder.deleteCharAt(jsonBuilder.length() - 1); // 移除最后一个逗号
            }
            jsonBuilder.append("],\"lines\":[");
            for (CodeDifference codeDifference : difference.getCodeDifferences()) {
                jsonBuilder.append("{\"type\":\"").append(codeDifference.getType()).append("\",\"startLineNum\":").append(codeDifference.getStartLineNum()).append(",\"endLineNum\":").append(codeDifference.getEndLineNum()).append("},");
            }
            if (!difference.getCodeDifferences().isEmpty()) {
                jsonBuilder.deleteCharAt(jsonBuilder.length() - 1); // 移除最后一个逗号
            }
            jsonBuilder.append("]},");
        }
        if (!differences.isEmpty()) {
            jsonBuilder.deleteCharAt(jsonBuilder.length() - 1); // 移除最后一个逗号
        }
        jsonBuilder.append("]");
        return jsonBuilder.toString();
    }
}

class JavaClassReader implements AutoCloseable {
    private final SyntheticRepository repository;
    private final ClassParser[] parsers;
    private int index;

    public  JavaClassReader(String jarPath) throws IOException {
        this.repository = SyntheticRepository.getInstance(new ClassPath(jarPath));
        ServiceLoader<ClassPath> loader = ServiceLoader.load(ClassPath.class);

        //this.parsers = new ClassParser[this.repository.getClassPath().length];
        ClassPath classPath = this.repository.getClassPath();


        this.parsers = new ClassParser[1];
        for (int i = 0; i < this.parsers.length; i++) {
           // this.parsers[i] = new ClassParser(this.repository.getClassPath()[i]);
        }
        this.parsers[0] = new ClassParser(jarPath);
    }

    public ClassParser nextClass() throws IOException {
        if (index < parsers.length) {
            return parsers[index++];
        } else {
            return null;
        }
    }

    @Override
    public void close() throws IOException {
        // 关闭资源
    }
}

class JarDifference {
    private String moduleName;
    private String type;
    private List<MethodDifference> methodDifferences = new ArrayList<>();
    private List<CodeDifference> codeDifferences = new ArrayList<>();

    // Getters and setters

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<MethodDifference> getMethodDifferences() {
        return methodDifferences;
    }

    public void setMethodDifferences(List<MethodDifference> methodDifferences) {
        this.methodDifferences = methodDifferences;
    }

    public List<CodeDifference> getCodeDifferences() {
        return codeDifferences;
    }

    public void setCodeDifferences(List<CodeDifference> codeDifferences) {
        this.codeDifferences = codeDifferences;
    }
}

class MethodDifference {
    private String methodName;
    private List<String> parameters;
    private int startLine;
    private int endLine;

    // Getters and setters

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public List<String> getParameters() {
        return parameters;
    }

    public void setParameters(List<String> parameters) {
        this.parameters = parameters;
    }

    public int getStartLine() {
        return startLine;
    }

    public void setStartLine(int startLine) {
        this.startLine = startLine;
    }

    public int getEndLine() {
        return endLine;
    }

    public void setEndLine(int endLine) {
        this.endLine = endLine;
    }
}

class CodeDifference {
    private String type;
    private int startLineNum;
    private int endLineNum;

    // Getters and setters

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getStartLineNum() {
        return startLineNum;
    }

    public void setStartLineNum(int startLineNum) {
        this.startLineNum = startLineNum;
    }

    public int getEndLineNum() {
        return endLineNum;
    }

    public void setEndLineNum(int endLineNum) {
        this.endLineNum = endLineNum;
    }
}

