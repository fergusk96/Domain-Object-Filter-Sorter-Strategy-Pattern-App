package com.fergus.intercom.service.filereader;

import java.io.IOException;
import java.util.List;

public interface IFileReaderService {
	
	public List<String> readFileAsStringList() throws IOException;
	
	public String getFileUri();

	

}
