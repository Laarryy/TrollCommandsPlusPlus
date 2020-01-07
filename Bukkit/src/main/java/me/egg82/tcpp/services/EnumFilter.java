package me.egg82.ae.services;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EnumFilter {
    private static final Logger logger = LoggerFactory.getLogger(EnumFilter.class);

    public static <T> EnumFilter.Builder<T> builder(Class<T> clazz) { return new EnumFilter.Builder<>(clazz); }

    public static class Builder<T> {
        private Class<T> clazz;
        private List<T> current;

        private Builder(Class<T> clazz) {
            if (clazz == null) {
                throw new IllegalArgumentException("clazz cannot be null.");
            }

            this.clazz = clazz;

            T[] enums = (clazz.isEnum()) ? clazz.getEnumConstants() : getStaticFields(clazz);
            current = new ArrayList<>(Arrays.asList((T[]) Arrays.copyOf(enums, enums.length, ((T[]) Array.newInstance(clazz, 0)).getClass())));
        }

        public EnumFilter.Builder<T> whitelist(String substring) {
            if (substring == null) {
                throw new IllegalArgumentException("substring cannot be null.");
            }

            substring = substring.toLowerCase();

            for (Iterator<T> i = current.iterator(); i.hasNext();) {
                T s = i.next();
                if (!s.toString().toLowerCase().contains(substring)) {
                    i.remove();
                }
            }

            return this;
        }

        public EnumFilter.Builder<T> blacklist(String substring) {
            if (substring == null) {
                throw new IllegalArgumentException("substring cannot be null.");
            }

            substring = substring.toLowerCase();

            for (Iterator<T> i = current.iterator(); i.hasNext();) {
                T s = i.next();
                if (s.toString().toLowerCase().contains(substring)) {
                    i.remove();
                }
            }

            return this;
        }

        public T[] build() {
            T[] retVal = (T[]) Array.newInstance(clazz, current.size());
            for (int i = 0; i < current.size(); i++) {
                retVal[i] = current.get(i);
            }

            return retVal;
        }

        private T[] getStaticFields(Class<?> clazz) {
            if (clazz == null) {
                throw new IllegalArgumentException("clazz cannot be null.");
            }

            Field[] fields = clazz.getDeclaredFields();
            ArrayList<Object> returns = new ArrayList<>();

            for (int i = 0; i < fields.length; i++) {
                if (!Modifier.isPrivate(fields[i].getModifiers())) {
                    try {
                        returns.add(fields[i].get(null));
                    } catch (IllegalAccessException ex) {
                        logger.warn(ex.getMessage(), ex);
                    }
                }
            }

            return returns.toArray((T[]) Array.newInstance(clazz, 0));
        }
    }
}
