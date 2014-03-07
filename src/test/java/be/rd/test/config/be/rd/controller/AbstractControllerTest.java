package be.rd.test.config.be.rd.controller;

import be.rd.test.config.ControllerTestConfig;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by ruben on 3/4/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ControllerTestConfig.class})
@ActiveProfiles("test")
public class AbstractControllerTest
{

}
