package woowacourse.fixture;

import static org.assertj.core.api.Assertions.assertThat;
import static woowacourse.fixture.Fixture.delete;
import static woowacourse.fixture.Fixture.get;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import woowacourse.shoppingcart.domain.CartItem;

public class CartFixture {

    private CartFixture() {
    }

    public static ExtractableResponse<Response> 장바구니_아이템_추가_요청(String token, long customerId, long productId, long count) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("productId", productId);
        requestBody.put("count", count);

        return Fixture.post("/api/customers/" + customerId + "/carts", token, requestBody);
    }

    public static ExtractableResponse<Response> 장바구니_아이템_목록_조회_요청(String token, long customerId) {
        return get("/api/customers/" + customerId + "/carts", token);
    }

    public static ExtractableResponse<Response> 장바구니_삭제_요청(String token, long customerId, long cartId) {
        return delete("/api/customers/" + customerId + "/carts/" + cartId, token);
    }

    public static Long 장바구니_아이템_추가_요청후_ID_반환(String token, long customerId, long productId, long count) {
        ExtractableResponse<Response> response = 장바구니_아이템_추가_요청(token, customerId, productId, count);
        return Long.parseLong(response.header("Location").split("/carts/")[1]);
    }

    public static void 장바구니_아이템_추가_검증(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    public static void 장바구니_아이템_목록_응답_검증(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 장바구니_아이템_목록_포함_검증(ExtractableResponse<Response> response, Long... productIds) {
        List<Long> resultProductIds = response.jsonPath().getList(".", CartItem.class).stream()
                .map(CartItem::getProductId)
                .collect(Collectors.toList());
        assertThat(resultProductIds).contains(productIds);
    }

    public static void 장바구니_삭제_검증(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
