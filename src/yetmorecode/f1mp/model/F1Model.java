package yetmorecode.f1mp.model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import yetmorecode.f1mp.model.driver.Driver;
import yetmorecode.f1mp.model.engine.Engine;
import yetmorecode.f1mp.model.engine.EngineResources;
import yetmorecode.f1mp.model.engineer.Engineer;
import yetmorecode.f1mp.model.sponsor.Sponsor;
import yetmorecode.f1mp.model.team.Team;
import yetmorecode.file.BinaryFileInputStream;
import yetmorecode.file.exception.InvalidHeaderException;
import yetmorecode.file.format.lx.LxExecutable;

public class F1Model {

	public String gameDirectory;
	
	public File engineFile;
	public File driverFile;
	public File teamFile;
	public File engineerFile;
	public File trackFile;
	public File historyFile;
	public File trackInfoFile;
	public File sponsorFile;
	public File saveFile[] = new File[10];
	public File f1eFile;
	public File usereFile;
	public File exeFile;
	public File model3DFile;
	
	public ArrayList<Driver> drivers = new ArrayList<>();
	public ArrayList<Team> teams = new ArrayList<>();
	public ArrayList<Engineer> engineers = new ArrayList<>();
	public ArrayList<Engine> engines = new ArrayList<>();
	public ArrayList<Sponsor> sponsors = new ArrayList<>();
	
	public LxExecutable exe;
	
	public static F1Model loadFrom(String directory) throws IOException {
		var model = new F1Model();
		
		System.out.println(String.format("Loading model from %s", directory));
		model.gameDirectory = directory;
		model.exeFile = new File(directory + "\\F1.exe");
		model.f1eFile = new File(directory + "\\f1_e.rsc");
		model.engineFile = new File(directory + "\\DATA97_E\\MOTOREN.DAT");
		model.driverFile = new File(directory + "\\DATA97_E\\FAHRER.DAT");
		model.teamFile = new File(directory + "\\DATA97_E\\TEAMS.DAT");
		model.engineerFile = new File(directory + "\\DATA97_E\\INGS.DAT");
		model.trackInfoFile = new File(directory + "\\DATA97_E\\STRINFO.DAT");
		model.trackFile = new File(directory + "\\DATA97_E\\STRECKEN.DAT");
		model.sponsorFile = new File(directory + "\\DATA97_E\\SPONSOR.DAT");
		
		model.loadEngines(model.engineFile);
		model.loadSponsors(model.sponsorFile);
		model.loadDrivers(model.driverFile);
		model.loadEngineers(model.engineerFile);
		model.loadTeams(model.teamFile);
		
		System.out.print(String.format("Loading F1.exe from %s.. ", model.exeFile.getAbsolutePath()));
		try {
			var input = new BinaryFileInputStream(model.exeFile);
			model.exe = LxExecutable.createFrom(input);
			
			EngineResources.createFrom(input, model.exe);
			input.close();
		} catch (InvalidHeaderException e) {
			e.printStackTrace();
		}
		
		return model;
	}
	
	public static void storeTo(String directory) {
		
	}
	
	private void loadEngines(File file) {
		engines = new ArrayList<>();
		var i = 0;
		try {
			System.out.print(String.format("Loading engines from %s.. ", file.getAbsolutePath()));
			var input = new BinaryFileInputStream(file);
			long offset = 0;
			while (offset < input.getChannel().size()) {
				var engine = Engine.createFrom(input, offset);
				engine.index = i++;
				engines.add(engine);
				offset += Engine.SIZE;
			}
			System.out.println(String.format("%d", engines.size()));
		} catch (IOException e) {
			System.out.println(String.format("Could not load engines from %s:", file.getAbsolutePath()));
			e.printStackTrace();
		}
	}
	
	private void loadSponsors(File file) {
		sponsors = new ArrayList<>();
		try {
			System.out.print(String.format("Loading sponsors from %s.. ", file.getAbsolutePath()));
			var input = new BinaryFileInputStream(file);
			long offset = 0;
			while (offset < input.getChannel().size()) {
				sponsors.add(Sponsor.createFrom(input, offset));
				offset += Sponsor.SIZE;
			}
			System.out.println(String.format("%d", sponsors.size()));
		} catch (IOException e) {
			System.out.println(String.format("Could not load sponsors from %s:", file.getAbsolutePath()));
			e.printStackTrace();
		}
	}
	
	private void loadDrivers(File file) {
		drivers = new ArrayList<>();
		try {
			System.out.print(String.format("Loading drivers from %s.. ", file.getAbsolutePath()));
			var input = new BinaryFileInputStream(file);
			long offset = 0;
			while (offset < input.getChannel().size()) {
				drivers.add(Driver.createFrom(input, offset));
				offset += Driver.SIZE;
			}
			System.out.println(String.format("%d", drivers.size()));
		} catch (IOException e) {
			System.out.println(String.format("Could not load drivers from %s:", file.getAbsolutePath()));
			e.printStackTrace();
		}
	}
	
	private void loadTeams(File file) {
		teams = new ArrayList<>();
		try {
			System.out.print(String.format("Loading teams from %s.. ", file.getAbsolutePath()));
			var input = new BinaryFileInputStream(file);
			long offset = 0;
			while (offset < input.getChannel().size()) {
				teams.add(Team.createFrom(input, offset));
				offset += Team.SIZE;
			}
			System.out.println(String.format("%d", teams.size()));
		} catch (IOException e) {
			System.out.println(String.format("Could not load teams from %s:", file.getAbsolutePath()));
			e.printStackTrace();
		}
	}
	
	private void loadEngineers(File file) {
		engineers = new ArrayList<>();
		try {
			System.out.print(String.format("Loading engineers from %s.. ", file.getAbsolutePath()));
			var input = new BinaryFileInputStream(file);
			long offset = 0;
			while (offset < input.getChannel().size()) {
				engineers.add(Engineer.createFrom(input, offset));
				offset += Engineer.SIZE;
			}
			System.out.println(String.format("%d", engineers.size()));
		} catch (IOException e) {
			System.out.println(String.format("Could not load engineers from %s:", file.getAbsolutePath()));
			e.printStackTrace();
		}
	}
}
