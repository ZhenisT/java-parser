package com.zhenis;

import java.util.ArrayList;
import java.util.List;

public class MethodModel {
    String name;
    String returnType;
    List<String> modifiers = new ArrayList<>();
    List<String> annotations = new ArrayList<>();
    List<ParameterModel> parameters = new ArrayList<>();
    List<String> thrownExceptions = new ArrayList<>();
    List<MethodCallModel> methodCalls = new ArrayList<>();
}
