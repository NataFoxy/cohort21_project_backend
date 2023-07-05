package de.ait.gethelp.services.impl;

import de.ait.gethelp.dto.CardDto;
import de.ait.gethelp.dto.CardsPage;
import de.ait.gethelp.dto.NewCardDto;
import de.ait.gethelp.dto.ProfileDto;
import de.ait.gethelp.models.Card;
import de.ait.gethelp.models.Category;
import de.ait.gethelp.models.SubCategory;
import de.ait.gethelp.models.User;
import de.ait.gethelp.repositories.CardsRepository;
import de.ait.gethelp.repositories.CategoriesRepository;
import de.ait.gethelp.repositories.SubCategoriesRepository;
import de.ait.gethelp.repositories.UsersRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CardsServiceImplTest {

    @Mock
    private CardsRepository cardsRepository;
    @Mock
    private CategoriesRepository categoriesRepository;
    @Mock
    private SubCategoriesRepository subCategoriesRepository;
    @Mock
    private UsersRepository usersRepository;

    @InjectMocks
    private CardsServiceImpl cardsService;

    private User user1;
    private User user2;
    private CardsPage cardsPage;
    private ProfileDto profileDto;
    private Card card1;
    private Category category1;
    private SubCategory subCategory;
    private NewCardDto newCard;
    private NewCardDto editCard;
    @BeforeEach
    public void init(){

        user1 = User.builder()
                .id(1l)
                .createdAt(LocalDateTime.now())
                .username("xx")
                .hashPassword("qwerty")
                .email("xx@xx.xx")
                .phone("455")
                .role(User.Role.USER)
                .isHelper(true)
                .isBlocked(false)
                .cards(null)
                .build();
        user2 = User.builder()
                .id(2l)
                .createdAt(LocalDateTime.now())
                .username("xx")
                .hashPassword("qwerty")
                .email("xx@xx.xx")
                .phone("455")
                .role(User.Role.USER)
                .isHelper(true)
                .isBlocked(false)
                .cards(null)    // TODO: 29.06.2023 тоже не влаживается карточка
                .build();
        cardsPage=CardsPage.builder()
                .cards(new ArrayList<CardDto>())
                .build();
        profileDto = ProfileDto.builder()
                .id(1L)
                .username("xx")
                .email("xx@xx.xx")
                .phone("455")
                .role(String.valueOf(User.Role.USER))
                .isHelper(true)
                .cards(cardsPage)
                .build();
        subCategory = SubCategory.builder()
                .id(1l)
                .createdAt(LocalDateTime.now())
                .title("xx")
                .description("xx")
        //        .category(category1)
      //          .cards(List.of(card1))
                .build();
        List<SubCategory> subCategories = List.of(subCategory);
        category1 = Category.builder()
                .id(1l)
                .createdAt(LocalDateTime.now())
                .title("xx")
                .description("xx")
                .subCategory(subCategories)   // TODO: 29.06.2023 не даёт пройти
             //   .cards(null)
                .build();

        card1 = Card.builder()
                .id(1l)
                .createdAt(LocalDateTime.now())
                .user(user1)
                .category(category1)
                .subcategory(subCategory)
                .price(22.22)
                .description("xx")
                .isActive(true)
                .build();
        subCategory.setCategory(category1);
        List<Card> cards = List.of(card1);
        subCategory.setCards(cards);
        category1.setCards(cards);
        newCard = new NewCardDto(
                                "xx",
                                1l,
                                1l,
                                10.00,
                                "xx",
                                "xx1xx"
                );
        editCard = new NewCardDto(
                                "yy",
                                2l,
                                2l,
                                20.00,
                                "yy",
                                "zz1zz"
                );
    }
    @Test
    @DisplayName("cardsService getAll return CardsPage")
    public void cardsService_GetAll_ReturnCardsPage(){
         List<Card> cards = List.of(card1);
        when(cardsRepository.findAll()).thenReturn(cards);

        CardsPage expectedResult = cardsService.getAll();

        assertAll(
                ()->{
                    Assertions.assertEquals(expectedResult.getCards().get(0).getDescription(), newCard.getDescription());
                }
        );
    }
    @Test
    @DisplayName("cardsService getByIg return CardDTO")
    public void cardsService_GetById_ReturnCardDTO(){
        when(cardsRepository.findById(1l)).thenReturn(Optional.ofNullable(card1));

        CardDto expectedResult = cardsService.getById(1l);
        assertAll(
                ()->{
                    Assertions.assertEquals(expectedResult.getDescription(), card1.getDescription());
                }
        );
    }

    @Test
    @DisplayName("cardsServices addCard return CardDTO") // TODO: 03.07.2023 no works
    public void cardsServices_addCard_ReturnCardDTO(){
        when(cardsRepository.findById(1l)).thenReturn(Optional.ofNullable(card1));
        CardDto expectedResult = cardsService.addCard(user1.getId(), newCard);

        assertAll(
                ()->{
                    Assertions.assertEquals(expectedResult.getDescription(), newCard.getDescription());
                }
        );
    }

    @Test
    @DisplayName("cardsServices editCard return CardDTO") // TODO: 03.07.2023 no works
    public void cardsServices_editCard_ReturnCardDTO(){
        when(cardsRepository.findById(1l)).thenReturn(Optional.ofNullable(card1));

        CardDto expected = cardsService.editCard(user1.getId(), card1.getId(), editCard);

        assertAll(()->{
            Assertions.assertEquals(expected.getTitle(), editCard.getTitle());
        });

    }

    @Test
    @DisplayName("cardsServices deleteCard return CardDTO") // TODO: 03.07.2023 no works
    public void cardsServices_deleteCard_ReturnCardDTO(){
        when(cardsRepository.findById(1l)).thenReturn(Optional.ofNullable(card1));
        assertAll(()->cardsService.deleteCard(1l));
    }

}
