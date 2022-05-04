package io.github.wimdeblauwe.hsbt.mvc;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


@WebMvcTest(HtmxHandlerMethodArgumentResolverTestController.class)
class HtmxHandlerMethodArgumentResolverTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TestService service;

    @Test
    void testIfNonHtmxRequest() throws Exception {
        mockMvc.perform(get("/method-arg-resolver"));

        ArgumentCaptor<HtmxRequest> captor = ArgumentCaptor.forClass(HtmxRequest.class);
        verify(service).doSomething(captor.capture());

        HtmxRequest request = captor.getValue();
        assertThat(request).isNotNull();
        assertThat(request.isHtmxRequest()).isFalse();
    }

    @Test
    void testIfHtmxRequest() throws Exception {
        mockMvc.perform(get("/method-arg-resolver")
                                .header("HX-Request", "true"));

        ArgumentCaptor<HtmxRequest> captor = ArgumentCaptor.forClass(HtmxRequest.class);
        verify(service).doSomething(captor.capture());

        HtmxRequest request = captor.getValue();
        assertThat(request).isNotNull();
        assertThat(request.isHtmxRequest()).isTrue();
        assertThat(request.isBoosted()).isFalse();
    }

    @Test
    void testHxBoosted() throws Exception {
        mockMvc.perform(get("/method-arg-resolver")
                                .header("HX-Request", "true")
                                .header("HX-Boosted", "true"));

        ArgumentCaptor<HtmxRequest> captor = ArgumentCaptor.forClass(HtmxRequest.class);
        verify(service).doSomething(captor.capture());

        HtmxRequest request = captor.getValue();
        assertThat(request).isNotNull();
        assertThat(request.isHtmxRequest()).isTrue();
        assertThat(request.isBoosted()).isTrue();
    }
}
