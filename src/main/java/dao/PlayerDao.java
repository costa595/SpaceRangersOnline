package dao;

import table.Player;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Андрей on 21.09.2014.
 */
public interface PlayerDao {
    public void addPlayer(Player player) throws SQLException; // добавление
    public void deletePlayer(Player player) throws SQLException; // удаление
    public Player getPlayer(int id) throws SQLException; // получение по ИД
    public Player getPlayerByLogin(String login) throws SQLException; // получение по логину
    public List<Player> getPlayers() throws SQLException; // получение всех юзеров
}
