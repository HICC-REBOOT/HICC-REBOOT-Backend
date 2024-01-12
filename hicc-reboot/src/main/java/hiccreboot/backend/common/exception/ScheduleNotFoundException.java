package hiccreboot.backend.common.exception;

public class ScheduleNotFoundException extends CustomException {

    public static final CustomException EXCEPTION = new ScheduleNotFoundException();

    public ScheduleNotFoundException() {
        super(GlobalErrorCode.SCHEDULE_NOT_FOUND);
    }
}
