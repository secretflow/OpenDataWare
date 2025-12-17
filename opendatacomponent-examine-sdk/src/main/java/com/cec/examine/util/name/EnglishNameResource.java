package com.cec.examine.util.name;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipInputStream;

public class EnglishNameResource {
	
    private static List<String> getResource(String resourceName)
    {
    	List<String> textList = new ArrayList<String>();
        ZipInputStream zip = new ZipInputStream(EnglishNameResource.class.getResourceAsStream(resourceName),StandardCharsets.UTF_8);
        try
        {
        	zip.getNextEntry();
            InputStreamReader inputStreamReader = new InputStreamReader(zip);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
    		String line;
    		while ((line = bufferedReader.readLine()) != null) {
    			textList.add(line);
    		}
    		inputStreamReader.close();
    		bufferedReader.close();			
        }
        catch (IOException e) {
        	e.printStackTrace();
        }
        return textList;
    }

    protected static List<String> getEnglishSingleNameTable()
    {
        String resourceName = "/data/english_single_name.db";
        return getResource(resourceName);
    }

    protected static List<String> getEnglishSpaceNameTable()
    {
        String resourceName = "/data/english_space_name.db";
        return getResource(resourceName);
    }
}