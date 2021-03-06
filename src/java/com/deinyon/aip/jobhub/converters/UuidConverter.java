package com.deinyon.aip.jobhub.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import java.util.UUID;

/**
 * A Java Server Faces datatype converter, which transforms a UUID object to
 * its String representation, and vice-versa.
 * @author Deinyon Davies <deinyond@gmail.com>
 */
@FacesConverter(forClass = UUID.class, value = "uuid")
public class UuidConverter implements Converter
{
    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
        return UUID.fromString(s);
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o) {
        if(!(o instanceof UUID))
            return null;

        return o.toString();
    }
}
