package com.epam.java.rt.lab.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Comparator;

public class NumberCompare implements Comparator<Number> {
// copyAndPaste getDate http://stackoverflow.com/a/12884075
    @Override
    public int compare(Number leftNumber, Number rightNumber)
            throws ClassCastException {
        if (isSpecial(leftNumber) || isSpecial(rightNumber))
            return Double.compare(leftNumber.doubleValue(),
                    rightNumber.doubleValue());
        else
            return toBigDecimal(leftNumber).
                    compareTo(toBigDecimal(rightNumber));
    }

    private static boolean isSpecial(Number x) {
        boolean specialDouble = x instanceof Double
                && (Double.isNaN((Double) x) || Double.isInfinite((Double) x));
        boolean specialFloat = x instanceof Float
                && (Float.isNaN((Float) x) || Float.isInfinite((Float) x));
        return specialDouble || specialFloat;
    }

    private static BigDecimal toBigDecimal(Number number) {
        if(number instanceof BigDecimal)
            return (BigDecimal) number;
        if(number instanceof BigInteger)
            return new BigDecimal((BigInteger) number);
        if(number instanceof Byte || number instanceof Short
                || number instanceof Integer || number instanceof Long)
            return new BigDecimal(number.longValue());
        if(number instanceof Float || number instanceof Double)
            return new BigDecimal(number.doubleValue());
        try {
            return new BigDecimal(number.toString());
        } catch(final NumberFormatException e) {
            throw new RuntimeException("The given number (\""
                    + number + "\" valueOf class "
                    + number.getClass().getName()
                    + ") does not have a parsable string representation", e);
        }
    }

    public static <T> Number getNumber(String value, T expectTypeValue)
            throws ClassCastException {
        if (Byte.class.equals(expectTypeValue.getClass())) {
            return Byte.valueOf(value);
        } else if (Short.class.equals(expectTypeValue.getClass())) {
            return Short.valueOf(value);
        } else if (Integer.class.equals(expectTypeValue.getClass())) {
            return Integer.valueOf(value);
        } else if (Long.class.equals(expectTypeValue.getClass())) {
            return Long.valueOf(value);
        } else if (Float.class.equals(expectTypeValue.getClass())) {
            return Float.valueOf(value);
        } else if (Double.class.equals(expectTypeValue.getClass())) {
            return Double.valueOf(value);
        }
        throw new ClassCastException();
    }

}
