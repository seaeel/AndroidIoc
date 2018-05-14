package shopping.com.androidioc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>描述:<p>
 *
 * @author guoweiquan
 * @version 1.0
 * @data 2018/5/14 下午2:25
 */


@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ViewBind
{
    int value();
}
