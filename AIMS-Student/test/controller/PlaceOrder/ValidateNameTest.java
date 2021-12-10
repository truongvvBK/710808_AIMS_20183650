package controller.PlaceOrder;

import controller.PlaceOrderController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ValidateNameTest {
    private PlaceOrderController placeOrderController;
    @BeforeEach
    void setUp() {
        placeOrderController = new PlaceOrderController();
    }
    @ParameterizedTest
    @CsvSource({
            "Truong, true",
            "Vu Van Truong, true",
            ",false",
            "Vu 2Van Truong, false",
            "Truong@ back khoa, false",
            "Truong123, false",
            "D@, false",
            "Dang1 .-/l;, false"

    })
    void validateName(String name, boolean expected){
        //when
        boolean isValid = placeOrderController.validateName(name);
        //then
        assertEquals(expected, isValid);
    }
}
