package gameplayHook.CodeModulePackage.components;

public enum OperatorType {
    // COMPARE
    EQUALS, NOT_EQUALS,
    GREATER_THAN, LESS_THAN,
    GREATER_OR_EQUALS, LESS_OR_EQUALS,
    AND, OR,

    // UNARY
    NOT, NEGATE,

    // ARITHMETIC
    ADD, SUBTRACT, MULTIPLY, DIVIDE
}