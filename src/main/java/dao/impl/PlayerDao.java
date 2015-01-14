package dao.impl;

import org.hibernate.criterion.Restrictions;
import table.Player;
import org.hibernate.Session;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Андрей on 21.09.2014.
 */
public class PlayerDao {
    public void addPlayer(Player player, Session session) throws SQLException {
        session.beginTransaction();
        session.save(player);
    }

    public void updatePlayer(Player player, Session session) throws SQLException {
        session.beginTransaction();
        session.update(player);
    }

    public void deletePlayer(Player player, Session session) throws SQLException {
            session.beginTransaction();
            session.delete(player);
    }

    public Player getPlayer(int id, Session session) throws SQLException {
        Player result = null;
        try {
            result = (Player) session.load(Player.class, id);
        }catch(Exception e) {
            e.printStackTrace();
        }finally {
            if((session != null) && session.isOpen())session.close();
        }
        return result;
    }

    public Player getPlayerByLogin(String login, Session session) throws SQLException {
        Player result = null;
        try {
            result = (Player) session.createCriteria(Player.class).add(Restrictions.eq("login", login)).uniqueResult();
        }catch(Exception e) {
            e.printStackTrace();
        }finally {
            if((session != null) && session.isOpen())session.close();
        }
        return result;
    }

    public List<Player> getPlayers(Session session) throws SQLException {
        try {
            return (List<Player>) session.createCriteria(Player.class).list();
        }catch(Exception e) {
            e.printStackTrace();
        }finally {
            if((session != null) && session.isOpen())session.close();
        }
        return null;
    }

    public List<Player> getPlayersRating(Session session) throws SQLException {

        try {
            return session.createSQLQuery("SELECT * FROM players ORDER BY score DESC LIMIT 0, 100;").addEntity(Player.class).list();
        }catch(Exception e) {
            e.printStackTrace();
        }finally {
            if((session != null) && session.isOpen())session.close();
        }
        return null;
    }
    public String countPlayers(Session session) {
        try {
            return session.createQuery("SELECT count(*) FROM players ORDER BY score DESC LIMIT 0, 100;").uniqueResult().toString();
        }catch(Exception e) {
            e.printStackTrace();
        }finally {
            if((session != null) && session.isOpen())session.close();
        }
        return null;
    }
}
