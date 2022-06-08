package woowacourse.shoppingcart.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.jdbc.Sql;
import woowacourse.shoppingcart.domain.Customer;
import woowacourse.shoppingcart.domain.Product;
import woowacourse.shoppingcart.dto.cartitem.CartResponse;

@JdbcTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Sql(scripts = {"classpath:schema.sql", "classpath:data.sql"})
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class CartQueryDaoTest {

    private final CartQueryDao cartQueryDao;

    private final CustomerDao customerDao;
    private final ProductDao productDao;
    private final CartItemDao cartItemDao;

    private long 저장된_회원_ID;
    private long 저장된_상품_ID;

    public CartQueryDaoTest(DataSource dataSource,
                            NamedParameterJdbcTemplate namedParameterJdbcTemplate,
                            JdbcTemplate jdbcTemplate) {
        this.cartQueryDao = new CartQueryDao(jdbcTemplate);
        this.customerDao = new CustomerDao(dataSource, namedParameterJdbcTemplate);
        this.productDao = new ProductDao(dataSource, namedParameterJdbcTemplate);
        this.cartItemDao = new CartItemDao(dataSource, namedParameterJdbcTemplate);
    }

    @BeforeEach
    void setUp() {
        this.저장된_회원_ID = customerDao.save(new Customer("philz@gmail.com", "swcho", "1q2w3e4r!"));
        this.저장된_상품_ID = productDao.save(new Product("사과", 3_000, "foo.com/app.png", 8_000));
        cartItemDao.addCartItem(저장된_회원_ID, 저장된_상품_ID, 40L);
    }

    @DisplayName("asd")
    @Test
    void qwe() {
        List<CartResponse> allCartByCustomerId = cartQueryDao.findAllCartByCustomerId(저장된_회원_ID);
        CartResponse cartResponse = allCartByCustomerId.get(0);
        CartResponse expected =
                new CartResponse(저장된_상품_ID, "foo.com/app.png", "사과", 3_000, 8_000L, 40L);

        assertThat(allCartByCustomerId.size()).isEqualTo(1);
        assertThat(cartResponse).isEqualTo(expected);

    }
}
