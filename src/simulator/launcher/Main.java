package simulator.launcher;

import java.io.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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

import simulator.control.Controller;
import simulator.factories.*;
import simulator.misc.Utils;
import simulator.model.*;

public class Main {
	private static Factory<Animal> animal_factory;
	private static Factory<Region> region_factory;

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
	private final static Double _default_time = 60.0; // in seconds
	private final static Double _default_dt = 0.03;

	// some attributes to stores values corresponding to command-line parameters
	//
	private static Double _time = 0.0;

	private static String _in_file = null;
	private static String _out_file = null;
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
		String dt = line.getOptionValue("dt", _default_dt.toString());
		try {
			_time = Double.parseDouble(dt);
			assert (_time >= 0);
		} catch (Exception e) {
			throw new ParseException("Invalid value for time: " + dt);
		}
	}
	private static void parse_help_option(CommandLine line, Options cmdLineOptions) {
		if (line.hasOption("h")) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp(Main.class.getCanonicalName(), cmdLineOptions, true);

		}
	}
	private static void parse_sv_option(CommandLine line, Options cmdLineOptions) {
		if (line.hasOption("sv")) {
			_sv = true;
		}
	}


	private static void parse_in_file_option(CommandLine line) throws ParseException {
		_in_file = line.getOptionValue("i");
		if (_mode == ExecMode.BATCH && _in_file == null) {
			throw new ParseException("In batch mode an input configuration file is required");
		}
	}
	private static void parse_out_file_option(CommandLine line) throws ParseException, IOException {
		String _out_file = line.getOptionValue("o");
		if (_mode == ExecMode.BATCH && _out_file == null) {
			throw new ParseException("In batch mode an input configuration file is required");
		}
	}
	private static void parse_time_option(CommandLine line) throws ParseException {
		String t = line.getOptionValue("t");
		try {
			_time = Double.parseDouble(t);
			assert (_time >= 0);
		} catch (Exception e) {
			throw new ParseException("Invalid value for time: " + t);
		}
	}

	private static void init_factories() throws FileNotFoundException {
		//strategies factory
		List<Builder<SelectionStrategy>> selection_strategy_builders = new ArrayList<>();
		selection_strategy_builders.add(new SelectFirstBuilder());
		selection_strategy_builders.add(new SelectClosestBuilder());
		selection_strategy_builders.add(new SelectYoungestBuilder());
		Factory<SelectionStrategy> selection_strategy_factory = new BuilderBasedFactory<SelectionStrategy>(selection_strategy_builders);

		try{
			//SelectionStrategy strategy = selection_strategy_factory.createInstance(load_JSON_file(_in_file));
			SelectionStrategy strategy = new SelectFirst();
			SelectionStrategy mate_strategy = new SelectYoungest();


			//animal factory
			List<Builder<Animal>> animal_builders = new ArrayList<>();
			animal_builders.add(new SheepBuilder(strategy));
			animal_builders.add(new WolfBuilder(strategy));
			animal_factory = new BuilderBasedFactory<Animal>(animal_builders);

			//region factory
			List<Builder<Region>> region_builders = new ArrayList<>();
			region_builders.add(new DefaultRegionBuilder());
			region_builders.add(new DynamicSupplyRegionBuilder());
			region_factory = new BuilderBasedFactory<Region>(region_builders);
		}	catch (Exception e) {
			System.err.println("Error while loading the input file: " + e.getLocalizedMessage());

		}



	}

	private static JSONObject load_JSON_file(InputStream in) {
		return new JSONObject(new JSONTokener(in));
	}

	private static boolean _sv = false;
	private static void start_batch_mode() throws Exception {
		InputStream is = new FileInputStream(_in_file);
		try{
			// (1) Load the input file into a JSONObject
			JSONObject inputJson = load_JSON_file(is);

			// (2) Create the output file
			OutputStream outputFile = new FileOutputStream(new File("output.json"));
			_out_file = "output.json";

			// (3) Create an instance of Simulator passing the appropriate information to its constructor
			Simulator simulator = new Simulator(inputJson.getInt("width"), inputJson.getInt("height"), inputJson.getInt("rows"), inputJson.getInt("cols"), animal_factory, region_factory);

			// (4) Create an instance of Controller, passing it the simulator
			Controller controller = new Controller(simulator);

			// (5) Call load_data by passing it the input JSONObject
			controller.load_data(inputJson);

			// (6) Call the run method with the corresponding parameters
			controller.run(_default_time, _default_dt, _sv, outputFile);

			// (7) Close the output file
			outputFile.close();

		} catch(IOException e){
			System.err.println("Error while loading the input file: " + e.getLocalizedMessage());
		}




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
