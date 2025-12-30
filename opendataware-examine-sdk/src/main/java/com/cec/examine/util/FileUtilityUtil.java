package com.cec.examine.util;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;


public class FileUtilityUtil {

	public static FileUtilityUtil getFileReader(String fileName) {
		return new FileUtilityUtil(fileName, "R");
	}

	public static FileUtilityUtil getFileReader(InputStream inputStream) {
		return new FileUtilityUtil(inputStream);
	}

	public static FileUtilityUtil getFileWriter(String fileName) {
		return new FileUtilityUtil(fileName,"W");
	}
	
	FileUtilityUtil(String fileName, String type) {
		if("R".equals(type)) {
			file = new File(fileName);
			try {
				read = new InputStreamReader(new FileInputStream(file));
				bufferedReader = new BufferedReader(read);
			} catch (Exception e) {
			}
		} else {
			try {
				fos = new FileOutputStream(fileName);
			} catch (Exception e) {
			}
		}
	}

	FileUtilityUtil(InputStream inputStream) {
		this.read = new InputStreamReader(inputStream);
		this.bufferedReader = new BufferedReader(read);
	}
	
	private File file = null;
	private InputStreamReader read = null;
	private BufferedReader bufferedReader = null;
	private String lineTxt = null;

	private FileOutputStream fos = null;


	public String getLine() {
		try {
			lineTxt = bufferedReader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(lineTxt == null) {
			try {
				bufferedReader.close();
				read.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return lineTxt;
	}

	/**
	 * 主要的不同在于他可以根据双引号跳过\N , 等特殊字符
	 * @param separater
	 * @return
	 */
	public String [] getCSVLineCells(String separater) {
		String line = this.getCSVLine();
		if(line != null) return split(line, separater);
		else return null;
	}

	/**
	 * 主要的不同在于他可以根据双引号跳过\N , 等特殊字符
	 * @return
	 */
	public String getCSVLine() {
		try {
			lineTxt = bufferedReader.readLine();
			if(lineTxt != null) {
				//修复单元格存在回车的问题
				String tmpLine = "";
				int countOfQuotation = 0;
				do {
					tmpLine += lineTxt;
					for (int j = 0; j < lineTxt.length(); j++) {
						if ('"' == lineTxt.charAt(j)) countOfQuotation++;
					}
					if (countOfQuotation % 2 == 1) {
						//如果发现是奇数，则继续往下读
						lineTxt = bufferedReader.readLine();
					} else break;
				} while (true);
				lineTxt = tmpLine;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(lineTxt == null) {
			try {
				bufferedReader.close();
				read.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return lineTxt;
	}
	
	public int getBytes(int size) {
		int readByte = -1;
		char [] cbuf = new char[1024];
		try {
			readByte = bufferedReader.read(cbuf);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(readByte == -1) {
			try {
				bufferedReader.close();
				read.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return readByte;
	}

	public List<String> getAllList() {
		List<String> r = new ArrayList<String>();
		try {
			while(true) {
				lineTxt = bufferedReader.readLine();
				if(lineTxt == null) {
					try {
						bufferedReader.close();
						read.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					break;
				} else {
					r.add(lineTxt);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return r;
	}

	public List<String> getCSVAllList() {
		List<String> r = new ArrayList<String>();
		while (true) {
			lineTxt = this.getCSVLine();
			if (lineTxt == null) {
				try {
					bufferedReader.close();
					read.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			} else {
				r.add(lineTxt);
			}
		}
		return r;
	}

	public String getAll() {
		StringBuilder r = new StringBuilder();
		try {
			while(true) {
				lineTxt = bufferedReader.readLine();
				if(lineTxt == null) {
					try {
						bufferedReader.close();
						read.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					break;
				} else {
					r.append(lineTxt);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return r.toString();
	}

	public void writeLine(String lineTxt) {
		try {
//			fos.write((lineTxt + "\n").getBytes(StandardCharsets.UTF_8));
			fos.write((lineTxt + System.lineSeparator()).getBytes(StandardCharsets.UTF_8));

//			fos.write((lineTxt).getBytes(StandardCharsets.UTF_8));

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void write(String text) {
		try {
			fos.write(text.getBytes(StandardCharsets.UTF_8));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void close() {
		try {
			if(fos !=null) {
				fos.flush();
				fos.close();
			} else if (read != null) {
				read.close();
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 流转文件，若存在则不转换
	 * @param inputStream
	 * @param filePath
	 * @return
	 */

	public static File convertInputStreamToFileIFNotExist(InputStream inputStream, String filePath) {
		File file = new File(filePath);
		if(file.exists()) {
			if(file.length() > 0) {
				return file;
			} else {
				file.delete();
			}
		}
		//若文件不存在，则把sdk包里面的数据库文件刷到磁盘
		try (OutputStream outputStream = new FileOutputStream(file)) {
			byte[] buffer = new byte[1024];
			int length;
			while ((length = inputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, length);
			}
			outputStream.flush();
			inputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return file;
	}

	/**
	 * 可以考虑带双引号的CSV文件，默认考虑
	 * @param s
	 * @param seperator
	 * @return
	 */
	public static String [] split(String s, String seperator) {
		return split(s, seperator, true);
	}
	/**
	 * 可以考虑带双引号的CSV文件
	 * @param s
	 * @param seperator
	 * @param considerDoubleQuotation
	 * @return
	 */
	public static String [] split(String s, String seperator, boolean considerDoubleQuotation) {
		if(!considerDoubleQuotation) {
			return s.split(seperator,-1);
		} else {
			//需要考虑双引号的连续性，如果一个单元格存在双引号则不参与分割;
			ArrayList<String> tmp = new ArrayList<String>();
			String [] strarr = s.split(seperator,-1);
			for(int i = 0; i < strarr.length; i++) {
				if(strarr[i].length() > 0 && strarr[i].charAt(0) == '"') {
					String cellStr = "";
					while (i < strarr.length) {
						cellStr += strarr[i] + seperator;
						if(strarr[i].length() > 0 && strarr[i].charAt(strarr[i].length()-1) == '"') {
							tmp.add(cellStr.substring(1,cellStr.length()-2));
							break;
						}
						i++;
					}
				} else tmp.add(strarr[i]);
			}
			String [] a = new String[tmp.size()];
			return tmp.toArray(a);
		}
	}

	public static String join(String [] cells, String sep) {
		String s = "";
		for(int i = 0; i < cells.length; i++ ) {
			if(i == cells.length -1 )
				s +=cells[i];
			else s += cells[i] + sep;
		}
		return s;
	}


}
