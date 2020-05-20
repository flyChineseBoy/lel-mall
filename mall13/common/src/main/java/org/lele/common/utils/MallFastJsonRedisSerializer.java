package org.lele.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.security.core.session.SessionInformation;

import java.nio.charset.Charset;

/**
 * @Author : lele
 * @param <T>
 */
public class MallFastJsonRedisSerializer<T> extends FastJsonRedisSerializer<T> {
 
    public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
    private Class<T> clazz;
 
    public MallFastJsonRedisSerializer(Class<T> clazz) {
        super(clazz);
        this.clazz = clazz;
    }
 
    @Override
    public byte[] serialize(T t) throws SerializationException {
        return super.serialize(t);
    }
 
    @Override
    public T deserialize(byte[] bytes) throws SerializationException {
        if (bytes == null || bytes.length <= 0) {
            return null;
        }
        String str = new String(bytes, DEFAULT_CHARSET);

        if(  clazz == SessionInformation.class ){
            return (T)pasreSessionInformation(str);
        }

        return super.deserialize(bytes);
    }

    SessionInformation pasreSessionInformation( String str ){
        JSONObject jsonObject= JSON.parseObject("str");
        return new SessionInformation( jsonObject.get("principal"),jsonObject.getString("sessionId"),jsonObject.getDate("lastRequest") );
    }


}