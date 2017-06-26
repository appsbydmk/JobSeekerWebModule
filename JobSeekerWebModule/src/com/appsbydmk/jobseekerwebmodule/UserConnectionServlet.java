package com.appsbydmk.jobseekerwebmodule;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.sql.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserConnectionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public UserConnectionServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		BufferedReader br = null;
		OutputStreamWriter writer = null;
		ResultSet rs = null;
		PreparedStatement pt = null;
		Connection con = null;
		try {
			br = new BufferedReader(new InputStreamReader(request.getInputStream()));
			String line = "";
			char[] charRead = new char[10];
			int n = 0;
			while ((n = br.read(charRead)) > 0) {
				line += String.copyValueOf(charRead, 0, n);
				charRead = new char[10];
			}
			String[] userDetails = line.split("\n");
			String status = "";
			String query = "SELECT * FROM jobseekeruser WHERE js_uname = ? AND js_pwd = ?";
			con = DbConnectionHandler.getConnection();
			pt = con.prepareStatement(query);
			pt.setString(1, userDetails[0]);
			pt.setString(2, userDetails[1]);
			rs = pt.executeQuery();
			if (rs.next()) {
				status = "success";
				System.out.println(rs.getString(1));
				System.out.println(rs.getString(2));
			} else {
				status = "failure";
			}
			response.setStatus(HttpServletResponse.SC_OK);
			writer = new OutputStreamWriter(response.getOutputStream());
			writer.write(status);
			writer.flush();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
				if (writer != null)
					writer.close();
				if (rs != null)
					rs.close();
				if (pt != null)
					pt.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
