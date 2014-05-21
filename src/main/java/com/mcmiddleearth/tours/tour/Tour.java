package com.mcmiddleearth.tours.tour;

import com.mcmiddleearth.tours.Tours;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

import static com.mcmiddleearth.tours.Tours.tours;
import static com.mcmiddleearth.tours.utils.Colors.*;

/**
 * @author dags_ <dags@dags.me>
 */

public class Tour
{
    private String leader;
    private String name;
    private List<String> tourists;

    public Tour(Player p)
    {
        this.leader = p.getName();
        this.name = this.leader;
        this.tourists = new ArrayList<String>();
        this.tourists.add(p.getName());
    }

    public void addTourist(Player p)
    {
        if (!this.tourists.contains(p.getName()))
        {
            this.tourists.add(p.getName());
            String alert = yellow + "Everybody welcome " + green + p.getName() + yellow + " to the tour!";
            this.tourNotify(alert);
        }
        else
        {
            p.sendMessage(gray + "You have already joined this tour!");
        }
    }

    public void removeTourist(Player p)
    {
        if (this.tourists.contains(p.getName()))
        {
            this.tourists.remove(p.getName());

            String alert = dGray + p.getName() + gray + " left the tour.";
            this.tourNotify(alert);
            p.sendMessage(gray + "You left the tour!");
        }
        else
        {
            p.sendMessage(gray + "You are not part of any tours!");
        }
    }

    public String getTourName()
    {
        return this.name;
    }

    public List<String> getTouristList()
    {
        return this.tourists;
    }

    public List<Player> getTourists()
    {
        List<Player> players = new ArrayList<Player>();

        for (String s : this.tourists)
        {
            OfflinePlayer op = Bukkit.getOfflinePlayer(s);
            if (op.isOnline())
            {
                players.add((Player) op);
            }
        }

        return players;
    }

    public void tourChat(Player p, String chat[])
    {
        String prefix;
        String message;

        if (p.hasPermission("Tours.ranger"))
        {
            prefix = "[" + aqua + "T" + reset + "] ";
            message = Tours.rangerChatColor + chat[1] + reset;
        }
        else
        {
            prefix = "[" + yellow + "T" + reset + "] ";
            message = Tours.userChatColor + chat[1] + reset;
        }

        for (String s : this.tourists)
        {
            OfflinePlayer op = Bukkit.getOfflinePlayer(s);
            if (op.isOnline())
            {
                ((Player) op).sendMessage(chat[0] + prefix + message);
            }
        }
    }

    public void tourNotify(String alert)
    {
        for (String s : this.tourists)
        {
            OfflinePlayer op = Bukkit.getOfflinePlayer(s);
            if (op.isOnline())
            {
                ((Player) op).sendMessage(alert);
            }
        }
    }

    public void tourClear()
    {
        this.tourists.clear();
        tours.remove(this.getTourName());
    }

    public void teleportToLeader(Player p)
    {
        OfflinePlayer op = Bukkit.getOfflinePlayer(name);
        if (op.isOnline())
        {
            Location l = op.getPlayer().getLocation();
            niceTp(l, p);
        }
    }

    public void teleportPlayer(Player p, String s)
    {
        Player target = p;

        for (String st : this.tourists)
        {
            if (st.toLowerCase().contains(s.toLowerCase()))
            {
                OfflinePlayer op = Bukkit.getOfflinePlayer(st);
                if (op.isOnline() && !st.equals(this.leader))
                {
                    target = (Player) op;
                    break;
                }
            }
        }

        if (!target.getName().equals(p.getName()))
        {
            Location l = p.getLocation();
            niceTp(l, target);
            p.sendMessage(dPurple + "User teleported!");
        }
        else
        {
            p.sendMessage(gray + "User not found on this tour!");
        }
    }

    public void teleportAll(Player p)
    {
        if (!this.tourists.isEmpty())
        {
            Location l = p.getLocation();

            for (String s : this.tourists)
            {
                OfflinePlayer op = Bukkit.getOfflinePlayer(s);
                {
                    if (op.isOnline())
                    {
                        Player q = op.getPlayer();
                        if (!q.getName().equals(p.getName()))
                        {
                            niceTp(l, q);
                        }
                    }
                }
            }
            p.sendMessage(dPurple + "Players teleported!");
        }
        else
        {
            p.sendMessage(gray + "No players to teleport!");
        }
    }

    private void niceTp(Location target, Player p)
    {
        target.setDirection(p.getLocation().getDirection());
        p.teleport(target);
        p.sendMessage(lPurple + "Teleported!");
    }

}
