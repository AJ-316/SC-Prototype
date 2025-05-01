package gameplayHook.CodeModulePackage.statements;

import gameplayHook.CodeModulePackage.components.Assignment;

public class AssignmentStatement extends Statement {
    public Assignment assignment;

    public AssignmentStatement(Assignment assignment) {
        this.assignment = assignment;
    }
}