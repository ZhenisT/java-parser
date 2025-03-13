package com.zhenis;

public class MethodComplexityModel {
    private String methodName;
    private int cyclomaticComplexity;
    private int nestingDepth;
    private int linesOfCode;
    private int lambdaCount;
    private int tryCatchCount;

    public MethodComplexityModel(String methodName, int cyclomaticComplexity, int nestingDepth,
                       int linesOfCode, int lambdaCount, int tryCatchCount) {
        this.methodName = methodName;
        this.cyclomaticComplexity = cyclomaticComplexity;
        this.nestingDepth = nestingDepth;
        this.linesOfCode = linesOfCode;
        this.lambdaCount = lambdaCount;
        this.tryCatchCount = tryCatchCount;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public int getCyclomaticComplexity() {
        return cyclomaticComplexity;
    }

    public void setCyclomaticComplexity(int cyclomaticComplexity) {
        this.cyclomaticComplexity = cyclomaticComplexity;
    }

    public int getNestingDepth() {
        return nestingDepth;
    }

    public void setNestingDepth(int nestingDepth) {
        this.nestingDepth = nestingDepth;
    }

    public int getLinesOfCode() {
        return linesOfCode;
    }

    public void setLinesOfCode(int linesOfCode) {
        this.linesOfCode = linesOfCode;
    }

    public int getLambdaCount() {
        return lambdaCount;
    }

    public void setLambdaCount(int lambdaCount) {
        this.lambdaCount = lambdaCount;
    }

    public int getTryCatchCount() {
        return tryCatchCount;
    }

    public void setTryCatchCount(int tryCatchCount) {
        this.tryCatchCount = tryCatchCount;
    }

    // Метод для вывода информации
    @Override
    public String toString() {
        return "MethodModel{" +
                "methodName='" + methodName + '\'' +
                ", cyclomaticComplexity=" + cyclomaticComplexity +
                ", nestingDepth=" + nestingDepth +
                ", linesOfCode=" + linesOfCode +
                ", lambdaCount=" + lambdaCount +
                ", tryCatchCount=" + tryCatchCount +
                '}';
    }
}
