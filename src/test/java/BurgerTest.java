import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.lang.AutoCloseable;
import ru.praktikum.Bun;
import ru.praktikum.Burger;
import ru.praktikum.Ingredient;
import ru.praktikum.IngredientType;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;


@RunWith(Parameterized.class)
public class BurgerTest {
    private Burger burger;
    private AutoCloseable mockitoCloseable;

    @Mock
    private Bun bunMock;
    @Mock
    private Ingredient ingredient1;
    @Mock
    private Ingredient ingredient2;

    @Before
    public void setUp() {
        mockitoCloseable = MockitoAnnotations.openMocks(this);
        burger = new Burger();
        burger.setBuns(bunMock);
    }
    @After
    public void tearDown() throws Exception {
        mockitoCloseable.close();
    }
    @Parameterized.Parameters(name = "Bun={0}, Ing1={1}, Ing2={2} => Expected={3}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[] [] {
                {100f, 50f, 30f, 280f},
                {200f, 10f, 15f, 425f},
        });
}
private final float bunPrice;
    private final float ingr1Price;
    private final float ingr2Price;
    private final float expectedPrice;

   public BurgerTest(float bunPrice, float ingr1Price, float ingr2Price, float expectedPrice) {
       this.bunPrice = bunPrice;
       this.ingr1Price = ingr1Price;
       this.ingr2Price = ingr2Price;
       this.expectedPrice = expectedPrice;
   }
   @Test
    public void testGetPrice() {
       when(bunMock.getPrice()).thenReturn(bunPrice);
       when(ingredient1.getPrice()).thenReturn(ingr1Price);
       when(ingredient2.getPrice()).thenReturn(ingr2Price);

       burger.addIngredient(ingredient1);
       burger.addIngredient(ingredient2);

       assertEquals(expectedPrice, burger.getPrice(), 0.01f);
   }
   @Test
    public void testAddIngredient() {
       burger.addIngredient(ingredient1);
       assertEquals(1, burger.ingredients.size());
   }
   @Test
    public void testRemoveIngredient() {
       burger.addIngredient(ingredient1);
       burger.removeIngredient(0);
       assertTrue(burger.ingredients.isEmpty());
   }
   @Test
    public void testMoveIngredient() {
       burger.addIngredient(ingredient1);
       burger.addIngredient(ingredient2);
       burger.moveIngredient(0, 1);

       assertEquals(ingredient1, burger.ingredients.get(1));
   }
   @Test
   public void testGetReceipt() {
       when(bunMock.getName()).thenReturn("black bun");
       when(bunMock.getPrice()).thenReturn(100f);
       when(ingredient1.getType()).thenReturn(IngredientType.SAUCE);
       when(ingredient1.getName()).thenReturn("hot sauce");
       when(ingredient1.getPrice()).thenReturn(50f);

       burger.addIngredient(ingredient1);

       String expectedReceipt =
               "(==== black bun ====)\n" +
                       "= sauce hot sauce =\n" +
                       "(==== black bun ====)\n\n" +
                       "Price: 250.000000\n";
       assertEquals(expectedReceipt, burger.getReceipt());

   }
}


