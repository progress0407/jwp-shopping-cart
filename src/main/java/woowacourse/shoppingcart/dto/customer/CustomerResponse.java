package woowacourse.shoppingcart.dto.customer;

public class CustomerResponse {

    private Long id;
    private String email;
    private String username;

    private CustomerResponse() {
    }

    public CustomerResponse(Long id, String email, String username) {
        this.id = id;
        this.email = email;
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }
}