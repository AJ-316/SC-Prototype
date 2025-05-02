package gameplayHook.CodeModulePackage.components.tokens;

public enum OperatorType {
    // COMPARE
    EQUALS, NOT_EQUALS,
    GREATER_THAN, LESS_THAN,
    GREATER_OR_EQUALS, LESS_OR_EQUALS,
    AND, OR,

    // UNARY
    NONE, NOT, NEGATE,

    // ARITHMETIC
    ADD, SUBTRACT, MULTIPLY, DIVIDE
}