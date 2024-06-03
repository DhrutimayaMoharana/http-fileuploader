package com.https.fileuploader.util;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Component;

@Component
public class FileUtility {

	public static boolean checkFileExistance(String fileLocation) {
		Path filePath = Paths.get(fileLocation);

		return Files.exists(filePath);
	}

}
