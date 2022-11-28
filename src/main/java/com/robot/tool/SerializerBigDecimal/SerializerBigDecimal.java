package com.robot.tool.SerializerBigDecimal;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * bigdecimal 序列化保证精度不丢失。
 * @author 李绍奔
 * @description: TODO
 * @date: 2022/6/21 9:26
 */
public class SerializerBigDecimal extends JsonSerializer<BigDecimal> {
	@Override
	public void serialize(BigDecimal value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
		if(Objects.isNull(value)) {
			gen.writeNull();
		} else {
			// 这里取floor
			gen.writeNumber(value.setScale(2, RoundingMode.HALF_UP));
		}
	}
}
