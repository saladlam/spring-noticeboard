package info.saladlam.example.spring.noticeboard.support;

import org.springframework.boot.context.TypeExcludeFilter;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;

// used in tests to exclude application's database configuration when annotated @SpringBootTest
public class DatabaseTypeExcludeFilter extends TypeExcludeFilter {

    @Override
    public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) {
        return "info.saladlam.example.spring.noticeboard.config.DatabaseConfig".equals(metadataReader.getClassMetadata().getClassName());
    }

    @Override
    public boolean equals(Object obj) {
        return (obj != null) && (getClass() == obj.getClass());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
