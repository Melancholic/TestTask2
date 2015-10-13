package com.anagorny.Polynomial;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Andrey Nagorny on 10.10.15.
 *
 * @author Andrey Nagorny
 * Класс, представляющий сущность полинома
 * и описывающий ряд операций над ним
 * <p/>
 * Область видимости - без ограничений (public)
 * Область инстанцирования - пакет com.anagorny.Polynomial {@link #Polynomial()}, {@link #Polynomial(char)}
 */
public class Polynomial {
    private final char variable;    //Символ переменной
    /**
     * Упорядоченный словарь, хранящий
     * одночлены полинома в виде
     * < key, value >, где
     * key - показатель степени данного одночлена при переменной
     * value - коэффициент данного одночлена при переменной
     */
    private TreeMap<Integer, Integer> monomials;

    /**
     * Конструктор по умолчанию с private-областью видимости
     * Инстанцирование вне класса Polynomial запрещено.
     * Для получения объекта вне пакета использовать метод {@link #parse(String)}
     */
    private Polynomial() {
        variable = 'x';
        this.monomials = new TreeMap<Integer, Integer>();
    }

    /**
     * Конструктор с пакетной областью видимости, инициализирующий переменную variable
     * значение параметра {@param variable}
     * Инстанцирование вне пакета com.anagorny.Polynomial запрещено.
     * Для получения объекта вне пакета использовать метод {@link #parse(String)}
     *
     * @param variable Символ переменной
     */
    Polynomial(char variable) {
        this.variable = variable;
        this.monomials = new TreeMap<Integer, Integer>();
    }

    /**
     * Метод, выполняющий разбор принятого в качестве аргумента выражения.
     *
     * @param exp Выражение, представляющее полином
     * @return Объект класса {@link com.anagorny.Polynomial.Polynomial}
     */
    public static Polynomial parse(String exp) {
        return (new PolynomParser()).parse(exp);
    }

    /**
     * Возвращает строку, представляющее полином в текстовом формате
     *
     * @return Текстовое представление в String
     */
    @Override
    public String toString() {
        trim();
        if (monomials.isEmpty()) return "0";

        StringBuilder result = new StringBuilder();
        for (Integer key : monomials.descendingKeySet()) {
            if (monomials.get(key) < 0) {
                result.append("-");
            } else if (result.length() > 0) {
                result.append("+");
            }
            int value = Math.abs(monomials.get(key));
            result.append((value == 1 && key!=0) ? "" : value);
            if (key != 0) {
                result.append(variable);
                if (key != 1) {
                    result.append("^" + key);
                }
            }

        }

        return result.toString();
    }

    /**
     * Метод, удаляющий "пустые" одночлены (коэффициент при переменной которых равен 0)
     */
    private void trim() {
        Iterator<Map.Entry<Integer, Integer>> iterator = monomials.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, Integer> entry = iterator.next();
            if (entry.getValue() == 0) iterator.remove();
        }
    }


    /**
     * Складывает текущий полином с одночленом,
     * представленном в параметрах. Если у полинома уже найден
     * одночлен с таким же степенным показателем,  произойдет
     * сложения числовых коэффициентов
     *
     * @param power       Показатель степени при переменной
     * @param coefficient Числовой коэффициент при переменной
     * @return Возвращает текущий полином
     */
    public Polynomial add(Integer power, Integer coefficient) {
        if (monomials.containsKey(power)) {
            monomials.put(power, monomials.get(power) + coefficient);
        } else {
            monomials.put(power, coefficient);
        }
        return this;
    }

    /**
     * Складывает текущий полином с другим полиномом
     *
     * @param other Другой полином
     * @return Текущий полином
     */
    public Polynomial add(Polynomial other) {
        for (Integer power : other.monomials.keySet()) {
            this.add(power, other.monomials.get(power));
        }
        return this;
    }

    /**
     * Вычитает одночлен, представленный в параметрах из текущего полинома.
     * Если у полинома уже найден одночлен с таким же степенным показателем,
     * произойдет вычитание числовых коэффициентов
     *
     * @param power       Показатель степени при переменной
     * @param coefficient Числовой коэффициент при переменной
     * @return Возвращает текущий полином
     */
    public Polynomial sub(Integer power, Integer coefficient) {
        return this.add(power, -1 * coefficient);
    }

    /**
     * Вычитает другой полином из текущего полинома.
     *
     * @param other Другой полином
     * @return Текущий полином
     */
    public Polynomial sub(Polynomial other) {
        return this.add(other.mul(new Polynomial(variable).add(0, -1)));
    }


    /**
     * Складывает два полином
     *
     * @param first  Первый полином
     * @param second Второй полином
     * @return Новый полином
     */
    public static Polynomial add(Polynomial first, Polynomial second) {
        Polynomial result = new Polynomial(first.variable);
        result.add(first);
        result.add(second);
        return result;
    }


    /**
     * Вычитает из первого полинома второй.
     *
     * @param first  Первый полином
     * @param second Второй полином
     * @return Новый полином
     */
    public static Polynomial sub(Polynomial first, Polynomial second) {
        Polynomial result = new Polynomial(first.variable);
        result.add(first);
        result.sub(second);
        return result;
    }

    /**
     * Произведение текущего полинома на другой
     *
     * @param other Другой полином
     * @return Текущей полином
     * @see #pow(Polynomial, int)
     * @see #pow(int)
     */
    public Polynomial mul(Polynomial other) {
        return mul(this, other);
    }

    /**
     * Произведение двух полиномов, переданных в параметрах.
     *
     * @param first  Первый полином
     * @param second Второй полином
     * @return Результат произведения двух полиномов
     */
    public static Polynomial mul(Polynomial first, Polynomial second) {
        Polynomial result = new Polynomial(first.variable);
        for (Integer firstPow : first.monomials.keySet()) {
            for (Integer secondPow : second.monomials.keySet()) {
                int sumPower = firstPow + secondPow;
                Integer mulCoefficient = first.monomials.get(firstPow) * second.monomials.get(secondPow);
                result.add(sumPower, mulCoefficient);
            }
        }
        return result;
    }

    /**
     * Возведенение текущег полинома в степень, показатель которой передается в параметре
     *
     * @param power Показатель степени
     * @return Текущий полином после возведения в переданную степень
     */
    public Polynomial pow(int power) {
        return pow(this, power);
    }

    /**
     * Рекурсивное возведение в степень полинома, переданного в параметрах.
     *
     * @param polynomial Полином
     * @param power      Показатель степени
     * @return Результат возведения полинома в степень
     */
    public static Polynomial pow(Polynomial polynomial, int power) {
        if (power == 0) {
            return new Polynomial(polynomial.variable).add(0, 1);
        } else if (power <= 1) {
            return polynomial;
        } else {
            return polynomial.mul(pow(polynomial, --power));
        }
    }


}