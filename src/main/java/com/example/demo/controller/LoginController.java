package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.demo.model.ModelType;
import com.example.demo.model.User;
import com.example.demo.utils.ConnectionModel;

@RestController
@RequestMapping("/api/v1")
public class LoginController {
	@GetMapping("/login")
	public ResponseEntity<HashMap<String,Object>> login(@RequestParam(defaultValue="0.0") String email,
    		@RequestParam(defaultValue="0.0") String password){
   	 Connection c = null;
   	 PreparedStatement st = null;
     ResultSet rs = null;
     HashMap<String,Object> h = new HashMap<String,Object>();
     try {
        Class.forName("org.postgresql.Driver");
        
        c = ConnectionModel.getConnection();
        c.setAutoCommit(false);

        st = c.prepareStatement("Select id,email, password from users where email=? and password=?");
        st.setString(1, email);
        st.setString(2, password);
        rs = st.executeQuery();
        
        
        if ( rs.next() ) {
           int id = rs.getInt("id");
           h.put("found", true);
           h.put("id", id);
        }
        else {
        	h.put("found", false);
        }
        rs.close();
        st.close();
        c.close();
        return new ResponseEntity<>(h, HttpStatus.OK);
     } catch ( Exception e ) {
        System.err.println( e.getClass().getName()+": "+ e.getMessage() );
        return new ResponseEntity<>(h,HttpStatus.BAD_REQUEST);
     }
	}
	
	
	@PostMapping("/signup")
	public ResponseEntity<HashMap<String,Object>> signup(@RequestBody User user){
   	 Connection c = null;
   	 PreparedStatement st = null;
     
     try {
        Class.forName("org.postgresql.Driver");
        
        c = ConnectionModel.getConnection();
    
        st = c.prepareStatement("insert into users(email,password) values(?,?)");
        st.setString(1, user.getEmail());
        st.setString(2, user.getPassword());
        st.executeUpdate();
        
        st.close();
        c.close();
        return new ResponseEntity<>(HttpStatus.CREATED);
     } catch ( Exception e ) {
        System.err.println( e.getClass().getName()+": "+ e.getMessage() );
        HashMap<String,Object> h = new HashMap<String,Object>();
        h.put("error", e.getClass().getName()+": "+ e.getMessage());
        return new ResponseEntity<>(h,HttpStatus.BAD_REQUEST);
     }
	}
}
