package in.jgssolution.study.CallbackMessage;

public interface ResponseCallback {
    void onSuccess(String message);
    void onFailure(String error);
}
