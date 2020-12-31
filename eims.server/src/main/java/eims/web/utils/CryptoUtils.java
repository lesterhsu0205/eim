package eims.web.utils;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import bxt.utils.org.commons.codec.Base64;
import eims.web.constants.BxConstants;
import eims.web.constants.SystemProperties;

public class CryptoUtils {

	private static final CryptoUtils instance = new CryptoUtils();


	private CryptoUtils() {
	}


	public static CryptoUtils instance() {
		return instance;
	}

	/**
	 * 알고리즘 정보
	 */
	public interface ALGORITHM_NAME {
		String MD5 = "MD5";
		String AES = "AES";
		String SHA_256 = "SHA-256";
		String AESWrap = "AESWrap";
	}

	/**
	 * 알고리즘 모드
	 */
	public interface ALGORITHM_MODE {
		String AES_CBC_PKCS5PADDING = "AES/CBC/PKCS5PADDING";
		String AES_CBC_ISO10126Padding = "AES/CBC/ISO10126Padding";
	}

	private static final String INITIALIZATION_VECTOR = "e511a66d84d977d2";
//	private static final String ENCRYPTION_KEY = "2f88ba962b490935";
	private static final String ENCRYPTION_KEY = SystemProperties.get(BxConstants.Keys.CRYPTO_KEY);

	/** Character set */
	private String charset = Charset.defaultCharset().name();


	/**
	 * MessageDigest 인스턴스 생성
	 *
	 * @param 알고리즘명
	 * @return MessageDigest
	 */
	public MessageDigest getMessageDigest(String algorithmName) throws NoSuchAlgorithmException {
		return MessageDigest.getInstance(algorithmName);
	}


	/**
	 * apache.commons.codec의 encodeBase64 이용한 암호화
	 *
	 * @param 암호화 대상 Byte
	 * @return 암호화된 문자열
	 */
	public String encodeBase64(byte[] raw) throws UnsupportedEncodingException {
		return new String(Base64.encodeBase64(raw), charset);
	}


	/**
	 * 해쉬된 바이트 문자열 구하기
	 *
	 * @param 알고리즘명
	 * @param 암호화 대상 Byte
	 * @return 바이트 배열로 해쉬된 바이트 반환
	 */
	public byte[] checksum(String algorithmName, byte[] data) throws NoSuchAlgorithmException {
		MessageDigest md = getMessageDigest(algorithmName);
		md.update(data);
		return md.digest();
	}


	/**
	 * SHA-256로 알고리즘으로 해수된 바이트 문자열 구하기
	 *
	 * @param 암호화 대상 Byte
	 * @return 바이트 배열로 해쉬된 바이트 반환
	 */
	public byte[] checksum_SHA256(byte[] data) throws NoSuchAlgorithmException {
		return checksum(ALGORITHM_NAME.SHA_256, data);
	}


	/**
	 * SHA-256로 알고리즘으로 해쉬된 바이트을 구해 encodeBase64 이용한 암호화 (파라미터 String)
	 *
	 * @param 암호화 대상 문자열
	 * @return 암호화된 문자열
	 */
	public String genChecksumSHA256(String targetString) throws Exception {
		byte raw[] = checksum_SHA256(targetString == null ? "".getBytes() : targetString.getBytes());
		return new String(Base64.encodeBase64(raw), charset);
	}


	/**
	 * SHA-256로 알고리즘으로 해쉬된 바이트을 구해 encodeBase64 이용한 암호화 (파라미터 Byte)
	 *
	 * @param 암호화 대상 Byte
	 * @return 암호화된 문자열
	 */
	public String genChecksumSHA256(byte[] target) throws Exception {
		byte raw[] = checksum_SHA256(target == null ? "".getBytes() : target);
		return new String(Base64.encodeBase64(raw), charset);
	}


	/**
	 * MD5로 알고리즘으로 해수된 바이트 문자열 구하기
	 *
	 * @param 암호화 대상 Byte
	 * @return 바이트 배열로 해쉬된 바이트 반환
	 */
	public byte[] checksum_MD5(byte[] data) throws NoSuchAlgorithmException {
		return checksum(ALGORITHM_NAME.MD5, data);
	}


	/**
	 * MD5로 알고리즘으로 해쉬된 바이트을 구해 encodeBase64 이용한 암호화 (파라미터 String)
	 *
	 * @param 암호화 대상 문자열
	 * @return 암호화된 문자열
	 */
	public String genChecksumMD5(String targetString) throws Exception {
		byte raw[] = checksum_MD5(targetString == null ? "".getBytes() : targetString.getBytes());
		return new String(Base64.encodeBase64(raw), charset);
	}


	/**
	 * MD5로 알고리즘으로 해쉬된 바이트을 구해 encodeBase64 이용한 암호화 (파라미터 Byte)
	 *
	 * @param 암호화 대상 Byte
	 * @return 암호화된 문자열
	 */
	public String genChecksumMD5(byte[] target) throws Exception {
		byte raw[] = checksum_MD5(target == null ? "".getBytes() : target);
		return new String(Base64.encodeBase64(raw), charset);
	}


	/**
	 * AES-128-CBC 이용한 encodeBase64 암호화 (파라미터 String)
	 *
	 * @param 평문문자열 (암호화 대상)
	 * @return 암호화된 문자열
	 */
	public String encrypt(String plainText) throws Exception {
		Cipher cipher = Cipher.getInstance(ALGORITHM_MODE.AES_CBC_PKCS5PADDING);
		SecretKeySpec key = new SecretKeySpec(ENCRYPTION_KEY.getBytes(charset), ALGORITHM_NAME.AES);
		cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(INITIALIZATION_VECTOR.getBytes(charset)));
		byte[] raw = cipher.doFinal(plainText.getBytes(charset));
		return new String(Base64.encodeBase64(raw), charset);
	}


	/**
	 * AES-128-CBC 이용한 encodeBase64 복호화 (파라미터 String)
	 *
	 * @param 암호화된 문자열
	 * @return 복호화된 평문 문자열
	 */
	public String decrypt(String encrypted) throws Exception {
		Cipher cipher = Cipher.getInstance(ALGORITHM_MODE.AES_CBC_PKCS5PADDING);
		SecretKeySpec key = new SecretKeySpec(ENCRYPTION_KEY.getBytes(charset), ALGORITHM_NAME.AES);
		cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(INITIALIZATION_VECTOR.getBytes(charset)));
		byte[] raw = Base64.decodeBase64(encrypted);
		return new String(cipher.doFinal(raw), charset);
	}


	/**
	 * AES-128-CBC 이용한 encodeBase64 암호화
	 * 암호화키를 appendStr를 고정된 위치에 추가하여 생성된 암호화키로 암호화 함
	 *
	 * @param 평문문자열 (암호화 대상)
	 * @param 암호화키 추가문자열
	 * @return 암호화된 문자열
	 */
	public String encrypt(String plainText, String appendStr) throws Exception {
		Cipher cipher = Cipher.getInstance(ALGORITHM_MODE.AES_CBC_PKCS5PADDING);
		SecretKeySpec key = new SecretKeySpec(getGeneratorKey(appendStr), ALGORITHM_NAME.AES);
		cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(INITIALIZATION_VECTOR.getBytes(charset)));
		byte[] raw = cipher.doFinal(plainText.getBytes(charset));
		return new String(Base64.encodeBase64(raw), charset);
	}


	/**
	 * AES-128-CBC 이용한 encodeBase64 복호화
	 * 암호화키를 appendStr를 고정된 위치에 추가하여 생성된 암호화키로 암호화 함
	 *
	 * @param 암호화된 문자열
	 * @param 암호화키 추가문자열
	 * @return 복호화된 평문 문자열
	 */
	public String decrypt(String encrypted, String appendStr) throws Exception {
		Cipher cipher = Cipher.getInstance(ALGORITHM_MODE.AES_CBC_PKCS5PADDING);
		SecretKeySpec key = new SecretKeySpec(getGeneratorKey(appendStr), ALGORITHM_NAME.AES);
		cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(INITIALIZATION_VECTOR.getBytes(charset)));
		byte[] raw = Base64.decodeBase64(encrypted);
		return new String(cipher.doFinal(raw), charset);
	}


	/**
	 * AES-128-CBC 이용한 encodeBase64 암호화
	 *
	 * @param 암호화key
	 * @param 벡터값
	 * @param 평문byte (암호화 대상)
	 * @return 암호화된 Byte
	 */
	public byte[] encrypt(byte[] key, byte[] iv, byte[] text) throws Exception {
		Cipher cipher = Cipher.getInstance(ALGORITHM_MODE.AES_CBC_PKCS5PADDING);
		SecretKeySpec sKey = new SecretKeySpec(key, ALGORITHM_NAME.AES);
		cipher.init(Cipher.ENCRYPT_MODE, sKey, new IvParameterSpec(iv));
		byte[] raw = cipher.doFinal(text);
		return Base64.encodeBase64(raw);
	}


	/**
	 * AES-128-CBC 이용한 encodeBase64 복호화
	 *
	 * @param 암호화key
	 * @param 벡터값
	 * @param 암호화된Byte
	 * @return 복호화된 Byte
	 */
	public byte[] decrypt(byte[] key, byte[] iv, byte[] encrypted) throws Exception {
		Cipher cipher = Cipher.getInstance(ALGORITHM_MODE.AES_CBC_PKCS5PADDING);
		SecretKeySpec sKey = new SecretKeySpec(key, ALGORITHM_NAME.AES);
		cipher.init(Cipher.DECRYPT_MODE, sKey, new IvParameterSpec(iv));
		byte[] raw = Base64.decodeBase64(encrypted);
		return cipher.doFinal(raw);
	}


	/**
	 * AES-128-CBC 이용한 encodeBase64 암호화
	 *
	 * @param 평문byte (암호화 대상)
	 * @return 암호화된 Byte
	 */
	public byte[] encrypt(byte[] text) throws Exception {
		Cipher cipher = Cipher.getInstance(ALGORITHM_MODE.AES_CBC_PKCS5PADDING);
		SecretKeySpec key = new SecretKeySpec(ENCRYPTION_KEY.getBytes(), ALGORITHM_NAME.AES);
		cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(INITIALIZATION_VECTOR.getBytes()));
		byte[] raw = cipher.doFinal(text);
		return Base64.encodeBase64(raw);
	}


	/**
	 * AES-128-CBC 이용한 encodeBase64 복호화
	 *
	 * @param 암호화된Byte
	 * @return 복호화된 Byte
	 */
	public byte[] decrypt(byte[] encrypted) throws Exception {
		Cipher cipher = Cipher.getInstance(ALGORITHM_MODE.AES_CBC_PKCS5PADDING);
		SecretKeySpec key = new SecretKeySpec(ENCRYPTION_KEY.getBytes(), ALGORITHM_NAME.AES);
		cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(INITIALIZATION_VECTOR.getBytes()));
		byte[] raw = Base64.decodeBase64(encrypted);
		return cipher.doFinal(raw);
	}


	/**
	 * 암복화키 Generator
	 *
	 * @param appendStr
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private byte[] getGeneratorKey(String appendStr) throws UnsupportedEncodingException {
		if (appendStr == null) {
			return ENCRYPTION_KEY.getBytes(charset);
		} else {
			StringBuilder sb = new StringBuilder();
			if (appendStr.length() < 4) {
				return ENCRYPTION_KEY.getBytes(charset);
			} else {
				sb.append(ENCRYPTION_KEY.substring(0, 9));
				sb.append(appendStr.substring(0, 4));
				sb.append(ENCRYPTION_KEY.substring(13));
				return sb.toString().getBytes(charset);
			}
		}
	}
}
