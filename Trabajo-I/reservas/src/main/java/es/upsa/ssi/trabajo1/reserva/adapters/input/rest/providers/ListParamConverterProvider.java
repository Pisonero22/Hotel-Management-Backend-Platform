package es.upsa.ssi.trabajo1.reserva.adapters.input.rest.providers;

import es.upsa.ssi.trabajo1.reserva.adapters.input.rest.providers.paramconverter.ListParamConverter;
import jakarta.ws.rs.ext.ParamConverter;
import jakarta.ws.rs.ext.ParamConverterProvider;
import jakarta.ws.rs.ext.Provider;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;


@Provider
public class ListParamConverterProvider implements ParamConverterProvider {

    @Override
    public <T> ParamConverter<T> getConverter(Class<T> aClass, Type type, Annotation[] annotations) {

        if(type instanceof ParameterizedType parameterizedType) {

            Class rawType = (Class)parameterizedType.getRawType();
            if(List.class.isAssignableFrom(rawType)){
                Class itemClass =(Class) parameterizedType.getActualTypeArguments()[0];
                if(String.class==itemClass){
                    return (ParamConverter<T>) new ListParamConverter(",");
                }
            }
        }
        return null;
    }
}
