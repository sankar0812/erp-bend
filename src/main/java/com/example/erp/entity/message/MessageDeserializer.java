package com.example.erp.entity.message;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import java.io.IOException;

public class MessageDeserializer extends StdDeserializer<Message> {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MessageDeserializer() {
        this(null);
    }

    public MessageDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Message deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        JsonNode node = jp.getCodec().readTree(jp);

        // Extract values from the JSON node
        Long senderId = node.get("senderId").asLong();
        Long receiverId = node.get("receiverId").asLong();
        String message = node.get("message").asText();

        // Create a new Message object using the extracted values
        Message messageObject = new Message();
        messageObject.setSenderId(senderId);
        messageObject.setReceiverId(receiverId);
        messageObject.setMessage(message);

        return messageObject;
    }
}
