package org.example.marketplace.cataloguesevice.controller;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;


import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@ExtendWith(RestDocumentationExtension.class)
public class ProductControllerIT {

    @Autowired
    MockMvc mockMvc;

    @Test
    @Sql("/sql/products.sql")
    void getAllProducts_ReturnsProductList() throws Exception{

       var requestBuilder = MockMvcRequestBuilders.get("/app/products")
                .with(jwt().jwt(builder -> builder.claim("scope", "view_catalogue")));

       this.mockMvc.perform(requestBuilder)
               .andDo(print())
               .andExpectAll(
                       status().isOk(),
                       content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
                       content().json("""
                     
                     [
                       {id: 1, name: "Товар 1", details: "Описание товара 1", price: 1000},
                       {id: 3, name: "Товар 3", details: "Описание товара 3", price: 2000}
                     ]
                     """)
               );
    }

    @Test
    @Sql("/sql/products2.sql")
    void createProduct_RequestIsValid_ReturnsNewProduct() throws Exception {

        var requetBuilders = MockMvcRequestBuilders.post("/app/products/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"name": "Кефир", "details": "Бодрый кефир", "price": 200}""")
                .with(jwt().jwt(builder -> builder.claim("scope", "edit_catalogue")));

        this.mockMvc.perform(requetBuilders)
                .andDo(print())
                .andExpectAll(status().isCreated(),
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
                        content().json("""
                                {"id": 1,
                                "name": "Кефир",
                                "details": "Бодрый кефир",
                                "price": 200
                                }"""));
    }

    @Test
    @Sql("/sql/products2.sql")
    void createProduct_RequestIsInvalid_ReturnsBadRequest() throws Exception{

        var requestBuilders = MockMvcRequestBuilders.post("/app/products/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""

                        {"name": "", "details": " ", "price": 200}""")
                .with(jwt().jwt(builder -> builder.claim("scope", "edit_catalogue")));

        this.mockMvc.perform(requestBuilders)
                .andDo(print())
                .andExpectAll(status().isBadRequest(),
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
                        content().json("""
                                {
                               "errors":{"details":"Описание должно быть от 3 до 50 символов"}
                                }"""
                        ));
    }

    @Test
    @Sql("/sql/products2.sql")
    void createProduct_UserIsNotAuthorized_ReturnForbidden() throws Exception{

        var requestBuilders = MockMvcRequestBuilders.post("/app/products/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"name": "", "details": " ", "price": 200}""")
                .with(jwt().jwt(builder -> builder.claim("scope", "view_catalogue")));

        this.mockMvc.perform(requestBuilders)
                .andDo(print())
                .andExpectAll(status().isForbidden()

                        );
        
    }


    @Test
    @Sql("/sql/products.sql")
    void updateProduct_RequestIsValid_ReturnsNoContent() throws Exception {

        //given
        var requestBuilders = MockMvcRequestBuilders.put("/app/products/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                            {
                             "name": "Кефир",
                             "details": "Бодрый кефир",
                             "price": 200}
                        """)
                .with(jwt().jwt(builder -> builder.claim("scope", "edit_catalogue")));


        this.mockMvc.perform(requestBuilders)
                .andDo(print())
                .andExpectAll(
                        status().isNoContent()
                );

    }

    @Test
    @Sql("/sql/products.sql")
    void getProduct_ReturnsProduct() throws Exception {

        var requestBuilders = MockMvcRequestBuilders.get("/app/products/1")
                .with(jwt().jwt(builder -> builder.claim("scope", "edit_catalogue")));

        this.mockMvc.perform(requestBuilders)
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
                        content().json(
                                """
                                         {
                                         id: 1,
                                         name: "Товар 1",
                                         details: "Описание товара 1",
                                         price: 1000
                                         }
                                        """
                        )
                )
                .andDo(document("catalogue/products/find_all",
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("id").description("Идентификатор товара").type("long"),
                                fieldWithPath("name").description("Название товара").type("string"),
                                fieldWithPath("details").description("Описание товара").type("string"),
                                fieldWithPath("price").description("Цена товара").type("double")
                        )));
    }

    @Test
    @Sql("/sql/products.sql")
    void deleteProduct_ReturnsNoContent() throws Exception {

        var requestBuilder = MockMvcRequestBuilders.delete("/app/products/1")
                .with(jwt().jwt(builder -> builder.claim("scope", "edit_catalogue")));

        this.mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpectAll(
                        status().isNoContent()
                );
    }
}