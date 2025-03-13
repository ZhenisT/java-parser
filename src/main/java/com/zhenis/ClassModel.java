package com.zhenis;

import java.util.ArrayList;
import java.util.List;

public class ClassModel {
    String name;
    String kind;
    List<String> modifiers = new ArrayList<>();
    List<String> annotations = new ArrayList<>();
    List<String> extendsTypes = new ArrayList<>();
    List<String> implementsTypes = new ArrayList<>();
    List<FieldModel> fields = new ArrayList<>();
    List<MethodModel> methods = new ArrayList<>();
    List<ClassModel> innerClasses = new ArrayList<>();
}
