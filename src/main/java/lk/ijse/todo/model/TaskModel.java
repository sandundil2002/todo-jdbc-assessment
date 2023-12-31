package lk.ijse.todo.model;

import lk.ijse.todo.db.DBConnection;
import lk.ijse.todo.dto.TaskDTO;
import lk.ijse.todo.dto.tm.CompleteTm;
import lk.ijse.todo.dto.tm.DueTm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TaskModel {
    public static String getCompiled() throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT SUM(CASE WHEN isCompleted = 1 THEN 1 ELSE 0 END) FROM task");
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getString(1);
        }
        return "0";
    }

    public static String getDueTask() throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT SUM(CASE WHEN isCompleted = 0 THEN 1 ELSE 0 END) FROM task");
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getString(1);
        }
        return "0";
    }

    public static boolean saveTask(TaskDTO taskDTO) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement statement = connection.prepareStatement("INSERT INTO task VALUES (?,?,?,?,?)");
        statement.setObject(1, taskDTO.getTask_id());
        statement.setObject(2, taskDTO.getEmail());
        statement.setObject(3, taskDTO.getDescription());
        statement.setObject(4, taskDTO.getDueDate());
        statement.setObject(5, taskDTO.getIsCompleted());

        int i = statement.executeUpdate();
        return 0 < i;
    }

    public static List<CompleteTm> getCompiledList() throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM task where isCompleted=1");
        ResultSet resultSet = statement.executeQuery();
        List<CompleteTm> list = new ArrayList<>();
        while (resultSet.next()) {
            CompleteTm completeTm = new CompleteTm();
            completeTm.setDescription(resultSet.getString(3));
            completeTm.setDueDate(resultSet.getString(4));
            completeTm.setId(Integer.parseInt(resultSet.getString(1)));
            list.add(completeTm);
        }
        return list;
    }

    public static List<DueTm> getDueList() throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM task where isCompleted=0");
        ResultSet resultSet = statement.executeQuery();
        List<DueTm> list = new ArrayList<>();
        while (resultSet.next()) {
            DueTm dueTm = new DueTm();
            dueTm.setDescription(resultSet.getString(3));
            dueTm.setDueDate(resultSet.getString(4));
            dueTm.setId(Integer.parseInt(resultSet.getString(1)));

            list.add(dueTm);
        }
        return list;
    }

    public static boolean delete(int id) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement statement = connection.prepareStatement("DELETE FROM task WHERE task_id=?");
        statement.setObject(1, id);
        int i = statement.executeUpdate();
        return 0 < i;
    }

    public static boolean Update(int id) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement statement = connection.prepareStatement("UPDATE task SET isCompleted=1 WHERE task_id=?");
        statement.setObject(1, id);
        int i = statement.executeUpdate();
        return 0 < i;
    }

    public static int generateId() throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT task_id FROM task ORDER BY task_id DESC LIMIT 1");
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            String id = resultSet.getString("task_id");
            int newTaskId = Integer.parseInt(id.replace("00-", "")) + 1;
            return newTaskId;
        } else {
            return 1;
        }
    }
}
