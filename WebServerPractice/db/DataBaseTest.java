package db;

import model.User;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DataBaseTest {

    @Test
    public void addUser_저장한_유저를_아이디로_조회할_수_있다() {
        User user = new User("dbtest_user1", "pw", "테스트", "dbtest1@test.com");
        DataBase.addUser(user);

        User found = DataBase.findUserById("dbtest_user1");
        assertEquals("테스트", found.getName());
        assertEquals("dbtest1@test.com", found.getEmail());
    }

    @Test
    public void findAll_저장한_유저가_목록에_포함된다() {
        User user = new User("dbtest_user2", "pw", "김철수", "dbtest2@test.com");
        DataBase.addUser(user);

        boolean found = DataBase.findAll().stream()
                .anyMatch(u -> "dbtest_user2".equals(u.getUserId()));
        assertTrue(found);
    }
}
