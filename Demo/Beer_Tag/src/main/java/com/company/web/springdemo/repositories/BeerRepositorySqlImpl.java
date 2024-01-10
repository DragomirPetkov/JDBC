package com.company.web.springdemo.repositories;

import com.company.web.springdemo.exceptions.EntityNotFoundException;
import com.company.web.springdemo.models.Beer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
@PropertySource("classpath:application.properties")
public class BeerRepositorySqlImpl implements BeerRepository {

    private final StyleRepository styleRepository;
    private final UserRepository userRepository;

    private String dbUrl, dbUsername, dbPassword;

    @Autowired
    public BeerRepositorySqlImpl(StyleRepository styleRepository, UserRepository userRepository, Environment env) {
        this.styleRepository = styleRepository;
        this.userRepository = userRepository;
        dbUrl = env.getProperty("database.url");
        dbUsername = env.getProperty("database.username");
        dbPassword = env.getProperty("database.password");
    }

    @Override
    public List<Beer> getByName(String name, Double minAbv, Double maxAbv,
                                Integer styleId, String sortBy, String sortOrder) {
        try (
                Connection connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
                Statement statement = connection.createStatement();

                ResultSet resultSet = statement.executeQuery("select * from beers");

        ) {
            return getBeers(resultSet);

        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Beer getByName(int id) {
        String query = "select beer_id, name, abv " +
                "from " +
                "beers " +
                "where beer_id = ?";
        try (
                Connection connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
                PreparedStatement statement = connection.prepareStatement(query);

        ) {
            statement.setInt(1, id);
            try (
                    ResultSet resultSet = statement.executeQuery();

            ) {
                List<Beer> result = getBeers(resultSet);
                if (result.size() == 0) {
                    throw new EntityNotFoundException("Beer", id);
                }
                return result.get(0);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Beer getByName(String name) {
        String query = "select beer_id, name, abv " +
                "from " +
                "beers " +
                "where name = ?";
        try (
                Connection connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
                PreparedStatement statement = connection.prepareStatement(query);

        ) {
            statement.setString(1, name);
            try (
                    ResultSet resultSet = statement.executeQuery();

            ) {
                List<Beer> result = getBeers(resultSet);
                if (result.size() == 0) {
                    throw new EntityNotFoundException("Beer", "name", name);
                }
                return result.get(0);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void create(Beer beer) {
        String query = "insert into beers (name, abv, style, createdBy) " +
                "values(?,?,?,?)";

        try (
                Connection connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
                PreparedStatement preparedStatement = connection.prepareStatement(query)
        ) {
            preparedStatement.setString(1, beer.getName());
            preparedStatement.setDouble(2, beer.getAbv());
            preparedStatement.setInt(3, beer.getStyle().getId());
            preparedStatement.setInt(4, beer.getCreateBy().getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Beer beer) {
        String query = "update beers set " +
                "name = ?," +
                "abv = ?," +
                "style = ? " +
                "where id = ?";
        try (
                Connection connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
                PreparedStatement preparedStatement = connection.prepareStatement(query);
        ) {
            preparedStatement.setString(1, beer.getName());
            preparedStatement.setDouble(2, beer.getAbv());
            preparedStatement.setInt(3, beer.getStyle().getId());
            preparedStatement.setInt(4, beer.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(int id) {
        String query = "delete from beers " +
                "where id = ? ";
        try (
                Connection connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
                PreparedStatement preparedStatement = connection.prepareStatement(query);
        ) {
            preparedStatement.setInt(1, id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Beer> getBeers(ResultSet beersData) throws SQLException {
        List<Beer> beers = new ArrayList<>();
        while (beersData.next()) {
            Beer beer = new Beer(
                    beersData.getInt("beer_id"),
                    beersData.getString("name"),
                    beersData.getDouble("abv")
            );
//            beer.setStyle(styleRepository.getName(beersData.getString("name")));
//            beer.setCreateBy(userRepository.getById(beersData.getInt("user_id")));
            beers.add(beer);
        }
        return beers;
    }
}
