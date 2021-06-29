package com.soen390.erp.users;

import com.soen390.erp.users.repository.UserRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CreateUserTests {
    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    private static final String username = "admin123@msn.com";

    @Test
    @WithMockUser("ROLE_ADMIN")
    void testLandingPage() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string("Let's ace this!"));
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    void givenRoleAdmin_whenCreateUser_thenCreated() throws Exception {
        String user = String.format("""
                {
                    "firstname":"Zubair",
                    "lastname":"Nurie",
                    "username":"%s",
                    "password":"soen390",
                    "role":"ROLE_ADMIN",
                    "active":true
                }""", username);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/create-user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(user)
                .with(csrf())
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isCreated())
//                .andExpect(content().string("Created!"))
                .andReturn();

        System.out.println(mvcResult.getResponse().getContentAsString());
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    void givenRoleAdmin_whenCreateExistingUser_thenForbidden() throws Exception {
        String user = String.format("""
                {
                    "firstname":"Zubair",
                    "lastname":"Nurie",
                    "username":"%s",
                    "password":"12345",
                    "role":"ROLE_ADMIN",
                    "active":true
                }""", username);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/create-user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(user)
                .with(csrf())
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isForbidden())
                .andExpect(content().string("Already Exists!"))
                .andReturn();

        System.out.println(mvcResult.getResponse().getContentAsString());
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void givenRoleAdmin_whenCreateWithUnauthorizedRole_thenForbidden() throws Exception {
        String user = """
                {
                    "firstname":"Zubair",
                    "lastname":"Nurie",
                    "username":"zubair1@gmail.com",
                    "password":"12345",
                    "role":"ROLE_ADMIN",
                    "active":true
                }""";

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/create-user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(user)
                .with(csrf())
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isForbidden())
                .andReturn();

        System.out.println(mvcResult.getResponse().getContentAsString());
    }

    @AfterAll
    void tearDown() {
        userRepository.findByUsername(username)
                .ifPresent(userRepository::delete);
    }
}
