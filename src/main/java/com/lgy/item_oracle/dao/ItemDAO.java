package com.lgy.item_oracle.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.lgy.item_oracle.dto.ItemDTO;

public class ItemDAO {
	DataSource dataSource;
	
	public ItemDAO() {
		try {
			Context ctx = new InitialContext();
			dataSource = (DataSource) ctx.lookup("java:comp/env/jdbc/oracle");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void write(String name, int price, String description) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = "insert into item(name, price, description) "
								  + "values(?,?,?)";
		
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, name);
			pstmt.setInt(2, price);
			pstmt.setString(3, description);
			pstmt.executeUpdate();
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			try{
				if(pstmt != null){pstmt.close(); }
				if(conn != null){conn.close(); }
			}catch(Exception se){
				se.printStackTrace();
			}
		}
		
	}
	
//	게시판 목록 조회(type parameter : 게시글 객체)
	public ArrayList<ItemDTO> list() {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "select name, price, description from item";
		ArrayList<ItemDTO> dtos = new ArrayList<>();
		
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				String name =rs.getString("name");
				int price =rs.getInt("price");
				String description =rs.getString("description");
				
//				하나의 게시글 객체
				ItemDTO dto = new ItemDTO(name, price, description);
//				게시글을 추가(dtos : 여러 게시글이 될수 있음)
				dtos.add(dto);
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			try{
				if(rs != null){rs.close(); }
				if(pstmt != null){pstmt.close(); }
				if(conn != null){conn.close(); }
			}catch(Exception se){
				se.printStackTrace();
			}
		}
		
		return dtos;
	}
}
