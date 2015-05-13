package ru.ifmo.cs.bcomp.android.util;

public final class AsmDevProgram {
    private AsmDevProgram() {
    }

    public static final String PROGRAM = "" +
        "ORG     000\n" +
        "RET:    WORD    ?\n" +
        "        BR      INT\n" +
        "\n" +
        "ORG     010\n" +
        "BEGIN:  CLA\n" +
        "        MOV     T\n" +
        "        ADD     CONST\n" +
        "        OUT     0\n" +
        "        EI\n" +
        "        CLA\n" +
        "        INC\n" +
        "LOOP:   ROR\n" +
        "        BCS     LOOP\n" +
        "        MOV     R\n" +
        "        BR      LOOP\n" +
        "\n" +
        "ORG     01C\n" +
        "CONST:  WORD    A\n" +
        "X:      WORD    0\n" +
        "T:      WORD    ?\n" +
        "R:      WORD    ?\n" +
        "\n" +
        "INT:    MOV     STOR_A\n" +
        "        ROL\n" +
        "        MOV     STOR_C\n" +
        "        TSF     0\n" +
        "        BR      IO1\n" +
        "        CLF     0\n" +
        "        ISZ     T\n" +
        "        NOP\n" +
        "IO1:    TSF     1\n" +
        "        BR      IO2\n" +
        "        CLA\n" +
        "        ADD     X\n" +
        "        OUT     1\n" +
        "        CLF     1\n" +
        "IO2:    TSF     2\n" +
        "        BR      IO3\n" +
        "        CLA\n" +
        "        IN      2\n" +
        "        CLF     2\n" +
        "        MOV     X\n" +
        "IO3:    TSF     3\n" +
        "        BR      END\n" +
        "        CLA\n" +
        "        IN      3\n" +
        "        MOV     TMP\n" +
        "        CLA\n" +
        "        ADD     X\n" +
        "        OUT     3\n" +
        "        CLF     3\n" +
        "        CLA\n" +
        "        ADD     TMP\n" +
        "        MOV     X\n" +
        "END:    CLA\n" +
        "        ADD     STOR_C\n" +
        "        ROR\n" +
        "        CLA\n" +
        "        CMA\n" +
        "        AND     STOR_A\n" +
        "        EI\n" +
        "        BR      (RET)\n" +
        "TMP:    WORD    ?\n" +
        "STOR_A: WORD    ?\n" +
        "STOR_C: WORD    ?";
}
