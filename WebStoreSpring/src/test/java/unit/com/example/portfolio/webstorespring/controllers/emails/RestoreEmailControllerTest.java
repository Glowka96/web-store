package com.example.portfolio.webstorespring.controllers.emails;

import com.example.portfolio.webstorespring.services.email.RestoreEmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashMap;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class RestoreEmailControllerTest {

    @Mock
    private RestoreEmailService restoreEmailService;

    @InjectMocks
    private RestoreEmailController underTest;

    private MockMvc mvc;

    private static final String URI = "/api/v1/restore-email/confirm";

    @BeforeEach
    void initialization() {
        mvc = MockMvcBuilders.standaloneSetup(underTest)
                .build();
    }

    @Test
    void shouldConfirmRestoreEmail() throws Exception {
        String msg = "Old account email restored";
        HashMap message = new HashMap();
        message.put("message", msg);

        given(restoreEmailService.confirmRestoreEmail(anyString())).willReturn(message);

        mvc.perform(patch(URI)
                        .param("token", "token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is(msg)))
                .andDo(print());
    }
}
