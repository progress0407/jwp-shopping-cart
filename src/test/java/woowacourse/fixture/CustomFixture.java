package woowacourse.fixture;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import woowacourse.auth.dto.TokenRequest;
import woowacourse.auth.dto.TokenResponse;
import woowacourse.shoppingcart.dto.customer.CustomerCreateRequest;
import woowacourse.shoppingcart.dto.customer.CustomerUpdateRequest;

public class CustomFixture extends Fixture {

    public static String 로그인_요청_및_토큰발급(TokenRequest request) {
        String path = "/api/auth/login";
        ExtractableResponse<Response> loginResponse = post(path, request);
        TokenResponse tokenResponse = loginResponse.body().as(TokenResponse.class);

        return tokenResponse.getAccessToken();
    }

    public static ExtractableResponse<Response> 회원가입_요청(CustomerCreateRequest request) {
        String path = "/api/customers";
        return post(path, request);
    }

    public static long 회원가입_요청_및_ID_추출(CustomerCreateRequest request) {
        long savedId = ID_추출(회원가입_요청(request));
        return savedId;
    }

    public static ExtractableResponse<Response> 회원조회_요청(String token, Long id) {
        String path = "/api/customers/" + id;
        return get(path, token);
    }

    public static ExtractableResponse<Response> 회원정보수정_요청(String token, long id, CustomerUpdateRequest request) {
        String path = "/api/customers/" + id;
        return put(path, token, request);
    }


    public static ExtractableResponse<Response> 회원탈퇴_요청(String token, long id) {
        String path = "/api/customers/" + id;
        return delete(path, token);
    }


    public static long ID_추출(ExtractableResponse<Response> response) {
        String[] locations = response.header("Location").split("/");
        return Long.parseLong(locations[locations.length - 1]);
    }
}
