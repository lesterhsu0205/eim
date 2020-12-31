package eims.web.utils;

import java.util.UUID;

public class UidUtils {
	public static String genUUID(){
		String uid = UUID.randomUUID().toString();
		String strUid = uid.replaceAll("-", "");
		
		return  strUid;
	}
}
