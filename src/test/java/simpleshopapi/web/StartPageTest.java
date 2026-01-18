package simpleshopapi.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import simpleshopapi.controller.StartPageController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StartPageController.class)
public class StartPageTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void testStartPage() throws Exception {
        mvc.perform(get("/"))
            .andExpect(status().isOk());
    }
}
