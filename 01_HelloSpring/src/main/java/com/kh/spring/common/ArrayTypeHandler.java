package com.kh.spring.common;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

//TypeHandler 인터페이스를 통해 <>안에 있는 타입을 메소드를 통해서 강제로 구현함.
//배열을 구현하기 위해 <String[]>형으로 만듦.
public class ArrayTypeHandler implements TypeHandler<String[]> {

   @Override
   public void setParameter(PreparedStatement ps, int i, String[] parameter, JdbcType jdbcType) throws SQLException {
      //String 형식으로 값을 넣어주게 세팅
      if(parameter!=null) {
         ps.setString(i, String.join(",", parameter));         
      }else {
         ps.setString(i, "");
      }
   }

   @Override
   public String[] getResult(ResultSet rs, String columnName) throws SQLException {
      String temp=rs.getString(columnName);
      return temp.split(",");
   }

   @Override
   public String[] getResult(ResultSet rs, int columnIndex) throws SQLException {
      String temp=rs.getString(columnIndex);
      return temp.split(",");
   }

   @Override
   public String[] getResult(CallableStatement cs, int columnIndex) throws SQLException {
      //CallableStatement : db 안에 프로시져를 등록했을때 그 프로시져를 실행해주는 아이.
      String temp=cs.getString(columnIndex);
      return temp.split(",");
   }

}