package io.github.pauliustechin.egzaminas.feature.recipe.controller;

import io.github.pauliustechin.egzaminas.exception.ResourceNotFoundException;
import io.github.pauliustechin.egzaminas.feature.recipe.dto.CreateRecipeRequest;
import io.github.pauliustechin.egzaminas.feature.recipe.dto.RecipeListResponse;
import io.github.pauliustechin.egzaminas.feature.recipe.dto.RecipeResponse;
import io.github.pauliustechin.egzaminas.feature.recipe.service.RecipeService;
import io.github.pauliustechin.egzaminas.feature.user.dto.UserRole;
import io.github.pauliustechin.egzaminas.feature.user.model.User;
import io.github.pauliustechin.egzaminas.security.jwt.AuthTokenFilter;
import io.github.pauliustechin.egzaminas.security.jwt.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.time.Instant;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

@WebMvcTest(controllers = RecipeController.class)
@AutoConfigureMockMvc(addFilters = false)
class RecipeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @MockitoBean
    private RecipeService recipeService;

    @MockitoBean
    private JwtUtils jwtUtils;

    @MockitoBean
    private AuthTokenFilter authTokenFilter;


    private CreateRecipeRequest createRecipeRequest;
    private RecipeResponse recipeResponse;
    private RecipeListResponse recipeListResponse;
    private User user;

    @BeforeEach
    void setUp() {

        createRecipeRequest = CreateRecipeRequest.builder()
                .recipeName("Cold soup")
                .description("Vegan cold soup")
                .ingredients(List.of(3L, 4L))
                .build();


        recipeResponse = RecipeResponse.builder()
                .recipeId(1L)
                .recipeName("Cold soup")
                .description("Vegan cold soup")
                .rating(null)
                .build();


        recipeListResponse = RecipeListResponse.builder()
                .content(List.of(recipeResponse))
                .number(0)
                .size(10)
                .totalElements(1L)
                .totalPages(1)
                .first(true)
                .last(true)
                .build();

        user = User.builder()
                .id(1L)
                .username("username")
                .role(UserRole.ROLE_USER)
                .createdAt(Instant.now())
                .updatedAt(null)
                .build();
    }




    @Nested
    @DisplayName("Create Recipe Controller Tests")
    class CreateRecipeControllerTests {


        @Test
        @DisplayName("Should create recipe and return 200")
        void shouldCreateRecipeSuccessfully() throws Exception {

            Long categoryId = 2L;


            given(recipeService.createRecipe(
                    anyLong(),
                    eq(categoryId),
                    any(CreateRecipeRequest.class)
            )).willReturn(recipeResponse);


            mockMvc.perform(post("/api/recipes")
                            .param("categoryId", categoryId.toString())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(
                                    objectMapper.writeValueAsString(createRecipeRequest)
                            ))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.recipeId").value(1))
                    .andExpect(jsonPath("$.recipeName")
                            .value("Cold soup"));


            verify(recipeService)
                    .createRecipe(
                            anyLong(),
                            eq(categoryId),
                            any(CreateRecipeRequest.class)
                    );
        }



        @Test
        @DisplayName("Should return 404 when category not found")
        void shouldReturn404WhenCategoryNotFound() throws Exception {

            Long categoryId = 99L;


            given(recipeService.createRecipe(
                    anyLong(),
                    eq(categoryId),
                    any(CreateRecipeRequest.class)
            ))
                    .willThrow(
                            new ResourceNotFoundException(
                                    "Category",
                                    categoryId
                            )
                    );


            mockMvc.perform(post("/api/recipes")
                            .param("categoryId", categoryId.toString())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(
                                    objectMapper.writeValueAsString(createRecipeRequest)
                            ))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message")
                            .value(
                                    "Category with id: 99 was not found."
                            ));


            verify(recipeService)
                    .createRecipe(
                            anyLong(),
                            eq(categoryId),
                            any(CreateRecipeRequest.class)
                    );
        }
    }



    @Test
    @DisplayName("Should return recipes with status 200")
    void shouldReturnRecipeList() throws Exception {


        given(recipeService.getAllRecipes(
                isNull(),
                isNull(),
                isNull(),
                isNull(),
                any(Pageable.class)
        ))
                .willReturn(recipeListResponse);



        mockMvc.perform(get("/api/recipes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()").value(1))
                .andExpect(jsonPath("$.content[0].recipeName")
                        .value("Cold soup"));


        verify(recipeService)
                .getAllRecipes(
                        isNull(),
                        isNull(),
                        isNull(),
                        isNull(),
                        any(Pageable.class)
                );
    }



    @Test
    @DisplayName("Should delete recipe and return 204")
    void shouldDeleteRecipe() throws Exception {

        Long recipeId = 1L;

        doNothing()
                .when(recipeService)
                .deleteRecipe(anyLong(), eq(recipeId));



        mockMvc.perform(delete("/api/recipes/{id}", recipeId))
                .andExpect(status().isNoContent());


        verify(recipeService)
                .deleteRecipe(anyLong(), eq(recipeId));
    }
}