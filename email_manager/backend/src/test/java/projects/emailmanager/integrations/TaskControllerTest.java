package projects.emailmanager.integrations;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import projects.emailmanager.entrypoints.TaskRestController;

@RunWith(SpringRunner.class)
@WebMvcTest(TaskRestController.class)
public class TaskControllerTest {
    Logger logger = LoggerFactory.getLogger(TaskControllerTest.class);
    @Autowired
    private MockMvc mvc;


    @Test
    public void shouldCreatesANewTask() {
        logger.info("This is my first test");
    }
}
