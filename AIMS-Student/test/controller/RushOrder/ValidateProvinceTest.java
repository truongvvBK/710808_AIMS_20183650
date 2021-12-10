package controller.RushOrder;

import controller.PlaceOrderController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ValidateProvinceTest {
    private PlaceOrderController placeOrderController;

    @BeforeEach
    void setUp() throws Exception {
        placeOrderController = new PlaceOrderController();
    }

    @ParameterizedTest
    @CsvSource({
            "Hà Nội,true",
            "Hồ Chí Minh,true",
            "Đà Nẵng,true",
            "Thanh Hóa,false",
            "Thái Nguyên,false",
    })

    void test(String province, boolean expected) {
        boolean isValid = placeOrderController.validateProvince(province);
        assertEquals(expected, isValid);
    }
}
