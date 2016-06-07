package com.motodb.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.motodb.model.Championship;
import com.motodb.model.Team;
import com.motodb.view.alert.AlertTypes;
import com.motodb.view.alert.AlertTypesImpl;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class TeamManagerImpl implements TeamManager{

    @Override
    public void addTeam(int year, String name, String location, String logo, ObservableList<String> classes, ObservableList<String> sponsors) {
        final DBManager db = DBManager.getDB();
        final Connection conn  = db.getConnection();
        boolean ok = true;
        
        java.sql.PreparedStatement statement = null;
        final String insert = "insert into TEAM(annoCampionato, nomeTeam, sede, logo) values (?,?,?,?)";
        try {
            statement = conn.prepareStatement(insert);
            statement.setInt(1, year);
            statement.setString(2, name);
            statement.setString(3, location);
            statement.setString(4, logo);
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            ok = false;
            AlertTypes alert = new AlertTypesImpl();
            alert.showError(e);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            }
            catch (SQLException e) {
                AlertTypes alert = new AlertTypesImpl();
                alert.showError(e);
            }
        }
        
        if (ok) {
                final String insert2 = "insert into FINANZIAMENTO(annoCampionato, nomeTeam, nomeSponsor) values (?,?,?)";
                classes.forEach(e -> {
                    java.sql.PreparedStatement statement2 = null;
                    try {
                        statement2 = conn.prepareStatement(insert2);
                        statement2.setInt(1, year);
                        statement2.setString(2, name);
                        statement2.setString(3, e);
                        statement2.executeUpdate();
                        statement2.close();
                    } catch (SQLException ex) {
                        AlertTypes alert = new AlertTypesImpl();
                        alert.showError(ex);
                    }
                });
                
                final String insert3 = "insert into ISCRIZIONE_CLASSE(annoCampionato, nomeTeam, nomeClasse) values (?,?,?)";
                sponsors.forEach(e -> {
                    java.sql.PreparedStatement statement3;
                    try {
                        statement3 = conn.prepareStatement(insert3);
                        statement3.setInt(1, year);
                        statement3.setString(2, name);
                        statement3.setString(3, e);
                        statement3.executeUpdate();
                        statement3.close();
                    } catch (SQLException ex) {
                        AlertTypes alert = new AlertTypesImpl();
                        alert.showError(ex);
                    } 
                });
        }
        
    }

    @Override
    public ObservableList<Team> getTeams() {
        
        final DBManager db = DBManager.getDB();
        final Connection conn  = db.getConnection();
        
        ObservableList<Team> listTeam = FXCollections.observableArrayList();
        final String retrieve = "select * from CAMPIONATO order by anno";
        try {
            final PreparedStatement statement = conn.prepareStatement(retrieve);
            final ResultSet result = statement.executeQuery();
            while (result.next()) {
                Team team = new Team();
                team.setYear(result.getInt("anno"));
                team.setName(result.getString("nomeTeam"));
                team.setLocation(result.getString("sede"));
                team.setLogo(result.getString("logo"));
                listTeam.add(team);
            }
            result.close();
            statement.close();
        } catch (SQLException e) {
            AlertTypes alert = new AlertTypesImpl();
            alert.showError(e);
        }
        
        return listTeam;
    }

}
