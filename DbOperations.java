package it.unibo.csr.big.webpoleu.kc;

import it.unibo.csr.big.webpoleu.kc.data.AliasList;
import it.unibo.csr.big.webpoleu.kc.data.ClipList;
import it.unibo.csr.big.webpoleu.kc.data.KeywordContain;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DbOperations {

	public static final String GET_ALIAS_COUNT = "SELECT count(*) FROM alias WHERE is_keyword = 1";
	public static final String GET_ALIASES = "SELECT a.id, a.name, t.class FROM alias a, topic t "
											+"WHERE a.topic_id = t.id and a.is_keyword = 1";

	public static final String GET_ALIAS_NEW_COUNT = "SELECT count(*) FROM alias WHERE is_keyword = 1 and is_new_raw = 1";
	public static final String GET_ALIASES_NEW = "SELECT a.id, a.name, t.class FROM alias a, topic t "
												 +"WHERE a.topic_id = t.id and a.is_keyword = 1 and a.is_new_raw = 1";
	
	public static final String GET_CLIP_COUNT = "SELECT count(*) FROM clip WHERE is_raw_processed = ?" +
												"	and is_checked_for_duplicates = 1 and to_exclude = 0 ";
	public static final String GET_CLIPS = 	"SELECT id, lower(content) content FROM clip " +
											"	WHERE is_raw_processed = ? and rownum<= ? " +
											"	and is_checked_for_duplicates = 1 and to_exclude = 0 ";
	
	public static final String GET_NEXT_CLIP_TO_FIX = "SELECT id FROM clip WHERE is_raw_processed=2 AND rownum=1";
	public static final String GET_COUNT_KC_TO_FIX_IN_CLIP = "SELECT nvl(max(count(*)),0) FROM keywordcontain kc " +
															"  WHERE clip_id = ? GROUP BY clip_id ";
	public static final String GET_KC_TO_FIX_IN_CLIP = 	"SELECT alias_id, occ, name FROM keywordcontain kc, alias a " +
														"	WHERE clip_id = ? AND alias_id = a.id ";
	
	
	public final static int ALIASES_ALL = 0;
	public final static int ALIASES_ONLY_NEW = 1;
	
	// id = 194349
	public static final String GET_CLIPS_DEBUG = " SELECT id, lower(content) content FROM clip WHERE id = ? ";
	
	
	
	public static AliasList getAliases(Connection conn){
		return getAliases(conn, ALIASES_ALL);
	}
	
	@SuppressWarnings("resource")
	public static AliasList getAliases(Connection conn, int option){
		
		try {
			PreparedStatement ps;
			if(option == ALIASES_ALL) ps = conn.prepareStatement(GET_ALIAS_COUNT);
			else ps = conn.prepareStatement(GET_ALIAS_NEW_COUNT);
			
			ResultSet rs = ps.executeQuery();
			rs.next();
			final int tot = rs.getInt(1);
			rs.close();
			ps.close();
			
			AliasList aliases = new AliasList(tot); 
			
			System.out.println("Loading aliases - Start ("+tot+")");
			if(option == ALIASES_ALL) ps = conn.prepareStatement(GET_ALIASES);
			else ps = conn.prepareStatement(GET_ALIASES_NEW);
			rs = ps.executeQuery();
			
			int i=0;
			while(rs.next()){
				aliases.id[i] = rs.getInt(1);
				aliases.name[i] = rs.getString(2);
				if(rs.getString(3).equals("hashtag")) {
					aliases.isHashtag[i] = true;
					aliases.name[i] = "#" + aliases.name[i];
				}
				else {
					aliases.isHashtag[i] = false;
				}
				i++;
				//System.out.println("AliasList #" + rs.getString(1));
			}
			rs.close();
			ps.close();
			System.out.println("Loading aliases - Done");
			return aliases;
			
		} catch (SQLException e) {
			System.out.println("Loading aliases - ERROR !!!");
			e.printStackTrace();
		}
		
		return null;
	}
	

	public static ClipList getClips(Connection conn, int max, int isRawProcessed){
		
		ClipList clips = new ClipList(); 
		int i=0;
		
		try {
			PreparedStatement ps = conn.prepareStatement(GET_CLIP_COUNT);
			ps.setInt(1, isRawProcessed);
			ResultSet rs = ps.executeQuery();
			rs.next();
			int count = rs.getInt(1);
			int tot = Math.min(max, count);
			rs.close();
			ps.close();
			
			if(tot == 0) return clips;
			
			clips.id = new int[tot];
			clips.content = new String[tot];

			System.out.println("Loading clips - Start ("+tot+"/"+count+")");
			ps = conn.prepareStatement(GET_CLIPS);
			ps.setInt(1, isRawProcessed);
			ps.setInt(2, tot);
			rs = ps.executeQuery();
			long timeStart = System.currentTimeMillis(); 
			
			while(rs.next()){
//				if(i%100 == 0 && i>0){
//					long timeElaboration = System.currentTimeMillis()-timeStart;
//					System.out.println("Loading clips - "+i+"/"+tot+" ("+(i/tot)+"%) - "+(timeElaboration/1000)+"s");
//					timeStart = System.currentTimeMillis();
//				}
				clips.id[i] = rs.getInt(1); //6 sec a gruppo da 100
				clips.content[i] = rs.getString(2);
				i++;
				if(i==tot) break;
				//System.out.println("AliasList #" + rs.getString(1));
			}
			rs.close();
			ps.close();
			long timeElaboration = System.currentTimeMillis()-timeStart;
			System.out.println("Loading clips - Done - "+(timeElaboration/1000)+"s");
			
		} catch (SQLException e) {
			System.out.println("Loading clips - ERROR !!!");
			e.printStackTrace();
		}
		
		return clips;
	}

	public static ClipList getClipsDebug(Connection conn, int idClip){
		
		ClipList clips = new ClipList(); 
		int i=0;
		
		try {
			int tot = 1;
			
			clips.id = new int[tot];
			clips.content = new String[tot];

			PreparedStatement ps = conn.prepareStatement(GET_CLIPS_DEBUG);
			ps.setInt(1, idClip);
			ResultSet rs = ps.executeQuery();
			long timeStart = System.currentTimeMillis(); 
			
			while(rs.next()){
//				if(i%100 == 0 && i>0){
//					long timeElaboration = System.currentTimeMillis()-timeStart;
//					System.out.println("Loading clips - "+i+"/"+tot+" ("+(i/tot)+"%) - "+(timeElaboration/1000)+"s");
//					timeStart = System.currentTimeMillis();
//				}
				clips.id[i] = rs.getInt(1); //6 sec a gruppo da 100
				clips.content[i] = rs.getString(2);
				i++;
				if(i==tot) break;
				//System.out.println("AliasList #" + rs.getString(1));
			}
			rs.close();
			ps.close();
			long timeElaboration = System.currentTimeMillis()-timeStart;
			System.out.println("Loading clips - Done - "+(timeElaboration/1000)+"s");
			
		} catch (SQLException e) {
			System.out.println("Loading clips - ERROR !!!");
			e.printStackTrace();
		}
		
		return clips;
	}


	public static void insertKeywordContain(Connection conn, int clipId, int aliasId, int occ){

		try {
			String sql = "INSERT INTO keywordcontain (clip_id, alias_id, occ) VALUES (?,?,?)";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, clipId);
			ps.setInt(2, aliasId);
			ps.setInt(3, occ);
			ps.execute();
			ps.close();
		} 
		catch (SQLException e) {
			System.out.println("Error inserting KC: ClipList #"+clipId+", AliasList #"+aliasId+", Occ "+occ);
			e.printStackTrace();
		}
		
	}

	public static void setClipRawProcessed(Connection conn, int clipId){

		try {
			String sql = "UPDATE clip SET is_raw_processed=1 WHERE id=?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, clipId);
			ps.execute();
			ps.close();
		} 
		catch (SQLException e) {
			System.out.println("Error setting clip #"+clipId+" is_raw_processed");
			e.printStackTrace();
		}
		
	}
	
	public static Integer getNextClipIdToFix(Connection conn){
		
		try {
			PreparedStatement ps = conn.prepareStatement(GET_NEXT_CLIP_TO_FIX);
			ResultSet rs = ps.executeQuery();
			Integer clipId = null;
			if(rs.next()){
				clipId = rs.getInt(1);
			}
			rs.close();
			ps.close();
			return clipId;
		} 
		catch (SQLException e) {
			System.out.println("Loading KCs for clip - ERROR !!!");
			e.printStackTrace();
		}
		
		return null;
	}	
	
	public static KeywordContain[] getKcArrayOfClip(Connection conn, int clipId){
		
		try {
			PreparedStatement ps = conn.prepareStatement(GET_COUNT_KC_TO_FIX_IN_CLIP);
			ps.setInt(1, clipId);
			ResultSet rs = ps.executeQuery();
			rs.next();
			int tot = rs.getInt(1);
			rs.close();
			ps.close();
			
			if(tot == 0) return null;
			KeywordContain[] kcs = new KeywordContain[tot]; 
			
			ps = conn.prepareStatement(GET_KC_TO_FIX_IN_CLIP);
			ps.setInt(1, clipId);
			rs = ps.executeQuery();
			
			int i=0;
			while(rs.next()){
				kcs[i] = new KeywordContain(tot);
				kcs[i].idClip = clipId;
				kcs[i].idAlias = rs.getInt(1);
				kcs[i].occ = rs.getInt(2);
				kcs[i].aliasName = rs.getString(3);
				kcs[i].isOld = 1;
				i++;
			}
			rs.close();
			ps.close();
			
			return kcs;
		} 
		catch (SQLException e) {
			System.out.println("Loading KCs for clip - ERROR !!!");
			e.printStackTrace();
		}
		
		return null;
	}
		
	public static ArrayList<KeywordContain> getKcOfClip(Connection conn, int clipId){
		
		try {
			ArrayList<KeywordContain> kcsList = new ArrayList<KeywordContain>();

			PreparedStatement ps = conn.prepareStatement(GET_KC_TO_FIX_IN_CLIP);
			ps.setInt(1, clipId);
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()){
				kcsList.add(new KeywordContain(clipId, rs.getInt(1), rs.getString(3), rs.getInt(2), 1));
			}
			rs.close();
			ps.close();
			
			return kcsList;
		} 
		catch (SQLException e) {
			System.out.println("Loading KCs for clip - ERROR !!!");
			e.printStackTrace();
		}
		
		return null;
		
	}
	
	public static void updateKeywordContainArray(Connection conn, KeywordContain[] kcs){
		PreparedStatement ps;
		String sql = "UPDATE keywordcontain SET occ = ? WHERE clip_id = ? AND alias_id = ? ";
		
		try {
			for(int i=0; i<kcs.length; i++){
				ps = conn.prepareStatement(sql);
				ps.setInt(1, kcs[i].occ);
				ps.setInt(2, kcs[i].idClip);
				ps.setInt(3, kcs[i].idAlias);
				ps.execute();
				ps.close();
			}
		} 
		catch (SQLException e) {
			System.out.println("Error updating kcs for clip "+kcs[0].idClip);
			e.printStackTrace();
		}
	}
	
	public static void updateKeywordContain(Connection conn, int clipId, int aliasId, int occ){
		PreparedStatement ps;
		String sql = "UPDATE keywordcontain SET occ = ? WHERE clip_id = ? AND alias_id = ? ";
		
		try {
			ps = conn.prepareStatement(sql);
			ps.setInt(1, occ);
			ps.setInt(2, clipId);
			ps.setInt(3, aliasId);
			ps.execute();
			ps.close();
		} 
		catch (SQLException e) {
			System.out.println("Error updating kcs for clip "+clipId+" and alias "+aliasId);
			e.printStackTrace();
		}
	}
	
	public static void cleanDuplicates(Connection conn){

		try {
			PreparedStatement ps;
			
			String sql = "update clip set is_checked_for_duplicates = 2 where is_checked_for_duplicates = 0";
			ps = conn.prepareStatement(sql);
			ps.execute();
			ps.close();
			
			sql = "delete from clip " + //elimino le clip duplicate
					"where id in " +
					"  (select c1.id " + //seleziono le clip col crawler_id duplicato e che sono duplicate
					"  from clip c1, " +
					"    (select min(id) id, crawler_id, count(*) " + //seleziono i crawler_id che hanno duplicati
					"    from clip " +
					"    group by crawler_id " +
					"    having count(*)>1) c2 " +
					"  where c1.crawler_id = c2.crawler_id " +
					"  and c1.id <> c2.id " +
					"  and c1.is_checked_for_duplicates = 2 " +
					")";
			ps = conn.prepareStatement(sql);
			ps.execute();
			ps.close();
			
			sql = "update clip set is_checked_for_duplicates = 1 where is_checked_for_duplicates = 2";
			ps = conn.prepareStatement(sql);
			ps.execute();
			ps.close();
			
			
		} 
		catch (SQLException e) {
			System.out.println("Error deleting duplicated clips");
			e.printStackTrace();
		}
	}

	public static void updateClipIsRawProcessed(Connection conn, int old_isRawProcessed, int new_isRawProcessed) {
		try {
			PreparedStatement ps = conn.prepareStatement("UPDATE clip SET is_raw_processed = ? WHERE is_raw_processed = ? ");
			ps.setInt(1, new_isRawProcessed);
			ps.setInt(2, old_isRawProcessed);
			int tot = ps.executeUpdate();
			ps.close();
			
			System.out.println("IS_RAW_PROCESSED set to "+new_isRawProcessed+" in "+tot+" clips");
			
		} catch (SQLException e) {
			System.out.println("ERROR setting IS_RAW_PROCESSED for clips !!!");
			e.printStackTrace();
		}
	}
}
