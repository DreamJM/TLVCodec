package com.dream.codec.tlv;

import com.dream.codec.tlv.annotation.MsgBean;
import com.dream.codec.tlv.utils.ClassUtils;

import java.lang.reflect.Field;
import java.util.List;

public class TLVContext {

    public static void init(String packageName) {
        List<Class<?>> classList = ClassUtils.getClasses(packageName);
        for (Class<?> clazz : classList) {
            if (clazz.isAnnotationPresent(MsgBean.class)) {
                MsgBean msgBean = clazz.getAnnotation(MsgBean.class);
                System.out.println(clazz.getName() + msgBean.type());
                Field[] fields = clazz.getDeclaredFields();
                for (Field field : fields) {

                }
            }
        }
    }


}
