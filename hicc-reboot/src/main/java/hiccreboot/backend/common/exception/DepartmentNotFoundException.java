package hiccreboot.backend.common.exception;

public class DepartmentNotFoundException extends CustomException {
	public static final CustomException EXCEPTION = new DepartmentNotFoundException();

	public DepartmentNotFoundException() {
		super(GlobalErrorCode.DEPARTMENT_NOT_FOUND);
	}
}
