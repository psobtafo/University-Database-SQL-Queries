/*****************************
Query the University Database
*****************************/
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.CallableStatement;
import java.util.*;
import java.lang.String;

public class MyQuery {
	private Connection conn = null;
	 private Statement statement = null;
	 private ResultSet resultSet = null;
	 private ResultSet resultSettwo = null;
   
   public MyQuery(Connection c)throws SQLException
   {
       conn = c;
       // Statements allow to issue SQL queries to the database
       statement = conn.createStatement();
   }
   
   public void findFall2009Students() throws SQLException
   {
       String query  = "select distinct name from student natural join takes where semester = \'Fall\' and year = 2009;";

       resultSet = statement.executeQuery(query);
       System.out.print(resultSet);
   }
   
   public void printFall2009Students() throws IOException, SQLException
   {
	      System.out.println("******** Query 0 ********");
        System.out.println("name");
        while (resultSet.next()) {
			// It is possible to get the columns via name
			// also possible to get the columns via the column number which starts at 1
			String name = resultSet.getString(1);
        System.out.println(name);
  		}        
   }

   public void findGPAInfo() throws SQLException
   {
	   //String query = "select id,name,(sum(numerical grades * tot_cred)/sum(credits)) as GPA from student";
	   String query = "SELECT ID,name,sum(CASE grade\n"
	   		+ "	when 'A' then 4.0\n"
	   		+ "	when 'A-' then 3.67\n"
	   		+ "	when 'B+' then 3.33\n"
	   		+ "	when 'B' then 3.0\n"
	   		+ "	when 'B-' then 2.67\n"
	   		+ "	when 'C+' then 2.33\n"
	   		+ "	when 'C' then 2.0\n"
	   		+ "	when 'C-' then 1.67\n"
	   		+ "	when 'D+' then 1.33\n"
	   		+ "	when 'D' then 1.0\n"
	   		+ "	when 'D-' then 0.67\n"
	   		+ "	when 'F' then 0.0\n"
	   		+ "	ELSE 0 \n"
	   		+ "END *CREDITS)/sum(CREDITS) \n"
	   		+ "AS GPA\n"
	   		+ "\n"
	   		+ "FROM student NATURAL JOIN takes NATURAL JOIN course\n" //Fix join statements
	   		+ "WHERE GRADE IS NOT NULL AND GRADE != 'F'\n"
	   		+ "GROUP BY ID, NAME;"
	   		//+ "SELECT SUM(grade * tot_cred) / SUM(tot_cred) FROM student";

;
   

;
			   					
	   resultSet = statement.executeQuery(query);
	   System.out.print(resultSet);
   }
   
   public void printGPAInfo() throws IOException, SQLException
   {
		   System.out.println("******** Query 1 ********");
		   System.out.printf("%-10s%-10s%-10s \n", "ID","name", "GPA");
	        while (resultSet.next()) {
				// It is possible to get the columns via name
				// also possible to get the columns via the column number which starts at 1
				String name = resultSet.getString(2);
	        //System.out.println(name);
				String ID = resultSet.getString(1);
				Double GPA = resultSet.getDouble(3);
	        System.out.printf("%-10s%-10s%-10.2f \n", ID,name,GPA);
	  		}        
		   //System.out.println("");
   }

   public void findMorningCourses() throws SQLException
   {
	   String query ="SELECT DISTINCT course.course_id, section.sec_id, course.title, takes.semester, section.year, instructor.name,  count( DISTINCT takes.ID) as enrollment\n"
	   		+ "FROM course JOIN section on (course.course_id = section.course_id)\n"
	   		+ "JOIN time_slot on (section.time_slot_id = time_slot.time_slot_id)\n"
	   		+ "JOIN takes on (takes.course_id = section.course_id AND takes.sec_id = section.sec_id AND takes.semester = section.semester AND takes.year = section.year) \n"
	   		+ "JOIN teaches  on (teaches.course_id = takes.course_id  AND takes.sec_id = teaches.sec_id AND takes.semester = teaches.semester AND takes.year = teaches.year)\n"
	   		+ "JOIN instructor on (instructor.ID = teaches.ID)\n"
	   		+ "WHERE start_hr <=12 \n"
	   		+ "GROUP BY course_id, sec_id, semester, year, name;\n"
	   		+ "";
			   resultSet = statement.executeQuery(query);
       System.out.print(resultSet);

   }

   public void printMorningCourses() throws IOException, SQLException
   {
	   	System.out.println("******** Query 2 ********");
	   	System.out.printf("%-10s%-10s%-30s%-10s%-10s%-15s%-10s \n", "course_id","sec_id", "title","semester","year","name","enrollment");
        while (resultSet.next()) {
			// It is possible to get the columns via name
			// also possible to get the columns via the column number which starts at 1
			String course_id = resultSet.getString(1);
        //System.out.println(name);
			String sec_id = resultSet.getString(2);
			String title = resultSet.getString(3);
			String semester = resultSet.getString(4);
			String year = resultSet.getString(5);
			String name = resultSet.getString(6);
			Double enrollment = resultSet.getDouble(7);
        System.out.printf("%-10s%-10s%-30s%-10s%-10s%-15s%-10.2f \n", course_id,sec_id,title,semester,year,name,enrollment);
        }
   }

   public void findBusyClassroom() throws SQLException
   {
	   String query ="SELECT building,room_number,count(time_slot_id) as frequency \n"
	   		+ "FROM section \n"
	   		+ "NATURAL JOIN classroom \n"
	   		+ "GROUP BY building, room_number\n"
	   		+ "HAVING count(time_slot_id)>= all(SELECT count(time_slot_id) as frequency \n"
	   		+ "FROM section \n"
	   		+ "NATURAL JOIN classroom \n"
	   		+ "GROUP BY building, room_number);";
	   resultSet = statement.executeQuery(query);
       System.out.print(resultSet);
	   

   }

   public void printBusyClassroom() throws IOException, SQLException
   {
		   System.out.println("******** Query 3 ********");
		   System.out.printf("%-10s%-10s%-10s \n", "building","room_number", "frequency");
	        while (resultSet.next()) {
				// It is possible to get the columns via name
				// also possible to get the columns via the column number which starts at 1
				String building = resultSet.getString(1);
	        //System.out.println(name);
				Double room_number = resultSet.getDouble(2);
				Double frequency = resultSet.getDouble(3);
	        System.out.printf("%-10s%-10.2f-10.2f \n", building,room_number,frequency);
	        }
   }

   public void findPrereq() throws SQLException
   {
	   String query  = "SELECT C1.title,IF(C2.title is null, \"N/A\",C2.title) as prereq\n"
	   		+ "FROM course C1 LEFT JOIN prereq on C1.course_id=prereq.course_id\n"
	   		+ "LEFT JOIN course C2 on C2.course_id=prereq.course_id\n"
	   		+ "GROUP BY C1.title,prereq; \n"; // \n

       resultSet = statement.executeQuery(query);
       System.out.print(resultSet);

   }

   public void printPrereq() throws IOException, SQLException
   {
		   System.out.println("******** Query 4 ********");
		   System.out.printf("%-30s%-10s \n", "title","prereq");
	        while (resultSet.next()) {
				// It is possible to get the columns via name
				// also possible to get the columns via the column number which starts at 1
				String title = resultSet.getString(1);
	        //System.out.println(name);
				String prereq = resultSet.getString(2);
				
	        System.out.printf("%-30s%-10s \n", title,prereq);
	        }
   }

   public void updateTable() throws SQLException
   {
	   String query ="UPDATE studentCopy \n"
	   		+ "SET tot_cred = (SELECT sum(credits) \n"
	   		+ "from course natural join takes \n"
	   		+ "where takes.id = studentCopy.id and takes.grade <> 'F' AND takes.grade is not null);\n"
	   		+ "\n"
	   		+ "";
	   query = "SELECT * FROM studentCopy;";
			  
		resultSet = statement.executeQuery(query);//executeQuery
       System.out.print(resultSet);
		

   }

   public void printUpdatedTable() throws IOException, SQLException
   {
		   System.out.println("******** Query 5 ********");
		   System.out.printf("%-10s%-10s%-15s%-10s \n", "ID","name", "dept_name","tot_cred");
	        while (resultSet.next()) {
				// It is possible to get the columns via name
				// also possible to get the columns via the column number which starts at 1
				String name = resultSet.getString(2);
	        //System.out.println(name);
				Double ID = resultSet.getDouble(1);
				String dept_name = resultSet.getString(3);
				Double tot_cred = resultSet.getDouble(4);
	        System.out.printf("%-10.2f%-10s%-15s%-10.2f \n", ID,name,dept_name,tot_cred);
	        }
   }
	
	 public void findDeptInfo() throws SQLException
	 {
		  System.out.println("******** Query 6 ********");
		  System.out.printf("%-10s%-10s%-10s \n", "ID","name", "GPA");
	        while (resultSet.next()) {
				// It is possible to get the columns via name
				// also possible to get the columns via the column number which starts at 1
				String name = resultSet.getString(2);
	        //System.out.println(name);
				String ID = resultSet.getString(1);
				Double GPA = resultSet.getDouble(3);
	        System.out.printf("%-10s%-10s%-10.2f \n", ID,name,GPA);
	        }
	 }
   
   
   public void findFirstLastSemester() throws SQLException
   {
	   String query = "SELECT DISTINCT student.ID, name,\n"
	   		+ "concat((SELECT DISTINCT \n"
	   		+ "CASE \n"
	   		+ "	WHEN EXISTS(SELECT semester \n"
	   		+ "    FROM takes \n"
	   		+ "    WHERE semester = 'Spring' AND student.ID = takes.ID AND takes.year\n"
	   		+ "                        = (SELECT min(year) as year \n"
	   		+ "                        FROM takes \n"
	   		+ "                        WHERE student.ID = takes.ID)) \n"
	   		+ "                        THEN 'Spring'\n"
	   		+ "						WHEN EXISTS(SELECT semester \n"
	   		+ "                        FROM takes \n"
	   		+ "                        WHERE semester = 'Summer' AND student.ID = takes.ID AND takes.year\n"
	   		+ "                        = (SELECT min(year) as year \n"
	   		+ "                        FROM takes \n"
	   		+ "                        WHERE student.ID = takes.ID)) \n"
	   		+ "                        THEN 'Summer'\n"
	   		+ "						ELSE 'Fall' \n"
	   		+ "                        END\n"
	   		+ "                FROM takes\n"
	   		+ "                GROUP BY ID,year), '  ',  (SELECT min(year) as year \n"
	   		+ "                FROM takes \n"
	   		+ "                WHERE student.ID = takes.ID)) as First_Semester,\n"
	   		+ "                concat((SELECT DISTINCT CASE \n"
	   		+ "                WHEN EXISTS(SELECT semester \n"
	   		+ "                FROM takes \n"
	   		+ "                WHERE semester = 'Fall' and student.ID = takes.ID and takes.year \n"
	   		+ "                              = (SELECT max(year) as year \n"
	   		+ "                              FROM takes \n"
	   		+ "                              WHERE student.ID = takes.ID)) \n"
	   		+ "                              THEN 'Fall'\n"
	   		+ "                                WHEN EXISTS(SELECT semester \n"
	   		+ "                                FROM takes \n"
	   		+ "                                WHERE semester = 'Summer' and student.ID = takes.ID and takes.year \n"
	   		+ "                               = (SELECT max(year) as year \n"
	   		+ "                               FROM takes \n"
	   		+ "                               WHERE student.ID = takes.ID)) \n"
	   		+ "                               THEN 'Summer'\n"
	   		+ "                                ELSE 'Spring' \n"
	   		+ "                                END\n"
	   		+ "                FROM takes\n"
	   		+ "                GROUP BY ID,year), ' ',(SELECT max(year) as year \n"
	   		+ "                FROM takes \n"
	   		+ "                WHERE student.ID = takes.ID)) as Last_Semester\n"
	   		+ "                    from student\n"
	   		+ "                WHERE EXISTS(SELECT ID \n"
	   		+ "                FROM student \n"
	   		+ "                NATURAL JOIN takes \n"
	   		+ "                WHERE student.ID = ID);\n"
	   		+ "";
	   resultSet = statement.executeQuery(query);
       System.out.print(resultSet);
	   

   }

   public void printFirstLastSemester() throws IOException, SQLException
   {
       System.out.println("******** Query 7 ********");
       System.out.printf("%-10s%-10s%-18s%-10s \n", "ID","name", "First_Semester","Last_Semester");
       while (resultSet.next()) {
			// It is possible to get the columns via name
			// also possible to get the columns via the column number which starts at 1
			String name = resultSet.getString(2);
       //System.out.println(name);
			Double ID = resultSet.getDouble(1);
			String First_Semester = resultSet.getString(3);
			String Last_Semester =  resultSet.getString(4);
       System.out.printf("%-10.2f%-10s%-18s%-10s \n", ID,name,First_Semester,Last_Semester);
       }
   }


}
