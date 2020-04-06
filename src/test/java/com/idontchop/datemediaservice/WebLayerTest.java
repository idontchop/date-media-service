package com.idontchop.datemediaservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.idontchop.datemediaservice.controllers.MainController;
import com.idontchop.datemediaservice.services.DataApiService;


@WebMvcTest(MainController.class)
@AutoConfigureRestDocs(outputDir = "target/snippets")
public class WebLayerTest {
	
	@Autowired
	private MockMvc mockMvc;
		
	@MockBean
	private DataApiService dataApiService;
	
	@Test
	public void testHelloWorld () throws Exception {
		this.mockMvc.perform( get("/helloWorld") )
			.andExpect( status().isOk())
			.andExpect( content().string( containsString("media-service")) )
			.andDo( document("helloWorld"));
	}

}
