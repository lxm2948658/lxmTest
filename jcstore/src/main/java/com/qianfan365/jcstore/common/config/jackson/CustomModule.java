package com.qianfan365.jcstore.common.config.jackson;

import java.io.IOException;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;

@Component
public class CustomModule extends SimpleModule {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  @Override
  public void setupModule(SetupContext context) {
    ObjectMapper owner = (ObjectMapper)context.getOwner();
    owner.getSerializerProvider().setNullValueSerializer(new JsonSerializer<Object>() {
      @Override
      public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers)
          throws IOException, JsonProcessingException {
        gen.writeString("");
      }
    });
    super.setupModule(context);
  }

}
