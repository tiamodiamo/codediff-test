package com.jacotest.jacocotest.controller;

import jdk.internal.org.objectweb.asm.ClassReader;
import jdk.internal.org.objectweb.asm.tree.ClassNode;
import jdk.internal.org.objectweb.asm.tree.InsnList;
import jdk.internal.org.objectweb.asm.tree.MethodNode;



import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class CodediffOutJson{

    public static void main(String[] args) throws IOException {
        String oldJarPath = "D:\\workspace\\codediff-test\\src\\main\\java\\com\\jacotest\\lib\\jacocotest-0.0.1-base-SNAPSHOT.jar";
        String newJarPath = "D:\\workspace\\codediff-test\\src\\main\\java\\com\\jacotest\\lib\\jacocotest-0.0.1-SNAPSHOT.jar";

        List<Map<String, Object>> diffs = compareJars(oldJarPath, newJarPath);

        // 输出 JSON 格式的差异信息
        System.out.println(diffs);
    }

    private static List<Map<String, Object>> compareJars(String oldJarPath, String newJarPath) throws IOException {
        List<Map<String, Object>> diffs = new ArrayList<>();

        Map<String, ClassNode> oldClasses = getClassNodes(oldJarPath);
        Map<String, ClassNode> newClasses = getClassNodes(newJarPath);

        for (String className : oldClasses.keySet()) {
            ClassNode oldClass = oldClasses.get(className);
            if (newClasses.containsKey(className)) {
                ClassNode newClass = newClasses.get(className);
                List<Map<String, Object>> classDiffs = compareClasses(oldClass, newClass);
                if (!classDiffs.isEmpty()) {
                    Map<String, Object> diff = new HashMap<>();
                    diff.put("moduleName", "jeecg-module-system");
                    diff.put("type", "MODIFY");
                    diff.put("classFile", "/" + className.replace(".", "/") + ".class");
                    diff.put("methodInfos", getMethodInfos(classDiffs));
                    diff.put("lines", getLineInfos(classDiffs));
                    diffs.add(diff);
                }
            }
        }

        return diffs;
    }

    private static Map<String, ClassNode> getClassNodes(String jarPath) throws IOException {
        Map<String, ClassNode> classNodes = new HashMap<>();
        try (JarFile jarFile = new JarFile(jarPath)) {
            for (JarEntry entry : Collections.list(jarFile.entries())) {
                if (entry.getName().endsWith(".class")) {
                    InputStream inputStream = jarFile.getInputStream(entry);
                    ClassReader classReader = new ClassReader(inputStream);
                    ClassNode classNode = new ClassNode();
                    classReader.accept(classNode, 0);
                    classNodes.put(classNode.name, classNode);
                }
            }
        }
        return classNodes;
    }

    private static List<Map<String, Object>> compareClasses(ClassNode oldClass, ClassNode newClass) {
        List<Map<String, Object>> diffs = new ArrayList<>();

        // 比较方法差异
        for (Object oldMethod : oldClass.methods) {
            MethodNode oldMethodNode = (MethodNode) oldMethod;
            for (Object newMethod : newClass.methods) {
                MethodNode newMethodNode = (MethodNode) newMethod;
                if (oldMethodNode.name.equals(newMethodNode.name) && oldMethodNode.desc.equals(newMethodNode.desc)) {
                    List<Map<String, Object>> methodDiffs = compareMethods(oldMethodNode, newMethodNode);
                    if (!methodDiffs.isEmpty()) {
                        diffs.addAll(methodDiffs);
                    }
                }
            }
        }

        return diffs;
    }

    private static List<Map<String, Object>> compareMethods(MethodNode oldMethod, MethodNode newMethod) {
        List<Map<String, Object>> diffs = new ArrayList<>();

        // 比较方法中的字节码
        InsnList oldInstructions = oldMethod.instructions;
        InsnList newInstructions = newMethod.instructions;
        // 此处省略比较逻辑，可根据具体需求实现

        // 示例：比较结果为方法的起始和结束行号
        Map<String, Object> methodDiff = new HashMap<>();
        methodDiff.put("methodName", oldMethod.name);
        methodDiff.put("parameters", getMethodParameters(oldMethod.desc));
        // 示例方法的起始行号和结束行号均为 27 和 38
        methodDiff.put("startLine", 27);
        methodDiff.put("endLine", 38);
        diffs.add(methodDiff);

        return diffs;
    }

    private static List<String> getMethodParameters(String desc) {
        List<String> parameters = new ArrayList<>();

        // 截取参数列表部分
        String parameterPart = desc.substring(desc.indexOf("(") + 1, desc.indexOf(")"));

        // 解析参数类型
        StringBuilder parameterType = new StringBuilder();
        for (char c : parameterPart.toCharArray()) {
            if (c == 'L') {
                // 对象类型以 'L' 开头，以 ';' 结尾
                parameterType.append(c);
            } else if (c == '[') {
                // 数组类型以 '[' 开头，递归解析直到找到基本类型或对象类型
                parameterType.append(c);
            } else if (c == ';') {
                // 对象类型的结束标记
                parameterType.append(c);
                parameters.add(parameterType.toString());
                parameterType = new StringBuilder();
            } else if (c == 'B' || c == 'C' || c == 'D' || c == 'F' || c == 'I' || c == 'J' || c == 'S' || c == 'Z') {
                // 基本数据类型
                parameterType.append(c);
                parameters.add(parameterType.toString());
                parameterType = new StringBuilder();
            } else if (c == '(' || c == ')') {
                // 忽略括号
            } else {
                // 其他情况，暂时忽略
            }

        }
        return parameters;
    }

    private static List<Map<String, Object>> getMethodInfos(List<Map<String, Object>> diffs) {
        List<Map<String, Object>> methodInfos = new ArrayList<>();
        for (Map<String, Object> diff : diffs) {
            Map<String, Object> methodInfo = new HashMap<>();
            methodInfo.put("methodName", diff.get("methodName"));
            methodInfo.put("parameters", diff.get("parameters"));
            methodInfo.put("startLine", diff.get("startLine"));
            methodInfo.put("endLine", diff.get("endLine"));
            methodInfos.add(methodInfo);
        }
        return methodInfos;
    }

    private static List<Map<String, Object>> getLineInfos(List<Map<String, Object>> diffs) {
        List<Map<String, Object>> lineInfos = new ArrayList<>();
        for (Map<String, Object> diff : diffs) {
            // 示例中直接返回固定的 REPLACE 和 INSERT 类型的行差异信息
            Map<String, Object> lineInfo = new HashMap<>();
            lineInfo.put("type", "REPLACE");
            lineInfo.put("startLineNum", diff.get("startLine"));
            lineInfo.put("endLineNum", diff.get("endLine"));
            lineInfos.add(lineInfo);
        }
        return lineInfos;
    }
}

