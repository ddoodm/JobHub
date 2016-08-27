package com.deinyon.aip.jobhub.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import java.math.BigDecimal;

/**
 * Created by Ddoodm on 8/24/2016.
 */
@FacesConverter(forClass = BigDecimal.class, value = "bigDecimal")
public class BigDecimalConverter implements Converter
{
    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
        return new BigDecimal(s);
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o) {
        if(!(o instanceof BigDecimal))
            return null;

        return o.toString();
    }
}
