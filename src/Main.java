
import java.io.*;
import java.util.Scanner;
import dataStructures.*;
import System.exceptions.*;
import System.*;


/**
* @author GUILHERME ROCHA (69112) gm.rocha@campus.fct.unl.pt
*/

public class Main {

    /** File name for persisting system state */
    private static final String DATA_FILE = "storedsystem.dat";

    // Command constants

    /** Command to insert a new line */
    private static final String INSERT_LINE = "IL";
    /** Command to remove a line */
    private static final String REMOVE_LINE = "RL";
    /** Command to consult a line's stations */
    private static final String CONSULT_LINE = "CL";
    /** Command to consult a station's lines */
    private static final String CONSULT_STATION_LINES = "CE";
    /** Command to insert a schedule */
    private static final String INSERT_SCHEDULE = "IH";
    /** Command to remove a schedule */
    private static final String REMOVE_SCHEDULE = "RH";
    /** Command to consult schedules */
    private static final String CONSULT_SCHEDULES = "CH";
    /** Command to consult trains at a station */
    private static final String CONSULT_STATION_TRAINS = "LC";
    /** Command to find best schedule */
    private static final String BEST_SCHEDULE = "MH";
    /** Command to terminate application */
    private static final String TERMINATE_APP = "TA";

    // Success messages

    /** Message for successful line insertion */
    private static final String LINE_INSERT_SUCCESS = "Inserção de linha com sucesso.";
    /** Message for successful line removal */
    private static final String LINE_REMOVE_SUCCESS = "Remoção de linha com sucesso.";
    /** Message for successful schedule insertion */
    private static final String SCHEDULE_INSERT_SUCCESS = "Criação de horário com sucesso.";
    /** Message for successful schedule removal */
    private static final String SCHEDULE_REMOVE_SUCCESS = "Remoção de horário com sucesso.";
    /** Message for successful application termination */
    private static final String APP_TERMINATED_SUCCESS = "Aplicação terminada.";
    /** Format string for train output */
    private static final String TRAIN = "Comboio %d %s:%s\n";
    /** Format string for schedule output */
    private static final String STATION = "%s %s:%s\n";

    // Error messages

    /** Message for existing line error */
    private static final String EXISTING_LINE = "Linha existente.";
    /** Message for nonexistent line error */
    private static final String NONEXISTENT_LINE = "Linha inexistente.";
    /** Message for invalid schedule error */
    private static final String INVALID_SCHEDULE = "Horário inválido.";
    /** Message for nonexistent schedule error */
    private static final String NONEXISTENT_SCHEDULE = "Horário inexistente.";
    /** Message for nonexistent departure station error */
    private static final String NONEXISTENT_DEPART_STATION = "Estação de partida inexistente.";
    /** Message for impossible route error */
    private static final String IMPOSSIBLE_ROUTE = "Percurso impossível.";
    /** Message for nonexistent station error */
    private static final String NONEXISTENT_STATION = "Estação inexistente.";

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        RailwaySystem sys = load();
        exec(in, sys);
        in.close();
        save(sys);
    }

    /**
     * Main execution loop that processes user commands.
     * Continues until TERMINATE_APP command is received.
     * @param in Scanner for user input
     * @param sys Railway system instance
     */
    private static void exec(Scanner in, RailwaySystem sys) {
        String comm;
        do {
            comm = in.next();
            switch(comm.toUpperCase()) {
                case INSERT_LINE -> insertLine(in, sys);
                case REMOVE_LINE -> removeLine(in, sys);
                case CONSULT_LINE -> consultLine(in, sys);
                case CONSULT_STATION_LINES -> consultStationLines(in, sys);
                case INSERT_SCHEDULE -> insertSchedule(in, sys);
                case REMOVE_SCHEDULE -> removeSchedule(in, sys);
                case CONSULT_SCHEDULES -> consultSchedule(in, sys);
                case CONSULT_STATION_TRAINS -> consultStationTrains(in, sys);
                case BEST_SCHEDULE -> bestSchedule(in, sys);
                case TERMINATE_APP -> terminate();
            }
        }while (!comm.equalsIgnoreCase(TERMINATE_APP));
    }

    /**
     * Processes command to insert a new line.
     * Reads line name and stations from input.
     * @param in Scanner for user input
     * @param sys Railway system instance
     */
    private static void insertLine(Scanner in, RailwaySystem sys) {
        try {
            String name = in.nextLine().trim();
            DoubleList<String> stations = makeList(in);
            sys.insertLine(name, stations);
            System.out.println(LINE_INSERT_SUCCESS);

            } catch (ExistentLineException e) {
                System.out.println(EXISTING_LINE);
            }
    }

    /**
     * Processes command to remove an existing line.
     * @param in Scanner for user input
     * @param sys Railway system instance
     */
    private static void removeLine(Scanner in, RailwaySystem sys) {
        try {
            String name = in.nextLine().trim();
            sys.removeLine(name);
            System.out.println(LINE_REMOVE_SUCCESS);

            } catch (NonexistentLineException e) {
                System.out.println(NONEXISTENT_LINE);
            }
    }

    /**
     * Processes command to consult stations in a line.
     * Lists all stations in order.
     * @param in Scanner for user input
     * @param sys Railway system instance
     */
    private static void consultLine(Scanner in, RailwaySystem sys) {
       try {
            String name = in.nextLine().trim();
           SafeStationIterator it = sys.consultLine(name);
            while(it.hasNext())
                System.out.println(it.next().getName());

            } catch (NonexistentLineException e) {
                System.out.println(NONEXISTENT_LINE);
            }
    }

    /**
     * Processes command to consult lines passing through a station.
     * Lists all lines that serve the station.
     * @param in Scanner for user input
     * @param sys Railway system instance
     */
    private static void consultStationLines(Scanner in, RailwaySystem sys) {
        try {
            String name = in.nextLine().trim();
            SafeLineIterator<String> it = sys.consultStation(name);
            while(it.hasNext())
                System.out.println(it.next().getKey());
            }
            catch (NonexistentStationException e) {
                System.out.println(NONEXISTENT_STATION);
            }
    }

    /**
     * Processes command to insert a new schedule.
     * Reads line, train, and station-time pairs.
     * @param in Scanner for user input
     * @param sys Railway system instance
     */
    private static void insertSchedule(Scanner in, RailwaySystem sys) {
        try {
            String name = in.nextLine().trim();
            String train = in.nextLine().trim();
            DoubleList<String[]> schedule = new DoubleList<>();
            String[] endLine = getStationTime(in);
            int i = 0;
            while(!endLine[0].isBlank()) {
                schedule.add(i++, endLine);
                endLine = getStationTime(in);
            }

            DoubleList<String> stationNames = extractStationNames(schedule);
            DoubleList<String[]> times = extractTimes(schedule);

            sys.insertSchedule(name, train, stationNames, times);
            System.out.println(SCHEDULE_INSERT_SUCCESS);

            } catch (NonexistentLineException e) {
                System.out.println(NONEXISTENT_LINE);
            }
            catch (InvalidScheduleException e) {
                System.out.println(INVALID_SCHEDULE);
            }
    }

    /**
     * Processes command to remove a schedule.
     * Removes schedule based on line and departure information.
     * @param in Scanner for user input
     * @param sys Railway system instance
     */
    private static void removeSchedule(Scanner in, RailwaySystem sys) {
        try {
            String lineName = in.nextLine().trim();

            String[] stationAndTime = in.nextLine().split(" ");

            String stationName = arrangeStationName(stationAndTime);

            String[] time = parseTime(stationAndTime[stationAndTime.length - 1]);

            sys.removeSchedule(lineName, stationName, time);

            System.out.println(SCHEDULE_REMOVE_SUCCESS);

            } catch (NonexistentLineException e) {
                System.out.println(NONEXISTENT_LINE);
            } catch (NonexistentScheduleException e) {
                System.out.println(NONEXISTENT_SCHEDULE);
            }
    }

    /**
     * Processes command to consult schedules.
     * Lists all schedules for a line from a specific station.
     * @param in Scanner for user input
     * @param sys Railway system instance
     */
    private static void consultSchedule(Scanner in, RailwaySystem sys) {
        try {
            String lineName = in.nextLine().trim();
            String stationName = in.nextLine().trim();

            SafeScheduleIterator<Time> it = sys.consultSchedules(lineName, stationName);
            while(it.hasNext()) {
                SafeSchedule schedule = it.next().getValue();
                printSchedule(schedule);
            }
            } catch (NonexistentLineException e) {
                System.out.println(NONEXISTENT_LINE);
            } catch (NonexistentStationException e) {
                System.out.println(NONEXISTENT_DEPART_STATION);
            }
    }

    /**
     * Processes command to consult trains passing through a station.
     * Lists all trains with their times at the station.
     * @param in Scanner for user input
     * @param sys Railway system instance
     */
    private static void consultStationTrains(Scanner in, RailwaySystem sys) {
        try {
            String stationName = in.nextLine().trim();
            SafeLineIterator<TimeTrainPairClass> it = sys.stationTrains(stationName);
            while(it.hasNext()) {
                Entry<TimeTrainPairClass, SafeLine> entry = it.next();
                Time time = entry.getKey().getTime();
                System.out.printf(TRAIN, entry.getKey().getTrain(), time.hour(), time.minute());
            }
            } catch(NonexistentStationException e) {
                System.out.println(NONEXISTENT_STATION);
            }
    }

    /**
     * Processes command to find best schedule.
     * Finds optimal schedule between stations before specified time.
     * @param in Scanner for user input
     * @param sys Railway system instance
     */
    private static void bestSchedule(Scanner in, RailwaySystem sys) {
        try {
            String lineName = in.nextLine().trim();
            String departureName = in.nextLine().trim();
            String destinationName = in.nextLine().trim();
            String[] timeOfArrival = parseTime(in.nextLine().trim());

            SafeSchedule schedule = sys.bestSchedule(lineName, departureName, destinationName, timeOfArrival);

            printSchedule(schedule);

            } catch (NonexistentLineException e) {
                System.out.println(NONEXISTENT_LINE);
            } catch (NonexistentStationException e) {
                System.out.println(NONEXISTENT_DEPART_STATION);
            } catch (ImpossibleRouteException e) {
                System.out.println(IMPOSSIBLE_ROUTE);
        }
    }

    /**
     * Processes termination command.
     * Displays termination message.
     */
    private static void terminate() {
        System.out.println(APP_TERMINATED_SUCCESS);
    }

    /**
     * Reads and parses station and time information from input.
     * @param in Scanner for user input
     * @return Array containing station name parts and time
     */
    private static String[] getStationTime(Scanner in) {
        String[] input;
        input = in.nextLine().split(" ");
        return input;
    }

    /**
     * Creates a list of strings from input until empty line.
     * Used for reading station lists.
     * @param in Scanner for user input
     * @return DoubleList containing the input strings
     */
    private static DoubleList<String> makeList(Scanner in) {
        DoubleList<String> stations = new DoubleList<>();
        String station;

        do{
            station = in.nextLine().trim();
            if(!station.isBlank())
                stations.addLast(station);
        }while(!station.isBlank());

        return stations;
    }

    /**
     * Extracts station names from schedule data
     * @param stations List of station-time pairs
     * @return List containing only station names
     * @throws InvalidScheduleException if schedule data is invalid
     */
    private static DoubleList<String> extractStationNames(DoubleList<String[]> stations)
            throws InvalidScheduleException {

        DoubleList<String> stationList = new DoubleList<>();

        for(int i = 0; i < stations.size(); i++) {
            stationList.addLast(arrangeStationName(stations.get(i)));
        }
        return stationList;
    }

    /**
     * Combines multiple strings to form a complete station name
     * @param station Array containing station name parts and time
     * @return Complete station name
     */
    private static String arrangeStationName(String[] station) {
        StringBuilder stationName = new StringBuilder(station[0]);
        for(int i = 1; i < station.length - 1; i++)
            stationName.append(" ").append(station[i]);
        return stationName.toString();
    }

    /**
     * Extracts time information from schedule data
     * @param stations List of station-time pairs
     * @return List of time arrays in HH:MM format
     */
    private static DoubleList<String[]> extractTimes(DoubleList<String[]> stations) {

        DoubleList<String[]> timesList = new DoubleList<>();

        for(int i = 0; i < stations.size(); i++) {
            String[] tmp = stations.get(i);
            timesList.addLast(parseTime(tmp[tmp.length - 1]));
        }
        return timesList;
    }

    /**
     * Splits a time string into hour and minute components
     * @param timeLine String containing time in HH:MM format
     * @return Array with hour and minute as separate strings
     */
    private static String[] parseTime(String timeLine) {
        return timeLine.split(":");
    }

    private static void printSchedule(SafeSchedule schedule) {
        SafeStationIterator it = schedule.getStationIt();
        System.out.println(schedule.getTrain());

        while(it.hasNext()) {
            SafeStation station = it.next();
            Time time = schedule.getStationTime(station);
            System.out.printf(STATION, station.getName(), time.hour(), time.minute());
        }
    }

    /**
     * Saves current system state to file.
     * @param sys Railway system to save
     */
    private static void save(RailwaySystem sys) {
        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(DATA_FILE));
             ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(sys);
            oos.flush();
        } catch (IOException e) {}
    }

    /**
     * Loads system state from file.
     * Creates new system if file doesn't exist or is corrupted.
     * @return Loaded or new RailwaySystem instance
     */
    private static RailwaySystem load() {
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(DATA_FILE));
             ObjectInputStream ois = new ObjectInputStream(bis)) {
            return (RailwaySystem) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new RailwaySystemClass();
        }
    }
}
