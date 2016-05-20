package ro.trenulmeu.mobile.api.typeadapter;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

import ro.trenulmeu.mobile.timespan.TimeSpan;

public class TimeSpanTypeAdapter implements JsonSerializer<TimeSpan>, JsonDeserializer<TimeSpan> {

    @Override
    public JsonElement serialize(TimeSpan src, Type srcType, JsonSerializationContext context) {
        return new JsonPrimitive(src.toString() + ":00");
    }

    @Override
    public TimeSpan deserialize(JsonElement json, Type type, JsonDeserializationContext context)
            throws JsonParseException {
        return new TimeSpan(json.getAsString());
    }

}
