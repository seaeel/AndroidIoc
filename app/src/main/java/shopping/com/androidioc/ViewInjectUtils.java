package shopping.com.androidioc;

import android.app.Activity;
import java.lang.reflect.Field;
import java.lang.reflect.Method;


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
    public static void inject(Activity activity)
    {

        bindContentView(activity);
        bindViews(activity);

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
}
