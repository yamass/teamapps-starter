package org.teamapps;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.impl.DataSourceConnectionProvider;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.impl.ThreadLocalTransactionProvider;
import org.teamapps.icon.material.MaterialIcon;
import org.teamapps.server.jetty.embedded.TeamAppsJettyEmbeddedServer;
import org.teamapps.ux.component.panel.Panel;
import org.teamapps.ux.component.rootpanel.RootPanel;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Objects;
import java.util.Properties;

public class Main {

	public static void main(String[] args) throws Exception {
		DSLContext dslContext = createDslContext();
		File webAppDirectory = Files.createTempDirectory("teamapps").toFile();
		TeamAppsJettyEmbeddedServer server = new TeamAppsJettyEmbeddedServer(context -> {
			RootPanel rootPanel = new RootPanel();
			context.addRootComponent(null, rootPanel);
			rootPanel.setContent(new Panel(MaterialIcon.ALARM, "Hello World!"));
		}, webAppDirectory);
		server.start();
	}

	private static DSLContext createDslContext() throws IOException {
		Properties hikariProperties = new Properties();
		hikariProperties.load(Objects.requireNonNull(Main.class.getClassLoader().getResourceAsStream("hikari.properties")));
		HikariConfig hikariConfig = new HikariConfig(hikariProperties);
		HikariDataSource dataSource = new HikariDataSource(hikariConfig);

		DefaultConfiguration jooqConfig = new DefaultConfiguration();
		DataSourceConnectionProvider connectionProvider = new DataSourceConnectionProvider(dataSource);
		jooqConfig.setConnectionProvider(connectionProvider);
		jooqConfig.setSQLDialect(SQLDialect.POSTGRES);
		jooqConfig.set(new ThreadLocalTransactionProvider(connectionProvider));

		return DSL.using(jooqConfig);
	}

}
