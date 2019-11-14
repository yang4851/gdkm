package com.insilicogen.gdkm.util;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class JsonUtil {

	/**
	 * object -> json
	 * 
	 * @author munstar
	 * @date 2017. 11. 3.
	 */
	public static String getJsonString(Object obj) {
		String json_job = "";
		ObjectMapper mapper = new ObjectMapper();
		try {
			json_job = mapper.writeValueAsString(obj);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return json_job;
	}

	/**
	 * object -> json
	 * 
	 * @author iksu
	 * @date 2017. 11. 3.
	 */
	public static String getJsonString2(Object obj) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String json_job = ow.writeValueAsString(obj);
		return json_job;
	}

	/**
	 * json -> List<VO>
	 * 
	 * @author iksu
	 * @date 2017. 11. 6.
	 */
	public static Object getJsonToList(String stjson, Class<?> m) {
		ObjectMapper mapper = new ObjectMapper();
		TypeFactory typeFactory = mapper.getTypeFactory();

		try {
			return mapper.readValue(stjson, typeFactory.constructCollectionType(List.class, m));
		} catch (IOException e) {
			e.printStackTrace();
		}

		return Collections.emptyList();
	}

	/**
	 * json(string) -> Map (쌍따옴표가 없어짐...)json 형태가아님... json형태로 변환하려면
	 * getJsonString을 사용
	 * 
	 * @author iksu
	 * @date 2017. 12. 6.
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> getMapFromJsonString(String string) {

		Map<String, Object> map = null;

		try {

			map = new ObjectMapper().readValue(string, Map.class);

		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return map;
	}

	/**
	 * json -> VO.class
	 * 
	 * @author munstar
	 * @date 2017. 11. 3.
	 */
	public static Object getObject(String stjson, Class<?> m) {
		ObjectMapper mapper = new ObjectMapper();
		Object vo = null;
		try {
			vo = mapper.readValue(stjson, m);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
		} catch (IOException e) {
			e.printStackTrace();
		}
		return vo;
	}
}
