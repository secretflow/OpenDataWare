package com.cec.examine.dict;

import java.io.Serializable;

/**
 * 识别出来的实体
 */
public class HIT implements Serializable {
	
	public int start = 0;//起始位置
	public int end = 0;//结束位置
	public String source = "";//原始实体
	public String target = "";//替换的目标实体

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String type = "";//实体类型

	public int getStart() {
		return start;
	}

	public int getEnd() {
		return end;
	}

	public String getSource() {
		return source;
	}

	public String getTarget() {
		return target;
	}

	public String toString() {
		return "{\"start\":" + start + "," +
				"\"end\":" + end + "," +
				"\"source\":" + source + "," +
				"\"target\":" + target + "," +
				"\"type\":" + type + "}";
	}
}
