package woowacourse.shoppingcart.exception.notfound;

public class InValidPassword extends NotFoundException {

    public InValidPassword() {
        super("비밀번호가 일치하지 않습니다.");
    }
}