package com.app.rurban.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ResponseDTO {
    private boolean success;
    private String error;
    private String message;
    private Object data;

    public ResponseDTO() {
        this.data = new ObjectMapper().createObjectNode();  // Initialize as an ObjectNode for JSON-like structure
    }

    public void addDataField(String key, Object value) {
        if (this.data instanceof ObjectNode) {
            ((ObjectNode) this.data).putPOJO(key, value);  // Add fields to the ObjectNode
        }
    }
}
