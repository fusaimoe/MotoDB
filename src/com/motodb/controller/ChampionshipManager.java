package com.motodb.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import com.motodb.model.Championship;

public class ChampionshipManager {    
    
    public void insertChampionship(final int year, final int edition) {
        final DBManager db = new DBManager();
        final Connection conn  = db.getConnection();
        
        final java.sql.PreparedStatement statement;
        final String insert = "insert into CAMPIONATO(anno, edizione) values (?,?)";
        try {
            statement = conn.prepareStatement(insert);
            statement.setInt(1, year);
            statement.setInt(2, edition);
            statement.executeUpdate();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
    
    public List<Championship> showChampionship() {

        final DBManager db = new DBManager();
        final Connection conn  = db.getConnection();
        
        List<Championship> listChampionship = new LinkedList<>();
        final String retrieve = "select * from CAMPIONATO";
        try {
            final PreparedStatement statement = conn.prepareStatement(retrieve);
            final ResultSet result = statement.executeQuery();
            if (result.next()) {
                Championship championship = new Championship();
                championship.setYear(result.getInt("anno"));
                championship.setEdition(result.getInt("edition"));
                listChampionship.add(championship);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return listChampionship;
        
    }
    
}

