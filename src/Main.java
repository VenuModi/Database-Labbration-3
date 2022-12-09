import java.sql.*;
import java.util.Scanner;

public class Main {
    private static final String category = "category";
    private static final String film = "Film";
    static Scanner scanner = new Scanner(System.in);


    private static Connection connection(){
        String url ="jdbc:sqlite:/C:/Users/VenuModi/DataGripProjects/Labb3";
        Connection connect = null;
        try {
            connect = DriverManager.getConnection(url);
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return connect;
    }

    public static void main(String[] args) {
        boolean quit = false;
        printActions();
        while (!quit){
            System.out.println("\nChoose (5 to show all options): ");
            int action = Integer.parseInt(scanner.nextLine());

            switch (action) {
                case 0 -> {
                    System.out.println("\nExit");
                    quit = true;
                }
                case 1 -> show();
                case 2 -> insert();
                case 3 -> update();
                case 4 -> delete();
                case 5 -> printActions();
            }
        }
    }

    private static void printActions(){
        System.out.println("\nChoose:\n");
        System.out.println("0 - Exit\n"+
                "1 - Show \n"+
                "2 - Insert \n"+
                "3 - Update \n"+
                "4 - Delete \n"+
                "5 - List of all options");
    }

    private static void show(){
        System.out.println("What do you want to see\n" +
                "a - Show category and film\n"+
                "b - Search for films\n"+
                "c - Get number of films from the database\n" +
                "d - Show from category\n"+
                "e - Show from film");

        switch (scanner.next()) {
            case "a" -> oneToManyJoins();
            case "b" -> searchFilms();
            case "c" -> numberOfFilms();
            case "d" -> showCategory();
            case "e" -> showFilms();
        }
    }

    private static void oneToManyJoins(){
        String sql = "SELECT * FROM film INNER JOIN category ON film.filmCategoryIDFK = category.categoryID";

        try {
            Connection connection = connection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()){
                System.out.println("Film ID: " + resultSet.getInt("filmID") + "\t" +
                        "Film Name: " + resultSet.getString("filmName") + "\t" +
                        "Production Company: " + resultSet.getString("productionCO") + "\t" +
                        "Category Name: " + resultSet.getString("categoryName"));
                scanner.nextLine();
            }
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void searchFilms(){
        String sql = "SELECT * FROM film INNER JOIN category ON film.filmCategoryIDFK = category.categoryID WHERE filmName = ?";

        try {
            Connection connection = connection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            selectTable(film);
            System.out.println("Enter name of the film: ");
            String film = scanner.nextLine();
            preparedStatement.setString (1, film);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                System.out.println("Film ID: " + resultSet.getInt("filmID") + "\t" +
                        "Film Name: " + resultSet.getString("filmName") + "\t" +
                        "Production Company: " + resultSet.getString("productionCo") + "\t" +
                        "Category Name: " + resultSet.getString("categoryName"));
                scanner.nextLine();
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    private static void numberOfFilms(){
        String sql = "SELECT COUNT (*) FROM film";
        try {
            Connection connection = connection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()){
                System.out.println("Number of Films = " + resultSet.getInt("COUNT (*)"));
                scanner.nextLine();
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    private static void showCategory(){
        String sqlCategory = "SELECT * FROM category";

        try {
            Connection connection = connection();
            Statement statement = connection.createStatement();
            ResultSet resultSetCategory = statement.executeQuery(sqlCategory);

            System.out.println("CATEGORIES");
            while (resultSetCategory.next()){
                System.out.println(resultSetCategory.getInt("categoryID") + "\t" +
                        resultSetCategory.getString("categoryName"));
                scanner.nextLine();
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    private static void showFilms(){
        String sqlFilm = "SELECT * FROM film";

        try {
            Connection connection = connection();
            Statement statement = connection.createStatement();
            ResultSet resultSetFilm = statement.executeQuery(sqlFilm);

            System.out.println("FILMS");
            while (resultSetFilm.next()){
                System.out.println(resultSetFilm.getInt("filmID") + "\t" +
                        resultSetFilm.getString("filmName") + "\t" +
                        resultSetFilm.getString("productionCo") + "\t" +
                        resultSetFilm.getInt("priceOfCD")+ "\t"+
                        resultSetFilm.getInt("filmCategoryIDFK"));
                scanner.nextLine();
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }


    private static void insert(){
        System.out.println("In which table would you like to insert\n" +
                "a - Insert into category\n"+
                "b - Insert into films");
        switch (scanner.next()) {
            case "a" -> insertCategory();
            case "b" -> insertFilms();
        }
    }

    private static void insertCategory(){
        selectTable(category);
        System.out.println("Category Name: ");
        String categoryName = scanner.nextLine();
        insert(categoryName);
    }

    private static void insert(String categoryName) {
        String sql = "INSERT INTO category (categoryName) VALUES (?)";

        try {
            Connection connection = connection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, categoryName);
            preparedStatement.executeUpdate();
            System.out.println("You have inserted a new category");
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    private static void insertFilms(){
        selectTable(film);
        System.out.println("Film Name: ");
        String filmName = scanner.nextLine();
        System.out.println("Production Company: ");
        String productionCo = scanner.nextLine();
        System.out.println("Price of CD: ");
        double priceOfCD = Double.parseDouble(scanner.nextLine());
        System.out.println("Insert film category ID (FK): ");
        int filmCategoryID = Integer.parseInt(scanner.nextLine());

        insert(filmName, productionCo, priceOfCD, filmCategoryID);
        scanner.nextLine();
    }

    private static void  insert( String filmName, String  productionCo, double priceOfCD, int filmCategoryID){
        String sql = "INSERT INTO film (filmName, productionCO, PriceOfCD, filmCategoryIDFK) VALUES (?,?,?,?)";

        try {
            Connection connection = connection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, filmName);
            preparedStatement.setString(2, productionCo);
            preparedStatement.setDouble(3, priceOfCD);
            preparedStatement.setInt(4, filmCategoryID);
            preparedStatement.executeUpdate();
            System.out.println("Now you have inserted film: " + filmName);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    private static void update(){
        System.out.println("What would you like to update\n" +
                "a - Update category name\n"+
                "b - Update film name");

        switch (scanner.next()) {
            case "a" -> updateCategoryName();
            case "b" -> updateFilmName();
        }
    }

    private static void updateCategoryName(){
        String sql = "UPDATE category SET categoryName = ? WHERE categoryID = ?";

        try {
            Connection connection = connection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            selectTable(category);
            System.out.println("Which ID do you want to update? ");
            int id = Integer.parseInt(scanner.nextLine());
            preparedStatement.setInt(2, id);
            System.out.println("New Category = ");
            String name = scanner.nextLine();
            preparedStatement.setString(1, name);
            preparedStatement.executeUpdate();
            System.out.println("ID updated");
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void updateFilmName(){
        String sql = "UPDATE film SET filmName = ? WHERE filmID = ?";

        try {
            Connection connection = connection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            selectTable(film);
            System.out.println("Which ID do you want to update?");
            int id = Integer.parseInt(scanner.nextLine());
            preparedStatement.setInt(2, id);
            System.out.println("New Film = ");
            String film = scanner.nextLine();
            preparedStatement.setString(1, film);
            preparedStatement.executeUpdate();
            System.out.println("ID and film updated");
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }


    private static void delete(){
        System.out.println("What would you like to delete\n" +
                "a - Delete from category\n"+
                "b - Delete from film");
        switch (scanner.next()) {
            case "a" -> deleteFrom(category);
            case "b" -> deleteFrom(film);
        }
    }

    private static void deleteFrom(String table){
        String sql = " DELETE FROM " + table + " WHERE " + table + "Name = ?";

        try {
            Connection connection = connection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            selectTable(table);
            System.out.println("Enter " + table + " to be deleted: ");
            String name = scanner.nextLine();
            preparedStatement.setString(1, name);
            preparedStatement.executeUpdate();
            System.out.println("Deleted!");
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }


    private static void selectTable(String table){
        String sql = "SELECT * FROM " + table;

        try {
            Connection connection = connection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()){
                System.out.println("ID: " + resultSet.getInt(table + "ID") + "\t" +
                        "Name: " + resultSet.getString(table + "Name"));
                scanner.nextLine();
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }
}
