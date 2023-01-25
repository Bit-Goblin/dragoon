package tech.bitgoblin.config;

import org.apache.commons.cli.*;

public class Cmd {

	private String configPath = "~/.config/dragoon.toml";

	public Cmd(String[] args) throws ParseException {
		Options options = new Options();

    Option configPath = new Option("c", "configPath", true, "configuration file path (defaults to /etc/dragoon/config.toml)");
    configPath.setRequired(false);
    options.addOption(configPath);

    CommandLineParser parser = new DefaultParser();
    HelpFormatter formatter = new HelpFormatter();
    CommandLine cmd = parser.parse(options, args);

    // set the configPath variable if the option was passed to the program
    if (cmd.hasOption("configPath")) {
    	this.configPath = cmd.getOptionValue("configPath");
    }
	}

	public String getConfigPath() {
		return this.configPath;
	}

}
