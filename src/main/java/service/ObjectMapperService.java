package service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ObjectMapperService {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public <T> T readValue(String message, Class<T> valueType) {
        try {
            return objectMapper.readValue(message, valueType);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
}







