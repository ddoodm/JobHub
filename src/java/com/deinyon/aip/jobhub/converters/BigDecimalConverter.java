package com.deinyon.aip.jobhub.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import java.math.BigDecimal;

/**
 * A Java Server Faces datatype converter which transforms strings to
 * BigDecimals, and BigDecimals to Strings.
 * @author Deinyon Davies <deinyond@gmail.com>
 */
@FacesConverter(forClass = BigDecimal.class, value = "bigDecimal")
public class BigDecimalConverter implements Converter
{
    /**
     * Converts the String representation of a BigDecimal to a BigDecimal
     */
    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
        return new BigDecimal(s);
    }

    /**
     * Converts a BigDecimal to the String representation of a BigDecimal
     */
    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o) {
        if(!(o instanceof BigDecimal))
            return null;

        return o.toString();
    }
}
