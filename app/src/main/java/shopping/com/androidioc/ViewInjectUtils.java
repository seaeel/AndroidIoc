package shopping.com.androidioc;

import android.app.Activity;
import android.view.View;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;


/**
 * <p>描述:<p>
 *
 * @author guoweiquan
 * @version 1.0
 * @data 2018/5/14 下午2:19
 */


public class ViewInjectUtils {
    private static final String SETCONTENTVIEW = "setContentView";
    private static final String FINDVIEWBYID = "findViewById";
    private static final String EVENTMETHOD = "setOnClickListener";
    private static final Class EVENTTYPE = View.OnClickListener.class;
    private static final String METHODNAME = "onClick";

    public static void inject(Activity activity)
    {

        bindContentView(activity);
        bindViews(activity);
        bindEvents(activity);
    }
    /**
     * 绑定主布局文件
     *
     * @param activity
     */
    private static void bindContentView(Activity activity)
    {
        Class<? extends Activity> clazz = activity.getClass();
        SetContentView contentView = clazz.getAnnotation(SetContentView.class);// 查询类上SetContentView注解
        if (contentView != null)
        {
            int contentViewLayoutId = contentView.value();
            try
            {
                Method method = clazz.getMethod(SETCONTENTVIEW,
                        int.class);
                method.setAccessible(true);//设置可以访问private域
                method.invoke(activity, contentViewLayoutId);
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    /**
     * 绑定所有的控件
     *
     * @param activity
     */
    private static void bindViews(Activity activity)
    {
        Class<? extends Activity> clazz = activity.getClass();
        Field[] fields = clazz.getDeclaredFields();//获取自己声明的各种字段，包括public，protected，private
        for (Field field : fields)
        {

            ViewBind viewInjectAnnotation = field
                    .getAnnotation(ViewBind.class);
            if (viewInjectAnnotation != null)
            {
                int viewId = viewInjectAnnotation.value();
                if (viewId != -1)
                {
                    try
                    {
                        Method method = clazz.getMethod(FINDVIEWBYID,
                                int.class);
                        Object view = method.invoke(activity, viewId);//找到相应控件对象
                        field.setAccessible(true);
                        field.set(activity, view);//给本字段赋值
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                }
            }

        }

    }

    /**
     * 绑定所有的事件
     *
     * @param activity
     */
    private static void bindEvents(Activity activity)
    {

        Class<? extends Activity> clazz = activity.getClass();
        Method[] methods = clazz.getMethods();
        //遍历所有的方法
        for (Method method : methods)
        {
            //拿到方法上的所有的注解
            Annotation[] annotations = method.getAnnotations();
            for (Annotation annotation : annotations)
            {
                //获取注解类型   OnClick
                Class<? extends Annotation> annotationType = annotation
                        .annotationType();
                //判断是否为OnClick注解
                if(annotationType.getName().equals(OnClick.class.getName()))
                {
                    try
                    {
                        //拿到Onclick注解中的value方法
                        Method method1 = annotationType
                                .getDeclaredMethod("value");
                        //取出所有的viewId
                        int[] viewIds = (int[]) method1.invoke(annotation);
                        //设置动态代理
                        DynamicHandler handler = new DynamicHandler(activity);
                        handler.addMethod(METHODNAME, method);//添加onClick方法
                        Object listener = Proxy.newProxyInstance(
                                EVENTTYPE.getClassLoader(),
                                new Class<?>[] { EVENTTYPE }, handler);

//                        BindListener listener = new BindListener(activity);
//                        listener.setmMethod(method);
                        //遍历所有的View，设置事件
                        for (int viewId : viewIds)
                        {
                            View view = activity.findViewById(viewId);
                            Method setEventListenerMethod = view.getClass().getMethod(EVENTMETHOD, EVENTTYPE);
                            setEventListenerMethod.invoke(view, listener);
                        }

                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }

            }
        }

    }



}
