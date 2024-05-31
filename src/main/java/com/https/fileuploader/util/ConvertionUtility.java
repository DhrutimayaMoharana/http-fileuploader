package com.https.fileuploader.util;

import java.util.Base64;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ConvertionUtility {

	public static String encodeToBase64(byte[] byteArray) {
		return Base64.getEncoder().encodeToString(byteArray);
	}

	public static byte[] decodeBase64ToByteArray(String base64String) {
		return Base64.getDecoder().decode(base64String);
	}

	public static byte[] convertMapToByteArray(Map<String, Object> map) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			return objectMapper.writeValueAsBytes(map);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	public static Map<String, Object> convertByteArrayToMap(byte[] byteArray) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			return objectMapper.readValue(byteArray, new TypeReference<Map<String, Object>>() {
			});
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	public static double bytesToKilobytes(long bytes) {
		return bytes / 1024.0;
	}

}
