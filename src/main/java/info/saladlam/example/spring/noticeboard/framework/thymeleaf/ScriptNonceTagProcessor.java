package info.saladlam.example.spring.noticeboard.framework.thymeleaf;

public class ScriptNonceTagProcessor extends CommonNonceTagProcessor {

    public static final String TAG_NAME = "script";

    public ScriptNonceTagProcessor(String dialectPrefix) {
        super(dialectPrefix, TAG_NAME);
    }

}
