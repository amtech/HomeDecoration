import com.giants3.hd.server.app.controller.AppQuotationController;
import com.giants3.hd.server.app.service.AppQuotationService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("file:/WEB-INF/mvc-dispatcher-servlet.xml")
public class AppQuotationControllerTest {

    private MockMvc mockMvc;


    @Autowired
    AppQuotationController appQuotationController;
    //Add WebApplicationContext field here

    //The setUp() method is omitted.

    @Test
    public void findAll_ShouldAddTodoEntriesToModelAndRenderTodoListView() throws Exception {

    }

    @Before
    public void setup() {
       // MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(appQuotationController).build();
    }

    @Test
    public void should_return_status_success_when_send_mail_success() throws Exception {
//        when(mailService.send("test@test.com", "test", "test")).thenReturn(new Result("成功"));
//
        mockMvc.perform(post("/app/quotation/createOne")
                .param("recipients", "test@test.com")
                .param("subject", "test")
                .param("content", "test"))
                .andDo(print())
                .andExpect(status().isOk()).andExpect(content().string(is("{\"status\":\""  + "\"}")));

//        verify(mailService).send("test@test.com", "test", "test");
    }
}
