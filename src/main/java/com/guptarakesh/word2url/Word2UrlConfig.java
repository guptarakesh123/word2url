package com.guptarakesh.word2url;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.ini4j.Ini;
import org.ini4j.Config;

public class Word2UrlConfig {

	private Ini ini;
	private Config conf;
	private static Word2UrlConfig config;
	private static Map<String, String> urls;

	private Word2UrlConfig() {
	}

	public static Word2UrlConfig getInstance() {
		if (config != null) {
			return config;
		}

		try {
			config = new Word2UrlConfig();
			config.ini = new Ini();
			config.conf = new Config();
			config.conf.setMultiOption(true);
			config.ini.setConfig(config.conf);
			config.ini.load(new File("config.ini"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		return config;
	}

	public String[] getAllStrings(String section, String key) {
		Word2UrlConfig config = getInstance();
		Ini.Section iniSection = config.ini.get(section);
		if (iniSection == null || iniSection.isEmpty()) {
			return new String[] { "empty or null" };
		}

		String[] allStrings = iniSection.getAll(key, String[].class);
		return allStrings;
	}

	public Map<String, String[]> getSectionMap(String section) {
		Map<String, String[]> map = new HashMap<>();
		Word2UrlConfig config = getInstance();
		Ini.Section iniSection = config.ini.get(section);
		if (iniSection == null || iniSection.isEmpty()) {
			return map;
		}

		for (String key : iniSection.keySet()) {
			String[] allStrings = iniSection.getAll(key, String[].class);
			map.put(key, allStrings);
		}

		return map;
	}

	public Map<String, String> getSingleUrls() {

		if (urls == null) {
			Map<String, String[]> sectionMap = getSectionMap("urls");
			urls = new HashMap<>();

			for (String key : sectionMap.keySet()) {
				String[] urlList = sectionMap.get(key);

				if (urlList.length > 0) {
					urls.put(key, urlList[0]);
				}
			}
		}

		return urls;
	}
}