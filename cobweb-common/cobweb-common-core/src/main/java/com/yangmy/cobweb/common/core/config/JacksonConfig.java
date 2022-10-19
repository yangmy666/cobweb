package com.yangmy.cobweb.common.core.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.yangmy.cobweb.common.core.utils.TimeUtils;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * 自定义序列化配置，前后端全部采用时间戳交互时间
 * Long类型全部采用String序列化解决js只能接收17位以内数字的精度问题
 * @author YangMingYang
 * @since 2022/5/23
 */
@Configuration
public class JacksonConfig {

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return builder -> {
            //全局Long类型序列化成String
            builder.serializerByType(Long.class, ToStringSerializer.instance);
            //Long类型字段接收的String转换为Long
            builder.deserializerByType(Long.class,new LongDeserializer());
            //全局LocalDateTime序列化为时间戳
            builder.serializerByType(LocalDateTime.class, new LocalDateTimeSerializer());
            //全局接收前端时间戳转换为LocalDateTime
            builder.deserializerByType(LocalDateTime.class, new LocalDateTimeDeserializer());
        };
    }

    public static class LongDeserializer extends JsonDeserializer<Long> {
        @Override
        public Long deserialize(JsonParser p, DeserializationContext context) throws IOException {
            String s=p.getValueAsString();
            if(s==null||s.equals("")){
                return null;
            }
            return Long.valueOf(s);
        }
    }

    public static class LocalDateTimeSerializer extends JsonSerializer<LocalDateTime> {
        @Override
        public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider serializers)
                throws IOException {
            if (value != null) {
                long timestamp = TimeUtils.toTimestamp(value);
                gen.writeNumber(timestamp);
            }
        }
    }

    public static class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {
        @Override
        public LocalDateTime deserialize(JsonParser p, DeserializationContext context)
                throws IOException {
            long timestamp = p.getValueAsLong();
            if (timestamp > 0) {
                return TimeUtils.toLocalDateTime(timestamp);
            } else {
                return null;
            }
        }
    }

    /**
      * RequestParam注解传参的时间戳转行成LocalDateTime
      */
    @Bean
    public Converter<Long, LocalDateTime> localDateTimeConverter() {
        return new Converter<Long, LocalDateTime>() {
            @Override
            public LocalDateTime convert(Long source) {
                //毫秒级时间戳转LocalDateTime
                return TimeUtils.toLocalDateTime(source);
            }
        };
    }

}
