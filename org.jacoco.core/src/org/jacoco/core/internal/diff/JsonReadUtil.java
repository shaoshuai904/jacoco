/*******************************************************************************
 * Copyright (c) 2009, 2023 Mountainminds GmbH & Co. KG and Contributors
 * This program and the accompanying materials are made available under
 * the terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    Marc R. Hoffmann - initial API and implementation
 *
 *******************************************************************************/
package org.jacoco.core.internal.diff;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

/**
 * json 读取工具类
 */
public class JsonReadUtil {

	/**
	 * 读取本地json文件
	 */
	public static String readJsonToString(String filePath) {
		if (filePath == null || filePath.trim().isEmpty()) {
			return null;
		}
		return readFileIfExists(new File(filePath));
	}

	public static String readFileIfExists(File file) {
		if (!file.exists() || !file.isFile()) {
			return null;
		}
		try {
			String content = new String(Files.readAllBytes(file.toPath()),
					StandardCharsets.UTF_8);
			return content.trim();
		} catch (IOException e) {
			return null;
		}
		// try (BufferedReader reader = new BufferedReader(
		// // new FileReader(file)
		// new InputStreamReader(new FileInputStream(file),
		// StandardCharsets.UTF_8)
		// )) {
		// StringBuilder content = new StringBuilder();
		// String line;
		// while ((line = reader.readLine()) != null) {
		// content.append(line).append(System.lineSeparator());
		// }
		// return content.toString().trim();
		// } catch (Exception e) {
		// return null;
		// }
	}

	public static Map<String, DiffClassBean> getClassDiffJsonInfoMaps(
			String filePath) {
		if (filePath == null || filePath.trim().isEmpty()) {
			return Collections.emptyMap();
		}
		List<String> jsonList = new ArrayList<>();
		Path path = Paths.get(filePath);
		if (Files.isDirectory(path)) {
			try (Stream<Path> paths = Files.walk(path)) {
				paths.filter(Files::isRegularFile).filter(
						p -> p.getFileName().toString().endsWith(".json"))
						.forEach(p -> readJsonFile(p, jsonList));
			} catch (IOException e) {
				System.err.printf("遍历 [%s] 目录出现异常: %s%n", filePath,
						e.getMessage());
			}
		} else {
			readJsonFile(path, jsonList);
		}
		return buildClassInfoMap(jsonList);
	}

	private static void readJsonFile(Path file, List<String> jsonList) {
		try {
			// String content = JsonReadUtil.readFileIfExists(file.toFile());
			String content = new String(Files.readAllBytes(file),
					StandardCharsets.UTF_8).trim();
			if (null != content && !content.isEmpty()) {
				jsonList.add(content);
			}
		} catch (IOException e) {
			System.err.printf("读取文件[%s]失败: %s%n", file, e.getMessage());
		}
	}

	public static Map<String, DiffClassBean> buildClassInfoMap(String jsonStr) {
		List<String> jsonList = Collections.singletonList(jsonStr);
		return buildClassInfoMap(jsonList);
	}

	public static Map<String, DiffClassBean> buildClassInfoMap(
			List<String> jsonList) {
		Map<String, DiffClassBean> map = new HashMap<>();
		if (jsonList == null || jsonList.isEmpty())
			return map;
		Gson gson = new Gson();
		for (String jsonStr : jsonList) {
			try {
				if (jsonStr.startsWith("[")) { // 处理数组类型JSON
					List<DiffClassBean> dtos = gson.fromJson(jsonStr,
							new TypeToken<List<DiffClassBean>>() {
							}.getType());
					for (DiffClassBean dto : dtos) {
						map.put(dto.getClassName(), dto);
					}
				} else { // 处理对象类型JSON
					DiffClassBean dto = gson.fromJson(jsonStr,
							DiffClassBean.class);
					map.put(dto.getClassName(), dto);
				}
			} catch (Exception e) {
				System.err.println("JSON解析失败: " + e.getMessage());
			}
		}
		return map;
	}

	// public static void main(String[] args) {
	// String filePath = "/Users/rayduan/jacoco/a.json";
	// String content = readJsonToString(filePath);
	// System.out.println("文本内容：\n" + content);
	// }

}
