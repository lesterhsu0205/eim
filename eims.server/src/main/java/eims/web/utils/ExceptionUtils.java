package eims.web.utils;

import bxt.utils.SystemUtils;
import bxt.utils.SystemUtils.STACK_TRACE_TYPE;

public class ExceptionUtils {

	private static final ExceptionUtils instance = new ExceptionUtils();

	private ExceptionUtils() {
	}

	public static ExceptionUtils instance() {
		return instance;
	}

	public String printException(Throwable e) {
		StringBuilder builder = new StringBuilder();
		recursivePrintException(builder, e, e != null ? e.getCause() : null, 0, false);
		return builder.toString();
	}

	public String printExceptionAll(Throwable e) {
		StringBuilder builder = new StringBuilder();
		recursivePrintException(builder, e, e != null ? e.getCause() : null, 0, true);
		return builder.toString();
	}

	private void recursivePrintException(StringBuilder builder, Throwable e, Throwable cause, int causeCnt,
			boolean isAllTrace) {
		if (causeCnt > 0) {
			builder.append(", CAUSE-").append(causeCnt).append(" ");
		}

		if (e == null) {
			builder.append("");
		} else {
			builder.append(System.lineSeparator()).append(e.getMessage()).append(" ").append(e.getClass().getName())
					.append(System.lineSeparator());
			if (!isAllTrace) {
				SystemUtils.instance().getStackTrace(builder, e.getStackTrace(), STACK_TRACE_TYPE.MULTI_LINE);
			} else {
				SystemUtils.instance().getAllStackTrace(builder, e.getStackTrace(), STACK_TRACE_TYPE.MULTI_LINE);
			}
		}

		if (cause != null && cause != e) {
			recursivePrintException(builder, cause, cause.getCause(), causeCnt + 1, isAllTrace);
		}
	}
}