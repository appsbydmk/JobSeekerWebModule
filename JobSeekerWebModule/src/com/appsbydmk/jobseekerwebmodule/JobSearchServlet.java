package com.appsbydmk.jobseekerwebmodule;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/JobSearchServlet")
public class JobSearchServlet extends HttpServlet {
	public JobSearchServlet() {
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
			String result = "";
			String query = "select * from (select * from JS_JOBS where lower(job_title) like ? order by job_posted_date desc) where ROWNUM <=1";
			String q = "select * from js_jobs";
			con = DbConnectionHandler.getConnection();
			pt = con.prepareStatement(query);
			pt.setString(1, "%" + line + "%");
			rs = pt.executeQuery();
			if (rs.next()) {
				result += rs.getDate(2) + "\n";
				result += rs.getString(3) + "\n";
				result += rs.getString(4) + "\n";
			}
			response.setStatus(HttpServletResponse.SC_OK);
			writer = new OutputStreamWriter(response.getOutputStream());
			writer.write(result);
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
