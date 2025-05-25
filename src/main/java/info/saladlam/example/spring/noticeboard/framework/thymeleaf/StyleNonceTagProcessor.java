package info.saladlam.example.spring.noticeboard.framework.thymeleaf;

public class StyleNonceTagProcessor extends CommonNonceTagProcessor {

    public static final String TAG_NAME = "style";

    public StyleNonceTagProcessor(String dialectPrefix) {
        super(dialectPrefix, TAG_NAME);
    }

}
