package eims.web.excel.drm;

import java.util.stream.Stream;

public enum FileType {
	NOT_EXIST(20, "File Not Exist"),
	SIZE_ZERO(21, "File Size Zero"),
	CANNOT_READ(22, "File Unable to Read"),
	NORMAL_FILE(29, "File Not Encrypted"),
	FSD_ENCRYPTED(26, "FSD Type Encryption"),
	WRAPSODY_ENCRYPTED(105, "Markany Type Encryption"),
	MARKANY_ENCRYPTED(101, "Wrapsody Type Encryption"),
	INCAPS_ENCRYPTED(104, "INCAPS Type Encryption"),
	FSN_ENCRYPTED(103, "FSN Type Encryption"),
	UNKNOWN(Integer.MAX_VALUE, "Unknown Type");

	private int code;
	private String description;


	private FileType(int code, String description) {

		this.code = code;
		this.description = description;
	}


	public static FileType ofValue(int codeValue) {

		return Stream.of(FileType.values()).filter((maybeFileType) -> (maybeFileType.code == codeValue)).findFirst()
				.orElse(UNKNOWN);
	}


	public int getCode() {
		return code;
	}


	public String getDescription() {
		return description;
	}
}
