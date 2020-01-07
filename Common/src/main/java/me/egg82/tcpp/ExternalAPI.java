package me.egg82.tcpp;

import co.aikar.commands.CommandIssuer;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLClassLoader;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import me.egg82.tcpp.api.Troll;
import me.egg82.tcpp.api.TrollType;

public class ExternalAPI {
    private static ExternalAPI api = null;

    private final Object concrete;
    private final Class<?> concreteClass;
    private final Class<?> exceptionClass;
    private final Object trollTypeConcrete;
    private final Class<?> trollTypeClass;

    private final ConcurrentMap<String, Method> methodCache = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, Method> exceptionMethodCache = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, Method> trollTypeMethodCache = new ConcurrentHashMap<>();

    private ExternalAPI(URLClassLoader classLoader) {
        if (classLoader == null) {
            throw new IllegalArgumentException("classLoader cannot be null.");
        }

        try {
            concreteClass = classLoader.loadClass("me.egg82.tcpp.TrollAPI");
            Constructor<?> constructor = concreteClass.getDeclaredConstructor();
            constructor.setAccessible(true);
            concrete = constructor.newInstance();
            exceptionClass = classLoader.loadClass("me.egg82.tcpp.APIException");
            trollTypeClass = classLoader.loadClass("me.egg82.tcpp.api.TrollType");
            Constructor<?> trollTypeConstructor = trollTypeClass.getDeclaredConstructor();
            trollTypeConstructor.setAccessible(true);
            trollTypeConcrete = trollTypeConstructor.newInstance();
        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException ex) {
            throw new RuntimeException("Could not get TrollAPI from classLoader.", ex);
        }
    }

    public static ExternalAPI getInstance() { return api; }

    public static void setInstance(URLClassLoader classLoader) {
        if (api != null) {
            throw new IllegalStateException("api is already set.");
        }
        api = new ExternalAPI(classLoader);
    }

    public boolean isTrolled(UUID playerID) throws APIException {
        try {
            return (Boolean) invokeMethod("isTrolled", playerID);
        } catch (NoSuchMethodException | IllegalAccessException ex) {
            throw new APIException(true, "Could not invoke base method.", ex);
        } catch (InvocationTargetException ex) {
            Throwable t = ex.getTargetException();
            if (t.getClass().getName().equals("me.egg82.tcpp.APIException")) {
                throw convertToAPIException(t);
            }
            throw new APIException(true, "Could not invoke base method.", ex);
        }
    }

    public boolean isTrolled(UUID playerID, TrollType type) throws APIException {
        try {
            return (Boolean) invokeMethod("isTrolled", playerID, getType(type));
        } catch (NoSuchMethodException | IllegalAccessException ex) {
            throw new APIException(true, "Could not invoke base method.", ex);
        } catch (InvocationTargetException ex) {
            Throwable t = ex.getTargetException();
            if (t.getClass().getName().equals("me.egg82.tcpp.APIException")) {
                throw convertToAPIException(t);
            }
            throw new APIException(true, "Could not invoke base method.", ex);
        }
    }

    public boolean startTroll(Troll troll, CommandIssuer issuer) throws APIException {
        try {
            return (Boolean) invokeMethod("startTroll", troll, issuer);
        } catch (NoSuchMethodException | IllegalAccessException ex) {
            throw new APIException(true, "Could not invoke base method.", ex);
        } catch (InvocationTargetException ex) {
            Throwable t = ex.getTargetException();
            if (t.getClass().getName().equals("me.egg82.tcpp.APIException")) {
                throw convertToAPIException(t);
            }
            throw new APIException(true, "Could not invoke base method.", ex);
        }
    }

    public boolean stopTroll(Troll troll, CommandIssuer issuer) throws APIException {
        try {
            return (Boolean) invokeMethod("stopTroll", troll, issuer);
        } catch (NoSuchMethodException | IllegalAccessException ex) {
            throw new APIException(true, "Could not invoke base method.", ex);
        } catch (InvocationTargetException ex) {
            Throwable t = ex.getTargetException();
            if (t.getClass().getName().equals("me.egg82.tcpp.APIException")) {
                throw convertToAPIException(t);
            }
            throw new APIException(true, "Could not invoke base method.", ex);
        }
    }

    public boolean stopTroll(UUID playerID, TrollType type, CommandIssuer issuer) throws APIException {
        try {
            return (Boolean) invokeMethod("stopTroll", playerID, getType(type), issuer);
        } catch (NoSuchMethodException | IllegalAccessException ex) {
            throw new APIException(true, "Could not invoke base method.", ex);
        } catch (InvocationTargetException ex) {
            Throwable t = ex.getTargetException();
            if (t.getClass().getName().equals("me.egg82.tcpp.APIException")) {
                throw convertToAPIException(t);
            }
            throw new APIException(true, "Could not invoke base method.", ex);
        }
    }

    public void stopAllTrolls(CommandIssuer issuer) throws APIException {
        try {
            invokeMethod("stopTrolls", issuer);
        } catch (NoSuchMethodException | IllegalAccessException ex) {
            throw new APIException(true, "Could not invoke base method.", ex);
        } catch (InvocationTargetException ex) {
            Throwable t = ex.getTargetException();
            if (t.getClass().getName().equals("me.egg82.tcpp.APIException")) {
                throw convertToAPIException(t);
            }
            throw new APIException(true, "Could not invoke base method.", ex);
        }
    }

    private Object invokeMethod(String name, Object... params) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Method tmp = methodCache.get(name);
        if (tmp == null) {
            synchronized (this) {
                tmp = methodCache.get(name);
                if (tmp == null) {
                    tmp = concreteClass.getMethod(name, getParamClasses(params));
                    methodCache.put(name, tmp);
                }
            }
        }

        return tmp.invoke(concrete, params);
    }

    private Object invokeExceptionMethod(String name, Throwable ex, Object... params) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Method tmp = exceptionMethodCache.get(name);
        if (tmp == null) {
            synchronized (this) {
                tmp = exceptionMethodCache.get(name);
                if (tmp == null) {
                    tmp = exceptionClass.getMethod(name, getParamClasses(params));
                    exceptionMethodCache.put(name, tmp);
                }
            }
        }

        return tmp.invoke(ex, params);
    }

    private Object invokeTrollTypeMethod(String name, Object... params) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Method tmp = trollTypeMethodCache.get(name);
        if (tmp == null) {
            synchronized (this) {
                tmp = trollTypeMethodCache.get(name);
                if (tmp == null) {
                    tmp = trollTypeClass.getMethod(name, getParamClasses(params));
                    trollTypeMethodCache.put(name, tmp);
                }
            }
        }

        return tmp.invoke(trollTypeConcrete, params);
    }

    private Class[] getParamClasses(Object[] params) {
        Class[] retVal = new Class[params.length];
        for (int i = 0; i < params.length; i++) {
            retVal[i] = (params[i] != null) ? params[i].getClass() : null;
        }
        return retVal;
    }

    private APIException convertToAPIException(Throwable e) throws APIException {
        try {
            boolean hard = (Boolean) invokeExceptionMethod("isHard", e);
            String message = (String) invokeExceptionMethod("getMessage", e);
            Throwable cause = (Throwable) invokeExceptionMethod("getCause", e);
            return new APIException(hard, message, cause);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ex) {
            throw new APIException(true, "Could not convert exception.", ex);
        }
    }

    private Object getType(TrollType type) throws APIException {
        try {
            return invokeTrollTypeMethod("fromName", type.getName());
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ex) {
            throw new APIException(true, "Could not convert exception.", ex);
        }
    }
}
