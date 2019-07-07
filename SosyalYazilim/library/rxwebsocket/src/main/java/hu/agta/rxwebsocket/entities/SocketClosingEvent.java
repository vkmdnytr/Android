package hu.agta.rxwebsocket.entities;


import hu.agta.rxwebsocket.SocketEventTypeEnum;

public class SocketClosingEvent extends SocketEvent {

    private final int code;
    private final String reason;

    public SocketClosingEvent(int code, String reason) {
        super(SocketEventTypeEnum.CLOSING);
        this.code = code;
        this.reason = reason;
    }

    public int getCode() {
        return code;
    }

    public String getReason() {
        return reason;
    }

    @Override
    public String toString() {
        return "SocketClosingEvent{" +
                "code=" + code +
                ", reason='" + reason + '\'' +
                '}';
    }
}
