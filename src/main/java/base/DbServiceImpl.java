package base;

import Users.UserProfile;
import dao.impl.PlayerDao;
import main.ConstructFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import table.Player;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Андрей on 12.12.2014.
 */
public class DbServiceImpl implements DBService {
    private SessionFactory sessionFactory;
    private PlayerDao playerDao = new PlayerDao();

    public DbServiceImpl(String config) {
        sessionFactory = new Configuration().configure(config).buildSessionFactory();
    }

    @Override
    public void addPlayer(Player player) throws SQLException {
        Session session = sessionFactory.openSession();
        try {
            playerDao.addPlayer(player, session);
            session.getTransaction().commit();
        }catch(Exception e) {
            e.printStackTrace();
        }finally {
            if((session != null) && session.isOpen())session.close();
        }
    }

    @Override
    public void updatePlayer(Player player) throws SQLException {
        Session session = sessionFactory.openSession();
        try {
            playerDao.updatePlayer(player, session);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if((session != null) && session.isOpen())session.close();
        }
    }

    @Override
    public void deletePlayer(Player player) throws SQLException {
        Session session = sessionFactory.openSession();
        try {
            playerDao.deletePlayer(player, session);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if((session != null) && session.isOpen())session.close();
        }
    }

    @Override
    public Player getPlayer(int id) throws SQLException {
        Session session = sessionFactory.openSession();
        return playerDao.getPlayer(id, session);
    }

    @Override
    public Player getPlayerByLogin(String login) throws SQLException {
        Session session = sessionFactory.openSession();
        return playerDao.getPlayerByLogin(login, session);
    }

    @Override
    public List<Player> getPlayers() throws SQLException {
        Session session = sessionFactory.openSession();
        return playerDao.getPlayers(session);
    }

    @Override
    public List<Player> getPlayersRating() throws SQLException {
        Session session = sessionFactory.openSession();
        return playerDao.getPlayersRating(session);
    }

    @Override
    public String countPlayers() throws SQLException {
        Session session = sessionFactory.openSession();
        return playerDao.countPlayers(session);
    }
}
