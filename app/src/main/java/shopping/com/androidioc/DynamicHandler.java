package shopping.com.androidioc;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * <p>描述:<p>
 *
 * @author guoweiquan
 * @version 1.0
 * @data 2018/5/15 上午11:28
 */


public class DynamicHandler implements InvocationHandler {
    private WeakReference<Object> handlerRef;
    private final HashMap<String, Method> methodMap = new HashMap<String, Method>();
    public DynamicHandler(Object handler)
    {
        this.handlerRef = new WeakReference<Object>(handler);
    }

    public void addMethod(String name, Method method)
    {
        methodMap.put(name, method);
    }

    public Object getHandler()
    {
        return handlerRef.get();
    }

    public void setHandler(Object handler)
    {
        this.handlerRef = new WeakReference<Object>(handler);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args)
            throws Throwable
    {
        Object handler = handlerRef.get();
        if (handler != null)
        {
            String methodName = method.getName();
            method = methodMap.get(methodName);
            if (method != null)
            {
                return method.invoke(handler, args);
            }
        }
        return null;
    }
}