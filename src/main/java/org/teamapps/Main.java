package org.teamapps;

import org.teamapps.icon.material.MaterialIcon;
import org.teamapps.server.jetty.embedded.TeamAppsJettyEmbeddedServer;
import org.teamapps.ux.component.panel.Panel;
import org.teamapps.ux.component.rootpanel.RootPanel;

import java.io.File;
import java.nio.file.Files;

public class Main {

	public static void main(String[] args) throws Exception {
		File webAppDirectory = Files.createTempDirectory("teamapps").toFile();
		TeamAppsJettyEmbeddedServer server = new TeamAppsJettyEmbeddedServer(context -> {
			RootPanel rootPanel = new RootPanel();
			context.addRootComponent(null, rootPanel);
			rootPanel.setContent(new Panel(MaterialIcon.ALARM, "Hello World!"));
		}, webAppDirectory);
		server.start();
	}

}
