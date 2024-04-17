package com.jacotest.jacocotest.controller;


import ch.qos.logback.classic.log4j.XMLLayout;
import difflib.DiffRow;
import difflib.DiffRowGenerator;
import javassist.*;
import javassist.bytecode.CodeAttribute;
import org.json.JSONArray;
import org.json.JSONObject;




import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class JarCodeDiffJGit {

    public static void main(String[] args) throws IOException, NotFoundException, CannotCompileException {
        // 两个版本的 JAR 文件路径
        String oldJarPath = "D:\\workspace\\codediff-test\\src\\main\\java\\com\\jacotest\\lib\\jacocotest-0.0.1-base-SNAPSHOT.jar";
        String newJarPath = "D:\\workspace\\codediff-test\\src\\main\\java\\com\\jacotest\\lib\\jacocotest-0.0.1-SNAPSHOT.jar";

        // 解析两个 JAR 文件，获取类信息
        List<CtClass> oldClasses = getClassesFromJar(oldJarPath);
        List<CtClass> newClasses = getClassesFromJar(newJarPath);

        // 比较两个版本的类信息，获取差异
        List<JSONObject> diffs = compareClasses(oldClasses, newClasses);

        // 转换差异信息为 JSON 字符串
        JSONArray jsonArray = new JSONArray(Collections.singletonList(diffs));
        String jsonString = jsonArray.toString();
        System.out.println(jsonString);
    }

    // 从 JAR 文件中解析类信息
    private static List<CtClass> getClassesFromJar(String jarPath) throws IOException, NotFoundException {
        List<CtClass> classes = new ArrayList<>();
        File jarFile = new File(jarPath);
        JarFile jar = new JarFile(jarFile);
        Enumeration<JarEntry> entries = jar.entries();
        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            if (entry.getName().endsWith(".class")) {
                String className = entry.getName().replace("/", ".").replace(".class", "");
                ClassPool classPool = ClassPool.getDefault();
                classPool.appendClassPath(jarPath);
                if (classPool.find(className)!=null) {
                    CtClass ctClass = classPool.get(className);
                    classes.add(ctClass);
                }

            }
        }
        return classes;
    }

    // 比较两个版本的类信息，获取差异
    private static List<JSONObject> compareClasses(List<CtClass> oldClasses, List<CtClass> newClasses)
            throws CannotCompileException, IOException {
        List<JSONObject> diffs = new ArrayList<>();
        for (CtClass oldClass : oldClasses) {
            String className = oldClass.getName();
            CtClass newClass = findClassByName(newClasses, className);
            if (newClass != null) {
                JSONObject diff = compareMethods(oldClass, newClass);
                if (diff != null) {
                    diffs.add(diff);
                }
            } else {
                // 类被删除
                JSONObject diff = new JSONObject();
                diff.put("moduleName", className);
                diff.put("type", "DELETE");
                diffs.add(diff);
            }
        }
        for (CtClass newClass : newClasses) {
            String className = newClass.getName();
            CtClass oldClass = findClassByName(oldClasses, className);
            if (oldClass == null) {
                // 新增的类
                JSONObject diff = new JSONObject();
                diff.put("moduleName", className);
                diff.put("type", "ADD");
                diffs.add(diff);
            }
        }
        return diffs;
    }

    // 比较两个类的方法信息，获取差异
    private static JSONObject compareMethods(CtClass oldClass, CtClass newClass) throws CannotCompileException {
        JSONObject diff = new JSONObject();
        String className = oldClass.getName();
        diff.put("classFile", className);

        CtMethod[] oldMethods = oldClass.getDeclaredMethods();
        CtMethod[] newMethods = newClass.getDeclaredMethods();

        JSONArray methodInfos = new JSONArray();
        for (CtMethod oldMethod : oldMethods) {
            String methodName = oldMethod.getName();
            CtMethod newMethod = findMethodByName(newMethods, methodName);
            if (newMethod != null) {
                JSONObject methodInfo = compareMethod(oldMethod, newMethod);
                if (methodInfo != null) {

                    methodInfos.put(methodInfo);
                }
            }
        }
        diff.put("methodInfos", methodInfos);
        diff.put("moduleName", className);
        diff.put("type", "MODIFY");
        return diff;
    }

    // 比较两个方法的代码信息，获取差异
    private static JSONObject compareMethod(CtMethod oldMethod, CtMethod newMethod) throws CannotCompileException {
        JSONObject methodInfo = new JSONObject();
        String methodName = oldMethod.getName();
        methodInfo.put("methodName", methodName);

        // 比较方法的参数信息
        String[] oldParameters = getMethodParameters(oldMethod);
        String[] newParameters = getMethodParameters(newMethod);
        if (!Arrays.equals(oldParameters, newParameters)) {
            methodInfo.put("parameters", newParameters);
        }

        // 比较方法的代码行信息
        CodeAttribute oldCode = oldMethod.getMethodInfo().getCodeAttribute();

        CodeAttribute newCode = newMethod.getMethodInfo().getCodeAttribute();
        if (!oldCode.equals(newCode)) {
            methodInfo.put("lines", compareCodeLines(oldCode.toString(), newCode.toString()));
        }

        // 返回方法信息
        return methodInfo;
    }

    // 比较两个方法的参数信息
    private static String[] getMethodParameters(CtMethod method) {
        // 获取方法参数信息的具体实现
        // 这里需要根据实际情况编写获取方法参数的代码
        // 返回方法的参数数组，例如 ["String", "int"]
        try {
            // 获取方法的参数类型
            CtClass[] parameterTypes = method.getParameterTypes();
            if (parameterTypes == null || parameterTypes.length == 0) {
                return new String[0];
            }

            // 构造参数类型字符串数组
            String[] parameters = new String[parameterTypes.length];
            for (int i = 0; i < parameterTypes.length; i++) {
                parameters[i] = parameterTypes[i].getName();
            }
            return parameters;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 比较两段代码的行信息，获取差异
    private static JSONArray compareCodeLines(String oldCode, String newCode) {
        JSONArray jsonArray = new JSONArray();
        try {
            DiffRowGenerator.Builder builder = new DiffRowGenerator.Builder();
            DiffRowGenerator generator = builder.build();

            List<DiffRow> rows = generator.generateDiffRows(Arrays.asList(oldCode),Arrays.asList(newCode));

            for (DiffRow row : rows) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("type", row.getTag().name());
                jsonObject.put("oldLine", row.getOldLine());
                jsonObject.put("newLine", row.getNewLine());
                jsonObject.put("line", row.getTag());
                jsonArray.put(jsonObject);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonArray;
    }

    // 根据类名查找类对象
    private static CtClass findClassByName(List<CtClass> classes, String className) {
        for (CtClass ctClass : classes) {
            if (ctClass.getName().equals(className)) {
                return ctClass;
            }
        }
        return null;
    }

    // 根据方法名查找方法对象
    private static CtMethod findMethodByName(CtMethod[] methods, String methodName) {
        for (CtMethod method : methods) {
            if (method.getName().equals(methodName)) {
                return method;
            }
        }
        return null;
    }
}
