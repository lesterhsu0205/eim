package eims.web.excel.drm;

import java.util.Hashtable;

import org.apache.commons.io.FilenameUtils;

import com.fasoo.adk.packager.WorkPackager;

import eims.web.excel.drm.exception.FasooDrmException;
import eims.web.excel.drm.exception.FileNotSupportedException;
import eims.web.excel.drm.exception.FileTypeException;

public class DrmUtil {

	private static final int FSN_ENCRYPTED_FILE = 103;
	private static final int NORMAL_FILE = 29;

	public static boolean decrypt(String inputFilePath, String outputFilePath)
			throws FasooDrmException, FileTypeException {

		WorkPackager workPackager = new WorkPackager();

		int inputFileType = workPackager.GetFileType(inputFilePath);

		switch (inputFileType) {
		case FSN_ENCRYPTED_FILE:
			Hashtable<?, ?> header = workPackager.GetFileHeader(inputFilePath);

			String CPID = (String) header.get("CPID");
			String keyDir = System.getenv("DRM_KEY_DIR");
			String domainId = System.getenv("DOMAIN_ID");

			System.out.println("-----------------------------------------------------------------------------------------------------------");
			System.out.println(keyDir);
			System.out.println("-----------------------------------------------------------------------------------------------------------");

			workPackager.DoExtract(keyDir, domainId, inputFilePath, outputFilePath);

			int lastErrorCode = workPackager.getLastErrorNum();
			if (lastErrorCode != 0) {
				throw new FasooDrmException(lastErrorCode, workPackager.getLastErrorStr());
			}

			return true;
		case NORMAL_FILE:
			return true;
		default:
			throw new FileTypeException(inputFilePath, FileType.ofValue(inputFileType));
		}
	}

	public static boolean encrypt(String inputFilePath, String outputFilePath)
			throws FileTypeException, FasooDrmException, FileNotSupportedException {

		WorkPackager workPackager = new WorkPackager();

		int inputFileType = workPackager.GetFileType(inputFilePath);

		switch (inputFileType) {
		case NORMAL_FILE:
			String keyDir = System.getenv("DRM_KEY_DIR");
			String domainId = System.getenv("DOMAIN_ID");

			if (workPackager.IsSupportFile(keyDir, domainId, inputFilePath)) {
				workPackager.setOverWriteFlag(false);

				String contentName = FilenameUtils.getName(inputFilePath);
				String userId = System.getenv("USER_ID");
				String userName = System.getenv("USER_NAME");
				String writerId = System.getenv("WRITER_ID");
				String writerName = System.getenv("WRITER_NAME");
				String writerDepartmentId = System.getenv("WRITER_DEPARTMENT_ID");
				String writerDepartmentName = System.getenv("WRITER_DEPARTMENT_NAME");
				String ownerId = System.getenv("OWNER_ID");
				String ownerName = System.getenv("OWNER_NAME");
				String ownerDepartmentId = System.getenv("OWNER_DEPARTMENT_ID");
				String ownerDepartmentName = System.getenv("OWNER_DEPARTMENT_NAME");
				String securityLevel = System.getenv("SECURITY_LEVEL");

				if (!workPackager.DoPackagingFsn2(keyDir, domainId, inputFilePath, outputFilePath, contentName, userId,
						userName, writerId, writerName, writerDepartmentId, writerDepartmentName, ownerId, ownerName,
						ownerDepartmentId, ownerDepartmentName, securityLevel)) {
					throw new FasooDrmException(workPackager.getLastErrorNum(), workPackager.getLastErrorStr());
				}
			} else {
				throw new FileNotSupportedException();
			}

			return true;
		case FSN_ENCRYPTED_FILE:
			return false;
		default:
			throw new FileTypeException(inputFilePath, FileType.ofValue(inputFileType));
		}
	}
}
