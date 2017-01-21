package helper;

import org.bson.types.ObjectId;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * Created by adam on 07.01.17.
 */

/**
 * Klasa serializująca i deserializująca id
 */
public class ObjectIdJaxAdapter extends XmlAdapter<String, ObjectId> {

    @Override
    public ObjectId unmarshal(String v) throws Exception {
        if (v == null)
            return null;
        return new ObjectId(v);
    }

    @Override
    public String marshal(ObjectId v) throws Exception {
        if (v == null)
            return null;
        return v.toString();
    }
}
