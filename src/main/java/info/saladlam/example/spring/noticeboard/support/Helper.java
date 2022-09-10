package info.saladlam.example.spring.noticeboard.support;

import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

public abstract class Helper {

	public static EmbeddedDatabase getEmbeddedDatabase(String name, String ... addScripts) {
		EmbeddedDatabaseBuilder db = new EmbeddedDatabaseBuilder()
			.setType(EmbeddedDatabaseType.H2)
			.setName(name)
			.setScriptEncoding("UTF-8")
			.addScript("classpath:/sql/dbschema.sql");
		for (String script : addScripts) {
			db.addScript(script);
		}
		return db.build();
	}

}
