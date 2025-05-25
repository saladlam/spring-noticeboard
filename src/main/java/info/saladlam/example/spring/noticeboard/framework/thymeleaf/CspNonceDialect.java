package info.saladlam.example.spring.noticeboard.framework.thymeleaf;

import org.thymeleaf.dialect.AbstractProcessorDialect;
import org.thymeleaf.processor.IProcessor;

import java.util.LinkedHashSet;
import java.util.Set;

public class CspNonceDialect extends AbstractProcessorDialect {

    public static final String NAME = "CSP nonce dialect";
    public static final String PREFIX = "th";
    public static final int PROCESSOR_PRECEDENCE = 1000;

    public CspNonceDialect() {
        super(NAME, PREFIX, PROCESSOR_PRECEDENCE);
    }

    public Set<IProcessor> getProcessors(String dialectPrefix) {
        final Set<IProcessor> processors = new LinkedHashSet<>();
        processors.add(new StyleNonceTagProcessor(dialectPrefix));
        processors.add(new ScriptNonceTagProcessor(dialectPrefix));
        return processors;
    }

}