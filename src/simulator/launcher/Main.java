package simulator.launcher;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.json.JSONObject;
import org.json.JSONTokener;

import simulator.factories.*;
import simulator.misc.Utils;
import simulator.model.SelectionStrategy;
import simulator.view.SimpleObjectViewer;

import javax.swing.plaf.basic.BasicSplitPaneUI;

public class Main {
	private enum ExecMode {
		BATCH("batch", "Batch mode"), GUI("gui", "Graphical User Interface mode");

		private String _tag;
		private String _desc;

		private ExecMode(String modeTag, String modeDesc) {
			_tag = modeTag;
			_desc = modeDesc;
		}

		public String get_tag() {
			return _tag;
		}

		public String get_desc() {
			return _desc;
		}
	}

	// default values for some parameters
	//
	private final static Double _default_time = 10.0; // in seconds
	private final static Double _default_dt = 0.03;

	// some attributes to stores values corresponding to command-line parameters
	//
	private static Double _time = null;
	private static String _in_file = null;
	private static ExecMode _mode = ExecMode.BATCH;

	private static void parse_args(String[] args) {

		// define the valid command line options
		//
		Options cmdLineOptions = build_options();

		// parse the command line as provided in args
		//
		CommandLineParser parser = new DefaultParser();
		try {
			CommandLine line = parser.parse(cmdLineOptions, args);
			parse_help_option(line, cmdLineOptions);
			parse_in_file_option(line);
			parse_time_option(line);
			parse_dtime_option(line);
			parse_sv_option(line, cmdLineOptions);
			parse_out_file_option(line);

			// if there are some remaining arguments, then something wrong is
			// provided in the command line!
			//
			String[] remaining = line.getArgs();
			if (remaining.length > 0) {
				String error = "Illegal arguments:";
				for (String o : remaining)
					error += (" " + o);
				throw new ParseException(error);
			}

		} catch (ParseException e) {
			System.err.println(e.getLocalizedMessage());
			System.exit(1);
		} catch (IOException e) {
            throw new RuntimeException(e);
        }

    }



	private static Options build_options() {
		Options cmdLineOptions = new Options();

		// help .
		cmdLineOptions.addOption(Option.builder("h").longOpt("help").desc("Print this message.").build());

		// input file
		cmdLineOptions.addOption(Option.builder("i").longOpt("input").hasArg().desc("A configuration file.").build());

		// steps
		cmdLineOptions.addOption(Option.builder("t").longOpt("time").hasArg()
				.desc("An real number representing the total simulation time in seconds. Default value: "
						+ _default_time + ".")
				.build());

		cmdLineOptions.addOption(Option.builder("dt").longOpt("delta time").hasArg()
				.desc("A double representing actual time, in seconds, per simulation step. Default value: "
						+ _default_dt + ".")
				.build());

		cmdLineOptions.addOption(Option.builder("sv").longOpt("simple-viewer").desc("Show the viewer window in console mode.").build());

		cmdLineOptions.addOption(Option.builder("o").longOpt("output").hasArg().desc("Output file. where output is written").build());
		return cmdLineOptions;
	}
	private static void parse_dtime_option(CommandLine line) throws ParseException {
		String t = line.getOptionValue("dt", _default_dt.toString());
		try {
			_time = Double.parseDouble(t);
			assert (_time >= 0);
		} catch (Exception e) {
			throw new ParseException("Invalid value for time: " + t);
		}
	}
	private static void parse_help_option(CommandLine line, Options cmdLineOptions) {
		if (line.hasOption("h")) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp(Main.class.getCanonicalName(), cmdLineOptions, true);
			System.exit(0);
		}
	}
	private static void parse_sv_option(CommandLine line, Options cmdLineOptions) {
		if (line.hasOption("sv")) {
			//SimpleObjectViewer objectViewer = new SimpleObjectViewer();
			//formatter.printHelp(Main.class.getCanonicalName(), cmdLineOptions, true);
			System.exit(0);
		}
	}

	private static void parse_in_file_option(CommandLine line) throws ParseException {
		_in_file = line.getOptionValue("i");
		if (_mode == ExecMode.BATCH && _in_file == null) {
			throw new ParseException("In batch mode an input configuration file is required");
		}
	}
	private static void parse_out_file_option(CommandLine line) throws ParseException, IOException {
		_in_file = line.getOptionValue("o");
		Path _out_file = Paths.get("out.json");
		Files.write(_out_file, _in_file.getBytes(), (OpenOption) StandardCharsets.UTF_8);

	}


	private static void parse_time_option(CommandLine line) throws ParseException {
		String t = line.getOptionValue("t", _default_time.toString());
		try {
			_time = Double.parseDouble(t);
			assert (_time >= 0);
		} catch (Exception e) {
			throw new ParseException("Invalid value for time: " + t);
		}
	}

	private static void init_factories() {
		// initialize the strategies factory
		List<Builder<SelectionStrategy>> selection_strategy_builders
				= new ArrayList<>();
		selection_strategy_builders.add(new SelectFirstBuilder());
		selection_strategy_builders.add(new SelectClosestBuilder());
		Factory<SelectionStrategy> selection_strategy_factory
				= new BuilderBasedFactory<SelectionStrategy>(selection_strategy_builders);
	}

	private static JSONObject load_JSON_file(InputStream in) {
		return new JSONObject(new JSONTokener(in));
	}


	private static void start_batch_mode() throws Exception {
		InputStream is = new FileInputStream(new File(_in_file));
	}

	private static void start_GUI_mode() throws Exception {
		throw new UnsupportedOperationException("GUI mode is not ready yet ...");
	}

	private static void start(String[] args) throws Exception {
		init_factories();
		parse_args(args);
		switch (_mode) {
		case BATCH:
			start_batch_mode();
			break;
		case GUI:
			start_GUI_mode();
			break;
		}
	}

	public static void main(String[] args) {
		Utils._rand.setSeed(2147483647l);
		try {
			start(args);
		} catch (Exception e) {
			System.err.println("Something went wrong ...");
			System.err.println();
			e.printStackTrace();
		}
	}
}
