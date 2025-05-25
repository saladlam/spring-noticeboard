package info.saladlam.example.spring.noticeboard.framework.thymeleaf;

import info.saladlam.example.spring.noticeboard.framework.CspNonceFilter;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.model.IOpenElementTag;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractElementTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.util.Validate;

public abstract class CommonNonceTagProcessor extends AbstractElementTagProcessor {

    public static final int PRECEDENCE = 5000;
    public static final String ATTR_NAME = "nonce";

    protected CommonNonceTagProcessor(String dialectPrefix, String elementName) {
        super(TemplateMode.HTML, dialectPrefix, elementName, false, null, false, PRECEDENCE);
        Validate.notNull(elementName, "elementName cannot be null");
    }

    @Override
    protected void doProcess(ITemplateContext context, IProcessableElementTag tag, IElementTagStructureHandler structureHandler) {
        Object nonce = context.getVariable(CspNonceFilter.ATTRIBUTE_NAME);
        if ((tag instanceof IOpenElementTag) && (nonce instanceof String)) {
            structureHandler.removeAttribute(ATTR_NAME);
            structureHandler.setAttribute(ATTR_NAME, (String) nonce);
        }
    }

}
