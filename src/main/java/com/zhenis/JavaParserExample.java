package com.zhenis;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JavaParserExample {
    public static void main(String[] args) throws IOException {

        File file = new File("/Users/tized/IdeaProjects/java-parser/src/main/resources/Test.txt");
        CompilationUnit compilationUnit;
        try (FileInputStream fis = new FileInputStream(file)) {
            compilationUnit = new JavaParser().parse(fis).getResult().orElseThrow();
        }

        List<ClassModel> classModels = new ArrayList<>();
        compilationUnit.getTypes().forEach(type -> classModels.add(processType(type)));

        // Example output
        classModels.forEach(JavaParserExample::printClassModel);

        compilationUnit.accept(new MethodVisitor(), null);
    }


    private static ClassModel processType(TypeDeclaration<?> type) {
        boolean isStatic = type.isClassOrInterfaceDeclaration() && ((ClassOrInterfaceDeclaration) type).isStatic();
        ClassModel classModel = isStatic ? new NestedClassModel() : new InnerClassModel();

        classModel.name = type.getNameAsString();
        classModel.kind = type instanceof ClassOrInterfaceDeclaration ? (((ClassOrInterfaceDeclaration) type).isInterface() ? "Interface" : "Class") : type.getClass().getSimpleName();
        type.getModifiers().forEach(mod -> classModel.modifiers.add(mod.toString()));
        type.getAnnotations().forEach(ann -> classModel.annotations.add(ann.toString()));

        if (type instanceof ClassOrInterfaceDeclaration) {
            ClassOrInterfaceDeclaration classOrInterface = (ClassOrInterfaceDeclaration) type;
            classOrInterface.getExtendedTypes().forEach(e -> classModel.extendsTypes.add(e.toString()));
            classOrInterface.getImplementedTypes().forEach(i -> classModel.implementsTypes.add(i.toString()));
        }

        type.getMembers().forEach(member -> {
            if (member instanceof FieldDeclaration) {
                classModel.fields.add(processField((FieldDeclaration) member));
            } else if (member instanceof MethodDeclaration) {
                classModel.methods.add(processMethod((MethodDeclaration) member));
            } else if (member instanceof TypeDeclaration) {
                classModel.innerClasses.add(processType((TypeDeclaration<?>) member));
            }
        });

        return classModel;
    }

    private static FieldModel processField(FieldDeclaration field) {
        FieldModel fieldModel = new FieldModel();
        field.getVariables().forEach(var -> {
            fieldModel.name = var.getNameAsString();
            fieldModel.type = var.getTypeAsString();
        });
        field.getModifiers().forEach(mod -> fieldModel.modifiers.add(mod.toString()));
        field.getAnnotations().forEach(ann -> fieldModel.annotations.add(ann.toString()));
        return fieldModel;
    }

    private static MethodModel processMethod(MethodDeclaration method) {
        MethodModel methodModel = new MethodModel();
        methodModel.name = method.getNameAsString();
        methodModel.returnType = method.getTypeAsString();
        method.getModifiers().forEach(mod -> methodModel.modifiers.add(mod.toString()));
        method.getAnnotations().forEach(ann -> methodModel.annotations.add(ann.toString()));
        method.getParameters().forEach(param -> {
            ParameterModel paramModel = new ParameterModel();
            paramModel.type = param.getTypeAsString();
            paramModel.name = param.getNameAsString();
            methodModel.parameters.add(paramModel);
        });
        method.getThrownExceptions().forEach(thrown -> methodModel.thrownExceptions.add(thrown.toString()));

        method.findAll(MethodCallExpr.class).forEach(call -> {
            MethodCallModel callModel = new MethodCallModel();
            callModel.caller = call.getScope().map(Object::toString).orElse("[implicit]");
            callModel.methodName = call.getNameAsString();
            methodModel.methodCalls.add(callModel);
        });
        return methodModel;
    }

    private static void printClassModel(ClassModel classModel) {
        printClassModel(classModel, 0);
    }

    private static void printClassModel(ClassModel classModel, int indent) {
        String indentStr = "  ".repeat(indent);
        System.out.println(indentStr + "Class: " + classModel.name + " (" + classModel.kind + ")");

        if (!classModel.modifiers.isEmpty()) {
            System.out.println(indentStr + "  Modifiers: " + String.join(", ", classModel.modifiers));
        }

        if (!classModel.annotations.isEmpty()) {
            System.out.println(indentStr + "  Annotations: " + String.join(", ", classModel.annotations));
        }

        if (!classModel.extendsTypes.isEmpty()) {
            System.out.println(indentStr + "  Extends: " + String.join(", ", classModel.extendsTypes));
        }

        if (!classModel.implementsTypes.isEmpty()) {
            System.out.println(indentStr + "  Implements: " + String.join(", ", classModel.implementsTypes));
        }

        // Вывод полей
        if (!classModel.fields.isEmpty()) {
            System.out.println(indentStr + "  Fields:");
            for (FieldModel field : classModel.fields) {
                System.out.println(indentStr + "    - " + field.type + " " + field.name);
                if (!field.modifiers.isEmpty()) {
                    System.out.println(indentStr + "      Modifiers: " + String.join(", ", field.modifiers));
                }
                if (!field.annotations.isEmpty()) {
                    System.out.println(indentStr + "      Annotations: " + String.join(", ", field.annotations));
                }
            }
        }

        // Вывод методов
        if (!classModel.methods.isEmpty()) {
            System.out.println(indentStr + "  Methods:");
            for (MethodModel method : classModel.methods) {
                System.out.println(indentStr + "    - " + method.returnType + " " + method.name + "(" +
                        method.parameters.stream().map(p -> p.type + " " + p.name).reduce((a, b) -> a + ", " + b).orElse("") +
                        ")");
                if (!method.modifiers.isEmpty()) {
                    System.out.println(indentStr + "      Modifiers: " + String.join(", ", method.modifiers));
                }
                if (!method.annotations.isEmpty()) {
                    System.out.println(indentStr + "      Annotations: " + String.join(", ", method.annotations));
                }
                if (!method.thrownExceptions.isEmpty()) {
                    System.out.println(indentStr + "      Throws: " + String.join(", ", method.thrownExceptions));
                }
                if (!method.methodCalls.isEmpty()) {
                    System.out.println(indentStr + "      Calls:");
                    for (MethodCallModel call : method.methodCalls) {
                        System.out.println(indentStr + "        - " + call.caller + "." + call.methodName + "()");
                    }
                }
            }
        }

        // Вывод вложенных классов
        if (!classModel.innerClasses.isEmpty()) {
            System.out.println(indentStr + "  Inner Classes:");
            for (ClassModel inner : classModel.innerClasses) {
                printClassModel(inner, indent + 2);
            }
        }
    }
}