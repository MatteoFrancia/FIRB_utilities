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
    private static final String GET_ID_TO_ANALYZE="SELECT CLIP.ID FROM CLIP LEFT JOIN SENTENCE ON CLIP.ID=SENTENCE.CLIP_ID WHERE SENTENCE.CLIP_ID IS NULL AND CLIP.IS_PROCESSED=1";
    /**
     * Retrieve clip status given the clip ID
     */
    private static final String GET_CLIP_STATUS="SELECT C.ID, C.IS_PROCESSED, INTERNAL.SENTNUMB FROM CLIP C, (SELECT COUNT(CLIP_ID) SENTNUMB FROM SENTENCE WHERE CLIP_ID= ?) INTERNAL WHERE C.ID=?";

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

    public static int[] getClipStatus(Connection conn,int clipID){
        PreparedStatement ps = null;
        ResultSet rs = null;
        int[] status =new int[3];
        try {
            ps = conn.prepareStatement(GET_CLIP_STATUS);

            ps.setInt(1, clipID);
            ps.setInt(2,clipID);

            rs = ps.executeQuery();

            while(rs.next())
            {
                status[0]=rs.getInt(1);
                status[1]=rs.getInt(2);
                status[2]=rs.getInt(3);
            }
            rs.close();
            ps.close();
        } catch (SQLException e1) {
            e1.printStackTrace();
        }

        return status;
    }
}