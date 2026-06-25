package io.github.pauliustechin.egzaminas.feature.recipe.service;

import static org.junit.jupiter.api.Assertions.*;

import io.github.pauliustechin.egzaminas.exception.ResourceNotFoundException;
import io.github.pauliustechin.egzaminas.feature.category.model.Category;
import io.github.pauliustechin.egzaminas.feature.category.repository.CategoryRepository;
import io.github.pauliustechin.egzaminas.feature.recipe.dto.CreateRecipeRequest;
import io.github.pauliustechin.egzaminas.feature.recipe.dto.RecipeMapper;
import io.github.pauliustechin.egzaminas.feature.recipe.dto.RecipeResponse;
import io.github.pauliustechin.egzaminas.feature.recipe.model.Recipe;
import io.github.pauliustechin.egzaminas.feature.recipe.repository.RecipeRepository;
import io.github.pauliustechin.egzaminas.feature.user.dto.UserRole;
import io.github.pauliustechin.egzaminas.feature.user.model.User;
import io.github.pauliustechin.egzaminas.feature.user.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("RecipeServiceImpl Unit Tests")
class RecipeServiceImplTest {

    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private RecipeMapper recipeMapper;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private RecipeServiceImpl recipeServiceImpl;

    private CreateRecipeRequest createRecipeRequest;
    private RecipeResponse recipeResponse;
    private User user;
    private Category category;
    private Recipe recipe;

    @BeforeEach
    void setUp() {

        createRecipeRequest = CreateRecipeRequest.builder()
                .recipeName("Cold soup")
                .description("Vegan cold soup. Step 1: ...")
                .ingredients(List.of(3L, 4L))
                .build();

        user = User.builder()
                .id(1L)
                .username("username")
                .role(UserRole.ROLE_USER)
                .createdAt(Instant.now())
                .updatedAt(null)
                .build();

        category = Category.builder()
                .id(2L)
                .categoryName("Vegan")
                .description("Description")
                .createdAt(Instant.now())
                .build();

        recipe = Recipe.builder()
                .id(1L)
                .recipeName("Cold soup")
                .description("Vegan cold soup. Step 1: ...")
                .user(user)
                .category(category)
                .ratings(null)
                .build();

        recipeResponse = RecipeResponse.builder()
                .recipeId(1L)
                .recipeName("Cold soup")
                .description("Vegan cold soup. Step 1: ...")
                .rating(null)
                .build();

    }

        @Nested
        @DisplayName("Create Recipe Service Tests")
        class CreateRecipeTests {

            @Test
            @DisplayName("Should create recipe when valid request and client exists")
            void shouldCreateProjectSuccessfully() {

                Long clientId = 1L;
                Long categoryId = 2L;
                when(userRepository.findById(clientId)).thenReturn(Optional.of(user));
                when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
                when(recipeMapper.toEntity(createRecipeRequest))
                        .thenReturn(recipe);
                when(recipeRepository.save(any(Recipe.class))).thenReturn(recipe);
                when(recipeMapper.toResponse(recipe, null)).thenReturn(recipeResponse);

                RecipeResponse result = recipeServiceImpl.createRecipe(clientId, categoryId, createRecipeRequest);

                assertNotNull(result);
                assertEquals(recipeResponse, result);
                verify(userRepository, times(1)).findById(clientId);
                verify(recipeMapper, times(1)).toEntity(createRecipeRequest);
                verify(recipeRepository, times(1)).save(any(Recipe.class));
                verify(recipeMapper, times(1)).toResponse(recipe, null);
                verify(recipeRepository)
                        .save(argThat(re -> re != null && re.getRecipeName().equals("Cold soup")));
            }

            @Test
            @DisplayName("Should throw ResourceNotFoundException when user is not found.")
            void shouldThrowResourceNotFoundExceptionWhenUserNotFound() {
                Long clientId = 1L;
                when(userRepository.findById(clientId)).thenReturn(Optional.empty());

                Long categoryId = 2L;

                ResourceNotFoundException exception = assertThrows(
                        ResourceNotFoundException.class,
                        () -> recipeServiceImpl.createRecipe(clientId, categoryId, createRecipeRequest)
                );

                assertEquals("User with id: " + clientId + " was not found.", exception.getMessage());
                verify(userRepository, times(1)).findById(clientId);
                verify(recipeMapper, times(0)).toEntity(createRecipeRequest);
                verifyNoInteractions(recipeRepository);
            }

            @Test
            @DisplayName("Should throw ResourceNotFoundException when category is not found.")
            void shouldThrowResourceNotFoundExceptionWhenCategoryNotFound() {

                Long clientId = 1L;
                when(userRepository.findById(clientId)).thenReturn(Optional.of(user));


                Long categoryId = 2L;
                when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());


                ResourceNotFoundException exception = assertThrows(
                        ResourceNotFoundException.class,
                        () -> recipeServiceImpl.createRecipe(clientId, categoryId, createRecipeRequest)
                );

                assertEquals("Category with id: " + categoryId + " was not found.", exception.getMessage());

                verify(userRepository).findById(clientId);
                verify(categoryRepository).findById(categoryId);
                verify(recipeMapper, never()).toEntity(createRecipeRequest);
                verifyNoInteractions(recipeRepository);
            }

            @Test
            @DisplayName("Should handle null userId in a request.")
            void shouldHandleNullUserIdInRequest() {

                ResourceNotFoundException exception = assertThrows(
                        ResourceNotFoundException.class,
                        () -> recipeServiceImpl.createRecipe(null, 2L, createRecipeRequest)
                );

                assertEquals("User with id: null was not found.", exception.getMessage());
                verifyNoInteractions(userRepository);
                verifyNoInteractions(recipeMapper);
                verifyNoInteractions(recipeRepository);
            }
        }
}
