package woowacourse.shoppingcart.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CartItem {

    private Long id;
    private Long productId;
    private String name;
    private int price;
    private String imageUrl;

    public CartItem(final Long id, final Product product) {
        this(id, product.getId(), product.getName(), product.getPrice(), product.getImageUrl());
    }
}