package com.example.mossymobile.MossFramework.Math;

import java.util.Random;

public final class MossMath {
    public static final float EPSILON = 0.00001f;
    public static final float PI = 3.1415926535897932384626433832795f;
    public static final float TWO_PI = PI * 2.0f;
    public static final float HALF_PI = PI / 2.0f;
    public static final float QUARTER_PI = PI / 4.0f;

    /**
     * Ensures the value will always be positive.
     *
     * @param value the value of the data to set to positive.
     *
     * @return the positive value of the variable.
     */
    public static float abs(float value) {
        return (value < 0) ? -value : value;
    }

    /**
     * Gets the sign of the input value.
     *
     * @param value the value of the data to get the sign from.
     *
     * @return 1 or -1 depending on whether the {@code value} is positive or negative.
     */
    public static int sign(float value)
    {
        return (value < 0) ? -1 : 1;
    }

    public static float degreeToRadian(float value) {
        return value * PI / 180.0f;
    }

    public static float radianToDegree(float value) {
        return value * 180.0f / PI;
    }

    public static <T extends Comparable<T>> T wrap(T val, T minVal, T maxVal) {
        if (val.compareTo(minVal) < 0) {
            return maxVal;
        } else if (val.compareTo(maxVal) > 0) {
            return minVal;
        }
        return val;
    }

    public static <T extends Comparable<T>> T clamp(T val, T minVal, T maxVal) {
        if (val.compareTo(minVal) < 0) {
            return minVal;
        } else if (val.compareTo(maxVal) > 0) {
            return maxVal;
        }
        return val;
    }

    public static <T extends Comparable<T>> T min(T x, T y) {
        return (x.compareTo(y) < 0) ? x : y;
    }

    public static <T extends Comparable<T>> T max(T x, T y) {
        return (x.compareTo(y) > 0) ? x : y;
    }

    public static <T extends Number> double square(T x) {
        return x.doubleValue() * x.doubleValue();
    }

    private static final Random random = new Random();

    public static int randInt() {
        return random.nextInt();
    }

    public static int randIntMinMax(int min, int max) {
        return min + random.nextInt(max - min + 1);
    }

    public static float randFloat() {
        return random.nextFloat();
    }

    public static float randFloatMinMax(float min, float max) {
        return min + random.nextFloat() * (max - min);
    }


    public static double fastSqrt_d(double number){
        double x = number;
        double xhalf = 0.5d*x;
        long i = Double.doubleToLongBits(x);
        i = 0x5fe6ec85e7de30daL - (i>>1);
        x = Double.longBitsToDouble(i);
        for(int it = 0; it < 4; it++){
            x = x*(1.5d - xhalf*x*x);
        }
        x *= number;
        return x;
    }

    public static float fastSqrt(float number) {
        float xhalf = 0.5f * number;
        int i = Float.floatToIntBits(number);
        i = 0x5f3759df - (i >> 1);
        number = Float.intBitsToFloat(i);
        number *= (1.5f - xhalf * number * number);
        return 1 / number;
    }
}
