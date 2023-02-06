package Initializer;

import com.reservation.beans.Booking;
import com.reservation.beans.Bus;
import com.reservation.beans.Route;
import com.reservation.beans.Station;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConnectionHandler {
    static final String DB_URL = "jdbc:postgresql://localhost/postgres";
    static final String DB_NAME = "reservation";
    public static Connection connection;
    public static Statement statement;
    static final String USER = "superuser";
    static final String PASSWORD = "superuser";

    public static Map<Long, String> userSessionMap = new HashMap<>();

    public static void createDatabase() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection("jdbc:postgresql://localhost/postgres", USER, PASSWORD);
            statement = connection.createStatement();



            String createDatabaseSQL = "CREATE DATABASE " + DB_NAME;
            statement.executeUpdate(createDatabaseSQL);


            System.out.println("Database created successfully...");
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                connection.close();
                statement.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            connection = DriverManager.getConnection("jdbc:postgresql://localhost/" + DB_NAME, USER, PASSWORD);
            statement = connection.createStatement();
        }
    }

    public static void closeConnection() throws SQLException {
        statement.close();
        connection.close();
        userSessionMap = new HashMap<>();
    }

    public static void createTables() {
        try {
            createUsersTables();
            createBusesTables();
            createStationsTables();
            createRoutesTables();
            createBookingsTables();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static void createBookingsTables() {
        try {
            String createBookings = "CREATE TABLE IF NOT EXISTS BOOKINGS (booking_id BIGINT, user_id BIGINT references USERS(id),bus_id BIGINT references BUSES(bus_id), booking_date DATE);";
            statement.executeUpdate(createBookings);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void createRoutesTables() {
        try {
            String createRoutesTable = "CREATE TABLE IF NOT EXISTS ROUTES (route_id BIGINT primary key, bus_id BIGINT references BUSES(bus_id), from_station BIGINT references STATIONS(station_id), to_station BIGINT references STATIONS(station_id));";
            statement.executeUpdate(createRoutesTable);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void createStationsTables() {
        try {
            String createStationsTable = "CREATE TABLE IF NOT EXISTS STATIONS (station_id BIGINT primary key, name TEXT);";
            statement.executeUpdate(createStationsTable);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void createBusesTables() {
        try {
            String createBusesTable = "CREATE TABLE IF NOT EXISTS BUSES (bus_id BIGINT primary key, name TEXT,seat_count INT,cost_per_seat BIGINT);";
            statement.executeUpdate(createBusesTable);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void createUsersTables() {
        try {
            String createUsersTable = "CREATE TABLE IF NOT EXISTS USERS (id BIGINT primary key, name TEXT, age INT, gender INT,password TEXT,email TEXT);";
            statement.executeUpdate(createUsersTable);

            Long adminUserId = fetchUserIdByMail("admin");
            if (adminUserId == null) {
                createUser("admin", 1, 1, "admin", "admin");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void createUser(String userName, int age, int gender, String password, String email) throws SQLException {
        String userStatement = "insert into USERS values(" + new Date().getTime() + ",\'" + userName + "\'," + age + "," + gender + ",\'" + password + "\',\'" + email + "\');";
        statement.executeUpdate(userStatement);
    }


    public static Long fetchUserIdByMail(String userName) throws SQLException {
        Long userId = null;
        try {
            String query = "Select id from users where email = '" + userName + "';";
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                userId = Long.parseLong(rs.getString("id"));
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userId;
    }

    public static void createBus(String busName, int seatCount, long costPerSeat) throws SQLException {
        String busStatement = "insert into BUSES values(" + new Date().getTime() + ",'" + busName + "'," + seatCount + "," + costPerSeat + ");";
        statement.executeUpdate(busStatement);
    }

    public static void createStation(String stationName) throws SQLException {
        String stationStatement = "insert into STATIONS values(" + new Date().getTime() + ",'" + stationName + "');";
        statement.executeUpdate(stationStatement);
    }

    public static void createRoute(Long bus_id, Long from_station, Long to_station) throws SQLException {
        String routeStatement = "insert into ROUTES values(" + new Date().getTime() + "," + bus_id + "," + from_station + "," + to_station + ");";
        statement.executeUpdate(routeStatement);
    }

    public static void createBooking(Long user_id, Long bus_id, java.sql.Date booking_date) throws SQLException {
        String bookingStatement = "insert into BOOKINGS values("+ new Date().getTime()+","+ user_id + "," + bus_id + ",'" + booking_date + "');";
        statement.executeUpdate(bookingStatement);
    }

    public static Long loginUser(String email, String password) throws SQLException {
        Long userId = null;
        try {
            String query = "Select id from users where email = '" + email + "' and password = '" + password + "';";
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                userId = Long.parseLong(rs.getString("id"));
                break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userId;

    }

    public static List<Bus> getBuses() {
        List<Bus> busList = new ArrayList<>();
        try {
            String query = "Select * from buses ;";
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                Bus bus = new Bus();
                bus.setId(Long.parseLong(rs.getString("bus_id")));
                bus.setCapacity(Integer.parseInt(rs.getString("seat_count")));
                bus.setCost(Integer.parseInt(rs.getString("cost_per_seat")));
                bus.setName(rs.getString("name"));
                busList.add(bus);
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return busList;
    }

    public static List<Station> getStations() {
        List<Station> stationList = new ArrayList<>();
        try {
            String query = "Select * from stations ;";
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                Station station = new Station();
                station.setId(Long.parseLong(rs.getString("station_id")));
                station.setName(rs.getString("name"));
                stationList.add(station);
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stationList;
    }

    public static List<Route> getRoutes() {
        List<Route> routeList = new ArrayList<>();
        try {
            String query = "Select * from routes ;";
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                Route route = new Route();
                route.setRoute_id(Long.parseLong(rs.getString("route_id")));
                route.setBus_id(Long.valueOf(rs.getString("bus_id")));
                route.setFrom_station(Long.valueOf(rs.getString("from_station")));
                route.setTo_station(Long.valueOf(rs.getString("to_station")));
                routeList.add(route);
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return routeList;
    }

    public static List<Route> getRoutes(Long fromStation, Long toStation) {
        List<Route> routeList = new ArrayList<>();
        try {
            String query = "Select * from routes where from_station = " + fromStation + "and to_station=" + toStation + ";";
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                Route route = new Route();
                route.setRoute_id(Long.parseLong(rs.getString("route_id")));
                route.setBus_id(Long.valueOf(rs.getString("bus_id")));
                route.setFrom_station(Long.valueOf(rs.getString("from_station")));
                route.setTo_station(Long.valueOf(rs.getString("to_station")));
                routeList.add(route);
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return routeList;
    }

    public static List<Bus> getBusesWithSeatingCapacity(List<String> listOfBusesRunningInThatRoute, java.sql.Date travelDate) {
        List<Bus> busList = new ArrayList<>();
        try {
//            String query = "Select * from buses left join bookings on buses.bus_id = bookings.bus_id where buses.bus_id in ("+listOfBusesRunningInThatRoute.toArray()+") and booking_date="+travelDate+";";
            String query = "Select * from buses where buses.bus_id in ("+String.join(",",listOfBusesRunningInThatRoute)+");";
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                Bus bus = new Bus();
                bus.setId(Long.parseLong(rs.getString("bus_id")));
                bus.setCapacity(Integer.parseInt(rs.getString("seat_count")));
                bus.setCost(Integer.parseInt(rs.getString("cost_per_seat")));
                bus.setName(rs.getString("name"));
                busList.add(bus);
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return busList;
    }

    public static List<Booking> getBookings(Long userId) {
        List<Booking> bookingList = new ArrayList<>();
        try {
//            String query = "Select * from buses left join bookings on buses.bus_id = bookings.bus_id where buses.bus_id in ("+listOfBusesRunningInThatRoute.toArray()+") and booking_date="+travelDate+";";
            String query = "Select * from bookings where user_id = "+userId +";";
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                Booking booking = new Booking();
                booking.setBooking_id(Long.parseLong(rs.getString("booking_id")));
                booking.setBus_id(Long.parseLong(rs.getString("bus_id")));
                booking.setUserId(Long.parseLong(rs.getString("user_id")));
                booking.setTravelDate(java.sql.Date.valueOf((rs.getString("booking_date"))));
                bookingList.add(booking);
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bookingList;
    }
}
