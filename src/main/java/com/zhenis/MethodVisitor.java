package com.zhenis;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.stmt.TryStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.List;

public class MethodVisitor extends VoidVisitorAdapter<Void> {

    @Override
    public void visit(MethodDeclaration method, Void arg) {
        super.visit(method, arg);

        // Собираем метрики для метода
        String methodName = method.getNameAsString();
        int cyclomaticComplexity = calculateCyclomaticComplexity(method);
        int depth = calculateNestingDepth(method);
        int linesOfCode = calculateLinesOfCode(method);
        int lambdaCount = countLambdas(method);
        int tryCatchCount = countTryCatchBlocks(method);

        // Создаём объект Model с результатами
        MethodComplexityModel methodModel = new MethodComplexityModel(
                methodName, cyclomaticComplexity, depth, linesOfCode, lambdaCount, tryCatchCount);

        // Выводим информацию
        System.out.println(methodModel);
    }

    // Цикломатическая сложность
    private int calculateCyclomaticComplexity(MethodDeclaration method) {
        int complexity = 1; // Начинаем с 1 для метода

        // Ищем управляющие конструкции (if, for, while, switch, case)
        complexity += method.toString().split("if|for|while|switch|case").length - 1;

        return complexity;
    }

    // Глубина вложенности
    private int calculateNestingDepth(MethodDeclaration method) {
        int maxDepth = 0;
        int currentDepth = 0;

        // Мы будем обходить код метода, увеличивая и уменьшая глубину при встрече с конструкциями
        String code = method.getBody().orElseThrow().toString();
        for (char c : code.toCharArray()) {
            if (c == '{') {
                currentDepth++;
                maxDepth = Math.max(maxDepth, currentDepth);
            } else if (c == '}') {
                currentDepth--;
            }
        }

        return maxDepth;
    }

    // Число строк в методе (без пустых строк и комментариев)
    private int calculateLinesOfCode(MethodDeclaration method) {
        String code = method.getBody().orElseThrow().toString();

        // Удаляем пустые строки и комментарии
        String[] lines = code.split("\n");
        int count = 0;
        for (String line : lines) {
            line = line.trim();
            if (!line.isEmpty() && !line.startsWith("//") && !line.startsWith("/*") && !line.endsWith("*/")) {
                count++;
            }
        }

        return count;
    }

    // Подсчитываем количество лямбда-выражений
    private int countLambdas(MethodDeclaration method) {
        List<LambdaExpr> lambdas = method.findAll(LambdaExpr.class);
        return lambdas.size();
    }

    // Подсчитываем количество блоков try-catch
    private int countTryCatchBlocks(MethodDeclaration method) {
        List<TryStmt> tryCatchBlocks = method.findAll(TryStmt.class);
        return tryCatchBlocks.size();
    }
}

