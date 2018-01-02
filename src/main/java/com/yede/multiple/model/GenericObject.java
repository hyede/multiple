package com.yede.multiple.model;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.lang.reflect.Field;
@Slf4j
public abstract class GenericObject implements Serializable {



    @Override
    public int hashCode() {
        int result = super.hashCode();
        try {
            int prime = 31;
            Field[] fields = this.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                Object value = field.get(this);
                result = result * prime + (value == null ? 0 : value.hashCode());
            }
        } catch (Exception e) {

        }
        return result;

    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}