# UniversityDB SQL + Java (JDBC) Project

This project combines SQL querying with Java programming using JDBC to interact with a relational database (`universityDB`). It demonstrates how to write SQL queries and integrate them into a standalone Java application to retrieve and manipulate data.

## 📁 Project Files

- `TestMyQuery.java`: Main Java file for running the program. Only three variables need editing: `mydatabase`, `username`, and `password`.
- `MyQuery.java`: Contains SQL queries embedded in Java. This is the primary file where you will implement and modify query functions.
- `mysql-connector-java-8.0.19.jar`: Required JDBC driver for connecting Java to MySQL.

## 📋 SQL Queries

1. **Query 0**: List students who took classes in Fall 2009 (sample).
2. **Query 1**: Calculate GPA for each student, converting letter grades to numerical values.
3. **Query 2**: Find morning course sections with enrollments.
4. **Query 3**: Find the most frequently used classroom (no `ORDER BY` or `LIMIT`).
5. **Query 4**: Find prerequisites for each course, include courses with no prereqs as `N/A`.
6. **Query 5**: Create and update a `studentCopy` table with accurate `tot_cred` values.
7. **Query 6**: Write and use a stored procedure `deptInfo` that returns department stats.
8. **Query 7**: Determine the first and last semesters each student has taken a course.
