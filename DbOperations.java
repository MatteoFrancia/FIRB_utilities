package com.big.firb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DbOperations {
    /**
     * Retrieve clip_id where the clip has been analyzed but the analysis has been never written
     */
    public static final String GET_ID_TO_ANALYZE="SELECT CLIP.ID FROM CLIP LEFT JOIN SENTENCE ON CLIP.ID=SENTENCE.CLIP_ID WHERE SENTENCE.CLIP_ID IS NULL AND CLIP.IS_PROCESSED=1";

    public static String[] getClipID(Connection conn){
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<String> clipIDs=new ArrayList<String>();

        try {
            ps = conn.prepareStatement(GET_ID_TO_ANALYZE);
            rs = ps.executeQuery();

            while(rs.next())
            {
                clipIDs.add(rs.getString(1));
            }

            rs.close();
            ps.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return clipIDs.toArray(new String[clipIDs.size()]);
    }
}