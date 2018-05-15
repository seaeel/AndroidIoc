package shopping.com.androidioc;

import android.util.Log;
import android.view.View;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * <p>描述:<p>
 *
 * @author guoweiquan
 * @version 1.0
 * @data 2018/5/15 下午3:32
 */


public class BindListener implements View.OnClickListener {
    private Method mMethod;

    public void setmMethod(Method mMethod) {
        this.mMethod = mMethod;
    }
    private Object obj;
    public BindListener(Object obj)
    {
        this.obj = obj;
    }

    @Override
    public void onClick(View v) {
        try {
            mMethod.invoke(obj,v);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
