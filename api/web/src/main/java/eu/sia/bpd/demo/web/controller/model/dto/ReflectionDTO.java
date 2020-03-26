package eu.sia.bpd.demo.web.controller.model.dto;

import eu.sia.meda.util.ReflectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public abstract class ReflectionDTO<T extends Serializable> implements DTO<T> {

    @SuppressWarnings("unchecked")
    private final Class<T> entityClazz = (Class<T>) ReflectionUtils.getGenericTypeClass(getClass(), 0);

    public T toEntity() {
        final T entity;
        try {
            entity = entityClazz.newInstance();

            for (Field dtoField : getClass().getDeclaredFields()) {
                final String dtoFieldNameCapitalize = StringUtils.capitalize(dtoField.getName());

                Method dtoMethod;
                try {
                    dtoMethod = this.getClass().getMethod("get" + dtoFieldNameCapitalize);
                } catch (NoSuchMethodException e) {
                    try {
                        dtoMethod = this.getClass().getMethod("is" + dtoFieldNameCapitalize);
                    } catch (NoSuchMethodException ex) {
                        throw new RuntimeException(String.format("Reflection getter not found for field %s", dtoField.getName()));
                    }
                }

                final Method entitySetter;
                try {
                    entitySetter = entityClazz.getMethod("set" + dtoFieldNameCapitalize, dtoMethod.getReturnType());
                } catch (NoSuchMethodException e) {
                    throw new RuntimeException(String.format("Reflection setter not found for field %s", dtoField.getName()));
                }

                entitySetter.invoke(entity, dtoMethod.invoke(this));
            }

        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }

        return entity;
    }

}
