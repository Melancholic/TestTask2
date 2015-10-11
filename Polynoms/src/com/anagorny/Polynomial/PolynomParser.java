package com.anagorny.Polynomial;

import java.util.Scanner;
import java.util.Stack;

/**
 * Created by Andrey Nagorny on 10.10.15.
 *
 * @author Andrey Nagorny
 * Класс, выполянющий разбор входной строки
 * и создающий объект класса Polynomial
 * <p/>
 * Область видимости - пакет com.anagorny.Polynomial
 * Область инстанцирования - пакет com.anagorny.Polynomial {@link #PolynomParser()}
 */
class PolynomParser {
    private static final String TERM_REGEXP = "[a-zA-Z0-9]";
    private static final String DIGIT_REGEXP = "[+-]?[0-9]+";
    private static final String SIGN_REGEXP = "[+-]?";
    private static final String POWER_REGEXP = "\\^[0-9]+";
    private static final String MONOM_REGEXP = "[-+]?[0-9]*[A-Za-z]?\\^?[0-9]*";
    private String expression;      //Исходное выражение
    private char variable;          //Символ переменной
    private int pointer = 0;        //Указатель на текущее положение разбора

    private enum Actions {
        ADD_AND_SUB,                //<op1> +<op2>, <op1> - <op2>
        MULTIPLIES_MONOM,           //<op1> * <op2>
        OPEN_PARENTHESIS,           // ( ....
        CLOSE_PARENTHESIS,          // ....)
        ADD_TO_PARENTHESIS,         //<op1> + (<op2>)
        SUBTRACTS_PARENTHESIS,      //<op1> - (<op2>)
        MULTIPLIES_PARENTHESIS,     //<op1> (op2>), <op1> * (<op2>)
        PARENTHESIS_MULTIPLY,       //(<op1>) * <op2>
        PARENTHESIS_POWER           //(<op1>) ^ <op2>
    }

    /**
     * Конструктор по умолчанию с пакетной областью видимости
     * Инстанцирование вне пакета {@link com.anagorny.Polynomial} запрещено.
     */
    PolynomParser() {
    }

    /**
     * Возвращает результат обработки входного выражения
     *
     * @param exp Входное выражение,содержащее скобки, операции +,-,*,^  и одну переменную,
     *            например: (x - 5)(2x^3 + x(x^2 - 9))
     * @return Объект класса {@link com.anagorny.Polynomial.Polynomial}
     */
    public Polynomial parse(String exp) {
        if (exp == null || exp.length() == 0) {
            return null;
        }
        checkExpression(exp);
        return parseOperations();
    }

    /**
     * Выполняет разбор выражения, используя рекурсивный спуск.
     *
     * @return Объект класса {@link com.anagorny.Polynomial.Polynomial}
     * @throws IllegalStateException При неверной конструкции исходного выражения
     */
    private Polynomial parseOperations() {

        Stack<Polynomial> stack = new Stack<>();
        whileLabel:
        while (pointer < expression.length()) {
            char current = expression.charAt(pointer);
            char next = (pointer < expression.length() - 1) ? expression.charAt(pointer + 1) : 0;
            Actions op = null;
            if (String.valueOf(current).matches(TERM_REGEXP)) {
                op = Actions.ADD_AND_SUB;
            } else switch (current) {
                case '(': {
                    if (stack.isEmpty()) {
                        op = Actions.OPEN_PARENTHESIS;
                    } else {
                        op = Actions.MULTIPLIES_PARENTHESIS;
                    }
                }
                break;
                case ')': {
                    if (next == '^') {
                        op = Actions.PARENTHESIS_POWER;
                    } else if (Character.isLetterOrDigit(next)) {
                        op = Actions.PARENTHESIS_MULTIPLY;
                    } else {
                        op = Actions.CLOSE_PARENTHESIS;
                    }
                }
                break;
                case '+': {
                    if (next == '(') {
                        op = Actions.ADD_TO_PARENTHESIS;
                    } else {
                        op = Actions.ADD_AND_SUB;
                    }
                }
                ;
                break;

                case '-': {
                    if (next == '(') {
                        op = Actions.SUBTRACTS_PARENTHESIS;
                    } else {
                        op = Actions.ADD_AND_SUB;
                    }
                }
                break;
                case '*': {
                    if (next == '(') {
                        pointer++;
                        op = Actions.MULTIPLIES_PARENTHESIS;
                    } else {
                        op = Actions.MULTIPLIES_MONOM;
                    }
                }
                break;
                default: {
                    throw new IllegalStateException("Error: Unknown symbol: \"" + current + "\"");
                }
            }
            switch (op) {
                case CLOSE_PARENTHESIS: {
                    pointer++;
                }
                break whileLabel;
                case ADD_AND_SUB: {
                    String monomial = scanNext(pointer, MONOM_REGEXP);
                    pointer += monomial.length();
                    stack.add(parseMonom(monomial));
                }
                break;
                case ADD_TO_PARENTHESIS: {
                    pointer += 2;
                    stack.add(parseOperations());
                }
                break;
                case SUBTRACTS_PARENTHESIS: {
                    pointer += 2;
                    stack.add(Polynomial.mul(new Polynomial(variable).add(0, -1), parseOperations()));
                }
                break;
                case OPEN_PARENTHESIS: {
                    pointer++;
                    stack.add(parseOperations());
                }
                break;
                case MULTIPLIES_PARENTHESIS: {
                    pointer++;
                    if (stack.isEmpty()) {
                        throw new IllegalStateException("Error: Stack is empty! ");
                    }
                    Polynomial operand1 = stack.pop();
                    Polynomial operand2 = parseOperations();
                    stack.add(operand1.mul(operand2));
                }
                break;
                case MULTIPLIES_MONOM: {
                    pointer++;
                    if (stack.isEmpty()) {
                        throw new IllegalStateException("Error: Stack is empty! ");
                    }
                    Polynomial operand1 = stack.pop();
                    Polynomial operand2;
                    String monomial = scanNext(pointer, MONOM_REGEXP);
                    pointer += monomial.length();
                    operand2 = parseMonom(monomial);
                    stack.add(operand1.mul(operand2));
                }
                break;
                case PARENTHESIS_MULTIPLY: {
                    pointer++;
                    Polynomial operand1 = new Polynomial(variable);
                    for (Polynomial polynomial : stack) {
                        operand1.add(polynomial);
                    }
                    String monomial = scanNext(pointer, MONOM_REGEXP);
                    pointer += monomial.length();
                    Polynomial operand2 = parseMonom(monomial);
                    return operand1.mul(operand2);
                }
                case PARENTHESIS_POWER: {
                    pointer += 2;
                    String power = scanNext(pointer, DIGIT_REGEXP);
                    pointer += power.length();
                    Polynomial operand = new Polynomial(variable);
                    for (Polynomial polynomial : stack) {
                        operand.add(polynomial);
                    }
                    return Polynomial.pow(operand, Integer.parseInt(power));
                }
            }
        }

        Polynomial result = new Polynomial(variable);
        for (Polynomial polynomial : stack) {
            result.add(polynomial);
        }
        return result;
    }

    /**
     * Изымает из исходного выражения последовательность символов,
     * удолетворяющее регулярному выражения {@param pattern} начиная с позиции {@param startPos}
     *
     * @param startPos Начальная позиция
     * @param pattern  Регулярное выражение
     * @return текстовая строка String
     * @throws IllegalStateException При неверной конструкции исходного выражения
     */
    private String scanNext(int startPos, String pattern) {
        return new Scanner(expression.substring(startPos)).findInLine(pattern);
    }

    /**
     * Разбирает одночлен(моном) на терминалы
     *
     * @param monom Строка, содержащая необходимый для разбора одночлен.
     * @return Объект класса {@link com.anagorny.Polynomial.Polynomial}
     * @throws IllegalArgumentException При неккоректном выражении одночлена.
     */
    private Polynomial parseMonom(String monom) {
        Polynomial result = new Polynomial(variable);
        int power = 0;
        int coefficient = 0;
        if (monom.matches(DIGIT_REGEXP)) {
            //число
            power = 0;
            coefficient = Integer.valueOf(monom);
        } else if (monom.matches(DIGIT_REGEXP + POWER_REGEXP)) {
            //число с показателем степени
            power = 0;
            int coef_pow = Integer.valueOf(monom.split("\\^")[1]);
            int coef_base = Integer.valueOf(monom.split("\\^")[0]);
            coefficient = ((Double) Math.pow(coef_base, coef_pow)).intValue();
        } else if (monom.matches(SIGN_REGEXP + variable)) {
            //переменная со знаком
            power = 1;
            coefficient = monom.matches("-" + variable) ? -1 : 1;
        } else if (monom.matches(DIGIT_REGEXP + variable)) {
            //переменная с числовым коэффициентом
            power = 1;
            coefficient = Integer.valueOf(monom.substring(0, monom.indexOf(variable)));
        } else if (monom.matches(SIGN_REGEXP + variable + POWER_REGEXP)) {
            //переменная со знаком и показателем степени
            power = Integer.valueOf(monom.split(SIGN_REGEXP + variable + "\\^")[1]);
            coefficient = monom.matches("-.*") ? -1 : 1;
        } else if (monom.matches(DIGIT_REGEXP + variable + POWER_REGEXP)) {
            //переменная с числовым коэффициентом  и показателем степени
            power = Integer.valueOf(monom.split(variable + "\\^")[1]);
            coefficient = Integer.valueOf(monom.split(variable + "\\^")[0]);
        } else {
            ;
            throw new IllegalArgumentException("Error: The expression \"" + monom + "\" could not be parsed ");
        }
        result.add(power, coefficient);
        return result;
    }

    /**
     * Проверяет исходное выражение на правильность составления многочлена
     *
     * @param sourceStr Выражение для проверки
     * @throws IllegalArgumentException Если обнаружена ошибка в исхолдном выражении
     */
    void checkExpression(String sourceStr) {
        //Очистка от лишних пробелов
        expression = sourceStr.replaceAll("\\s", "");
        if (expression.equals("")) return;
        //Определение переменной
        String vars = expression.replaceAll("[^A-Za-z]", "").replaceAll("(.)\\1{1,}", "$1");
        if (vars.length() > 1) {
            throw new IllegalArgumentException("Error: The input line contains several variables: " +
                    vars.replaceAll("(.)", "$1 "));
        } else if (vars.length() != 0) {
            variable = vars.charAt(0);
        } else {
            throw new IllegalArgumentException("Error: The variable is not found in the input string");
        }
        //Определение правильности расставленных скобок
        if (expression.replaceAll("[^(]", "").length() != expression.replaceAll("[^)]", "").length()) {
            throw new IllegalArgumentException("Error: The amount \"(\" do not coincide with the amount of \")\" ");
        }
        //Выявление неккоректных синтакситеских последовательностей (xx, x^Cx, C^Kx)
        if (expression.matches(variable + "{2,}") ||
                expression.matches(variable + POWER_REGEXP + variable) ||
                expression.matches(DIGIT_REGEXP + POWER_REGEXP + variable)) {
            throw new IllegalArgumentException("Error: Unknown sequence of characters in the input string");
        }

    }

}
