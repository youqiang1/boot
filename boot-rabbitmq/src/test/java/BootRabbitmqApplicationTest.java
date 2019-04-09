import com.yq.rabbitmq.BootRabbitmqApplication;
import com.yq.rabbitmq.mq.sender.Sender;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * <p> 测试</p>
 * @author youq  2019/4/9 17:31
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BootRabbitmqApplication.class)
public class BootRabbitmqApplicationTest {

    @Autowired
    private Sender sender;

    @Test
    public void hello() {
        sender.send("hello test!");
    }

}
